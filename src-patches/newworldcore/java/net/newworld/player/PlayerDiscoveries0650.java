package net.newworld.player;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import net.newworld.config.NewWorldTuning;

/** Server snapshot, compact int protocol, and first functional Player Discoveries view. */
public final class PlayerDiscoveries0650 {
    public static final int SNAPSHOT_BEGIN_BASE = 1_440_000_000;
    public static final int SNAPSHOT_BEGIN_MAX = SNAPSHOT_BEGIN_BASE + 512;
    public static final int RECORD_BEGIN = 1_440_001_000;
    public static final int RECORD_END = 1_440_001_001;
    public static final int SNAPSHOT_END = 1_440_001_002;
    public static final int FIELD_TOTAL = 1_440_001_010;
    public static final int FIELD_LABEL = 1_440_001_011;
    public static final int FIELD_KIND = 1_440_001_012;
    public static final int FIELD_DIMENSION = 1_440_001_013;
    public static final int FIELD_SOURCE = 1_440_001_014;
    public static final int FIELD_PRIMARY = 1_440_001_015;
    public static final int FIELD_X = 1_440_001_020;
    public static final int FIELD_Y = 1_440_001_021;
    public static final int FIELD_Z = 1_440_001_022;
    public static final int FIELD_DISTANCE = 1_440_001_023;
    public static final int FIELD_RESERVE = 1_440_001_024;
    public static final int FIELD_ANALYSIS = 1_440_001_025;
    public static final int FIELD_LAST_SEEN = 1_440_001_026;
    public static final int FIELD_FLAGS = 1_440_001_027;
    // Negative ranges pass through the legacy bridge; positive values >=100 are client status codes.
    public static final int ACTION_TARGET_BASE = -10_000;
    public static final int ACTION_ROUTE_BASE = -11_000;
    public static final int ACTION_FAVORITE_BASE = -12_000;
    public static final int ACTION_LIMIT = 512;
    public static final int STATUS_TARGET_OK = 1_440_002_010;
    public static final int STATUS_ROUTE_OK = 1_440_002_011;
    public static final int STATUS_FAVORITE_ON = 1_440_002_012;
    public static final int STATUS_FAVORITE_OFF = 1_440_002_013;
    public static final int STATUS_ACTION_FAILED = 1_440_002_014;
    public static final int STATUS_ACTION_DISABLED = 1_440_002_015;

    private static final List<DiscoveryView> CLIENT = new ArrayList<>();
    private static final Map<Object, ViewState> VIEWS = java.util.Collections.synchronizedMap(new WeakHashMap<>());
    private static final Map<Object, List<String>> SERVER_KEYS = java.util.Collections.synchronizedMap(new WeakHashMap<>());
    private static boolean receiving;
    private static int expected;
    private static int total;
    private static Builder builder;
    private static int numericField = -1;
    private static int stringField = -1;
    private static int stringRemaining = -1;
    private static ByteArrayOutputStream stringBytes;
    private static boolean renderFailureReported;
    private static String actionStatus = "";

    private PlayerDiscoveries0650() {}

    /** Routes every payload value through the discovery decoder before legacy range routing. */
    public static void handlePayload(Object payload, Object context) {
        try {
            Object raw = call(payload, "mode");
            int code = raw instanceof Number number ? number.intValue() : 0;
            if (accept(code)) return;
            invokeStatic("net.newworld.player.PlayerFieldSurvey0504Bridge", "handle0650Base", payload, context);
        } catch (Throwable failure) {
            System.err.println("[NewWorldCore] Player Discoveries payload routing failed: " + failure);
            failure.printStackTrace(System.err);
        }
    }

