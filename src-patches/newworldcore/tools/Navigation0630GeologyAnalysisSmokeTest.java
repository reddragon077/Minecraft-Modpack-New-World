import net.newworld.navigation.Navigation0540GeologyClientProgress;
import net.newworld.navigation.Navigation0520GeologyClientState;
import net.newworld.navigation.Navigation0630GeologyAnalysis;

/** Standalone client reveal-level and exact-filter-lock smoke test. */
public final class Navigation0630GeologyAnalysisSmokeTest {
    private Navigation0630GeologyAnalysisSmokeTest() {}

    public static void main(String[] args) {
        Navigation0540GeologyClientProgress.dispatch(1_305_000_002);
        Navigation0520GeologyClientState.beginScan();
        result(0, 20, 3);
        result(1, 40, 2);
        Navigation0540GeologyClientProgress.dispatch(1_299_999_999);

        var results = Navigation0520GeologyClientState.results();
        expect("result count", results.size(), 2);
        expect("known result level", Navigation0630GeologyAnalysis.resultAnalysisLevel(results.get(0)), 3);
        expect("masked result level", Navigation0630GeologyAnalysis.resultAnalysisLevel(results.get(1)), 2);
        expectText("masked per-result label", Navigation0630GeologyAnalysis.clientLabel(results.get(1)),
                "RESOURCE-RICH DEPOSIT");
        String exact = Navigation0630GeologyAnalysis.clientLabel(results.get(0));
        if ("RESOURCE-RICH DEPOSIT".equals(exact)) {
            throw new AssertionError("known level-3 result remained masked");
        }
        expect("exact filter count", Navigation0630GeologyAnalysis.clientAvailableTypes().size(), 1);
        expect("exact filter type", Navigation0630GeologyAnalysis.clientAvailableTypes().get(0), 0);

        Navigation0520GeologyClientState.beginScan();
        expectText("empty exact-family filter lock", Navigation0630GeologyAnalysis.lockedFilterMessage(),
                "EXACT FAMILY FILTERS REQUIRE ANALYSIS III");
        System.out.println("NewWorldCore geology analysis progression smoke test passed");
    }

    private static void result(int type, int distance, int analysis) {
        Navigation0540GeologyClientProgress.dispatch(1_010_000_000 + type);
        Navigation0540GeologyClientProgress.dispatch(1_130_000_000);
        Navigation0540GeologyClientProgress.dispatch(1_170_002_048);
        Navigation0540GeologyClientProgress.dispatch(1_210_000_000);
        Navigation0540GeologyClientProgress.dispatch(1_250_000_010);
        Navigation0540GeologyClientProgress.dispatch(1_260_000_100);
        Navigation0540GeologyClientProgress.dispatch(1_270_000_000 + distance);
        Navigation0540GeologyClientProgress.dispatch(1_280_000_000 + analysis);
    }

    private static void expect(String label, int actual, int expected) {
        if (actual != expected) throw new AssertionError(label + ": expected " + expected + ", got " + actual);
    }

    private static void expectText(String label, String actual, String expected) {
        if (!expected.equals(actual)) throw new AssertionError(label + ": expected " + expected + ", got " + actual);
    }
}
