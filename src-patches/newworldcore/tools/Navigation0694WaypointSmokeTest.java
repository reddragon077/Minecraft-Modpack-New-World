import java.io.*;
import java.util.*;
import org.objectweb.asm.*;
import net.newworld.navigation.Navigation0694WaypointFix;

/** Runs the shipped calculator with only external game/height/flight boundaries replaced. */
public final class Navigation0694WaypointSmokeTest {
    static final String ROUTE = "net.newworld.navigation.Navigation0472ServerRoute";
    static final String FIX = "net.newworld.navigation.Navigation0694WaypointFix";
    static final String TARGET = "net.newworld.navigation.Navigation0471gFix";
    static final String AUTOPILOT = "net.newworld.navigation.Navigation0473HopAutopilot";
    static final String RANGE = "net.newworld.navigation.Navigation0476aRangeGuard";
    static final String SELF = "Navigation0694WaypointSmokeTest";
    public static Record selected = new Record();
    public static Manager manager = new Manager();
    public static Object loaded;
    static void check(boolean v, String message) { if (!v) throw new AssertionError(message); }
    static Object field(Object o, String n) throws Exception { var f=o.getClass().getDeclaredField(n); f.setAccessible(true); return f.get(o); }
    static Object invoke(Class<?> c, String n, Class<?>[] p, Object... a) throws Exception {var m=c.getDeclaredMethod(n,p);m.setAccessible(true);return m.invoke(null,a);}
    public static Object manager(Object ignored) { return manager; }
    public static Object fakeStatic(String type, String name, Object[] args) {
        if (type.endsWith("NavigationDiscoverySavedData") && name.equals("get")) return new Data();
        if (type.endsWith("ShipRoomRegistry") && name.equals("maxRange")) return 512;
        throw new AssertionError(type + '.' + name);
    }
    public static void loaded(Object context, Object plan) throws Exception {
        ClassLoader loader = plan.getClass().getClassLoader();
        invoke(loader.loadClass(RANGE), "clampPlan", new Class<?>[]{plan.getClass()}, plan);
        invoke(loader.loadClass(FIX), "normalizeHopAltitudes", new Class<?>[]{plan.getClass()}, plan);
        loaded = plan;
    }
    public static void dirty(Object ignored) {}
    public static final class Data { public Object selected(String ship) { return selected; } }
    public static final class Context { public Object level = new Object(); }
    public static final class Record {
        public String kind="WAYPOINT", source="MANUAL", label="LOCATION", dimension="minecraft:overworld";
        public int x=-2454, y=63, z=181, distance=0;
    }
    public record Dim(String location) {}
    public record Pos(int getX, int getY, int getZ) {}
    public static final class World { public Dim dimension() { return new Dim("minecraft:overworld"); } }
    public static final class Manager {
        public Pos pos = new Pos(-2464,61,176); public String dim="minecraft:overworld";
        public String getId() { return "test-ship"; }
        public Dim getCurrentExteriorDimension() { return new Dim(dim); }
        public Pos getCurrentExteriorPosition() { return pos; }
        public World getExteriorWorld() { return new World(); }
    }
    public static void main(String[] args) throws Exception {
        check(Navigation0694WaypointFix.distance(selected,manager)==11,"3D live distance");
        manager.pos=new Pos(-2454,63,189); check(Navigation0694WaypointFix.distance(selected,manager)==8,"moving ship refresh");
        manager.pos=new Pos(-2454,63,181); check(Navigation0694WaypointFix.distance(selected,manager)==0,"real zero");
        manager.dim="minecraft:the_nether"; check(Navigation0694WaypointFix.distance(selected,manager)==-1,"cross dimension");
        check(Navigation0694WaypointFix.distance(selected,null)==-2,"unavailable must not be zero");
        check(Navigation0694WaypointFix.distanceText("-1").equals("DIFFERENT DIMENSION"),"dimension text");
        check(Navigation0694WaypointFix.distanceText("-2").equals("DISTANCE UNAVAILABLE"),"unknown text");
        check(Navigation0694WaypointFix.distanceText("11").equals("11 BLOCKS"),"distance text");
        check(Navigation0694WaypointFix.distanceText("bad").equals("DISTANCE UNAVAILABLE"),"invalid text");
        check(selected.distance==0 && selected.y==63,"read-only telemetry changed save");
        Loader loader=new Loader(); Class<?> route=loader.loadClass(ROUTE), fix=loader.loadClass(FIX);
        manager.dim="minecraft:overworld"; manager.pos=new Pos(-2464,61,176);
        calculate(route); assertEnd(63,1);
        selected.x=-1000; selected.y=-30; calculate(route); assertEnd(-30,3);
        var points=(List<?>)field(loaded,"points"); check((int)field(points.get(0),"y")==61,"intermediate cruise policy changed");
        selected.y=-64; calculate(route); assertEnd(-64,3);
        check(loaded.getClass().getMethod("encode").invoke(loaded).toString().contains("-64"),"exact Y not persisted in plan");
        invoke(fix,"normalizeHopAltitudes",new Class<?>[]{loaded.getClass()},loaded);
        assertEnd(61,3); // Outside the fresh manual scope the original fallback still applies.
        selected.x=manager.pos.getX(); selected.z=manager.pos.getZ(); selected.y=100; calculate(route); assertEnd(100,1);
        selected.dimension="minecraft:the_nether"; calculate(route); check((int)field(loaded,"endY")==100,"cross-dimension target Y");
        selected.dimension="minecraft:overworld"; selected.x=-1000; selected.kind="GEOLOGY"; calculate(route); assertEnd(71,3);
        selected.kind="STRUCTURE"; calculate(route); assertEnd(71,3);
        selected.kind="WAYPOINT"; selected.source="RADAR"; calculate(route); assertEnd(71,3);
        selected.source="MANUAL";
        Object shown=invoke(fix,"displayY",new Class<?>[]{Object.class,String.class,int.class,int.class,int.class,Object.class},
                new Object(),selected.dimension,selected.x,selected.z,63,selected);
        check(shown.equals(63),"manual display consulted bad heightmap");
        selected.kind="STRUCTURE";
        shown=invoke(fix,"displayY",new Class<?>[]{Object.class,String.class,int.class,int.class,int.class,Object.class},
                new Object(),selected.dimension,selected.x,selected.z,63,selected);
        check(shown.equals(-64),"legacy display policy changed");
        verifyHooks();
        System.out.println("Waypoint terminal live distance/Y and actual calculator single/multi/vertical/cross-dimension/legacy regression passed.");
    }
    static void calculate(Class<?> route) throws Exception { loaded=null; route.getMethod("calculate",Object.class,Object.class).invoke(null,new Context(),new Object()); check(loaded!=null,"calculator failed"); }
    static void assertEnd(int y,int hops) throws Exception {
        check((int)field(loaded,"endX")==selected.x && (int)field(loaded,"endZ")==selected.z && (int)field(loaded,"endY")==y,"wrong final destination");
        var points=(List<?>)field(loaded,"points"); check(points.size()==hops,"wrong hop count");
        check((int)field(points.getLast(),"y")==y,"last hop Y");
    }
    static byte[] bytes(String name) throws IOException { try(var in=Navigation0694WaypointSmokeTest.class.getClassLoader().getResourceAsStream(name.replace('.','/')+".class")){return in.readAllBytes();} }
    static void verifyHooks() throws Exception {
        Map<String,Integer> calls=new HashMap<>();
        for(String name:List.of(TARGET,ROUTE,AUTOPILOT,"net.newworld.navigation.Navigation0550TerminalV2")) new ClassReader(bytes(name)).accept(new ClassVisitor(Opcodes.ASM9) {
            public MethodVisitor visitMethod(int a,String n,String d,String s,String[] e){return new MethodVisitor(Opcodes.ASM9){
                public void visitMethodInsn(int op,String owner,String method,String desc,boolean itf){if(owner.equals(FIX.replace('.','/')))calls.merge(method,1,Integer::sum);}
            };}
        },0);
        check(calls.getOrDefault("displayY",0)==1 && calls.getOrDefault("terminalDistance",0)==1,"target hook missing");
        check(calls.getOrDefault("routeY",0)==2 && calls.getOrDefault("hopCount",0)==1,"route hook missing");
        check(calls.getOrDefault("loadFirstHop",0)==1 && calls.getOrDefault("normalizeHopAltitudes",0)==1,"loader hook missing");
        check(calls.getOrDefault("distanceText",0)==3,"terminal text hooks missing");
    }
    static final class Loader extends ClassLoader {
        Loader(){super(Navigation0694WaypointSmokeTest.class.getClassLoader());}
        protected Class<?> loadClass(String name,boolean resolve) throws ClassNotFoundException {
            if(!(name.equals(FIX)||name.equals(TARGET)||name.equals(AUTOPILOT)||name.equals(RANGE)||name.startsWith(ROUTE)))return super.loadClass(name,resolve);
            Class<?> c=findLoadedClass(name); if(c==null)try{
                ClassReader reader=new ClassReader(bytes(name));ClassWriter writer=new ClassWriter(0);
                reader.accept(new ClassVisitor(Opcodes.ASM9,writer){
                    public MethodVisitor visitMethod(int a,String n,String d,String s,String[] e){
                        MethodVisitor mv=super.visitMethod(a,n,d,s,e);
                        if(name.equals(AUTOPILOT)&&n.equals("loadFirstHop")){
                            mv.visitCode();mv.visitVarInsn(Opcodes.ALOAD,0);mv.visitVarInsn(Opcodes.ALOAD,1);
                            mv.visitMethodInsn(Opcodes.INVOKESTATIC,SELF,"loaded","(Ljava/lang/Object;Ljava/lang/Object;)V",false);
                            mv.visitInsn(Opcodes.RETURN);mv.visitMaxs(2,2);mv.visitEnd();return null;
                        }
                        String fixture=null;
                        if(name.equals(ROUTE)&&n.equals("tardisManager"))fixture="manager";
                        if(name.equals(ROUTE)&&n.equals("invokeStaticCompatible"))fixture="fakeStatic";
                        if(fixture!=null){mv.visitCode();int local=0;for(Type t:Type.getArgumentTypes(d)){mv.visitVarInsn(Opcodes.ALOAD,local++);}mv.visitMethodInsn(Opcodes.INVOKESTATIC,SELF,fixture,d,false);mv.visitInsn(Opcodes.ARETURN);mv.visitMaxs(3,local);mv.visitEnd();return null;}
                        if(name.equals(ROUTE)&&n.equals("surfaceY")||name.equals(TARGET)&&n.equals("resolveLandingY")){
                            mv.visitCode();mv.visitIntInsn(Opcodes.BIPUSH,name.equals(ROUTE)?71:-64);mv.visitInsn(Opcodes.IRETURN);mv.visitMaxs(1,5);mv.visitEnd();return null;
                        }
                        return new MethodVisitor(Opcodes.ASM9,mv){
                            public void visitMethodInsn(int op,String owner,String method,String desc,boolean itf){
                                if(name.equals(ROUTE)&&n.equals("calculate")&&method.equals("markDirtySavedData")){super.visitMethodInsn(Opcodes.INVOKESTATIC,SELF,"dirty","(Ljava/lang/Object;)V",false);return;}
                                super.visitMethodInsn(op,owner,method,desc,itf);
                            }
                        };
                    }
                },0);byte[] raw=writer.toByteArray();c=defineClass(name,raw,0,raw.length);
            }catch(IOException ex){throw new ClassNotFoundException(name,ex);}
            if(resolve)resolveClass(c);return c;
        }
    }
}
