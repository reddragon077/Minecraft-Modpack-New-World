import java.nio.file.*;
import java.util.*;
import net.newworld.config.NewWorldConfig;
import net.newworld.player.PlayerShipLink0680;
import net.newworld.player.PlayerShipLink0680.Snapshot;
import net.newworld.player.PlayerDiscoveries0650;
import net.newworld.player.PlayerOverview0670;
import org.objectweb.asm.*;

public final class PlayerShipLink0680SmokeTest {
    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("newworld-link-smoke-");
        System.setProperty("newworldcore.configDir", root.toString());
        Path config = root.resolve("ship-link.properties");
        try {
            connectionDescriptorRegression();
            eq(PlayerShipLink0680.range(), 5000); eq(PlayerShipLink0680.refreshTicks(), 20); eq(PlayerShipLink0680.staleTicks(), 120);
            check(PlayerShipLink0680.dimensional(), "Default dimensional");
            state(true, false, "overworld", "overworld", 4999.9, "CONNECTED");
            state(true, false, "overworld", "overworld", 5000, "CONNECTED");
            state(true, false, "overworld", "overworld", 5000.1, "LOST");
            state(true, false, "nether", "overworld", -1, "DIMENSIONAL");
            state(true, true, "tardis", "overworld", -1, "CONNECTED");
            state(false, true, "tardis", "overworld", 0, "LOST");
            state(true, false, "overworld", "overworld", Double.NaN, "LOST");
            state(true, false, "overworld", "overworld", Double.POSITIVE_INFINITY, "LOST");
            state(true, false, "overworld", null, 0, "LOST");
            check(!PlayerShipLink0680.classify("a", true, false, "nether", "overworld", -1, 5000, false).allowed(), "Dimensional deny");
            check(PlayerShipLink0680.classify("a", true, true, "tardis", "overworld", -1, 16, false).allowed(), "On-board exception");
            check(!PlayerShipLink0680.resolve(new Object()).snapshot().allowed(), "Failed resolver must fail closed");
            check(PlayerShipLink0680.linkedShip(new Object()) == null, "No unknown ship exposure");
            FakeLevel exterior = new FakeLevel("minecraft:overworld"), interior = new FakeLevel("dwm:tardis");
            FakePlayer player = new FakePlayer(); FakeManager manager = new FakeManager(player.owner, interior);
            Snapshot actual = PlayerShipLink0680.fromManager(player, exterior, manager).snapshot();
            check(actual.allowed() && actual.distance() == 13, "Reflective XYZ distance must include Y (3,4,12)");
            check(PlayerShipLink0680.fromManager(player, interior, manager).snapshot().reason().equals("ON BOARD"), "Own interior adapter");
            check(!PlayerShipLink0680.fromManager(player, exterior, new FakeManager(UUID.randomUUID(), interior)).snapshot().allowed(), "Foreign owner adapter");
            check(!PlayerShipLink0680.fromManager(player, interior, new FakeManager(UUID.randomUUID(), interior)).snapshot().allowed(), "Foreign interior bypass");
            check(!PlayerShipLink0680.fromManager(player, exterior, new FakeManager(player.owner, null)).snapshot().allowed(), "Unloaded interior");
            Files.writeString(config, "range_blocks=1\nrefresh_ticks=1\nstale_after_ticks=1\nallow_dimensional_link=false\n"); NewWorldConfig.reload();
            eq(PlayerShipLink0680.range(), 16); eq(PlayerShipLink0680.refreshTicks(), 20); eq(PlayerShipLink0680.staleTicks(), 40);
            check(!PlayerShipLink0680.dimensional(), "Live dimensional toggle");
            Files.writeString(config, "range_blocks=99999999\nrefresh_ticks=99999\nstale_after_ticks=40\n"); NewWorldConfig.reload();
            eq(PlayerShipLink0680.range(), 30000000); eq(PlayerShipLink0680.refreshTicks(), 1200); eq(PlayerShipLink0680.staleTicks(), 2400);
            Files.writeString(config, "range_blocks=oops\nallow_dimensional_link=invalid\n"); NewWorldConfig.reload();
            eq(PlayerShipLink0680.range(), 5000); check(PlayerShipLink0680.dimensional(), "Invalid fallback");
            Files.writeString(config, "range_blocks=64\nallow_dimensional_link=false\n"); NewWorldConfig.reload(); eq(PlayerShipLink0680.range(), 64);
            Snapshot source = new Snapshot("CONNECTED", "owner-ship", "minecraft:overworld", 123.75, "IN RANGE", 20, 120);
            PlayerShipLink0680.resetClient(); check(!PlayerShipLink0680.clientAllowed(), "Initial fail closed");
            for (int code : PlayerShipLink0680.encode(source)) {
                check(PlayerShipLink0680.isWireCode(code), "Framing");
                check(!PlayerOverview0670.isWireCode(code), "Overview collision");
                check(!PlayerDiscoveries0650.isActionMode(code), "Action collision");
                PlayerShipLink0680.accept(code);
            }
            check(source.equals(PlayerShipLink0680.clientSnapshot()), "Lossy wire roundtrip");
            check(PlayerShipLink0680.clientAllowed(), "Fresh valid link");
            PlayerShipLink0680.accept(PlayerShipLink0680.BEGIN); PlayerShipLink0680.accept(PlayerShipLink0680.END);
            check(source.equals(PlayerShipLink0680.clientSnapshot()), "Partial frame replaced snapshot");
            PlayerShipLink0680.accept(PlayerShipLink0680.BEGIN);
            for (int i = 0; i < 400; i++) PlayerShipLink0680.accept(PlayerShipLink0680.WIRE_BASE);
            PlayerShipLink0680.accept(PlayerShipLink0680.END);
            check(source.equals(PlayerShipLink0680.clientSnapshot()), "Oversized frame accepted");
            for (int code : PlayerShipLink0680.encode(new Snapshot("LOST", "", "UNKNOWN", -1, "NO OWNED SHIP", 20, 120))) PlayerShipLink0680.accept(code);
            check(!PlayerShipLink0680.clientAllowed(), "Lost snapshot must revoke access");
            check(PlayerDiscoveries0650.clientEntries().isEmpty(), "Old discoveries leaked after loss");
            long received = 1_000_000_000L;
            check(PlayerShipLink0680.fresh(received + 6_000_000_000L, received, received, 120), "Timeout boundary");
            check(!PlayerShipLink0680.fresh(received + 6_000_000_001L, received, received, 120), "Timeout failed");
            check(!PlayerShipLink0680.fresh(received, received, received + 1, 120), "Old screen session accepted");
            check(!PlayerShipLink0680.fresh(received - 1, received, 0, 120), "Clock reversal");
            FakeScreen screen = new FakeScreen(); FakeGraphics graphics = new FakeGraphics();
            PlayerShipLink0680.drawHeader(screen, graphics, 0, 0, source);
            PlayerShipLink0680.drawHeader(screen, graphics, 0, 0, new Snapshot("DIMENSIONAL", "a", "verylongmod:" + "dimension".repeat(12), -1, "DIFFERENT DIMENSION", 20, 120));
            check(screen.rows == 4 && graphics.flushes == 4, "Header missing / deferred layer");
            check(PlayerShipLink0680.blockContentClick(screen, 30, 100), "Lost content click");
            check(!PlayerShipLink0680.blockContentClick(screen, 30, 50), "Tab switching blocked");
            PlayerShipLink0680.resetClient(); check(PlayerShipLink0680.clientSnapshot() == null, "Session reset");
            System.out.println("Player Ship Link ownership/range/dimension, live config, framing, stale/reset and layout smoke test passed.");
        } finally { Files.deleteIfExists(config); Files.deleteIfExists(root); }
    }
    private static void state(boolean owner, boolean inside, String p, String e, double distance, String expected) {
        Snapshot actual = PlayerShipLink0680.classify("a", owner, inside, p, e, distance, 5000, true);
        check(actual.state().equals(expected), expected + " != " + actual);
        check(actual.allowed() == !expected.equals("LOST"), "Policy bypass");
    }
    /** Actual JVM-only collision: identical name/arguments, unrelated return types, unlike Java overloads. */
    private static void connectionDescriptorRegression() throws Exception {
        for (boolean reverse : List.of(false, true)) for (boolean connected : List.of(false, true)) {
            String name = "ConnectionCollision" + reverse + connected;
            ClassWriter cw = new ClassWriter(0);
            cw.visit(Opcodes.V21, Opcodes.ACC_PUBLIC, name, null, "java/lang/Object", null);
            MethodVisitor init = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
            init.visitCode(); init.visitVarInsn(Opcodes.ALOAD, 0);
            init.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            init.visitInsn(Opcodes.RETURN); init.visitMaxs(1, 1); init.visitEnd();
            for (boolean expected : reverse ? List.of(true, false) : List.of(false, true)) {
                String type = expected ? "PlayerShipLink0680SmokeTest$FakeConnection" : "java/lang/String";
                MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "getConnection", "()L" + type + ";", null, null);
                mv.visitCode();
                if (expected && connected) mv.visitFieldInsn(Opcodes.GETSTATIC, type, "INSTANCE", "L" + type + ";");
                else if (!expected && !connected) mv.visitLdcInsn("unrelated transport must not grant a link");
                else mv.visitInsn(Opcodes.ACONST_NULL);
                mv.visitInsn(Opcodes.ARETURN); mv.visitMaxs(1, 1); mv.visitEnd();
            }
            cw.visitEnd();
            Object fixture = new FixtureLoader().define(name, cw.toByteArray()).getConstructor().newInstance();
            check(fixture.getClass().getDeclaredMethods().length == 2, "Collision fixture missing getter");
            Object value = PlayerOverview0670.callReturning(fixture, "getConnection", FakeConnection.class.getName());
            check(value == (connected ? FakeConnection.INSTANCE : null), "Return descriptor selection / disconnect failed");
            try {
                PlayerOverview0670.callReturning(fixture, "getConnection", "missing.Listener");
                throw new AssertionError("Missing listener descriptor fell back to another getter");
            } catch (NoSuchMethodException expected) { }
        }
        System.out.println("Client connection JVM descriptor collision: both method orders, live/null listener and missing-type rejection passed.");
    }
    public static final class FakeConnection { public static final FakeConnection INSTANCE = new FakeConnection(); }
    private static final class FixtureLoader extends ClassLoader {
        Class<?> define(String name, byte[] bytes) { return defineClass(name, bytes, 0, bytes.length); }
    }
    private static void check(boolean value, String message) { if (!value) throw new AssertionError(message); }
    private static void eq(int actual, int expected) { check(actual == expected, actual + " != " + expected); }
    public static final class FakeFont { public int width(String text) { return text.length() * 6; } }
    public record FakeDimension(String location) {}
    public record FakeLevel(String name) { public FakeDimension dimension() { return new FakeDimension(name); } }
    public static final class FakePlayer {
        UUID owner = UUID.randomUUID();
        public UUID getUUID() { return owner; }
        public double getX() { return 3; } public double getY() { return 4; } public double getZ() { return 12; }
    }
    public static final class FakePos { public int getX() { return 0; } public int getY() { return 0; } public int getZ() { return 0; } }
    public record FakeManager(UUID owner, Object world) {
        public UUID getOwner() { return owner; } public Object getWorld() { return world; }
        public String getId() { return "fake-owned-ship"; }
        public FakeDimension getCurrentExteriorDimension() { return new FakeDimension("minecraft:overworld"); }
        public FakePos getCurrentExteriorPosition() { return new FakePos(); }
    }
    public static final class FakeScreen {
        public int width = 540, height = 300, tab = 2, rows;
        public FakeFont font = new FakeFont();
        public void text(Object g, String text, int x, int y, int color) {
            check(x >= 302 && x + font.width(text) <= 526 && y >= 11 && y + 9 <= 40, "Header bounds: " + text); rows++;
        }
    }
    public static final class FakeGraphics {
        int flushes;
        public void flush() { flushes++; }
        public void fill(int x, int y, int x2, int y2, int color) { check(x == 296 && x2 == 532 && y == 6 && y2 == 40, "Header panel bounds"); }
    }
}
