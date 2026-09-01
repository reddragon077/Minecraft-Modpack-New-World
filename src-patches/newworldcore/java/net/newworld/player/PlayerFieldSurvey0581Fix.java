package net.newworld.player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import net.newworld.navigation.Navigation0581DynamicStructureScanner;

/** Keeps the STRUCTURE survey isolated from geology discoveries. */
public final class PlayerFieldSurvey0581Fix {
    private static final int RANGE = 96;
    private static final int CHUNK_RADIUS = 6;

    private PlayerFieldSurvey0581Fix() {}

    public static void scanStructures(Object player) {
        if (player == null) return;
        try {
            Object level = call(player, "serverLevel");
            Object playerPos = call(player, "blockPosition");
            if (level == null || playerPos == null) {
                message(player, "FIELD SURVEY // No active world link.");
                sendResult(player, 100);
                return;
            }

            Object ship = invokePrivateStatic("net.newworld.player.PlayerFieldSurveyRuntime", "findOwnedShip",
                    player, level, playerPos);
            if (ship == null) {
                message(player, "SHIP LINK LOST // No owned TARDIS could be resolved.");
                sendResult(player, 101);
                return;
            }
            String shipId = String.valueOf(call(ship, "id"));
            purgeInvalidStructureDiscoveries(level, shipId);
            Map<String, FoundStructure> found = findLoadedStructures(level, playerPos);
            if (found.isEmpty()) {
                message(player, "FIELD SURVEY // No structures detected within 96 blocks.");
                sendResult(player, 102);
                return;
            }

            Set<String> families = new LinkedHashSet<>();
            for (FoundStructure structure : found.values()) {
                record(level, shipId, structure);
                families.add(structure.family);
            }
            message(player, "FIELD SURVEY // Identified " + found.size()
                    + (found.size() == 1 ? " structure: " : " structures: ")
                    + String.join(", ", families));
            sendResult(player, 1000 + Math.min(found.size(), 999));
        } catch (Throwable failure) {
            System.err.println("[NewWorldCore] Dynamic field survey failed: " + failure);
            failure.printStackTrace(System.err);
            message(player, "FIELD SURVEY // Scan failed. Check server log.");
            sendResult(player, 104);
        }
    }

    private static Map<String, FoundStructure> findLoadedStructures(Object level, Object playerPos) throws Exception {
        Object manager = call(level, "structureManager");
        Object registryAccess = call(level, "registryAccess");
        Class<?> registries = Class.forName("net.minecraft.core.registries.Registries");
        Object structureKey = registries.getField("STRUCTURE").get(null);
        Object registry = call(registryAccess, "registryOrThrow", structureKey);
        int px = intCall(playerPos, "getX");
        int pz = intCall(playerPos, "getZ");
        int baseChunkX = px >> 4;
        int baseChunkZ = pz >> 4;
        Constructor<?> chunkPosCtor = Class.forName("net.minecraft.world.level.ChunkPos")
                .getConstructor(int.class, int.class);
        Predicate<Object> allStructures = unused -> true;
        Map<String, FoundStructure> found = new LinkedHashMap<>();

        for (int dz = -CHUNK_RADIUS; dz <= CHUNK_RADIUS; dz++) {
            for (int dx = -CHUNK_RADIUS; dx <= CHUNK_RADIUS; dx++) {
                Object chunkPos = chunkPosCtor.newInstance(baseChunkX + dx, baseChunkZ + dz);
                Object startsValue = call(manager, "startsForStructure", chunkPos, allStructures);
                if (!(startsValue instanceof List<?> starts)) continue;
                for (Object start : starts) {
                    if (start == null || !Boolean.TRUE.equals(call(start, "isValid"))) continue;
                    Object structure = call(start, "getStructure");
                    Object location = call(registry, "getKey", structure);
                    if (location == null) continue;
                    String namespace = String.valueOf(call(location, "getNamespace"));
                    String path = String.valueOf(call(location, "getPath"));
                    if (Navigation0581DynamicStructureScanner.isGeologyStructure(namespace, path)) continue;
                    Object box = call(start, "getBoundingBox");
                    int minX = intCall(box, "minX");
                    int maxX = intCall(box, "maxX");
                    int minZ = intCall(box, "minZ");
                    int maxZ = intCall(box, "maxZ");
                    int nearestX = clamp(px, minX, maxX);
                    int nearestZ = clamp(pz, minZ, maxZ);
                    double edgeDistance = Math.hypot((double) nearestX - px, (double) nearestZ - pz);
                    if (edgeDistance > RANGE) continue;

                    Object center = call(box, "getCenter");
                    int x = intCall(center, "getX");
                    int y = intCall(center, "getY");
                    int z = intCall(center, "getZ");
                    int distance = (int) Math.round(Math.hypot((double) x - px, (double) z - pz));
                    Object startChunk = call(start, "getChunkPos");
                    String key = namespace + ':' + path + '@' + String.valueOf(startChunk);
                    found.putIfAbsent(key, new FoundStructure(
                            Navigation0581DynamicStructureScanner.family(path),
                            "minecraft".equals(namespace) ? "VANILLA" : namespace.replace('_', ' ').toUpperCase(Locale.ROOT),
                            x, y, z, distance));
                }
            }
        }
        return found;
    }

