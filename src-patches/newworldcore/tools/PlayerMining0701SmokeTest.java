import java.nio.file.*;
import java.util.*;
import org.objectweb.asm.*;
import net.newworld.config.NewWorldConfig;
import net.newworld.player.*;
import net.newworld.player.PlayerMiningStop0701.*;
import static net.newworld.player.PlayerMiningStop0701.*;

public final class PlayerMining0701SmokeTest {
    static void check(boolean b, String m) { if (!b) throw new AssertionError(m); }
    static void eq(Object a, Object b) { check(Objects.equals(a,b), a + " != " + b); }
    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("mining-yield-stop-"); Path config = root.resolve("player-mining.properties");
        System.setProperty("newworldcore.configDir", root.toString()); NewWorldConfig.reload();
        try {
            eq(confirmTicks(),100); eq(cooldownTicks(),40); check(enabled(),"Default disabled"); eq(PlayerMining0700.resourceRows(),5);
            Files.writeString(config,"stop.enabled=false\nstop.confirm_ticks=1\nstop.cooldown_ticks=0\ntop_resources.rows=0\n"); NewWorldConfig.reload();
            check(!enabled(),"Live toggle"); eq(confirmTicks(),40); eq(cooldownTicks(),20); eq(PlayerMining0700.resourceRows(),1);
            Files.writeString(config,"stop.enabled=bad\nstop.confirm_ticks=99999\nstop.cooldown_ticks=99999\ntop_resources.rows=999\n"); NewWorldConfig.reload();
            check(enabled(),"Fallback"); eq(confirmTicks(),200); eq(cooldownTicks(),1200); eq(PlayerMining0700.resourceRows(),5);
            Files.writeString(config,""); NewWorldConfig.reload();
            yieldsTest(); actualPersistence(); stop(config); wireAndLayout(); auditHooks();
            System.out.println("Mining yield persistence, actual State reset, injection audit, stop guards/OFF-only, config, wire and detail layout smoke test passed.");
        } finally { Files.deleteIfExists(config); Files.deleteIfExists(root); }
    }
    static void yieldsTest() throws Exception {
        Object phase = PlayerMining0700SmokeTest.state("MiningPhaseSavedData");
        PlayerMining0700SmokeTest.set(phase,"minedTargets",101L);
        MiningYield0701.record(phase,new Target("minecraft:raw_iron",false));
        MiningYield0701.record(phase,new Target("minecraft:raw_iron",false));
        MiningYield0701.record(phase,new Target("minecraft:diamond",false));
        MiningYield0701.record(phase,new Target("minecraft:lava",true));
        MiningYield0701.record(phase,new Target("bad id",false));
        eq(MiningYield0701.top(phase,5),List.of(Map.entry("minecraft:raw_iron",2L),Map.entry("minecraft:diamond",1L)));
        check(MiningYield0701.scope(phase).contains("100 EARLIER UNTRACKED"),"Invented legacy counts");
        Data data = new Data(); data.states.put("ship",phase); Tag root = new Tag(); root.putLong("unrelated",42);
        MiningYield0701.write(data,root); eq(root.getLong("unrelated"),42L);
        Object restored = PlayerMining0700SmokeTest.state("MiningPhaseSavedData"); Data loaded = new Data(); loaded.states.put("ship",restored);
        MiningYield0701.read(loaded,root); eq(MiningYield0701.top(restored,5),MiningYield0701.top(phase,5));
        eq(MiningYield0701.scope(restored),MiningYield0701.scope(phase));
        MiningYield0701.record(restored,new Target("minecraft:diamond",false));
        eq(MiningYield0701.top(restored,1),List.of(Map.entry("minecraft:diamond",2L))); // Stable lexical tie.
        // Execute the patched production reset, not a call to the helper in its place.
        restored.getClass().getMethod("resetArea",String.class,int.class,int.class,long.class).invoke(restored,"minecraft:overworld",1,2,100L);
        check(MiningYield0701.top(restored,5).isEmpty(),"Area reset kept old counters");
        PlayerMining0700SmokeTest.set(restored,"minedTargets",1L); MiningYield0701.record(restored,new Target("minecraft:coal",false));
        eq(MiningYield0701.scope(restored),"THIS SCAN AREA / MINED BLOCKS");
        MiningYield0701.read(loaded,new Tag()); check(MiningYield0701.top(restored,5).isEmpty(),"Legacy missing namespace fabricated counters");
        Tag counts = root.getCompound(MiningYield0701.KEY).getCompound("ship").getCompound("counts");
        counts.putLong("minecraft:negative",-5); counts.putLong("bad key",9); counts.putLong("minecraft:raw_iron",Long.MAX_VALUE);
        MiningYield0701.read(loaded,root); MiningYield0701.record(restored,new Target("minecraft:raw_iron",false));
        eq(MiningYield0701.top(restored,5).getFirst(),Map.entry("minecraft:raw_iron",Long.MAX_VALUE));
        eq(MiningYield0701.top(restored,5).size(),2);
        eq(MiningYield0701.top(null,5),List.of()); eq(MiningYield0701.scope(null),"NO TRACKED MINING YET");
    }
    static void stop(Path config) throws Exception {
        Gate gate = new Gate(); Object player = new Object(); Manager manager = new Manager();
        Context context = new Context("ship",manager,true); long t = 20_000_000_000L;
        eq(gate.apply(player,ARM,context,t),DENIED); eq(manager.writes,0);
        gate.observe(player,"ship",t); int token = gate.apply(player,ARM,context,t);
        check(tokenCode(token),"Missing challenge"); eq(manager.writes,0); // First click never writes.
        eq(gate.apply(player,ARM,context,t+1),token);
        eq(gate.apply(player,token,context,t+300_000_000L),STOPPED);
        eq(manager.writes,1); check(!manager.mining && manager.handbrake && manager.route == 17 && manager.shield,"Unrelated state mutation");
        eq(gate.apply(player,token,context,t+400_000_000L),WAIT); eq(manager.writes,1);
        t += 3_000_000_000L; gate.observe(player,"ship",t);
        eq(gate.apply(player,token,context,t),EXPIRED); // Replay after cooldown.
        token = gate.apply(player,ARM,context,t); eq(gate.apply(player,token,context,t+300_000_000L),ALREADY); eq(manager.writes,1);
        Gate other = new Gate(); other.observe(player,"ship",t); token = other.apply(player,ARM,context,t);
        eq(other.apply(new Object(),token,context,t+300_000_000L),DENIED);
        eq(other.apply(player,token,new Context("other",manager,true),t+300_000_000L),DENIED);
        eq(other.apply(player,token,context,t+300_000_000L),EXPIRED);
        token = other.apply(player,ARM,context,t); eq(other.apply(player,token,new Context("ship",manager,false),t+300_000_000L),DENIED);
        token = other.apply(player,ARM,context,t); eq(other.apply(player,token,context,t+5_100_000_000L),EXPIRED);
        token = other.apply(player,ARM,context,t); eq(other.apply(player,token,context,t+100_000_000L),EXPIRED);
        token = other.apply(player,ARM,context,t); eq(other.apply(player,token-1,context,t+300_000_000L),EXPIRED);
        token = other.apply(player,ARM,context,t);
        Files.writeString(config,"stop.enabled=false\n"); NewWorldConfig.reload(); eq(other.apply(player,token,context,t+300_000_000L),DENIED);
        Files.writeString(config,""); NewWorldConfig.reload();
        eq(other.apply(player,ARM,context,t+7_000_000_000L),DENIED); // Stale view.
        eq(manager.writes,1);
        Gate changed = new Gate(); changed.observe(player,"ship",t); token = changed.apply(player,ARM,context,t);
        changed.observe(player,"other",t+1); changed.observe(player,"ship",t+2);
        eq(changed.apply(player,token,context,t+300_000_000L),EXPIRED);
        Gate broken = new Gate(); broken.observe(player,"ship",t); Manager noOp = new Manager() { public boolean setShieldsMiningState(boolean value) { return false; } };
        Context bad = new Context("ship",noOp,true); token = broken.apply(player,ARM,bad,t);
        eq(broken.apply(player,token,bad,t+300_000_000L),FAILED);
    }
    /** Executes the shipped save/load bodies and injected hooks; only Minecraft NBT/storage boundaries are fixtures. */
    static void actualPersistence() throws Exception {
        String name="net/newworld/mining/MiningPhaseSavedData";
        ClassWriter out=new ClassWriter(0);
        try(var in=PlayerMining0701SmokeTest.class.getResourceAsStream("/"+name+".class")) {
            new ClassReader(in).accept(new ClassVisitor(Opcodes.ASM9,out) {
                String remap(String s) {
                    return s==null?null:s.replace("net/minecraft/world/level/saveddata/SavedData","PlayerMining0701SmokeTest$Storage")
                        .replace("net/minecraft/nbt/CompoundTag","PlayerMining0701SmokeTest$Nbt")
                        .replace("net/minecraft/nbt/Tag","PlayerMining0701SmokeTest$Nbt")
                        .replace("net/minecraft/core/HolderLookup$Provider","java/lang/Object");
                }
                public void visit(int v,int a,String n,String sig,String sup,String[] it) { super.visit(v,a,n,null,remap(sup),it); }
                public FieldVisitor visitField(int a,String n,String d,String sig,Object value) {
                    return n.equals("FACTORY")?null:super.visitField(a,n,remap(d),null,value);
                }
                public MethodVisitor visitMethod(int a,String n,String d,String sig,String[] ex) {
                    if(n.equals("<clinit>") || n.equals("get")) return null;
                    return new MethodVisitor(Opcodes.ASM9,super.visitMethod(a,n,remap(d),null,ex)) {
                        public void visitTypeInsn(int op,String type) { super.visitTypeInsn(op,remap(type)); }
                        public void visitFieldInsn(int op,String owner,String f,String desc) { super.visitFieldInsn(op,remap(owner),f,remap(desc)); }
                        public void visitMethodInsn(int op,String owner,String m,String desc,boolean itf) { super.visitMethodInsn(op,remap(owner),m,remap(desc),itf); }
                        public void visitFrame(int type,int nl,Object[] locals,int ns,Object[] stack) {
                            if(locals!=null) for(int i=0;i<nl;i++) if(locals[i] instanceof String s) locals[i]=remap(s);
                            if(stack!=null) for(int i=0;i<ns;i++) if(stack[i] instanceof String s) stack[i]=remap(s);
                            super.visitFrame(type,nl,locals,ns,stack);
                        }
                    };
                }
            },0);
        }
        Class<?> type=new ClassLoader(PlayerMining0701SmokeTest.class.getClassLoader()) {
            Class<?> define() { return defineClass(name.replace('/','.'),out.toByteArray(),0,out.toByteArray().length); }
        }.define();
        Object data=type.getConstructor().newInstance(), phase=type.getMethod("state",String.class).invoke(data,"ship");
        PlayerMining0700SmokeTest.set(phase,"exteriorDimension","minecraft:overworld");
        PlayerMining0700SmokeTest.set(phase,"minedTargets",7L); MiningYield0701.record(phase,new Target("minecraft:raw_iron",false));
        Nbt root=new Nbt(); root.putLong("unrelated",42);
        eq(type.getMethod("save",Nbt.class,Object.class).invoke(data,root,null),root);
        check(root.contains(MiningYield0701.KEY),"Actual save missed ledger"); eq(root.getLong("unrelated"),42L);
        Object reload=type.getMethod("load",Nbt.class,Object.class).invoke(null,root,null);
        Object restored=type.getMethod("state",String.class).invoke(reload,"ship");
        eq(MiningYield0701.top(restored,5),List.of(Map.entry("minecraft:raw_iron",1L)));
        eq(restored.getClass().getField("minedTargets").getLong(restored),7L);
        eq(MiningYield0701.scope(restored),"SINCE UPDATE / 6 EARLIER UNTRACKED");
        root.values.remove(MiningYield0701.KEY);
        Object legacy=type.getMethod("load",Nbt.class,Object.class).invoke(null,root,null);
        Object old=type.getMethod("state",String.class).invoke(legacy,"ship");
        eq(old.getClass().getField("minedTargets").getLong(old),7L); check(MiningYield0701.top(old,5).isEmpty(),"Legacy invented breakdown");
    }
    static void wireAndLayout() throws Exception {
        var s = new PlayerMining0700.Snapshot("ship","MINING","EXTRACTION","minecraft:overworld","CH 1, 2","100%","1%","42","0","0","50","SMART AUTO","MOVED AE 1 / REP 3",20,120,
                "THIS SCAN AREA / MINED BLOCKS",List.of("5 BLOCKS / minecraft:raw_iron","2 BLOCKS / minecraft:coal"),true);
        for(int code:PlayerMining0700.encode(s)) PlayerMining0700.accept(code); eq(PlayerMining0700.clientSnapshot(),s);
        for(int code:new int[]{-1_000_001,-2_000_000,STOPPED,FAILED}) {
            check(isReply(code) && !PlayerDiscoveries0650.isActionMode(code) && !PlayerMining0700.isWireCode(code)
                    && !PlayerNavigation0690.isWireCode(code) && !PlayerOverview0670.isWireCode(code) && !PlayerShipLink0680.isWireCode(code),"Code collision");
        }
        check(!tokenCode(-1_000_000) && !tokenCode(-2_000_001),"Token bounds");
        var screen = new Screen(); var graphics = new PlayerNavigation0690SmokeTest.Graphics();
        PlayerMining0700.drawDetails(screen,graphics,0,0,s);
        check(screen.lines.contains("5 BLOCKS / minecraft:raw_iron"),"Resource lost in UI");
        var detail = PlayerMining0700.class.getDeclaredField("details"); detail.setAccessible(true); detail.setBoolean(null,true);
        PlayerMining0700.draw(screen,graphics,0,0,s,true); check(screen.lines.contains("STOP MINING"),"Stop UI missing");
        PlayerMining0700.resetClient(); check(!detail.getBoolean(null),"Details leaked across session"); eq(PlayerMiningStop0701.message(),"");
    }
    static void auditHooks() throws Exception {
        Map<String,Integer> expected = Map.of("MiningPhaseRuntime",1,"MiningPhaseSavedData",2,"MiningPhaseSavedData$State",1);
        for (var entry:expected.entrySet()) {
            int[] calls={0};
            try (var in=PlayerMining0701SmokeTest.class.getResourceAsStream("/net/newworld/mining/"+entry.getKey()+".class")) {
                new ClassReader(in).accept(new ClassVisitor(Opcodes.ASM9) {
                    public MethodVisitor visitMethod(int a,String name,String d,String sig,String[] ex) {
                        return new MethodVisitor(Opcodes.ASM9) {
                            boolean increment;
                            public void visitFieldInsn(int op,String owner,String field,String desc) { if(op==Opcodes.PUTFIELD && field.equals("minedTargets")) increment=true; }
                            public void visitMethodInsn(int op,String owner,String method,String desc,boolean itf) {
                                if(owner.equals("net/newworld/player/MiningYield0701")) { calls[0]++;
                                    if(method.equals("record")) check(name.equals("extract") && increment,"Counter hooked before success");
                                    if(method.equals("write")) eq(name,"save"); if(method.equals("read")) eq(name,"load"); if(method.equals("reset")) eq(name,"resetArea");
                                }
                            }
                        };
                    }
                },0);
            }
            eq(calls[0],entry.getValue());
        }
    }
    public record Target(String itemId, boolean hazard) {}
    public static class Data { public Map<String,Object> states=new HashMap<>(); }
    public static class Tag {
        public Map<String,Object> values=new HashMap<>();
        public Tag() {} public void put(String k,Tag t) { values.put(k,t); } public void putLong(String k,long n) { values.put(k,n); }
        public long getLong(String k) { return values.get(k) instanceof Number n?n.longValue():0; }
        public Tag getCompound(String k) { return values.get(k) instanceof Tag t?t:new Tag(); }
        public boolean contains(String k) { return values.containsKey(k); } public Set<String> getAllKeys() { return values.keySet(); }
    }
    public static class Manager {
        public boolean mining=true,handbrake=true,shield=true; public int writes,route=17;
        public boolean isShieldsMiningEnabled() { return mining; }
        public boolean setShieldsMiningState(boolean value) { check(!value,"Attempted restart"); writes++; mining=value; return true; }
    }
    public static class Storage { public Storage() {} public void setDirty() {} }
    public static class Nbt {
        public Map<String,Object> values=new HashMap<>(); public Nbt() {}
        public Nbt put(String k,Nbt n) { Object old=values.put(k,n); return old instanceof Nbt t?t:null; }
        public boolean contains(String k) { return values.containsKey(k); } public Set<String> getAllKeys() { return values.keySet(); }
        public Nbt getCompound(String k) { return values.get(k) instanceof Nbt t?t:new Nbt(); }
        public void putString(String k,String n) { values.put(k,n); } public String getString(String k) { return values.get(k) instanceof String n?n:""; }
        public void putInt(String k,int n) { values.put(k,n); } public int getInt(String k) { return (int)getLong(k); }
        public void putLong(String k,long n) { values.put(k,n); } public long getLong(String k) { return values.get(k) instanceof Number n?n.longValue():0; }
        public void putLongArray(String k,long[] n) { values.put(k,n); } public long[] getLongArray(String k) { return values.get(k) instanceof long[] n?n:new long[0]; }
    }
    public static class Screen {
        public PlayerNavigation0690SmokeTest.Font font=new PlayerNavigation0690SmokeTest.Font(); public List<String> lines=new ArrayList<>();
        public void text(Object graphics,String text,int x,int y,int color) {
            check(x>=26 && x+font.width(text)<=514 && y>=91 && y+9<=288,"Detail out of bounds: "+text+" at "+x+","+y); lines.add(text);
        }
    }
}
