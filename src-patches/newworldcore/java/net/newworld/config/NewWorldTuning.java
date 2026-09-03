package net.newworld.config;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Map;

import net.newworld.mining.MiningUpgradeRuntime;
import net.newworld.navigation.NavigationUpgradeRuntime;

/** Config-backed balance formulas used by the binary compatibility patch. */
public final class NewWorldTuning {
    private NewWorldTuning() {}

    public static int radarBatchIntervalTicks() {
        int requested = NewWorldConfig.integer("radar", "scan.batch_interval_ticks", 4, 4, 1200);
        return ((requested + 3) / 4) * 4;
    }
    public static int radarMaxResults() { return NewWorldConfig.integer("radar", "scan.max_results", 128, 1, 512); }
    public static int radarMaxPlacementTasks() { return NewWorldConfig.integer("radar", "scan.max_placement_tasks", 512, 1, 2048); }
    public static int radarDuplicateRadius() { return NewWorldConfig.integer("radar", "scan.duplicate_radius_blocks", 8, 0, 128); }

    public static int navigationScanRange(Object container) {
        int level = NavigationUpgradeRuntime.rangeLevel(container);
        int base = NewWorldConfig.integer("radar", "navigation.range.base_blocks", 2000, 16, 100_000);
        int perLevel = NewWorldConfig.integer("radar", "navigation.range.per_upgrade_blocks", 1000, 0, 100_000);
        return saturatingInt((long) base + (long) perLevel * clampLevel(level));
    }

    public static int navigationTilesPerBatch(Object container) {
        int base = NewWorldConfig.integer("radar", "navigation.tiles_per_batch.base", 1, 1, 128);
        int perLevel = NewWorldConfig.integer("radar", "navigation.tiles_per_batch.per_upgrade", 1, 0, 128);
        return saturatingInt((long) base + (long) perLevel * clampLevel(NavigationUpgradeRuntime.speedLevel(container)));
    }

    public static int navigationClassificationChunks(Object container) {
        int base = NewWorldConfig.integer("radar", "navigation.classification.base_chunks", 2, 1, 128);
        int perLevel = NewWorldConfig.integer("radar", "navigation.classification.per_upgrade_chunks", 1, 0, 128);
        return saturatingInt((long) base + (long) perLevel * clampLevel(NavigationUpgradeRuntime.accuracyLevel(container)));
    }

    public static long navigationClassificationSq(Object container) {
        long base = NewWorldConfig.longValue("radar", "navigation.classification.base_radius_blocks", 64L, 1L, 100_000L);
        long perLevel = NewWorldConfig.longValue("radar", "navigation.classification.per_upgrade_radius_blocks", 32L, 0L, 100_000L);
        long radius = Math.min(1_000_000L, base + perLevel * clampLevel(NavigationUpgradeRuntime.accuracyLevel(container)));
        return radius * radius;
    }

    public static int navigationEfficiencyPercent(Object container) {
        return levelInt("radar", "navigation.efficiency.percent", NavigationUpgradeRuntime.efficiencyLevel(container), 100, 82, 65, 50, 1, 1000);
    }

    public static double navigationCpuTimeMultiplier(Object container) {
        return levelDouble("radar", "navigation.cpu.time_multiplier", NavigationUpgradeRuntime.cpuSpeedLevel(container), 1.0D, 0.72D, 0.52D, 0.38D, 0.01D, 100.0D);
    }

    public static double navigationCpuPowerMultiplier(Object container) {
        return levelDouble("radar", "navigation.cpu.power_multiplier", NavigationUpgradeRuntime.cpuSpeedLevel(container), 1.0D, 1.3D, 1.7D, 2.2D, 0.01D, 100.0D);
    }

