package net.newworld.player;

import java.io.*;
import java.util.*;
import net.newworld.config.NewWorldConfig;
import static net.newworld.player.PlayerOverview0670.*;

/** Owner-scoped telemetry; stop clicks delegate to the separately guarded shield-OFF action. */
public final class PlayerMining0700 {
    public static final int MODE = 9, WIRE_BASE = 1_540_000_000, BEGIN = WIRE_BASE + 0x1000000, END = BEGIN + 1;
    private static final int MAX_BYTES = 4096, TEXTS = 19;
    private static final String MINING = "net.newworld.mining.";
    private static final Map<Object, Long> REQUESTS = Collections.synchronizedMap(new WeakHashMap<>());
    private static final Map<Object, String> LOGGED = Collections.synchronizedMap(new WeakHashMap<>());
    private static Snapshot client;
    private static long receivedAt, requestedAt, begunAt;
    private static ByteArrayOutputStream incoming;
    private static boolean renderError;
    private static boolean details;
    private PlayerMining0700() {}

    public static int refreshTicks() { return NewWorldConfig.integer("player-mining", "refresh_ticks", 20, 20, 1200); }
    public static int staleTicks() { return Math.max(refreshTicks() * 2, NewWorldConfig.integer("player-mining", "stale_after_ticks", 120, 40, 3600)); }
    public static boolean showArea() { return NewWorldConfig.bool("player-mining", "show_scan_area", true); }
    public static boolean showBuffers() { return NewWorldConfig.bool("player-mining", "show_buffers", true); }
    public static int resourceRows() { return NewWorldConfig.integer("player-mining", "top_resources.rows", 5, 1, 5); }

    public record Snapshot(String ship, String status, String phase, String dimension, String area,
            String scan, String extraction, String remaining, String collection, String ae,
            String replication, String routing, String flow, int refresh, int stale,
            String yieldScope, List<String> yields, boolean stopAllowed) {
        public Snapshot { yields = List.copyOf(yields); if (yields.size() > 5) throw new IllegalArgumentException("Yield rows"); }
        public Snapshot(String ship, String status, String phase, String dimension, String area, String scan, String extraction,
                String remaining, String collection, String ae, String replication, String routing, String flow, int refresh, int stale) {
            this(ship, status, phase, dimension, area, scan, extraction, remaining, collection, ae, replication, routing, flow, refresh, stale, "NO TRACKED MINING YET", List.of(), false);
        }
        List<String> texts() {
            List<String> t = new ArrayList<>(List.of(ship, status, phase, dimension, area, scan, extraction, remaining, collection, ae, replication, routing, flow, yieldScope));
            for (int i = 0; i < 5; i++) t.add(i < yields.size() ? yields.get(i) : ""); return t;
        }
        public static Snapshot unavailable(String ship, String reason) {
            return new Snapshot(ship, reason, "UNKNOWN", "", "NO SCAN AREA", "N/A", "N/A", "UNKNOWN", "UNKNOWN", "UNKNOWN", "UNKNOWN", "UNKNOWN", "", refreshTicks(), staleTicks());
        }
    }

    public static void request(Object player) {
        long now = System.nanoTime(); Long previous = REQUESTS.get(player);
        if (previous != null && now - previous < refreshTicks() * 50_000_000L) return;
        REQUESTS.put(player, now);
        try {
            Snapshot s = sample(player);
            if (s.stopAllowed) PlayerMiningStop0701.observe(player, s.ship);
            for (int code : encode(s)) stat("net.newworld.player.PlayerFieldSurvey0504Bridge", "sendResult", player, code);
            String summary = s.ship + "/" + s.status + "/" + s.phase + "/" + s.routing;
            if (!summary.equals(LOGGED.put(player, summary))) System.out.println("[NewWorld Player Mining] " + summary);
        } catch (Exception e) { System.err.println("[NewWorld Player Mining] snapshot failed: " + e); }
    }

