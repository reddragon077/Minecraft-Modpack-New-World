package net.newworld.player;

import net.newworld.navigation.Navigation0520GeologyScanRuntime;

/** Routes the existing bidirectional survey payload without changing its protocol. */
public final class PlayerFieldSurvey0620Dispatcher {
    private PlayerFieldSurvey0620Dispatcher() {}

    public static void handle(Object player, int mode) {
        if (player == null) return;
        try {
            Object server = PlayerOverview0670.call(player, "getServer");
            PlayerOverview0670.call(server, "execute", (Runnable) () -> handleOnServer(player, mode));
        } catch (Exception failure) { System.err.println("[NewWorld Ship Link] dispatch failed: " + failure); }
    }

    private static void handleOnServer(Object player, int mode) {
        try {
            if (mode == 5) { PlayerShipLink0680.request(player); return; }
            if (mode == 6) { PlayerNavigation0690.request(player); return; }
            if (mode != 4 && !PlayerShipLink0680.requireLink(player)) return;
            if (mode == 0) {
                PlayerFieldSurvey0503Fix.scanStructures(player);
            } else if (mode == 1) {
                PlayerGeologicalFieldSurvey0620.scan(player);
            } else if (mode == 2) {
                Navigation0520GeologyScanRuntime.handle(player);
            } else if (mode == 3) {
                PlayerDiscoveries0650.sendSnapshot(player);
            } else if (mode == 4) {
                PlayerOverview0670.request(player);
            } else if (PlayerDiscoveries0650.isActionMode(mode)) {
                PlayerDiscoveries0650.handleAction(player, mode);
            } else {
                System.err.println("[NewWorldCore/Survey] unsupported survey mode=" + mode);
            }
        } catch (Throwable failure) {
            System.err.println("[NewWorldCore/Survey] survey dispatch failed: " + failure);
            failure.printStackTrace(System.err);
        }
    }
}
