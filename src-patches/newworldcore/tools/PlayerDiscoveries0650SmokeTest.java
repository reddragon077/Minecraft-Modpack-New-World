import java.nio.charset.StandardCharsets;
import java.util.List;

import net.newworld.player.PlayerDiscoveries0650;
import net.newworld.player.PlayerDiscoveries0650.DiscoveryView;

/** Standalone decoder coverage for the compact Player Discoveries snapshot stream. */
public final class PlayerDiscoveries0650SmokeTest {
    private PlayerDiscoveries0650SmokeTest() {}

    public static void main(String[] args) {
        accept(PlayerDiscoveries0650.SNAPSHOT_BEGIN_BASE + 1);
        number(PlayerDiscoveries0650.FIELD_TOTAL, 42);
        accept(PlayerDiscoveries0650.RECORD_BEGIN);
        string(PlayerDiscoveries0650.FIELD_LABEL, "TIN-RICH DEPOSIT");
        string(PlayerDiscoveries0650.FIELD_KIND, "GEOLOGY");
        string(PlayerDiscoveries0650.FIELD_DIMENSION, "minecraft:overworld");
        string(PlayerDiscoveries0650.FIELD_SOURCE, "FIELD");
        string(PlayerDiscoveries0650.FIELD_PRIMARY, "tin");
        number(PlayerDiscoveries0650.FIELD_X, -2696);
        number(PlayerDiscoveries0650.FIELD_Y, 32);
        number(PlayerDiscoveries0650.FIELD_Z, -728);
        number(PlayerDiscoveries0650.FIELD_DISTANCE, 0);
        number(PlayerDiscoveries0650.FIELD_RESERVE, 1819);
        number(PlayerDiscoveries0650.FIELD_ANALYSIS, 3);
        number(PlayerDiscoveries0650.FIELD_LAST_SEEN, 123456);
        number(PlayerDiscoveries0650.FIELD_FLAGS, 3);
        accept(PlayerDiscoveries0650.RECORD_END);
        accept(PlayerDiscoveries0650.SNAPSHOT_END);

        List<DiscoveryView> entries = PlayerDiscoveries0650.clientEntries();
        expect("entry count", entries.size(), 1);
        expect("expected count", PlayerDiscoveries0650.clientExpected(), 1);
        expect("server total", PlayerDiscoveries0650.clientTotal(), 42);
        if (PlayerDiscoveries0650.clientReceiving()) throw new AssertionError("decoder remained in receiving state");
        DiscoveryView entry = entries.getFirst();
        expectText("label", entry.label(), "TIN-RICH DEPOSIT");
        expectText("kind", entry.kind(), "GEOLOGY");
        expectText("dimension", entry.dimension(), "minecraft:overworld");
        expectText("source", entry.source(), "FIELD");
        expectText("primary", entry.primary(), "tin");
        expect("x", entry.x(), -2696);
        expect("y", entry.y(), 32);
        expect("z", entry.z(), -728);
        expect("reserve", entry.reserve(), 1819);
        expect("analysis", entry.analysis(), 3);
        if (!entry.favorite() || !entry.visited()) throw new AssertionError("favorite/visited flags were not decoded");
        expectText("live player distance",
                PlayerDiscoveries0650.proximityLabel(entry, "minecraft:overworld", -2693, 36, -728),
                "PLAYER DIST 5 BLOCKS");
        expectText("different dimension",
                PlayerDiscoveries0650.proximityLabel(entry, "minecraft:the_nether", -2693, 36, -728),
                "DIFFERENT DIMENSION");
        System.out.println("Player Discoveries snapshot smoke test passed.");
    }

    private static void number(int field, int value) {
        accept(field);
        accept(value);
    }

    private static void string(int field, String value) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        accept(field);
        accept(bytes.length);
        for (int offset = 0; offset < bytes.length; offset += 4) {
            int packed = 0;
            for (int slot = 0; slot < 4 && offset + slot < bytes.length; slot++) {
                packed |= (bytes[offset + slot] & 0xff) << (slot * 8);
            }
            accept(packed);
        }
    }

    private static void accept(int code) {
        if (!PlayerDiscoveries0650.accept(code)) throw new AssertionError("decoder rejected code " + code);
    }

    private static void expect(String label, long actual, long expected) {
        if (actual != expected) throw new AssertionError(label + ": expected " + expected + ", got " + actual);
    }

    private static void expectText(String label, String actual, String expected) {
        if (!expected.equals(actual)) throw new AssertionError(label + ": expected " + expected + ", got " + actual);
    }
}
