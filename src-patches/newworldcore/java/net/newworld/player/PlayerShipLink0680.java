package net.newworld.player;

import java.io.*;
import java.util.*;
import net.newworld.config.NewWorldConfig;
import static net.newworld.player.PlayerOverview0670.call;
import static net.newworld.player.PlayerOverview0670.stat;
import static net.newworld.player.PlayerOverview0670.field;
import static net.newworld.player.PlayerOverview0670.label;

/** Server-authoritative owner/range link. Never loads chunks or changes ship/world state. */
public final class PlayerShipLink0680 {
    public static final int WIRE_BASE = 1_480_000_000, BEGIN = WIRE_BASE + 0x1000000, END = BEGIN + 1;
    private static final int MAX_BYTES = 1024;
    private static final Map<Object, Long> REQUESTS = Collections.synchronizedMap(new WeakHashMap<>());
    private static final Map<Object, String> LOGGED = Collections.synchronizedMap(new WeakHashMap<>());
    private static Object connection, screenIdentity, levelIdentity, playerIdentity;
    private static Snapshot client;
    private static long receivedAt, requestedAt, begunAt, openedAt;
    private static ByteArrayOutputStream incoming;
    private static boolean renderError;
    private static boolean discoveriesRequested;
    private PlayerShipLink0680() {}

    public static int range() { return NewWorldConfig.integer("ship-link", "range_blocks", 5000, 16, 30000000); }
    public static int refreshTicks() { return NewWorldConfig.integer("ship-link", "refresh_ticks", 20, 20, 1200); }
    public static int staleTicks() { return Math.max(refreshTicks() * 2, NewWorldConfig.integer("ship-link", "stale_after_ticks", 120, 40, 3600)); }
    public static boolean dimensional() { return NewWorldConfig.bool("ship-link", "allow_dimensional_link", true); }

    public record Ship(String id, Object manager) {}
    public record Resolution(Ship ship, Snapshot snapshot) {}
    public record Snapshot(String state, String ship, String dimension, double distance, String reason,
                           int refresh, int stale) {
        public boolean allowed() { return !ship.isBlank() && (state.equals("CONNECTED") || state.equals("DIMENSIONAL")); }
    }
    private static Snapshot lost(String reason) { return new Snapshot("LOST", "", "UNKNOWN", -1, reason, refreshTicks(), staleTicks()); }

    /** Pure policy: own interior bypasses exterior distance/dimension, not ownership. */
    public static Snapshot classify(String id, boolean owned, boolean inside, String playerDim, String exteriorDim,
                                    double distance, int range, boolean dimensional) {
        if (!owned || id == null || id.isBlank()) return lost("NO OWNED SHIP");
        if (inside) return new Snapshot("CONNECTED", id, Objects.toString(exteriorDim, "UNKNOWN"), -1, "ON BOARD", refreshTicks(), staleTicks());
        if (playerDim == null || exteriorDim == null || exteriorDim.isBlank()) return lost("POSITION UNAVAILABLE");
        if (!playerDim.equals(exteriorDim)) return new Snapshot(dimensional ? "DIMENSIONAL" : "LOST", id, exteriorDim, -1,
                dimensional ? "DIFFERENT DIMENSION" : "DIMENSION LINK DISABLED", refreshTicks(), staleTicks());
        if (!Double.isFinite(distance) || distance < 0) return lost("POSITION UNAVAILABLE");
        return new Snapshot(distance <= range ? "CONNECTED" : "LOST", id, exteriorDim, distance,
                distance <= range ? "IN RANGE" : "OUT OF RANGE", refreshTicks(), staleTicks());
    }

