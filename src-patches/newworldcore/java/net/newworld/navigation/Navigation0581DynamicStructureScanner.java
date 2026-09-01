package net.newworld.navigation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.stream.Stream;

/** Placement-only structure registry scanner used by the binary compatibility patch. */
public final class Navigation0581DynamicStructureScanner {
    private static final int MAX_RESULTS = 128;
    private static final List<String> VANILLA_FILTER_LABELS = List.of(
            "VILLAGE", "MINESHAFT", "SHIPWRECK", "BURIED TREASURE", "RUINED PORTAL",
            "ANCIENT CITY", "TRIAL CHAMBERS", "STRONGHOLD", "OCEAN MONUMENT",
            "WOODLAND MANSION", "NETHER FORTRESS", "BASTION", "END CITY");
    private static final Map<Object, ScanContext> CONTEXTS = new WeakHashMap<>();

    private Navigation0581DynamicStructureScanner() {}

    public static void scanOneTile(Object container) {
        try {
            Object tilesValue = field(container, "scanTiles");
            if (!(tilesValue instanceof List<?>)) return;
            @SuppressWarnings("unchecked")
            List<Object> tiles = (List<Object>) tilesValue;
            Object level = field(container, "scanExterior");
            Object origin = field(container, "scanOrigin");
            Object resultsValue = field(container, "radarResults");
            if (level == null || origin == null || !(resultsValue instanceof List<?>)) return;
            @SuppressWarnings("unchecked")
            List<Object> results = (List<Object>) resultsValue;

            ScanContext context;
            synchronized (CONTEXTS) {
                context = CONTEXTS.get(container);
                if (context == null || context.level != level) {
                    context = createContext(container, level, origin, tiles);
                    CONTEXTS.put(container, context);
                }
            }

            int index = intField(container, "scanTileIndex");
            if (index < 0 || index >= tiles.size()) return;

            if (index >= context.dynamicTasks.size()) return;

            int taskIndex = index;
            setIntField(container, "scanTileIndex", index + 1);

            PlacementTask task = context.dynamicTasks.get(taskIndex);
            Object foundPos = nearestPotential(context, task.placement, origin);
            if (foundPos == null) return;
            int x = intCall(foundPos, "getX");
            int y = intCall(foundPos, "getY");
            int z = intCall(foundPos, "getZ");
            String family = task.family();
            if (alreadyPresent(results, family, x, z)) return;

            int ox = intCall(origin, "getX");
            int oz = intCall(origin, "getZ");
            int distance = (int) Math.round(Math.hypot((double) x - ox, (double) z - oz));
            addBounded(results, newRadarResult(family, task.clazz(), distance, x, y, z));
        } catch (Throwable failure) {
            System.err.println("[NewWorldCore] Dynamic structure radar tile failed: " + failure);
            failure.printStackTrace(System.err);
        }
    }

    public static void prepareClassification(Object container) {
        synchronized (CONTEXTS) {
            CONTEXTS.remove(container);
        }
        try {
            Object resultsValue = field(container, "radarResults");
            if (resultsValue instanceof List<?> rawResults) {
                @SuppressWarnings("unchecked")
                List<Object> results = (List<Object>) rawResults;
                int before = results.size();
                int mask = ((Number) invokeStatic("net.newworld.navigation.Navigation0475RadarFilteX",
                        "mask", container)).intValue();
                Set<String> selected = selectedLabels(container);
                results.removeIf(result -> !passesFilter(result, mask)
                        || !passesDynamicFilter(result, selected));
                results.sort(Comparator.comparingInt(Navigation0581DynamicStructureScanner::resultDistance));
                trimToLimit(results);
                System.out.println("[NewWorldCore] Placement radar finished with " + results.size()
                        + " results (" + before + " before active filters; selected="
                        + (selected.isEmpty() ? "ALL" : String.join(",", selected)) + ").");
            }
            invokeStatic("net.newworld.navigation.Navigation0491RadarVisitGate", "refineUndergroundY", container);
            purgeInvalidDiscoveries(container);
            invokeStatic("net.newworld.navigation.NavigationDiscoveryBridge", "finishScan", container);
        } catch (Throwable failure) {
            System.err.println("[NewWorldCore] Dynamic structure radar finish failed: " + failure);
            failure.printStackTrace(System.err);
        }
    }

