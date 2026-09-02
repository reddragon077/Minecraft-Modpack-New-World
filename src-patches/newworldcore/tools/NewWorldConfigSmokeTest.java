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
        expect("player survey range", NewWorldTuning.playerFieldSurveyRangeBlocks(), 48L);
        expect("player survey chunks", NewWorldTuning.playerFieldSurveyChunkRadius(), 3L);
        expect("player survey delay", NewWorldTuning.playerFieldSurveyDelayTicks(), 80L);
        expect("GUI overlay depth", Math.round(NewWorldTuning.guiFilterOverlayZ()), 1000L);
        expect("GUI background alpha", NewWorldTuning.guiPlayerBackdropArgb(), -1_291_845_632L);
        expect("network FE transfer", NewWorldTuning.networkNodeTransferLimit(0, 3), 100_000L);
        expect("network item capacity", NewWorldTuning.networkNodeCapacityLimit(1, 4), 1024L);
        if (!"STRUCTURE RANGE: 48 blocks // RESPONSE: ~4.0s".equals(NewWorldTuning.playerSurveyDetailLine())) {
            throw new AssertionError("player survey GUI detail line: " + NewWorldTuning.playerSurveyDetailLine());
        }
        System.out.println("NewWorldCore config smoke test passed: " + NewWorldConfig.root());
    }

    private static void expect(String label, long actual, long expected) {
        if (actual != expected) throw new AssertionError(label + ": expected " + expected + ", got " + actual);
    }
}
