package net.newworld.player;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import net.newworld.config.NewWorldConfig;

/** Read-only, owner-resolved ship telemetry. No chunk loads, control writes or new saved-data schema. */
public final class PlayerOverview0670 {
    // Each data int contains three bytes in an isolated positive range; no raw int can become a command.
    public static final int WIRE_BASE = 1_450_000_000;
    public static final int BEGIN = WIRE_BASE + 0x1000000;
    public static final int END = BEGIN + 1;
    private static final int MAX_BYTES = 4096;
    private static final Map<Object, ServerView> SERVER = Collections.synchronizedMap(new WeakHashMap<>());
    private static final Map<Object, Long> REQUESTS = Collections.synchronizedMap(new WeakHashMap<>());
    private static final Map<Object, ClientView> VIEWS = Collections.synchronizedMap(new WeakHashMap<>());
    private static volatile Snapshot client;
    private static volatile long receivedAt;
    private static ByteArrayOutputStream incoming;
    private static long begunAt;
    private static Object clientConnection;
    private static boolean renderErrorLogged;
    private PlayerOverview0670() {}

    public static int refreshTicks() { return cfg("refresh_ticks", 40, 20, 1200); }
    public static int warningPercent() { return cfg("warning_percent", 20, 1, 99); }
    public static int criticalPercent() { return Math.min(warningPercent(), cfg("critical_percent", 5, 0, 98)); }
    public static int warningRows() { return cfg("warning_rows", 2, 1, 3); }
    public static int staleTicks() { return Math.max(refreshTicks() * 2, cfg("stale_after_ticks", 120, 40, 3600)); }
    private static int cfg(String key, int value, int min, int max) {
        return NewWorldConfig.integer("overview", key, value, min, max);
    }

    /** All state sampling is dispatched onto the owning server thread, bounded per connected player. */
    public static void request(Object player) {
        try {
            long now = System.nanoTime();
            synchronized (REQUESTS) {
                Long previous = REQUESTS.get(player);
                if (previous != null && now - previous < refreshTicks() * 50_000_000L) return;
                REQUESTS.put(player, now);
            }
            Object server = call(call(player, "serverLevel"), "getServer");
            call(server, "execute", (Runnable) () -> sendSnapshot(player));
        } catch (Throwable failure) { report("request", failure); }
    }

    private static void sendSnapshot(Object player) {
        try {
            Snapshot snapshot = sample(player);
            for (int code : encode(snapshot)) stat("net.newworld.player.PlayerFieldSurvey0504Bridge", "sendResult", player, code);
        } catch (Throwable failure) { report("snapshot", failure); }
    }