    private static ScanContext createContext(Object container, Object level, Object origin, List<Object> tiles)
            throws Exception {
        Object access = call(level, "registryAccess");
        Class<?> registries = Class.forName("net.minecraft.core.registries.Registries");
        Field structureField = registries.getField("STRUCTURE");
        Object structureKey = structureField.get(null);
        Object registry = call(access, "registryOrThrow", structureKey);
        Object chunkSource = call(level, "getChunkSource");
        Object structureState = call(chunkSource, "getGeneratorState");

        Map<Object, PlacementTask> tasksByPlacement = new IdentityHashMap<>();
        Object streamValue = call(registry, "holders");
        if (streamValue instanceof Stream<?> stream) {
            try (stream) {
                stream.forEach(holder -> {
                    try {
                        StructureId id = structureId(registry, holder);
                        if (id == null) return;
                        if (isGeologyStructure(id.namespace, id.path)) return;
                        Object placementsValue = call(structureState, "getPlacementsForStructure", holder);
                        if (!(placementsValue instanceof List<?> placements)) return;
                        for (Object placement : placements) {
                            if (placement == null) continue;
                            String placementType = placement.getClass().getSimpleName();
                            if (!placementType.contains("RandomSpread") && !placementType.contains("ConcentricRings")) continue;
                            PlacementTask task = tasksByPlacement.computeIfAbsent(placement, PlacementTask::new);
                            task.ids.add(id.namespace + ':' + id.path);
                            task.families.add(family(id.path));
                            task.namespaces.add(id.namespace);
                        }
                    } catch (Throwable failure) {
                        System.err.println("[NewWorldCore] Ignoring unreadable structure registry entry: " + failure);
                    }
                });
            }
        }
        List<PlacementTask> tasks = new ArrayList<>(tasksByPlacement.values());
        Set<String> selected = selectedLabels(container);
        if (!selected.isEmpty()) {
            tasks.removeIf(task -> !selected.contains(normalizeLabel(task.family())));
        }
        tasks.sort(Comparator.comparingInt(Navigation0581DynamicStructureScanner::priority)
                .thenComparing(PlacementTask::sortKey));
        int rangeBlocks = Math.max(16, ((Number) invokeStatic(
                "net.newworld.navigation.NavigationUpgradeRuntime", "scanRange", container)).intValue());
        int rangeChunks = Math.max(1, (int) Math.ceil((double) rangeBlocks / 16.0D));
        long rangeSq = (long) rangeBlocks * rangeBlocks;
        List<Object> originalTiles = new ArrayList<>(tiles);
        tiles.clear();
        for (int i = 0; i < tasks.size(); i++) tiles.add(origin);
        System.out.println("[NewWorldCore] Dynamic radar queued " + tasks.size()
                + " placement-only structure tasks at range " + rangeBlocks + " blocks; replaced "
                + originalTiles.size() + " legacy locate tiles; selected="
                + (selected.isEmpty() ? "ALL" : String.join(",", selected)) + '.');
        return new ScanContext(level, registry, structureState, tasks, rangeChunks, rangeSq);
    }

    private static int priority(PlacementTask task) {
        if (task.hasPrefix("explorify:campsite")) return 0;
        if (task.hasPrefix("structory:abandoned_camp")) return 1;
        if (task.hasPrefix("betterarcheology:archeologist_camp")) return 2;
        if (task.hasPrefix("explorify:")) return 10;
        if (task.hasPrefix("structory:")) return 11;
        if (task.hasPrefix("betterarcheology:")) return 12;
        return 100;
    }

