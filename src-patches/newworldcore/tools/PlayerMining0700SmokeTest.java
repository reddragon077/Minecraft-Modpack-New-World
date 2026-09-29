import java.nio.file.*;
import java.util.*;
import net.newworld.config.NewWorldConfig;
import net.newworld.player.*;
import net.newworld.player.PlayerMining0700.Snapshot;
import org.objectweb.asm.*;

public final class PlayerMining0700SmokeTest {
    static void check(boolean b, String message) { if (!b) throw new AssertionError(message); }
    static void eq(Object a, Object b) { check(Objects.equals(a, b), a + " != " + b); }
    static Object state(String name) throws Exception { return Class.forName("net.newworld.mining." + name + "$State").getConstructor().newInstance(); }
    static void set(Object o, String f, Object v) throws Exception { o.getClass().getField(f).set(o, v); }
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("newworld-mining-test-"); Path config = root.resolve("player-mining.properties");
        System.setProperty("newworldcore.configDir", root.toString()); NewWorldConfig.reload();
        try {
            eq(PlayerMining0700.refreshTicks(), 20); eq(PlayerMining0700.staleTicks(), 120);
            check(PlayerMining0700.showArea() && PlayerMining0700.showBuffers(), "Display defaults");
            Files.writeString(config, "refresh_ticks=0\nstale_after_ticks=1\nshow_scan_area=false\nshow_buffers=false\n"); NewWorldConfig.reload();
            eq(PlayerMining0700.refreshTicks(), 20); eq(PlayerMining0700.staleTicks(), 40);
            check(!PlayerMining0700.showArea() && !PlayerMining0700.showBuffers(), "Live display toggles");
            Files.writeString(config, "refresh_ticks=99999\nstale_after_ticks=99999\n"); NewWorldConfig.reload();
            eq(PlayerMining0700.refreshTicks(), 1200); eq(PlayerMining0700.staleTicks(), 3600);
            Files.writeString(config, "refresh_ticks=bad\nshow_buffers=bad\n"); NewWorldConfig.reload();
            eq(PlayerMining0700.refreshTicks(), 20); check(PlayerMining0700.showBuffers(), "Invalid fallback");
            Files.writeString(config, "refresh_ticks=1200\nstale_after_ticks=40\n"); NewWorldConfig.reload(); eq(PlayerMining0700.staleTicks(), 2400);
            Files.writeString(config, ""); NewWorldConfig.reload();
            eq(PlayerMining0700.progress(1, 2), "50.0% / 1 OF 2");
            eq(PlayerMining0700.progress(0, 0), "N/A"); eq(PlayerMining0700.progress(-1, 10), "N/A");
            check(PlayerMining0700.progress(Long.MAX_VALUE, Long.MAX_VALUE).startsWith("100.0%"), "Large progress overflow");
            check(PlayerMining0700.progress(11, 10).startsWith("100.0%"), "Progress clamp");
            eq(PlayerMining0700.add(Long.MAX_VALUE, 1), Long.MAX_VALUE); eq(PlayerMining0700.add(1, -9), 1L);
            eq(PlayerMining0700.bufferText(0, 0, 256), "0 ITEMS / 0/256 TYPES");
            check(PlayerMining0700.loaded(new LoadedWorld(), new Pos(0, 0, 0)), "Inherited LevelReader default ignored");
            check(!PlayerMining0700.loaded(new LoadedWorld(), new Pos(17, 0, 0)), "Unloaded chunk allowed");
            check(PlayerMining0700.sample(new Object()).ship().isBlank(), "Unowned data leak");
            Data data = new Data(); eq(PlayerMining0700.state(data, "absent"), null); check(data.states.isEmpty(), "Read created state");
            Object runtime = state("MiningRuntimeSavedData"), phase = state("MiningPhaseSavedData"), routing = state("MiningRoutingSavedData");
            eq(PlayerMining0700.buffer(null, runtime, "bufferPos", "COLLECTION_ID"), "NOT BOUND");
            eq(PlayerMining0700.buffer(null, null, "bufferPos", "COLLECTION_ID"), "NOT BOUND");
            Manager manager = new Manager();
            Snapshot empty = PlayerMining0700.describe("ship", manager, null, null, null, "NOT BOUND", "NOT BOUND", "NOT BOUND");
            eq(empty.area(), "NO SCAN AREA"); eq(empty.scan(), "N/A"); eq(empty.routing(), "ROUTING UNKNOWN");
            set(phase, "exteriorDimension", "minecraft:overworld"); set(phase, "chunkX", -1); set(phase, "chunkZ", -1);
            set(phase, "phase", 1); set(phase, "scanCursor", 50L); set(phase, "totalBlocks", 100L);
            set(phase, "minedTargets", 5L); set(phase, "hazardsFound", 100L); set(routing, "mode", 1);
            Map<String, Long> found = (Map<String, Long>) phase.getClass().getField("found").get(phase); found.put("minecraft:iron_ore", 10L);
            Snapshot scanning = PlayerMining0700.describe("ship", manager, runtime, phase, routing, "0 ITEMS / 0/256 TYPES", "MISSING", "UNLOADED");
            eq(scanning.phase(), "SCANNING"); eq(scanning.area(), "SCAN CENTER CH -1, -1"); eq(scanning.scan(), "50.0% / 50 OF 100");
            eq(scanning.extraction(), "N/A / SCANNING"); eq(scanning.routing(), "SMART AUTO");
            set(phase, "phase", 2);
            Snapshot source = PlayerMining0700.describe("ship", manager, runtime, phase, routing, scanning.collection(), "MISSING", "UNLOADED");
            eq(source.extraction(), "50.0% / 5 OF 10"); // Hazards must not inflate the resource denominator.
            eq(found.size(), 1); eq(phase.getClass().getField("minedTargets").getLong(phase), 5L);
            manager.pos = new Pos(17, 64, 17);
            eq(PlayerMining0700.describe("ship", manager, runtime, phase, routing, "", "", "").area(), "LAST SCAN CH -1, -1");
            set(routing, "mode", 0); eq(PlayerMining0700.describe("ship", manager, runtime, phase, routing, "", "", "").routing(), "ROUTING PAUSED");
            set(routing, "mode", 2); eq(PlayerMining0700.describe("ship", manager, runtime, phase, routing, "", "", "").routing(), "FORCE REPLICATION");
            set(phase, "phase", 3); eq(PlayerMining0700.describe("ship", manager, runtime, phase, routing, "", "", "").phase(), "WAITING RESEARCH");
            PlayerMining0700.resetClient();
            for (int code : PlayerMining0700.encode(source)) {
                check(!PlayerNavigation0690.isWireCode(code) && !PlayerOverview0670.isWireCode(code) && !PlayerShipLink0680.isWireCode(code)
                        && !PlayerDiscoveries0650.isActionMode(code), "Wire collision");
                check(PlayerMining0700.accept(code), "Wire rejected");
            }
            eq(PlayerMining0700.clientSnapshot(), source);
            PlayerMining0700.accept(PlayerMining0700.BEGIN); PlayerMining0700.accept(PlayerMining0700.END); eq(PlayerMining0700.clientSnapshot(), source);
            PlayerMining0700.accept(PlayerMining0700.BEGIN);
            for (int i = 0; i < 1500; i++) PlayerMining0700.accept(PlayerMining0700.WIRE_BASE);
            PlayerMining0700.accept(PlayerMining0700.END); eq(PlayerMining0700.clientSnapshot(), source);
            Snapshot bad = new Snapshot("ship", "bad", "", "", "", "", "", "", "", "", "", "", "", 0, 120);
            for (int code : PlayerMining0700.encode(bad)) PlayerMining0700.accept(code); eq(PlayerMining0700.clientSnapshot(), source);
            var link = new PlayerShipLink0680.Snapshot("CONNECTED", "ship", "minecraft:overworld", 1, "IN RANGE", 20, 120);
            long now = 10_000_000_000L;
            check(PlayerMining0700.visible(source, link, true, now, now), "Fresh data hidden");
            check(!PlayerMining0700.visible(source, link, false, now, now), "Link loss leak");
            check(!PlayerMining0700.visible(source, link, true, now, 1), "Stale leak");
            var other = new PlayerShipLink0680.Snapshot("CONNECTED", "other", "minecraft:overworld", 1, "IN RANGE", 20, 120);
            check(!PlayerMining0700.visible(source, other, true, now, now), "Other ship leak");
            var screen = new PlayerNavigation0690SmokeTest.FakeScreen(); var graphics = new PlayerNavigation0690SmokeTest.Graphics();
            PlayerMining0700.draw(screen, graphics, 0, 0, source, true);
            check(screen.lines.contains("SMART AUTO") && screen.lines.contains("MISSING") && screen.lines.contains("UNLOADED"), "Missing rendered facts");
            Files.writeString(config, "show_scan_area=false\nshow_buffers=false\n"); NewWorldConfig.reload();
            screen.lines.clear(); PlayerMining0700.draw(screen, graphics, 0, 0, source, true);
            check(screen.lines.contains("SCAN AREA HIDDEN") && screen.lines.contains("BUFFER COUNTS HIDDEN") && !screen.lines.contains(source.collection()), "Display privacy");
            screen.lines.clear(); PlayerMining0700.draw(screen, graphics, 0, 0, source, false); check(!screen.lines.contains("SMART AUTO"), "Stale render leak");
            PlayerShipLink0680.resetClient(); eq(PlayerMining0700.clientSnapshot(), null);
            ClickScreen click = new ClickScreen();
            check(PlayerDiscoveries0650.mouseClicked(click, 370, 50, 0) && click.tab == 4, "Mining header disabled");
            check(PlayerDiscoveries0650.mouseClicked(click, 300, 170, 0), "Mining content reached legacy actions");
            check(!PlayerDiscoveries0650.mouseClicked(click, 30, 50, 0), "Mining swallowed Overview header");
            check(!PlayerDiscoveries0650.mouseClicked(click, 100, 50, 0), "Mining swallowed Survey header");
            check(PlayerDiscoveries0650.mouseClicked(click, 280, 50, 0) && click.tab == 3, "Mining to Navigation failed");
            auditBytecode();
            System.out.println("Player Mining real-state adapter, progress, config, bounded wire, link/stale, read-only and layout smoke test passed.");
        } finally { Files.deleteIfExists(config); Files.deleteIfExists(root); }
    }
    static void auditBytecode() throws Exception {
        Set<String> forbidden = Set.of("state", "touch", "setDirty", "insert", "extract", "bindClickedStage", "cycleMode", "ensureBuffers", "getChunk", "calculate", "resetArea");
        try (var in = PlayerMining0700.class.getResourceAsStream("PlayerMining0700.class")) {
            new ClassReader(in).accept(new ClassVisitor(Opcodes.ASM9) {
                public MethodVisitor visitMethod(int access, String name, String desc, String sig, String[] ex) {
                    return new MethodVisitor(Opcodes.ASM9) {
                        public void visitLdcInsn(Object value) { if (value instanceof String s) check(!forbidden.contains(s), "Mutating reflection target: " + s); }
                    };
                }
            }, 0);
        }
    }
    public static class Data { public Map<String, Object> states = new HashMap<>(); }
    public static class ClickScreen { public int width = 540, height = 300, tab; }
    public interface WorldReader { default boolean hasChunkAt(Pos pos) { return pos.x() == 0; } }
    public static class LoadedWorld implements WorldReader {}
    public record Pos(int x, int y, int z) { public int getX() { return x; } public int getZ() { return z; } }
    public record Dim(String value) { public String location() { return value; } }
    public static class Manager {
        public Pos pos = new Pos(-1, 64, -1);
        public Pos getCurrentExteriorPosition() { return pos; }
        public Dim getCurrentExteriorDimension() { return new Dim("minecraft:overworld"); }
    }
}