    public static long navigationRadarFePerTile(Object container) {
        double base = NewWorldConfig.decimal("radar", "energy.base_fe_per_task", 1800.0D, 0.0D, 1.0E12D);
        double range = levelDouble("radar", "energy.range_multiplier", NavigationUpgradeRuntime.rangeLevel(container), 1.0D, 1.15D, 1.3D, 1.45D, 0.0D, 100.0D);
        double speed = levelDouble("radar", "energy.speed_multiplier", NavigationUpgradeRuntime.speedLevel(container), 1.0D, 1.3D, 1.6D, 1.9D, 0.0D, 100.0D);
        double accuracy = levelDouble("radar", "energy.accuracy_multiplier", NavigationUpgradeRuntime.accuracyLevel(container), 1.0D, 1.08D, 1.18D, 1.3D, 0.0D, 100.0D);
        return Math.max(0L, Math.round(base * range * speed * accuracy * navigationEfficiencyPercent(container) / 100.0D));
    }

    public static void navigationScanBatch(Object container) {
        try {
            Object level = field(container, "scanExterior");
            Method gameTimeMethod = level.getClass().getMethod("getGameTime");
            long gameTime = ((Number) gameTimeMethod.invoke(level)).longValue();
            if (Math.floorMod(gameTime, radarBatchIntervalTicks()) != 0L) return;
        } catch (Throwable ignored) {
            // If timing cannot be read, preserve the original scheduler instead of blocking Radar.
        }
        invokeBase("net.newworld.navigation.NavigationUpgradeRuntime", "scanBatchConfigBase",
                new Class<?>[]{Object.class}, container);
    }

    public static int miningScanProbes(Object context, String shipId) {
        return levelInt("mining", "scan.probes", MiningUpgradeRuntime.getLevel(context, shipId, 1), 512, 768, 1024, 1536, 1, 1_000_000);
    }

    public static long miningMineInterval(Object context, String shipId) {
        return levelLong("mining", "mine.interval_ticks", MiningUpgradeRuntime.getLevel(context, shipId, 1), 40L, 30L, 20L, 10L, 1L, 72_000L);
    }

    public static long miningEnergyCost(Object context, String shipId) {
        int speed = MiningUpgradeRuntime.getLevel(context, shipId, 1);
        int efficiency = MiningUpgradeRuntime.getLevel(context, shipId, 2);
        long base = levelLong("mining", "energy.base_fe", speed, 5000L, 7000L, 10_000L, 14_000L, 0L, 1_000_000_000_000L);
        int percent = levelInt("mining", "energy.efficiency_percent", efficiency, 100, 82, 65, 50, 0, 1000);
        return Math.max(1L, Math.round(base * percent / 100.0D));
    }

    public static long miningEfficiencyDisplay(int level) {
        long base = NewWorldConfig.longValue("mining", "energy.display_base_fe", 5000L, 0L, 1_000_000_000_000L);
        int percent = levelInt("mining", "energy.efficiency_percent", level, 100, 82, 65, 50, 0, 1000);
        return Math.max(0L, Math.round(base * percent / 100.0D));
    }

    public static long feEnergyCapacity(int storageModules, int reserveModules) {
        long base = NewWorldConfig.longValue("matrix", "fe.capacity.base", 1_000_000L, 0L, Long.MAX_VALUE / 4);
        long perStorage = NewWorldConfig.longValue("matrix", "fe.capacity.per_storage_module", 1_000_000L, 0L, Long.MAX_VALUE / 4);
        long perReserve = NewWorldConfig.longValue("matrix", "fe.capacity.per_emergency_module", 250_000L, 0L, Long.MAX_VALUE / 4);
        return saturatingLong(base, multiplyNonNegative(storageModules, perStorage), multiplyNonNegative(reserveModules, perReserve));
    }

    public static long feTransferLimit(int providerModules, int ioModules) {
        long providerUnit = NewWorldConfig.longValue("matrix", "fe.transfer.per_provider_module", 25_000L, 0L, Integer.MAX_VALUE);
        long ioUnit = NewWorldConfig.longValue("matrix", "fe.transfer.per_io_module", 100_000L, 0L, Integer.MAX_VALUE);
        long provider = multiplyNonNegative(providerModules, providerUnit);
        long io = multiplyNonNegative(ioModules, ioUnit);
        return Math.min(Integer.MAX_VALUE, provider + Math.min(provider, io));
    }

