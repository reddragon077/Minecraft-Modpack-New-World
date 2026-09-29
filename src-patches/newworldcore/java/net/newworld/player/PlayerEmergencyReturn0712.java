package net.newworld.player;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.BiConsumer;
import static net.newworld.player.PlayerOverview0670.*;

/** Owner-only rescue. Uses Doctor's actual teleporter-room placement, never the entrance. */
public final class PlayerEmergencyReturn0712 {
    private PlayerEmergencyReturn0712() {}
    public static final String COOLDOWN_KEY="NewWorldEmergencyReturnUntilV1";
    private static Class<?> type(String n)throws Exception{return Class.forName(n);}
    private static Object constant(String n,String f)throws Exception{return type(n).getField(f).get(null);}
    public static int remaining(Object player,long now)throws Exception{
        Object persisted=call(call(player,"getPersistentData"),"getCompound","PlayerPersisted");
        long until=((Number)call(persisted,"getLong",COOLDOWN_KEY)).longValue();
        return (int)Math.max(0,Math.min(86_400_000L,until-now));
    }
    public static void charge(Object player,long now,int millis)throws Exception{
        Object data=call(player,"getPersistentData"), persisted=call(data,"getCompound","PlayerPersisted");
        call(persisted,"putLong",COOLDOWN_KEY,now+millis);
        call(data,"put","PlayerPersisted",persisted);
    }
    public static String ready(Object player,Object manager,Object world)throws Exception{
        if(!call(player,"getUUID").equals(call(manager,"getOwner")))return "NOT SHIP OWNER";
        if(call(player,"serverLevel")==world)return "ALREADY ON BOARD";
        if(!Boolean.TRUE.equals(call(player,"isAlive")) || Boolean.TRUE.equals(call(player,"isRemoved")))return "PLAYER NOT AVAILABLE";
        if(Boolean.TRUE.equals(call(player,"isPassenger")) || Boolean.TRUE.equals(call(player,"isVehicle")))return "DISMOUNT BEFORE RETURN";
        if(Boolean.TRUE.equals(call(player,"isSleeping")))return "WAKE BEFORE RETURN";
        if(Boolean.TRUE.equals(call(manager,"isBroken")))return "SHIP BROKEN";
        for(String s:List.of("net.drgmes.dwm.common.tardis.systems.TardisSystemFlight","net.drgmes.dwm.common.tardis.systems.TardisSystemConsoleRoom")){
            Object system=call(manager,"getSystem",type(s));
            if(system==null || Boolean.TRUE.equals(call(system,"inProgress")))return "SHIP BUSY / TRY LATER";
        }
        return "READY";
    }
    public static int execute(Object player,PlayerEmergency0710.Context expected)throws Exception{
        var link=PlayerShipLink0680.resolve(player);
        if(link.ship()==null || !link.ship().id().equals(expected.ship()))return PlayerEmergency0710.DENIED;
        Object manager=link.ship().manager(), world=call(manager,"getWorld");
        if(world!=expected.world() || !ready(player,manager,world).equals("READY"))return PlayerEmergency0710.DENIED;
        // Validate the persistent container before moving, and recheck cooldown on the server.
        if(remaining(player,System.currentTimeMillis())>0)return PlayerEmergency0710.WAIT;
        Object pos=destination(player,manager,world);
        if(pos==null)return PlayerEmergency0710.FULL; // Explicit safe-room-unavailable result.
        double x=num(call(pos,"getX"))+.5,y=num(call(pos,"getY")),z=num(call(pos,"getZ"))+.5;
        Object vector=type("net.minecraft.world.phys.Vec3").getConstructor(double.class,double.class,double.class).newInstance(x,y,z);
        // Doctor's helper calls ServerPlayer.changeDimension, retaining NeoForge travel veto hooks.
        Method teleport=type("net.drgmes.dwm.utils.helpers.EntityHelper").getMethod("teleport",
                type("net.minecraft.world.entity.Entity"),type("net.minecraft.server.level.ServerLevel"),type("net.minecraft.world.phys.Vec3"),float.class,float.class);
        Object arrived=teleport.invoke(null,player,world,vector,((Number)call(player,"getYRot")).floatValue(),0f);
        if(arrived==null || call(arrived,"serverLevel")!=world || Math.abs(num(call(arrived,"getX"))-x)>.1
                || Math.abs(num(call(arrived,"getY"))-y)>.1 || Math.abs(num(call(arrived,"getZ"))-z)>.1)return PlayerEmergency0710.FAILED;
        charge(arrived,System.currentTimeMillis(),PlayerEmergency0710.cooldownMillis());
        // Successful return is already recorded. Ancillary cleanup/save failure must not undo that result.
        try{
            type("net.minecraft.world.entity.Entity").getMethod("setDeltaMovement",double.class,double.class,double.class).invoke(arrived,0d,0d,0d);
            type("net.minecraft.world.entity.Entity").getField("fallDistance").setFloat(arrived,0f);
            call(arrived,"clearFire");
            call(call(call(arrived,"getServer"),"getPlayerList"),"save",arrived);
        }catch(Exception e){System.err.println("[NewWorld Emergency] returned; player cleanup/checkpoint warning: "+e);}
        System.out.println("[NewWorld Emergency] returned ship="+expected.ship()+" room="+pos+" cooldown_ms="+PlayerEmergency0710.cooldownMillis());
        return PlayerEmergency0710.SENT;
    }
    private static double num(Object v){return ((Number)v).doubleValue();}
    private record Placement(Object rotation,Object base){}
    /** Read-only Doctor placement callback: near and far room origins in alternating order. */
    public static Object destination(Object player,Object manager,Object world)throws Exception{
        Object room=call(manager,"getConsoleRoom");if(room==null)return null;
        Object template=call(room,"getTeleporterRoomTemplate",world), console=call(room,"getTemplate",world);
        Object zero=constant("net.minecraft.core.BlockPos","ZERO");
        Object settings=type("net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings").getConstructor().newInstance();
        Object creator=call(constant("net.drgmes.dwm.setup.ModBlocks","TARDIS_ARS_CREATOR"),"getBlock");
        Object teleporter=call(constant("net.drgmes.dwm.setup.ModBlocks","TARDIS_TELEPORTER"),"getBlock");
        List<?> creators=(List<?>)call(console,"filterBlocks",zero,settings,creator);
        List<?> pads=(List<?>)call(template,"filterBlocks",zero,settings,teleporter);
        if(creators.isEmpty() || creators.size()>16 || pads.isEmpty())return null;
        List<Placement> placements=new ArrayList<>();
        Method process=Arrays.stream(room.getClass().getDeclaredMethods()).filter(m->m.getName().equals("processTeleporterRooms")&&m.getParameterCount()==4).findFirst().orElseThrow();
        process.setAccessible(true);
        process.invoke(room,call(room,"getCenterPosition"),template,creators,(BiConsumer<Object,Object>)(placed,base)->{
            try{placements.add(new Placement(call(placed,"getRotation"),base));}
            catch(Exception e){throw new IllegalStateException("Teleporter placement rotation",e);}
        });
        Object size=call(template,"getSize");int sx=(int)num(call(size,"getX")),sy=(int)num(call(size,"getY")),sz=(int)num(call(size,"getZ"));
        if(sx<3 || sy<3 || sz<3 || sx>32 || sy>32 || sz>32)return null;
        // Only console-side Teleporter Rooms; never unrelated far ARS rooms or the ship entrance.
        for(int i=0;i<placements.size();i+=2){
            Placement p=placements.get(i);Object padLocal=call(pads.get(0),"pos"),pad=transform(p,padLocal);
            if(!Boolean.TRUE.equals(call(world,"hasChunkAt",pad)))continue;
            Object tile=call(world,"getBlockEntity",pad);
            if(tile==null || !tile.getClass().getName().equals("net.drgmes.dwm.blocks.tardis.misc.tardisteleporter.TardisTeleporterBlockEntity"))continue;
            // Search only nearby template cells. A destroyed/blocked room fails, not a guessed fallback.
            int px=(int)num(call(padLocal,"getX")),py=(int)num(call(padLocal,"getY")),pz=(int)num(call(padLocal,"getZ"));
            for(int radius=1;radius<=3;radius++)for(int dy=0;dy<=2;dy++)for(int dx=-radius;dx<=radius;dx++)for(int dz=-radius;dz<=radius;dz++){
                if(Math.max(Math.abs(dx),Math.abs(dz))!=radius)continue;
                int x=px+dx,y=py+dy,z=pz+dz;
                if(x<1||x>=sx-1||y<1||y>=sy-1||z<1||z>=sz-1)continue;
                Object candidate=transform(p,blockPos(x,y,z));
                if(safe(world,player,candidate))return candidate;
            }
        }
        return null;
    }
    private static Object blockPos(int x,int y,int z)throws Exception{return type("net.minecraft.core.BlockPos").getConstructor(int.class,int.class,int.class).newInstance(x,y,z);}
    private static Object transform(Placement p,Object local)throws Exception{return call(p.base,"offset",call(local,"rotate",p.rotation));}
    private static boolean safe(Object world,Object player,Object pos)throws Exception{
        Object below=call(pos,"below"),above=call(pos,"above");
        for(Object q:List.of(below,pos,above))if(!Boolean.TRUE.equals(call(world,"hasChunkAt",q)) || !Boolean.TRUE.equals(call(call(world,"getWorldBorder"),"isWithinBounds",q)))return false;
        Object feet=call(world,"getBlockState",pos),head=call(world,"getBlockState",above),floor=call(world,"getBlockState",below);
        if(!Boolean.TRUE.equals(call(feet,"isAir")) || !Boolean.TRUE.equals(call(head,"isAir")))return false;
        for(Object state:List.of(feet,head,floor))if(!Boolean.TRUE.equals(call(call(state,"getFluidState"),"isEmpty")))return false;
        String id=String.valueOf(call(floor,"getBlock")).toLowerCase(Locale.ROOT);
        for(String hazard:List.of("fire","magma","cactus","portal","teleport","powder","wither","berry","pointed"))if(id.contains(hazard))return false;
        if(!Boolean.TRUE.equals(call(floor,"isFaceSturdy",world,below,constant("net.minecraft.core.Direction","UP"))))return false;
        double x=num(call(pos,"getX")),y=num(call(pos,"getY")),z=num(call(pos,"getZ"));
        Object box=type("net.minecraft.world.phys.AABB").getConstructor(double.class,double.class,double.class,double.class,double.class,double.class)
                .newInstance(x+.1,y,z+.1,x+.9,y+2,z+.9);
        // Interface default method: resolve explicitly, not the legacy reflection helper.
        return Boolean.TRUE.equals(world.getClass().getMethod("noCollision",type("net.minecraft.world.entity.Entity"),type("net.minecraft.world.phys.AABB")).invoke(world,player,box));
    }
}
