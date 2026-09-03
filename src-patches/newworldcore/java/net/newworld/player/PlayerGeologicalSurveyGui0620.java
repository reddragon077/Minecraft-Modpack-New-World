package net.newworld.player;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/** Enables the reserved Geological Scan card while retaining the legacy screen implementation. */
public final class PlayerGeologicalSurveyGui0620 {
    private PlayerGeologicalSurveyGui0620() {}

    public static boolean mouseClicked(Object screen, double mouseX, double mouseY, int button) {
        try {
            if (PlayerDiscoveries0650.mouseClicked(screen, mouseX, mouseY, button)) return true;
            if (isGeologyButton(screen, mouseX, mouseY)) {
                setField(screen, "status", "SCANNING // Geological survey in progress...");
                sendSurveyMode(1);
                return true;
            }
            Object result = call(screen, "mouseClicked0620Base", mouseX, mouseY, button);
            return result instanceof Boolean handled && handled;
        } catch (Throwable failure) {
            System.err.println("[NewWorldCore] Geological Survey GUI dispatch failed: " + failure);
            try { setField(screen, "status", "Geological scan failed // check log."); }
            catch (Throwable ignored) {}
            return true;
        }
    }

    public static boolean isGeologyButton(Object screen, double mouseX, double mouseY) {
        if (screen == null) return false;
        try {
            if (intField(screen, "tab") != 1) return false;
            int left = (intField(screen, "width") - 540) / 2;
            int top = (intField(screen, "height") - 300) / 2;
            int x = left + 266;
            int y = top + 158;
            return mouseX >= x && mouseX < x + 220 && mouseY >= y && mouseY < y + 42;
        } catch (Throwable ignored) {
            return false;
        }
    }

    public static void sendSurveyMode(int mode) throws Exception {
        Class<?> payloadType = Class.forName("net.newworld.player.PlayerFieldSurveyPayload");
        Constructor<?> constructor = payloadType.getConstructor(int.class);
        Object payload = constructor.newInstance(mode);
        Class<?> distributor = Class.forName("net.neoforged.neoforge.network.PacketDistributor");
        for (Method method : distributor.getMethods()) {
            if (!method.getName().equals("sendToServer") || !Modifier.isStatic(method.getModifiers())) continue;
            Class<?>[] parameters = method.getParameterTypes();
            if (parameters.length == 1 && parameters[0].isInstance(payload)) {
                method.invoke(null, payload);
                return;
            }
            if (parameters.length == 2 && parameters[0].isInstance(payload) && parameters[1].isArray()) {
                Object empty = Array.newInstance(parameters[1].getComponentType(), 0);
                method.invoke(null, payload, empty);
                return;
            }
        }
        throw new NoSuchMethodException("PacketDistributor.sendToServer");
    }

    private static Object call(Object target, String name, Object... args) throws Exception {
        Method method = findMethod(target.getClass(), name, args);
        method.setAccessible(true);
        return method.invoke(target, args);
    }

    private static Method findMethod(Class<?> type, String name, Object[] args) throws NoSuchMethodException {
        for (Class<?> current = type; current != null; current = current.getSuperclass()) {
            for (Method method : current.getDeclaredMethods()) {
                if (!method.getName().equals(name) || method.getParameterCount() != args.length) continue;
                Class<?>[] parameters = method.getParameterTypes();
                boolean match = true;
                for (int i = 0; i < parameters.length; i++) {
                    if (!compatible(parameters[i], args[i])) { match = false; break; }
                }
                if (match) return method;
            }
        }
        throw new NoSuchMethodException(type.getName() + '.' + name);
    }

    private static boolean compatible(Class<?> parameter, Object value) {
        if (value == null) return !parameter.isPrimitive();
        if (parameter == double.class) return value instanceof Double;
        if (parameter == int.class) return value instanceof Integer;
        return parameter.isInstance(value);
    }

    private static int intField(Object target, String name) throws Exception {
        return ((Number) findField(target.getClass(), name).get(target)).intValue();
    }

    private static void setField(Object target, String name, Object value) throws Exception {
        findField(target.getClass(), name).set(target, value);
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
}
