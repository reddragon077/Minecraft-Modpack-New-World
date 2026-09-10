import java.nio.file.*;
import java.util.*;
import net.newworld.config.NewWorldConfig;
import net.newworld.player.*;
import net.newworld.player.PlayerNavigation0690.Snapshot;

public final class PlayerNavigation0690SmokeTest {
    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("newworld-navigation-test-");
        Path config = root.resolve("player-navigation.properties");
        System.setProperty("newworldcore.configDir", root.toString()); NewWorldConfig.reload();
        try {
            eq(PlayerNavigation0690.refreshTicks(), 20); eq(PlayerNavigation0690.staleTicks(), 120);
            check(PlayerNavigation0690.showCoordinates() && PlayerNavigation0690.showEstimate(), "Display defaults");
            Files.writeString(config, "refresh_ticks=0\nstale_after_ticks=1\nshow_coordinates=false\nshow_we_estimate=false\n"); NewWorldConfig.reload();
            eq(PlayerNavigation0690.refreshTicks(), 20); eq(PlayerNavigation0690.staleTicks(), 40);
            check(!PlayerNavigation0690.showCoordinates() && !PlayerNavigation0690.showEstimate(), "Live display toggles");
            Files.writeString(config, "refresh_ticks=99999\nstale_after_ticks=99999\n"); NewWorldConfig.reload();
            eq(PlayerNavigation0690.refreshTicks(), 1200); eq(PlayerNavigation0690.staleTicks(), 3600);
            Files.writeString(config, "refresh_ticks=bad\nshow_coordinates=bad\n"); NewWorldConfig.reload();
            eq(PlayerNavigation0690.refreshTicks(), 20); check(PlayerNavigation0690.showCoordinates(), "Invalid bool fallback");
            Files.writeString(config, "refresh_ticks=20\nstale_after_ticks=120\n"); NewWorldConfig.reload();
            eq(PlayerNavigation0690.targetDistance("a", "a", 0, 0, 0, 3, 4, 0), "SHIP DIST 5 BLOCKS");
            eq(PlayerNavigation0690.targetDistance("a", "b", 0, 0, 0, 3, 4, 0), "DIFFERENT DIMENSION");
            eq(PlayerNavigation0690.targetDistance("a", "a", -30000000, 0, 0, 30000000, 0, 0), "SHIP DIST 60000000 BLOCKS");
            Snapshot denied = PlayerNavigation0690.sample(new Object());
            check(denied.ship().isBlank() && denied.target().isBlank() && denied.warp().isBlank(), "Unknown owner leaked data");

            Manager manager = new Manager(); Discovery discovery = new Discovery(); Plan plan = new Plan();
            Snapshot empty = PlayerNavigation0690.describe("ship", manager, null, null, "1000");
            eq(empty.target(), "NO TARGET SELECTED"); eq(empty.route(), "NO ROUTE");
            Snapshot loaded = PlayerNavigation0690.describe("ship", manager, discovery, plan, "1000");
            eq(loaded.progress(), "LOADED HOP 2 / 2"); eq(loaded.nextHop(), "X=3 Y=4 Z=0");
            eq(loaded.distance(), "SHIP DIST 5 BLOCKS");
            if (Boolean.getBoolean("newworldcore.navigationEngineTest")) eq(loaded.estimate(), "101 WE / NEXT HOP");
            plan.status = "ROUTE_COMPLETE";
            Snapshot complete = PlayerNavigation0690.describe("ship", manager, discovery, plan, "1000");
            eq(complete.nextHop(), "NONE"); eq(complete.progress(), "COMPLETE / 2 HOPS"); eq(complete.estimate(), "NOT AVAILABLE");
            plan.status = "READY_HOP99_LOADED";
            eq(PlayerNavigation0690.describe("ship", manager, discovery, plan, "1000").nextHop(), "NONE");
            plan.shipId = "other";
            eq(PlayerNavigation0690.describe("ship", manager, discovery, plan, "1000").route(), "NO ROUTE");
            plan.shipId = "ship"; plan.status = "READY_HOP2_LOADED"; manager.destination = new Pos(99, 4, 0);
            eq(PlayerNavigation0690.describe("ship", manager, discovery, plan, "1000").estimate(), "DESTINATION CHANGED");
            discovery.dimension = "minecraft:the_nether";
            eq(PlayerNavigation0690.describe("ship", manager, discovery, null, "1000").distance(), "DIFFERENT DIMENSION");
            discovery.kind = "GEOLOGY"; discovery.analysisLevel = 0; discovery.label = "SECRET-RICH DEPOSIT";
            check(!PlayerNavigation0690.describe("ship", manager, discovery, null, "1000").target().contains("SECRET"), "Unidentified resource leaked");

            Snapshot source = new Snapshot("ship", "LIVE / READ ONLY", "Türkçe test", "minecraft:overworld", "X=-30000000 Y=64 Z=30000000",
                    "SHIP DIST 60000000 BLOCKS", "READY_HOP2_LOADED", "LOADED HOP 2 / 3", "X=3 Y=4 Z=0", "SHIP DIST 5 BLOCKS", "101 WE / NEXT HOP", "1000", 20, 120);
            PlayerNavigation0690.resetClient();
            for (int code : PlayerNavigation0690.encode(source)) {
                check(!PlayerOverview0670.isWireCode(code) && !PlayerShipLink0680.isWireCode(code) && !PlayerDiscoveries0650.isActionMode(code), "Protocol collision");
                check(PlayerNavigation0690.accept(code), "Wire rejected");
            }
            eq(PlayerNavigation0690.clientSnapshot(), source);
            for (int code : PlayerNavigation0690.encode(new Snapshot("ship", "bad timing", "", "", "", "", "", "", "", "", "", "", 0, 120))) PlayerNavigation0690.accept(code);
            eq(PlayerNavigation0690.clientSnapshot(), source);
            PlayerNavigation0690.accept(PlayerNavigation0690.BEGIN); PlayerNavigation0690.accept(PlayerNavigation0690.END);
            eq(PlayerNavigation0690.clientSnapshot(), source);
            PlayerNavigation0690.accept(PlayerNavigation0690.BEGIN);
            for (int i = 0; i < 1500; i++) PlayerNavigation0690.accept(PlayerNavigation0690.WIRE_BASE);
            PlayerNavigation0690.accept(PlayerNavigation0690.END); eq(PlayerNavigation0690.clientSnapshot(), source);
            var link = new PlayerShipLink0680.Snapshot("CONNECTED", "ship", "minecraft:overworld", 1, "IN RANGE", 20, 120);
            long now = 10_000_000_000L;
            check(PlayerNavigation0690.visible(source, link, true, now, now), "Fresh same ship hidden");
            check(!PlayerNavigation0690.visible(source, link, false, now, now), "Lost link leaked");
            check(!PlayerNavigation0690.visible(source, link, true, now, 1), "Stale data leaked");
            check(!PlayerNavigation0690.visible(source, new PlayerShipLink0680.Snapshot("CONNECTED", "other", "a", 1, "IN RANGE", 20, 120), true, now, now), "Ship switch leaked");
            FakeScreen screen = new FakeScreen();
            PlayerNavigation0690.draw(screen, new Graphics(), 0, 0, source, true);
            check(screen.lines.contains("EST 101 WE / NEXT HOP"), "WE label absent");
            Files.writeString(config, "show_coordinates=false\nshow_we_estimate=false\n"); NewWorldConfig.reload();
            screen.lines.clear(); PlayerNavigation0690.draw(screen, new Graphics(), 0, 0, source, true);
            check(screen.lines.contains("COORDINATES HIDDEN") && screen.lines.contains("WE ESTIMATE HIDDEN"), "Display toggle draw failed");
            screen.lines.clear(); PlayerNavigation0690.draw(screen, new Graphics(), 0, 0, source, false);
            check(!screen.lines.contains(source.target()), "Stale draw exposed target");
            PlayerNavigation0690.draw(screen, new Graphics(), 0, 0, denied, true);
            PlayerShipLink0680.resetClient(); check(PlayerNavigation0690.clientSnapshot() == null, "Link reset did not clear Navigation");
            System.out.println("Player Navigation adapter, config, masking, route, bounded wire, link/stale and layout smoke test passed.");
        } finally { Files.deleteIfExists(config); Files.deleteIfExists(root); }
    }
    static void check(boolean value, String msg) { if (!value) throw new AssertionError(msg); }
    static void eq(Object a, Object b) { if (!Objects.equals(a, b)) throw new AssertionError(a + " != " + b); }
    public record Dim(String value) { public String location() { return value; } }
    public record Pos(int x, int y, int z) { public int getX() { return x; } public int getY() { return y; } public int getZ() { return z; } }
    public static final class Manager {
        public Pos destination = new Pos(3, 4, 0);
        public Dim getCurrentExteriorDimension() { return new Dim("minecraft:overworld"); }
        public Dim getDestinationExteriorDimension() { return getCurrentExteriorDimension(); }
        public Pos getCurrentExteriorPosition() { return new Pos(0, 0, 0); }
        public Pos getDestinationExteriorPosition() { return destination; }
    }
    public static final class Discovery { public String label = "CAMP", kind = "STRUCTURE", dimension = "minecraft:overworld"; public int x = 3, y = 4, z, analysisLevel = 1; }
    public static final class Hop { public int x = 3, y = 4, z; }
    public static final class Plan {
        public String shipId = "ship", status = "READY_HOP2_LOADED", dimension = "minecraft:overworld";
        public List<Hop> points = List.of(new Hop(), new Hop());
    }
    public static final class Font { public int width(String s) { return s.length() * 6; } }
    public static final class FakeScreen {
        public Font font = new Font(); public List<String> lines = new ArrayList<>();
        public void text(Object g, String s, int x, int y, int color) {
            check(x >= 26 && x + font.width(s) <= 514 && y >= 91 && y + 9 <= 288, "Panel text bounds: " + s);
            if (y >= 118 && y <= 238 && x < 275) check(x + font.width(s) <= 265, "Column overlap");
            lines.add(s);
        }
    }
    public static final class Graphics {
        public void fill(int x, int y, int x2, int y2, int color) { check(x >= 26 && x2 <= 514 && y >= 110 && y2 <= 248, "Fill bounds"); }
    }
}