    public static Snapshot sample(Object player) {
        PlayerShipLink0680.Resolution link = PlayerShipLink0680.resolve(player);
        if (!link.snapshot().allowed() || link.ship() == null) return Snapshot.unavailable("", "SHIP LINK LOST");
        String id = link.ship().id();
        try {
            Object manager = link.ship().manager(), world = call(manager, "getWorld");
            if (world == null) return Snapshot.unavailable(id, "INTERIOR NOT LOADED");
            Object runtime = state(stat(MINING + "MiningRuntimeSavedData", "get", world), id);
            Object phase = state(stat(MINING + "MiningPhaseSavedData", "get", world), id);
            Object routing = state(stat(MINING + "MiningRoutingSavedData", "get", world), id);
            return describe(id, manager, runtime, phase, routing,
                    buffer(world, runtime, "bufferPos", "COLLECTION_ID"),
                    buffer(world, routing, "aeBufferPos", "AE_BUFFER_ID"),
                    buffer(world, routing, "replicationBufferPos", "REP_BUFFER_ID"));
        } catch (Exception e) {
            System.err.println("[NewWorld Player Mining] sampling failed: " + e);
            return Snapshot.unavailable(id, "MINING DATA UNAVAILABLE");
        }
    }

    /** Map lookup intentionally replaces state(id), which would create persistent records. */
    public static Object state(Object data, String id) throws Exception { return ((Map<?, ?>) field(data, "states")).get(id); }

    public static Snapshot describe(String id, Object manager, Object runtime, Object phase, Object routing,
            String collection, String ae, String replication) throws Exception {
        String status = runtime == null ? "NO MINING DATA" : Objects.toString(field(runtime, "status"), "UNKNOWN").replace('_', ' ');
        String phaseText = "NO SCAN", dim = "", area = "NO SCAN AREA", scan = "N/A", extraction = "N/A", remaining = "UNKNOWN";
        if (phase != null && !str(phase, "exteriorDimension").isBlank()) {
            dim = str(phase, "exteriorDimension");
            int cx = (int) num(phase, "chunkX"), cz = (int) num(phase, "chunkZ"), p = (int) num(phase, "phase");
            Object pos = call(manager, "getCurrentExteriorPosition");
            String currentDim = String.valueOf(call(call(manager, "getCurrentExteriorDimension"), "location"));
            boolean here = dim.equals(currentDim) && Math.floorDiv(((Number) call(pos, "getX")).intValue(), 16) == cx
                    && Math.floorDiv(((Number) call(pos, "getZ")).intValue(), 16) == cz;
            area = (here ? "SCAN CENTER " : "LAST SCAN ") + "CH " + cx + ", " + cz;
            phaseText = switch (p) { case 0 -> "IDLE"; case 1 -> "SCANNING"; case 2 -> "EXTRACTION"; case 3 -> "WAITING RESEARCH"; case 4 -> "COMPLETED"; default -> "UNKNOWN"; };
            scan = progress(num(phase, "scanCursor"), num(phase, "totalBlocks"));
            // found contains resource targets only; foundTotal() also includes hazards and is NOT this denominator.
            long found = 0;
            for (Object count : ((Map<?, ?>) field(phase, "found")).values()) if (count instanceof Number n) found = add(found, n.longValue());
            extraction = p == 1 ? "N/A / SCANNING" : progress(num(phase, "minedTargets"), found);
            remaining = "PENDING " + amount(((Number) call(phase, "knownRemaining")).longValue())
                    + " / UNKNOWN " + amount(((Number) call(phase, "unknownRemaining")).longValue());
        }
        int mode = routing == null ? -1 : (int) num(routing, "mode");
        String routingText = switch (mode) { case 0 -> "ROUTING PAUSED"; case 1 -> "SMART AUTO"; case 2 -> "FORCE REPLICATION"; default -> "ROUTING UNKNOWN"; };
        String flow = routing == null ? "NO ROUTING DATA" : "MOVED AE " + amount(num(routing, "routedToAe")) + " / REP " + amount(num(routing, "routedToReplication"));
        List<String> yields = MiningYield0701.top(phase, resourceRows()).stream().map(e -> amount(e.getValue()) + " BLOCKS / " + e.getKey()).toList();
        return new Snapshot(id, status, phaseText, dim, area, scan, extraction, remaining, collection, ae, replication, routingText, flow, refreshTicks(), staleTicks(), MiningYield0701.scope(phase), yields, PlayerMiningStop0701.enabled());
    }

