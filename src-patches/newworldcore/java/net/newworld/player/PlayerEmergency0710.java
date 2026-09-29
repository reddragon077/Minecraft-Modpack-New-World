package net.newworld.player;

import java.io.*;
import java.security.SecureRandom;
import java.util.*;
import net.newworld.config.*;
import static net.newworld.player.PlayerOverview0670.*;

/** Distress selects a server-observed waypoint only. No teleport, route calculation or WE write. */
public final class PlayerEmergency0710 {
    public static final int MODE=11, ARM=12, BASE=1_560_000_000, BEGIN=BASE+0x1000000, END=BEGIN+1;
    public static final int SENT=1_440_004_100, DENIED=SENT+1, EXPIRED=SENT+2, WAIT=SENT+3, FULL=SENT+4, FAILED=SENT+5;
    private static final SecureRandom RANDOM=new SecureRandom();
    private static final Gate SERVER=new Gate();
    private static final Map<Object,Long> REQUESTS=new WeakHashMap<>(), ACTIONS=new WeakHashMap<>();
    private static Snapshot client;
    private static long received, requested, begun, actionStarted, armed;
    private static ByteArrayOutputStream incoming;
    private static int token;
    private static boolean waiting, renderError;
    private static String actionShip="", message="";
    private PlayerEmergency0710() {}
    public static boolean enabled(){return NewWorldConfig.bool("player-emergency","beacon.enabled",true);}
    public static int refreshTicks(){return NewWorldConfig.integer("player-emergency","refresh_ticks",20,20,1200);}
    public static int staleTicks(){return Math.max(2*refreshTicks(),NewWorldConfig.integer("player-emergency","stale_after_ticks",120,40,3600));}
    public static int confirmTicks(){return NewWorldConfig.integer("player-emergency","beacon.confirm_ticks",100,40,200);}
    public static int cooldownTicks(){return NewWorldConfig.integer("player-emergency","beacon.cooldown_ticks",200,20,1200);}
    public static boolean tokenCode(int v){return v<=-3_000_001 && v>=-4_000_000;}
    public static boolean isRequest(int v){return v==MODE || v==ARM || tokenCode(v);}
    public static boolean isReply(int v){return tokenCode(v) || v>=SENT && v<=FAILED;}
    public static boolean isWireCode(int v){return v>=BASE && v<=END;}
    public record Snapshot(String ship,String status,String dimension,String position,boolean allowed,int refresh,int stale) {}
    public record Context(String ship,Object world,String dimension,int x,int y,int z,String reason) {
        public String identity(){return ship+"/"+dimension+"/"+x+","+y+","+z;}
        public boolean allowed(){return reason.equals("READY") && world!=null && !ship.isBlank();}
    }
    public static Context context(Object player) {
        String ship="";
        try {
            var link=PlayerShipLink0680.resolve(player);
            if(link.ship()==null || !link.snapshot().allowed()) return denied(ship,"SHIP LINK LOST");
            ship=link.ship().id(); Object world=call(link.ship().manager(),"getWorld");
            if(world==null) return denied(ship,"INTERIOR NOT LOADED");
            if(!enabled() || !NewWorldTuning.playerDiscoveriesTargetEnabled()) return denied(ship,"BEACON DISABLED BY SERVER");
            Object level=call(player,"serverLevel");
            Object manager=stat("net.drgmes.dwm.common.tardis.TardisStateManager","get",level);
            if(level==world || manager instanceof Optional<?> o && o.isPresent()) return denied(ship,"EXIT SHIP TO SEND BEACON");
            if(!Boolean.TRUE.equals(call(player,"isAlive"))) return denied(ship,"PLAYER NOT ALIVE");
            Object pos=call(player,"blockPosition");
            return new Context(ship,world,String.valueOf(call(call(level,"dimension"),"location")),
                    ((Number)call(pos,"getX")).intValue(),((Number)call(pos,"getY")).intValue(),((Number)call(pos,"getZ")).intValue(),"READY");
        } catch(Exception e){System.err.println("[NewWorld Emergency] context unavailable: "+e);return denied(ship,"DATA UNAVAILABLE");}
    }
    private static Context denied(String ship,String reason){return new Context(ship,null,"",0,0,0,reason);}
    public static void request(Object player) throws Exception {
        long now=System.nanoTime(); Long last=REQUESTS.get(player);
        if(last!=null && now-last<refreshTicks()*50_000_000L)return;
        REQUESTS.put(player,now); Context c=context(player);
        SERVER.observe(player,c,now);
        Snapshot s=new Snapshot(c.ship,c.reason,c.dimension,c.allowed()?"X="+c.x+" Y="+c.y+" Z="+c.z:"",c.allowed(),refreshTicks(),staleTicks());
        for(int code:encode(s))send(player,code);
    }
    private record Observation(String ship,long time){}
    private record Ticket(String identity,Object world,int code,long time){}
    @FunctionalInterface public interface Writer { int write(Context context) throws Exception; }
    /** All access runs on the existing server dispatcher thread. A confirmation is consumed before any write. */
    public static final class Gate {
        private final Map<Object,Observation> seen=new WeakHashMap<>();
        private final Map<Object,Ticket> pending=new WeakHashMap<>();
        private final Map<Object,Long> sent=new WeakHashMap<>();
        public void observe(Object player,Context c,long now){
            Observation old=seen.put(player,new Observation(c.ship,now));
            if(!c.allowed() || old!=null && !old.ship.equals(c.ship))pending.remove(player);
        }
        public int apply(Object player,int mode,Context c,long now,Writer writer) throws Exception {
            Observation view=seen.get(player);
            if(!enabled() || !NewWorldTuning.playerDiscoveriesTargetEnabled() || !c.allowed() || view==null || !view.ship.equals(c.ship)
                    || now<view.time || now-view.time>staleTicks()*50_000_000L){pending.remove(player);return DENIED;}
            Long last=sent.get(player);
            if(last!=null && now-last<cooldownTicks()*50_000_000L){pending.remove(player);return WAIT;}
            Ticket t=pending.get(player);
            if(mode==ARM){
                if(t==null || !t.identity.equals(c.identity()) || t.world!=c.world || now-t.time>confirmTicks()*50_000_000L){
                    t=new Ticket(c.identity(),c.world,-3_000_001-RANDOM.nextInt(1_000_000),now);pending.put(player,t);
                }
                return t.code;
            }
            pending.remove(player);
            if(!tokenCode(mode) || t==null || mode!=t.code || !t.identity.equals(c.identity()) || t.world!=c.world
                    || now-t.time<200_000_000L || now-t.time>confirmTicks()*50_000_000L)return EXPIRED;
            // Charge cooldown before invoking persistence: a partially failed write cannot be spammed.
            sent.put(player,now); return writer.write(c);
        }
    }
    public static void handle(Object player,int mode){
        try {
            if(mode==MODE){request(player);return;}
            long now=System.nanoTime(); Long last=ACTIONS.get(player);
            if(last!=null && now-last<200_000_000L)return;
            ACTIONS.put(player,now); Context c=context(player);
            int result=SERVER.apply(player,mode,c,now,PlayerEmergency0710::write);
            send(player,result);
            if(!tokenCode(result))System.out.println("[NewWorld Emergency] beacon ship="+c.ship+" result="+result);
        } catch(Exception e){System.err.println("[NewWorld Emergency] failed: "+e);try{send(player,FAILED);}catch(Exception ignored){}}
    }
    private static int write(Context c) throws Exception {
        Object record=Class.forName("net.newworld.navigation.NavigationDiscoverySavedData$Discovery").getConstructor().newInstance();
        PlayerLocation0692.initialize(record,c.dimension,c.x,c.y,c.z,System.currentTimeMillis());
        var label=record.getClass().getDeclaredField("label");label.setAccessible(true);label.set(record,"DISTRESS "+c.x+" "+c.y+" "+c.z);
        Object data=stat("net.newworld.navigation.NavigationDiscoverySavedData","get",c.world);
        int result=persistAndSelect(data,c.ship,record,PlayerLocation0692.limit());
        if(result==SENT)stat("net.newworld.navigation.NavigationFavoriteOps","invalidate",data,c.ship);
        return result;
    }
    public static int persistAndSelect(Object data,String ship,Object record,int limit) throws Exception {
        int result=PlayerLocation0692.persist(data,ship,record,limit);
        if(result!=PlayerLocation0692.SAVED)return result==PlayerLocation0692.FULL?FULL:FAILED;
        PlayerDiscoveries0650.selectSavedTarget(data,ship,String.valueOf(call(record,"key")));
        return SENT;
    }
    private static void send(Object player,int code)throws Exception{stat("net.newworld.player.PlayerFieldSurvey0504Bridge","sendResult",player,code);}
    private static String clean(String s){s=s.replaceAll("[\\p{Cntrl}§]"," ");return s.substring(0,Math.min(96,s.length()));}
    public static int[] encode(Snapshot s)throws IOException{
        var bytes=new ByteArrayOutputStream();var out=new DataOutputStream(bytes);
        out.writeInt(1);for(String t:List.of(s.ship,s.status,s.dimension,s.position))out.writeUTF(clean(t));
        out.writeBoolean(s.allowed);out.writeInt(s.refresh);out.writeInt(s.stale);
        byte[] raw=bytes.toByteArray();if(raw.length>2048)throw new IOException("Emergency size");
        int[] codes=new int[(raw.length+2)/3+2];codes[0]=BEGIN;codes[codes.length-1]=END;
        for(int i=0;i<raw.length;i+=3){int bits=0;for(int j=0;j<3;j++)bits=bits<<8|(i+j<raw.length?raw[i+j]&255:0);codes[i/3+1]=BASE+bits;}
        return codes;
    }
    public static synchronized void accept(int code){
        if(!isWireCode(code))return;
        if(code==BEGIN){incoming=new ByteArrayOutputStream();begun=System.nanoTime();return;}
        if(incoming==null)return;
        if(System.nanoTime()-begun>10_000_000_000L){incoming=null;return;}
        if(code==END){
            try{
                var in=new DataInputStream(new ByteArrayInputStream(incoming.toByteArray()));
                if(in.readInt()!=1)throw new IOException("Emergency schema");
                String[] t=new String[4];for(int i=0;i<4;i++){t[i]=in.readUTF();if(t[i].length()>96)throw new IOException("Emergency text");}
                boolean allowed=in.readBoolean();int refresh=in.readInt(),stale=in.readInt();
                if(refresh<20 || refresh>1200 || stale<2*refresh || stale>3600 || in.available()>2)throw new IOException("Emergency bounds");
                while(in.available()>0)if(in.readByte()!=0)throw new IOException("Emergency padding");
                if(client!=null && (!client.ship.equals(t[0]) || !allowed))resetAction();
                client=new Snapshot(t[0],t[1],t[2],t[3],allowed,refresh,stale);received=System.nanoTime();
            }catch(IOException e){System.err.println("[NewWorld Emergency] decode rejected: "+e);}
            incoming=null;
        }else if(incoming.size()+3<=2050){int bits=code-BASE;incoming.write(bits>>>16);incoming.write(bits>>>8);incoming.write(bits);}else incoming=null;
    }
    private static void resetAction(){token=0;waiting=false;actionStarted=armed=0;actionShip="";message="";}
    public static void resetClient(){client=null;received=requested=0;incoming=null;resetAction();}
    public static Snapshot clientSnapshot(){return client;}
    public static boolean visible(Snapshot s,PlayerShipLink0680.Snapshot link,boolean allowed,long now,long at){
        return allowed && s!=null && link!=null && !s.ship.isBlank() && s.ship.equals(link.ship()) && PlayerShipLink0680.fresh(now,at,0,s.stale);
    }
    private static boolean fresh(){return visible(client,PlayerShipLink0680.clientSnapshot(),PlayerShipLink0680.clientAllowed(),System.nanoTime(),received);}
    public static void acceptReply(int code){
        if(!isReply(code) || !fresh() || !client.ship.equals(actionShip))return;
        if(tokenCode(code)){
            if(waiting && System.nanoTime()-actionStarted<5_000_000_000L){token=code;armed=System.nanoTime();waiting=false;message="Confirm current location. Stay on the same block.";}
            return;
        }
        if(!waiting)return;waiting=false;token=0;
        message=switch(code){case SENT->"BEACON SENT / TARGET SET / ROUTE UNCHANGED";case DENIED->"DENIED / CHECK LINK AND SERVER SETTINGS";
            case EXPIRED->"MOVED OR CONFIRM EXPIRED / TRY AGAIN";case WAIT->"BEACON COOLDOWN / PLEASE WAIT";case FULL->"WAYPOINT LIMIT REACHED";default->"BEACON FAILED / CHECK LOG";};
    }
    public static String button(){
        long now=System.nanoTime();
        if(waiting && now-actionStarted>5_000_000_000L){waiting=false;message="NO RESPONSE / CHECK CONNECTION";}
        if(token!=0 && now-armed>2_000_000_000L){token=0;message="CONFIRM EXPIRED / TRY AGAIN";}
        return waiting?"WAITING...":token!=0?"CONFIRM DISTRESS BEACON":"DISTRESS BEACON";
    }
    public static void render(Object screen,Object graphics,int left,int top){
        try{
            if(!PlayerShipLink0680.clientAllowed()){resetClient();return;}
            long now=System.nanoTime();int ticks=client==null?refreshTicks():client.refresh;
            if(requested==0 || now-requested>=ticks*50_000_000L){requested=now;PlayerGeologicalSurveyGui0620.sendSurveyMode(MODE);}
            draw(screen,graphics,left,top,client,fresh());
        }catch(Exception e){if(!renderError){renderError=true;System.err.println("[NewWorld Emergency] render failed: "+e);}}
    }
    public static void draw(Object screen,Object graphics,int left,int top,Snapshot s,boolean fresh)throws Exception{
        label(screen,graphics,"EMERGENCY // "+(fresh?"DISTRESS LINK":"SYNCING"),left+26,top+91,0xFFFFCF45,488);
        if(!fresh){resetAction();label(screen,graphics,"Waiting for fresh emergency data...",left+26,top+120,0xFF8DA7B4,488);return;}
        call(graphics,"fill",left+26,top+110,left+514,top+246,0xFF112A35);
        String[] lines={s.status,s.dimension,s.position,"Beacon sets the shared Navigation target only.","Existing route unchanged. No flight, teleport or WE cost.","RETURN TO SHIP: NOT AVAILABLE IN THIS BUILD"};
        for(int i=0;i<lines.length;i++)label(screen,graphics,lines[i],left+33,top+119+i*20,i==0?0xFF64EAB5:i==5?0xFFFFCF45:0xFFD5E7EF,474);
        call(graphics,"fill",left+26,top+258,left+514,top+276,s.allowed?0xFF643B25:0xFF182329);
        label(screen,graphics,s.allowed?button():"BEACON UNAVAILABLE",left+33,top+263,0xFFFFCF45,474);
        label(screen,graphics,message.isBlank()?"Two clicks to confirm. Uses the shared waypoint limit.":message,left+26,top+279,0xFF8DA7B4,488);
    }
    public static boolean mouseClicked(double x,double y,int b,int left,int top){
        if(x<left+10 || x>=left+530 || y<top+76 || y>=top+288)return false;
        if(b!=0 || !fresh() || !client.allowed){resetAction();return true;}
        if(x>=left+26 && x<left+514 && y>=top+258 && y<top+276){
            button();if(waiting || token!=0 && System.nanoTime()-armed<250_000_000L)return true;
            int mode=token==0?ARM:token;token=0;waiting=true;actionStarted=System.nanoTime();actionShip=client.ship;message="Waiting for server acknowledgement...";
            try{PlayerGeologicalSurveyGui0620.sendSurveyMode(mode);}catch(Exception e){waiting=false;message="SEND FAILED";}
        }
        return true;
    }
}