    /** Called only on server thread. Prefer the player's own interior, then the existing owner resolver. */
    public static Resolution resolve(Object player) {
        try {
            Object level = call(player, "serverLevel"), owner = call(player, "getUUID");
            Object manager = null;
            Object ownInterior = null;
            try { ownInterior = stat("net.drgmes.dwm.common.tardis.TardisStateManager", "get", level); }
            catch (Exception notInterior) { /* Existing resolver checks the other loaded worlds. */ }
            if (ownInterior instanceof Optional<?> opt && opt.isPresent()
                    && owner.equals(call(opt.get(), "getOwner"))) manager = opt.get();
            if (manager == null) {
                Object found = stat("net.newworld.player.PlayerFieldSurveyRuntime", "findOwnedShip", player, level, call(player, "blockPosition"));
                if (found != null) manager = call(found, "manager");
            }
            return fromManager(player, level, manager);
        } catch (Throwable failure) { return new Resolution(null, lost("LINK DATA UNAVAILABLE")); }
    }

    /** Adapter is kept dependency-free so real reflective access and ownership checks have fixture tests. */
    public static Resolution fromManager(Object player, Object level, Object manager) {
        try {
            Object owner = call(player, "getUUID");
            if (owner == null || manager == null || !owner.equals(call(manager, "getOwner"))) return new Resolution(null, lost("NO OWNED SHIP"));
            String id = String.valueOf(call(manager, "getId"));
            Object world = call(manager, "getWorld");
            if (world == null) return new Resolution(null, lost("INTERIOR NOT LOADED"));
            boolean inside = level == world;
            String pDim = String.valueOf(call(call(level, "dimension"), "location"));
            String eDim = String.valueOf(call(call(manager, "getCurrentExteriorDimension"), "location"));
            double distance = -1;
            if (!inside && pDim.equals(eDim)) {
                Object pos = call(manager, "getCurrentExteriorPosition");
                double dx = number(call(player, "getX")) - number(call(pos, "getX"));
                double dy = number(call(player, "getY")) - number(call(pos, "getY"));
                double dz = number(call(player, "getZ")) - number(call(pos, "getZ"));
                distance = Math.hypot(Math.hypot(dx, dy), dz);
            }
            return new Resolution(new Ship(id, manager), classify(id, true, inside, pDim, eDim, distance, range(), dimensional()));
        } catch (Throwable failure) { return new Resolution(null, lost("LINK DATA UNAVAILABLE")); }
    }
    private static double number(Object value) { return value instanceof Number n ? n.doubleValue() : Double.NaN; }
    public static Object linkedShip(Object player) {
        Resolution r = resolve(player);
        return r.snapshot.allowed() ? r.ship : null;
    }
    public static boolean requireLink(Object player) {
        Resolution r = resolve(player);
        if (r.snapshot.allowed()) return true;
        PlayerDiscoveries0650.clearServerSelection(player);
        send(player, r.snapshot);
        try { stat("net.newworld.player.PlayerFieldSurvey0504Bridge", "sendResult", player, 101); } catch (Exception ignored) {}
        return false;
    }
    public static void request(Object player) {
        long now = System.nanoTime();
        synchronized (REQUESTS) {
            Long last = REQUESTS.get(player);
            if (last != null && now - last < refreshTicks() * 50_000_000L) return;
            REQUESTS.put(player, now);
        }
        Snapshot s = resolve(player).snapshot;
        if (!s.allowed()) PlayerDiscoveries0650.clearServerSelection(player);
        String key = s.state + "/" + s.ship + "/" + s.reason;
        if (!key.equals(LOGGED.put(player, key))) System.out.println("[NewWorld Ship Link] state=" + s.state + " ship=" + s.ship + " dimension=" + s.dimension + " reason=" + s.reason);
        send(player, s);
    }
    private static void send(Object player, Snapshot s) {
        try { for (int code : encode(s)) stat("net.newworld.player.PlayerFieldSurvey0504Bridge", "sendResult", player, code); }
        catch (Exception failure) { System.err.println("[NewWorld Ship Link] snapshot failed: " + failure); }
    }