    /** Server side: sends the newest configurable slice from the ship's shared database. */
    public static void sendSnapshot(Object player) {
        try {
            Object level = call(player, "serverLevel");
            Object pos = call(player, "blockPosition");
            Object ship = invokePrivateStatic("net.newworld.player.PlayerFieldSurveyRuntime", "findOwnedShip",
                    player, level, pos);
            if (ship == null) {
                SERVER_KEYS.put(player, List.of());
                send(player, SNAPSHOT_BEGIN_BASE);
                send(player, FIELD_TOTAL); send(player, 0);
                send(player, SNAPSHOT_END);
                return;
            }

            String shipId = String.valueOf(call(ship, "id"));
            Object data = invokeStatic("net.newworld.navigation.NavigationDiscoverySavedData", "get", level);
            Object state = call(data, "state", shipId);
            Object raw = field(state, "discoveries");
            ArrayList<Object> records = new ArrayList<>();
            if (raw instanceof Map<?, ?> map) {
                for (Object record : map.values()) if (record != null) records.add(record);
            }
            records.sort(Comparator.comparingLong(PlayerDiscoveries0650::lastSeen).reversed());
            int totalRecords = records.size();
            int perCategoryLimit = NewWorldTuning.playerDiscoveriesSyncLimit();
            ArrayList<Object> snapshot = balancedSnapshot(records, perCategoryLimit);
            ArrayList<String> keys = new ArrayList<>(snapshot.size());
            for (Object record : snapshot) keys.add(String.valueOf(call(record, "key")));
            SERVER_KEYS.put(player, List.copyOf(keys));
            int synced = snapshot.size();
            send(player, SNAPSHOT_BEGIN_BASE + synced);
            send(player, FIELD_TOTAL); send(player, totalRecords);
            for (Object record : snapshot) sendRecord(player, record);
            send(player, SNAPSHOT_END);
            System.out.println("[NewWorld Player Discoveries] synced=" + synced + " total=" + totalRecords
                    + " perCategory=" + perCategoryLimit + " ship=" + shipId);
        } catch (Throwable failure) {
            System.err.println("[NewWorldCore] Player Discoveries snapshot failed: " + failure);
            SERVER_KEYS.put(player, List.of());
            try {
                send(player, SNAPSHOT_BEGIN_BASE);
                send(player, FIELD_TOTAL); send(player, 0);
                send(player, SNAPSHOT_END);
            } catch (Throwable ignored) {}
        }
    }

    /** Keeps recent Structure and Geology history independently so one scanner cannot hide the other. */
    private static ArrayList<Object> balancedSnapshot(List<Object> records, int perCategoryLimit) {
        ArrayList<Object> snapshot = new ArrayList<>();
        int structures = 0;
        int geology = 0;
        int other = 0;
        for (Object record : records) {
            String kind;
            try { kind = stringField(record, "kind"); }
            catch (Throwable ignored) { kind = ""; }
            if ("STRUCTURE".equalsIgnoreCase(kind)) {
                if (structures >= perCategoryLimit) continue;
                structures++;
            } else if ("GEOLOGY".equalsIgnoreCase(kind)) {
                if (geology >= perCategoryLimit) continue;
                geology++;
            } else {
                if (other >= perCategoryLimit) continue;
                other++;
            }
            snapshot.add(record);
        }
        snapshot.sort(Comparator.comparingLong(PlayerDiscoveries0650::lastSeen).reversed());
        return snapshot;
    }

    private static void sendRecord(Object player, Object record) throws Exception {
        int analysis = intField(record, "analysisLevel");
        String kind = stringField(record, "kind");
        String exactLabel = stringField(record, "label");
        String exactPrimary = stringField(record, "primaryResource");
        String label = "GEOLOGY".equalsIgnoreCase(kind)
                ? NewWorldTuning.geologyAnalysisLabel(analysis, exactLabel) : exactLabel;
        String primary = "GEOLOGY".equalsIgnoreCase(kind)
                ? NewWorldTuning.geologyAnalysisPrimary(analysis, exactPrimary) : exactPrimary;

        send(player, RECORD_BEGIN);
        sendString(player, FIELD_LABEL, label);
        sendString(player, FIELD_KIND, kind);
        sendString(player, FIELD_DIMENSION, stringField(record, "dimension"));
        sendString(player, FIELD_SOURCE, stringField(record, "source"));
        sendString(player, FIELD_PRIMARY, primary);
        sendInt(player, FIELD_X, intField(record, "x"));
        sendInt(player, FIELD_Y, intField(record, "y"));
        sendInt(player, FIELD_Z, intField(record, "z"));
        sendInt(player, FIELD_DISTANCE, intField(record, "distance"));
        sendInt(player, FIELD_RESERVE, intField(record, "reserve"));
        sendInt(player, FIELD_ANALYSIS, analysis);
        sendInt(player, FIELD_LAST_SEEN, saturatingInt(lastSeen(record)));
        int flags = (booleanField(record, "favorite") ? 1 : 0) | (booleanField(record, "visited") ? 2 : 0);
        sendInt(player, FIELD_FLAGS, flags);
        send(player, RECORD_END);
    }

    private static void sendString(Object player, int field, String value) {
        byte[] bytes = clean(value, 80).getBytes(StandardCharsets.UTF_8);
        send(player, field);
        send(player, bytes.length);
        for (int offset = 0; offset < bytes.length; offset += 4) {
            int packed = 0;
            for (int slot = 0; slot < 4 && offset + slot < bytes.length; slot++) {
                packed |= (bytes[offset + slot] & 0xff) << (slot * 8);
            }
            send(player, packed);
        }
    }

    private static void sendInt(Object player, int field, int value) {
        send(player, field);
        send(player, value);
    }

    private static void send(Object player, int code) {
        PlayerFieldSurvey0504Bridge.sendResult(player, code);
    }

