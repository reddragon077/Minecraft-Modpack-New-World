import java.nio.file.*;
import java.util.*;
import net.newworld.config.NewWorldConfig;
import net.newworld.player.PlayerOverview0670;
import net.newworld.player.PlayerOverview0670.Snapshot;
import net.newworld.player.OverviewEnergyMeter0670;

public final class PlayerOverview0670SmokeTest {
    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("newworld-overview-smoke-");
        System.setProperty("newworldcore.configDir", root.toString());
        Path config = root.resolve("overview.properties");
        try {
            Object pool = new Object();
            eq(OverviewEnergyMeter0670.record(pool, "a", 123, true), 123);
            eq(OverviewEnergyMeter0670.total(pool, "a"), 0);
            eq(OverviewEnergyMeter0670.record(pool, "a", 0, false), 0);
            eq(OverviewEnergyMeter0670.record(pool, "a", 4_000_000_000L, false), 4_000_000_000L);
            eq(OverviewEnergyMeter0670.total(pool, "a"), 4_000_000_000L);
            eq(OverviewEnergyMeter0670.total(pool, "b"), 0);
            eq(OverviewEnergyMeter0670.total(new Object(), "a"), 0);
            eq((long) OverviewEnergyMeter0670.perTick(0, 4_000_000_000L, 40), 100_000_000L);
            eq((long) OverviewEnergyMeter0670.perTick(Long.MAX_VALUE - 5, Long.MIN_VALUE + 4, 2), 5);
            eq(PlayerOverview0670.refreshTicks(), 40);
            eq(PlayerOverview0670.warningPercent(), 20);
            eq(PlayerOverview0670.criticalPercent(), 5);
            eq(PlayerOverview0670.staleTicks(), 120);
            eq(PlayerOverview0670.warningRows(), 2);
            if (!PlayerOverview0670.showResolvedWarnings()) throw new AssertionError("History default");
            PlayerOverview0670.WarningHistory history = new PlayerOverview0670.WarningHistory();
            expect(history.update(Map.of("FE LEVEL LOW", 1)), "[ACTIVE] FE LEVEL LOW");
            expect(history.update(Map.of("FE LEVEL LOW", 2)), "[CRITICAL] FE LEVEL LOW");
            expect(history.update(Map.of()), "[RESOLVED] FE LEVEL LOW");
            expect(history.update(Map.of()), "[RESOLVED] FE LEVEL LOW");
            expect(history.update(Map.of("FE LEVEL LOW", 1)), "[ACTIVE] FE LEVEL LOW");
            expect(history.update(Map.of("MINING NO_ENERGY", 2)), "[CRITICAL] MINING NO_ENERGY", "[RESOLVED] FE LEVEL LOW");
            LinkedHashMap<String, Integer> many = new LinkedHashMap<>();
            many.put("OLD WARNING", 1); many.put("NEW WARNING", 1); many.put("THIRD WARNING", 1); many.put("ENGINE BROKEN", 2);
            List<String> prioritized = history.update(many);
            eq(prioritized.size(), 3);
            if (!prioritized.getFirst().equals("[CRITICAL] ENGINE BROKEN") || prioritized.stream().anyMatch(w -> w.startsWith("[RESOLVED]"))) throw new AssertionError("Critical priority / active starvation");
            expect(new PlayerOverview0670.WarningHistory().update(Map.of()));
            eq(PlayerOverview0670.warningColor("[ACTIVE] FE LEVEL LOW"), 0xFFFFCF45);
            eq(PlayerOverview0670.warningColor("[CRITICAL] FE LEVEL LOW"), 0xFFFF7272);
            eq(PlayerOverview0670.warningColor("[RESOLVED] FE LEVEL LOW"), 0xFF8DA7B4);
            eq(PlayerOverview0670.energySeverity(20, 100), 1);
            eq(PlayerOverview0670.energySeverity(5, 100), 2);
            eq(PlayerOverview0670.energySeverity(0, 0), 1);
            eq(PlayerOverview0670.energySeverity(-1, 100), 1);
            eq(PlayerOverview0670.energySeverity(Long.MAX_VALUE - 1, Long.MAX_VALUE), 0);
            Files.writeString(config, "refresh_ticks=0\nwarning_percent=10\ncritical_percent=90\nstale_after_ticks=0\nwarning_rows=99\n");
            NewWorldConfig.reload();
            eq(PlayerOverview0670.refreshTicks(), 20);
            eq(PlayerOverview0670.criticalPercent(), 10);
            eq(PlayerOverview0670.staleTicks(), 40);
            eq(PlayerOverview0670.warningRows(), 3);
            Files.writeString(config, "refresh_ticks=9999\nwarning_percent=oops\ncritical_percent=-5\nwarning_rows=-1\n");
            NewWorldConfig.reload();
            eq(PlayerOverview0670.refreshTicks(), 1200);
            eq(PlayerOverview0670.staleTicks(), 2400);
            eq(PlayerOverview0670.warningPercent(), 20);
            eq(PlayerOverview0670.criticalPercent(), 0);
            eq(PlayerOverview0670.warningRows(), 1);

            Files.writeString(config, "show_resolved_warnings=false\nwarning_rows=3\n");
            NewWorldConfig.reload();
            List<String> mixed = List.of("[CRITICAL] MINING NO_ENERGY", "[ACTIVE] FE LEVEL LOW", "[RESOLVED] ENGINE UNKNOWN");
            expect(PlayerOverview0670.visibleWarnings(mixed), mixed.get(0), mixed.get(1));
            Files.writeString(config, "show_resolved_warnings=true\nwarning_rows=3\n");
            NewWorldConfig.reload();
            if (!PlayerOverview0670.visibleWarnings(mixed).equals(mixed)) throw new AssertionError("Live resolved visibility reload");

            Snapshot source = new Snapshot("ship-ç-test", Long.MAX_VALUE, Long.MAX_VALUE, 200, 1000,
                    "-100 FE/t NET", "READY", "LOCKED", "OFF", "WAITING_FOR_MODULE", "NO TARGET", "ONLINE", "ONLINE",
                    "ONLINE", "minecraft:overworld", "X=-200 Y=64 Z=500", "WARNING", mixed);
            int[] codes = PlayerOverview0670.encode(source);
            PlayerOverview0670.resetClient();
            if (PlayerOverview0670.accept(4) || PlayerOverview0670.accept(-10000) || PlayerOverview0670.accept(1440001002)) throw new AssertionError("Protocol overlap");
            for (int code : codes) {
                if (!PlayerOverview0670.isWireCode(code) || !PlayerOverview0670.accept(code)) throw new AssertionError("Escaped frame range");
            }
            if (!source.equals(PlayerOverview0670.clientSnapshot())) throw new AssertionError("Lossy snapshot / long / UTF round trip");
            FakeScreen screen = new FakeScreen();
            PlayerOverview0670.draw(screen, new FakeGraphics(), 0, 0, source, true, "WARNING");
            if (screen.drawn < 16) throw new AssertionError("Missing Overview rows");
            eq(screen.colors.get("[CRITICAL] MINING NO ENERGY"), 0xFFFF7272);
            eq(screen.colors.get("[ACTIVE] FE LEVEL LOW"), 0xFFFFCF45);
            eq(screen.colors.get("[RESOLVED] ENGINE UNKNOWN"), 0xFF8DA7B4);
            screen.drawn = 0;
            screen.columns = false;
            PlayerOverview0670.draw(screen, new FakeGraphics(), 0, 0, source, false, "STALE");
            eq(screen.drawn, 3);
            PlayerOverview0670.draw(screen, new FakeGraphics(), 0, 0, Snapshot.unavailable("NO SHIP"), true, "UNAVAILABLE");
            PlayerOverview0670.accept(PlayerOverview0670.BEGIN);
            PlayerOverview0670.accept(PlayerOverview0670.END);
            if (!source.equals(PlayerOverview0670.clientSnapshot())) throw new AssertionError("Partial frame replaced valid snapshot");
            PlayerOverview0670.accept(PlayerOverview0670.BEGIN);
            for (int i = 0; i < 1500; i++) PlayerOverview0670.accept(PlayerOverview0670.WIRE_BASE);
            PlayerOverview0670.accept(PlayerOverview0670.END);
            if (!source.equals(PlayerOverview0670.clientSnapshot())) throw new AssertionError("Oversized frame accepted");
            for (int code : PlayerOverview0670.encode(Snapshot.unavailable("NO SHIP"))) PlayerOverview0670.accept(code);
            if (!PlayerOverview0670.clientSnapshot().ship().isEmpty()) throw new AssertionError("Old ship leaked");
            if (!PlayerOverview0670.amount(-1).equals("?") || !PlayerOverview0670.amount(5_000_000_000L).equals("5.00G")) throw new AssertionError("FE formatting");
            PlayerOverview0670.resetClient();
            if (PlayerOverview0670.clientSnapshot() != null) throw new AssertionError("Disconnect reset");
            for (String reason : List.of("OUT OF RANGE", "DIMENSION LINK DISABLED", "NO OWNED SHIP",
                    "INTERIOR NOT LOADED", "POSITION UNAVAILABLE", "LINK DATA UNAVAILABLE")) {
                var link = new net.newworld.player.PlayerShipLink0680.Snapshot("LOST", "known-ship",
                        "minecraft:overworld", 11770, reason, 20, 120);
                Snapshot unavailable = PlayerOverview0670.unavailableForLink(link);
                expect(unavailable.warnings(), "SHIP LINK LOST // " + reason);
                if (!unavailable.ship().isEmpty() || unavailable.fe() != -1 || unavailable.warp() != -1
                        || !unavailable.health().equals("UNAVAILABLE")) throw new AssertionError("Denied telemetry leaked");
                for (int code : PlayerOverview0670.encode(unavailable)) PlayerOverview0670.accept(code);
                if (!unavailable.equals(PlayerOverview0670.clientSnapshot())) throw new AssertionError("Lost reason wire mismatch");
                FakeScreen deniedScreen = new FakeScreen(); deniedScreen.columns = false;
                PlayerOverview0670.draw(deniedScreen, new FakeGraphics(), 0, 0, unavailable, true, "UNAVAILABLE");
                if (!deniedScreen.colors.containsKey("SHIP LINK LOST // " + reason)) throw new AssertionError("Lost reason missing from GUI");
            }
            expect(PlayerOverview0670.unavailableForLink(null).warnings(), "SHIP LINK LOST // LINK DATA UNAVAILABLE");
            PlayerOverview0670.resetClient();
            System.out.println("Player Overview consumption meter, config, long/UTF protocol, malformed frames and reset smoke test passed.");
        } finally { Files.deleteIfExists(config); Files.deleteIfExists(root); }
    }
    private static void eq(long actual, long expected) { if (actual != expected) throw new AssertionError(actual + " != " + expected); }
    private static void expect(List<String> actual, String... expected) { if (!actual.equals(List.of(expected))) throw new AssertionError("Warning lifecycle: " + actual); }
    public static final class FakeFont { public int width(String text) { return text.length() * 6; } }
    public static final class FakeScreen {
        public final FakeFont font = new FakeFont();
        int drawn;
        boolean columns = true;
        final Map<String, Integer> colors = new HashMap<>();
        public void text(Object graphics, String text, int x, int y, int color) {
            if (x < 26 || x + font.width(text) > 514 || y < 91 || y + 9 > 288) throw new AssertionError("Text outside panel: " + text);
            if (columns && y >= 116 && y <= 200 && x < 275 && x + font.width(text) > 265) throw new AssertionError("Column overlap: " + text);
            drawn++;
            colors.put(text, color);
        }
    }
    public static final class FakeGraphics {
        public void fill(int x, int y, int x2, int y2, int color) {
            if (x < 26 || x2 > 514 || y < 109 || y2 > 209 || x2 <= x || y2 <= y) throw new AssertionError("Panel bounds");
        }
    }
}
