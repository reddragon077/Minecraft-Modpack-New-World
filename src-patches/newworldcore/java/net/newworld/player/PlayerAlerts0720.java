package net.newworld.player;

import java.io.*;
import java.util.*;
import net.newworld.config.NewWorldConfig;
import static net.newworld.player.PlayerOverview0670.*;

/** Read-only server sampling; bounded session history, independent of opening the GUI. */
public final class PlayerAlerts0720 {
    public static final int MODE=10, WIRE_BASE=1_640_000_000, BEGIN=WIRE_BASE+0x1000000, END=BEGIN+1;
    private static final int MAX_BYTES=4096;
    public enum Kind {
        WARP("WARP ENERGY LOW"), COLLECTION("COLLECTION TYPE SLOTS HIGH/FULL"),
        FE_MATRIX("FE MATRIX OFFLINE"), WARP_MATRIX("WARP MATRIX OFFLINE"),
        ENGINE_MATRIX("ENGINE MATRIX OFFLINE"), ENGINE("ENGINE BROKEN"),
        DRIVE("REQUIRED FLIGHT DRIVE MISSING"), BLOCKED("MINING BUFFER FULL"),
        FE("FE LEVEL LOW"), FE_METER("FE CONSUMPTION METER UNAVAILABLE"),
        FE_DATA("FE TELEMETRY UNAVAILABLE"), WARP_DATA("WARP TELEMETRY UNAVAILABLE"),
        FE_MATRIX_DATA("FE MATRIX UNKNOWN"), WARP_MATRIX_DATA("WARP MATRIX UNKNOWN"),
        ENGINE_MATRIX_DATA("ENGINE MATRIX UNKNOWN"), ENGINE_DATA("ENGINE TELEMETRY UNAVAILABLE"),
        MINING_DATA("MINING TELEMETRY UNAVAILABLE"), NAVIGATION_DATA("NAVIGATION TELEMETRY UNAVAILABLE"),
        EXTERIOR_DATA("EXTERIOR POSITION UNAVAILABLE"), PARTIAL_DATA("PARTIAL TELEMETRY / CHECK LOG"),
        MINING_ENERGY("MINING NO_ENERGY"), MINING_WAIT("MINING WAITING / CHECK OVERVIEW"),
        OTHER_OVERVIEW("OTHER OVERVIEW WARNING / CHECK OVERVIEW");
        public final String text; Kind(String text){this.text=text;}
    }
    public record Event(long sequence,long time,Kind kind,int severity) {}
    public record Snapshot(String ship,String state,int refresh,int stale,int unknown,int knownMask,List<Event> events) {
        public Snapshot {events=List.copyOf(events);}
        public Snapshot(String ship,String state,int refresh,int stale,int unknown,List<Event> events){this(ship,state,refresh,stale,unknown,(1<<Kind.values().length)-1,events);}
    }
    public static final class History {
        public final EnumMap<Kind,Integer> values=new EnumMap<>(Kind.class);
        public final LinkedList<Event> events=new LinkedList<>(); private long sequence;
        public void update(Map<Kind,Integer> next,long now) {
            for(Kind k:Kind.values()) {
                int value=next.getOrDefault(k,-1); if(value<0)continue; // Missing data is never a recovery.
                Integer previous=values.put(k,value);
                if((previous==null && value>0) || (previous!=null && previous!=value))
                    events.addFirst(new Event(++sequence,now,k,value));
            }
            while(events.size()>historyLimit())events.removeLast();
        }
    }
    private static final class ServerState {long requested; String ship=""; History history=new History();}
    private static final Map<Object,ServerState> SERVER=Collections.synchronizedMap(new WeakHashMap<>());
    private static Object connection,level,player,detailScreen;
    private static String lastShip="";
    private static Snapshot client; private static long requested,received,begun,watermark,lastNotice;
    private static ByteArrayOutputStream incoming;
    private static final ArrayDeque<Event> notices=new ArrayDeque<>();
    private static final EnumMap<Kind,Long> shown=new EnumMap<>(Kind.class);
    private static final EnumMap<Kind,Integer> shownSeverity=new EnumMap<>(Kind.class);
    private static int page; private static boolean failureLogged;
    private PlayerAlerts0720(){}
    public static boolean enabled(){return NewWorldConfig.bool("ship-alerts","enabled",true);}
    public static boolean notifications(){return NewWorldConfig.bool("ship-alerts","notifications.enabled",true);}
    public static int refreshTicks(){return NewWorldConfig.integer("ship-alerts","refresh_ticks",40,20,1200);}
    public static int staleTicks(){return Math.max(refreshTicks()*3,120);}
    public static int historyLimit(){return NewWorldConfig.integer("ship-alerts","history_limit",16,4,32);}
    public static int noticeCooldown(){return NewWorldConfig.integer("ship-alerts","notifications.repeat_seconds",60,5,3600);}
    public static int noticeSpacing(){return NewWorldConfig.integer("ship-alerts","notifications.spacing_seconds",4,3,30);}
    public static int warningPercent(){return PlayerOverview0670.warningPercent();}
    public static int criticalPercent(){return PlayerOverview0670.criticalPercent();}
    public static int bufferPercent(){return NewWorldConfig.integer("ship-alerts","collection.warning_percent",80,1,99);}
    public static int lowEnergy(long n,long cap){return n<0||cap<=0?-1:PlayerOverview0670.energySeverity(n,cap);}
    public static int slots(int used,int capacity){return used<0||capacity<=0?-1:used>=capacity?2:100d*used/capacity>=bufferPercent()?1:0;}
    public static int drive(int travel,int[] modules){
        if(travel==0)return 0;
        if(modules==null||modules.length<5)return -1;
        return (travel==2?modules[4]>0:modules[3]>0||modules[4]>0)?0:2;
    }
    public static Object room(String id,String type)throws Exception {
        var f=Class.forName("net.newworld.core.ShipRoomRegistry").getDeclaredField("STATS");f.setAccessible(true);
        return ((Map<?,?>)f.get(null)).get(id+":"+type);
    }
    public static EnumMap<Kind,Integer> sample(Object manager,Object world,String id) {
        EnumMap<Kind,Integer> result=new EnumMap<>(Kind.class);
        for(Kind k:Kind.values())result.put(k,-1);
        if(world==null)return result;
        try {
            Object from=call(manager,"getCurrentExteriorDimension"),to=call(manager,"getDestinationExteriorDimension");
            if(from!=null && to!=null){
                int travel=Objects.equals(from,to)?0:((Number)stat("net.drgmes.dwm.newworld.EngineTravelBalance","travelClass",from,to)).intValue();
                Object r=room(id,"ENGINE");
                // Inspect the same slots as the real flight gate, but don't invent missing modules from unknown telemetry.
                int[] modules=r!=null&&Boolean.TRUE.equals(call(r,"active"))?(int[])call(r,"modules"):null;
                result.put(Kind.DRIVE,drive(travel,modules));
            }
        }catch(Exception ignored){}
        try {
            Object runtime=PlayerMining0700.state(stat("net.newworld.mining.MiningRuntimeSavedData","get",world),id);
            if(runtime!=null){
                String text=PlayerMining0700.buffer(world,runtime,"bufferPos","COLLECTION_ID");
                result.put(Kind.COLLECTION,collection(text));
            }
        }catch(Exception ignored){}
        return result;
    }
    /** Never project the three display rows: every raw Overview warning participates, including telemetry failures. */
    public static void mergeOverview(EnumMap<Kind,Integer> values,PlayerOverview0670.Inspection inspection){
        var s=inspection.snapshot();
        if(s.ship().isEmpty())return;
        for(Kind k:Kind.values())if(k!=Kind.COLLECTION&&k!=Kind.DRIVE)values.put(k,0);
        for(var entry:inspection.active().entrySet()){
            Kind k=overviewKind(entry.getKey());values.merge(k,entry.getValue(),Math::max);
        }
        // Missing measurements produce their own warning, never a fabricated recovery of an operational fault.
        if(s.fe()<0||s.feCapacity()<=0)values.put(Kind.FE,-1);
        if(s.warp()<0||s.warpCapacity()<=0)values.put(Kind.WARP,-1);
        if("UNKNOWN".equals(s.feMatrix()))values.put(Kind.FE_MATRIX,-1);
        if("UNKNOWN".equals(s.warpMatrix()))values.put(Kind.WARP_MATRIX,-1);
        if("UNKNOWN".equals(s.engineMatrix()))values.put(Kind.ENGINE_MATRIX,-1);
        if("UNKNOWN".equals(s.engine()))values.put(Kind.ENGINE,-1);
        if("UNKNOWN".equals(s.mining()))for(Kind k:List.of(Kind.BLOCKED,Kind.MINING_ENERGY,Kind.MINING_WAIT))values.put(k,-1);
        if("UNKNOWN".equals(s.shield()))values.put(Kind.MINING_WAIT,-1);
    }
    public static Kind overviewKind(String text){
        return switch(text){
            case "FE LEVEL LOW"->Kind.FE;
            case "WARP LEVEL LOW"->Kind.WARP;
            case "FE MATRIX OFFLINE"->Kind.FE_MATRIX;
            case "WARP MATRIX OFFLINE"->Kind.WARP_MATRIX;
            case "ENGINE MATRIX OFFLINE"->Kind.ENGINE_MATRIX;
            case "ENGINE BROKEN"->Kind.ENGINE;
            case "MINING BUFFER_FULL"->Kind.BLOCKED;
            case "MINING NO_ENERGY"->Kind.MINING_ENERGY;
            case "FE CONSUMPTION METER UNAVAILABLE"->Kind.FE_METER;
            case "FE TELEMETRY UNAVAILABLE"->Kind.FE_DATA;
            case "WARP TELEMETRY UNAVAILABLE"->Kind.WARP_DATA;
            case "FE MATRIX UNKNOWN"->Kind.FE_MATRIX_DATA;
            case "WARP MATRIX UNKNOWN"->Kind.WARP_MATRIX_DATA;
            case "ENGINE MATRIX UNKNOWN"->Kind.ENGINE_MATRIX_DATA;
            case "ENGINE UNKNOWN", "ENGINE TELEMETRY UNAVAILABLE"->Kind.ENGINE_DATA;
            case "MINING TELEMETRY UNAVAILABLE"->Kind.MINING_DATA;
            case "NAVIGATION TELEMETRY UNAVAILABLE"->Kind.NAVIGATION_DATA;
            case "EXTERIOR POSITION UNAVAILABLE"->Kind.EXTERIOR_DATA;
            case "PARTIAL TELEMETRY / CHECK LOG"->Kind.PARTIAL_DATA;
            default->text.startsWith("MINING WAITING_")?Kind.MINING_WAIT:Kind.OTHER_OVERVIEW;
        };
    }
    /** The exact existing buffer telemetry reports virtual TYPE capacity, not an invented item limit. */
    public static int collection(String text){
        var match=java.util.regex.Pattern.compile(".* / (\\d+)/(\\d+) TYPES").matcher(text);
        if(!match.matches())return -1;
        try{return slots(Integer.parseInt(match.group(1)),Integer.parseInt(match.group(2)));}catch(NumberFormatException e){return -1;}
    }
    public static void request(Object p){
        ServerState state=SERVER.computeIfAbsent(p,k->new ServerState());long now=System.nanoTime();
        if(now-state.requested<refreshTicks()*50_000_000L)return;state.requested=now;
        try {
            var link=PlayerShipLink0680.resolve(p);String id=link.ship()==null?"":link.ship().id();
            if(!id.equals(state.ship)){state.ship=id;long sequence=state.history.sequence;state.history=new History();state.history.sequence=sequence;}
            Snapshot s;
            if(!enabled()||!link.snapshot().allowed()||link.ship()==null)
                s=new Snapshot("",enabled()?"LINK UNAVAILABLE":"ALERTS DISABLED",refreshTicks(),staleTicks(),0,List.of());
            else {
                Object manager=link.ship().manager(),world=call(manager,"getWorld");
                var values=sample(manager,world,id);
                var inspection=PlayerOverview0670.inspect(p);
                if(id.equals(inspection.snapshot().ship()))mergeOverview(values,inspection);
                state.history.update(values,System.currentTimeMillis());
                int unknown=(int)values.values().stream().filter(v->v<0).count();
                int active=(int)values.values().stream().filter(v->v>0).count();
                int mask=0;for(Kind k:Kind.values())if(values.get(k)>=0)mask|=1<<k.ordinal();
                s=new Snapshot(id,active+" ACTIVE / "+unknown+" UNKNOWN",refreshTicks(),staleTicks(),unknown,mask,state.history.events);
            }
            for(int code:encode(s))stat("net.newworld.player.PlayerFieldSurvey0504Bridge","sendResult",p,code);
        }catch(Exception e){System.err.println("[NewWorld Alerts] sample failed: "+e);}
    }
    public static int[] encode(Snapshot s)throws IOException {
        var bytes=new ByteArrayOutputStream();var out=new DataOutputStream(bytes);
        out.writeByte(2);out.writeUTF(s.ship);out.writeUTF(s.state);out.writeInt(s.refresh);out.writeInt(s.stale);out.writeByte(s.unknown);out.writeInt(s.knownMask);out.writeByte(s.events.size());
        for(Event e:s.events){out.writeLong(e.sequence);out.writeLong(e.time);out.writeByte(e.kind.ordinal());out.writeByte(e.severity);}
        byte[] b=bytes.toByteArray();if(b.length>MAX_BYTES)throw new IOException("Alert size");
        int[] codes=new int[(b.length+2)/3+2];codes[0]=BEGIN;codes[codes.length-1]=END;
        for(int i=0;i<b.length;i+=3)codes[1+i/3]=WIRE_BASE+((b[i]&255)<<16)+(i+1<b.length?(b[i+1]&255)<<8:0)+(i+2<b.length?b[i+2]&255:0);
        return codes;
    }
    public static boolean isWireCode(int n){return n>=WIRE_BASE&&n<=END;}
    public static synchronized boolean accept(int code){
        if(!isWireCode(code))return false;
        long now=System.nanoTime();
        if(code==BEGIN){incoming=new ByteArrayOutputStream();begun=now;return true;}
        if(incoming==null)return true;
        if(now-begun>10_000_000_000L){incoming=null;return true;}
        if(code!=END){int n=code-WIRE_BASE;incoming.write(n>>16);incoming.write(n>>8);incoming.write(n);if(incoming.size()>MAX_BYTES)incoming=null;return true;}
        byte[] b=incoming.toByteArray();incoming=null;
        try {
            var in=new DataInputStream(new ByteArrayInputStream(b));if(in.readUnsignedByte()!=2)throw new IOException("schema");
            String ship=in.readUTF(),state=in.readUTF();int refresh=in.readInt(),stale=in.readInt(),unknown=in.readUnsignedByte(),mask=in.readInt(),count=in.readUnsignedByte();
            if(ship.length()>96||state.length()>96||refresh<20||refresh>1200||stale<refresh*2||stale>3600||unknown>Kind.values().length||mask<0||(mask>>>Kind.values().length)!=0||count>32)throw new IOException("bounds");
            List<Event> events=new ArrayList<>();long last=Long.MAX_VALUE;
            for(int i=0;i<count;i++){
                long seq=in.readLong(),time=in.readLong();int kind=in.readUnsignedByte(),severity=in.readUnsignedByte();
                if(seq<=0||seq>=last||time<0||kind>=Kind.values().length||severity>2)throw new IOException("event");last=seq;
                events.add(new Event(seq,time,Kind.values()[kind],severity));
            }
            if(in.available()>2)throw new IOException("trailing");while(in.available()>0)if(in.readByte()!=0)throw new IOException("padding");
            receive(new Snapshot(ship,state,refresh,stale,unknown,mask,events),now);
        }catch(Exception e){System.err.println("[NewWorld Alerts] frame rejected: "+e);}
        return true;
    }
    public static synchronized void receive(Snapshot s,long now){
        if(!s.ship.isEmpty()&&!lastShip.equals(s.ship)){lastShip=s.ship;watermark=0;notices.clear();shown.clear();shownSeverity.clear();page=0;}
        if(s.ship.isEmpty())notices.clear();
        if(!s.ship.isEmpty())for(Event e:s.events)if(e.sequence>watermark&&e.severity>0&&notices.size()<3)notices.add(e);
        if(!s.events.isEmpty())watermark=Math.max(watermark,s.events.getFirst().sequence);
        // A resolved event arriving before its queued notification must cancel that stale notification.
        Map<Kind,Integer> latest=new EnumMap<>(Kind.class);
        for(Event e:s.events)latest.putIfAbsent(e.kind,e.severity);
        notices.removeIf(e->latest.getOrDefault(e.kind,-1)!=e.severity||(s.knownMask&(1<<e.kind.ordinal()))==0);
        client=s;received=now;
    }
    public static synchronized Snapshot clientSnapshot(){return client;}
    public static synchronized void resetClient(){client=null;incoming=null;requested=received=watermark=lastNotice=0;notices.clear();shown.clear();shownSeverity.clear();lastShip="";detailScreen=null;page=0;}
    public static synchronized boolean fresh(long now){return client!=null&&now-received<=client.stale*50_000_000L;}
    /** Hooked into the existing client post-tick subscriber, including ticks outside the screen. */
    public static synchronized void tick(){
        try {
            Object mc=stat("net.minecraft.client.Minecraft","getInstance"),c=clientConnection(mc),l=field(mc,"level"),p=field(mc,"player");
            if(c!=connection||p!=player){resetClient();connection=c;player=p;}
            else if(l!=level){client=null;incoming=null;requested=received=0;notices.clear();detailScreen=null;page=0;}
            level=l;
            if(c==null||l==null||p==null)return;
            long now=System.nanoTime();int ticks=client==null?refreshTicks():client.refresh;
            if(now-requested>=ticks*50_000_000L){requested=now;PlayerGeologicalSurveyGui0620.sendSurveyMode(MODE);}
            if(!notifications()||!fresh(now)||client.ship.isEmpty()){notices.clear();return;}
            Event event=nextNotice(now);
            if(event!=null)call(p,"displayClientMessage",stat("net.minecraft.network.chat.Component","literal","[SHIP] "+event.kind.text+(event.severity==2?" [CRITICAL]":"")),true);
        }catch(Throwable e){if(!failureLogged){failureLogged=true;System.err.println("[NewWorld Alerts] tick failed: "+e);}}
    }
    public static synchronized Event nextNotice(long now){
        if(now-lastNotice<noticeSpacing()*1_000_000_000L)return null;
        while(!notices.isEmpty()){
            Event e=notices.removeFirst();long old=shown.getOrDefault(e.kind,Long.MIN_VALUE/2);
            if(now-old<noticeCooldown()*1_000_000_000L&&e.severity<=shownSeverity.getOrDefault(e.kind,0))continue;
            shown.put(e.kind,now);shownSeverity.put(e.kind,e.severity);lastNotice=now;return e;
        }
        return null;
    }
    public static boolean mouseClicked(Object screen,double mx,double my,int button)throws Exception {
        if(((Number)field(screen,"tab")).intValue()!=0||button!=0)return false;
        int left=(((Number)field(screen,"width")).intValue()-540)/2,top=(((Number)field(screen,"height")).intValue()-300)/2;
        double x=mx-left,y=my-top;
        if(detailScreen==screen){
            if(y>=258&&y<276){if(x>=26&&x<210){detailScreen=null;page=0;}else if(x>=320&&x<405)page=Math.max(0,page-1);else if(x>=420&&x<514)page++;}
            return x>=10&&x<530&&y>=76&&y<288;
        }
        if(x>=365&&x<514&&y>=229&&y<242){detailScreen=screen;page=0;return true;}return false;
    }
    public static boolean draw(Object screen,Object graphics,int left,int top)throws Exception {
        if(detailScreen!=screen){
            call(graphics,"fill",left+365,top+229,left+514,top+242,0xFF194456);
            label(screen,graphics,"ALERT HISTORY >",left+370,top+232,0xFF62D5F1,139);return false;
        }
        call(graphics,"fill",left+10,top+76,left+530,top+288,0xFF09151B);
        label(screen,graphics,"SHIP ALERTS // SESSION HISTORY",left+26,top+91,0xFF62D5F1,488);
        Snapshot s=clientSnapshot();boolean valid=fresh(System.nanoTime())&&s!=null&&!s.ship.isEmpty();
        label(screen,graphics,valid?s.state:"Alerts unavailable / waiting for fresh link",left+26,top+112,0xFFFFCF45,488);
        List<Event> events=valid?s.events:List.of();int pages=Math.max(1,(events.size()+4)/5);page=Math.max(0,Math.min(page,pages-1));
        if(valid&&events.isEmpty())label(screen,graphics,"No alert transitions recorded this session.",left+26,top+139,0xFF8DA7B4,488);
        for(int i=0;i<5&&page*5+i<events.size();i++){
            Event e=events.get(page*5+i);String state=e.severity==0?"RESOLVED":e.severity==2?"CRITICAL":"WARNING";
            label(screen,graphics,"["+state+"] "+e.kind.text,left+26,top+139+i*22,e.severity==0?0xFF8DA7B4:e.severity==2?0xFFFF7272:0xFFFFCF45,488);
        }
        label(screen,graphics,"Unknown data does not resolve alerts. History resets on logout.",left+26,top+249,0xFF8DA7B4,488);
        label(screen,graphics,"< OVERVIEW",left+26,top+264,0xFF62D5F1,160);
        label(screen,graphics,"< PREV     "+(page+1)+"/"+pages+"     NEXT >",left+320,top+264,0xFF62D5F1,194);
        return true;
    }
}
