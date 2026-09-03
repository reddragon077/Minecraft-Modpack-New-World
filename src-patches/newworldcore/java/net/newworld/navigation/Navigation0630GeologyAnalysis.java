package net.newworld.navigation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.WeakHashMap;

import net.newworld.config.NewWorldTuning;

/** Accuracy/field-quality analysis progression without changing the geology packet or save schema. */
public final class Navigation0630GeologyAnalysis {
    private static final ThreadLocal<Integer> SERVER_RADAR_ANALYSIS = new ThreadLocal<>();
    private static final ThreadLocal<Integer> SERVER_RADAR_ACCURACY = new ThreadLocal<>();
    private static final ThreadLocal<Integer> PENDING_SCAN_ANALYSIS = new ThreadLocal<>();
    private static final Map<Object, Integer> CLIENT_RESULT_ANALYSIS =
            java.util.Collections.synchronizedMap(new WeakHashMap<>());

    private Navigation0630GeologyAnalysis() {}

    /** Wrapper around the original timed-scan completion path. */
    public static void runImmediate(Object job) throws Exception {
        int accuracy = intCall(job, "accuracyLevel");
        int analysis = NewWorldTuning.geologyRadarAnalysisLevel(accuracy);
        SERVER_RADAR_ANALYSIS.set(analysis);
        SERVER_RADAR_ACCURACY.set(accuracy);
        try {
            Class<?> owner = Class.forName("net.newworld.navigation.Navigation0540GeologyTimedScan");
            Method base = null;
            for (Method method : owner.getDeclaredMethods()) {
                if ("runImmediate0630Base".equals(method.getName()) && method.getParameterCount() == 1) {
                    base = method;
                    break;
                }
            }
            if (base == null) throw new NoSuchMethodException("Navigation0540GeologyTimedScan.runImmediate0630Base");
            base.setAccessible(true);
            base.invoke(null, job);
        } finally {
            SERVER_RADAR_ANALYSIS.remove();
            SERVER_RADAR_ACCURACY.remove();
        }
    }

    /** Read by the discovery merge while Radar records are being written on the same server thread. */
    public static int currentRadarAnalysisLevel() {
        Integer level = SERVER_RADAR_ANALYSIS.get();
        return level == null ? NewWorldTuning.geologyRadarAnalysisLevel(0) : clamp(level);
    }

    /** Chooses the per-family tier reveal while a server Radar scan records this result. */
    public static int currentRadarAnalysisLevel(Object discovery) {
        Integer accuracy = SERVER_RADAR_ACCURACY.get();
        if (accuracy == null || discovery == null) return currentRadarAnalysisLevel();
        try {
            return NewWorldTuning.geologyRadarAnalysisFor(String.valueOf(field(discovery, "depositId")), accuracy);
        } catch (Throwable ignored) {
            return currentRadarAnalysisLevel();
        }
    }

    public static int clientAnalysisLevel() {
        try {
            return NewWorldTuning.geologyRadarAnalysisLevel(Navigation0540GeologyClientProgress.accuracyLevel());
        } catch (Throwable ignored) {
            return NewWorldTuning.geologyRadarAnalysisLevel(0);
        }
    }

    /** Wraps the scan's original record method and captures the merged per-coordinate level. */
    public static void captureScanRecord(Object data, String shipId, String dimension, Object hit) throws Exception {
        invokeDeclaredStatic("net.newworld.navigation.Navigation0520GeologyScanRuntime",
                "record0631Base", data, shipId, dimension, hit);
        int fallback = currentRadarAnalysisLevel();
        try {
            int x = intCall(hit, "x");
            int y = intCall(hit, "y");
            int z = intCall(hit, "z");
            Object state = call(data, "state", shipId);
            Object raw = field(state, "discoveries");
            if (raw instanceof Map<?, ?> discoveries) {
                for (Object discovery : discoveries.values()) {
                    if (discovery == null) continue;
                    if (intField(discovery, "x") != x || intField(discovery, "y") != y
                            || intField(discovery, "z") != z) continue;
                    fallback = Navigation0610DiscoveryRuntime.analysisLevel(discovery);
                    break;
                }
            }
        } catch (Throwable failure) {
            System.err.println("[NewWorldCore] Could not resolve merged geology analysis level: " + failure);
        }
        PENDING_SCAN_ANALYSIS.set(clamp(fallback));
    }