    private static Snapshot sample(Object player) throws Exception {
        ServerView view = SERVER.computeIfAbsent(player, ignored -> new ServerView());
        Object level = call(player, "serverLevel");
        Object ship = stat("net.newworld.player.PlayerFieldSurveyRuntime", "findOwnedShip", player, level, call(player, "blockPosition"));
        if (ship == null) { view.reset(""); return Snapshot.unavailable("NO OWNED SHIP / LOADED INTERIOR REQUIRED"); }
        String id = String.valueOf(call(ship, "id"));
        if (!id.equals(view.ship)) view.reset(id);
        Object manager = call(ship, "manager");
        Object world = call(manager, "getWorld");
        if (world == null) return Snapshot.unavailable("SHIP INTERIOR UNAVAILABLE");
        long tick = num(call(world, "getGameTime"));
        long fe = readNumber("net.newworld.core.LongFEEnergySystem", "getEnergy", world, id);
        long cap = readNumber("net.newworld.core.LongFEEnergySystem", "getCapacity", world, id);
        long we = readNumber("net.newworld.core.WarpEnergySystem", "getEnergy", world);
        long weCap = readNumber("net.newworld.core.WarpEnergySystem", "capacityForShip", id);
        String rate = "SAMPLING";
        long consumed = 0;
        boolean meterKnown = false;
        try {
            Object pool = stat("net.newworld.core.LongFEEnergySystem", "data", world);
            consumed = OverviewEnergyMeter0670.total(pool, id);
            meterKnown = true;
        } catch (Exception failure) { view.problem("FE CONSUMPTION METER UNAVAILABLE"); }
        if (fe >= 0 && view.energy >= 0 && tick > view.tick && tick - view.tick <= staleTicks()) {
            double delta = ((double) fe - (double) view.energy) / (tick - view.tick);
            String used = meterKnown && view.meterKnown
                    ? amount(Math.round(OverviewEnergyMeter0670.perTick(view.consumed, consumed, tick - view.tick))) : "?";
            rate = "OUT " + used + " / NET " + (delta >= 0 ? "+" : "-") + amount(Math.round(Math.abs(delta))) + " FE/t";
        }
        view.energy = fe; view.tick = tick; view.consumed = consumed; view.meterKnown = meterKnown;
        String feMatrix = matrix(id, "FE"), warpMatrix = matrix(id, "WARP"), engineMatrix = matrix(id, "ENGINE");
        String handbrake = flag(manager, "isHandbrakeLocked", "LOCKED", "RELEASED");
        String shield = flag(manager, "isShieldsMiningEnabled", "ON", "OFF");
        String engine = "UNKNOWN";
        try {
            Object flight = call(manager, "getSystem", Class.forName("net.drgmes.dwm.common.tardis.systems.TardisSystemFlight"));
            if (Boolean.TRUE.equals(call(manager, "isBroken"))) engine = "BROKEN";
            else if (Boolean.TRUE.equals(call(flight, "inProgress"))) engine = "IN FLIGHT";
            else {
                long cooldown = Math.max(0, num(call(flight, "newWorld$getCooldownUntil")) - tick);
                engine = cooldown > 0 ? "COOLDOWN " + ((cooldown + 19) / 20) + "s"
                        : "ONLINE".equals(engineMatrix) ? "READY" : "MATRIX " + engineMatrix;
            }
        } catch (Exception failure) { view.problem("ENGINE TELEMETRY UNAVAILABLE"); }
        String mining = "UNKNOWN", navigation = "UNKNOWN";
        try {
            Object data = stat("net.newworld.mining.MiningRuntimeSavedData", "get", world);
            Object state = ((Map<?, ?>) field(data, "states")).get(id);
            mining = state == null ? "NO RECORD" : String.valueOf(field(state, "status"));
        } catch (Exception failure) { view.problem("MINING TELEMETRY UNAVAILABLE"); }
        try {
            Object data = stat("net.newworld.navigation.NavigationDiscoverySavedData", "get", world);
            Object state = ((Map<?, ?>) field(data, "ships")).get(id);
            String selected = state == null ? "" : String.valueOf(field(state, "selectedKey"));
            navigation = selected.isBlank() || selected.equals("null") ? "NO TARGET" : "TARGET SELECTED";
            Object plan = ((Map<?, ?>) staticField("net.newworld.navigation.Navigation0472ServerRoute", "PLANS")).get(id);
            if (plan != null) navigation = "ROUTE " + field(plan, "status");
        } catch (Exception failure) { view.problem("NAVIGATION TELEMETRY UNAVAILABLE"); }
        String dimension = "UNKNOWN", position = "POSITION UNKNOWN";
        try {
            dimension = String.valueOf(call(call(manager, "getCurrentExteriorDimension"), "location"));
            Object pos = call(manager, "getCurrentExteriorPosition");
            position = "X=" + call(pos, "getX") + " Y=" + call(pos, "getY") + " Z=" + call(pos, "getZ");
        } catch (Exception failure) { view.problem("EXTERIOR POSITION UNAVAILABLE"); }
        ArrayList<String> active = new ArrayList<>();
        int severity = Math.max(energySeverity(fe, cap), energySeverity(we, weCap));
        if (energySeverity(fe, cap) > 0) active.add(cap <= 0 || fe < 0 ? "FE TELEMETRY UNAVAILABLE" : "FE LEVEL LOW");
        if (energySeverity(we, weCap) > 0) active.add(weCap <= 0 || we < 0 ? "WARP TELEMETRY UNAVAILABLE" : "WARP LEVEL LOW");
        if ("BROKEN".equals(engine) || "NO_ENERGY".equals(mining)) severity = 2;
        for (String entry : List.of("FE MATRIX " + feMatrix, "WARP MATRIX " + warpMatrix, "ENGINE MATRIX " + engineMatrix)) {
            if (!entry.endsWith(" ONLINE")) active.add(entry);
        }
        if ("BROKEN".equals(engine) || "UNKNOWN".equals(engine)) active.add("ENGINE " + engine);
        if (mining.startsWith("WAITING_") && "ON".equals(shield) || mining.equals("NO_ENERGY") || mining.equals("BUFFER_FULL")) active.add("MINING " + mining);
        if (mining.equals("UNKNOWN") || navigation.equals("UNKNOWN") || dimension.equals("UNKNOWN") || handbrake.equals("UNKNOWN") || shield.equals("UNKNOWN")) active.add("PARTIAL TELEMETRY / CHECK LOG");
        if (!active.isEmpty()) severity = Math.max(1, severity);
        for (String warning : active) if (!view.active.contains(warning)) view.problem(warning);
        view.active = Set.copyOf(active);
        Snapshot result = new Snapshot(id, fe, cap, we, weCap, rate, engine, handbrake, shield,
                mining, navigation, feMatrix, warpMatrix, engineMatrix, dimension, position,
                severity == 2 ? "CRITICAL" : severity == 1 ? "WARNING" : "NOMINAL", List.copyOf(view.warnings));
        String statusKey = result.health + "/" + result.engine + "/" + result.mining + "/" + result.navigation;
        if (!statusKey.equals(view.logged)) {
            System.out.println("[NewWorld Overview] ship=" + id + " health=" + result.health + " FE=" + fe + "/" + cap
                    + " WE=" + we + "/" + weCap + " engine=" + engine + " brake=" + handbrake + " mining=" + mining + " navigation=" + navigation);
            view.logged = statusKey;
        }
        return result;
    }

