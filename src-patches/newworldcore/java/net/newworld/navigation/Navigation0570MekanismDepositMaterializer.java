package net.newworld.navigation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

/**
 * Compatibility materializer retained under its original class name because
 * Navigation0533GeologyGameEvents already calls it. The expanded marker lets
 * existing chunks receive all post-vanilla deposits without replaying the
 * original physical-worldgen marker.
 */
public final class Navigation0570MekanismDepositMaterializer {
    private static final String MARKER = "NewWorldGeologyExpandedV1";

    private static final Set<String> IDS = Set.of(
        "newworldcore:osmium_strata",
        "newworldcore:tin_lode",
        "newworldcore:lead_galena",
        "newworldcore:uranium_pitchblende",
        "newworldcore:fluorite_crystal",
        "newworldcore:bauxite_strata",
        "newworldcore:nickel_sulfide",
        "newworldcore:silver_vein",
        "newworldcore:zinc_lode",
        "newworldcore:platinum_intrusion",
        "newworldcore:uraninite_pocket",
        "newworldcore:certus_quartz_matrix"
    );

    private Navigation0570MekanismDepositMaterializer() {
    }

    public static void onChunkLoad(Object event) {
        try {
            Object level = call(event, "getLevel");
            Object chunk = call(event, "getChunk");
            if (level == null || chunk == null || !boolPrivate("isServerLevel", level)) {
                return;
            }
            if (!"minecraft:overworld".equals(stringPrivate("dimensionString", level)) || isMarked(chunk)) {
                return;
            }

            Object chunkPos = call(chunk, "getPos");
            int chunkX = intPrivate("chunkCoord", chunkPos, "x");
            int chunkZ = intPrivate("chunkCoord", chunkPos, "z");
            long seed = number(call(level, "getSeed")).longValue();

            Method candidatesForChunk = privateMethod(
                "candidatesForChunk",
                Navigation0520GeologyDefinitions.Def.class,
                long.class,
                int.class,
                int.class,
                Navigation0533DepositTemplates.Template.class
            );
            Method placeChunkPart = privateMethod(
                "placeChunkPart",
                Object.class,
                int.class,
                int.class,
                int.class,
                int.class,
                int.class,
                Navigation0533DepositTemplates.Template.class
            );

            int placedBlocks = 0;
            int candidateCount = 0;
            for (Navigation0520GeologyDefinitions.Def definition : Navigation0520GeologyDefinitions.all()) {
                if (!IDS.contains(definition.id()) || !"minecraft:overworld".equals(definition.dimension())) {
                    continue;
                }
                Navigation0533DepositTemplates.Template template = Navigation0533DepositTemplates.get(definition.id());
                if (template == null) {
                    continue;
                }
                List<?> candidates = (List<?>) candidatesForChunk.invoke(null, definition, seed, chunkX, chunkZ, template);
                for (Object candidate : candidates) {
                    candidateCount++;
                    int x = intField(candidate, "x");
                    int y = intField(candidate, "y");
                    int z = intField(candidate, "z");
                    Object result = placeChunkPart.invoke(null, chunk, chunkX, chunkZ, x, y, z, template);
                    if (result instanceof Number value) {
                        placedBlocks += value.intValue();
                    }
                }
            }

            mark(chunk);
            try {
                invokePrivate("setUnsaved", chunk);
            } catch (Throwable ignored) {
            }
            if (placedBlocks > 0) {
                System.out.println("[NewWorld Geology] Expanded deposits materialized chunk=" + chunkX + "," + chunkZ + " candidates=" + candidateCount + " blocks=" + placedBlocks);
            }
        } catch (Throwable error) {
            System.err.println("[NewWorld 0.5.58.0] Expanded deposit materializer failed: " + error);
            error.printStackTrace();
        }
    }

    private static Class<?> core() {
        return Navigation0533PhysicalDepositMaterializer.class;
    }

    private static Method privateMethod(String name, Class<?>... parameterTypes) throws Exception {
        Method method = core().getDeclaredMethod(name, parameterTypes);
        method.setAccessible(true);
        return method;
    }

    private static Object invokePrivate(String name, Object... args) throws Exception {
        for (Method method : core().getDeclaredMethods()) {
            if (method.getName().equals(name) && method.getParameterCount() == args.length) {
                method.setAccessible(true);
                return method.invoke(null, args);
            }
        }
        throw new NoSuchMethodException(name);
    }

    private static boolean boolPrivate(String name, Object value) throws Exception {
        return Boolean.TRUE.equals(invokePrivate(name, value));
    }

    private static String stringPrivate(String name, Object value) throws Exception {
        Object result = invokePrivate(name, value);
        return result == null ? "" : String.valueOf(result);
    }

    private static int intPrivate(String name, Object value, Object fieldName) throws Exception {
        Object result = invokePrivate(name, value, fieldName);
        return result instanceof Number number ? number.intValue() : 0;
    }

    private static Object call(Object target, String name, Object... args) throws Exception {
        for (Class<?> type = target.getClass(); type != null; type = type.getSuperclass()) {
            for (Method method : type.getDeclaredMethods()) {
                if (method.getName().equals(name) && method.getParameterCount() == args.length) {
                    method.setAccessible(true);
                    return method.invoke(target, args);
                }
            }
        }
        throw new NoSuchMethodException(target.getClass().getName() + "." + name);
    }

    private static Number number(Object value) {
        return value instanceof Number number ? number : 0L;
    }

    private static int intField(Object target, String name) throws Exception {
        Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return field.getInt(target);
    }

    private static boolean isMarked(Object chunk) {
        try {
            Object data = call(chunk, "getPersistentData");
            if (data == null) {
                return false;
            }
            try {
                if (Boolean.TRUE.equals(call(data, "getBoolean", MARKER))) {
                    return true;
                }
            } catch (Throwable ignored) {
            }
            try {
                Object result = call(data, "getInt", MARKER);
                return result instanceof Number number && number.intValue() != 0;
            } catch (Throwable ignored) {
                return false;
            }
        } catch (Throwable ignored) {
            return false;
        }
    }

    private static void mark(Object chunk) {
        try {
            Object data = call(chunk, "getPersistentData");
            if (data == null) {
                return;
            }
            try {
                call(data, "putBoolean", MARKER, Boolean.TRUE);
            } catch (Throwable firstFailure) {
                try {
                    call(data, "putInt", MARKER, 1);
                } catch (Throwable ignored) {
                }
            }
        } catch (Throwable ignored) {
        }
    }
}