    private static Object nearestPotential(ScanContext context, Object placement, Object origin) throws Exception {
        if (placement.getClass().getSimpleName().contains("ConcentricRings")) {
            return nearestRingPotential(context, placement, origin);
        }
        int spacing = intCall(placement, "spacing");
        if (spacing <= 0) return null;
        int originChunkX = Math.floorDiv(intCall(origin, "getX"), 16);
        int originChunkZ = Math.floorDiv(intCall(origin, "getZ"), 16);
        int originRegionX = Math.floorDiv(originChunkX, spacing);
        int originRegionZ = Math.floorDiv(originChunkZ, spacing);
        int regionRadius = Math.max(1, (int) Math.ceil((double) context.rangeChunks / spacing) + 1);
        long seed = ((Number) call(context.structureState, "getLevelSeed")).longValue();
        Constructor<?> chunkPosCtor = Class.forName("net.minecraft.world.level.ChunkPos")
                .getConstructor(int.class, int.class);
        Object best = null;
        double bestSq = Double.MAX_VALUE;
        int ox = intCall(origin, "getX");
        int oz = intCall(origin, "getZ");

        for (int rz = originRegionZ - regionRadius; rz <= originRegionZ + regionRadius; rz++) {
            for (int rx = originRegionX - regionRadius; rx <= originRegionX + regionRadius; rx++) {
                // RandomSpreadStructurePlacement divides its chunk-space inputs by spacing.
                // Feed one chunk from each region; passing the region indexes directly would
                // divide them a second time and repeatedly inspect the wrong regions.
                int regionChunkX = regionSampleChunk(rx, spacing);
                int regionChunkZ = regionSampleChunk(rz, spacing);
                Object chunkPos = call(placement, "getPotentialStructureChunk",
                        seed, regionChunkX, regionChunkZ);
                int cx = intField(chunkPos, "x");
                int cz = intField(chunkPos, "z");
                if (Math.abs(cx - originChunkX) > context.rangeChunks
                        || Math.abs(cz - originChunkZ) > context.rangeChunks) continue;
                if (!Boolean.TRUE.equals(call(placement, "isStructureChunk", context.structureState, cx, cz))) continue;
                Object locatePos = call(placement, "getLocatePos", chunkPosCtor.newInstance(cx, cz));
                int x = intCall(locatePos, "getX");
                int z = intCall(locatePos, "getZ");
                double distanceSq = ((double) x - ox) * ((double) x - ox) + ((double) z - oz) * ((double) z - oz);
                if (distanceSq > context.rangeSq) continue;
                if (distanceSq < bestSq) {
                    bestSq = distanceSq;
                    best = locatePos;
                }
            }
        }
        return best;
    }

    private static int regionSampleChunk(int region, int spacing) {
        return Math.multiplyExact(region, spacing);
    }

    private static Object nearestRingPotential(ScanContext context, Object placement, Object origin) throws Exception {
        Object positionsValue = call(context.structureState, "getRingPositionsFor", placement);
        if (!(positionsValue instanceof List<?> positions)) return null;
        int originChunkX = Math.floorDiv(intCall(origin, "getX"), 16);
        int originChunkZ = Math.floorDiv(intCall(origin, "getZ"), 16);
        Object best = null;
        double bestSq = Double.MAX_VALUE;
        int ox = intCall(origin, "getX");
        int oz = intCall(origin, "getZ");
        for (Object chunkPos : positions) {
            int cx = intField(chunkPos, "x");
            int cz = intField(chunkPos, "z");
            if (Math.abs(cx - originChunkX) > context.rangeChunks
                    || Math.abs(cz - originChunkZ) > context.rangeChunks) continue;
            Object locatePos = call(placement, "getLocatePos", chunkPos);
            int x = intCall(locatePos, "getX");
            int z = intCall(locatePos, "getZ");
            double distanceSq = ((double) x - ox) * ((double) x - ox) + ((double) z - oz) * ((double) z - oz);
            if (distanceSq > context.rangeSq) continue;
            if (distanceSq < bestSq) {
                bestSq = distanceSq;
                best = locatePos;
            }
        }
        return best;
    }

    private static StructureId structureId(Object registry, Object holder) throws Exception {
        Object optionalValue = call(holder, "unwrapKey");
        Object key = optionalValue instanceof Optional<?> optional ? optional.orElse(null) : null;
        Object location = key == null ? null : call(key, "location");
        if (location == null) {
            Object value = call(holder, "value");
            location = call(registry, "getKey", value);
        }
        if (location == null) return null;
        return new StructureId(String.valueOf(call(location, "getNamespace")),
                String.valueOf(call(location, "getPath")));
    }

    private static Object newRadarResult(String label, String clazz, int distance, int x, int y, int z) throws Exception {
        Class<?> type = Class.forName("net.newworld.navigation.NavigationGuiBootstrap$RadarResult");
        Constructor<?> constructor = type.getDeclaredConstructor(String.class, String.class,
                int.class, int.class, int.class, int.class);
        constructor.setAccessible(true);
        return constructor.newInstance(label, clazz, distance, x, y, z);
    }

