import java.nio.file.*;
import java.util.*;
import net.newworld.config.*;
import net.newworld.player.*;
import net.newworld.navigation.*;

public final class PlayerLocation0692SmokeTest {
    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("newworld-location-test-");
        Path config = root.resolve("player-navigation.properties");
        System.setProperty("newworldcore.configDir", root.toString()); NewWorldConfig.reload();
        try {
            check(PlayerLocation0692.enabled() && PlayerLocation0692.limit() == 128 && PlayerLocation0692.cooldown() == 40, "defaults");
            Files.writeString(config, "location.enabled=false\nlocation.max_per_ship=0\nlocation.cooldown_ticks=0\n"); NewWorldConfig.reload();
            check(!PlayerLocation0692.enabled() && PlayerLocation0692.limit() == 1 && PlayerLocation0692.cooldown() == 20, "live lower bounds");
            Files.writeString(config, "location.enabled=bad\nlocation.max_per_ship=99999\nlocation.cooldown_ticks=99999\n"); NewWorldConfig.reload();
            check(PlayerLocation0692.enabled() && PlayerLocation0692.limit() == 1024 && PlayerLocation0692.cooldown() == 1200, "upper and bool fallback");
            Files.writeString(config, "location.max_per_ship=bad\nlocation.cooldown_ticks=bad\n"); NewWorldConfig.reload();
            check(PlayerLocation0692.limit() == 128 && PlayerLocation0692.cooldown() == 40, "numeric fallback");
            check(PlayerLocation0692.gate(false,true,false,10,null,40) == PlayerLocation0692.DISABLED, "disabled");
            check(PlayerLocation0692.gate(true,false,false,10,null,40) == PlayerLocation0692.FAILED, "link");
            check(PlayerLocation0692.gate(true,true,true,10,null,40) == PlayerLocation0692.INTERIOR, "interior");
            check(PlayerLocation0692.gate(true,true,false,1_999_999_999L,0L,40) == PlayerLocation0692.WAIT, "cooldown");
            check(PlayerLocation0692.gate(true,true,false,2_000_000_000L,0L,40) == PlayerLocation0692.SAVED, "cooldown boundary");
            Data data = new Data();
            var first = location("minecraft:overworld", -1, 64, 2);
            check(PlayerLocation0692.persist(data,"ship",first,1) == PlayerLocation0692.SAVED, "save");
            check(data.state.discoveries.size() == 1 && data.dirty && first.favorite && first.visited, "persistent favorite");
            check(data.state.selectedKey.equals("old-target") && data.route.equals("old-route"), "target/route changed");
            check(first.kind.equals("WAYPOINT") && first.source.equals("MANUAL") && first.analysisLevel == 0, "metadata");
            check(PlayerLocation0692.persist(data,"ship",location("minecraft:overworld",-1,64,2),1) == PlayerLocation0692.SAVED, "duplicate");
            check(data.state.discoveries.size() == 1 && data.state.discoveries.get(first.key()) == first, "duplicate replaced");
            check(PlayerLocation0692.persist(data,"ship",location("minecraft:the_nether",-1,64,2),1) == PlayerLocation0692.FULL, "cap across dimensions");
            check(PlayerLocation0692.persist(data,"ship",location("minecraft:the_nether",-1,64,2),2) == PlayerLocation0692.SAVED, "dimension key");
            first.kind="GEOLOGY"; first.source="FIELD"; first.label="REAL ORE"; first.analysisLevel=3; first.favorite=false;
            PlayerLocation0692.persist(data,"ship",location("minecraft:overworld",-1,64,2),1);
            check(first.favorite && first.label.equals("REAL ORE") && first.analysisLevel==3 && first.source.equals("FIELD"), "evidence overwritten");
            // Actual patched metadata encoder/decoder (not just the new normalizer) must retain the new category.
            var original = location("minecraft:overworld", 8, -30, 10);
            Tag tag = new Tag(); Navigation0510DiscoveryMeta.saveMeta(tag,"S0_D0_",original);
            Navigation0510DiscoveryMeta.saveSchema(tag);
            var restored = location("minecraft:overworld",8,-30,10); restored.kind=null; restored.source=null;
            Navigation0510DiscoveryMeta.loadMeta(tag,"S0_D0_",restored);
            check(restored.kind.equals("WAYPOINT") && restored.source.equals("MANUAL"), "metadata round-trip");
            check(NewWorldTuning.discoveryAnalysisLevel("WAYPOINT","MANUAL") == 0, "waypoint analysis");
            check(PlayerLocation0692.normalizeKind(null).equals("STRUCTURE") && PlayerLocation0692.normalizeSource(null).equals("RADAR"), "legacy fallback");
            check(PlayerLocation0692.normalizeKind("geology").equals("GEOLOGY") && PlayerLocation0692.normalizeSource("field").equals("FIELD"), "legacy categories");
            for(int code=PlayerLocation0692.SAVED;code<=PlayerLocation0692.FAILED;code++) {
                check(!PlayerNavigation0690.isWireCode(code) && !PlayerShipLink0680.isWireCode(code) && !PlayerDiscoveries0650.isActionMode(code), "status collision");
                check(PlayerLocation0692.accept(code) && !PlayerLocation0692.status().isBlank(), "status");
            }
            PlayerShipLink0680.resetClient(); check(PlayerLocation0692.status().isBlank(), "link reset");
            System.out.println("Player Location save/dedup/evidence/cap/policy/config and actual metadata round-trip smoke test passed.");
        } finally { Files.deleteIfExists(config); Files.deleteIfExists(root); }
    }
    static Navigation0610DiscoveryRuntimeSmokeTest.FakeDiscovery location(String dim,int x,int y,int z) throws Exception {
        var d = new Navigation0610DiscoveryRuntimeSmokeTest.FakeDiscovery();
        PlayerLocation0692.initialize(d,dim,x,y,z,100L); return d;
    }
    public static final class Data {
        public final State state = new State(); public boolean dirty; public String route="old-route";
        public State state(String id) { return state; }
        public void setDirty() { dirty=true; }
        public void record(String id, Navigation0610DiscoveryRuntimeSmokeTest.FakeDiscovery d) { Navigation0610DiscoveryRuntime.record(this,id,d); }
        public void record0610Base(String id, Navigation0610DiscoveryRuntimeSmokeTest.FakeDiscovery d) { state.discoveries.put(d.key(),d); setDirty(); }
    }
    public static final class State {
        public final Map<String,Object> discoveries = new LinkedHashMap<>(); public String selectedKey="old-target";
    }
    public static final class Tag {
        final Map<String,Object> values = new HashMap<>();
        public void putString(String key,String value){values.put(key,value);} public String getString(String key){return (String)values.getOrDefault(key,"");}
        public void putInt(String key,int value){values.put(key,value);} public int getInt(String key){return ((Number)values.getOrDefault(key,0)).intValue();}
        public void putLong(String key,long value){values.put(key,value);} public long getLong(String key){return ((Number)values.getOrDefault(key,0L)).longValue();}
    }
    static void check(boolean value,String message){if(!value)throw new AssertionError(message);}
}