    public static int feProviderBonus(int ioModules, int providerModules) {
        long providerUnit = NewWorldConfig.longValue("matrix", "fe.transfer.per_provider_module", 25_000L, 0L, Integer.MAX_VALUE);
        long ioUnit = NewWorldConfig.longValue("matrix", "fe.transfer.per_io_module", 100_000L, 0L, Integer.MAX_VALUE);
        return saturatingInt(Math.min(multiplyNonNegative(providerModules, providerUnit), multiplyNonNegative(ioModules, ioUnit)));
    }

    public static int feTierWeight(int tier) {
        int[] defaults = {1, 4, 16, 64, 256, 1024, 4096, 16384};
        int clamped = Math.max(1, Math.min(8, tier));
        return NewWorldConfig.integer("matrix", "fe.tier_weight." + clamped, defaults[clamped - 1], 1, Integer.MAX_VALUE);
    }

    public static int warpTierWeight(int tier) {
        int clamped = Math.max(1, Math.min(8, tier));
        return NewWorldConfig.integer("matrix", "warp.tier_weight." + clamped, clamped, 1, Integer.MAX_VALUE);
    }

    public static int warpCapacity(int capacitorModules) {
        long perModule = NewWorldConfig.longValue("matrix", "warp.capacity.per_capacitor_module", 1000L, 0L, Integer.MAX_VALUE);
        return saturatingInt(multiplyNonNegative(capacitorModules, perModule));
    }

    public static int warpFePerWe(int efficiencyCoils) {
        long base = NewWorldConfig.longValue("matrix", "warp.fe_per_we.base", 10_000L, 0L, Integer.MAX_VALUE);
        long reduction = NewWorldConfig.longValue("matrix", "warp.fe_per_we.reduction_per_coil", 500L, 0L, Integer.MAX_VALUE);
        long minimum = NewWorldConfig.longValue("matrix", "warp.fe_per_we.minimum", 4000L, 0L, Integer.MAX_VALUE);
        return saturatingInt(Math.max(minimum, base - multiplyNonNegative(efficiencyCoils, reduction)));
    }

    public static int warpMaxProduction(int producers, int catalysts) {
        int bonusPer = NewWorldConfig.integer("matrix", "warp.production.bonus_percent_per_catalyst", 10, 0, 10_000);
        int maxBonus = NewWorldConfig.integer("matrix", "warp.production.max_bonus_percent", 100, 0, 100_000);
        long active = Math.max(0, producers);
        long bonus = Math.min(maxBonus, multiplyNonNegative(catalysts, bonusPer));
        return saturatingInt((active * (100L + bonus) + 99L) / 100L);
    }

    public static int shipWarpCapacity(String shipId) {
        int[] modules = shipModules(shipId, ":WARP");
        return modules == null || modules.length == 0 ? 0 : warpCapacity(modules[0]);
    }

    public static int shipMaxRange(String shipId) {
        int[] modules = shipModules(shipId, ":ENGINE");
        int level = modules == null || modules.length == 0 ? 0 : Math.max(0, modules[0]);
        long base = NewWorldConfig.longValue("travel", "engine.base_range_blocks", 512L, 1L, Integer.MAX_VALUE);
        double multiplier = NewWorldConfig.decimal("travel", "engine.range_multiplier_per_module", 2.0D, 1.0D, 1000.0D);
        int maxLevel = NewWorldConfig.integer("travel", "engine.max_effective_modules", 22, 0, 62);
        double value = base * Math.pow(multiplier, Math.min(level, maxLevel));
        return saturatingInt(value >= Integer.MAX_VALUE ? Integer.MAX_VALUE : Math.round(value));
    }