    /** Appends one reserved analysis packet immediately after each legacy distance/result packet. */
    public static void sendScanPacket(Object player, int code) throws Exception {
        invokeDeclaredStatic("net.newworld.navigation.Navigation0520GeologyScanRuntime",
                "send0631Base", player, code);
        if (code >= 1_270_000_000 && code < 1_270_010_000) {
            Integer analysis = PENDING_SCAN_ANALYSIS.get();
            PENDING_SCAN_ANALYSIS.remove();
            if (analysis != null) {
                invokeDeclaredStatic("net.newworld.navigation.Navigation0520GeologyScanRuntime",
                        "send0631Base", player, 1_280_000_000 + clamp(analysis));
            }
        }
    }

    /** Wraps the legacy decoder and attaches a level to the just-created immutable Result record. */
    public static synchronized void acceptLegacy(int code) throws Exception {
        invokeDeclaredStatic("net.newworld.navigation.Navigation0520GeologyClientState",
                "acceptLegacy0631Base", code);
        if (code >= 1_000_000_000 && code < 1_010_000_000) CLIENT_RESULT_ANALYSIS.clear();
        if (code >= 1_280_000_000 && code < 1_280_000_004) {
            List<?> results = Navigation0520GeologyClientState.results();
            if (!results.isEmpty()) CLIENT_RESULT_ANALYSIS.put(results.get(results.size() - 1), code - 1_280_000_000);
        }
    }

    /** Replaces persistent-snapshot serialization so saved per-record levels survive reopening the GUI. */
    public static void sendSnapshot(Object player, List<?> records) {
        ArrayList<Object> valid = new ArrayList<>();
        for (Object record : records) if (definitionIndex(record) >= 0) valid.add(record);
        sendPacket(player, 1_000_000_000 + valid.size());
        for (Object record : valid) {
            int type = definitionIndex(record);
            sendPacket(player, 1_010_000_000 + type);
            sendPacket(player, 1_100_000_000 + clampCoord(intField(record, "x")) + 30_000_000);
            sendPacket(player, 1_170_000_000 + Math.max(-2048, Math.min(2047, intField(record, "y"))) + 2048);
            sendPacket(player, 1_180_000_000 + clampCoord(intField(record, "z")) + 30_000_000);
            sendPacket(player, 1_250_000_000 + Math.max(0, Math.min(9999, intField(record, "radius"))));
            sendPacket(player, 1_260_000_000 + Math.max(0, Math.min(9_999_999, intField(record, "reserve"))));
            sendPacket(player, 1_270_000_000 + Math.max(0, Math.min(9999, intField(record, "distance"))));
            sendPacket(player, 1_280_000_000 + Navigation0610DiscoveryRuntime.analysisLevel(record));
        }
        sendPacket(player, 1_299_999_999);
    }

    public static String clientLabel(Object result) {
        Navigation0520GeologyDefinitions.Def definition = definition(result);
        String exact = definition == null ? "UNKNOWN GEOLOGICAL ANOMALY" : definition.label();
        return NewWorldTuning.geologyAnalysisLabel(resultAnalysisLevel(result), exact);
    }

    public static String clientPrimary(Object result) {
        Navigation0520GeologyDefinitions.Def definition = definition(result);
        String exact = definition == null ? "unknown" : definition.primary();
        return NewWorldTuning.geologyAnalysisPrimary(resultAnalysisLevel(result), exact);
    }

    public static String clientLabelForType(int type) {
        Navigation0520GeologyDefinitions.Def definition = Navigation0520GeologyDefinitions.byIndex(type);
        String exact = definition == null ? "TYPE " + type : definition.label();
        return exact;
    }

    /** Exact-family filters are intentionally unavailable until analysis III. */
    public static List<Integer> clientAvailableTypes() {
        TreeSet<Integer> types = new TreeSet<>(Comparator.comparing(
                Navigation0630GeologyAnalysis::clientLabelForType, String.CASE_INSENSITIVE_ORDER));
        for (Object result : Navigation0520GeologyClientState.results()) {
            if (resultAnalysisLevel(result) >= 3) types.add(type(result));
        }
        return new ArrayList<>(types);
    }

    public static List<?> clientFiltered(Object screen, List<?> results) {
        if (results == null) return List.of();
        LinkedHashSet<Integer> selected = selectedTypes(screen);
        if (selected == null || selected.isEmpty()) return new ArrayList<>(results);
        ArrayList<Object> filtered = new ArrayList<>();
        for (Object result : results) {
            if (resultAnalysisLevel(result) >= 3 && selected.contains(type(result))) filtered.add(result);
        }
        return filtered;
    }