    /** Unknown capacity is WARNING, never a false empty/critical battery. Uses double to avoid long overflow. */
    public static int energySeverity(long stored, long capacity) {
        if (stored < 0 || capacity <= 0) return 1;
        double percent = 100.0 * (double) stored / capacity;
        return percent <= criticalPercent() ? 2 : percent <= warningPercent() ? 1 : 0;
    }

    private static String matrix(String id, String type) {
        try {
            Object record = ((Map<?, ?>) staticField("net.newworld.core.ShipRoomRegistry", "STATS")).get(id + ':' + type);
            return record == null ? "UNKNOWN" : Boolean.TRUE.equals(call(record, "active")) ? "ONLINE" : "OFFLINE";
        } catch (Exception failure) { return "UNKNOWN"; }
    }

    public record Snapshot(String ship, long fe, long feCapacity, long warp, long warpCapacity,
            String rate, String engine, String brake, String shield, String mining, String navigation,
            String feMatrix, String warpMatrix, String engineMatrix, String dimension, String position,
            String health, List<String> warnings) {
        public Snapshot { warnings = List.copyOf(warnings); }
        public static Snapshot unavailable(String reason) {
            return new Snapshot("", -1, -1, -1, -1, "UNKNOWN", "UNKNOWN", "UNKNOWN", "UNKNOWN", "UNKNOWN", "UNKNOWN",
                    "UNKNOWN", "UNKNOWN", "UNKNOWN", "UNKNOWN", "POSITION UNKNOWN", "UNAVAILABLE", List.of(reason));
        }
    }

