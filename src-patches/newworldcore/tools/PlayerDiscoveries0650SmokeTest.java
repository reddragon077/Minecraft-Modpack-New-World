import java.nio.charset.StandardCharsets;
import java.util.List;

import net.newworld.player.PlayerDiscoveries0650;
import net.newworld.player.PlayerDiscoveries0650.DiscoveryView;

/** Standalone decoder coverage for the compact Player Discoveries snapshot stream. */
public final class PlayerDiscoveries0650SmokeTest {
    private PlayerDiscoveries0650SmokeTest() {}

    public static void main(String[] args) throws Exception {
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
        expect("snapshot index", entry.snapshotIndex(), 0);
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
        expectText("last seen seconds", PlayerDiscoveries0650.lastSeenLabel(entry, 123856), "LAST SEEN 20s AGO");
        expectText("last seen minutes", PlayerDiscoveries0650.lastSeenLabel(entry, 195456), "LAST SEEN 1h AGO");
        if (!PlayerDiscoveries0650.isActionMode(PlayerDiscoveries0650.ACTION_TARGET_BASE)
                || !PlayerDiscoveries0650.isActionMode(PlayerDiscoveries0650.ACTION_ROUTE_BASE - 127)
                || !PlayerDiscoveries0650.isActionMode(PlayerDiscoveries0650.ACTION_FAVORITE_BASE - 511)
                || PlayerDiscoveries0650.isActionMode(PlayerDiscoveries0650.ACTION_TARGET_BASE - 512)
                || PlayerDiscoveries0650.isActionMode(100)) {
            throw new AssertionError("discovery action protocol range regression");
        }
        accept(PlayerDiscoveries0650.STATUS_ROUTE_OK);
        favoritesChecks();
        System.out.println("Player Discoveries snapshot smoke test passed.");
    }

    private static void favoritesChecks() throws Exception {
        java.util.ArrayList<Object> records = new java.util.ArrayList<>();
        for (int i = 0; i < 200; i++) records.add(new FavoriteRecord(false, 1000 + i));
        FavoriteRecord oldest = new FavoriteRecord(true, 1), newest = new FavoriteRecord(true, 2);
        records.add(oldest); records.add(newest);
        var favorites = PlayerDiscoveries0650.favoriteSnapshot(records, 128);
        if (!favorites.equals(List.of(newest, oldest))) throw new AssertionError("Old favorites lost behind recent nonfavorites");
        expect("favorite limit", PlayerDiscoveries0650.favoriteSnapshot(records, 1).size(), 1);
        expect("empty favorites", PlayerDiscoveries0650.favoriteSnapshot(List.of(), 128).size(), 0);
        for (int i = 0; i < 600; i++) records.add(new FavoriteRecord(true, i + 5));
        expect("wire hard bound", PlayerDiscoveries0650.favoriteSnapshot(records, 9999).size(), 512);
        if (!PlayerDiscoveries0650.favoriteSelectionAllowed(true, true, 0, newest)
                || PlayerDiscoveries0650.favoriteSelectionAllowed(true, false, 0, newest)
                || PlayerDiscoveries0650.favoriteSelectionAllowed(true, true, 1, newest)
                || PlayerDiscoveries0650.favoriteSelectionAllowed(true, true, 2, newest)
                || PlayerDiscoveries0650.favoriteSelectionAllowed(true, true, 0, new FavoriteRecord(false, 2))
                || PlayerDiscoveries0650.favoriteSelectionAllowed(true, true, 0, null)
                || !PlayerDiscoveries0650.favoriteSelectionAllowed(false, false, 1, newest)) {
            throw new AssertionError("Favorite server action gate regression");
        }
        // Reuse the actual renderer: picker has SEND TO SHIP only, no route/favorite mutation controls.
        FakeScreen screen = new FakeScreen();
        var viewsField = PlayerDiscoveries0650.class.getDeclaredField("VIEWS"); viewsField.setAccessible(true);
        @SuppressWarnings("unchecked") var views = (java.util.Map<Object,Object>) viewsField.get(null);
        Class<?> stateType = Class.forName("net.newworld.player.PlayerDiscoveries0650$ViewState");
        var constructor = stateType.getDeclaredConstructor(); constructor.setAccessible(true); Object state = constructor.newInstance();
        var picker = stateType.getDeclaredField("favoritesPicker"); picker.setAccessible(true); picker.set(state, true);
        var filter = stateType.getDeclaredField("filter"); filter.setAccessible(true); filter.set(state, 3);
        views.put(screen, state);
        var render = PlayerDiscoveries0650.class.getDeclaredMethod("renderDiscoveries", Object.class, Object.class, int.class, int.class);
        render.setAccessible(true); render.invoke(null, screen, new Graphics(), 0, 0);
        if (!screen.lines.contains("NAVIGATION // FAVORITES") || !screen.lines.contains("SEND TO SHIP")
                || screen.lines.contains("ROUTE") || screen.lines.contains("* FAV")) throw new AssertionError("Picker actions/layout");
        accept(PlayerDiscoveries0650.SNAPSHOT_BEGIN_BASE); number(PlayerDiscoveries0650.FIELD_TOTAL, 0); accept(PlayerDiscoveries0650.SNAPSHOT_END);
        screen.lines.clear(); render.invoke(null, screen, new Graphics(), 0, 0);
        if (!screen.lines.contains("NO FAVORITES")) throw new AssertionError("Empty picker guidance");
        PlayerDiscoveries0650.resetClientLink();
        if (!views.isEmpty() || !PlayerDiscoveries0650.clientEntries().isEmpty()) throw new AssertionError("Link reset retained picker");
    }

    public static final class FavoriteRecord {
        public boolean favorite; public long lastSeenAt, discoveredAt;
        public FavoriteRecord(boolean f, long seen) { favorite = f; lastSeenAt = seen; }
    }
    public static final class FakeScreen {
        public java.util.ArrayList<String> lines = new java.util.ArrayList<>();
        public void text(Object graphics, String text, int x, int y, int color) {
            if (x < 26 || x + text.length() * 6 > 514 || y < 91 || y + 9 > 288) throw new AssertionError("Text bounds: " + text);
            lines.add(text);
        }
    }
    public static final class Graphics {
        public void fill(int x, int y, int x2, int y2, int color) {
            if (x < 26 || x2 > 514 || y < 110 || y2 > 278) throw new AssertionError("Fill bounds");
        }
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
