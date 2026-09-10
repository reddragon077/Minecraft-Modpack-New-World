package net.newworld.player;

import java.util.*;

/** Transient meter of actual FE-pool withdrawals. Never changes the amount returned by the pool. */
public final class OverviewEnergyMeter0670 {
    private static final Map<Object, Map<String, Long>> TOTALS = new WeakHashMap<>();
    private OverviewEnergyMeter0670() {}
    public static synchronized long record(Object pool, String ship, long removed, boolean simulate) {
        if (!simulate && removed > 0 && pool != null && ship != null) {
            Map<String, Long> ships = TOTALS.computeIfAbsent(pool, ignored -> new HashMap<>());
            // Modulo-long counter; subtraction still gives the correct interval delta across wraparound.
            ships.put(ship, ships.getOrDefault(ship, 0L) + removed);
        }
        return removed;
    }
    public static synchronized long total(Object pool, String ship) {
        Map<String, Long> ships = TOTALS.get(pool);
        return ships == null ? 0 : ships.getOrDefault(ship, 0L);
    }
    public static double perTick(long before, long after, long ticks) {
        return ticks <= 0 ? 0 : Math.max(0L, after - before) / (double) ticks;
    }
}
