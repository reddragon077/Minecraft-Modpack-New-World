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
        expect("geological survey range", NewWorldTuning.playerGeologicalSurveyRangeBlocks(), 48L);
        expect("geological survey vertical range", NewWorldTuning.playerGeologicalSurveyVerticalRangeBlocks(), 128L);
        expect("geological survey delay", NewWorldTuning.playerGeologicalSurveyDelayTicks(), 80L);
        expect("geological survey results", NewWorldTuning.playerGeologicalSurveyMaxResults(), 8L);
        expect("geological survey block checks", NewWorldTuning.playerGeologicalSurveyMaxBlockChecks(), 4096L);
        expect("geological survey minimum matches", NewWorldTuning.playerGeologicalSurveyMinimumMatches(), 3L);
        expect("structure radar analysis", NewWorldTuning.discoveryAnalysisLevel("STRUCTURE", "RADAR"), 0L);
        expect("structure field analysis", NewWorldTuning.discoveryAnalysisLevel("STRUCTURE", "FIELD"), 1L);
        expect("geology radar analysis fallback", NewWorldTuning.discoveryAnalysisLevel("GEOLOGY", "RADAR"), 0L);
        expect("geology field analysis", NewWorldTuning.discoveryAnalysisLevel("GEOLOGY", "FIELD"), 2L);
        expect("geology radar accuracy 0", NewWorldTuning.geologyRadarAnalysisLevel(0), 0L);
        expect("geology radar accuracy 1", NewWorldTuning.geologyRadarAnalysisLevel(1), 1L);
        expect("geology radar accuracy 2", NewWorldTuning.geologyRadarAnalysisLevel(2), 2L);
        expect("geology radar accuracy 3", NewWorldTuning.geologyRadarAnalysisLevel(3), 2L);
        expect("iron reveal tier", NewWorldTuning.geologyRequiredAccuracy("newworldcore:iron_oxide"), 0L);
        expect("tin reveal tier", NewWorldTuning.geologyRequiredAccuracy("tin_lode"), 1L);
        expect("diamond reveal tier", NewWorldTuning.geologyRequiredAccuracy("newworldcore:diamond_pipe"), 2L);
        expect("uraninite reveal tier", NewWorldTuning.geologyRequiredAccuracy("newworldcore:uraninite_pocket"), 3L);
        expect("basic family exact at accuracy 0", NewWorldTuning.geologyRadarAnalysisFor("iron_oxide", 0), 3L);
        expect("advanced family masked at accuracy 1", NewWorldTuning.geologyRadarAnalysisFor("diamond_pipe", 1), 1L);
        expect("advanced family exact at accuracy 2", NewWorldTuning.geologyRadarAnalysisFor("diamond_pipe", 2), 3L);
        expect("rare family masked at accuracy 2", NewWorldTuning.geologyRadarAnalysisFor("uraninite_pocket", 2), 2L);
        expect("rare family exact at accuracy 3", NewWorldTuning.geologyRadarAnalysisFor("uraninite_pocket", 3), 3L);
        expect("geology field exact matches", NewWorldTuning.geologyFieldExactMinimumMatches(), 8L);
        expect("geology field base quality", NewWorldTuning.geologyFieldAnalysisLevel(3), 2L);
        expect("geology field exact quality", NewWorldTuning.geologyFieldAnalysisLevel(8), 3L);
        expectText("geology anomaly label", NewWorldTuning.geologyAnalysisLabel(0, "TIN-RICH DEPOSIT"), "GEOLOGICAL ANOMALY");
        expectText("geology metallic label", NewWorldTuning.geologyAnalysisLabel(1, "TIN-RICH DEPOSIT"), "METALLIC ANOMALY");
        expectText("geology rich label", NewWorldTuning.geologyAnalysisLabel(2, "TIN-RICH DEPOSIT"), "RESOURCE-RICH DEPOSIT");
        expectText("geology exact label", NewWorldTuning.geologyAnalysisLabel(3, "TIN-RICH DEPOSIT"), "TIN-RICH DEPOSIT");
        expect("GUI overlay depth", Math.round(NewWorldTuning.guiFilterOverlayZ()), 1000L);
        expect("GUI background alpha", NewWorldTuning.guiPlayerBackdropArgb(), -1_291_845_632L);
        expect("discoveries sync limit", NewWorldTuning.playerDiscoveriesSyncLimit(), 64L);
        expect("discoveries rows", NewWorldTuning.playerDiscoveriesRows(), 6L);
        expect("network FE transfer", NewWorldTuning.networkNodeTransferLimit(0, 3), 100_000L);
        expect("network item capacity", NewWorldTuning.networkNodeCapacityLimit(1, 4), 1024L);
        if (!"SURVEY RANGE S:48 G:48x128 blocks // RESPONSE S:~4.0s G:~4.0s".equals(NewWorldTuning.playerSurveyDetailLine())) {
            throw new AssertionError("player survey GUI detail line: " + NewWorldTuning.playerSurveyDetailLine());
        }
        System.out.println("NewWorldCore config smoke test passed: " + NewWorldConfig.root());
    }

    private static void expect(String label, long actual, long expected) {
        if (actual != expected) throw new AssertionError(label + ": expected " + expected + ", got " + actual);
    }

    private static void expectText(String label, String actual, String expected) {
        if (!expected.equals(actual)) throw new AssertionError(label + ": expected " + expected + ", got " + actual);
    }
}