    private static boolean alreadyPresent(List<Object> results, String label, int x, int z) {
        for (Object result : results) {
            try {
                String existingLabel = String.valueOf(call(result, "label"));
                int existingX = intCall(result, "x");
                int existingZ = intCall(result, "z");
                if (label.equals(existingLabel) && Math.abs(existingX - x) <= 8 && Math.abs(existingZ - z) <= 8) return true;
            } catch (Throwable ignored) {}
        }
        return false;
    }

    private static void addBounded(List<Object> results, Object candidate) {
        results.add(candidate);
        trimToLimit(results);
    }

    private static void trimToLimit(List<Object> results) {
        while (results.size() > MAX_RESULTS) {
            int farthestIndex = -1;
            int farthestDistance = Integer.MIN_VALUE;
            for (int i = 0; i < results.size(); i++) {
                try {
                    int distance = intCall(results.get(i), "distance");
                    if (distance > farthestDistance) {
                        farthestDistance = distance;
                        farthestIndex = i;
                    }
                } catch (Throwable ignored) {}
            }
            if (farthestIndex < 0) farthestIndex = results.size() - 1;
            results.remove(farthestIndex);
        }
    }

    private static boolean passesFilter(Object result, int mask) {
        try {
            String label = String.valueOf(call(result, "label")).toUpperCase(Locale.ROOT);
            int vanillaIndex = VANILLA_FILTER_LABELS.indexOf(label);
            return vanillaIndex < 0 || (mask & (1 << vanillaIndex)) != 0;
        } catch (Throwable ignored) {
            return true;
        }
    }

    private static int resultDistance(Object result) {
        try {
            return intCall(result, "distance");
        } catch (Throwable ignored) {
            return Integer.MAX_VALUE;
        }
    }

    private static Set<String> selectedLabels(Object container) {
        try {
            Object value = invokeStatic("net.newworld.navigation.Navigation0475RadarFilter",
                    "selected", container);
            if (!(value instanceof Set<?> raw)) return Set.of();
            Set<String> selected = new LinkedHashSet<>();
            for (Object label : raw) {
                String normalized = normalizeLabel(String.valueOf(label));
                if (!normalized.isBlank()) selected.add(normalized);
            }
            return selected;
        } catch (Throwable failure) {
            System.err.println("[NewWorldCore] Could not read dynamic structure filters: " + failure);
            return Set.of();
        }
    }

    private static boolean passesDynamicFilter(Object result, Set<String> selected) {
        if (selected == null || selected.isEmpty()) return true;
        try {
            return selected.contains(normalizeLabel(String.valueOf(call(result, "label"))));
        } catch (Throwable ignored) {
            return false;
        }
    }

    private static String normalizeLabel(String label) {
        return label == null ? "" : label.trim()
                .replace('_', ' ')
                .replaceAll("\\s+", " ")
                .toUpperCase(Locale.ROOT);
    }

    private static void purgeInvalidDiscoveries(Object container) {
        try {
            Object level = field(container, "scanExterior");
            Object shipId = invokeStatic("net.newworld.navigation.NavigationDiscoveryBridge", "shipId", container);
            invokeStatic("net.newworld.player.PlayerFieldSurvey0581Fix", "purgeInvalidStructureDiscoveries",
                    level, String.valueOf(shipId));
        } catch (Throwable failure) {
            System.err.println("[NewWorldCore] Could not purge invalid structure discoveries: " + failure);
        }
    }

