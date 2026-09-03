import net.newworld.config.NewWorldTuning;
import net.newworld.player.PlayerGeologicalFieldSurvey0620;
import net.newworld.player.PlayerGeologicalSurveyGui0620;

/** Standalone deterministic-coordinate, GUI hitbox, and config smoke test. */
public final class PlayerGeologicalFieldSurvey0620SmokeTest {
    private PlayerGeologicalFieldSurvey0620SmokeTest() {}

    public static void main(String[] args) {
        int[] first = PlayerGeologicalFieldSurvey0620.candidateCoordinates(
                123456789L, 50, 30, -3, 7, -48, -48, false);
        int[] second = PlayerGeologicalFieldSurvey0620.candidateCoordinates(
                123456789L, 50, 30, -3, 7, -48, -48, false);
        if (!java.util.Arrays.equals(first, second)) throw new AssertionError("candidate math is not deterministic");
        if (Math.floorMod(first[0] - 8, 16) != 0 || Math.floorMod(first[2] - 8, 16) != 0) {
            throw new AssertionError("candidate centers are not chunk-centered");
        }
        expect("candidate fixed Y", first[1], -48);

        FakeScreen survey = new FakeScreen(800, 600, 1);
        if (!PlayerGeologicalSurveyGui0620.isGeologyButton(survey, 556, 321)) {
            throw new AssertionError("geology button center was not detected");
        }
        FakeScreen overview = new FakeScreen(800, 600, 0);
        if (PlayerGeologicalSurveyGui0620.isGeologyButton(overview, 556, 321)) {
            throw new AssertionError("geology button was active outside Survey tab");
        }

        expect("geology horizontal range", NewWorldTuning.playerGeologicalSurveyRangeBlocks(), 48);
        expect("geology vertical range", NewWorldTuning.playerGeologicalSurveyVerticalRangeBlocks(), 128);
        expect("geology delay", NewWorldTuning.playerGeologicalSurveyDelayTicks(), 80);
        expect("geology max results", NewWorldTuning.playerGeologicalSurveyMaxResults(), 8);
        expect("geology max checks", NewWorldTuning.playerGeologicalSurveyMaxBlockChecks(), 4096);
        expect("geology minimum matches", NewWorldTuning.playerGeologicalSurveyMinimumMatches(), 3);
        expect("geology exact matches", NewWorldTuning.geologyFieldExactMinimumMatches(), 8);
        expect("geology base field analysis", NewWorldTuning.geologyFieldAnalysisLevel(3), 2);
        expect("geology exact field analysis", NewWorldTuning.geologyFieldAnalysisLevel(8), 3);
        System.out.println("NewWorldCore Geological Field Survey smoke test passed");
    }

    private static void expect(String label, int actual, int expected) {
        if (actual != expected) throw new AssertionError(label + ": expected " + expected + ", got " + actual);
    }

    public static final class FakeScreen {
        public int width;
        public int height;
        private int tab;
        FakeScreen(int width, int height, int tab) { this.width = width; this.height = height; this.tab = tab; }
    }
}