    public static long geologyEnergyCost(int tasks, int rangeLevel, int speedLevel, int accuracyLevel, int efficiencyPercent) {
        double base = NewWorldConfig.decimal("geology", "scan.energy.base_fe_per_task", 1800.0D, 0.0D, 1.0E12D);
        double range = levelDouble("geology", "scan.energy.range_multiplier", rangeLevel, 1.0D, 1.15D, 1.3D, 1.45D, 0.0D, 100.0D);
        double speed = levelDouble("geology", "scan.energy.speed_multiplier", speedLevel, 1.0D, 1.3D, 1.6D, 1.9D, 0.0D, 100.0D);
        double accuracy = levelDouble("geology", "scan.energy.accuracy_multiplier", accuracyLevel, 1.0D, 1.08D, 1.18D, 1.3D, 0.0D, 100.0D);
        return Math.max(1L, Math.round(Math.max(0, tasks) * base * range * speed * accuracy * Math.max(0, efficiencyPercent) / 100.0D));
    }

    public static int geologyEfficiencyPercent(int level) {
        return levelInt("geology", "scan.energy.efficiency_percent", level, 100, 82, 65, 50, 0, 1000);
    }

    public static long replicationIntervalTicks() { return NewWorldConfig.longValue("replication", "feed.interval_ticks", 5L, 1L, 72_000L); }
    public static long replicationBatchSize() { return NewWorldConfig.longValue("replication", "feed.batch_items", 64L, 1L, 1_000_000L); }

    public static int playerFieldSurveyRangeBlocks() {
        return NewWorldConfig.integer("player", "field_survey.range_blocks", 48, 16, 512);
    }

    public static int playerFieldSurveyChunkRadius() {
        return Math.max(1, (playerFieldSurveyRangeBlocks() + 15) / 16);
    }

    public static int playerFieldSurveyDelayTicks() {
        return NewWorldConfig.integer("player", "field_survey.delay_ticks", 80, 0, 1200);
    }

    public static int playerGeologicalSurveyRangeBlocks() {
        return NewWorldConfig.integer("player", "geological_survey.horizontal_range_blocks", 48, 16, 512);
    }

    public static int playerGeologicalSurveyVerticalRangeBlocks() {
        return NewWorldConfig.integer("player", "geological_survey.vertical_range_blocks", 128, 16, 1024);
    }

    public static int playerGeologicalSurveyDelayTicks() {
        return NewWorldConfig.integer("player", "geological_survey.delay_ticks", 80, 0, 1200);
    }

    public static int playerGeologicalSurveyMaxResults() {
        return NewWorldConfig.integer("player", "geological_survey.max_results", 8, 1, 32);
    }

    public static int playerGeologicalSurveyMaxBlockChecks() {
        return NewWorldConfig.integer("player", "geological_survey.max_block_checks_per_candidate", 4096, 32, 100_000);
    }

    public static int playerGeologicalSurveyMinimumMatches() {
        return NewWorldConfig.integer("player", "geological_survey.minimum_matching_blocks", 3, 1, 64);
    }

    public static int geologyFieldExactMinimumMatches() {
        return NewWorldConfig.integer("discovery", "analysis.geology_field_exact_minimum_matches", 8,
                playerGeologicalSurveyMinimumMatches(), 256);
    }

    public static int geologyRadarAnalysisLevel(int accuracyLevel) {
        int level = clampLevel(accuracyLevel);
        return NewWorldConfig.integer("discovery", "analysis.geology_radar_accuracy_" + level + "_level",
                level, 0, 3);
    }

    /**
     * Returns the persistent Radar analysis level for one deposit family. Accuracy still
     * controls the generic anomaly/metal/resource-rich stages, while each family can be
     * promoted to exact (L3) at a configurable tier.
     */
    public static int geologyRadarAnalysisFor(String depositId, int accuracyLevel) {
        int accuracy = clampLevel(accuracyLevel);
        int base = geologyRadarAnalysisLevel(accuracy);
        return accuracy >= geologyRequiredAccuracy(depositId) ? Math.max(base, 3) : base;
    }