    public static String family(String rawPath) {
        String path = rawPath == null ? "structure" : rawPath.toLowerCase(Locale.ROOT);
        int slash = path.lastIndexOf('/');
        if (slash >= 0) path = path.substring(slash + 1);
        if (path.contains("archeologist_camp")) return "ARCHEOLOGIST CAMP";
        if (path.contains("abandoned_camp")) return "ABANDONED CAMP";
        if (path.contains("campsite")) return "CAMPSITE";
        if (path.contains("village")) return "VILLAGE";
        if (path.contains("pillager_outpost") || path.equals("outpost")) return "PILLAGER OUTPOST";
        if (path.contains("mineshaft")) return "MINESHAFT";
        if (path.contains("ruined_portal")) return "RUINED PORTAL";
        if (path.contains("ocean_ruin")) return "OCEAN RUIN";
        if (path.contains("shipwreck")) return "SHIPWRECK";
        if (path.contains("stronghold")) return "STRONGHOLD";
        if (path.contains("trial_chamber")) return "TRIAL CHAMBERS";
        if (path.contains("ancient_city")) return "ANCIENT CITY";
        if (path.contains("woodland_mansion") || path.equals("mansion")) return "WOODLAND MANSION";
        if (path.contains("bastion")) return "BASTION REMNANT";
        if (path.contains("nether_fortress") || path.equals("fortress")) return "NETHER FORTRESS";
        if (path.contains("monument")) return "OCEAN MONUMENT";
        if (path.contains("dark_tower")) return "DARK TOWER";
        if (path.contains("observation_tower")) return "OBSERVATION TOWER";
        if (path.contains("clown_caravan")) return "CLOWN CARAVAN";
        if (path.contains("fossil")) return "FOSSIL";

        String previous;
        do {
            previous = path;
            path = path.replaceFirst("_(plains|desert|savanna|snowy|taiga|jungle|swamp|badlands|grassy|forest|birch|dark_forest|mountain|mountains|mesa|ocean|beach|warm|cold|frozen|nether|end)$", "");
            path = path.replaceFirst("_(variant_?)?\\d+$", "");
        } while (!previous.equals(path));
        return path.replace('_', ' ').replace('-', ' ').replaceAll("\\s+", " ").trim().toUpperCase(Locale.ROOT);
    }

    public static boolean isGeologyStructure(String namespace, String rawPath) {
        String ns = namespace == null ? "" : namespace.toLowerCase(Locale.ROOT);
        String path = rawPath == null ? "" : rawPath.toLowerCase(Locale.ROOT);
        return "newworldcore".equals(ns)
                && (path.endsWith("_deposit") || path.startsWith("geology/"));
    }

    private static String clazz(String namespace) {
        if ("minecraft".equals(namespace)) return "VANILLA";
        return namespace.replace('_', ' ').toUpperCase(Locale.ROOT);
    }

    private static Object invokeStatic(String className, String method, Object... args) throws Exception {
        Class<?> type = Class.forName(className);
        Method found = findMethod(type, method, true, args);
        found.setAccessible(true);
        return found.invoke(null, args);
    }

    private static Object invokeNamed(Object target, String method, Object... args) throws Exception {
        return call(target, method, args);
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

    private static Object field(Object target, String name) throws Exception {
        return findField(target.getClass(), name).get(target);
    }

    private static int intField(Object target, String name) throws Exception {
        return ((Number) field(target, name)).intValue();
    }

    private static void setIntField(Object target, String name, int value) throws Exception {
        findField(target.getClass(), name).setInt(target, value);
    }

    private static int intCall(Object target, String method) throws Exception {
        return ((Number) call(target, method)).intValue();
    }

    private record StructureId(String namespace, String path) {}

    private static final class ScanContext {
        final Object level;
        final Object registry;
        final Object structureState;
        final List<PlacementTask> dynamicTasks;
        final int rangeChunks;
        final long rangeSq;

        ScanContext(Object level, Object registry, Object structureState, List<PlacementTask> dynamicTasks,
                    int rangeChunks, long rangeSq) {
            this.level = level;
            this.registry = registry;
            this.structureState = structureState;
            this.dynamicTasks = dynamicTasks;
            this.rangeChunks = rangeChunks;
            this.rangeSq = rangeSq;
        }
    }

    private static final class PlacementTask {
        final Object placement;
        final Set<String> ids = new LinkedHashSet<>();
        final Set<String> families = new LinkedHashSet<>();
        final Set<String> namespaces = new LinkedHashSet<>();

        PlacementTask(Object placement) {
            this.placement = placement;
        }

        boolean hasPrefix(String prefix) {
            return ids.stream().anyMatch(id -> id.startsWith(prefix));
        }

        String family() {
            return families.size() == 1 ? families.iterator().next() : "UNKNOWN STRUCTURE";
        }

        String clazz() {
            return namespaces.size() == 1 ? Navigation0581DynamicStructureScanner.clazz(namespaces.iterator().next()) : "MODDED";
        }

        String sortKey() {
            return ids.isEmpty() ? "~" : ids.iterator().next();
        }
    }
}
