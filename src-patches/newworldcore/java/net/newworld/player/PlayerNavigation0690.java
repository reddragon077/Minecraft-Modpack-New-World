package net.newworld.player;

import java.io.*;
import java.util.*;
import net.newworld.config.NewWorldConfig;
import net.newworld.config.NewWorldTuning;
import static net.newworld.player.PlayerOverview0670.*;

/** Read-only view of the existing selected discovery, route plan and loaded flight destination. */
public final class PlayerNavigation0690 {
    public static final int WIRE_BASE = 1_510_000_000, BEGIN = WIRE_BASE + 0x1000000, END = BEGIN + 1;
    private static final int MAX_BYTES = 4096;
    private static final Map<Object, Long> REQUESTS = Collections.synchronizedMap(new WeakHashMap<>());
    private static final Map<Object, String> LOGGED = Collections.synchronizedMap(new WeakHashMap<>());
    private static Snapshot client;
    private static long receivedAt, requestedAt, begunAt;
    private static ByteArrayOutputStream incoming;
    private static boolean renderError;
    private PlayerNavigation0690() {}

    public static int refreshTicks() { return NewWorldConfig.integer("player-navigation", "refresh_ticks", 20, 20, 1200); }
    public static int staleTicks() { return Math.max(refreshTicks() * 2, NewWorldConfig.integer("player-navigation", "stale_after_ticks", 120, 40, 3600)); }
    public static boolean showCoordinates() { return NewWorldConfig.bool("player-navigation", "show_coordinates", true); }
    public static boolean showEstimate() { return NewWorldConfig.bool("player-navigation", "show_we_estimate", true); }

    public record Snapshot(String ship, String status, String target, String dimension, String position,
            String distance, String route, String progress, String nextHop, String nextDistance,
            String estimate, String warp, int refresh, int stale) {
        public static Snapshot unavailable(String reason) {
            return new Snapshot("", reason, "", "", "", "", "", "", "", "", "", "", refreshTicks(), staleTicks());
        }
        List<String> texts() { return List.of(ship, status, target, dimension, position, distance, route, progress, nextHop, nextDistance, estimate, warp); }
    }

    /** Invoked on the server thread by the common dispatcher; no data is read before owner/range resolution. */
    public static void request(Object player) {
        long now = System.nanoTime();
        Long previous = REQUESTS.get(player);
        if (previous != null && now - previous < refreshTicks() * 50_000_000L) return;
        REQUESTS.put(player, now);
        try {
            Snapshot snapshot = sample(player);
            for (int code : encode(snapshot)) stat("net.newworld.player.PlayerFieldSurvey0504Bridge", "sendResult", player, code);
            String summary = snapshot.ship + "/" + snapshot.status + "/" + snapshot.target + "/" + snapshot.route + "/" + snapshot.progress;
            if (!summary.equals(LOGGED.put(player, summary))) System.out.println("[NewWorld Player Navigation] " + summary);
        } catch (Exception failure) { System.err.println("[NewWorld Player Navigation] snapshot failed: " + failure); }
    }

    public static Snapshot sample(Object player) {
        PlayerShipLink0680.Resolution link = PlayerShipLink0680.resolve(player);
        if (!link.snapshot().allowed() || link.ship() == null) return Snapshot.unavailable("SHIP LINK LOST // " + link.snapshot().reason());
        try {
            String id = link.ship().id(); Object manager = link.ship().manager();
            Object world = call(manager, "getWorld");
            if (world == null) return Snapshot.unavailable("INTERIOR NOT LOADED");
            Object data = stat("net.newworld.navigation.NavigationDiscoverySavedData", "get", world);
            // Map lookup only: unlike state(), this does not create an empty ship record.
            Object state = ((Map<?, ?>) field(data, "ships")).get(id);
            Object selected = null;
            if (state != null) selected = ((Map<?, ?>) field(state, "discoveries")).get(field(state, "selectedKey"));
            var plansField = Class.forName("net.newworld.navigation.Navigation0472ServerRoute").getDeclaredField("PLANS");
            plansField.setAccessible(true);
            Object plan = ((Map<?, ?>) plansField.get(null)).get(id);
            return describe(id, manager, selected, plan, String.valueOf(stat("net.newworld.core.WarpEnergySystem", "getEnergy", world)));
        } catch (Exception failure) {
            System.err.println("[NewWorld Player Navigation] sampling failed: " + failure);
            return Snapshot.unavailable("NAVIGATION DATA UNAVAILABLE");
        }
    }

