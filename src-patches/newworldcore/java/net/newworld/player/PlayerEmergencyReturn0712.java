package net.newworld.player;

import java.lang.reflect.*;
import java.util.*;
import static net.newworld.player.PlayerOverview0670.*;

/** Owner-only rescue to the same interior arrival cell and facing as Doctor's entrance portal. */
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
        if(pos==null)return PlayerEmergency0710.FULL; // Never fall back to a different room.
        double x=num(call(pos,"getX"))+.5,y=num(call(pos,"getY")),z=num(call(pos,"getZ"))+.5;
        Object vector=type("net.minecraft.world.phys.Vec3").getConstructor(double.class,double.class,double.class).newInstance(x,y,z);
        // Doctor's helper calls ServerPlayer.changeDimension, retaining NeoForge travel veto hooks.
        Method teleport=type("net.drgmes.dwm.utils.helpers.EntityHelper").getMethod("teleport",
                type("net.minecraft.world.entity.Entity"),type("net.minecraft.server.level.ServerLevel"),type("net.minecraft.world.phys.Vec3"),float.class,float.class);
        float yaw=((Number)call(call(manager,"getEntranceFacing"),"toYRot")).floatValue();
        Object arrived=teleport.invoke(null,player,world,vector,yaw,0f);
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
        System.out.println("[NewWorld Emergency] returned ship="+expected.ship()+" entrance="+pos+" cooldown_ms="+PlayerEmergency0710.cooldownMillis());
        return PlayerEmergency0710.SENT;
    }
    private static double num(Object v){return ((Number)v).doubleValue();}
    /** Same getEntrancePosition().relative(getEntranceFacing()) used by getPortalDestination. */
    public static Object destination(Object player,Object manager,Object world)throws Exception{
        Object door=call(manager,"getMainInteriorDoorsTile");if(door==null)return null;
        Object entrance=call(manager,"getEntrancePosition"),facing=call(manager,"getEntranceFacing");
        // Require the registered door to exist in the loaded world, not a stale template fallback.
        if(!PlayerMining0700.loaded(world,entrance) || call(world,"getBlockEntity",entrance)!=door)return null;
        Object pos=call(entrance,"relative",facing);
        return safe(world,player,pos)?pos:null;
    }
    private static boolean safe(Object world,Object player,Object pos)throws Exception{
        Object below=call(pos,"below"),above=call(pos,"above");
        for(Object q:List.of(below,pos,above))if(!PlayerMining0700.loaded(world,q) || !Boolean.TRUE.equals(call(call(world,"getWorldBorder"),"isWithinBounds",q)))return false;
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