    /** Client side: consumes only the reserved snapshot stream. */
    public static synchronized boolean accept(int code) {
        if (acceptActionStatus(code)) return true;
        if (code >= SNAPSHOT_BEGIN_BASE && code <= SNAPSHOT_BEGIN_MAX) {
            CLIENT.clear();
            expected = code - SNAPSHOT_BEGIN_BASE;
            total = expected;
            receiving = true;
            builder = null;
            clearPending();
            return true;
        }
        if (!receiving) return false;

        if (stringField >= 0) {
            if (stringRemaining < 0) {
                stringRemaining = Math.max(0, Math.min(1024, code));
                stringBytes = new ByteArrayOutputStream(stringRemaining);
                if (stringRemaining == 0) finishString();
            } else {
                for (int slot = 0; slot < 4 && stringRemaining > 0; slot++) {
                    stringBytes.write((code >>> (slot * 8)) & 0xff);
                    stringRemaining--;
                }
                if (stringRemaining == 0) finishString();
            }
            return true;
        }
        if (numericField >= 0) {
            applyNumber(numericField, code);
            numericField = -1;
            return true;
        }

        if (code == RECORD_BEGIN) { builder = new Builder(); return true; }
        if (code == RECORD_END) {
            if (builder != null) CLIENT.add(builder.build());
            builder = null;
            return true;
        }
        if (code == SNAPSHOT_END) {
            receiving = false;
            builder = null;
            clearPending();
            return true;
        }
        if (isStringField(code)) { stringField = code; stringRemaining = -1; return true; }
        if (isNumericField(code)) { numericField = code; return true; }
        return true;
    }

    private static boolean isStringField(int code) {
        return code >= FIELD_LABEL && code <= FIELD_PRIMARY;
    }

    private static boolean isNumericField(int code) {
        return code == FIELD_TOTAL || (code >= FIELD_X && code <= FIELD_FLAGS);
    }

    private static void finishString() {
        String value = new String(stringBytes.toByteArray(), StandardCharsets.UTF_8);
        if (builder != null) {
            switch (stringField) {
                case FIELD_LABEL -> builder.label = value;
                case FIELD_KIND -> builder.kind = value;
                case FIELD_DIMENSION -> builder.dimension = value;
                case FIELD_SOURCE -> builder.source = value;
                case FIELD_PRIMARY -> builder.primary = value;
                default -> {}
            }
        }
        stringField = -1;
        stringRemaining = -1;
        stringBytes = null;
    }

    private static void applyNumber(int field, int value) {
        if (field == FIELD_TOTAL) { total = Math.max(0, value); return; }
        if (builder == null) return;
        switch (field) {
            case FIELD_X -> builder.x = value;
            case FIELD_Y -> builder.y = value;
            case FIELD_Z -> builder.z = value;
            case FIELD_DISTANCE -> builder.distance = value;
            case FIELD_RESERVE -> builder.reserve = value;
            case FIELD_ANALYSIS -> builder.analysis = value;
            case FIELD_LAST_SEEN -> builder.lastSeen = value;
            case FIELD_FLAGS -> builder.flags = value;
            default -> {}
        }
    }

    private static void clearPending() {
        numericField = -1;
        stringField = -1;
        stringRemaining = -1;
        stringBytes = null;
    }

    public static synchronized List<DiscoveryView> clientEntries() { return List.copyOf(CLIENT); }
    public static synchronized int clientExpected() { return expected; }
    public static synchronized int clientTotal() { return total; }
    public static synchronized boolean clientReceiving() { return receiving; }

    private static boolean acceptActionStatus(int code) {
        String status = switch (code) {
            case STATUS_TARGET_OK -> "TARGET SET";
            case STATUS_ROUTE_OK -> "ROUTE READY";
            case STATUS_FAVORITE_ON -> "FAVORITE ADDED";
            case STATUS_FAVORITE_OFF -> "FAVORITE REMOVED";
            case STATUS_ACTION_FAILED -> "ACTION FAILED";
            case STATUS_ACTION_DISABLED -> "ACTION DISABLED";
            default -> null;
        };
        if (status == null) return false;
        actionStatus = status;
        return true;
    }

