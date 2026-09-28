package net.newworld.player;

import java.util.*;
import net.newworld.config.NewWorldConfig;
import static net.newworld.player.PlayerOverview0670.*;

/** Server-owned coordinate bookmark; reuses Discovery persistence, never selects or routes. */
public final class PlayerLocation0692 {
    public static final int SAVED = 1_440_002_020, DISABLED = SAVED + 1, WAIT = SAVED + 2,
            FULL = SAVED + 3, INTERIOR = SAVED + 4, FAILED = SAVED + 5;
    private static final Map<Object, Long> SAVES = Collections.synchronizedMap(new WeakHashMap<>());
    private static String status = "";
    private PlayerLocation0692() {}
    public static boolean enabled() { return NewWorldConfig.bool("player-navigation", "location.enabled", true); }
    public static int limit() { return NewWorldConfig.integer("player-navigation", "location.max_per_ship", 128, 1, 1024); }
    public static int cooldown() { return NewWorldConfig.integer("player-navigation", "location.cooldown_ticks", 40, 20, 1200); }
    public static String normalizeKind(String value) {
        return "WAYPOINT".equalsIgnoreCase(value) ? "WAYPOINT" : "GEOLOGY".equalsIgnoreCase(value) ? "GEOLOGY" : "STRUCTURE";
    }
    public static String normalizeSource(String value) {
        return "MANUAL".equalsIgnoreCase(value) ? "MANUAL" : "FIELD".equalsIgnoreCase(value) ? "FIELD" : "RADAR";
    }
    public static void resetClient() { status = ""; }
    public static String status() { return status; }
    public static void beginClient() { status = "SAVING LOCATION..."; }
    public static boolean accept(int code) {
        String next = switch (code) {
            case SAVED -> "LOCATION SAVED // OPEN FAVORITES";
            case DISABLED -> "LOCATION SAVE DISABLED";
            case WAIT -> "PLEASE WAIT BEFORE SAVING AGAIN";
            case FULL -> "LOCATION LIMIT REACHED // CHECK CONFIG";
            case INTERIOR -> "EXIT THE SHIP TO SAVE YOUR LOCATION";
            case FAILED -> "LOCATION SAVE FAILED // CHECK LOG";
            default -> null;
        };
        if (next == null) return false;
        status = next; return true;
    }
    public static int gate(boolean enabled, boolean linked, boolean inside, long now, Long last, int ticks) {
        if (!enabled) return DISABLED;
        if (!linked) return FAILED;
        if (inside) return INTERIOR;
        if (last != null && now - last < ticks * 50_000_000L) return WAIT;
        return SAVED;
    }
    public static void save(Object player) {
        try {
            var link = PlayerShipLink0680.resolve(player);
            Object level = call(player, "serverLevel");
            Object interior = link.ship() == null ? null : call(link.ship().manager(), "getWorld");
            Object interiorManager = stat("net.drgmes.dwm.common.tardis.TardisStateManager", "get", level);
            boolean inside = level == interior || interiorManager instanceof Optional<?> optional && optional.isPresent();
            long now = System.nanoTime();
            int result = gate(enabled(), link.snapshot().allowed() && interior != null,
                    inside, now, SAVES.get(player), cooldown());
            if (result == SAVED) {
                SAVES.put(player, now);
                Object pos = call(player, "blockPosition");
                String dimension = String.valueOf(call(call(level, "dimension"), "location"));
                Object record = Class.forName("net.newworld.navigation.NavigationDiscoverySavedData$Discovery").getConstructor().newInstance();
                initialize(record, dimension, ((Number) call(pos, "getX")).intValue(),
                        ((Number) call(pos, "getY")).intValue(), ((Number) call(pos, "getZ")).intValue(), System.currentTimeMillis());
                Object data = stat("net.newworld.navigation.NavigationDiscoverySavedData", "get", interior);
                result = persist(data, link.ship().id(), record, limit());
                if (result == SAVED) stat("net.newworld.navigation.NavigationFavoriteOps", "invalidate", data, link.ship().id());
                System.out.println("[NewWorld Player Location] result=" + result + " ship=" + link.ship().id() + " key=" + call(record, "key"));
            }
            stat("net.newworld.player.PlayerFieldSurvey0504Bridge", "sendResult", player, result);
        } catch (Exception failure) {
            System.err.println("[NewWorld Player Location] save failed: " + failure);
            try { stat("net.newworld.player.PlayerFieldSurvey0504Bridge", "sendResult", player, FAILED); } catch (Exception ignored) {}
        }
    }
    public static void initialize(Object record, String dimension, int x, int y, int z, long now) throws Exception {
        set(record, "label", "LOCATION " + x + " " + y + " " + z);
        set(record, "clazz", "SAVED LOCATION"); set(record, "kind", "WAYPOINT"); set(record, "source", "MANUAL");
        set(record, "dimension", dimension); set(record, "x", x); set(record, "y", y); set(record, "z", z);
        set(record, "discoveredAt", now); set(record, "lastSeenAt", now);
        set(record, "favorite", true); set(record, "visited", true); set(record, "analysisLevel", 0);
    }
    /** Exact coordinate collisions reuse the original record without replacing evidence or selecting it. */
    public static int persist(Object data, String ship, Object incoming, int limit) throws Exception {
        Object state = call(data, "state", ship);
        Map<?, ?> records = (Map<?, ?>) field(state, "discoveries");
        Object existing = records.get(call(incoming, "key"));
        if (existing != null) {
            set(existing, "favorite", true); call(data, "setDirty"); return SAVED;
        }
        long count = 0;
        for (Object record : records.values()) if ("WAYPOINT".equals(field(record, "kind"))) count++;
        if (count >= limit) return FULL;
        call(data, "record", ship, incoming);
        return SAVED;
    }
    private static void set(Object object, String name, Object value) throws Exception {
        var f = object.getClass().getDeclaredField(name); f.setAccessible(true); f.set(object, value);
    }
}