    public static int geologyRequiredAccuracy(String depositId) {
        String family = depositFamily(depositId);
        int fallback = switch (family) {
            case "iron_oxide", "copper_sulfide", "carboniferous" -> 0;
            case "quartz_vein", "gold_lode", "redstone_cluster", "lapis_basin",
                    "tin_lode", "lead_galena", "zinc_lode" -> 1;
            case "diamond_pipe", "emerald_shear", "osmium_strata", "fluorite_crystal",
                    "bauxite_strata", "nickel_sulfide", "silver_vein", "certus_quartz_matrix" -> 2;
            case "uranium_pitchblende", "platinum_intrusion", "uraninite_pocket" -> 3;
            default -> 3;
        };
        return NewWorldConfig.integer("discovery", "reveal.required_accuracy." + family,
                fallback, 0, 3);
    }

    public static int geologyFieldAnalysisLevel(int matchingBlocks) {
        int base = NewWorldConfig.integer("discovery", "analysis.geology_field_level", 2, 0, 3);
        int exact = NewWorldConfig.integer("discovery", "analysis.geology_field_exact_level", 3, 0, 3);
        return matchingBlocks >= geologyFieldExactMinimumMatches() ? Math.max(base, exact) : base;
    }

    public static String geologyAnalysisLabel(int analysisLevel, String exactLabel) {
        int level = clampLevel(analysisLevel);
        if (level >= 3 && exactLabel != null && !exactLabel.isBlank()) return exactLabel;
        String[] defaults = {"GEOLOGICAL ANOMALY", "METALLIC ANOMALY", "RESOURCE-RICH DEPOSIT"};
        return NewWorldConfig.text("discovery", "display.geology_level_" + level + "_label",
                defaults[Math.min(level, 2)], 48);
    }

    public static String geologyAnalysisPrimary(int analysisLevel, String exactPrimary) {
        int level = clampLevel(analysisLevel);
        if (level >= 3 && exactPrimary != null && !exactPrimary.isBlank()) return exactPrimary;
        String[] defaults = {"unknown signal", "metallic signature", "resource-rich signature"};
        return NewWorldConfig.text("discovery", "display.geology_level_" + level + "_resource",
                defaults[Math.min(level, 2)], 48);
    }

    public static int discoveryAnalysisLevel(String kind, String source) {
        boolean geology = "GEOLOGY".equalsIgnoreCase(kind);
        boolean field = "FIELD".equalsIgnoreCase(source);
        String key;
        int fallback;
        if (geology && field) {
            key = "analysis.geology_field_level";
            fallback = 2;
        } else if (geology) {
            key = "analysis.geology_radar_level";
            fallback = 0;
        } else if (field) {
            key = "analysis.structure_field_level";
            fallback = 1;
        } else {
            key = "analysis.structure_radar_level";
            fallback = 0;
        }
        return NewWorldConfig.integer("discovery", key, fallback, 0, 3);
    }

    public static double guiFilterOverlayZ() {
        return NewWorldConfig.decimal("gui", "filters.overlay_z", 1000.0D, 1.0D, 10_000.0D);
    }

    public static int guiPlayerBackdropArgb() {
        int percent = NewWorldConfig.integer("gui", "player.background_dim_percent", 70, 0, 100);
        int alpha = (int) Math.round(percent * 255.0D / 100.0D);
        return alpha << 24;
    }

    public static int playerDiscoveriesSyncLimit() {
        return NewWorldConfig.integer("gui", "player.discoveries.sync_limit_per_category", 64, 16, 256);
    }

    public static int playerDiscoveriesRows() {
        return NewWorldConfig.integer("gui", "player.discoveries.rows_per_page", 6, 5, 6);
    }