    /** Server side: applies a Discoveries action to the exact record sent in the last snapshot. */
    public static void handleAction(Object player, int mode) {
        int action;
        int index;
        if (inActionRange(mode, ACTION_TARGET_BASE)) {
            action = 0; index = ACTION_TARGET_BASE - mode;
        } else if (inActionRange(mode, ACTION_ROUTE_BASE)) {
            action = 1; index = ACTION_ROUTE_BASE - mode;
        } else if (inActionRange(mode, ACTION_FAVORITE_BASE)) {
            action = 2; index = ACTION_FAVORITE_BASE - mode;
        } else {
            send(player, STATUS_ACTION_FAILED);
            return;
        }

        try {
            if (action == 0 && !NewWorldTuning.playerDiscoveriesTargetEnabled()
                    || action == 1 && !NewWorldTuning.playerDiscoveriesRouteEnabled()
                    || action == 2 && !NewWorldTuning.playerDiscoveriesFavoriteEnabled()) {
                send(player, STATUS_ACTION_DISABLED);
                return;
            }
            ServerSelection selection = resolveServerSelection(player, index);
            if (selection == null) {
                send(player, STATUS_ACTION_FAILED);
                return;
            }
            if (action == 0) {
                selectTarget(selection);
                send(player, STATUS_TARGET_OK);
                System.out.println("[NewWorld Player Discoveries] target=" + selection.key + " ship=" + selection.shipId);
            } else if (action == 1) {
                selectTarget(selection);
                RouteContext context = new RouteContext(selection.level);
                invokeStatic("net.newworld.navigation.Navigation0472ServerRoute", "calculate", context, player);
                boolean ready = routeReady(selection.shipId);
                send(player, ready ? STATUS_ROUTE_OK : STATUS_ACTION_FAILED);
                System.out.println("[NewWorld Player Discoveries] route=" + selection.key + " ready=" + ready
                        + " ship=" + selection.shipId);
            } else {
                boolean favorite = !booleanField(selection.record, "favorite");
                setField(selection.record, "favorite", favorite);
                call(selection.data, "setDirty");
                invokeStatic("net.newworld.navigation.NavigationFavoriteOps", "invalidate",
                        selection.data, selection.shipId);
                send(player, favorite ? STATUS_FAVORITE_ON : STATUS_FAVORITE_OFF);
                sendSnapshot(player);
                System.out.println("[NewWorld Player Discoveries] favorite=" + favorite + " key=" + selection.key
                        + " ship=" + selection.shipId);
            }
        } catch (Throwable failure) {
            System.err.println("[NewWorldCore] Player Discoveries action failed: " + failure);
            failure.printStackTrace(System.err);
            send(player, STATUS_ACTION_FAILED);
        }
    }

    public static boolean isActionMode(int mode) {
        return inActionRange(mode, ACTION_TARGET_BASE)
                || inActionRange(mode, ACTION_ROUTE_BASE)
                || inActionRange(mode, ACTION_FAVORITE_BASE);
    }

    private static boolean inActionRange(int mode, int base) {
        return mode <= base && mode > base - ACTION_LIMIT;
    }

    private static ServerSelection resolveServerSelection(Object player, int index) throws Exception {
        List<String> keys = SERVER_KEYS.get(player);
        if (keys == null || index < 0 || index >= keys.size()) return null;
        String key = keys.get(index);
        Object level = call(player, "serverLevel");
        Object pos = call(player, "blockPosition");
        Object ship = invokePrivateStatic("net.newworld.player.PlayerFieldSurveyRuntime", "findOwnedShip",
                player, level, pos);
        if (ship == null) return null;
        String shipId = String.valueOf(call(ship, "id"));
        Object data = invokeStatic("net.newworld.navigation.NavigationDiscoverySavedData", "get", level);
        Object state = call(data, "state", shipId);
        Object raw = field(state, "discoveries");
        if (!(raw instanceof Map<?, ?> map)) return null;
        Object record = map.get(key);
        return record == null ? null : new ServerSelection(level, data, state, shipId, key, record);
    }

    private static void selectTarget(ServerSelection selection) throws Exception {
        setField(selection.state, "selectedKey", selection.key);
        call(selection.data, "setDirty");
    }

    private static boolean routeReady(String shipId) {
        try {
            Object raw = staticField("net.newworld.navigation.Navigation0472ServerRoute", "PLANS");
            Object plan = raw instanceof Map<?, ?> plans ? plans.get(shipId) : null;
            String status = plan == null ? "" : stringField(plan, "status");
            return !status.isBlank() && !status.startsWith("ERROR") && !status.startsWith("NO_");
        } catch (Throwable ignored) {
            return false;
        }
    }

    /** Wrapper target for PlayerShipScreen.survey; non-Discoveries tabs retain the base method. */
    public static void renderContent(Object screen, Object graphics, int left, int top, int mouseX, int mouseY) {
        try {
            if (intField(screen, "tab") != 2) {
                call(screen, "survey0650Base", graphics, left, top, mouseX, mouseY);
                return;
            }
            renderDiscoveries(screen, graphics, left, top);
        } catch (Throwable failure) {
            if (!renderFailureReported) {
                renderFailureReported = true;
                System.err.println("[NewWorldCore] Player Discoveries render failed: " + failure);
            }
        }
    }

