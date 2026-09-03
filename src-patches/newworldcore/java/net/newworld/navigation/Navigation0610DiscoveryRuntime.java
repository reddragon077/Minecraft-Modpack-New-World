package net.newworld.navigation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

import net.newworld.config.NewWorldTuning;
import net.newworld.navigation.NavigationDiscoveryEventBus.Change;
import net.newworld.navigation.NavigationDiscoveryEventBus.Event;

/** Schema-v3 metadata, repeat-observation merge, and common event bridge. */
public final class Navigation0610DiscoveryRuntime {
    public static final int SCHEMA_VERSION = 3;
    private static final String ANALYSIS_SUFFIX = "AnalysisLevel";
    private static final String LAST_SEEN_SUFFIX = "LastSeenAt";

    private Navigation0610DiscoveryRuntime() {}

    /** Wraps NavigationDiscoverySavedData.record while retaining its proven persistence/map behavior. */
    public static void record(Object data, String shipId, Object incoming) {
        if (data == null || incoming == null) return;
        boolean baseCalled = false;
        try {
            Navigation0510DiscoveryMeta.ensureMeta(incoming);
            Object state = call(data, "state", shipId);
            Map<Object, Object> discoveries = mapField(state, "discoveries");
            String key = String.valueOf(call(incoming, "key"));
            Object existing = discoveries.get(key);

            int previousAnalysis = existing == null ? -1 : analysisLevel(existing);
            long observedAt = positive(longField(incoming, "discoveredAt"));
            long firstAt = longField(incoming, "discoveredAt");
            long previousLastSeen = 0L;

            String kind = Navigation0510DiscoveryMeta.kind(incoming);
            String incomingSource = Navigation0510DiscoveryMeta.source(incoming);
            String effectiveSource = incomingSource;
            int configuredAnalysis = NewWorldTuning.discoveryAnalysisLevel(kind, incomingSource);
            int requestedAnalysis = Math.max(analysisLevel(incoming), configuredAnalysis);
            if ("GEOLOGY".equalsIgnoreCase(kind) && "RADAR".equalsIgnoreCase(incomingSource)) {
                requestedAnalysis = Math.max(requestedAnalysis,
                        Navigation0630GeologyAnalysis.currentRadarAnalysisLevel(incoming));
            }

            if (existing != null) {
                long existingFirst = longField(existing, "discoveredAt");
                if (existingFirst != 0L) firstAt = existingFirst;
                previousLastSeen = lastSeenAt(existing);
                if (previousLastSeen <= 0L) previousLastSeen = positive(existingFirst);

                // FIELD is stronger evidence than RADAR and must not be downgraded by a later scan.
                String existingSource = Navigation0510DiscoveryMeta.source(existing);
                if (Navigation0510DiscoveryMeta.SOURCE_FIELD.equals(existingSource)) {
                    effectiveSource = Navigation0510DiscoveryMeta.SOURCE_FIELD;
                }

                boolean favorite = booleanField(existing, "favorite") || booleanField(incoming, "favorite");
                boolean visited = booleanField(existing, "visited") || booleanField(incoming, "visited");
                setField(existing, "favorite", favorite);
                setField(existing, "visited", visited);
                setField(incoming, "favorite", favorite);
                setField(incoming, "visited", visited);
            }

            int mergedAnalysis = clampAnalysis(Math.max(previousAnalysis, requestedAnalysis));
            long mergedLastSeen = Math.max(previousLastSeen, observedAt);
            setField(incoming, "source", effectiveSource);
            setField(incoming, "discoveredAt", firstAt);
            setField(incoming, "analysisLevel", mergedAnalysis);
            setField(incoming, "lastSeenAt", mergedLastSeen);

            invokeRecordBase(data, shipId, incoming);
            baseCalled = true;

            Change change = existing == null
                    ? Change.DISCOVERED
                    : (mergedAnalysis > previousAnalysis ? Change.ANALYSIS_UPGRADED : Change.SEEN);
            NavigationDiscoveryEventBus.emit(new Event(
                    shipId, incoming, key, kind, effectiveSource, change,
                    Math.max(0, previousAnalysis), mergedAnalysis, firstAt, mergedLastSeen));
        } catch (Throwable failure) {
            System.err.println("[NewWorldCore] Discovery metadata/event bridge failed: " + failure);
            if (!baseCalled) {
                try {
                    invokeRecordBase(data, shipId, incoming);
                } catch (Throwable baseFailure) {
                    throw new IllegalStateException("Discovery record and compatibility fallback both failed", baseFailure);
                }
            }
        }
    }

