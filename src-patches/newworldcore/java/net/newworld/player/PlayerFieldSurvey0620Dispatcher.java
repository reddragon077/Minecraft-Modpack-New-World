package net.newworld.player;

import net.newworld.navigation.Navigation0520GeologyScanRuntime;

/** Routes the existing bidirectional survey payload without changing its protocol. */
public final class PlayerFieldSurvey0620Dispatcher {
    private PlayerFieldSurvey0620Dispatcher() {}

    public static void handle(Object player, int mode) {
        if (player == null) return;
        try {
            if (mode == 0) {
                PlayerFieldSurvey0503Fix.scanStructures(player);
            } else if (mode == 1) {
                PlayerGeologicalFieldSurvey0620.scan(player);
            } else if (mode == 2) {
                Navigation0520GeologyScanRuntime.handle(player);
            } else if (mode == 3) {
                PlayerDiscoveries0650.sendSnapshot(player);
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