    public static String clientFilterSummary(Object screen) {
        LinkedHashSet<Integer> selected = selectedTypes(screen);
        List<Integer> available = clientAvailableTypes();
        if (available.isEmpty()) return "LOCKED // NO EXACT FAMILIES";
        if (selected == null || selected.isEmpty()) return "ALL";
        if (selected.size() == 1) return fit(clientLabelForType(selected.iterator().next()), 18);
        return selected.size() + " TYPES";
    }

    public static String lockedFilterMessage() {
        return clientAvailableTypes().isEmpty()
                ? "EXACT FAMILY FILTERS REQUIRE ANALYSIS III"
                : "NO DEPOSIT FAMILIES IN CURRENT SCAN";
    }

    public static int resultAnalysisLevel(Object result) {
        Integer level = CLIENT_RESULT_ANALYSIS.get(result);
        return level == null ? clientAnalysisLevel() : clamp(level);
    }

    private static Navigation0520GeologyDefinitions.Def definition(Object result) {
        try { return Navigation0520GeologyDefinitions.byIndex(type(result)); }
        catch (Throwable ignored) { return null; }
    }

    private static int type(Object result) {
        try { return intCall(result, "type"); }
        catch (Throwable ignored) { return -1; }
    }

    @SuppressWarnings("unchecked")
    private static LinkedHashSet<Integer> selectedTypes(Object screen) {
        try {
            Map<Object, LinkedHashSet<Integer>> filters = (Map<Object, LinkedHashSet<Integer>>) filterMap();
            return filters.get(screen);
        } catch (Throwable ignored) {
            return null;
        }
    }

    private static Object filterMap() throws Exception {
        Field field = Class.forName("net.newworld.navigation.Navigation0558UnifiedRadarUi")
                .getDeclaredField("FILTER_TYPES");
        field.setAccessible(true);
        return field.get(null);
    }

    private static int intCall(Object target, String name) throws Exception {
        Method method = target.getClass().getDeclaredMethod(name);
        method.setAccessible(true);
        return ((Number) method.invoke(target)).intValue();
    }

    private static Object call(Object target, String name, Object... args) throws Exception {
        Method method = findMethod(target.getClass(), name, false, args);
        method.setAccessible(true);
        return method.invoke(target, args);
    }

    private static Object invokeDeclaredStatic(String owner, String name, Object... args) throws Exception {
        Method method = findMethod(Class.forName(owner), name, true, args);
        method.setAccessible(true);
        return method.invoke(null, args);
    }

    private static Method findMethod(Class<?> owner, String name, boolean requireStatic, Object[] args)
            throws NoSuchMethodException {
        for (Method method : owner.getDeclaredMethods()) {
            if (!method.getName().equals(name)
                    || java.lang.reflect.Modifier.isStatic(method.getModifiers()) != requireStatic
                    || method.getParameterCount() != args.length) continue;
            return method;
        }
        throw new NoSuchMethodException(owner.getName() + '.' + name);
    }

    private static Object field(Object target, String name) throws Exception {
        Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return field.get(target);
    }

    private static int intField(Object target, String name) {
        try {
            Field field = target.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.getInt(target);
        } catch (Throwable ignored) {
            return 0;
        }
    }

    private static int definitionIndex(Object record) {
        try {
            String id = String.valueOf(field(record, "depositId"));
            String label = String.valueOf(field(record, "label"));
            for (Navigation0520GeologyDefinitions.Def definition : Navigation0520GeologyDefinitions.all()) {
                if (id != null && !id.isBlank() && id.equalsIgnoreCase(definition.id())) return definition.index();
                if (label != null && !label.isBlank() && label.equalsIgnoreCase(definition.label())) return definition.index();
            }
        } catch (Throwable ignored) {}
        return -1;
    }

    private static void sendPacket(Object player, int code) {
        try {
            Class<?> bridge = Class.forName("net.newworld.player.PlayerFieldSurvey0504Bridge");
            Method method = findMethod(bridge, "sendResult", true, new Object[]{player, code});
            method.setAccessible(true);
            method.invoke(null, player, code);
        } catch (Throwable failure) {
            throw new IllegalStateException("Could not send geology analysis packet", failure);
        }
    }

    private static int clampCoord(int value) { return Math.max(-30_000_000, Math.min(30_000_000, value)); }

    private static int clamp(int level) { return Math.max(0, Math.min(3, level)); }

    private static String roman(int level) {
        return switch (clamp(level)) {
            case 1 -> "I/III";
            case 2 -> "II/III";
            case 3 -> "III/III";
            default -> "0/III";
        };
    }

    private static String fit(String value, int max) {
        if (value == null) return "";
        return value.length() <= max ? value : value.substring(0, Math.max(1, max - 2)) + "..";
    }
}
