package net.newworld.config;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/** Lightweight, dependency-free reader for config/newworldcore/*.properties. */
public final class NewWorldConfig {
    private static final long RECHECK_NANOS = 1_000_000_000L;
    private static final Map<String, CachedFile> CACHE = new ConcurrentHashMap<>();
    private static volatile Path resolvedRoot;

    private NewWorldConfig() {}

    public static int integer(String file, String key, int fallback, int min, int max) {
        String value = value(file, key);
        if (value == null) return clamp(fallback, min, max);
        try { return clamp(Integer.parseInt(value.trim()), min, max); }
        catch (NumberFormatException ignored) { warn(file, key, value, fallback); return clamp(fallback, min, max); }
    }

    public static long longValue(String file, String key, long fallback, long min, long max) {
        String value = value(file, key);
        if (value == null) return clamp(fallback, min, max);
        try { return clamp(Long.parseLong(value.trim()), min, max); }
        catch (NumberFormatException ignored) { warn(file, key, value, fallback); return clamp(fallback, min, max); }
    }

    public static double decimal(String file, String key, double fallback, double min, double max) {
        String value = value(file, key);
        if (value == null) return clamp(fallback, min, max);
        try { return clamp(Double.parseDouble(value.trim()), min, max); }
        catch (NumberFormatException ignored) { warn(file, key, value, fallback); return clamp(fallback, min, max); }
    }

    public static boolean bool(String file, String key, boolean fallback) {
        String value = value(file, key);
        if (value == null) return fallback;
        return switch (value.trim().toLowerCase(java.util.Locale.ROOT)) {
            case "true", "yes", "on", "1" -> true;
            case "false", "no", "off", "0" -> false;
            default -> { warn(file, key, value, fallback); yield fallback; }
        };
    }

    public static String text(String file, String key, String fallback, int maxLength) {
        String value = value(file, key);
        String selected = value == null ? fallback : value.trim();
        if (selected == null || selected.isBlank()) selected = fallback == null ? "" : fallback;
        int limit = Math.max(1, maxLength);
        return selected.length() <= limit ? selected : selected.substring(0, limit);
    }

    public static void reload() { CACHE.clear(); }

    public static Path root() {
        Path cached = resolvedRoot;
        if (cached != null) return cached;
        synchronized (NewWorldConfig.class) {
            if (resolvedRoot == null) resolvedRoot = discoverRoot();
            return resolvedRoot;
        }
    }

    private static String value(String file, String key) { return properties(file).getProperty(key); }

    private static Properties properties(String file) {
        long now = System.nanoTime();
        CachedFile cached = CACHE.get(file);
        if (cached != null && now < cached.nextCheckNanos) return cached.properties;
        Path path = root().resolve(file + ".properties");
        long modified = modified(path);
        if (cached != null && cached.modifiedMillis == modified) {
            CACHE.put(file, new CachedFile(cached.properties, modified, now + RECHECK_NANOS));
            return cached.properties;
        }
        Properties loaded = new Properties();
        if (Files.isRegularFile(path)) {
            try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) { loaded.load(reader); }
            catch (IOException failure) { System.err.println("[NewWorldCore] Could not read config " + path + ": " + failure); }
        }
        CACHE.put(file, new CachedFile(loaded, modified, now + RECHECK_NANOS));
        return loaded;
    }

    private static Path discoverRoot() {
        String override = System.getProperty("newworldcore.configDir");
        if (override != null && !override.isBlank()) return Path.of(override).toAbsolutePath().normalize();
        try {
            Class<?> fmlPaths = Class.forName("net.neoforged.fml.loading.FMLPaths");
            Field configDir = fmlPaths.getField("CONFIGDIR");
            Object holder = configDir.get(null);
            Method get = holder.getClass().getMethod("get");
            Object value = get.invoke(holder);
            if (value instanceof Path path) return path.resolve("newworldcore").toAbsolutePath().normalize();
        } catch (Throwable ignored) {}
        return Path.of(System.getProperty("user.dir", "."), "config", "newworldcore").toAbsolutePath().normalize();
    }

    private static long modified(Path path) {
        try { return Files.isRegularFile(path) ? Files.getLastModifiedTime(path).toMillis() : -1L; }
        catch (IOException ignored) { return -1L; }
    }

    private static int clamp(int value, int min, int max) { return Math.max(min, Math.min(max, value)); }
    private static long clamp(long value, long min, long max) { return Math.max(min, Math.min(max, value)); }
    private static double clamp(double value, double min, double max) { return Math.max(min, Math.min(max, value)); }

    private static void warn(String file, String key, String value, Object fallback) {
        System.err.println("[NewWorldCore] Invalid config " + file + '.' + key + "='" + value + "'; using " + fallback + '.');
    }

    private record CachedFile(Properties properties, long modifiedMillis, long nextCheckNanos) {}
}
