import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicReference;

import net.newworld.navigation.Navigation0610DiscoveryRuntime;
import net.newworld.navigation.NavigationDiscoveryEventBus;

/** Standalone schema-v3 merge, persistence, migration, and event smoke test. */
public final class Navigation0610DiscoveryRuntimeSmokeTest {
    private Navigation0610DiscoveryRuntimeSmokeTest() {}

    public static void main(String[] args) {
        FakeData data = new FakeData();
        AtomicReference<NavigationDiscoveryEventBus.Event> event = new AtomicReference<>();
        NavigationDiscoveryEventBus.Listener listener = event::set;
        NavigationDiscoveryEventBus.subscribe(listener);
        try {
            FakeDiscovery radar = discovery("STRUCTURE", "RADAR", 10L, false);
            Navigation0610DiscoveryRuntime.record(data, "ship-test", radar);
            expect("new analysis", radar.analysisLevel, 0);
            expect("new last seen", radar.lastSeenAt, 10L);
            expect("new event", event.get().change(), NavigationDiscoveryEventBus.Change.DISCOVERED);

            FakeDiscovery field = discovery("STRUCTURE", "FIELD", 20L, true);
            Navigation0610DiscoveryRuntime.record(data, "ship-test", field);
            expect("first discovery retained", field.discoveredAt, 10L);
            expect("field analysis", field.analysisLevel, 1);
            expect("repeat last seen", field.lastSeenAt, 20L);
            expect("visited merge", field.visited, true);
            expect("upgrade event", event.get().change(), NavigationDiscoveryEventBus.Change.ANALYSIS_UPGRADED);

            FakeDiscovery radarAgain = discovery("STRUCTURE", "RADAR", 30L, false);
            Navigation0610DiscoveryRuntime.record(data, "ship-test", radarAgain);
            expect("analysis never downgrades", radarAgain.analysisLevel, 1);
            expect("FIELD source never downgrades", radarAgain.source, "FIELD");
            expect("seen event", event.get().change(), NavigationDiscoveryEventBus.Change.SEEN);
            expect("third last seen", radarAgain.lastSeenAt, 30L);

            FakeTag oldTag = new FakeTag();
            oldTag.putInt("NWDiscoverySchema", 2);
            FakeDiscovery migrated = discovery("GEOLOGY", "RADAR", 75L, false);
            Navigation0610DiscoveryRuntime.afterLoad(oldTag, "S0_D0_", migrated);
            expect("migration analysis", migrated.analysisLevel, 0);
            expect("migration last seen", migrated.lastSeenAt, 75L);

            FakeTag saved = new FakeTag();
            Navigation0610DiscoveryRuntime.afterSaveSchema(saved);
            Navigation0610DiscoveryRuntime.afterSave(saved, "S0_D0_", migrated);
            expect("saved schema", saved.getInt("NWDiscoverySchema"), 3);
            expect("saved analysis", saved.getInt("S0_D0_AnalysisLevel"), 0);
            expect("saved last seen", saved.getLong("S0_D0_LastSeenAt"), 75L);
            if (!data.dirty) throw new AssertionError("record did not retain SavedData dirty behavior");
        } finally {
            NavigationDiscoveryEventBus.unsubscribe(listener);
        }
        System.out.println("NewWorldCore discovery schema-v3 smoke test passed");
    }

    private static FakeDiscovery discovery(String kind, String source, long at, boolean visited) {
        FakeDiscovery discovery = new FakeDiscovery();
        discovery.kind = kind;
        discovery.source = source;
        discovery.discoveredAt = at;
        discovery.visited = visited;
        return discovery;
    }

    private static void expect(String label, long actual, long expected) {
        if (actual != expected) throw new AssertionError(label + ": expected " + expected + ", got " + actual);
    }

    private static void expect(String label, boolean actual, boolean expected) {
        if (actual != expected) throw new AssertionError(label + ": expected " + expected + ", got " + actual);
    }

    private static void expect(String label, Object actual, Object expected) {
        if (!java.util.Objects.equals(actual, expected)) {
            throw new AssertionError(label + ": expected " + expected + ", got " + actual);
        }
    }

    public static final class FakeData {
        private final FakeState state = new FakeState();
        boolean dirty;

        public FakeState state(String ignored) { return state; }

        public void record0610Base(String ignored, FakeDiscovery discovery) {
            FakeDiscovery existing = state.discoveries.get(discovery.key());
            if (existing != null) {
                discovery.favorite = existing.favorite;
                discovery.visited = existing.visited;
                state.discoveries.remove(discovery.key());
            }
            state.discoveries.put(discovery.key(), discovery);
            setDirty();
        }

        public void setDirty() { dirty = true; }
    }

    public static final class FakeState {
        final LinkedHashMap<String, FakeDiscovery> discoveries = new LinkedHashMap<>();
    }

    public static final class FakeDiscovery {
        public String label = "TEST";
        public String clazz = "TEST";
        public String dimension = "minecraft:overworld";
        public int x = 1;
        public int y = 2;
        public int z = 3;
        public long discoveredAt;
        public boolean favorite;
        public boolean visited;
        public String kind;
        public String source;
        public int analysisLevel;
        public long lastSeenAt;

        public String key() { return dimension + '|' + x + '|' + y + '|' + z; }
    }

    public static final class FakeTag {
        private final LinkedHashMap<String, Number> values = new LinkedHashMap<>();
        public int getInt(String key) { return values.getOrDefault(key, 0).intValue(); }
        public long getLong(String key) { return values.getOrDefault(key, 0L).longValue(); }
        public void putInt(String key, int value) { values.put(key, value); }
        public void putLong(String key, long value) { values.put(key, value); }
    }
}