    public static String playerSurveyDetailLine() {
        boolean range = NewWorldConfig.bool("gui", "player.show_survey_range", true);
        boolean delay = NewWorldConfig.bool("gui", "player.show_survey_delay", true);
        if (!range && !delay) return "FIELD SURVEY PARAMETERS HIDDEN";
        StringBuilder line = new StringBuilder();
        if (range) {
            line.append("SURVEY RANGE S:").append(playerFieldSurveyRangeBlocks())
                    .append(" G:").append(playerGeologicalSurveyRangeBlocks()).append('x')
                    .append(playerGeologicalSurveyVerticalRangeBlocks()).append(" blocks");
        }
        if (delay) {
            if (!line.isEmpty()) line.append(" // ");
            line.append("RESPONSE S:~")
                    .append(String.format(Locale.ROOT, "%.1f", playerFieldSurveyDelayTicks() / 20.0D))
                    .append("s G:~")
                    .append(String.format(Locale.ROOT, "%.1f", playerGeologicalSurveyDelayTicks() / 20.0D))
                    .append('s');
        }
        return line.toString();
    }

    public static int networkNodeTransferLimit(int resourceType, int tier) {
        int[][] defaults = {
                {1000, 5000, 25_000, 100_000, 1_000_000, 10_000_000, 100_000_000, 1_000_000_000, Integer.MAX_VALUE},
                {4, 8, 16, 32, 64, 128, 256, 512, 1024},
                {250, 1000, 4000, 16_000, 64_000, 256_000, 1_000_000, 4_000_000, 16_000_000},
                {250, 1000, 4000, 16_000, 64_000, 256_000, 1_000_000, 4_000_000, 16_000_000}
        };
        int type = Math.max(0, Math.min(3, resourceType));
        int level = Math.max(0, Math.min(8, tier));
        double multiplier = NewWorldConfig.decimal("network", "node.transfer." + resourceName(type) + "_multiplier",
                1.0D, 0.01D, 1000.0D);
        return Math.max(1, saturatingInt(Math.round(defaults[type][level] * multiplier)));
    }

    public static int networkNodeCapacityLimit(int resourceType, int tier) {
        int[][] defaults = {
                {10_000, 50_000, 250_000, 1_000_000, 10_000_000, 100_000_000, 500_000_000, 1_000_000_000, 2_000_000_000},
                {64, 128, 256, 512, 1024, 2048, 4096, 8192, 16_384},
                {16_000, 64_000, 256_000, 1_000_000, 4_000_000, 16_000_000, 64_000_000, 256_000_000, 1_000_000_000},
                {16_000, 64_000, 256_000, 1_000_000, 4_000_000, 16_000_000, 64_000_000, 256_000_000, 1_000_000_000}
        };
        int type = Math.max(0, Math.min(3, resourceType));
        int level = Math.max(0, Math.min(8, tier));
        double multiplier = NewWorldConfig.decimal("network", "node.capacity." + resourceName(type) + "_multiplier",
                1.0D, 0.01D, 1000.0D);
        return Math.max(1, saturatingInt(Math.round(defaults[type][level] * multiplier)));
    }

    public static boolean roomProtected(Object level, Object pos) {
        if (!NewWorldConfig.bool("rooms", "protection.enabled", true)) return false;
        return Boolean.TRUE.equals(invokeBase("net.newworld.core.RoomProtectionManager", "isProtectedConfigBase", null, level, pos));
    }

    public static int networkEmergencyLimit(Object context, int requested) {
        if (!NewWorldConfig.bool("network", "emergency_reserve.enabled", true)) return Math.max(0, requested);
        Object value = invokeBase("net.newworld.network.EmergencyPowerPolicy", "limitConfigBase", new Class<?>[]{Object.class, int.class}, context, requested);
        return value instanceof Number number ? number.intValue() : Math.max(0, requested);
    }

