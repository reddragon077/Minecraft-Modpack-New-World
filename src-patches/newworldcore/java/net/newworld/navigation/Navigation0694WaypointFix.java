package net.newworld.navigation;

import java.lang.reflect.*;
import java.util.Objects;

/** Adapters for the existing terminal/route engine. Never mutate a discovery or initiate flight. */
public final class Navigation0694WaypointFix {
    private static final ThreadLocal<Object> EXACT_PLAN = new ThreadLocal<>();
    private Navigation0694WaypointFix() {}
    public static boolean manual(Object record) {
        try { return "WAYPOINT".equals(field(record, "kind")) && "MANUAL".equals(field(record, "source")); }
        catch (Exception ignored) { return false; }
    }
    public static int displayY(Object level, String dimension, int x, int z, int y, Object record) throws Exception {
        if (manual(record)) return y;
        return ((Number) invoke(Class.forName("net.newworld.navigation.Navigation0471gFix"), null,
                "resolveLandingY", level, dimension, x, z, y)).intValue();
    }
    /** Existing surface policy remains intact except for the exact manual destination. */
    public static int routeY(Object level, int x, int z, int fallback, Object record) throws Exception {
        if (manual(record) && number(record, "x") == x && number(record, "z") == z) return number(record, "y");
        return ((Number) invoke(Navigation0472ServerRoute.class, null, "surfaceY", level, x, z, fallback)).intValue();
    }
    public static int hopCount(int count, Object record) { return manual(record) ? Math.max(1, count) : count; }

    /** Scoped to the fresh calculation; the existing loader still applies range and flight gates. */
    public static void loadFirstHop(Object context, Navigation0472ServerRoute.Plan plan, Object record) throws Exception {
        Object previous = EXACT_PLAN.get();
        try {
            if (manual(record)) EXACT_PLAN.set(plan); else EXACT_PLAN.remove();
            invoke(Class.forName("net.newworld.navigation.Navigation0473HopAutopilot"), null, "loadFirstHop", context, plan);
        } finally { if (previous == null) EXACT_PLAN.remove(); else EXACT_PLAN.set(previous); }
    }
    public static void normalizeHopAltitudes(Navigation0472ServerRoute.Plan plan) throws Exception {
        Navigation0472ServerRoute.Hop exact = EXACT_PLAN.get() == plan && !plan.points.isEmpty()
                ? plan.points.get(plan.points.size() - 1) : null;
        invoke(Class.forName("net.newworld.navigation.Navigation0473HopAutopilot"), null, "normalizeHopAltitudes", plan);
        if (exact != null) { plan.points.set(plan.points.size() - 1, exact); plan.endY = exact.y; }
    }

    /** Negative values are presentation sentinels, never persisted as discovery distance. */
    public static int terminalDistance(Object record, String ignoredField, int ignoredFallback, Object level) {
        try {
            Object manager = invoke(Class.forName("net.newworld.navigation.Navigation0471gFix"), null, "tardisManager", level);
            return distance(record, manager);
        } catch (Exception ignored) { return -2; }
    }
    public static int distance(Object record, Object manager) {
        try {
            Object dimension = call(manager, "getCurrentExteriorDimension");
            String shipDimension = Objects.toString(call(dimension, "location"));
            if (!shipDimension.equals(field(record, "dimension"))) return -1;
            Object pos = call(manager, "getCurrentExteriorPosition");
            double dx = (double) number(record, "x") - ((Number) call(pos, "getX")).intValue();
            double dy = (double) number(record, "y") - ((Number) call(pos, "getY")).intValue();
            double dz = (double) number(record, "z") - ((Number) call(pos, "getZ")).intValue();
            return (int) Math.min(Integer.MAX_VALUE, Math.round(Math.sqrt(dx * dx + dy * dy + dz * dz)));
        } catch (Exception ignored) { return -2; }
    }
    public static String distanceText(String encoded) {
        try {
            int distance = Integer.parseInt(encoded);
            return distance == -1 ? "DIFFERENT DIMENSION" : distance < 0 ? "DISTANCE UNAVAILABLE" : distance + " BLOCKS";
        } catch (RuntimeException ignored) { return "DISTANCE UNAVAILABLE"; }
    }
    private static int number(Object o, String name) throws Exception { return ((Number) field(o, name)).intValue(); }
    private static Object field(Object o, String name) throws Exception {
        for (Class<?> c = o.getClass(); c != null; c = c.getSuperclass()) try {
            Field f = c.getDeclaredField(name); f.setAccessible(true); return f.get(o);
        } catch (NoSuchFieldException ignored) {}
        throw new NoSuchFieldException(name);
    }
    private static Object call(Object o, String name) throws Exception { return invoke(o.getClass(), o, name); }
    private static Object invoke(Class<?> type, Object target, String name, Object... args) throws Exception {
        for (Class<?> c = type; c != null; c = c.getSuperclass()) for (Method m : c.getDeclaredMethods()) {
            if (!m.getName().equals(name) || m.getParameterCount() != args.length
                    || Modifier.isStatic(m.getModifiers()) != (target == null)) continue;
            boolean compatible = true;
            for (int i = 0; i < args.length; i++) {
                Class<?> p = m.getParameterTypes()[i]; if (p == int.class) p = Integer.class;
                if (args[i] == null ? p.isPrimitive() : !p.isInstance(args[i])) compatible = false;
            }
            if (compatible) { m.setAccessible(true); return m.invoke(target, args); }
        }
        throw new NoSuchMethodException(type.getName() + '.' + name);
    }
}
