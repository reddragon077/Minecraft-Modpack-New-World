import net.newworld.config.NewWorldConfig;
import net.newworld.config.NewWorldTuning;

/** Standalone smoke test for the shipped default configuration. */
public final class NewWorldConfigSmokeTest {
    private NewWorldConfigSmokeTest() {}

    public static void main(String[] args) {
        expect("radar batch interval", NewWorldTuning.radarBatchIntervalTicks(), 8L);
        expect("radar results", NewWorldTuning.radarMaxResults(), 128L);
        expect("FE capacity", NewWorldTuning.feEnergyCapacity(2, 1), 3_250_000L);
        expect("FE transfer", NewWorldTuning.feTransferLimit(2, 1), 100_000L);
        expect("Warp capacity", NewWorldTuning.warpCapacity(3), 3000L);
        expect("Warp FE/WE", NewWorldTuning.warpFePerWe(4), 8000L);
        expect("replication interval", NewWorldTuning.replicationIntervalTicks(), 5L);
        expect("replication batch", NewWorldTuning.replicationBatchSize(), 64L);
        System.out.println("NewWorldCore config smoke test passed: " + NewWorldConfig.root());
    }

    private static void expect(String label, long actual, long expected) {
        if (actual != expected) throw new AssertionError(label + ": expected " + expected + ", got " + actual);
    }
}
