import java.io.*;
import java.util.*;
import java.util.function.BiConsumer;
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
    }
    public record Vector(double x,double y,double z){}
    public record Box(double x,double y,double z,double x2,double y2,double z2){}
    public static class Settings{public String rot="near";public String getRotation(){return rot;}}
    public static class Direction{public static final Direction UP=new Direction();}
    public static class Builder{public Object getBlock(){return this;}}
    public static class Blocks{public static final Builder TARDIS_ARS_CREATOR=new Builder(),TARDIS_TELEPORTER=new Builder();}
    public static class Tile{}
    public record Info(Pos pos){}
    public static class Template{
        public Pos getSize(){return new Pos(7,6,7);}
        public List<Info> filterBlocks(Pos zero,Settings settings,Object block){return List.of(new Info(new Pos(3,1,3)));}
    }
    public static class Room{
        public Template getTemplate(World w){return new Template();}public Template getTeleporterRoomTemplate(World w){return new Template();}
        public Pos getCenterPosition(){return Pos.ZERO;}
        private void processTeleporterRooms(Pos center,Template t,List<?> creators,BiConsumer<Object,Object> callback){
            Settings shared=new Settings();callback.accept(shared,new Pos(100,50,100));
            shared.rot="far";callback.accept(shared,new Pos(10000,50,10000));
        }
    }
    public static class SystemState{public boolean busy;public boolean inProgress(){return busy;}}
    public static class Manager{
        public UUID owner=UUID.randomUUID();public boolean broken;public SystemState system=new SystemState();
        public UUID getOwner(){return owner;}public String getId(){return "ship";}public World getWorld(){return world;}
        public boolean isBroken(){return broken;}public SystemState getSystem(Class<?> c){return system;}public Room getConsoleRoom(){return new Room();}
    }
    public static class State{
        final String id;State(String id){this.id=id;}public boolean isAir(){return id.equals("air");}
        public Fluid getFluidState(){return new Fluid();}public String getBlock(){return id;}
        public boolean isFaceSturdy(World w,Pos p,Direction d){return !isAir();}
    }
    public static class Fluid{public boolean isEmpty(){return true;}}
    public static class Border{public boolean isWithinBounds(Pos p){return true;}}
    public static class World{
        public boolean loaded=true,blocked,collision,missingPad,hazard;
        public boolean hasChunkAt(Pos p){return loaded;}
        public Tile getBlockEntity(Pos p){return !missingPad&&p.equals(new Pos(103,51,103))?new Tile():null;}
        public Border getWorldBorder(){return new Border();}
        public State getBlockState(Pos p){return new State(blocked?"solid":p.getY==51?(hazard?"magma_block":"stone"):"air");}
        public boolean noCollision(Player p,Box b){return !collision;}
    }
    public static class Player extends PlayerEmergency0710SmokeTest.Player{
        public Object level=new Object();public boolean alive=true,mounted;public double x,y,z;public float fallDistance=20;
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
            if(veto)return null;p.level=w;p.x=v.x;p.y=v.y;p.z=v.z;return p;
        }
    }
    public static PlayerShipLink0680.Resolution resolve(Object p){return new PlayerShipLink0680.Resolution(new PlayerShipLink0680.Ship("ship",manager),
        new PlayerShipLink0680.Snapshot("LOST","ship","minecraft:overworld",6000,"OUT OF RANGE",20,120));}
    static final Map<String,String> TYPES=Map.ofEntries(
        Map.entry("net.minecraft.core.BlockPos",Pos.class.getName()),Map.entry("net.minecraft.core.Direction",Direction.class.getName()),
        Map.entry("net.minecraft.world.phys.Vec3",Vector.class.getName()),Map.entry("net.minecraft.world.phys.AABB",Box.class.getName()),
        Map.entry("net.minecraft.world.entity.Entity",Player.class.getName()),Map.entry("net.minecraft.server.level.ServerLevel",World.class.getName()),
        Map.entry("net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings",Settings.class.getName()),
        Map.entry("net.drgmes.dwm.setup.ModBlocks",Blocks.class.getName()),Map.entry("net.drgmes.dwm.utils.helpers.EntityHelper",Teleports.class.getName()),
        Map.entry("net.drgmes.dwm.blocks.tardis.misc.tardisteleporter.TardisTeleporterBlockEntity",Tile.class.getName()));
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
        Object pos=destination.invoke(null,player,manager,world);check(pos instanceof Pos p&&p.getX<110&&p.getY==52,"actual near placement must snapshot mutable settings rotation");
        world.loaded=false;check(destination.invoke(null,player,manager,world)==null,"unloaded room");world.loaded=true;
        world.blocked=true;check(destination.invoke(null,player,manager,world)==null,"blocked room");world.blocked=false;
        world.hazard=true;check(destination.invoke(null,player,manager,world)==null,"hazard floor");world.hazard=false;
        world.collision=true;check(destination.invoke(null,player,manager,world)==null,"entity collision");world.collision=false;
        world.missingPad=true;check(destination.invoke(null,player,manager,world)==null,"missing actual teleporter");world.missingPad=false;
        var execute=adapter.getMethod("execute",Object.class,PlayerEmergency0710.Context.class);
        var context=new PlayerEmergency0710.Context("ship",world,"minecraft:overworld",1,2,3,"READY");
        veto=true;check(execute.invoke(null,player,context).equals(PlayerEmergency0710.FAILED),"travel veto");
        check(PlayerEmergencyReturn0712.remaining(player,System.currentTimeMillis())==0,"veto charged cooldown");veto=false;
        manager.system.busy=true;check(execute.invoke(null,player,context).equals(PlayerEmergency0710.DENIED),"busy ship");manager.system.busy=false;
        check(execute.invoke(null,player,context).equals(PlayerEmergency0710.SENT),"return success");
        check(player.level==world&&player.fallDistance==0,"safe arrival cleanup");
        check(PlayerEmergencyReturn0712.remaining(player,System.currentTimeMillis())>1799000,"persisted success cooldown");
        player.level=new Object();check(execute.invoke(null,player,context).equals(PlayerEmergency0710.WAIT),"repeat return");
        System.out.println("Emergency production adapter: placement/rotation, blocked/unloaded/hazard/collision rooms, travel veto, busy ship, successful arrival and persisted cooldown passed.");
    }
    @SuppressWarnings("unchecked")
    static void inspectDoctor(String jar)throws Exception{
        try(var zip=new java.util.zip.ZipFile(jar)){
            for(String path:List.of("data/dwm/structure/tardis/teleporter_rooms/new_world_bridge.nbt","data/dwm/structure/tardis/console_rooms/new_world_bridge_v2.nbt")){
                try(var in=new DataInputStream(new java.util.zip.GZIPInputStream(zip.getInputStream(zip.getEntry(path))))){
                    check(in.readUnsignedByte()==10,"NBT root");in.readUTF();Map<String,Object> root=(Map<String,Object>)nbt(in,10);
                    List<?> size=(List<?>)root.get("size"),palette=(List<?>)root.get("palette"),blocks=(List<?>)root.get("blocks");
                    List<Object> anchors=new ArrayList<>();
                    for(Object value:blocks){Map<String,Object>b=(Map<String,Object>)value;Map<?,?>state=(Map<?,?>)palette.get(((Number)b.get("state")).intValue());
                        if(String.valueOf(state.get("Name")).matches(".*tardis_(teleporter|ars_creator)$"))anchors.add(List.of(state.get("Name"),b.get("pos")));
                    }
                    check(!anchors.isEmpty(),"real room has no anchor "+path);
                    if(path.contains("teleporter_rooms"))for(Object n:size)check(((Number)n).intValue()<=32,"room exceeds safety bound");
                    System.out.println("Doctor room contract: "+path+" size="+size+" anchors="+anchors);
                }
            }
            byte[] room=zip.getInputStream(zip.getEntry("net/drgmes/dwm/common/tardis/consolerooms/TardisConsoleRoomEntry.class")).readAllBytes();
            Set<String> calls=new HashSet<>();new ClassReader(room).accept(new ClassVisitor(Opcodes.ASM9){
                public MethodVisitor visitMethod(int a,String n,String d,String s,String[] ex){if(!n.equals("processTeleporterRooms"))return null;
                    return new MethodVisitor(Opcodes.ASM9){public void visitMethodInsn(int op,String owner,String method,String desc,boolean itf){
                        check(!method.matches("setBlock|placeInWorld|clearArea|teleport.*"),"placement callback must be read-only");calls.add(method);
                    }};
                }
            },0);check(calls.containsAll(List.of("setRotation","accept","rotate","offset")),"Doctor placement contract changed");
        }
    }
    static Object nbt(DataInputStream in,int type)throws IOException{
        return switch(type){
            case 1->in.readByte();case 2->in.readShort();case 3->in.readInt();case 4->in.readLong();case 5->in.readFloat();case 6->in.readDouble();
            case 7->in.readNBytes(in.readInt());case 8->in.readUTF();
            case 9->{int kind=in.readUnsignedByte(),count=in.readInt();List<Object>list=new ArrayList<>();for(int i=0;i<count;i++)list.add(nbt(in,kind));yield list;}
            case 10->{Map<String,Object>map=new LinkedHashMap<>();int kind;while((kind=in.readUnsignedByte())!=0)map.put(in.readUTF(),nbt(in,kind));yield map;}
            case 11->{int count=in.readInt();int[] data=new int[count];for(int i=0;i<count;i++)data[i]=in.readInt();yield data;}
            case 12->{int count=in.readInt();long[] data=new long[count];for(int i=0;i<count;i++)data[i]=in.readLong();yield data;}
            default->throw new IOException("NBT type "+type);
        };
    }
}
