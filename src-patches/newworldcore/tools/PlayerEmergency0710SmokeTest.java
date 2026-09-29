import java.nio.file.*;
import java.util.*;
import org.objectweb.asm.*;
import net.newworld.config.*;
import net.newworld.player.*;
import static net.newworld.player.PlayerEmergency0710.*;

public final class PlayerEmergency0710SmokeTest {
    static void check(boolean v,String m){if(!v)throw new AssertionError(m);}
    static void eq(Object a,Object b){check(Objects.equals(a,b),a+" != "+b);}
    public static void main(String[] args)throws Exception{
        Path root=Files.createTempDirectory("emergency-test-");Path config=root.resolve("player-emergency.properties");
        System.setProperty("newworldcore.configDir",root.toString());NewWorldConfig.reload();
        try{
            check(enabled(),"default");eq(refreshTicks(),20);eq(staleTicks(),120);eq(confirmTicks(),100);eq(cooldownMillis(),1800000);
            Files.writeString(config,"return.enabled=false\nrefresh_ticks=0\nstale_after_ticks=0\nreturn.confirm_ticks=0\nreturn.cooldown_minutes=0\n");NewWorldConfig.reload();
            check(!enabled(),"live toggle");eq(refreshTicks(),20);eq(staleTicks(),40);eq(confirmTicks(),40);eq(cooldownMillis(),60000);
            Files.writeString(config,"return.enabled=bad\nrefresh_ticks=99999\nstale_after_ticks=99999\nreturn.confirm_ticks=99999\nreturn.cooldown_minutes=99999\n");NewWorldConfig.reload();
            check(enabled(),"bool fallback");eq(refreshTicks(),1200);eq(staleTicks(),3600);eq(confirmTicks(),200);eq(cooldownMillis(),86400000);
            Files.writeString(config,"");NewWorldConfig.reload();
            guards(config);persistence();wire();lateReply();receipts();rapidRetry();audit();
            System.out.println("Emergency config, single-use guards, persistence, delayed/framed/replayed receipts through production dispatcher, cooldown/reopen/stale/cross-ship, bounded wire and layout passed.");
        }finally{Files.deleteIfExists(config);Files.deleteIfExists(root);}
    }
    static void guards(Path config)throws Exception{
        Gate g=new Gate();Player p=new Player();Object world=new Object();Context c=new Context("ship",world,"minecraft:overworld",-2454,63,181,"READY");
        int[] writes={0};Writer writer=x->{writes[0]++;PlayerEmergencyReturn0712.charge(p,System.currentTimeMillis(),cooldownMillis());return SENT;};long t=20_000_000_000L;
        eq(g.apply(p,ARM,c,t,writer),DENIED);g.observe(p,c,t);int token=g.apply(p,ARM,c,t,writer);check(tokenCode(token),"challenge");eq(writes[0],0);
        eq(g.apply(p,ARM,c,t+1,writer),token);eq(g.apply(p,token,c,t+300_000_000L,writer),SENT);eq(writes[0],1);
        eq(g.confirmRemaining(p,token,t+300_000_000L),0);check(g.cooldownRemaining(p,t)>1799000,"persistent 30min");
        eq(g.apply(p,token,c,t+400_000_000L,writer),WAIT);p.data=new Tag();t+=11_000_000_000L;g.observe(p,c,t);eq(g.apply(p,token,c,t,writer),EXPIRED);
        token=g.apply(p,ARM,c,t,writer);
        eq(g.apply(p,token,new Context("ship",world,c.dimension(),c.x()+1,c.y(),c.z(),"READY"),t+300_000_000L,writer),EXPIRED);
        eq(writes[0],1);
        token=g.apply(p,ARM,c,t,writer);eq(g.apply(p,token,c,t+100_000_000L,writer),EXPIRED);
        token=g.apply(p,ARM,c,t,writer);eq(g.apply(p,token,c,t+5_100_000_000L,writer),EXPIRED);
        token=g.apply(p,ARM,c,t,writer);eq(g.apply(new Object(),token,c,t+300_000_000L,writer),DENIED);
        eq(g.apply(p,token,new Context("other",world,c.dimension(),c.x(),c.y(),c.z(),"READY"),t+300_000_000L,writer),DENIED);
        token=g.apply(p,ARM,c,t,writer);eq(g.apply(p,token,new Context("ship",new Object(),c.dimension(),c.x(),c.y(),c.z(),"READY"),t+300_000_000L,writer),EXPIRED);
        token=g.apply(p,ARM,c,t,writer);eq(g.apply(p,token,new Context("ship",world,"minecraft:the_nether",c.x(),c.y(),c.z(),"READY"),t+300_000_000L,writer),EXPIRED);
        token=g.apply(p,ARM,c,t,writer);eq(g.apply(p,token,new Context("ship",world,c.dimension(),c.x(),c.y(),c.z(),"EXIT SHIP TO SEND BEACON"),t+300_000_000L,writer),DENIED);
        token=g.apply(p,ARM,c,t,writer);Files.writeString(config,"return.enabled=false\n");NewWorldConfig.reload();eq(g.apply(p,token,c,t+300_000_000L,writer),DENIED);
        Files.writeString(config,"");NewWorldConfig.reload();eq(g.apply(p,ARM,c,t+7_000_000_000L,writer),DENIED);eq(writes[0],1);
        token=g.apply(p,ARM,c,t,writer);g.observe(p,new Context("ship",null,"",0,0,0,"SHIP LINK LOST"),t+1);g.observe(p,c,t+2);
        eq(g.apply(p,token,c,t+300_000_000L,writer),EXPIRED);
        Path gui=config.resolveSibling("gui.properties");
        try{Files.writeString(gui,"player.discoveries.enable_target_action=false\n");NewWorldConfig.reload();
            check(!NewWorldTuning.playerDiscoveriesTargetEnabled(),"shared permission config");
            check(tokenCode(g.apply(p,ARM,c,t+300_000_000L,writer)),"rescue independent of discovery permission");eq(writes[0],1);
        }finally{Files.deleteIfExists(gui);NewWorldConfig.reload();}
    }
    public static class Tag {
        final Map<String,Object> values=new HashMap<>();
        public Tag getCompound(String key){return values.get(key) instanceof Tag t?t:new Tag();}
        public long getLong(String key){return values.get(key) instanceof Long l?l:0;}
        public void putLong(String key,long value){values.put(key,value);}
        public void put(String key,Tag value){values.put(key,value);}
    }
    public static class Player {public Tag data=new Tag();public Tag getPersistentData(){return data;}}
    static void persistence()throws Exception{
        Player p=new Player();eq(PlayerEmergencyReturn0712.remaining(p,1000),0);
        PlayerEmergencyReturn0712.charge(p,1000,1800000);
        Player reload=new Player();reload.data=p.data;
        eq(PlayerEmergencyReturn0712.remaining(reload,61000),1740000);
        eq(PlayerEmergencyReturn0712.remaining(reload,1801000),0);
        check(PlayerEmergencyReturn0712.remaining(reload,0)>0,"clock rollback must not unlock");
        Gate g=new Gate();Player failed=new Player();Context c=new Context("ship",new Object(),"dim",1,2,3,"READY");
        long now=System.nanoTime();g.observe(failed,c,now);int token=g.apply(failed,ARM,c,now,x->FAILED);
        eq(g.apply(failed,token,c,now+300000000,x->FAILED),FAILED);eq(g.cooldownRemaining(failed,now),0);
        eq(g.apply(failed,token,c,now+400000000,x->SENT),EXPIRED);
        check(!isRequest(11)&&!isRequest(12)&&!isRequest(-3000001),"legacy Beacon requests must not teleport");
    }
    static void wire()throws Exception{
        Snapshot s=new Snapshot("ship","READY","minecraft:overworld","X=-2454 Y=63 Z=181",true,20,120);
        for(int code:encode(s)){check(!PlayerMining0700.isWireCode(code)&&!PlayerNavigation0690.isWireCode(code)&&!PlayerShipLink0680.isWireCode(code),"wire collision");accept(code);}eq(clientSnapshot(),s);
        var link=new PlayerShipLink0680.Snapshot("CONNECTED","ship","minecraft:overworld",12,"",20,120);
        check(visible(s,link,true,100,100),"fresh");check(!visible(s,link,false,100,100),"lost");check(!visible(s,link,true,7_000_000_100L,100),"stale");
        check(!visible(new Snapshot("other","READY","","",true,20,120),link,true,100,100),"other ship");
        int[] invalid=encode(new Snapshot("ship","READY","","",true,0,120));for(int code:invalid)accept(code);eq(clientSnapshot(),s);
        accept(BEGIN);for(int i=0;i<800;i++)accept(BASE);accept(END);eq(clientSnapshot(),s);
        acceptReply(-5_000_001);eq(button(),"EMERGENCY RETURN TO SHIP"); // Unsolicited ack cannot arm.
        Screen screen=new Screen();var graphics=new PlayerNavigation0690SmokeTest.Graphics();draw(screen,graphics,0,0,s,true);
        check(screen.lines.stream().anyMatch(x->x.contains("Teleporter Room")),"return destination description");
        screen.lines.clear();draw(screen,graphics,0,0,s,false);check(!screen.lines.contains(s.position()),"stale coords");
        var click=new PlayerMining0700SmokeTest.ClickScreen();check(PlayerDiscoveries0650.mouseClicked(click,470,50,0)&&click.tab==5,"header");
        check(PlayerDiscoveries0650.mouseClicked(click,40,265,0),"content leaked to legacy");check(!PlayerDiscoveries0650.mouseClicked(click,30,50,0),"Overview swallowed");
        PlayerShipLink0680.resetClient();eq(clientSnapshot(),null);
    }
    public static class Screen{
        public PlayerNavigation0690SmokeTest.Font font=new PlayerNavigation0690SmokeTest.Font();public List<String> lines=new ArrayList<>();
        public void text(Object g,String s,int x,int y,int color){check(x>=26&&x+font.width(s)<=514&&y>=91&&y+9<=288,"layout "+s);lines.add(s);}
    }
    static void set(String name,Object value)throws Exception{
        var f=PlayerEmergency0710.class.getDeclaredField(name);f.setAccessible(true);f.set(null,value);
    }
    static Object get(String name)throws Exception{
        var f=PlayerEmergency0710.class.getDeclaredField(name);f.setAccessible(true);return f.get(null);
    }
    static void lateReply()throws Exception{
        PlayerShipLink0680.resetClient();
        for(int code:PlayerShipLink0680.encode(new PlayerShipLink0680.Snapshot("CONNECTED","ship","minecraft:overworld",16,"",20,120)))PlayerShipLink0680.accept(code);
        for(int code:encode(new Snapshot("ship","READY","minecraft:overworld","X=1 Y=63 Z=2",true,20,120)))accept(code);
        set("waiting",true);set("actionShip","ship");set("actionStarted",System.nanoTime()-6_000_000_000L);
        button();acceptReply(SENT);
        check(String.valueOf(get("message")).startsWith("RETURNED TO SHIP"),"Late successful reply permanently discarded after UI timeout");
        resetClient();
    }
    public record Payload(int mode){}
    public static class ClientContext{public String flow(){return "CLIENTBOUND";}}
    static void feed(Snapshot s)throws Exception{
        for(int code:encode(s))PlayerDiscoveries0650.handlePayload(new Payload(code),new ClientContext());
        eq(clientSnapshot(),s);
    }
    static Snapshot receipt(String ship,long seq,int request,int result,int confirm,int cooldown){
        return new Snapshot(ship,"READY","minecraft:overworld","X=1 Y=63 Z=2",true,20,120,seq,request,result,confirm,cooldown);
    }
    static void pending(int mode,long floor)throws Exception{
        set("waiting",true);set("pendingMode",mode);set("receiptFloor",floor);set("actionShip","ship");set("actionStarted",System.nanoTime());
    }
    static void receipts()throws Exception{
        int challenge=-5_000_123;
        feed(new Snapshot("ship","READY","minecraft:overworld","X=1 Y=63 Z=2",true,20,120));
        pending(ARM,0);feed(receipt("ship",1,ARM,challenge,4900,0));eq(button(),"CONFIRM RETURN TO SHIP");
        pending(challenge,1);set("token",0);set("actionStarted",System.nanoTime()-6_000_000_000L);
        eq(button(),"WAITING...");
        // Neither an older challenge nor an unrelated command may settle the new confirmation.
        feed(receipt("ship",1,ARM,challenge,1000,0));eq(button(),"WAITING...");
        feed(receipt("ship",2,ARM,WAIT,0,0));eq(button(),"WAITING...");
        // Drop the immediate action response; the next normal telemetry contains the same receipt.
        set("received",System.nanoTime()-7_000_000_000L);
        draw(new Screen(),new PlayerNavigation0690SmokeTest.Graphics(),0,0,clientSnapshot(),false);
        eq(get("waiting"),true);
        feed(receipt("ship",3,challenge,SENT,0,9000));
        check(String.valueOf(get("message")).startsWith("RETURNED TO SHIP"),"polled success missing");check(button().startsWith("RETURN COOLDOWN"),"server cooldown missing");
        eq(get("pendingMode"),0);feed(receipt("ship",3,challenge,SENT,0,8000));eq(get("token"),0);
        // Reopen shows current cooldown, but must never replay an old challenge or show a new success.
        resetClient();feed(receipt("ship",3,challenge,SENT,0,7000));eq(get("message"),"");check(button().startsWith("RETURN COOLDOWN"),"reopen cooldown");
        pending(ARM,3);feed(receipt("ship",4,ARM,challenge,0,0));eq(button(),"EMERGENCY RETURN TO SHIP");
        check(String.valueOf(get("message")).contains("EXPIRED"),"expired server challenge armed");
        pending(ARM,4);feed(receipt("ship",5,ARM,WAIT,0,5000));check(String.valueOf(get("message")).contains("COOLDOWN"),"cooldown reply lost");
        pending(ARM,5);feed(receipt("other",6,ARM,challenge,5000,0));eq(get("token"),0);eq(get("pendingMode"),0);
        resetClient();feed(receipt("ship",7,ARM,challenge,5000,0));eq(get("token"),0);
        pending(ARM,7);
        feed(new Snapshot("ship","BEACON DISABLED BY SERVER","","",false,20,120,8,ARM,DENIED,0,0));
        check(String.valueOf(get("message")).startsWith("RETURN DENIED"),"policy denial hidden");eq(get("waiting"),false);
        Snapshot valid=clientSnapshot();for(int code:encode(receipt("ship",9,ARM,SENT,10001,0)))accept(code);eq(clientSnapshot(),valid);
        resetClient();
    }
    @SuppressWarnings("unchecked")
    static void rapidRetry()throws Exception{
        Object p=new Object();Context c=new Context("ship",new Object(),"minecraft:overworld",1,63,2,"READY");
        var remember=PlayerEmergency0710.class.getDeclaredMethod("remember",Object.class,Context.class,int.class,int.class);remember.setAccessible(true);
        remember.invoke(null,p,c,-5_000_123,EXPIRED);
        ((Map<Object,Long>)get("ACTIONS")).put(p,System.nanoTime());handle(p,ARM);
        Object receipt=((Map<?,?>)get("RECEIPTS")).get(p);
        for(var entry:Map.of("request",(Object)ARM,"result",WAIT,"sequence",2L).entrySet()){
            var accessor=receipt.getClass().getDeclaredMethod(entry.getKey());accessor.setAccessible(true);eq(accessor.invoke(receipt),entry.getValue());
        }
    }
    static void audit()throws Exception{
        Set<String> forbidden=Set.of("consume","calculate","takeoff","setShieldsMiningState","getChunk","setDestinationPosition","selectSavedTarget","persistAndSelect");
        Set<String> hooks=new HashSet<>();
        for(String name:List.of("PlayerEmergency0710","PlayerDiscoveries0650","PlayerFieldSurvey0620Dispatcher","PlayerShipLink0680")){
            try(var in=PlayerEmergency0710.class.getResourceAsStream(name+".class")){
                new ClassReader(in).accept(new ClassVisitor(Opcodes.ASM9){
                    public MethodVisitor visitMethod(int access,String method,String desc,String sig,String[] ex){return new MethodVisitor(Opcodes.ASM9){
                        public void visitLdcInsn(Object v){if(name.equals("PlayerEmergency0710")&&v instanceof String s)check(!forbidden.contains(s),"forbidden "+s);}
                        public void visitMethodInsn(int opcode,String owner,String target,String d,boolean itf){if(owner.endsWith("PlayerEmergency0710"))hooks.add(name+":"+target);}
                    };}
                },0);
            }
        }
        for(String hook:List.of("PlayerDiscoveries0650:render","PlayerDiscoveries0650:mouseClicked","PlayerDiscoveries0650:accept","PlayerDiscoveries0650:acceptReply","PlayerFieldSurvey0620Dispatcher:handle","PlayerShipLink0680:resetClient"))check(hooks.contains(hook),"missing "+hook);
    }
}