    /** Called after the schema-v2 loader; missing fields are migrated without touching old keys. */
    public static void afterLoad(Object tag, String prefix, Object discovery) {
        if (tag == null || discovery == null) return;
        try {
            int storedSchema = intCall(tag, "getInt", "NWDiscoverySchema");
            int analysis = storedSchema >= SCHEMA_VERSION
                    ? intCall(tag, "getInt", prefix + ANALYSIS_SUFFIX)
                    : NewWorldTuning.discoveryAnalysisLevel(
                            Navigation0510DiscoveryMeta.kind(discovery),
                            Navigation0510DiscoveryMeta.source(discovery));
            long lastSeen = storedSchema >= SCHEMA_VERSION
                    ? longCall(tag, "getLong", prefix + LAST_SEEN_SUFFIX)
                    : positive(longField(discovery, "discoveredAt"));
            setField(discovery, "analysisLevel", clampAnalysis(analysis));
            setField(discovery, "lastSeenAt", Math.max(0L, lastSeen));
        } catch (Throwable failure) {
            System.err.println("[NewWorldCore] Discovery schema-v3 load migration failed: " + failure);
        }
    }

    /** Called after the existing metadata saver so older kind/geology keys remain unchanged. */
    public static void afterSave(Object tag, String prefix, Object discovery) {
        if (tag == null || discovery == null) return;
        try {
            int configured = NewWorldTuning.discoveryAnalysisLevel(
                    Navigation0510DiscoveryMeta.kind(discovery),
                    Navigation0510DiscoveryMeta.source(discovery));
            int analysis = clampAnalysis(Math.max(analysisLevel(discovery), configured));
            long lastSeen = lastSeenAt(discovery);
            if (lastSeen <= 0L) lastSeen = positive(longField(discovery, "discoveredAt"));
            setField(discovery, "analysisLevel", analysis);
            setField(discovery, "lastSeenAt", lastSeen);
            call(tag, "putInt", prefix + ANALYSIS_SUFFIX, analysis);
            call(tag, "putLong", prefix + LAST_SEEN_SUFFIX, lastSeen);
        } catch (Throwable failure) {
            System.err.println("[NewWorldCore] Discovery schema-v3 save failed: " + failure);
        }
    }

    /** Advances the global schema marker after the previous metadata schema writer runs. */
    public static void afterSaveSchema(Object tag) {
        if (tag == null) return;
        try {
            call(tag, "putInt", "NWDiscoverySchema", SCHEMA_VERSION);
        } catch (Throwable failure) {
            System.err.println("[NewWorldCore] Discovery schema marker update failed: " + failure);
        }
    }

    /** Public bridge for future Research/Field systems that explicitly raise analysis. */
    public static boolean upgradeAnalysis(Object data, String shipId, String key, int requestedLevel, long seenAt, String source) {
        if (data == null || key == null || key.isBlank()) return false;
        try {
            Object state = call(data, "state", shipId);
            Object discovery = mapField(state, "discoveries").get(key);
            if (discovery == null) return false;
            int previous = analysisLevel(discovery);
            int next = clampAnalysis(Math.max(previous, requestedLevel));
            long lastSeen = Math.max(lastSeenAt(discovery), positive(seenAt));
            String currentSource = Navigation0510DiscoveryMeta.source(discovery);
            String effectiveSource = Navigation0510DiscoveryMeta.SOURCE_FIELD.equalsIgnoreCase(source)
                    ? Navigation0510DiscoveryMeta.SOURCE_FIELD : currentSource;
            setField(discovery, "analysisLevel", next);
            setField(discovery, "lastSeenAt", lastSeen);
            setField(discovery, "source", effectiveSource);
            call(data, "setDirty");
            NavigationDiscoveryEventBus.emit(new Event(
                    shipId, discovery, key, Navigation0510DiscoveryMeta.kind(discovery), effectiveSource,
                    next > previous ? Change.ANALYSIS_UPGRADED : Change.SEEN,
                    previous, next, longField(discovery, "discoveredAt"), lastSeen));
            return next > previous;
        } catch (Throwable failure) {
            System.err.println("[NewWorldCore] Discovery analysis upgrade failed: " + failure);
            return false;
        }
    }

