package net.newworld.navigation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.newworld.config.NewWorldTuning;

/** Draws the legacy structure-filter overlay above every radar text layer. */
public final class Navigation0592RadarFilterOverlayFix {
    private Navigation0592RadarFilterOverlayFix() {}

    public static void render(Object screen, Object graphics, int left, int top, Object filterState) throws Exception {
        Object pose = call(graphics, "pose");
        if (pose == null) {
            renderBase(screen, graphics, left, top, filterState);
            return;
        }

        boolean pushed = false;
        try {
            call(pose, "pushPose");
            pushed = true;
            translate(pose, 0.0D, 0.0D, NewWorldTuning.guiFilterOverlayZ());
            renderBase(screen, graphics, left, top, filterState);
        } finally {
            if (pushed) call(pose, "popPose");
        }
    }

    private static void renderBase(Object screen, Object graphics, int left, int top, Object filterState)
            throws Exception {
        Class<?> owner = Class.forName("net.newworld.navigation.Navigation0510RadarDualMode");
        Class<?> stateType = Class.forName("net.newworld.navigation.Navigation0510RadarDualMode$FilterState");
        Method method = owner.getDeclaredMethod("renderFilterOverlay0592Base",
                Object.class, Object.class, int.class, int.class, stateType);
        method.setAccessible(true);
        try {
            method.invoke(null, screen, graphics, left, top, filterState);
        } catch (InvocationTargetException failure) {
            Throwable cause = failure.getCause();
            if (cause instanceof Exception exception) throw exception;
            if (cause instanceof Error error) throw error;
            throw failure;
        }
    }

    private static void translate(Object pose, double x, double y, double z) throws Exception {
        Method method = findMethod(pose.getClass(), "translate", 3);
        Class<?>[] types = method.getParameterTypes();
        method.setAccessible(true);
        method.invoke(pose, number(types[0], x), number(types[1], y), number(types[2], z));
    }

    private static Object call(Object target, String name) throws Exception {
        if (target == null) return null;
        Method method = findMethod(target.getClass(), name, 0);
        method.setAccessible(true);
        return method.invoke(target);
    }

    private static Method findMethod(Class<?> type, String name, int parameters) throws NoSuchMethodException {
        for (Class<?> current = type; current != null; current = current.getSuperclass()) {
            for (Method method : current.getDeclaredMethods()) {
                if (method.getName().equals(name) && method.getParameterCount() == parameters) return method;
            }
        }
        for (Method method : type.getMethods()) {
            if (method.getName().equals(name) && method.getParameterCount() == parameters) return method;
        }
        throw new NoSuchMethodException(type.getName() + '.' + name + '/' + parameters);
    }

    private static Object number(Class<?> type, double value) {
        if (type == float.class || type == Float.class) return (float) value;
        if (type == double.class || type == Double.class) return value;
        if (type == int.class || type == Integer.class) return (int) Math.round(value);
        if (type == long.class || type == Long.class) return Math.round(value);
        if (type == short.class || type == Short.class) return (short) Math.round(value);
        if (type == byte.class || type == Byte.class) return (byte) Math.round(value);
        throw new IllegalArgumentException("Unsupported pose translation number type: " + type.getName());
    }
}
