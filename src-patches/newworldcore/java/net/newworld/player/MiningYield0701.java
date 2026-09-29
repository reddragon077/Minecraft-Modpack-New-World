package net.newworld.player;

import java.util.*;
import static net.newworld.player.PlayerOverview0670.*;

/** Per-area successful resource-block counts; optional namespaced addition to existing phase NBT. */
public final class MiningYield0701 {
    public static final String KEY = "NewWorldMiningYieldV1";
    private static final int MAX_TYPES = 4096;
    private static final Map<Object, Ledger> LEDGERS = Collections.synchronizedMap(new WeakHashMap<>());
    private static boolean errorReported;
    public record Ledger(long baseline, Map<String, Long> counts) {}
    private MiningYield0701() {}
    public static void reset(Object phase) { LEDGERS.remove(phase); }
    public static boolean validId(String id) { return id != null && id.length() <= 256 && id.matches("[a-z0-9_.-]+:[a-z0-9_./-]+"); }
    /** Injected only after the successful minedTargets increment, not scan, skips or buffer-full returns. */
    public static void record(Object phase, Object target) {
        try {
            if (Boolean.TRUE.equals(call(target, "hazard"))) return;
            String id = String.valueOf(call(target, "itemId"));
            if (!validId(id)) return;
            Ledger ledger = LEDGERS.computeIfAbsent(phase, ignored -> new Ledger(baseline(phase), new HashMap<>()));
            if (!ledger.counts.containsKey(id) && ledger.counts.size() >= MAX_TYPES) return;
            ledger.counts.merge(id, 1L, PlayerMining0700::add);
        } catch (Exception e) { report(e); }
    }
    private static long baseline(Object phase) {
        try { return Math.max(0, ((Number) field(phase, "minedTargets")).longValue() - 1); }
        catch (Exception e) { return 0; }
    }
    public static List<Map.Entry<String, Long>> top(Object phase, int limit) {
        Ledger ledger = LEDGERS.get(phase);
        if (ledger == null) return List.of();
        return ledger.counts.entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .limit(Math.max(0, Math.min(5, limit))).map(e -> Map.entry(e.getKey(), e.getValue())).toList();
    }
    public static String scope(Object phase) {
        Ledger ledger = LEDGERS.get(phase);
        if (ledger == null) return "NO TRACKED MINING YET";
        return ledger.baseline == 0 ? "THIS SCAN AREA / MINED BLOCKS" : "SINCE UPDATE / " + amount(ledger.baseline) + " EARLIER UNTRACKED";
    }
    public static void write(Object data, Object root) {
        try {
            Object tag = root.getClass().getConstructor().newInstance();
            for (var entry : ((Map<?, ?>) field(data, "states")).entrySet()) {
                Ledger ledger = LEDGERS.get(entry.getValue()); if (ledger == null) continue;
                Object ship = root.getClass().getConstructor().newInstance();
                call(ship, "putLong", "baseline", ledger.baseline);
                Object counts = root.getClass().getConstructor().newInstance();
                for (var count : ledger.counts.entrySet()) call(counts, "putLong", count.getKey(), count.getValue());
                call(ship, "put", "counts", counts); call(tag, "put", entry.getKey().toString(), ship);
            }
            call(root, "put", KEY, tag);
        } catch (Exception e) { report(e); }
    }
    public static void read(Object data, Object root) {
        try {
            Object tag = call(root, "getCompound", KEY);
            for (var entry : ((Map<?, ?>) field(data, "states")).entrySet()) {
                reset(entry.getValue());
                String shipId = entry.getKey().toString();
                if (!Boolean.TRUE.equals(call(tag, "contains", shipId))) continue;
                Object ship = call(tag, "getCompound", shipId), counts = call(ship, "getCompound", "counts");
                long baseline = Math.max(0, ((Number) call(ship, "getLong", "baseline")).longValue());
                Map<String, Long> values = new HashMap<>();
                for (Object key : (Set<?>) call(counts, "getAllKeys")) {
                    if (values.size() >= MAX_TYPES) break;
                    String id = key.toString(); long n = ((Number) call(counts, "getLong", id)).longValue();
                    if (validId(id) && n > 0) values.put(id, n);
                }
                LEDGERS.put(entry.getValue(), new Ledger(baseline, values));
            }
        } catch (Exception e) { report(e); }
    }
    private static void report(Exception e) { if (!errorReported) { errorReported = true; System.err.println("[NewWorld Mining Yield] failed: " + e); } }
}
