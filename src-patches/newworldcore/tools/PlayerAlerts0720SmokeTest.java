import java.nio.file.*;
import java.util.*;
import org.objectweb.asm.*;
import net.newworld.config.NewWorldConfig;
import net.newworld.player.*;
import net.newworld.player.PlayerAlerts0720.*;
import static net.newworld.player.PlayerAlerts0720.*;

public final class PlayerAlerts0720SmokeTest {
    static void check(boolean b,String s){if(!b)throw new AssertionError(s);}
    static void eq(Object a,Object b){check(Objects.equals(a,b),a+" != "+b);}
    public static void main(String[] args)throws Exception{
        Path root=Files.createTempDirectory("ship-alerts-"),config=root.resolve("ship-alerts.properties"),overview=root.resolve("overview.properties");
        System.setProperty("newworldcore.configDir",root.toString());NewWorldConfig.reload();
        try{
            check(enabled()&&notifications(),"enabled defaults");eq(refreshTicks(),40);eq(staleTicks(),120);eq(historyLimit(),16);
            check(PlayerGeologicalSurveyGui0620.isReadOnlyPoll(MODE),"Background poll must work without GUI link state");
            for(int mode:List.of(0,1,2,3,6,7,8,9))check(!PlayerGeologicalSurveyGui0620.isReadOnlyPoll(mode),"Action link bypass");
            eq(warningPercent(),20);eq(criticalPercent(),5);eq(bufferPercent(),80);eq(noticeCooldown(),60);eq(noticeSpacing(),4);
            Files.writeString(config,"enabled=false\nnotifications.enabled=false\nrefresh_ticks=0\nhistory_limit=0\nwarp.warning_percent=10\nwarp.critical_percent=99\ncollection.warning_percent=999\nnotifications.repeat_seconds=0\nnotifications.spacing_seconds=0\n");NewWorldConfig.reload();
            Files.writeString(overview,"warning_percent=10\ncritical_percent=99\n");NewWorldConfig.reload();
            check(!enabled()&&!notifications(),"live toggles");eq(refreshTicks(),20);eq(historyLimit(),4);eq(criticalPercent(),10);eq(bufferPercent(),99);eq(noticeCooldown(),5);eq(noticeSpacing(),3);
            Files.writeString(overview,"");NewWorldConfig.reload();
            Files.writeString(config,"enabled=bad\nrefresh_ticks=99999\nhistory_limit=999\nwarp.warning_percent=oops\nnotifications.repeat_seconds=99999\nnotifications.spacing_seconds=999\n");NewWorldConfig.reload();
            check(enabled(),"fallback");eq(refreshTicks(),1200);eq(staleTicks(),3600);eq(historyLimit(),32);eq(warningPercent(),20);eq(noticeCooldown(),3600);eq(noticeSpacing(),30);
            Files.writeString(config,"");NewWorldConfig.reload();
            Files.writeString(config,"warp.warning_percent=99\nwarp.critical_percent=99\n");NewWorldConfig.reload();
            eq(lowEnergy(21,100),0);eq(warningPercent(),PlayerOverview0670.warningPercent());
            Files.writeString(config,"");NewWorldConfig.reload();
            eq(lowEnergy(20,100),1);eq(lowEnergy(5,100),2);eq(lowEnergy(21,100),0);eq(lowEnergy(-1,100),-1);eq(lowEnergy(0,0),-1);eq(lowEnergy(Long.MAX_VALUE,Long.MAX_VALUE),0);
            eq(collection("100M ITEMS / 205/256 TYPES"),1);eq(collection("256 ITEMS / 256/256 TYPES"),2);
            eq(collection("100M ITEMS / 1/256 TYPES"),0);eq(collection("UNLOADED"),-1);eq(collection("NOT BOUND"),-1);
            check(sample(new Object(),null,"ship").values().stream().allMatch(v->v==-1),"unloaded interior must be unknown");
            eq(drive(0,null),0);eq(drive(1,null),-1);eq(drive(1,new int[5]),2);eq(drive(2,new int[]{0,0,0,1,0}),2);
            eq(drive(1,new int[]{0,0,0,1,0}),0);eq(drive(1,new int[]{0,0,0,0,1}),0);eq(drive(2,new int[]{0,0,0,0,1}),0);
            History h=new History();h.update(Map.of(Kind.WARP,0),1);eq(h.events.size(),0);
            h.update(Map.of(Kind.WARP,1),2);h.update(Map.of(Kind.WARP,1),3);eq(h.events.size(),1);
            h.update(Map.of(Kind.WARP,-1),4);h.update(Map.of(),5);eq(h.events.size(),1);
            h.update(Map.of(Kind.WARP,2),6);h.update(Map.of(Kind.WARP,0),7);eq(h.events.size(),3);eq(h.events.getFirst().severity(),0);
            for(int i=0;i<99;i++)h.update(Map.of(Kind.WARP,i%3),8+i);eq(h.events.size(),16);
            unified();wire(h);notices();layout();adapter();audit();
            System.out.println("Ship Alerts thresholds/type-capacity/drives, unknown-preserving transitions, bounded history, live config, codec, notification dedup/escalation/link recovery, UI and tick/payload hooks passed.");
        }finally{Files.deleteIfExists(config);Files.deleteIfExists(overview);Files.deleteIfExists(root);}
    }
    static PlayerOverview0670.Inspection inspection(Map<String,Integer> active,boolean unknown){
        return new PlayerOverview0670.Inspection(new PlayerOverview0670.Snapshot("ship",unknown?-1:11,100,unknown?-1:100,100,
            "SAMPLING",unknown?"UNKNOWN":"READY","LOCKED",unknown?"UNKNOWN":"ON",unknown?"UNKNOWN":"MINING","NO TARGET",
            unknown?"UNKNOWN":"ONLINE",unknown?"UNKNOWN":"ONLINE",unknown?"UNKNOWN":"ONLINE","earth","0,0,0","WARNING",List.of("clipped display row")),active);
    }
    static void unified(){
        var values=new EnumMap<Kind,Integer>(Kind.class);
        var all=new LinkedHashMap<String,Integer>();
        for(String s:List.of("FE LEVEL LOW","WARP LEVEL LOW","FE MATRIX OFFLINE","WARP MATRIX OFFLINE","ENGINE MATRIX OFFLINE",
            "ENGINE BROKEN","ENGINE UNKNOWN","ENGINE TELEMETRY UNAVAILABLE","MINING BUFFER_FULL","MINING NO_ENERGY",
            "MINING WAITING_FOR_HANDBRAKE","MINING WAITING_FOR_MODULE","FE CONSUMPTION METER UNAVAILABLE","FE TELEMETRY UNAVAILABLE",
            "WARP TELEMETRY UNAVAILABLE","FE MATRIX UNKNOWN","WARP MATRIX UNKNOWN","ENGINE MATRIX UNKNOWN","MINING TELEMETRY UNAVAILABLE",
            "NAVIGATION TELEMETRY UNAVAILABLE","EXTERIOR POSITION UNAVAILABLE","PARTIAL TELEMETRY / CHECK LOG")){
            check(overviewKind(s)!=Kind.OTHER_OVERVIEW,"unmapped Overview warning "+s);all.put(s,1);
        }
        values.put(Kind.COLLECTION,2);values.put(Kind.DRIVE,2);mergeOverview(values,inspection(all,false));
        for(Kind k:Kind.values())if(k!=Kind.OTHER_OVERVIEW)check(values.get(k)>0,"warning lost beyond display rows: "+k);
        eq(values.get(Kind.COLLECTION),2);eq(values.get(Kind.DRIVE),2);
        mergeOverview(values,inspection(Map.of("FUTURE WARNING",1),false));eq(values.get(Kind.OTHER_OVERVIEW),1);
        History h=new History();mergeOverview(values,inspection(Map.of("FE LEVEL LOW",1,"MINING NO_ENERGY",2),false));h.update(values,1);
        eq(h.values.get(Kind.FE),1);eq(h.values.get(Kind.MINING_ENERGY),2);
        mergeOverview(values,inspection(Map.of("FE TELEMETRY UNAVAILABLE",1,"MINING TELEMETRY UNAVAILABLE",1),true));h.update(values,2);
        eq(h.values.get(Kind.FE),1);eq(h.values.get(Kind.MINING_ENERGY),2);eq(h.values.get(Kind.FE_DATA),1);
        check(h.events.stream().noneMatch(e->e.kind()==Kind.FE&&e.severity()==0),"unknown resolved low FE");
        mergeOverview(values,inspection(Map.of(),false));h.update(values,3);eq(h.values.get(Kind.FE),0);eq(h.values.get(Kind.FE_DATA),0);
    }
    static Snapshot snap(List<Event> events){return new Snapshot("ship","1 ACTIVE / 0 UNKNOWN",40,120,0,events);}
    static void wire(History h)throws Exception{
        resetClient();Snapshot s=snap(h.events);
        for(int code:encode(s)){
            check(!PlayerOverview0670.isWireCode(code)&&!PlayerMining0700.isWireCode(code)&&!PlayerShipLink0680.isWireCode(code)
                &&!PlayerEmergency0710.isWireCode(code)&&!PlayerNavigation0690.isWireCode(code)&&!PlayerDiscoveries0650.isActionMode(code),"wire collision");
            check(accept(code),"codec code rejected");
        }eq(clientSnapshot(),s);accept(BEGIN);accept(END);eq(clientSnapshot(),s);
        accept(BEGIN);for(int i=0;i<1600;i++)accept(WIRE_BASE);accept(END);eq(clientSnapshot(),s);
        for(int code:encode(new Snapshot("bad","bad",1,1,0,List.of())))accept(code);eq(clientSnapshot(),s);
        for(int code:encode(snap(List.of(new Event(1,1,Kind.WARP,3)))))accept(code);eq(clientSnapshot(),s);
        check(!fresh(Long.MAX_VALUE),"stale visible");resetClient();eq(clientSnapshot(),null);
        // Production payload adapter, not just direct codec calls.
        for(int code:encode(s))PlayerDiscoveries0650.handlePayload(new Payload(code),new Context());eq(clientSnapshot(),s);
        Snapshot high=new Snapshot("ship","extended mask",40,120,15,1<<Kind.MINING_WAIT.ordinal(),List.of(new Event(100,1,Kind.MINING_WAIT,1)));
        for(int code:encode(high))accept(code);eq(clientSnapshot(),high);
        for(int code:encode(new Snapshot("bad","bad",40,120,0,-1,List.of())))accept(code);eq(clientSnapshot(),high);
    }
    public record Payload(int mode){}
    public static class Context{public String flow(){return "CLIENTBOUND";}}
    static void notices(){
        resetClient();long t=100_000_000_000L;
        Event warning=new Event(1,1,Kind.WARP,1);receive(snap(List.of(warning)),t);eq(nextNotice(t),warning);
        receive(snap(List.of(warning)),t+5_000_000_000L);eq(nextNotice(t+5_000_000_000L),null);
        Event critical=new Event(2,2,Kind.WARP,2);receive(snap(List.of(critical,warning)),t+6_000_000_000L);eq(nextNotice(t+6_000_000_000L),critical);
        receive(new Snapshot("","LINK UNAVAILABLE",40,120,0,List.of()),t+7_000_000_000L);
        receive(snap(List.of(critical,warning)),t+8_000_000_000L);eq(nextNotice(t+12_000_000_000L),null);
        Event recovery=new Event(3,3,Kind.WARP,0);receive(snap(List.of(recovery,critical)),t+13_000_000_000L);eq(nextNotice(t+13_000_000_000L),null);
        receive(snap(List.of(new Event(4,4,Kind.WARP,1),recovery)),t+14_000_000_000L);eq(nextNotice(t+14_000_000_000L),null);
        resetClient();receive(snap(List.of(recovery,critical,warning)),t);eq(nextNotice(t),null);
        resetClient();receive(snap(List.of(critical)),t);receive(snap(List.of(recovery,critical)),t+1);eq(nextNotice(t+5_000_000_000L),null);
        resetClient();receive(new Snapshot("ship","UNKNOWN",40,120,1,254,List.of(critical)),t);eq(nextNotice(t),null);
        resetClient();Event fe=new Event(5,1,Kind.FE,1);receive(snap(List.of(fe)),t);eq(nextNotice(t),fe);
    }
    public static class Font{public int width(String s){return s.length()*6;}}
    public static class Screen{
        public int width=540,height=300,tab=0;public Font font=new Font();public List<String> lines=new ArrayList<>();
        public void text(Object g,String s,int x,int y,int color){check(x>=26&&x+font.width(s)<=514&&y>=91&&y+9<=288,"text bounds "+s);lines.add(s);}
    }
    public static class Graphics{public void fill(int x,int y,int x2,int y2,int color){check(x>=10&&x2<=530&&y>=76&&y2<=288,"fill bounds");}}
    static void layout()throws Exception{
        Screen s=new Screen();Graphics g=new Graphics();resetClient();check(!draw(s,g,0,0),"unexpected detail");
        check(mouseClicked(s,400,235,0),"history button");check(draw(s,g,0,0),"detail missing");
        check(s.lines.stream().anyMatch(t->t.contains("unavailable")),"missing stale explanation");
        receive(snap(List.of(new Event(1,1,Kind.WARP,2))),System.nanoTime());s.lines.clear();draw(s,g,0,0);
        check(s.lines.stream().anyMatch(t->t.contains("CRITICAL")),"event hidden");
        check(mouseClicked(s,450,265,0),"next");draw(s,g,0,0);
        check(mouseClicked(s,30,265,0),"back");check(!draw(s,g,0,0),"back failed");
    }
    static void audit()throws Exception{
        final int[] hooks={0};
        try(var in=PlayerAlerts0720.class.getClassLoader().getResourceAsStream("net/newworld/player/PlayerShipClientEvents.class")){
            new ClassReader(in).accept(new ClassVisitor(Opcodes.ASM9){public MethodVisitor visitMethod(int a,String n,String d,String s,String[] e){
                if(!n.equals("onClientTick"))return null;
                return new MethodVisitor(Opcodes.ASM9){public void visitMethodInsn(int o,String owner,String method,String desc,boolean itf){
                    if(owner.equals("net/newworld/player/PlayerAlerts0720")&&method.equals("tick"))hooks[0]++;
                }};
            }},0);
        }eq(hooks[0],1);
        // No world/action mutators may be introduced into the alert implementation.
        try(var in=PlayerAlerts0720.class.getResourceAsStream("PlayerAlerts0720.class")){
            new ClassReader(in).accept(new ClassVisitor(Opcodes.ASM9){public MethodVisitor visitMethod(int a,String n,String d,String s,String[] e){
                return new MethodVisitor(Opcodes.ASM9){public void visitLdcInsn(Object v){
                    if(v instanceof String name)check(!Set.of("setDirty","setBlock","setSelected","setHandbrakeLocked","setShieldsMiningEnabled","teleport","extractEnergy","consume","prepare","commit").contains(name),"forbidden alert writer "+name);
                }};
            }},0);
        }
    }
    public static class Manager {
        boolean broken;Object from=new Dimension("earth"),to=from;
        public Object getWorld(){return this;}
        public long getGameTime(){return 100;}
        public Object getSystem(Class<?> type){return new Flight();}
        public boolean isHandbrakeLocked(){return true;}
        public boolean isShieldsMiningEnabled(){return true;}
        public Object getCurrentExteriorPosition(){return new Position();}
        public boolean isBroken(){return broken;}
        public Object getCurrentExteriorDimension(){return from;}
        public Object getDestinationExteriorDimension(){return to;}
    }
    public record Dimension(String location){}
    public static class Position{public int getX(){return 0;}public int getY(){return 64;}public int getZ(){return 0;}}
    public static class Flight{public boolean inProgress(){return false;}public long newWorld$getCooldownUntil(){return 0;}}
    public static class FEnergy{static long energy=11;public static long getEnergy(Object w,String id){return energy;}public static long getCapacity(Object w,String id){return 100;}public static Object data(Object w){return w;}}
    public static class Registry{public static Map<String,Object> STATS=new HashMap<>();}
    public static class NavData{public Map<String,Object> ships=new HashMap<>();public static NavData get(Object w){return new NavData();}}
    public static class Plans{public static Map<String,Object> PLANS=new HashMap<>();}
    public record Room(boolean active,int[] modules){}
    public static class Energy {public static long getEnergy(Object w){return 5;}public static long capacityForShip(String id){return 100;}}
    public static class Travel {public static int travelClass(Object a,Object b){return 2;}}
    public static class Runtime {public String status="BUFFER_FULL";}
    public static class MiningData {public Map<String,Object> states=new HashMap<>(Map.of("ship",new Runtime()));public static MiningData get(Object w){return new MiningData();}}
    static Manager manager=new Manager();static Room room=new Room(true,new int[5]);static boolean allowed=true;
    static final List<Integer> sent=new ArrayList<>();
    public static Object room(String id,String type){return room;}
    public static String buffer(Object a,Object b,String c,String d){return "999 ITEMS / 256/256 TYPES";}
    public static void sendResult(Object p,int code){sent.add(code);}
    public static PlayerShipLink0680.Resolution resolve(Object p){return new PlayerShipLink0680.Resolution(new PlayerShipLink0680.Ship("ship",manager),
            new PlayerShipLink0680.Snapshot(allowed?"CONNECTED":"LOST","ship","earth",1,"test",20,120));}
    static void adapter()throws Exception{
        String name="net.newworld.player.PlayerAlerts0720";
        ClassLoader loader=new ClassLoader(PlayerAlerts0720SmokeTest.class.getClassLoader()){
            protected Class<?> loadClass(String n,boolean resolve)throws ClassNotFoundException{
                if(!n.startsWith(name)&&!n.startsWith("net.newworld.player.PlayerOverview0670"))return super.loadClass(n,resolve);
                Class<?> c=findLoadedClass(n);if(c!=null)return c;
                try(var in=getParent().getResourceAsStream(n.replace('.','/')+".class")){
                    ClassWriter w=new ClassWriter(0);new ClassReader(in).accept(new ClassVisitor(Opcodes.ASM9,w){
                        public MethodVisitor visitMethod(int a,String mn,String d,String s,String[] e){return new MethodVisitor(Opcodes.ASM9,super.visitMethod(a,mn,d,s,e)){
                            public void visitLdcInsn(Object v){
                                if(v instanceof String t)v=switch(t){
                                    case "net.newworld.core.WarpEnergySystem"->Energy.class.getName();
                                    case "net.newworld.core.LongFEEnergySystem"->FEnergy.class.getName();
                                    case "net.newworld.core.ShipRoomRegistry"->Registry.class.getName();
                                    case "net.drgmes.dwm.common.tardis.systems.TardisSystemFlight"->Flight.class.getName();
                                    case "net.newworld.navigation.NavigationDiscoverySavedData"->NavData.class.getName();
                                    case "net.newworld.navigation.Navigation0472ServerRoute"->Plans.class.getName();
                                    case "net.drgmes.dwm.newworld.EngineTravelBalance"->Travel.class.getName();
                                    case "net.newworld.mining.MiningRuntimeSavedData"->MiningData.class.getName();
                                    case "net.newworld.player.PlayerFieldSurvey0504Bridge"->PlayerAlerts0720SmokeTest.class.getName();
                                    default->t;
                                };super.visitLdcInsn(v);
                            }
                            public void visitMethodInsn(int op,String owner,String method,String desc,boolean itf){
                                if(owner.equals("net/newworld/player/PlayerShipLink0680")&&method.equals("resolve")
                                    ||owner.equals("net/newworld/player/PlayerAlerts0720")&&method.equals("room")
                                    ||owner.equals("net/newworld/player/PlayerMining0700")&&method.equals("buffer"))owner="PlayerAlerts0720SmokeTest";
                                super.visitMethodInsn(op,owner,method,desc,itf);
                            }
                        };}
                    },0);byte[] bytes=w.toByteArray();c=defineClass(n,bytes,0,bytes.length);if(resolve)resolveClass(c);return c;
                }catch(Exception e){throw new ClassNotFoundException(n,e);}
            }
        };
        // Production adapter and its package-private reflection helpers share a loader;
        // only the external game/registry/buffer boundaries above are fixtures.
        Class<?> c=loader.loadClass(name);
        var sample=c.getMethod("sample",Object.class,Object.class,String.class);
        var values=(Map<?,?>)sample.invoke(null,manager,manager,"ship");
        Map<String,Integer> mapped=new HashMap<>();values.forEach((k,v)->mapped.put(k.toString(),(Integer)v));
        eq(mapped.get("COLLECTION"),2);eq(mapped.get("DRIVE"),0);
        manager.to="other";manager.broken=true;values=(Map<?,?>)sample.invoke(null,manager,manager,"ship");
        mapped.clear();values.forEach((k,v)->mapped.put(k.toString(),(Integer)v));eq(mapped.get("DRIVE"),2);
        room=null;values=(Map<?,?>)sample.invoke(null,manager,manager,"ship");mapped.clear();values.forEach((k,v)->mapped.put(k.toString(),(Integer)v));eq(mapped.get("DRIVE"),-1);eq(mapped.get("ENGINE_MATRIX"),-1);
        room=new Room(true,new int[]{0,0,0,0,1});
        for(String type:List.of("FE","WARP","ENGINE"))Registry.STATS.put("ship:"+type,room);
        var request=c.getMethod("request",Object.class);allowed=false;sent.clear();request.invoke(null,new Object());resetClient();for(int code:sent)accept(code);check(clientSnapshot()!=null&&clientSnapshot().ship().isEmpty(),"denied link leaked");
        allowed=true;sent.clear();Object p=new Object();request.invoke(null,p);for(int code:sent)accept(code);eq(clientSnapshot().ship(),"ship");
        Class<?> overviewClass=loader.loadClass("net.newworld.player.PlayerOverview0670");
        var displayViews=overviewClass.getDeclaredField("SERVER");displayViews.setAccessible(true);
        check(!((Map<?,?>)displayViews.get(null)).containsKey(p),"background poll reset visible FE/t sampling clock");
        var events=clientSnapshot().events();
        for(Kind k:List.of(Kind.FE,Kind.WARP,Kind.ENGINE,Kind.BLOCKED,Kind.COLLECTION))check(events.stream().anyMatch(e->e.kind()==k&&e.severity()>0),"production request lost "+k);
        eq(events.stream().filter(e->e.kind()==Kind.FE).findFirst().orElseThrow().severity(),1);
        eq(events.stream().filter(e->e.kind()==Kind.BLOCKED).findFirst().orElseThrow().severity(),1);
        sent.clear();request.invoke(null,p);eq(sent.size(),0);
        FEnergy.energy=-1;poll(c,request,p);
        check(clientSnapshot().events().stream().anyMatch(e->e.kind()==Kind.FE_DATA&&e.severity()==1),"missing FE telemetry warning");
        check(clientSnapshot().events().stream().noneMatch(e->e.kind()==Kind.FE&&e.severity()==0),"production unknown falsely resolved FE");
        FEnergy.energy=100;poll(c,request,p);
        for(Kind k:List.of(Kind.FE,Kind.FE_DATA))check(clientSnapshot().events().stream().anyMatch(e->e.kind()==k&&e.severity()==0),"production recovery lost "+k);
    }
    static void poll(Class<?> c,java.lang.reflect.Method request,Object p)throws Exception{
        var sf=c.getDeclaredField("SERVER");sf.setAccessible(true);Object state=((Map<?,?>)sf.get(null)).get(p);
        var throttle=state.getClass().getDeclaredField("requested");throttle.setAccessible(true);throttle.setLong(state,0);
        sent.clear();request.invoke(null,p);for(int code:sent)accept(code);
    }
}