    private static int[] shipModules(String shipId, String suffix) {
        if (shipId == null || shipId.isBlank()) return null;
        try {
            Class<?> registry = Class.forName("net.newworld.core.ShipRoomRegistry");
            Field statsField = registry.getDeclaredField("STATS");
            statsField.setAccessible(true);
            Object raw = statsField.get(null);
            if (!(raw instanceof Map<?, ?> stats)) return null;
            Object record = stats.get(shipId.concat(suffix));
            if (record == null) return null;
            Method active = record.getClass().getDeclaredMethod("active");
            Method modules = record.getClass().getDeclaredMethod("modules");
            active.setAccessible(true);
            modules.setAccessible(true);
            if (!Boolean.TRUE.equals(active.invoke(record))) return null;
            Object value = modules.invoke(record);
            return value instanceof int[] array ? array : null;
        } catch (Throwable ignored) { return null; }
    }

    private static String resourceName(int resourceType) {
        return switch (resourceType) {
            case 1 -> "item";
            case 2 -> "fluid";
            case 3 -> "gas";
            default -> "fe";
        };
    }

    private static String depositFamily(String depositId) {
        if (depositId == null) return "unknown";
        String normalized = depositId.trim().toLowerCase(Locale.ROOT);
        int namespace = normalized.indexOf(':');
        if (namespace >= 0 && namespace + 1 < normalized.length()) normalized = normalized.substring(namespace + 1);
        normalized = normalized.replaceAll("[^a-z0-9_.-]", "_");
        return normalized.isBlank() ? "unknown" : normalized;
    }

    private static Object invokeBase(String owner, String name, Class<?>[] parameterTypes, Object... args) {
        try {
            Class<?> type = Class.forName(owner);
            Method method;
            if (parameterTypes != null) method = type.getDeclaredMethod(name, parameterTypes);
            else {
                method = null;
                for (Method candidate : type.getDeclaredMethods()) {
                    if (candidate.getName().equals(name) && candidate.getParameterCount() == args.length) { method = candidate; break; }
                }
                if (method == null) throw new NoSuchMethodException(owner + '.' + name);
            }
            method.setAccessible(true);
            return method.invoke(null, args);
        } catch (Throwable failure) {
            System.err.println("[NewWorldCore] Config wrapper failed for " + owner + '.' + name + ": " + failure);
            return null;
        }
    }

    private static Object field(Object target, String name) throws Exception {
        for (Class<?> type = target.getClass(); type != null; type = type.getSuperclass()) {
            try {
                Field field = type.getDeclaredField(name);
                field.setAccessible(true);
                return field.get(target);
            } catch (NoSuchFieldException ignored) {}
        }
        throw new NoSuchFieldException(name);
    }

    private static int levelInt(String file, String prefix, int level, int d0, int d1, int d2, int d3, int min, int max) {
        int[] defaults = {d0, d1, d2, d3}; int clamped = clampLevel(level);
        return NewWorldConfig.integer(file, prefix + ".level_" + clamped, defaults[clamped], min, max);
    }

    private static long levelLong(String file, String prefix, int level, long d0, long d1, long d2, long d3, long min, long max) {
        long[] defaults = {d0, d1, d2, d3}; int clamped = clampLevel(level);
        return NewWorldConfig.longValue(file, prefix + ".level_" + clamped, defaults[clamped], min, max);
    }

    private static double levelDouble(String file, String prefix, int level, double d0, double d1, double d2, double d3, double min, double max) {
        double[] defaults = {d0, d1, d2, d3}; int clamped = clampLevel(level);
        return NewWorldConfig.decimal(file, prefix + ".level_" + clamped, defaults[clamped], min, max);
    }

    private static int clampLevel(int level) { return Math.max(0, Math.min(3, level)); }
    private static long multiplyNonNegative(int value, long unit) {
        if (value <= 0 || unit <= 0) return 0L;
        if (unit > Long.MAX_VALUE / value) return Long.MAX_VALUE;
        return unit * value;
    }
    private static long saturatingLong(long... values) {
        long total = 0L;
        for (long value : values) { if (value > 0) { if (Long.MAX_VALUE - total < value) return Long.MAX_VALUE; total += value; } }
        return total;
    }
    private static int saturatingInt(long value) { return value <= 0L ? 0 : (value >= Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) value); }
}