    private static void renderDiscoveries(Object screen, Object graphics, int left, int top) throws Exception {
        ViewState view = VIEWS.computeIfAbsent(screen, ignored -> new ViewState());
        List<DiscoveryView> filtered = filtered(view.filter);
        int rows = NewWorldTuning.playerDiscoveriesRows();
        int pages = Math.max(1, (filtered.size() + rows - 1) / rows);
        view.page = Math.max(0, Math.min(view.page, pages - 1));
        int start = view.page * rows;

        text(screen, graphics, "DISCOVERIES", left + 26, top + 92, 0xff7ff7ff);
        text(screen, graphics, "SHARED SHIP DATABASE // NEWEST RECORDS", left + 26, top + 107, 0xffb5c8d8);
        button(screen, graphics, left + 26, top + 124, 64, "ALL", view.filter == 0);
        button(screen, graphics, left + 94, top + 124, 100, "STRUCTURES", view.filter == 1);
        button(screen, graphics, left + 198, top + 124, 80, "GEOLOGY", view.filter == 2);

        int listX = left + 26;
        int listY = top + 150;
        int listW = 296;
        for (int row = 0; row < rows; row++) {
            int index = start + row;
            int y = listY + row * 18;
            fill(graphics, listX, y, listX + listW, y + 16,
                    index == view.selected ? 0xff173a4b : (row % 2 == 0 ? 0xff102733 : 0xff0d222d));
            if (index >= filtered.size()) continue;
            DiscoveryView item = filtered.get(index);
            text(screen, graphics, fit(item.label, 26), listX + 6, y + 4,
                    item.visited() ? 0xff80ffc2 : 0xffdceaf3);
            text(screen, graphics, "L" + item.analysis, listX + 236, y + 4, analysisColor(item.analysis));
            text(screen, graphics, item.favorite() ? "*" : shortKind(item.kind), listX + 268, y + 4,
                    item.favorite() ? 0xffffcc3d : 0xff7f9aaa);
        }

        int detailX = left + 334;
        fill(graphics, detailX, top + 124, left + 514, top + 276, 0xff102733);
        DiscoveryView selected = filtered.isEmpty() ? null
                : filtered.get(Math.max(0, Math.min(view.selected, filtered.size() - 1)));
        if (selected == null) {
            text(screen, graphics, clientReceiving() ? "SYNCING..." : "NO DISCOVERIES", detailX + 10, top + 138, 0xff8fa8b8);
        } else {
            text(screen, graphics, fit(selected.label, 24), detailX + 10, top + 134, 0xff80ffc2);
            text(screen, graphics, fit(selected.kind + " // " + selected.source, 25), detailX + 10, top + 149, 0xff8fa8b8);
            text(screen, graphics, "ANALYSIS L" + selected.analysis + "/3", detailX + 10, top + 164, analysisColor(selected.analysis));
            text(screen, graphics, fit(selected.primary, 24), detailX + 10, top + 179, 0xffdceaf3);
            text(screen, graphics, reserveLabel(selected), detailX + 10, top + 194, 0xffdceaf3);
            text(screen, graphics, clientLastSeenLabel(selected), detailX + 10, top + 209, 0xff8fa8b8);
            text(screen, graphics, "X=" + selected.x + " Y=" + selected.y, detailX + 10, top + 224, 0xffb5c8d8);
            text(screen, graphics, "Z=" + selected.z + "  " + shortDim(selected.dimension), detailX + 10, top + 237, 0xffb5c8d8);
            text(screen, graphics, playerProximity(selected), detailX + 10, top + 249, 0xffffcc3d);
            actionButton(screen, graphics, detailX + 4, top + 258, 52, "TARGET",
                    NewWorldTuning.playerDiscoveriesTargetEnabled(), false);
            actionButton(screen, graphics, detailX + 60, top + 258, 54, "ROUTE",
                    NewWorldTuning.playerDiscoveriesRouteEnabled(), false);
            actionButton(screen, graphics, detailX + 118, top + 258, 58,
                    selected.favorite() ? "* FAV" : "FAV",
                    NewWorldTuning.playerDiscoveriesFavoriteEnabled(), selected.favorite());
        }

        button(screen, graphics, left + 26, top + 260, 64, "< PREV", false);
        button(screen, graphics, left + 94, top + 260, 64, "NEXT >", false);
        text(screen, graphics, (view.page + 1) + "/" + pages, left + 168, top + 266, 0xff8fa8b8);
        String sync = clientReceiving() ? "SYNCING" : actionStatus.isBlank()
                ? "SYNC " + clientEntries().size() + "/" + clientTotal() : actionStatus;
        text(screen, graphics, sync, left + 236, top + 266, 0xff80ffc2);
    }