    public static boolean isWireCode(int code) { return code >= WIRE_BASE && code <= END; }
    public static int[] encode(Snapshot value) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bytes);
        out.writeInt(1);
        out.writeLong(value.fe); out.writeLong(value.feCapacity); out.writeLong(value.warp); out.writeLong(value.warpCapacity);
        for (String text : List.of(value.ship, value.rate, value.engine, value.brake, value.shield, value.mining, value.navigation,
                value.feMatrix, value.warpMatrix, value.engineMatrix, value.dimension, value.position, value.health)) out.writeUTF(clean(text));
        int count = Math.min(3, value.warnings.size());
        out.writeInt(count);
        for (int i = 0; i < count; i++) out.writeUTF(clean(value.warnings.get(i)));
        byte[] raw = bytes.toByteArray();
        if (raw.length > MAX_BYTES) throw new IOException("Overview snapshot too large");
        int[] result = new int[(raw.length + 2) / 3 + 2]; result[0] = BEGIN; result[result.length - 1] = END;
        for (int i = 0; i < raw.length; i += 3) {
            int bits = 0;
            for (int j = 0; j < 3; j++) bits = (bits << 8) | (i + j < raw.length ? raw[i + j] & 255 : 0);
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
                if (in.readInt() != 1) throw new IOException("Unknown Overview schema");
                long fe = in.readLong(), cap = in.readLong(), we = in.readLong(), weCap = in.readLong();
                String[] values = new String[13];
                for (int i = 0; i < values.length; i++) { values[i] = in.readUTF(); if (values[i].length() > 96) throw new IOException("Oversized field"); }
                int count = in.readInt(); if (count < 0 || count > 3) throw new IOException("Invalid warning count");
                List<String> warnings = new ArrayList<>();
                for (int i = 0; i < count; i++) { String warning = in.readUTF(); if (warning.length() > 96) throw new IOException("Oversized warning"); warnings.add(warning); }
                if (in.available() > 2) throw new IOException("Trailing snapshot data");
                while (in.available() > 0) if (in.readByte() != 0) throw new IOException("Invalid padding");
                client = new Snapshot(values[0], fe, cap, we, weCap, values[1], values[2], values[3], values[4], values[5], values[6],
                        values[7], values[8], values[9], values[10], values[11], values[12], warnings);
                receivedAt = System.nanoTime();
            } catch (IOException failure) { report("decode", failure); }
            incoming = null;
        } else if (incoming.size() + 3 <= MAX_BYTES + 2) {
            int bits = code - WIRE_BASE;
            incoming.write(bits >>> 16); incoming.write(bits >>> 8); incoming.write(bits);
        } else incoming = null;
        return true;
    }

    public static Snapshot clientSnapshot() { return client; }
    public static synchronized void resetClient() { client = null; receivedAt = 0; incoming = null; VIEWS.clear(); }

    public static void render(Object screen, Object graphics, int left, int top) {
        try {
            Object minecraft = stat("net.minecraft.client.Minecraft", "getInstance");
            Object connection = call(minecraft, "getConnection");
            if (connection != clientConnection) { resetClient(); clientConnection = connection; }
            ClientView view = VIEWS.computeIfAbsent(screen, ignored -> new ClientView());
            long now = System.nanoTime();
            if (connection != null && now - view.requested >= refreshTicks() * 50_000_000L) {
                view.requested = now;
                PlayerGeologicalSurveyGui0620.sendSurveyMode(4);
            }
            Snapshot s = client;
            boolean fresh = s != null && receivedAt >= view.opened && now - receivedAt <= staleTicks() * 50_000_000L;
            String health = fresh ? s.health : s == null || receivedAt < view.opened ? "SYNCING" : "STALE";
            draw(screen, graphics, left, top, s, fresh, health);
        } catch (Throwable failure) {
            if (!renderErrorLogged) { renderErrorLogged = true; report("render", failure); }
        }
    }

    /** Draw-only entry point also exercised by the headless geometry regression test. */
    public static void draw(Object screen, Object graphics, int left, int top, Snapshot s, boolean fresh, String health) throws Exception {
            int color = health.equals("NOMINAL") ? 0xFF64EAB5 : health.equals("CRITICAL") ? 0xFFFF7272 : 0xFFFFCF45;
            label(screen, graphics, "SHIP OVERVIEW // " + health, left + 26, top + 91, color, 480);
            if (!fresh || s.ship.isBlank()) {
                label(screen, graphics, !fresh ? "Waiting for fresh server telemetry..." : s.warnings.getFirst(), left + 26, top + 120, 0xFFD5E7EF, 476);
                label(screen, graphics, "Read-only view. No system controls or world changes.", left + 26, top + 142, 0xFF8DA7B4, 476);
                return;
            }
            int x = left + 26, right = left + 275;
            fill(graphics, x, top + 109, left + 265, top + 209, 0xFF112A35);
            fill(graphics, right, top + 109, left + 514, top + 209, 0xFF112A35);
            String[] energy = {"FE " + amount(s.fe) + " / " + amount(s.feCapacity), s.rate,
                    "WARP " + amount(s.warp) + " / " + amount(s.warpCapacity) + " WE",
                    "FE MATRIX " + s.feMatrix, "WARP MATRIX " + s.warpMatrix, "ENGINE MATRIX " + s.engineMatrix};
            String[] systems = {"ENGINE " + s.engine, "HANDBRAKE " + s.brake, "MINING SHIELD " + s.shield,
                    "MINING " + s.mining.replace('_', ' '), "NAV " + s.navigation, "SHIP " + s.ship};
            for (int i = 0; i < energy.length; i++) {
                label(screen, graphics, energy[i], x + 7, top + 116 + i * 15, i == 1 ? 0xFF8DA7B4 : 0xFFD5E7EF, 225);
                label(screen, graphics, systems[i], right + 7, top + 116 + i * 15, 0xFFD5E7EF, 225);
            }
            label(screen, graphics, "EXT " + s.dimension, x, top + 216, 0xFF64EAB5, 239);
            label(screen, graphics, s.position, right, top + 216, 0xFF64EAB5, 239);
            label(screen, graphics, "RECENT WARNINGS // THIS OBSERVATION SESSION", x, top + 233, 0xFF8DA7B4, 486);
            if (s.warnings.isEmpty()) label(screen, graphics, "No warnings observed.", x, top + 247, 0xFF8DA7B4, 486);
            else for (int i = 0; i < Math.min(warningRows(), s.warnings.size()); i++) label(screen, graphics, s.warnings.get(i), x, top + 247 + i * 12, 0xFFFFCF45, 486);
    }

    public static String amount(long value) {
        if (value < 0) return "?";
        if (value < 10_000) return Long.toString(value);
        String[] suffix = {"", "K", "M", "G", "T", "P", "E"}; double shown = value; int i = 0;
        while (shown >= 1000 && i < suffix.length - 1) { shown /= 1000; i++; }
        return String.format(Locale.ROOT, "%.2f%s", shown, suffix[i]);
    }

    private static void label(Object screen, Object graphics, String text, int x, int y, int color, int width) throws Exception {
        Object font = field(screen, "font");
        String shown = clean(text);
        if (((Number) call(font, "width", shown)).intValue() > width) {
            while (!shown.isEmpty() && ((Number) call(font, "width", shown + "...")).intValue() > width) shown = shown.substring(0, shown.length() - 1);
            shown += "...";
        }
        call(screen, "text", graphics, shown, x, y, color);
    }
    private static String clean(String s) { String t = Objects.toString(s, "UNKNOWN").replaceAll("[\\p{Cntrl}§]", " "); return t.substring(0, Math.min(96, t.length())); }
    private static void fill(Object g, int x, int y, int x2, int y2, int color) throws Exception { call(g, "fill", x, y, x2, y2, color); }
    private static String flag(Object target, String method, String yes, String no) { try { Object value = call(target, method); return value instanceof Boolean b ? b ? yes : no : "UNKNOWN"; } catch (Exception failure) { return "UNKNOWN"; } }
    private static long num(Object o) { return o instanceof Number n ? n.longValue() : -1; }
    private static long readNumber(String type, String method, Object... args) { try { return num(stat(type, method, args)); } catch (Exception failure) { return -1; } }
    private static Object call(Object target, String name, Object... args) throws Exception { return invoke(target.getClass(), target, name, args); }
    private static Object stat(String type, String name, Object... args) throws Exception { return invoke(Class.forName(type), null, name, args); }
    private static Object invoke(Class<?> type, Object target, String name, Object[] args) throws Exception {
        for (Class<?> current = type; current != null; current = current.getSuperclass()) for (Method m : current.getDeclaredMethods()) {
            if (!m.getName().equals(name) || m.getParameterCount() != args.length || Modifier.isStatic(m.getModifiers()) != (target == null)) continue;
            boolean compatible = true;
            for (int i = 0; i < args.length; i++) {
                Class<?> p = m.getParameterTypes()[i];
                if (p == int.class) p = Integer.class;
                if (p == long.class) p = Long.class;
                if (p == boolean.class) p = Boolean.class;
                if (args[i] != null && !p.isInstance(args[i]) || args[i] == null && p.isPrimitive()) compatible = false;
            }
            if (compatible) { m.setAccessible(true); return m.invoke(target, args); }
        }
        throw new NoSuchMethodException(type.getName() + '.' + name);
    }
    private static Object field(Object target, String name) throws Exception { return getField(target.getClass(), name).get(target); }
    private static Object staticField(String type, String name) throws Exception { return getField(Class.forName(type), name).get(null); }
    private static Field getField(Class<?> type, String name) throws Exception {
        for (Class<?> c = type; c != null; c = c.getSuperclass()) try { Field f = c.getDeclaredField(name); f.setAccessible(true); return f; } catch (NoSuchFieldException ignored) {}
        throw new NoSuchFieldException(type.getName() + '.' + name);
    }
    private static void report(String phase, Throwable failure) { System.err.println("[NewWorld Overview] " + phase + " failed: " + failure); }
    private static final class ClientView { long requested; final long opened = System.nanoTime(); }
    private static final class ServerView {
        String ship = "", logged = ""; long energy = -1, tick, consumed; boolean meterKnown; Set<String> active = Set.of();
        final ArrayList<String> warnings = new ArrayList<>();
        void reset(String id) { ship = id; logged = ""; energy = -1; tick = 0; consumed = 0; meterKnown = false; active = Set.of(); warnings.clear(); }
        void problem(String warning) { warnings.remove(warning); warnings.addFirst(warning); while (warnings.size() > 3) warnings.removeLast(); }
    }
}