    public static String buffer(Object world, Object owner, String positionField, String expectedId) {
        try {
            if (owner == null) return "NOT BOUND";
            long packed = num(owner, positionField);
            if (packed == Long.MIN_VALUE) return "NOT BOUND";
            Object pos = stat("net.minecraft.core.BlockPos", "of", packed);
            if (!loaded(world, pos)) return "UNLOADED";
            Object type = Class.forName(MINING + "MiningRoutingRuntime").getField(expectedId).get(null);
            if (stat(MINING + "MiningRoutingRuntime", "valid", world, packed, type) == null) return "MISSING";
            Object data = stat(MINING + "MiningBufferSavedData", "get", world);
            List<?> ids = (List<?>) call(data, "itemIds", world, pos);
            long count = 0; int types = 0;
            for (Object item : ids) {
                long n = ((Number) call(data, "count", world, pos, item)).longValue();
                if (n > 0) { count = add(count, n); types++; }
            }
            int capacity = Class.forName(MINING + "MiningBufferSavedData").getField("VIRTUAL_SLOTS").getInt(null);
            return bufferText(count, types, capacity);
        } catch (Exception e) { return "UNAVAILABLE"; }
    }
    public static String bufferText(long count, int types, int capacity) { return amount(count) + " ITEMS / " + types + "/" + capacity + " TYPES"; }
    /** LevelReader may supply this as an inherited interface default, not a declared class method. */
    public static boolean loaded(Object world, Object pos) throws Exception {
        for (var method : world.getClass().getMethods()) {
            if (method.getName().equals("hasChunkAt") && method.getParameterCount() == 1
                    && method.getReturnType() == boolean.class && method.getParameterTypes()[0].isInstance(pos))
                return Boolean.TRUE.equals(method.invoke(world, pos));
        }
        throw new NoSuchMethodException("hasChunkAt(BlockPos)");
    }
    public static long add(long a, long b) { a = Math.max(0, a); b = Math.max(0, b); return Long.MAX_VALUE - a < b ? Long.MAX_VALUE : a + b; }
    public static String progress(long done, long total) {
        if (total <= 0 || done < 0) return "N/A";
        double percent = Math.min(100.0, 100.0 * ((double) done / total));
        return String.format(Locale.ROOT, "%.1f%% / %s OF %s", percent, amount(done), amount(total));
    }
    private static long num(Object o, String f) throws Exception { return ((Number) field(o, f)).longValue(); }
    private static String str(Object o, String f) throws Exception { return Objects.toString(field(o, f), ""); }
    private static String clean(String text) { String s = text.replaceAll("[\\p{Cntrl}§]", " "); return s.substring(0, Math.min(96, s.length())); }

