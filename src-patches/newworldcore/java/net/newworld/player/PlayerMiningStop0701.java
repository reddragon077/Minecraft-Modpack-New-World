package net.newworld.player;

import java.security.SecureRandom;
import java.util.*;
import net.newworld.config.NewWorldConfig;
import static net.newworld.player.PlayerOverview0670.*;

/** Two-step, owner/ship/link-bound stop. The only world write is mining shield OFF. */
public final class PlayerMiningStop0701 {
    public static final int ARM = 10, STOPPED = 1_440_003_100, ALREADY = STOPPED + 1,
            DENIED = STOPPED + 2, EXPIRED = STOPPED + 3, WAIT = STOPPED + 4, FAILED = STOPPED + 5;
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final Gate SERVER = new Gate();
    private static final Map<Object, Long> REQUESTS = new WeakHashMap<>();
    private static int clientToken;
    private static long clientStarted, clientArmed;
    private static String clientShip = "", message = "";
    private static boolean waiting;
    private PlayerMiningStop0701() {}
    public static boolean enabled() { return NewWorldConfig.bool("player-mining", "stop.enabled", true); }
    public static int confirmTicks() { return NewWorldConfig.integer("player-mining", "stop.confirm_ticks", 100, 40, 200); }
    public static int cooldownTicks() { return NewWorldConfig.integer("player-mining", "stop.cooldown_ticks", 40, 20, 1200); }
    public static boolean tokenCode(int code) { return code <= -1_000_001 && code >= -2_000_000; }
    public static boolean isRequest(int code) { return code == ARM || tokenCode(code); }
    public static boolean isReply(int code) { return tokenCode(code) || code >= STOPPED && code <= FAILED; }
    public record Context(String ship, Object manager, boolean allowed) {}
    public record Observation(String ship, long time) {}
    public record Ticket(String ship, int token, long time) {}

    /** Server-thread state; separated so the real guard and mutation can be exercised without a world. */
    public static final class Gate {
        private final Map<Object, Observation> seen = new WeakHashMap<>();
        private final Map<Object, Ticket> pending = new WeakHashMap<>();
        private final Map<Object, Long> stopped = new WeakHashMap<>();
        public void observe(Object player, String ship, long now) {
            Observation old = seen.put(player, new Observation(ship, now));
            if (old != null && !old.ship.equals(ship)) pending.remove(player);
        }
        public int apply(Object player, int mode, Context context, long now) {
            Observation view = seen.get(player);
            if (!enabled() || !context.allowed || context.manager == null || context.ship.isBlank()
                    || view == null || !view.ship.equals(context.ship) || now < view.time
                    || now - view.time > PlayerMining0700.staleTicks() * 50_000_000L) {
                pending.remove(player); return DENIED;
            }
            Long last = stopped.get(player);
            if (last != null && now - last < cooldownTicks() * 50_000_000L) { pending.remove(player); return WAIT; }
            Ticket ticket = pending.get(player);
            if (mode == ARM) {
                if (ticket == null || !ticket.ship.equals(context.ship) || now - ticket.time > confirmTicks() * 50_000_000L) {
                    ticket = new Ticket(context.ship, -1_000_001 - RANDOM.nextInt(1_000_000), now);
                    pending.put(player, ticket);
                }
                return ticket.token;
            }
            pending.remove(player); // One attempt consumes the challenge, successful or not.
            if (!tokenCode(mode) || ticket == null || ticket.token != mode || !ticket.ship.equals(context.ship)
                    || now - ticket.time < 200_000_000L || now - ticket.time > confirmTicks() * 50_000_000L) return EXPIRED;
            try {
                boolean wasOn = Boolean.TRUE.equals(call(context.manager, "isShieldsMiningEnabled"));
                if (wasOn) call(context.manager, "setShieldsMiningState", false);
                if (Boolean.TRUE.equals(call(context.manager, "isShieldsMiningEnabled"))) return FAILED;
                stopped.put(player, now);
                return wasOn ? STOPPED : ALREADY;
            } catch (Exception e) { System.err.println("[NewWorld Mining Stop] failed: " + e); return FAILED; }
        }
    }
    public static void observe(Object player, String ship) { SERVER.observe(player, ship, System.nanoTime()); }
    public static void handle(Object player, int mode) {
        long now = System.nanoTime(); Long last = REQUESTS.get(player);
        if (last != null && now - last < 200_000_000L) return;
        REQUESTS.put(player, now);
        var link = PlayerShipLink0680.resolve(player);
        Context context = new Context("", null, false);
        try {
            if (link.ship() != null && link.snapshot().allowed()) {
                Object manager = link.ship().manager();
                context = new Context(link.ship().id(), manager, call(manager, "getWorld") != null);
            }
            int result = SERVER.apply(player, mode, context, now);
            stat("net.newworld.player.PlayerFieldSurvey0504Bridge", "sendResult", player, result);
            if (!tokenCode(result)) System.out.println("[NewWorld Mining Stop] ship=" + context.ship + " result=" + result);
        } catch (Exception e) { System.err.println("[NewWorld Mining Stop] dispatch failed: " + e); }
    }
    public static void resetClient() { clientToken = 0; clientStarted = clientArmed = 0; waiting = false; clientShip = ""; message = ""; }
    public static void accept(int code) {
        if (!isReply(code) || !PlayerMining0700.freshClient()) return;
        if (!clientShip.equals(PlayerMining0700.clientSnapshot().ship())) { resetClient(); return; }
        if (tokenCode(code)) {
            if (waiting && System.nanoTime() - clientStarted < 5_000_000_000L) {
                waiting = false; clientToken = code; clientArmed = System.nanoTime(); message = "Confirm: mining shield OFF only. No restart.";
            }
            return;
        }
        if (!waiting) return;
        clientToken = 0; waiting = false;
        message = switch (code) {
            case STOPPED -> "STOPPED / MINING SHIELD OFF";
            case ALREADY -> "ALREADY STOPPED / MINING SHIELD OFF";
            case DENIED -> "STOP DENIED / CHECK LINK AND SERVER CONFIG";
            case EXPIRED -> "CONFIRM EXPIRED / TRY AGAIN";
            case WAIT -> "STOP COOLDOWN / PLEASE WAIT";
            default -> "STOP FAILED / USE SHIP TERMINAL";
        };
    }
    public static String button() {
        long now = System.nanoTime();
        if (waiting && now - clientStarted > 5_000_000_000L) { waiting = false; message = "NO RESPONSE / CHECK SHIP TERMINAL"; }
        if (clientToken != 0 && now - clientArmed > 2_000_000_000L) { clientToken = 0; message = "CONFIRM EXPIRED / TRY AGAIN"; }
        return waiting ? "WAITING..." : clientToken != 0 ? "CONFIRM STOP MINING" : "STOP MINING";
    }
    public static String message() { return message; }
    public static void click() {
        if (!PlayerMining0700.freshClient() || !PlayerMining0700.clientSnapshot().stopAllowed()) { resetClient(); return; }
        button(); if (waiting) return;
        long now = System.nanoTime();
        if (clientToken != 0 && now - clientArmed < 250_000_000L) return;
        int mode = clientToken == 0 ? ARM : clientToken;
        clientShip = PlayerMining0700.clientSnapshot().ship(); clientToken = 0;
        waiting = true; clientStarted = now; message = "Waiting for server acknowledgement...";
        try { PlayerGeologicalSurveyGui0620.sendSurveyMode(mode); }
        catch (Exception e) { waiting = false; message = "SEND FAILED / USE SHIP TERMINAL"; }
    }
}