    /** Called before the legacy click handler. */
    public static boolean mouseClicked(Object screen, double mouseX, double mouseY, int button) {
        try {
            int left = (intField(screen, "width") - 540) / 2;
            int top = (intField(screen, "height") - 300) / 2;
            int discoveriesX = left + 182;
            if (inside(mouseX, mouseY, discoveriesX, top + 46, 88, 22)) {
                setField(screen, "tab", 2);
                setField(screen, "status", "SYNCING // Discoveries...");
                VIEWS.put(screen, new ViewState());
                PlayerGeologicalSurveyGui0620.sendSurveyMode(3);
                return true;
            }
            if (intField(screen, "tab") != 2) return false;
            ViewState view = VIEWS.computeIfAbsent(screen, ignored -> new ViewState());
            if (inside(mouseX, mouseY, left + 26, top + 124, 64, 18)) { view.filter = 0; resetView(view); return true; }
            if (inside(mouseX, mouseY, left + 94, top + 124, 100, 18)) { view.filter = 1; resetView(view); return true; }
            if (inside(mouseX, mouseY, left + 198, top + 124, 80, 18)) { view.filter = 2; resetView(view); return true; }

            List<DiscoveryView> filtered = filtered(view.filter);
            int rows = NewWorldTuning.playerDiscoveriesRows();
            for (int row = 0; row < rows; row++) {
                if (inside(mouseX, mouseY, left + 26, top + 150 + row * 18, 296, 16)) {
                    int index = view.page * rows + row;
                    if (index < filtered.size()) view.selected = index;
                    return true;
                }
            }
            int pages = Math.max(1, (filtered.size() + rows - 1) / rows);
            if (inside(mouseX, mouseY, left + 26, top + 260, 64, 18)) {
                view.page = Math.max(0, view.page - 1); view.selected = view.page * rows; return true;
            }
            if (inside(mouseX, mouseY, left + 94, top + 260, 64, 18)) {
                view.page = Math.min(pages - 1, view.page + 1); view.selected = view.page * rows; return true;
            }
            DiscoveryView selected = filtered.isEmpty() ? null
                    : filtered.get(Math.max(0, Math.min(view.selected, filtered.size() - 1)));
            if (selected != null && inside(mouseX, mouseY, left + 338, top + 258, 52, 18)) {
                actionStatus = "SETTING TARGET...";
                PlayerGeologicalSurveyGui0620.sendSurveyMode(ACTION_TARGET_BASE - selected.snapshotIndex);
                return true;
            }
            if (selected != null && inside(mouseX, mouseY, left + 394, top + 258, 54, 18)) {
                actionStatus = "BUILDING ROUTE...";
                PlayerGeologicalSurveyGui0620.sendSurveyMode(ACTION_ROUTE_BASE - selected.snapshotIndex);
                return true;
            }
            if (selected != null && inside(mouseX, mouseY, left + 452, top + 258, 58, 18)) {
                actionStatus = "UPDATING FAVORITE...";
                PlayerGeologicalSurveyGui0620.sendSurveyMode(ACTION_FAVORITE_BASE - selected.snapshotIndex);
                return true;
            }
            return false;
        } catch (Throwable failure) {
            System.err.println("[NewWorldCore] Player Discoveries click failed: " + failure);
            return true;
        }
    }

    private static void resetView(ViewState view) { view.page = 0; view.selected = 0; }

    private static synchronized List<DiscoveryView> filtered(int filter) {
        ArrayList<DiscoveryView> filtered = new ArrayList<>();
        for (DiscoveryView entry : CLIENT) {
            if (filter == 1 && !"STRUCTURE".equalsIgnoreCase(entry.kind)) continue;
            if (filter == 2 && !"GEOLOGY".equalsIgnoreCase(entry.kind)) continue;
            filtered.add(entry);
        }
        return filtered;
    }

    private static void button(Object screen, Object graphics, int x, int y, int width, String label, boolean active) throws Exception {
        fill(graphics, x, y, x + width, y + 18, active ? 0xff16485b : 0xff102f3d);
        if (active) fill(graphics, x, y + 16, x + width, y + 18, 0xff20d8ff);
        text(screen, graphics, label, x + 7, y + 5, active ? 0xff7ff7ff : 0xff9fb4c1);
    }

    private static void actionButton(Object screen, Object graphics, int x, int y, int width, String label,
                                     boolean enabled, boolean selected) throws Exception {
        int background = !enabled ? 0xff18242b : selected ? 0xff665410 : 0xff17465a;
        int accent = selected ? 0xffffcc3d : 0xff20d8ff;
        fill(graphics, x, y, x + width, y + 18, background);
        if (enabled) fill(graphics, x, y + 16, x + width, y + 18, accent);
        text(screen, graphics, label, x + 5, y + 5, enabled ? (selected ? 0xffffe58a : 0xff7ff7ff) : 0xff5f7079);
    }

