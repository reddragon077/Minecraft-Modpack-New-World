package net.newworld.player;

import java.lang.reflect.Field;

/** Client status text for both Structure and Geological Field Survey modes. */
public final class PlayerFieldSurvey0620ClientStatus {
    private PlayerFieldSurvey0620ClientStatus() {}

    public static void update(int code) {
        try {
            String text;
            if (code >= 2000 && code < 3000) {
                int count = code - 2000;
                text = "Identified " + count + (count == 1 ? " physical deposit." : " physical deposits.")
                        + " Discovery Database updated.";
            } else if (code >= 1000 && code < 2000) {
                int count = code - 1000;
                text = "Identified " + count + (count == 1 ? " structure." : " structures.")
                        + " Discovery Database updated.";
            } else {
                text = switch (code) {
                    case 100, 200 -> "No active world link.";
                    case 101, 201 -> "SHIP LINK LOST // No owned TARDIS resolved.";
                    case 102 -> "No structures detected within the configured range.";
                    case 202 -> "No physical deposits verified within the configured range.";
                    case 104, 204 -> "Scan failed // check server log.";
                    default -> "Survey response received.";
                };
            }

            Class<?> minecraftType = Class.forName("net.minecraft.client.Minecraft");
            Object minecraft = minecraftType.getMethod("getInstance").invoke(null);
            Object screen = minecraftType.getField("screen").get(minecraft);
            if (screen == null || !screen.getClass().getName().equals("net.newworld.player.PlayerShipScreen")) return;
            Field status = findField(screen.getClass(), "status");
            status.setAccessible(true);
            status.set(screen, text);
        } catch (Throwable failure) {
            System.err.println("[NewWorldCore] client survey status update failed: " + failure);
        }
    }

    private static Field findField(Class<?> type, String name) throws NoSuchFieldException {
        for (Class<?> current = type; current != null; current = current.getSuperclass()) {
            try { return current.getDeclaredField(name); }
            catch (NoSuchFieldException ignored) {}
        }
        throw new NoSuchFieldException(name);
    }
}
