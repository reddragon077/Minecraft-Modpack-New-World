import java.nio.file.*;
import java.util.*;
import java.lang.reflect.*;
import net.newworld.config.NewWorldConfig;
import net.newworld.player.*;

public final class PlayerNavigationSend0693SmokeTest {
    static void check(boolean ok, String message) { if (!ok) throw new AssertionError(message); }
    static void eq(Object a, Object b) { check(Objects.equals(a, b), a + " != " + b); }
    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("newworld-send-test-");
        Path config = root.resolve("player-navigation.properties"), discovery = root.resolve("gui.properties");
        System.setProperty("newworldcore.configDir", root.toString()); NewWorldConfig.reload();
        try {
            check(PlayerNavigationSend0693.enabled() && PlayerNavigationSend0693.clientEnabled(), "Default enabled");
            eq(PlayerNavigationSend0693.cooldown(), 40);
            Files.writeString(config, "send_to_ship.enabled=false\nsend_to_ship.cooldown_ticks=1\n"); NewWorldConfig.reload();
            check(!PlayerNavigationSend0693.clientEnabled(), "Live disable"); eq(PlayerNavigationSend0693.cooldown(), 20);
            Files.writeString(config, "send_to_ship.cooldown_ticks=999999\n"); NewWorldConfig.reload(); eq(PlayerNavigationSend0693.cooldown(), 1200);
            Files.writeString(config, "send_to_ship.enabled=bad\nsend_to_ship.cooldown_ticks=bad\n"); NewWorldConfig.reload();
            check(PlayerNavigationSend0693.enabled(), "Invalid fallback"); eq(PlayerNavigationSend0693.cooldown(), 40);
            Files.writeString(config, ""); Files.writeString(discovery, "player.discoveries.enable_target_action=false\n"); NewWorldConfig.reload();
            check(!PlayerNavigationSend0693.clientEnabled(), "Shared target permission bypass");
            Files.writeString(discovery, ""); NewWorldConfig.reload();
            long now = 10_000_000_000L;
            eq(PlayerNavigationSend0693.gate(true, true, true, true, now, null, 40), PlayerNavigationSend0693.SENT);
            for (int missing = 0; missing < 4; missing++)
                eq(PlayerNavigationSend0693.gate(missing != 0, missing != 1, missing != 2, missing != 3, now, null, 40), PlayerDiscoveries0650.STATUS_ACTION_DISABLED);
            eq(PlayerNavigationSend0693.gate(true, true, true, true, now, now - 1_999_999_999L, 40), PlayerNavigationSend0693.WAIT);
            eq(PlayerNavigationSend0693.gate(true, true, true, true, now, now - 2_000_000_000L, 40), PlayerNavigationSend0693.SENT);
            Object player = new Object();
            eq(PlayerNavigationSend0693.claim(player, true, false), PlayerDiscoveries0650.STATUS_ACTION_DISABLED);
            eq(PlayerNavigationSend0693.claim(player, true, true), PlayerNavigationSend0693.SENT);
            // Refresh does not erase the per-player cooldown; another player is independent.
            PlayerDiscoveries0650.clearServerSelection(player);
            eq(PlayerNavigationSend0693.claim(player, true, true), PlayerNavigationSend0693.WAIT);
            eq(PlayerNavigationSend0693.claim(new Object(), true, true), PlayerNavigationSend0693.SENT);
            verifySharedWriter();
            verifyPicker();
            System.out.println("Navigation SEND TO SHIP config, permission, cooldown, shared writer/route preservation and GUI smoke test passed.");
        } finally {
            PlayerDiscoveries0650.resetClientLink();
            Files.deleteIfExists(config); Files.deleteIfExists(discovery); Files.deleteIfExists(root);
        }
    }

    static void verifySharedWriter() throws Exception {
        Data data = new Data(); State state = new State();
        String key = "minecraft:overworld|-2454|63|189";
        Class<?> selection = Class.forName("net.newworld.player.PlayerDiscoveries0650$ServerSelection");
        Constructor<?> ctor = selection.getDeclaredConstructors()[0]; ctor.setAccessible(true);
        Object selected = ctor.newInstance(new Object(), data, state, "ship", key, new Object());
        Method writer = PlayerDiscoveries0650.class.getDeclaredMethod("selectTarget", selection); writer.setAccessible(true);
        Object route = state.route;
        writer.invoke(null, selected); eq(state.selectedKey, key); eq(data.dirty, 1);
        check(state.route == route && state.warp == 1000 && !state.flight, "Target changed route/energy/flight");
        writer.invoke(null, selected); eq(state.selectedKey, key);
        check(state.route == route, "Repeat changed route");
    }

    static void verifyPicker() throws Exception {
        var client = PlayerDiscoveries0650.class.getDeclaredField("CLIENT"); client.setAccessible(true);
        @SuppressWarnings("unchecked") var entries = (List<PlayerDiscoveries0650.DiscoveryView>) client.get(null);
        entries.clear(); entries.add(new PlayerDiscoveries0650.DiscoveryView(0, "LOCATION -2454 63 189", "WAYPOINT",
                "minecraft:overworld", "MANUAL", "", -2454, 63, 189, 0, 0, 0, 0, 3));
        var field = PlayerDiscoveries0650.class.getDeclaredField("VIEWS"); field.setAccessible(true);
        @SuppressWarnings("unchecked") var views = (Map<Object,Object>) field.get(null);
        Class<?> view = Class.forName("net.newworld.player.PlayerDiscoveries0650$ViewState");
        var ctor = view.getDeclaredConstructor(); ctor.setAccessible(true); Object state = ctor.newInstance();
        var picker = view.getDeclaredField("favoritesPicker"); picker.setAccessible(true); picker.set(state, true);
        var filter = view.getDeclaredField("filter"); filter.setAccessible(true); filter.set(state, 3);
        var screen = new PlayerDiscoveries0650SmokeTest.FakeScreen(); views.put(screen, state);
        Method render = PlayerDiscoveries0650.class.getDeclaredMethod("renderDiscoveries", Object.class, Object.class, int.class, int.class);
        render.setAccessible(true);
        for (int code : new int[] { PlayerNavigationSend0693.SENT, PlayerNavigationSend0693.WAIT, PlayerDiscoveries0650.STATUS_ACTION_DISABLED }) {
            check(!PlayerNavigation0690.isWireCode(code) && !PlayerShipLink0680.isWireCode(code)
                    && !PlayerOverview0670.isWireCode(code) && !PlayerDiscoveries0650.isActionMode(code)
                    && !PlayerLocation0692.accept(code), "Status protocol collision");
            check(PlayerDiscoveries0650.accept(code), "Status not routed");
            screen.lines.clear(); render.invoke(null, screen, new PlayerDiscoveries0650SmokeTest.Graphics(), 0, 0);
            check(screen.lines.contains("SEND TO SHIP") && !screen.lines.contains("TARGET") && !screen.lines.contains("ROUTE"), "Picker controls");
            check(screen.lines.contains(code == PlayerNavigationSend0693.SENT ? "SENT TO SHIP" : code == PlayerNavigationSend0693.WAIT ? "SEND WAIT" : "ACTION DISABLED"), "Acknowledgement missing");
        }
        picker.set(state, false); filter.set(state, 0); screen.lines.clear();
        render.invoke(null, screen, new PlayerDiscoveries0650SmokeTest.Graphics(), 0, 0);
        check(screen.lines.contains("TARGET") && screen.lines.contains("ROUTE") && !screen.lines.contains("SEND TO SHIP"), "Discoveries controls changed");
        PlayerDiscoveries0650.resetClientLink();
        var status = PlayerDiscoveries0650.class.getDeclaredField("actionStatus"); status.setAccessible(true);
        eq(status.get(null), "");
    }
    public static final class Data { public int dirty; public void setDirty() { dirty++; } }
    public static final class State { public String selectedKey = "old"; public final Object route = new Object(); public long warp = 1000; public boolean flight; }
}