    private static void text(Object screen, Object graphics, String value, int x, int y, int color) throws Exception {
        call(screen, "text", graphics, value, x, y, color);
    }

    private static void fill(Object graphics, int x1, int y1, int x2, int y2, int color) throws Exception {
        call(graphics, "fill", x1, y1, x2, y2, color);
    }

    private static boolean inside(double mx, double my, int x, int y, int width, int height) {
        return mx >= x && mx < x + width && my >= y && my < y + height;
    }

    private static int analysisColor(int analysis) {
        return switch (Math.max(0, Math.min(3, analysis))) {
            case 3 -> 0xff80ffc2;
            case 2 -> 0xffffcc3d;
            case 1 -> 0xff7ff7ff;
            default -> 0xff8fa8b8;
        };
    }

    private static String shortKind(String kind) {
        return "GEOLOGY".equalsIgnoreCase(kind) ? "GEO" : "STR";
    }

    private static String shortDim(String dimension) {
        if (dimension == null) return "unknown";
        int split = dimension.indexOf(':');
        return fit(split >= 0 ? dimension.substring(split + 1) : dimension, 18);
    }

    private static String playerProximity(DiscoveryView discovery) {
        try {
            Object minecraft = Class.forName("net.minecraft.client.Minecraft").getMethod("getInstance").invoke(null);
            Object player = field(minecraft, "player");
            if (player == null) return "PLAYER DIST UNKNOWN";
            Object level = call(player, "level");
            Object dimensionKey = call(level, "dimension");
            String dimension = String.valueOf(call(dimensionKey, "location"));
            Object pos = call(player, "blockPosition");
            return proximityLabel(discovery, dimension,
                    numberCall(pos, "getX"), numberCall(pos, "getY"), numberCall(pos, "getZ"));
        } catch (Throwable ignored) {
            return "PLAYER DIST UNKNOWN";
        }
    }

    private static String reserveLabel(DiscoveryView discovery) {
        return "GEOLOGY".equalsIgnoreCase(discovery.kind)
                ? "EST RESERVE " + Math.max(0, discovery.reserve) : "EST RESERVE N/A";
    }

    private static String clientLastSeenLabel(DiscoveryView discovery) {
        try {
            Object minecraft = Class.forName("net.minecraft.client.Minecraft").getMethod("getInstance").invoke(null);
            Object player = field(minecraft, "player");
            Object level = player == null ? null : call(player, "level");
            Object now = level == null ? null : call(level, "getGameTime");
            if (now instanceof Number number) return lastSeenLabel(discovery, number.longValue());
        } catch (Throwable ignored) {}
        return "LAST SEEN UNKNOWN";
    }

    public static String lastSeenLabel(DiscoveryView discovery, long now) {
        if (discovery == null || discovery.lastSeen <= 0) return "LAST SEEN UNKNOWN";
        long seconds = Math.max(0L, now - discovery.lastSeen) / 20L;
        if (seconds < 60L) return "LAST SEEN " + seconds + "s AGO";
        long minutes = seconds / 60L;
        if (minutes < 60L) return "LAST SEEN " + minutes + "m AGO";
        long hours = minutes / 60L;
        return "LAST SEEN " + hours + "h AGO";
    }

    public static String proximityLabel(DiscoveryView discovery, String playerDimension,
                                        int playerX, int playerY, int playerZ) {
        if (discovery == null) return "PLAYER DIST UNKNOWN";
        if (!sameDimension(discovery.dimension, playerDimension)) return "DIFFERENT DIMENSION";
        long dx = (long) discovery.x - playerX;
        long dy = (long) discovery.y - playerY;
        long dz = (long) discovery.z - playerZ;
        long distance = Math.round(Math.sqrt((double) dx * dx + (double) dy * dy + (double) dz * dz));
        return "PLAYER DIST " + distance + " BLOCKS";
    }

    private static boolean sameDimension(String discoveryDimension, String playerDimension) {
        String discovery = clean(discoveryDimension, 128);
        String player = clean(playerDimension, 128);
        if (discovery.equalsIgnoreCase(player)) return true;
        return discovery.indexOf(':') < 0 && discovery.equalsIgnoreCase(shortDim(player))
                || player.indexOf(':') < 0 && player.equalsIgnoreCase(shortDim(discovery));
    }

    private static String fit(String value, int max) {
        String safe = value == null ? "" : value;
        return safe.length() <= max ? safe : safe.substring(0, Math.max(1, max - 2)) + "..";
    }

