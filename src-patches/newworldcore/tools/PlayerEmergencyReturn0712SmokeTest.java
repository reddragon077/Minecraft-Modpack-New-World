import java.io.*;
import java.util.*;
import org.objectweb.asm.*;
import net.newworld.player.*;

/** Executes the shipped return adapter; only external Minecraft/Doctor boundaries are fixtures. */
public final class PlayerEmergencyReturn0712SmokeTest {
    static final String ADAPTER="net.newworld.player.PlayerEmergencyReturn0712";
    static Manager manager;static Player player;static World world;static boolean veto;
    static void check(boolean b,String s){if(!b)throw new AssertionError(s);}
    public record Pos(int getX,int getY,int getZ){
        public static final Pos ZERO=new Pos(0,0,0);
        public Pos above(){return new Pos(getX,getY+1,getZ);}public Pos below(){return new Pos(getX,getY-1,getZ);}
        public Pos offset(Pos p){return new Pos(getX+p.getX,getY+p.getY,getZ+p.getZ);}
        public Pos rotate(String rotation){return rotation.equals("near")?this:new Pos(-getX,getY,-getZ);}
        public Pos relative(Direction d){return new Pos(getX+d.x,getY,getZ+d.z);}
    }
    public record Vector(double x,double y,double z){}
    public record Box(double x,double y,double z,double x2,double y2,double z2){}
    public enum Direction{
        UP(0,0,0),SOUTH(0,1,0),WEST(-1,0,90),NORTH(0,-1,180),EAST(1,0,270);
        final int x,z;final float yaw;Direction(int x,int z,float yaw){this.x=x;this.z=z;this.yaw=yaw;}
        public float toYRot(){return yaw;}
    }
    public static class Tile{}
    public static class SystemState{public boolean busy;public boolean inProgress(){return busy;}}
    public static class Manager{
        public UUID owner=UUID.randomUUID();public boolean broken;public SystemState system=new SystemState();
        public Pos entrance=new Pos(100,52,100);public Direction facing=Direction.SOUTH;public Tile door=new Tile();
        public Pos getEntrancePosition(){return entrance;}public Direction getEntranceFacing(){return facing;}
        public Tile getMainInteriorDoorsTile(){return door;}
        public UUID getOwner(){return owner;}public String getId(){return "ship";}public World getWorld(){return world;}
        public boolean isBroken(){return broken;}public SystemState getSystem(Class<?> c){return system;}
    }
    public static class State{
        final String id;State(String id){this.id=id;}public boolean isAir(){return id.equals("air");}
        public Fluid getFluidState(){return new Fluid();}public String getBlock(){return id;}
        public boolean isFaceSturdy(World w,Pos p,Direction d){return !isAir();}
    }
    public static class Fluid{public boolean isEmpty(){return true;}}
    public static class Border{public boolean isWithinBounds(Pos p){return true;}}
    public interface WorldReader {
        default boolean hasChunkAt(Pos p){return ((World)this).loaded&&!p.equals(((World)this).unloaded);}
        default boolean noCollision(Player p,Box b){return !((World)this).collision;}
    }
    public static class World implements WorldReader{
        public boolean loaded=true,blocked,collision,missingDoor,hazard;public Pos unloaded;
        public Tile getBlockEntity(Pos p){return !missingDoor&&p.equals(manager.entrance)?manager.door:null;}
        public Border getWorldBorder(){return new Border();}
        public State getBlockState(Pos p){return new State(blocked&&p.equals(manager.entrance.relative(manager.facing))?"solid":p.getY==manager.entrance.getY-1?(hazard?"magma_block":"stone"):"air");}
    }
    public static class Player extends PlayerEmergency0710SmokeTest.Player{
        public Object level=new Object();public boolean alive=true,mounted;public double x,y,z;public float fallDistance=20,yaw=-37;
        public UUID getUUID(){return manager.owner;}public Object serverLevel(){return level;}
        public boolean isAlive(){return alive;}public boolean isRemoved(){return false;}public boolean isPassenger(){return mounted;}
        public boolean isVehicle(){return false;}public boolean isSleeping(){return false;}
        public float getYRot(){return 0;}public double getX(){return x;}public double getY(){return y;}public double getZ(){return z;}
        public void setDeltaMovement(double a,double b,double c){}public void clearFire(){}
        public Server getServer(){return new Server();}
    }
    public static class Server{public Server getPlayerList(){return this;}public void save(Player p){}}
    public static class Teleports{
        public static Player teleport(Player p,World w,Vector v,float yaw,float pitch){
            if(veto)return null;p.level=w;p.x=v.x;p.y=v.y;p.z=v.z;p.yaw=yaw;return p;
        }
    }
    public static PlayerShipLink0680.Resolution resolve(Object p){return new PlayerShipLink0680.Resolution(new PlayerShipLink0680.Ship("ship",manager),
        new PlayerShipLink0680.Snapshot("LOST","ship","minecraft:overworld",6000,"OUT OF RANGE",20,120));}
    static final Map<String,String> TYPES=Map.ofEntries(
        Map.entry("net.minecraft.core.BlockPos",Pos.class.getName()),Map.entry("net.minecraft.core.Direction",Direction.class.getName()),
        Map.entry("net.minecraft.world.phys.Vec3",Vector.class.getName()),Map.entry("net.minecraft.world.phys.AABB",Box.class.getName()),
        Map.entry("net.minecraft.world.entity.Entity",Player.class.getName()),Map.entry("net.minecraft.server.level.ServerLevel",World.class.getName()),
        Map.entry("net.drgmes.dwm.utils.helpers.EntityHelper",Teleports.class.getName()));
    static class Loader extends ClassLoader{
        Loader(){super(PlayerEmergencyReturn0712SmokeTest.class.getClassLoader());}
        protected Class<?> loadClass(String name,boolean resolve)throws ClassNotFoundException{
            if(!name.startsWith(ADAPTER)&&!name.equals("net.newworld.player.PlayerOverview0670"))return super.loadClass(name,resolve);
            Class<?> c=findLoadedClass(name);if(c!=null)return c;
            try(var in=getParent().getResourceAsStream(name.replace('.','/')+".class")){
                ClassWriter writer=new ClassWriter(0);new ClassReader(in).accept(new ClassVisitor(Opcodes.ASM9,writer){
                    public MethodVisitor visitMethod(int a,String n,String d,String s,String[] e){return new MethodVisitor(Opcodes.ASM9,super.visitMethod(a,n,d,s,e)){
                        public void visitLdcInsn(Object value){
                            if(value instanceof String t)value=t.equals("net.drgmes.dwm.common.tardis.systems.")?"":TYPES.getOrDefault(t,t);
                            if(value instanceof String t&&(t.endsWith(".TardisSystemFlight")||t.endsWith(".TardisSystemConsoleRoom")))value=SystemState.class.getName();
                            super.visitLdcInsn(value);
                        }
                        public void visitMethodInsn(int op,String owner,String method,String desc,boolean itf){
                            if(owner.equals("net/newworld/player/PlayerShipLink0680")&&method.equals("resolve"))owner="PlayerEmergencyReturn0712SmokeTest";
                            super.visitMethodInsn(op,owner,method,desc,itf);
                        }
                    };}
                },0);byte[] bytes=writer.toByteArray();c=defineClass(name,bytes,0,bytes.length);if(resolve)resolveClass(c);return c;
            }catch(Exception e){throw new ClassNotFoundException(name,e);}
        }
    }
    public static void main(String[] args)throws Exception{
        if(args.length>0)inspectDoctor(args[0]);
        Class<?> adapter=new Loader().loadClass(ADAPTER);world=new World();manager=new Manager();player=new Player();
        var destination=adapter.getMethod("destination",Object.class,Object.class,Object.class);
        for(Pos entrance:List.of(new Pos(100,52,100),new Pos(-19,128,-27)))for(Direction facing:List.of(Direction.SOUTH,Direction.WEST,Direction.NORTH,Direction.EAST)){
            manager.entrance=entrance;manager.facing=facing;
            check(destination.invoke(null,player,manager,world).equals(entrance.relative(facing)),"exact portal arrival for moved/rotated entrance");
        }
        Pos pos=(Pos)destination.invoke(null,player,manager,world);
        for(Pos unavailable:List.of(manager.entrance,pos.below(),pos,pos.above())){
            world.unloaded=unavailable;check(destination.invoke(null,player,manager,world)==null,"each required cell must be loaded");
        }world.unloaded=null;
        world.loaded=false;check(destination.invoke(null,player,manager,world)==null,"unloaded room");world.loaded=true;
        world.blocked=true;check(destination.invoke(null,player,manager,world)==null,"blocked entrance must not fall back to safe neighboring cells");world.blocked=false;
        world.hazard=true;check(destination.invoke(null,player,manager,world)==null,"hazard floor");world.hazard=false;
        world.collision=true;check(destination.invoke(null,player,manager,world)==null,"entity collision");world.collision=false;
        world.missingDoor=true;check(destination.invoke(null,player,manager,world)==null,"missing live entrance door");world.missingDoor=false;
        Tile door=manager.door;manager.door=null;check(destination.invoke(null,player,manager,world)==null,"missing registered entrance door");manager.door=door;
        var execute=adapter.getMethod("execute",Object.class,PlayerEmergency0710.Context.class);
        var context=new PlayerEmergency0710.Context("ship",world,"minecraft:overworld",1,2,3,"READY");
        world.blocked=true;check(execute.invoke(null,player,context).equals(PlayerEmergency0710.FULL),"unsafe entrance result");world.blocked=false;
        check(PlayerEmergencyReturn0712.remaining(player,System.currentTimeMillis())==0,"unsafe entrance charged cooldown");
        veto=true;check(execute.invoke(null,player,context).equals(PlayerEmergency0710.FAILED),"travel veto");
        check(PlayerEmergencyReturn0712.remaining(player,System.currentTimeMillis())==0,"veto charged cooldown");veto=false;
        manager.system.busy=true;check(execute.invoke(null,player,context).equals(PlayerEmergency0710.DENIED),"busy ship");manager.system.busy=false;
        check(execute.invoke(null,player,context).equals(PlayerEmergency0710.SENT),"return success");
        check(player.level==world&&player.fallDistance==0,"safe arrival cleanup");
        check(player.x==pos.getX+.5&&player.y==pos.getY&&player.z==pos.getZ+.5&&player.yaw==manager.facing.toYRot(),"portal bottom-center coordinates and facing");
        check(PlayerEmergencyReturn0712.remaining(player,System.currentTimeMillis())>1799000,"persisted success cooldown");
        player.level=new Object();check(execute.invoke(null,player,context).equals(PlayerEmergency0710.WAIT),"repeat return");
        System.out.println("Emergency production adapter: exact portal position/facing in four directions, missing/blocked/unloaded/hazard/collision entrance, no fallback, travel veto, busy ship and persisted success cooldown passed.");
    }
    static void inspectDoctor(String jar)throws Exception{
        try(var zip=new java.util.zip.ZipFile(jar)){
            byte[] exterior=zip.getInputStream(zip.getEntry("net/drgmes/dwm/blocks/tardis/exteriors/BaseTardisExteriorBlock.class")).readAllBytes();
            List<String> calls=new ArrayList<>();new ClassReader(exterior).accept(new ClassVisitor(Opcodes.ASM9){
                public MethodVisitor visitMethod(int a,String n,String d,String s,String[] ex){if(!n.equals("getPortalDestination"))return null;
                    return new MethodVisitor(Opcodes.ASM9){public void visitMethodInsn(int op,String owner,String method,String desc,boolean itf){
                        if(owner.equals("net/drgmes/dwm/common/tardis/TardisStateManager")||owner.equals("net/minecraft/core/BlockPos")||owner.equals("net/minecraft/core/Direction")||owner.equals("net/minecraft/world/phys/Vec3"))calls.add(method+desc);
                    }};
                }
            },0);
            List<String> expected=List.of("getEntranceFacing()Lnet/minecraft/core/Direction;","getWorld()Lnet/minecraft/server/level/ServerLevel;",
                "getEntrancePosition()Lnet/minecraft/core/BlockPos;","relative(Lnet/minecraft/core/Direction;)Lnet/minecraft/core/BlockPos;",
                "atBottomCenterOf(Lnet/minecraft/core/Vec3i;)Lnet/minecraft/world/phys/Vec3;","toYRot()F");
            check(Collections.indexOfSubList(calls,expected)>=0,"Doctor entrance portal position/facing contract changed: "+calls);
            System.out.println("Actual Doctor portal contract: entrance.relative(facing), bottom-center arrival, facing.toYRot passed.");
        }
    }
}