    public static boolean isWireCode(int code) { return code >= WIRE_BASE && code <= END; }
    public static int[] encode(Snapshot s) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream(); DataOutputStream out = new DataOutputStream(bytes);
        out.writeInt(1);
        for (String v : List.of(s.state, s.ship, s.dimension, s.reason)) out.writeUTF(clean(v));
        out.writeDouble(s.distance); out.writeInt(s.refresh); out.writeInt(s.stale);
        byte[] raw = bytes.toByteArray(); if (raw.length > MAX_BYTES) throw new IOException("Oversized link frame");
        int[] result = new int[(raw.length + 2) / 3 + 2]; result[0] = BEGIN; result[result.length - 1] = END;
        for (int i = 0; i < raw.length; i += 3) {
            int bits = 0;
            for (int j = 0; j < 3; j++) bits = bits << 8 | (i + j < raw.length ? raw[i + j] & 255 : 0);
            result[i / 3 + 1] = WIRE_BASE + bits;
        }
        return result;
    }
    public static synchronized boolean accept(int code) {
        if (!isWireCode(code)) return false;
        if (code == BEGIN) { incoming = new ByteArrayOutputStream(); begunAt = System.nanoTime(); return true; }
        if (incoming == null) return true;
        if (System.nanoTime() - begunAt > 10_000_000_000L) { incoming = null; return true; }
        if (code == END) {
            try {
                DataInputStream in = new DataInputStream(new ByteArrayInputStream(incoming.toByteArray()));
                if (in.readInt() != 1) throw new IOException("Link schema");
                String state = in.readUTF(), ship = in.readUTF(), dim = in.readUTF(), reason = in.readUTF();
                for (String v : List.of(state, ship, dim, reason)) if (v.length() > 96) throw new IOException("Link text bound");
                double dist = in.readDouble(); int refresh = in.readInt(), stale = in.readInt();
                if (!Set.of("CONNECTED", "DIMENSIONAL", "LOST").contains(state) || !Double.isFinite(dist) || dist < -1
                        || refresh < 20 || refresh > 1200 || stale < refresh * 2 || stale > 3600) throw new IOException("Link values");
                if (in.available() > 2) throw new IOException("Link trailing bytes");
                while (in.available() > 0) if (in.readByte() != 0) throw new IOException("Link padding");
                Snapshot next = new Snapshot(state, ship, dim, dist, reason, refresh, stale);
                if (client == null || !client.ship.equals(next.ship) || client.allowed() != next.allowed()) {
                    PlayerDiscoveries0650.resetClientLink(); PlayerOverview0670.resetClient(); discoveriesRequested = false;
                }
                client = next; receivedAt = System.nanoTime();
            } catch (IOException failure) { System.err.println("[NewWorld Ship Link] decode rejected: " + failure); }
            incoming = null;
        } else if (incoming.size() + 3 <= MAX_BYTES + 2) {
            int bits = code - WIRE_BASE; incoming.write(bits >>> 16); incoming.write(bits >>> 8); incoming.write(bits);
        } else incoming = null;
        return true;
    }
    public static Snapshot clientSnapshot() { return client; }
    public static boolean fresh(long now, long received, long opened, int stale) {
        return received > 0 && received >= opened && now >= received && now - received <= stale * 50_000_000L;
    }
    public static boolean clientAllowed() {
        return client != null && client.allowed() && fresh(System.nanoTime(), receivedAt, openedAt, client.stale);
    }
    public static synchronized void resetClient() {
        client = null; receivedAt = 0; requestedAt = 0; incoming = null; discoveriesRequested = false;
        PlayerDiscoveries0650.resetClientLink(); PlayerOverview0670.resetClient();
    }

    /** Runs before the legacy screen render: requests link on every tab and invalidates stale sessions. */
    public static void beforeRender(Object screen) {
        try {
            Object mc = stat("net.minecraft.client.Minecraft", "getInstance");
            Object nextConnection = call(mc, "getConnection"), nextLevel = field(mc, "level"), nextPlayer = field(mc, "player");
            if (nextConnection != connection || nextLevel != levelIdentity || nextPlayer != playerIdentity || screen != screenIdentity) {
                resetClient(); openedAt = System.nanoTime();
                connection = nextConnection; levelIdentity = nextLevel; playerIdentity = nextPlayer; screenIdentity = screen;
            }
            long now = System.nanoTime();
            int ticks = client == null ? refreshTicks() : client.refresh;
            if (connection != null && now - requestedAt >= ticks * 50_000_000L) {
                requestedAt = now; PlayerGeologicalSurveyGui0620.sendSurveyMode(5);
            }
            if (!clientAllowed()) discoveriesRequested = false;
            else if (!discoveriesRequested && ((Number) field(screen, "tab")).intValue() == 2) {
                discoveriesRequested = true; PlayerGeologicalSurveyGui0620.sendSurveyMode(3);
            }
        } catch (Throwable failure) { resetClient(); }
    }
    public static void afterRender(Object screen, Object graphics) {
        try {
            int left = (((Number) field(screen, "width")).intValue() - 540) / 2;
            int top = (((Number) field(screen, "height")).intValue() - 300) / 2;
            boolean active = clientAllowed();
            boolean fresh = client != null && fresh(System.nanoTime(), receivedAt, openedAt, client.stale);
            drawHeader(screen, graphics, left, top, fresh ? client : lost(client == null ? "SYNCING" : "STALE"));
            if (!active && ((Number) field(screen, "tab")).intValue() != 0) {
                call(graphics, "flush");
                call(graphics, "fill", left + 10, top + 76, left + 530, top + 288, 0xFF09151B);
                call(graphics, "flush");
                label(screen, graphics, "SHIP LINK LOST // REMOTE FEATURES LOCKED", left + 26, top + 100, 0xFFFFCF45, 488);
                label(screen, graphics, fresh ? client.reason : "Waiting for fresh server link...", left + 26, top + 125, 0xFF8DA7B4, 488);
                label(screen, graphics, "Reconnect to your ship to use Survey and Discoveries.", left + 26, top + 149, 0xFFD5E7EF, 488);
            }
        } catch (Throwable failure) {
            if (!renderError) { renderError = true; System.err.println("[NewWorld Ship Link] render failed: " + failure); }
        }
    }
    public static void drawHeader(Object screen, Object graphics, int left, int top, Snapshot s) throws Exception {
        call(graphics, "flush");
        call(graphics, "fill", left + 296, top + 6, left + 532, top + 40, 0xFF071118);
        call(graphics, "flush");
        String distance = s.distance >= 0 ? " // " + Math.round(s.distance) + " BLK" : s.reason.equals("ON BOARD") ? " // ON BOARD" : "";
        label(screen, graphics, "LINK " + s.state + distance, left + 302, top + 11,
                s.state.equals("LOST") ? 0xFFFFCF45 : s.state.equals("DIMENSIONAL") ? 0xFF62D5F1 : 0xFF64EAB5, 224);
        label(screen, graphics, s.dimension.equals("UNKNOWN") ? s.reason : "SHIP " + s.dimension, left + 302, top + 25, 0xFF8DA7B4, 224);
    }
    public static boolean blockContentClick(Object screen, double x, double y) throws Exception {
        int tab = ((Number) field(screen, "tab")).intValue();
        int left = (((Number) field(screen, "width")).intValue() - 540) / 2;
        int top = (((Number) field(screen, "height")).intValue() - 300) / 2;
        return tab != 0 && !clientAllowed() && x >= left + 10 && x < left + 530 && y >= top + 76 && y < top + 288;
    }
    private static String clean(String text) { String s = text.replaceAll("[\\p{Cntrl}§]", " "); return s.substring(0, Math.min(96, s.length())); }
}