    private static String clean(String value, int max) {
        String safe = value == null ? "" : value.replace('\n', ' ').replace('\r', ' ').trim();
        return safe.length() <= max ? safe : safe.substring(0, max);
    }

    private static long lastSeen(Object record) {
        try {
            long seen = longField(record, "lastSeenAt");
            return seen > 0 ? seen : longField(record, "discoveredAt");
        } catch (Throwable ignored) { return 0L; }
    }

    private static int saturatingInt(long value) {
        return value <= Integer.MIN_VALUE ? Integer.MIN_VALUE
                : (value >= Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) value);
    }

    private static Object invokeStatic(String owner, String name, Object... args) throws Exception {
        Method method = findMethod(Class.forName(owner), name, true, args);
        method.setAccessible(true);
        return method.invoke(null, args);
    }

    private static Object invokePrivateStatic(String owner, String name, Object... args) throws Exception {
        return invokeStatic(owner, name, args);
    }

    private static Object call(Object target, String name, Object... args) throws Exception {
        Method method = findMethod(target.getClass(), name, false, args);
        method.setAccessible(true);
        return method.invoke(target, args);
    }

    private static Method findMethod(Class<?> type, String name, boolean requireStatic, Object[] args) throws NoSuchMethodException {
        for (Class<?> current = type; current != null; current = current.getSuperclass()) {
            for (Method method : current.getDeclaredMethods()) {
                if (!method.getName().equals(name) || Modifier.isStatic(method.getModifiers()) != requireStatic
                        || method.getParameterCount() != args.length) continue;
                Class<?>[] parameters = method.getParameterTypes();
                boolean match = true;
                for (int i = 0; i < parameters.length; i++) {
                    if (!compatible(parameters[i], args[i])) { match = false; break; }
                }
                if (match) return method;
            }
        }
        throw new NoSuchMethodException(type.getName() + '.' + name);
    }

    private static boolean compatible(Class<?> parameter, Object value) {
        if (value == null) return !parameter.isPrimitive();
        if (parameter.isPrimitive()) parameter = wrap(parameter);
        return parameter.isInstance(value);
    }

    private static Class<?> wrap(Class<?> type) {
        if (type == int.class) return Integer.class;
        if (type == long.class) return Long.class;
        if (type == boolean.class) return Boolean.class;
        if (type == double.class) return Double.class;
        if (type == float.class) return Float.class;
        return type;
    }

    private static Field findField(Class<?> type, String name) throws NoSuchFieldException {
        for (Class<?> current = type; current != null; current = current.getSuperclass()) {
            try {
                Field field = current.getDeclaredField(name);
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException ignored) {}
        }
        throw new NoSuchFieldException(name);
    }

    private static Object field(Object target, String name) throws Exception { return findField(target.getClass(), name).get(target); }
    private static Object staticField(String owner, String name) throws Exception {
        return findField(Class.forName(owner), name).get(null);
    }
    private static void setField(Object target, String name, Object value) throws Exception { findField(target.getClass(), name).set(target, value); }
    private static String stringField(Object target, String name) throws Exception {
        Object value = field(target, name);
        return value == null ? "" : String.valueOf(value);
    }
    private static int intField(Object target, String name) throws Exception { return ((Number) field(target, name)).intValue(); }
    private static long longField(Object target, String name) throws Exception { return ((Number) field(target, name)).longValue(); }
    private static boolean booleanField(Object target, String name) throws Exception { return Boolean.TRUE.equals(field(target, name)); }
    private static int numberCall(Object target, String name) throws Exception { return ((Number) call(target, name)).intValue(); }

    public record DiscoveryView(int snapshotIndex, String label, String kind, String dimension, String source, String primary,
                                int x, int y, int z, int distance, int reserve, int analysis,
                                int lastSeen, int flags) {
        public boolean favorite() { return (flags & 1) != 0; }
        public boolean visited() { return (flags & 2) != 0; }
    }

    private static final class Builder {
        String label = "UNKNOWN";
        String kind = "STRUCTURE";
        String dimension = "unknown";
        String source = "RADAR";
        String primary = "";
        int x, y, z, distance, reserve, analysis, lastSeen, flags;
        DiscoveryView build() {
            return new DiscoveryView(CLIENT.size(), label, kind, dimension, source, primary,
                    x, y, z, distance, reserve, analysis, lastSeen, flags);
        }
    }

    private static final class ViewState {
        int filter;
        int page;
        int selected;
    }

    private record ServerSelection(Object level, Object data, Object state, String shipId, String key, Object record) {}

    /** Minimal screen-compatible context consumed reflectively by the existing route engine. */
    private static final class RouteContext {
        final Object level;
        final Object scanExterior = null;
        final Object scanOrigin = null;
        RouteContext(Object level) { this.level = level; }
    }
}