    public static void purgeInvalidStructureDiscoveries(Object level, String shipId) throws Exception {
        if (level == null || shipId == null || shipId.isBlank() || "null".equalsIgnoreCase(shipId)) return;
        Object data = invokeStatic("net.newworld.navigation.NavigationDiscoverySavedData", "get", level);
        Object state = call(data, "state", shipId);
        Object discoveriesValue = getField(state, "discoveries");
        if (!(discoveriesValue instanceof Map<?, ?> discoveries)) return;

        int before = discoveries.size();
        discoveries.entrySet().removeIf(entry -> invalidStructureDiscovery(entry.getValue()));
        if (discoveries.size() == before) return;

        Object selectedKey = getField(state, "selectedKey");
        if (selectedKey != null && !discoveries.containsKey(selectedKey)) {
            setField(state, "selectedKey", null);
        }
        call(data, "setDirty");
        System.out.println("[NewWorldCore] Removed " + (before - discoveries.size())
                + " invalid structure discovery record(s) for ship " + shipId + '.');
    }

    private static boolean invalidStructureDiscovery(Object discovery) {
        if (discovery == null) return false;
        try {
            String kind = String.valueOf(getField(discovery, "kind"));
            String label = String.valueOf(getField(discovery, "label")).trim().toUpperCase(Locale.ROOT);
            String clazz = String.valueOf(getField(discovery, "clazz")).trim().toUpperCase(Locale.ROOT);
            return "STRUCTURE".equalsIgnoreCase(kind)
                    && ("MODDED STRUCTURE".equals(label)
                    || ("NEWWORLDCORE".equals(clazz) && isGeologyFamilyLabel(label)));
        } catch (Throwable ignored) {
            return false;
        }
    }

    public static boolean isGeologyFamilyLabel(String rawLabel) {
        if (rawLabel == null) return false;
        return rawLabel.trim().toUpperCase(Locale.ROOT).endsWith(" DEPOSIT");
    }

    private static void record(Object level, String shipId, FoundStructure structure) throws Exception {
        Class<?> discoveryType = Class.forName("net.newworld.navigation.NavigationDiscoverySavedData$Discovery");
        Object discovery = discoveryType.getConstructor().newInstance();
        setField(discovery, "label", structure.family);
        setField(discovery, "clazz", structure.clazz);
        setField(discovery, "dimension", dimension(level));
        setField(discovery, "distance", structure.distance);
        setField(discovery, "x", structure.x);
        setField(discovery, "y", structure.y);
        setField(discovery, "z", structure.z);
        setField(discovery, "discoveredAt", ((Number) call(level, "getGameTime")).longValue());
        setField(discovery, "visited", true);
        setField(discovery, "kind", "STRUCTURE");
        setField(discovery, "source", "FIELD");

        Object data = invokeStatic("net.newworld.navigation.NavigationDiscoverySavedData", "get", level);
        call(data, "record", shipId, discovery);
    }

    private static String dimension(Object level) {
        try {
            Object key = call(level, "dimension");
            Object location = call(key, "location");
            return String.valueOf(location);
        } catch (Throwable ignored) {
            return "minecraft:overworld";
        }
    }

    private static void message(Object player, String text) {
        try {
            invokePrivateStatic("net.newworld.player.PlayerFieldSurvey0503Fix", "message", player, text);
        } catch (Throwable ignored) {
            System.out.println("[NewWorldCore] " + text);
        }
    }

    private static void sendResult(Object player, int code) {
        try {
            invokeStatic("net.newworld.player.PlayerFieldSurvey0504Bridge", "sendResult", player, code);
        } catch (Throwable failure) {
            System.err.println("[NewWorldCore] Could not send field survey result: " + failure);
        }
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private static Object invokeStatic(String className, String method, Object... args) throws Exception {
        Class<?> type = Class.forName(className);
        Method found = findMethod(type, method, true, args);
        found.setAccessible(true);
        return found.invoke(null, args);
    }

    private static Object invokePrivateStatic(String className, String method, Object... args) throws Exception {
        return invokeStatic(className, method, args);
    }

    private static Object call(Object target, String method, Object... args) throws Exception {
        if (target == null) return null;
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
        for (Method method : type.getMethods()) {
            if (!method.getName().equals(name) || Modifier.isStatic(method.getModifiers()) != requireStatic) continue;
            Class<?>[] parameters = method.getParameterTypes();
            if (parameters.length != args.length) continue;
            boolean matches = true;
            for (int i = 0; i < parameters.length; i++) {
                if (!compatible(parameters[i], args[i])) { matches = false; break; }
            }
            if (matches) return method;
        }
        throw new NoSuchMethodException(type.getName() + "." + name);
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

    private static void setField(Object target, String name, Object value) throws Exception {
        Field field = findField(target.getClass(), name);
        field.set(target, value);
    }

    private static Object getField(Object target, String name) throws Exception {
        return findField(target.getClass(), name).get(target);
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

    private static int intCall(Object target, String method) throws Exception {
        return ((Number) call(target, method)).intValue();
    }

    private record FoundStructure(String family, String clazz, int x, int y, int z, int distance) {}
}
