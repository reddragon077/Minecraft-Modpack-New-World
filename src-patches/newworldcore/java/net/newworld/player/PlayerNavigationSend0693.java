package net.newworld.player;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import net.newworld.config.NewWorldConfig;
import net.newworld.config.NewWorldTuning;

/** Policy for the favorite picker's existing TARGET action; never invokes the route/flight engine. */
public final class PlayerNavigationSend0693 {
    public static final int SENT = 1_440_002_030, WAIT = SENT + 1;
    private static final Map<Object, Long> SENDS = Collections.synchronizedMap(new WeakHashMap<>());
    private PlayerNavigationSend0693() {}

    public static boolean enabled() { return NewWorldConfig.bool("player-navigation", "send_to_ship.enabled", true); }
    public static boolean clientEnabled() { return enabled() && NewWorldTuning.playerDiscoveriesTargetEnabled(); }
    public static int cooldown() { return NewWorldConfig.integer("player-navigation", "send_to_ship.cooldown_ticks", 40, 20, 1200); }

    public static int gate(boolean enabled, boolean targetEnabled, boolean favoritePicker,
                           boolean favorite, long now, Long last, int ticks) {
        if (!enabled || !targetEnabled || !favoritePicker || !favorite)
            return PlayerDiscoveries0650.STATUS_ACTION_DISABLED;
        if (last != null && now - last < ticks * 50_000_000L) return WAIT;
        return SENT;
    }

    /** Called only after existing server owner/link, same-ship snapshot and favorite checks. */
    public static int claim(Object player, boolean favoritePicker, boolean favorite) {
        synchronized (SENDS) {
            long now = System.nanoTime();
            int result = gate(enabled(), NewWorldTuning.playerDiscoveriesTargetEnabled(),
                    favoritePicker, favorite, now, SENDS.get(player), cooldown());
            if (result == SENT) SENDS.put(player, now);
            return result;
        }
    }
}