    public static boolean isWireCode(int code) { return code >= WIRE_BASE && code <= END; }
    public static int[] encode(Snapshot s) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream(); DataOutputStream out = new DataOutputStream(bytes);
        out.writeInt(2); for (String t : s.texts()) out.writeUTF(clean(t)); out.writeInt(s.refresh); out.writeInt(s.stale); out.writeBoolean(s.stopAllowed);
        byte[] raw = bytes.toByteArray(); if (raw.length > MAX_BYTES) throw new IOException("Mining size");
        int[] codes = new int[(raw.length + 2) / 3 + 2]; codes[0] = BEGIN; codes[codes.length - 1] = END;
        for (int i = 0; i < raw.length; i += 3) {
            int bits = 0; for (int j = 0; j < 3; j++) bits = bits << 8 | (i + j < raw.length ? raw[i + j] & 255 : 0);
            codes[i / 3 + 1] = WIRE_BASE + bits;
        }
        return codes;
    }
    public static synchronized boolean accept(int code) {
        if (!isWireCode(code)) return false;
        if (code == BEGIN) { incoming = new ByteArrayOutputStream(); begunAt = System.nanoTime(); return true; }
        if (incoming == null) return true;
        if (System.nanoTime() - begunAt > 10_000_000_000L) { incoming = null; return true; }
        if (code == END) {
            try {
                DataInputStream in = new DataInputStream(new ByteArrayInputStream(incoming.toByteArray()));
                if (in.readInt() != 2) throw new IOException("Mining schema");
                String[] t = new String[TEXTS]; for (int i = 0; i < t.length; i++) { t[i] = in.readUTF(); if (t[i].length() > 96) throw new IOException("Text bound"); }
                int refresh = in.readInt(), stale = in.readInt(); boolean stopAllowed = in.readBoolean();
                if (refresh < 20 || refresh > 1200 || stale < refresh * 2 || stale > 3600 || in.available() > 2) throw new IOException("Mining timing/trailing bytes");
                while (in.available() > 0) if (in.readByte() != 0) throw new IOException("Padding");
                if (client != null && !client.ship.equals(t[0])) PlayerMiningStop0701.resetClient();
                client = new Snapshot(t[0], t[1], t[2], t[3], t[4], t[5], t[6], t[7], t[8], t[9], t[10], t[11], t[12], refresh, stale, t[13], Arrays.stream(t, 14, 19).filter(v -> !v.isEmpty()).toList(), stopAllowed);
                receivedAt = System.nanoTime();
            } catch (IOException e) { System.err.println("[NewWorld Player Mining] decode rejected: " + e); }
            incoming = null;
        } else if (incoming.size() + 3 <= MAX_BYTES + 2) {
            int bits = code - WIRE_BASE; incoming.write(bits >>> 16); incoming.write(bits >>> 8); incoming.write(bits);
        } else incoming = null;
        return true;
    }
    public static Snapshot clientSnapshot() { return client; }
    public static synchronized void resetClient() { client = null; receivedAt = 0; requestedAt = 0; incoming = null; details = false; PlayerMiningStop0701.resetClient(); }
    public static boolean freshClient() { return visible(client, PlayerShipLink0680.clientSnapshot(), PlayerShipLink0680.clientAllowed(), System.nanoTime(), receivedAt); }
    public static boolean visible(Snapshot s, PlayerShipLink0680.Snapshot link, boolean allowed, long now, long received) {
        return allowed && s != null && link != null && !s.ship.isBlank() && s.ship.equals(link.ship()) && PlayerShipLink0680.fresh(now, received, 0, s.stale);
    }
    public static void render(Object screen, Object graphics, int left, int top) {
        try {
            long now = System.nanoTime(); boolean allowed = PlayerShipLink0680.clientAllowed();
            if (!allowed) { resetClient(); return; }
            int refresh = client == null ? refreshTicks() : client.refresh;
            if (requestedAt == 0 || now - requestedAt >= refresh * 50_000_000L) { requestedAt = now; PlayerGeologicalSurveyGui0620.sendSurveyMode(MODE); }
            draw(screen, graphics, left, top, client, visible(client, PlayerShipLink0680.clientSnapshot(), allowed, now, receivedAt));
        } catch (Exception e) { if (!renderError) { renderError = true; System.err.println("[NewWorld Player Mining] render failed: " + e); } }
    }
    public static void draw(Object screen, Object graphics, int left, int top, Snapshot s, boolean fresh) throws Exception {
        label(screen, graphics, "MINING // " + (fresh ? (details ? "MINED RESOURCES" : "LIVE") : "SYNCING"), left + 26, top + 91, 0xFF62D5F1, 488);
        if (!fresh) { PlayerMiningStop0701.resetClient(); label(screen, graphics, "Waiting for fresh mining data...", left + 26, top + 120, 0xFFFFCF45, 488); return; }
        if (details) {
            drawDetails(screen, graphics, left, top, s);
            drawActions(screen, graphics, left, top, s); return;
        }
        call(graphics, "fill", left + 26, top + 110, left + 265, top + 250, 0xFF112A35);
        call(graphics, "fill", left + 275, top + 110, left + 514, top + 250, 0xFF112A35);
        String[] a = {s.status, "PHASE " + s.phase, showArea() ? s.dimension : "SCAN AREA HIDDEN", showArea() ? s.area : "",
                "SCAN " + s.scan, "EXTRACT " + s.extraction, s.remaining};
        String[] b = {"COLLECTION BUFFER", showBuffers() ? s.collection : "BUFFER COUNTS HIDDEN", "AE TRANSFER BUFFER", showBuffers() ? s.ae : "",
                "REPLICATION FEED", showBuffers() ? s.replication : "", s.routing};
        for (int i = 0; i < a.length; i++) {
            label(screen, graphics, a[i], left + 33, top + 118 + i * 18, i == 0 ? 0xFF64EAB5 : 0xFFD5E7EF, 225);
            label(screen, graphics, b[i], left + 282, top + 118 + i * 18, i == 6 ? 0xFF64EAB5 : 0xFFD5E7EF, 225);
        }
        drawActions(screen, graphics, left, top, s);
    }
    public static void drawDetails(Object screen, Object graphics, int left, int top, Snapshot s) throws Exception {
        label(screen, graphics, s.yieldScope, left + 26, top + 112, 0xFF8DA7B4, 488);
        label(screen, graphics, "Mined blocks, not stack items. New scan area resets counts.", left + 26, top + 128, 0xFF8DA7B4, 488);
        for (int i = 0; i < 5; i++) {
            String text = i < s.yields.size() ? s.yields.get(i) : i == 0 ? "No successful resource extraction recorded yet." : "";
            label(screen, graphics, text, left + 26, top + 148 + i * 18, 0xFF64EAB5, 488);
        }
        label(screen, graphics, s.flow, left + 26, top + 244, 0xFF8DA7B4, 488);
    }
    private static void drawActions(Object screen, Object graphics, int left, int top, Snapshot s) throws Exception {
        call(graphics, "fill", left + 26, top + 258, left + 265, top + 276, 0xFF194456);
        label(screen, graphics, details ? "< MINING OVERVIEW" : "MINED RESOURCES / FLOW", left + 33, top + 263, 0xFF62D5F1, 225);
        call(graphics, "fill", left + 275, top + 258, left + 514, top + 276, s.stopAllowed ? 0xFF643B25 : 0xFF182329);
        label(screen, graphics, s.stopAllowed ? PlayerMiningStop0701.button() : "STOP DISABLED BY SERVER", left + 282, top + 263, 0xFFFFCF45, 225);
        String msg = PlayerMiningStop0701.message();
        label(screen, graphics, msg.isBlank() ? "Scan-area counts, not deposit reserves. Stop: shield OFF only." : msg, left + 26, top + 279, 0xFF8DA7B4, 488);
    }
    public static boolean mouseClicked(double x, double y, int button, int left, int top) {
        if (x < left + 10 || x >= left + 530 || y < top + 76 || y >= top + 288) return false;
        if (button != 0 || !freshClient()) { PlayerMiningStop0701.resetClient(); return true; }
        if (y >= top + 258 && y < top + 276) {
            if (x >= left + 26 && x < left + 265) { details = !details; PlayerMiningStop0701.resetClient(); }
            else if (x >= left + 275 && x < left + 514) PlayerMiningStop0701.click();
        }
        return true;
    }
}
