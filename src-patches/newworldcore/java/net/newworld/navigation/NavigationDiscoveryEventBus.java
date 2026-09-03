package net.newworld.navigation;

import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Dependency-free discovery event bus for later Research and Exploration XP listeners.
 * Events are emitted synchronously on the thread that updates SavedData (normally the server thread).
 */
public final class NavigationDiscoveryEventBus {
    private static final CopyOnWriteArrayList<Listener> LISTENERS = new CopyOnWriteArrayList<>();

    private NavigationDiscoveryEventBus() {}

    public enum Change {
        DISCOVERED,
        SEEN,
        ANALYSIS_UPGRADED
    }

    public record Event(
            String shipId,
            Object discovery,
            String key,
            String kind,
            String source,
            Change change,
            int previousAnalysisLevel,
            int analysisLevel,
            long firstDiscoveredAt,
            long lastSeenAt) {}

    @FunctionalInterface
    public interface Listener {
        void onDiscovery(Event event);
    }

    public static void subscribe(Listener listener) {
        LISTENERS.addIfAbsent(Objects.requireNonNull(listener, "listener"));
    }

    public static void unsubscribe(Listener listener) {
        if (listener != null) LISTENERS.remove(listener);
    }

    public static int listenerCount() {
        return LISTENERS.size();
    }

    static void emit(Event event) {
        for (Listener listener : LISTENERS) {
            try {
                listener.onDiscovery(event);
            } catch (Throwable failure) {
                System.err.println("[NewWorldCore] Discovery event listener failed: " + failure);
            }
        }
    }
}
