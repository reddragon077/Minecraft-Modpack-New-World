package net.newworld.player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import net.newworld.config.NewWorldTuning;
import net.newworld.navigation.Navigation0520GeologyDefinitions;
import net.newworld.navigation.Navigation0533DepositTemplates;

/** Short-range, physical-evidence Geological Field Survey. */
public final class PlayerGeologicalFieldSurvey0620 {
    private static final Set<String> PENDING = ConcurrentHashMap.newKeySet();
    private static volatile Object BLOCK_REGISTRY;

    private PlayerGeologicalFieldSurvey0620() {}

    public static void scan(Object player) {
        if (player == null) return;
        int delayTicks = NewWorldTuning.playerGeologicalSurveyDelayTicks();
        if (delayTicks <= 0) {
            scanNow(player);
            return;
        }

        String playerKey = playerKey(player);
        if (!PENDING.add(playerKey)) {
            message(player, "GEOLOGICAL FIELD SURVEY // Scan already in progress.");
            return;
        }

        try {
            Object server = call(player, "getServer");
            if (server == null) {
                PENDING.remove(playerKey);
                scanNow(player);
                return;
            }
            long queuedAt = System.nanoTime();
            long delayMillis = Math.max(0L, (long) delayTicks * 50L);
            CompletableFuture.delayedExecutor(delayMillis, TimeUnit.MILLISECONDS).execute(() -> {
                try {
                    Runnable scan = () -> {
                        try {
                            int found = scanNow(player);
                            long elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - queuedAt);
                            System.out.println("[NewWorld Geological Field Survey] completed horizontal="
                                    + NewWorldTuning.playerGeologicalSurveyRangeBlocks() + " vertical="
                                    + NewWorldTuning.playerGeologicalSurveyVerticalRangeBlocks() + " blocks results="
                                    + found + " elapsed=" + elapsed + "ms");
                        } finally {
                            PENDING.remove(playerKey);
                        }
                    };
                    call(server, "execute", scan);
                } catch (Throwable failure) {
                    PENDING.remove(playerKey);
                    System.err.println("[NewWorldCore] Could not schedule Geological Field Survey: " + failure);
                    sendResult(player, 204);
                }
            });
            System.out.println("[NewWorld Geological Field Survey] queued horizontal="
                    + NewWorldTuning.playerGeologicalSurveyRangeBlocks() + " vertical="
                    + NewWorldTuning.playerGeologicalSurveyVerticalRangeBlocks() + " blocks delay="
                    + delayTicks + "t");
        } catch (Throwable failure) {
            PENDING.remove(playerKey);
            System.err.println("[NewWorldCore] Geological Field Survey delay fallback: " + failure);
            scanNow(player);
        }
    }

    private static int scanNow(Object player) {
        try {
            Object level = call(player, "serverLevel");
            Object playerPos = call(player, "blockPosition");
            if (level == null || playerPos == null) {
                message(player, "GEOLOGICAL FIELD SURVEY // No active world link.");
                sendResult(player, 200);
                return 0;
            }

            Object ship = invokePrivateStatic("net.newworld.player.PlayerFieldSurveyRuntime", "findOwnedShip",
                    player, level, playerPos);
            if (ship == null) {
                message(player, "SHIP LINK LOST // No owned TARDIS could be resolved.");
                sendResult(player, 201);
                return 0;
            }

            String shipId = String.valueOf(call(ship, "id"));
            List<VerifiedDeposit> deposits = findPhysicalDeposits(level, playerPos);
            if (deposits.isEmpty()) {
                message(player, "GEOLOGICAL FIELD SURVEY // No physical deposits verified within "
                        + NewWorldTuning.playerGeologicalSurveyRangeBlocks() + " horizontal blocks.");
                sendResult(player, 202);
                return 0;
            }

            Set<String> families = new LinkedHashSet<>();
            for (VerifiedDeposit deposit : deposits) {
                record(level, shipId, deposit);
                families.add(deposit.definition.label());
                System.out.println("[NewWorld Geological Field Survey] verified " + deposit.definition.label()
                        + " center=" + deposit.x + ',' + deposit.y + ',' + deposit.z
                        + " matches=" + deposit.matches + '/' + deposit.checks);
            }
            message(player, "GEOLOGICAL FIELD SURVEY // Identified " + deposits.size()
                    + (deposits.size() == 1 ? " physical deposit: " : " physical deposits: ")
                    + String.join(", ", families));
            sendResult(player, 2000 + Math.min(deposits.size(), 999));
            return deposits.size();
        } catch (Throwable failure) {
            System.err.println("[NewWorldCore] Geological Field Survey failed: " + failure);
            failure.printStackTrace(System.err);
            message(player, "GEOLOGICAL FIELD SURVEY // Scan failed. Check server log.");
            sendResult(player, 204);
            return 0;
        }
    }

    private static List<VerifiedDeposit> findPhysicalDeposits(Object level, Object playerPos) throws Exception {
        int px = intCall(playerPos, "getX");
        int py = intCall(playerPos, "getY");
        int pz = intCall(playerPos, "getZ");
        int horizontalRange = NewWorldTuning.playerGeologicalSurveyRangeBlocks();
        int verticalRange = NewWorldTuning.playerGeologicalSurveyVerticalRangeBlocks();
        int maxResults = NewWorldTuning.playerGeologicalSurveyMaxResults();
        long seed = longCall(level, "getSeed");
        String dimension = dimension(level);
        List<VerifiedDeposit> found = new ArrayList<>();

        for (Navigation0520GeologyDefinitions.Def definition : Navigation0520GeologyDefinitions.all()) {
            if (!sameDimension(definition.dimension(), dimension)) continue;
            Navigation0533DepositTemplates.Template template = Navigation0533DepositTemplates.get(definition.id());
            if (template == null) continue;

            int spacing = Math.max(2, Math.round((float) definition.spacing() / 16.0F));
            int separation = Math.max(1, spacing * 2 / 5);
            if (separation >= spacing) separation = spacing - 1;
            int spread = Math.max(1, spacing - separation);
            int playerChunkX = Math.floorDiv(px, 16);
            int playerChunkZ = Math.floorDiv(pz, 16);
            int chunkRadius = (horizontalRange + 15) / 16 + 1;
            int minRegionX = Math.floorDiv(playerChunkX - chunkRadius, spacing) - 1;
            int maxRegionX = Math.floorDiv(playerChunkX + chunkRadius, spacing) + 1;
            int minRegionZ = Math.floorDiv(playerChunkZ - chunkRadius, spacing) - 1;
            int maxRegionZ = Math.floorDiv(playerChunkZ + chunkRadius, spacing) + 1;

            for (int regionX = minRegionX; regionX <= maxRegionX; regionX++) {
                for (int regionZ = minRegionZ; regionZ <= maxRegionZ; regionZ++) {
                    long regionSeed = seed + (long) regionX * 341873128712L
                            + (long) regionZ * 132897987541L + definition.salt();
                    int[] center = candidateCoordinates(regionSeed, spacing, spread, regionX, regionZ,
                            definition.minY(), definition.maxY(), "minecraft:the_nether".equals(dimension));
                    long dx = (long) center[0] - px;
                    long dz = (long) center[2] - pz;
                    int distance = (int) Math.round(Math.sqrt(dx * dx + dz * dz));
                    if (distance > horizontalRange || Math.abs((long) center[1] - py) > verticalRange) continue;

                    Match match = verifyPhysical(level, dimension, center[0], center[1], center[2], template);
                    if (match.matches < NewWorldTuning.playerGeologicalSurveyMinimumMatches()) continue;
                    int radius = between(regionSeed ^ 0xBADC0FFEE0DDF00DL,
                            definition.minRadius(), definition.maxRadius(), true);
                    int reserve = between(regionSeed ^ 0xC0FFEE1234L,
                            definition.minReserve(), definition.maxReserve(), true);
                    int density = between(regionSeed ^ 0x1234ABCDL,
                            definition.minDensity(), definition.maxDensity(), true);
                    found.add(new VerifiedDeposit(definition, center[0], center[1], center[2],
                            radius, reserve, density, distance, match.matches, match.checks));
                }
            }
        }

        found.sort(Comparator.comparingInt(VerifiedDeposit::distance));
        if (found.size() > maxResults) found.subList(maxResults, found.size()).clear();
        return found;
    }

    public static int[] candidateCoordinates(long regionSeed, int spacing, int spread, int regionX, int regionZ,
                                             int minY, int maxY, boolean nether) {
        Random random = new Random(regionSeed);
        int chunkX = regionX * spacing + random.nextInt(Math.max(1, spread));
        int chunkZ = regionZ * spacing + random.nextInt(Math.max(1, spread));
        int y = between(regionSeed ^ 0x5DEECE66DL, minY, maxY, !nether);
        return new int[]{chunkX * 16 + 8, y, chunkZ * 16 + 8};
    }

    private static Match verifyPhysical(Object level, String dimension, int centerX, int centerY, int centerZ,
                                        Navigation0533DepositTemplates.Template template) throws Exception {
        int originX = centerX - template.sx() / 2;
        int originY = centerY - template.sy() / 2;
        int originZ = centerZ - template.sz() / 2;
        int maxChecks = NewWorldTuning.playerGeologicalSurveyMaxBlockChecks();
        int minimum = NewWorldTuning.playerGeologicalSurveyMinimumMatches();
        int checks = 0;
        int matches = 0;
        for (Navigation0533DepositTemplates.Block block : template.blocks()) {
            if (checks >= maxChecks) break;
            if (block.state() < 0 || block.state() >= template.palette().length) continue;
            int x = originX + block.x();
            int y = originY + block.y();
            int z = originZ + block.z();
            Object pos = blockPos(x, y, z);
            if (!hasChunk(level, pos)) continue;
            checks++;
            String expected = template.palette()[block.state()];
            if (!"minecraft:the_nether".equals(dimension)) expected = depthVariant(expected, y);
            String actual = stateId(call(level, "getBlockState", pos));
            if (expected != null && expected.equals(actual)) {
                matches++;
                if (matches >= minimum) break;
            }
        }
        return new Match(matches, checks);
    }

    private static void record(Object level, String shipId, VerifiedDeposit deposit) throws Exception {
        Class<?> discoveryType = Class.forName("net.newworld.navigation.NavigationDiscoverySavedData$Discovery");
        Object discovery = discoveryType.getConstructor().newInstance();
        Navigation0520GeologyDefinitions.Def definition = deposit.definition;
        setField(discovery, "label", definition.label());
        setField(discovery, "clazz", "PHYSICAL DEPOSIT");
        setField(discovery, "dimension", dimension(level));
        setField(discovery, "distance", deposit.distance);
        setField(discovery, "x", deposit.x);
        setField(discovery, "y", deposit.y);
        setField(discovery, "z", deposit.z);
        setField(discovery, "discoveredAt", System.currentTimeMillis());
        setField(discovery, "favorite", false);
        setField(discovery, "visited", true);
        setField(discovery, "kind", "GEOLOGY");
        setField(discovery, "source", "FIELD");
        setField(discovery, "depositId", definition.id());
        setField(discovery, "primaryResource", definition.primary());
        setField(discovery, "secondaryResources", definition.secondary());
        setField(discovery, "byproducts", definition.byproducts());
        setField(discovery, "reserve", deposit.reserve);
        setField(discovery, "radius", deposit.radius);
        setField(discovery, "minY", definition.minY());
        setField(discovery, "maxY", definition.maxY());
        setField(discovery, "density", deposit.density);
        setField(discovery, "rarity", definition.rarity());

        Object data = invokeStatic("net.newworld.navigation.NavigationDiscoverySavedData", "get", level);
        call(data, "record", shipId, discovery);
    }

    private static int between(long seed, int min, int max, boolean mixed) {
        if (max < min) { int swap = min; min = max; max = swap; }
        if (min == max) return min;
        long value = mixed ? mix(seed) : seed;
        return min + (int) Math.floorMod(value, (long) max - min + 1L);
    }

    private static long mix(long value) {
        value = (value ^ value >>> 33) * -49064778989728563L;
        value = (value ^ value >>> 33) * -4265267296055464877L;
        return value ^ value >>> 33;
    }

    private static String depthVariant(String id, int y) {
        if (id == null || y >= 0) return id;
        return switch (id) {
            case "minecraft:coal_ore" -> "minecraft:deepslate_coal_ore";
            case "minecraft:iron_ore" -> "minecraft:deepslate_iron_ore";
            case "minecraft:copper_ore" -> "minecraft:deepslate_copper_ore";
            case "minecraft:gold_ore" -> "minecraft:deepslate_gold_ore";
            case "minecraft:redstone_ore" -> "minecraft:deepslate_redstone_ore";
            case "minecraft:lapis_ore" -> "minecraft:deepslate_lapis_ore";
            case "minecraft:diamond_ore" -> "minecraft:deepslate_diamond_ore";
            case "minecraft:emerald_ore" -> "minecraft:deepslate_emerald_ore";
            default -> id;
        };
    }

    private static boolean hasChunk(Object level, Object pos) {
        try { return Boolean.TRUE.equals(call(level, "hasChunkAt", pos)); }
        catch (Throwable ignored) { return false; }
    }

    private static Object blockPos(int x, int y, int z) throws Exception {
        return Class.forName("net.minecraft.core.BlockPos").getConstructor(int.class, int.class, int.class)
                .newInstance(x, y, z);
    }

    private static String stateId(Object state) {
        try {
            Object block = call(state, "getBlock");
            Object key = call(blockRegistry(), "getKey", block);
            return key == null ? null : String.valueOf(key);
        } catch (Throwable ignored) {
            return null;
        }
    }

    private static Object blockRegistry() throws Exception {
        Object cached = BLOCK_REGISTRY;
        if (cached != null) return cached;
        return BLOCK_REGISTRY = Class.forName("net.minecraft.core.registries.BuiltInRegistries")
                .getField("BLOCK").get(null);
    }

    private static String dimension(Object level) {
        try {
            Object key = call(level, "dimension");
            return String.valueOf(call(key, "location"));
        } catch (Throwable ignored) {
            return "unknown";
        }
    }

    private static boolean sameDimension(String left, String right) {
        if (left == null || right == null) return false;
        String a = left.toLowerCase(Locale.ROOT);
        String b = right.toLowerCase(Locale.ROOT);
        if (a.startsWith("minecraft:")) a = a.substring(10);
        if (b.startsWith("minecraft:")) b = b.substring(10);
        return a.equals(b);
    }

    private static void message(Object player, String text) {
        try { invokePrivateStatic("net.newworld.player.PlayerFieldSurvey0503Fix", "message", player, text); }
        catch (Throwable ignored) { System.out.println("[NewWorldCore] " + text); }
    }

    private static void sendResult(Object player, int code) {
        try { invokeStatic("net.newworld.player.PlayerFieldSurvey0504Bridge", "sendResult", player, code); }
        catch (Throwable failure) { System.err.println("[NewWorldCore] Could not send Geological Survey result: " + failure); }
    }

    private static String playerKey(Object player) {
        try { return String.valueOf(call(player, "getUUID")); }
        catch (Throwable ignored) { return player.getClass().getName() + '@' + System.identityHashCode(player); }
    }

    private static Object invokeStatic(String className, String method, Object... args) throws Exception {
        Method found = findMethod(Class.forName(className), method, true, args);
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
                if (matches(method.getParameterTypes(), args)) return method;
            }
        }
        for (Method method : type.getMethods()) {
            if (!method.getName().equals(name) || Modifier.isStatic(method.getModifiers()) != requireStatic) continue;
            if (matches(method.getParameterTypes(), args)) return method;
        }
        throw new NoSuchMethodException(type.getName() + '.' + name);
    }

    private static boolean matches(Class<?>[] parameters, Object[] args) {
        if (parameters.length != args.length) return false;
        for (int i = 0; i < parameters.length; i++) {
            if (!compatible(parameters[i], args[i])) return false;
        }
        return true;
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

    private static int intCall(Object target, String method) throws Exception {
        return ((Number) call(target, method)).intValue();
    }

    private static long longCall(Object target, String method) throws Exception {
        return ((Number) call(target, method)).longValue();
    }

    private record Match(int matches, int checks) {}

    private record VerifiedDeposit(Navigation0520GeologyDefinitions.Def definition,
                                   int x, int y, int z, int radius, int reserve, int density,
                                   int distance, int matches, int checks) {}
}