    /** Adapter separated for regression fixtures. Never invokes a selector, route calculator or flight setter. */
    public static Snapshot describe(String id, Object manager, Object selected, Object plan, String warp) throws Exception {
        Object currentDim = call(manager, "getCurrentExteriorDimension"), currentPos = call(manager, "getCurrentExteriorPosition");
        String target = "NO TARGET SELECTED", dim = "", pos = "", dist = "";
        if (selected != null) {
            target = textField(selected, "label");
            if ("GEOLOGY".equals(textField(selected, "kind"))) target = NewWorldTuning.geologyAnalysisLabel(integer(selected, "analysisLevel"), target);
            dim = textField(selected, "dimension"); pos = position(integer(selected, "x"), integer(selected, "y"), integer(selected, "z"));
            dist = targetDistance(String.valueOf(call(currentDim, "location")), dim,
                    coordinate(currentPos, "X"), coordinate(currentPos, "Y"), coordinate(currentPos, "Z"),
                    integer(selected, "x"), integer(selected, "y"), integer(selected, "z"));
        }
        String route = "NO ROUTE", progress = "NO LOADED HOP", next = "NONE", nextDistance = "", estimate = "NOT AVAILABLE";
        if (plan != null && id.equals(textField(plan, "shipId"))) {
            route = textField(plan, "status");
            List<?> points = (List<?>) field(plan, "points");
            int loaded = ((Number) stat("net.newworld.navigation.Navigation0477MultiHop", "loadedHop", route)).intValue();
            progress = "ROUTE_COMPLETE".equals(route) ? "COMPLETE / " + points.size() + " HOPS" : "HOPS " + points.size() + " / NONE LOADED";
            if (loaded > 0 && loaded <= points.size()) {
                Object hop = points.get(loaded - 1);
                progress = "LOADED HOP " + loaded + " / " + points.size();
                next = position(integer(hop, "x"), integer(hop, "y"), integer(hop, "z"));
                String routeDim = textField(plan, "dimension");
                nextDistance = targetDistance(String.valueOf(call(currentDim, "location")), routeDim,
                        coordinate(currentPos, "X"), coordinate(currentPos, "Y"), coordinate(currentPos, "Z"),
                        integer(hop, "x"), integer(hop, "y"), integer(hop, "z"));
                // Estimate only the destination actually loaded into this ship, never a different selected target.
                Object destination = call(manager, "getDestinationExteriorPosition"), destinationDim = call(manager, "getDestinationExteriorDimension");
                if (destination != null && destinationDim != null && routeDim.equals(String.valueOf(call(destinationDim, "location")))
                        && coordinate(destination, "X") == integer(hop, "x") && coordinate(destination, "Y") == integer(hop, "y") && coordinate(destination, "Z") == integer(hop, "z")) {
                    estimate = estimateLoadedHop(manager, id, currentDim, destinationDim, currentPos, destination);
                } else estimate = "DESTINATION CHANGED";
            }
        }
        return new Snapshot(id, "LIVE / READ ONLY", target, dim, pos, dist, route, progress, next, nextDistance, estimate, warp, refreshTicks(), staleTicks());
    }

    private static String estimateLoadedHop(Object manager, String id, Object fromDim, Object toDim, Object from, Object to) {
        try {
            String gate = "net.drgmes.dwm.newworld.EngineTravelGate";
            int distance = Objects.equals(fromDim, toDim) ? ((Number) stat(gate, "distance", from, to)).intValue() : 0;
            int[] modules = (int[]) stat(gate, "engineModules", id);
            int efficiency = ((Number) stat(gate, "module", modules, 1)).intValue();
            int cost = ((Number) stat("net.drgmes.dwm.newworld.EngineTravelBalance", "warpCost", distance, efficiency, fromDim, toDim)).intValue();
            return cost > 0 ? cost + " WE / NEXT HOP" : "NOT AVAILABLE";
        } catch (Exception failure) { return "NOT AVAILABLE"; }
    }

    public static String targetDistance(String shipDim, String targetDim, int x, int y, int z, int tx, int ty, int tz) {
        if (!shipDim.equals(targetDim)) return "DIFFERENT DIMENSION";
        double dx = (double) tx - x, dy = (double) ty - y, dz = (double) tz - z;
        return "SHIP DIST " + Math.round(Math.sqrt(dx * dx + dy * dy + dz * dz)) + " BLOCKS";
    }
    private static String position(int x, int y, int z) { return "X=" + x + " Y=" + y + " Z=" + z; }
    private static int coordinate(Object pos, String axis) throws Exception { return ((Number) call(pos, "get" + axis)).intValue(); }
    private static int integer(Object o, String name) throws Exception { return ((Number) field(o, name)).intValue(); }
    private static String textField(Object o, String name) throws Exception { return Objects.toString(field(o, name), ""); }
    private static String clean(String text) { String s = text.replaceAll("[\\p{Cntrl}§]", " "); return s.substring(0, Math.min(96, s.length())); }