    public static int analysisLevel(Object discovery) {
        try { return clampAnalysis(intField(discovery, "analysisLevel")); }
        catch (Throwable ignored) { return 0; }
    }

    public static long lastSeenAt(Object discovery) {
        try { return Math.max(0L, longField(discovery, "lastSeenAt")); }
        catch (Throwable ignored) { return 0L; }
    }

    private static void invokeRecordBase(Object data, String shipId, Object discovery) throws Exception {
        Method method = findMethod(data.getClass(), "record0610Base", false, new Object[]{shipId, discovery});
        method.setAccessible(true);
        method.invoke(data, shipId, discovery);
    }

    @SuppressWarnings("unchecked")
    private static Map<Object, Object> mapField(Object target, String name) throws Exception {
        Object value = findField(target.getClass(), name).get(target);
        if (!(value instanceof Map<?, ?> map)) throw new IllegalStateException(name + " is not a Map");
        return (Map<Object, Object>) map;
    }

    private static Object call(Object target, String method, Object... args) throws Exception {
        Method found = findMethod(target.getClass(), method, false, args);
        found.setAccessible(true);
        return found.invoke(target, args);
    }

    private static Method findMethod(Class<?> type, String name, boolean requireStatic, Object[] args) throws NoSuchMethodException {
        for (Class<?> current = type; current != null; current = current.getSuperclass()) {
            for (Method method : current.getDeclaredMethods()) {
                if (!method.getName().equals(name) || Modifier.isStatic(method.getModifiers()) != requireStatic) continue;
                Class<?>[] parameters = method.getParameterTypes();
                if (parameters.length != args.length) continue;
                boolean matches = true;
                for (int i = 0; i < parameters.length; i++) {
                    if (!compatible(parameters[i], args[i])) { matches = false; break; }
                }
                if (matches) return method;
            }
        }
        throw new NoSuchMethodException(type.getName() + '.' + name);
    }

    private static boolean compatible(Class<?> parameter, Object value) {
        if (value == null) return !parameter.isPrimitive();
        if (parameter.isPrimitive()) parameter = wrap(parameter);
        return parameter.isInstance(value);
    }

    private static Class<?> wrap(Class<?> type) {
        if (type == int.class) return Integer.class;
        if (type == long.class) return Long.class;
        if (type == boolean.class) return Boolean.class;
        if (type == double.class) return Double.class;
        if (type == float.class) return Float.class;
        if (type == short.class) return Short.class;
        if (type == byte.class) return Byte.class;
        if (type == char.class) return Character.class;
        return type;
    }

    private static Field findField(Class<?> type, String name) throws NoSuchFieldException {
        for (Class<?> current = type; current != null; current = current.getSuperclass()) {
            try {
                Field field = current.getDeclaredField(name);
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException ignored) {}
        }
        throw new NoSuchFieldException(name);
    }

    private static void setField(Object target, String name, Object value) throws Exception {
        findField(target.getClass(), name).set(target, value);
    }

    private static int intField(Object target, String name) throws Exception {
        Object value = findField(target.getClass(), name).get(target);
        return value instanceof Number number ? number.intValue() : 0;
    }

    private static long longField(Object target, String name) throws Exception {
        Object value = findField(target.getClass(), name).get(target);
        return value instanceof Number number ? number.longValue() : 0L;
    }

    private static boolean booleanField(Object target, String name) throws Exception {
        Object value = findField(target.getClass(), name).get(target);
        return value instanceof Boolean flag && flag;
    }

    private static int intCall(Object target, String method, String key) throws Exception {
        Object value = call(target, method, key);
        return value instanceof Number number ? number.intValue() : 0;
    }

    private static long longCall(Object target, String method, String key) throws Exception {
        Object value = call(target, method, key);
        return value instanceof Number number ? number.longValue() : 0L;
    }

    private static int clampAnalysis(int level) {
        return Math.max(0, Math.min(3, level));
    }

    private static long positive(long value) {
        return value > 0L ? value : 0L;
    }
}