    public static boolean isWireCode(int code) { return code >= WIRE_BASE && code <= END; }
    public static int[] encode(Snapshot s) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream(); DataOutputStream out = new DataOutputStream(bytes);
        out.writeInt(1); for (String t : s.texts()) out.writeUTF(clean(t)); out.writeInt(s.refresh); out.writeInt(s.stale);
        byte[] raw = bytes.toByteArray(); if (raw.length > MAX_BYTES) throw new IOException("Navigation size");
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
                if (in.readInt() != 1) throw new IOException("Navigation schema");
                String[] t = new String[12]; for (int i = 0; i < t.length; i++) { t[i] = in.readUTF(); if (t[i].length() > 96) throw new IOException("Text bound"); }
                int refresh = in.readInt(), stale = in.readInt();
                if (refresh < 20 || refresh > 1200 || stale < refresh * 2 || stale > 3600 || in.available() > 2) throw new IOException("Navigation timing/trailing bytes");
                while (in.available() > 0) if (in.readByte() != 0) throw new IOException("Padding");
                client = new Snapshot(t[0], t[1], t[2], t[3], t[4], t[5], t[6], t[7], t[8], t[9], t[10], t[11], refresh, stale);
                receivedAt = System.nanoTime();
            } catch (IOException failure) { System.err.println("[NewWorld Player Navigation] decode rejected: " + failure); }
            incoming = null;
        } else if (incoming.size() + 3 <= MAX_BYTES + 2) {
            int bits = code - WIRE_BASE; incoming.write(bits >>> 16); incoming.write(bits >>> 8); incoming.write(bits);
        } else incoming = null;
        return true;
    }
    public static Snapshot clientSnapshot() { return client; }
    public static synchronized void resetClient() { client = null; receivedAt = 0; requestedAt = 0; incoming = null; }
    public static boolean visible(Snapshot s, PlayerShipLink0680.Snapshot link, boolean allowed, long now, long received) {
        return allowed && s != null && link != null && s.ship.equals(link.ship()) && PlayerShipLink0680.fresh(now, received, 0, s.stale);
    }
    public static void render(Object screen, Object graphics, int left, int top) {
        try {
            long now = System.nanoTime(); boolean allowed = PlayerShipLink0680.clientAllowed();
            if (!allowed) { resetClient(); return; }
            int refresh = client == null ? refreshTicks() : client.refresh;
            if (requestedAt == 0 || now - requestedAt >= refresh * 50_000_000L) { requestedAt = now; PlayerGeologicalSurveyGui0620.sendSurveyMode(6); }
            draw(screen, graphics, left, top, client, visible(client, PlayerShipLink0680.clientSnapshot(), allowed, now, receivedAt));
        } catch (Exception failure) { if (!renderError) { renderError = true; System.err.println("[NewWorld Player Navigation] render failed: " + failure); } }
    }
    public static void draw(Object screen, Object graphics, int left, int top, Snapshot s, boolean fresh) throws Exception {
        label(screen, graphics, "NAVIGATION // " + (fresh ? s.status : "SYNCING"), left + 26, top + 91, 0xFF62D5F1, 488);
        if (!fresh || s.ship.isBlank()) {
            label(screen, graphics, fresh ? s.status : "Waiting for fresh navigation data...", left + 26, top + 120, 0xFFFFCF45, 488); return;
        }
        call(graphics, "fill", left + 26, top + 110, left + 265, top + 248, 0xFF112A35);
        call(graphics, "fill", left + 275, top + 110, left + 514, top + 248, 0xFF112A35);
        String[] target = {"SELECTED TARGET", s.target, s.dimension, showCoordinates() ? s.position : "COORDINATES HIDDEN", s.distance,
                "WARP AVAILABLE " + s.warp + " WE"};
        String[] route = {"EXISTING SHIP ROUTE", s.route, s.progress, showCoordinates() ? "NEXT " + s.nextHop : "COORDINATES HIDDEN", s.nextDistance,
                showEstimate() ? "EST " + s.estimate : "WE ESTIMATE HIDDEN"};
        for (int i = 0; i < target.length; i++) {
            label(screen, graphics, target[i], left + 33, top + 118 + i * 20, i == 1 ? 0xFF64EAB5 : 0xFFD5E7EF, 225);
            label(screen, graphics, route[i], left + 282, top + 118 + i * 20, i == 1 ? 0xFFFFCF45 : 0xFFD5E7EF, 225);
        }
        label(screen, graphics, "Next-hop estimate only; flight gates still apply.", left + 26, top + 258, 0xFF8DA7B4, 488);
        label(screen, graphics, "Use Discoveries TARGET/ROUTE or the ship terminal.", left + 26, top + 273, 0xFF8DA7B4, 488);
    }
}
