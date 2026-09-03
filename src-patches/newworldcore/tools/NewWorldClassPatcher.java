import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public final class NewWorldClassPatcher {
    private record Target(String helperOwner, String helperName, String helperDescriptor, int returnOpcode) {}

    private static final Map<String, Map<String, Target>> TARGETS = new HashMap<>();
    private static final String RADAR_UI_OWNER = "net/newworld/navigation/Navigation0510RadarDualMode";
    private static final String RADAR_OVERLAY_NAME = "renderFilterOverlay";
    private static final String RADAR_OVERLAY_BASE_NAME = "renderFilterOverlay0592Base";
    private static final String RADAR_OVERLAY_DESC = "(Ljava/lang/Object;Ljava/lang/Object;IILnet/newworld/navigation/Navigation0510RadarDualMode$FilterState;)V";
    private static final String ROOM_PROTECTION_OWNER = "net/newworld/core/RoomProtectionManager";
    private static final String EMERGENCY_POLICY_OWNER = "net/newworld/network/EmergencyPowerPolicy";
    private static final String REPLICATION_OWNER = "net/newworld/mining/ReplicationFeedRuntime";
    private static final String NAVIGATION_UPGRADE_OWNER = "net/newworld/navigation/NavigationUpgradeRuntime";
    private static final String GEOLOGY_UI_OWNER = "net/newworld/navigation/Navigation0559SingleOwnerRadarUi";
    private static final String TRUE_SINGLE_GEOLOGY_UI_OWNER = "net/newworld/navigation/Navigation0561TrueSingleRadarUi";
    private static final String GEOLOGY_ANALYSIS_UI_OWNER = "net/newworld/navigation/Navigation0558UnifiedRadarUi";
    private static final String GEOLOGY_TIMED_OWNER = "net/newworld/navigation/Navigation0540GeologyTimedScan";
    private static final String GEOLOGY_SCAN_OWNER = "net/newworld/navigation/Navigation0520GeologyScanRuntime";
    private static final String GEOLOGY_CLIENT_STATE_OWNER = "net/newworld/navigation/Navigation0520GeologyClientState";
    private static final String PLAYER_GUI_OWNER = "net/newworld/player/PlayerShipScreen";
    private static final String DISCOVERY_DATA_OWNER = "net/newworld/navigation/NavigationDiscoverySavedData";
    private static final String DISCOVERY_VALUE_OWNER = "net/newworld/navigation/NavigationDiscoverySavedData$Discovery";
    private static final String DISCOVERY_META_OWNER = "net/newworld/navigation/Navigation0510DiscoveryMeta";

    static {
        add("net/newworld/navigation/Navigation0475RadarFilteX", "scanOneTile(Ljava/lang/Object;)V",
                "net/newworld/navigation/Navigation0581DynamicStructureScanner", "scanOneTile", "(Ljava/lang/Object;)V", Opcodes.RETURN);
        add("net/newworld/navigation/Navigation0475RadarFilteX", "prepareClassification(Ljava/lang/Object;)V",
                "net/newworld/navigation/Navigation0581DynamicStructureScanner", "prepareClassification", "(Ljava/lang/Object;)V", Opcodes.RETURN);
        add("net/newworld/player/PlayerFieldSurvey0503Fix", "scanStructures(Ljava/lang/Object;)V",
                "net/newworld/player/PlayerFieldSurvey0581Fix", "scanStructures", "(Ljava/lang/Object;)V", Opcodes.RETURN);
        add("net/newworld/player/PlayerFieldSurveyRuntime", "handle(Ljava/lang/Object;I)V",
                "net/newworld/player/PlayerFieldSurvey0620Dispatcher", "handle", "(Ljava/lang/Object;I)V", Opcodes.RETURN);
        add("net/newworld/player/PlayerFieldSurvey0504Bridge", "updateClientStatus(I)V",
                "net/newworld/player/PlayerFieldSurvey0620ClientStatus", "update", "(I)V", Opcodes.RETURN);
        add("net/newworld/core/FETierRegistration", "findRegister(Ljava/lang/Class;)Ljava/lang/reflect/Method;",
                "net/newworld/core/FETierRegistration0581Fix", "findRegister", "(Ljava/lang/Class;)Ljava/lang/reflect/Method;", Opcodes.ARETURN);
        add("net/newworld/navigation/NavigationUpgradeRuntime", "scanRange(Ljava/lang/Object;)I",
                "net/newworld/config/NewWorldTuning", "navigationScanRange", "(Ljava/lang/Object;)I", Opcodes.IRETURN);
        add("net/newworld/navigation/NavigationUpgradeRuntime", "tilesPerBatch(Ljava/lang/Object;)I",
                "net/newworld/config/NewWorldTuning", "navigationTilesPerBatch", "(Ljava/lang/Object;)I", Opcodes.IRETURN);
        add("net/newworld/navigation/NavigationUpgradeRuntime", "classificationChunks(Ljava/lang/Object;)I",
                "net/newworld/config/NewWorldTuning", "navigationClassificationChunks", "(Ljava/lang/Object;)I", Opcodes.IRETURN);
        add("net/newworld/navigation/NavigationUpgradeRuntime", "classificationSq(Ljava/lang/Object;)J",
                "net/newworld/config/NewWorldTuning", "navigationClassificationSq", "(Ljava/lang/Object;)J", Opcodes.LRETURN);
        add("net/newworld/navigation/NavigationUpgradeRuntime", "efficiencyPercent(Ljava/lang/Object;)I",
                "net/newworld/config/NewWorldTuning", "navigationEfficiencyPercent", "(Ljava/lang/Object;)I", Opcodes.IRETURN);
        add("net/newworld/navigation/NavigationUpgradeRuntime", "cpuTimeMultiplier(Ljava/lang/Object;)D",
                "net/newworld/config/NewWorldTuning", "navigationCpuTimeMultiplier", "(Ljava/lang/Object;)D", Opcodes.DRETURN);
        add("net/newworld/navigation/NavigationUpgradeRuntime", "cpuPowerMultiplier(Ljava/lang/Object;)D",
                "net/newworld/config/NewWorldTuning", "navigationCpuPowerMultiplier", "(Ljava/lang/Object;)D", Opcodes.DRETURN);
        add("net/newworld/navigation/NavigationWorkloadRuntime", "radarFePerTile(Ljava/lang/Object;)J",
                "net/newworld/config/NewWorldTuning", "navigationRadarFePerTile", "(Ljava/lang/Object;)J", Opcodes.LRETURN);
        add("net/newworld/mining/MiningUpgradeRuntime", "scanProbes(Ljava/lang/Object;Ljava/lang/String;)I",
                "net/newworld/config/NewWorldTuning", "miningScanProbes", "(Ljava/lang/Object;Ljava/lang/String;)I", Opcodes.IRETURN);
        add("net/newworld/mining/MiningUpgradeRuntime", "mineInterval(Ljava/lang/Object;Ljava/lang/String;)J",
                "net/newworld/config/NewWorldTuning", "miningMineInterval", "(Ljava/lang/Object;Ljava/lang/String;)J", Opcodes.LRETURN);
        add("net/newworld/mining/MiningEnergyBalance", "energyCost(Ljava/lang/Object;Ljava/lang/String;)J",
                "net/newworld/config/NewWorldTuning", "miningEnergyCost", "(Ljava/lang/Object;Ljava/lang/String;)J", Opcodes.LRETURN);
        add("net/newworld/mining/MiningEnergyBalance", "efficiencyDisplay(I)J",
                "net/newworld/config/NewWorldTuning", "miningEfficiencyDisplay", "(I)J", Opcodes.LRETURN);
        add("net/newworld/core/FETierSupport", "energyCapacity(II)J",
                "net/newworld/config/NewWorldTuning", "feEnergyCapacity", "(II)J", Opcodes.LRETURN);
        add("net/newworld/core/FETierSupport", "transferLimit(II)J",
                "net/newworld/config/NewWorldTuning", "feTransferLimit", "(II)J", Opcodes.LRETURN);
        add("net/newworld/core/FETierSupport", "providerBonus(II)I",
                "net/newworld/config/NewWorldTuning", "feProviderBonus", "(II)I", Opcodes.IRETURN);
        add("net/newworld/core/MatrixTierSupport", "feWeight(I)I",
                "net/newworld/config/NewWorldTuning", "feTierWeight", "(I)I", Opcodes.IRETURN);
        add("net/newworld/core/MatrixTierSupport", "warpWeight(I)I",
                "net/newworld/config/NewWorldTuning", "warpTierWeight", "(I)I", Opcodes.IRETURN);
        add("net/newworld/core/WarpEnergySystem", "capacity(I)I",
                "net/newworld/config/NewWorldTuning", "warpCapacity", "(I)I", Opcodes.IRETURN);
        add("net/newworld/core/WarpEnergySystem", "fePerWe(I)I",
                "net/newworld/config/NewWorldTuning", "warpFePerWe", "(I)I", Opcodes.IRETURN);
        add("net/newworld/core/WarpEnergySystem", "maxProduction(II)I",
                "net/newworld/config/NewWorldTuning", "warpMaxProduction", "(II)I", Opcodes.IRETURN);
        add("net/newworld/core/ShipRoomRegistry", "warpCapacity(Ljava/lang/String;)I",
                "net/newworld/config/NewWorldTuning", "shipWarpCapacity", "(Ljava/lang/String;)I", Opcodes.IRETURN);
        add("net/newworld/core/ShipRoomRegistry", "maxRange(Ljava/lang/String;)I",
                "net/newworld/config/NewWorldTuning", "shipMaxRange", "(Ljava/lang/String;)I", Opcodes.IRETURN);
        add("net/newworld/navigation/Navigation0540GeologyTimedScan", "geologyEnergyCost(IIIII)J",
                "net/newworld/config/NewWorldTuning", "geologyEnergyCost", "(IIIII)J", Opcodes.LRETURN);
        add("net/newworld/navigation/Navigation0540GeologyTimedScan", "efficiencyPercent(I)I",
                "net/newworld/config/NewWorldTuning", "geologyEfficiencyPercent", "(I)I", Opcodes.IRETURN);
        add("net/newworld/navigation/Navigation0520GeologyClientState",
                "label(Lnet/newworld/navigation/Navigation0520GeologyClientState$Result;)Ljava/lang/String;",
                "net/newworld/navigation/Navigation0630GeologyAnalysis", "clientLabel",
                "(Ljava/lang/Object;)Ljava/lang/String;", Opcodes.ARETURN);
        add("net/newworld/navigation/Navigation0520GeologyClientState",
                "primary(Lnet/newworld/navigation/Navigation0520GeologyClientState$Result;)Ljava/lang/String;",
                "net/newworld/navigation/Navigation0630GeologyAnalysis", "clientPrimary",
                "(Ljava/lang/Object;)Ljava/lang/String;", Opcodes.ARETURN);
        add(GEOLOGY_ANALYSIS_UI_OWNER, "filtered(Ljava/lang/Object;Ljava/util/List;)Ljava/util/List;",
                "net/newworld/navigation/Navigation0630GeologyAnalysis", "clientFiltered",
                "(Ljava/lang/Object;Ljava/util/List;)Ljava/util/List;", Opcodes.ARETURN);
        add(GEOLOGY_ANALYSIS_UI_OWNER, "availableTypes()Ljava/util/List;",
                "net/newworld/navigation/Navigation0630GeologyAnalysis", "clientAvailableTypes",
                "()Ljava/util/List;", Opcodes.ARETURN);
        add(GEOLOGY_ANALYSIS_UI_OWNER, "filterSummary(Ljava/lang/Object;)Ljava/lang/String;",
                "net/newworld/navigation/Navigation0630GeologyAnalysis", "clientFilterSummary",
                "(Ljava/lang/Object;)Ljava/lang/String;", Opcodes.ARETURN);
        add(GEOLOGY_ANALYSIS_UI_OWNER, "labelForType(I)Ljava/lang/String;",
                "net/newworld/navigation/Navigation0630GeologyAnalysis", "clientLabelForType",
                "(I)Ljava/lang/String;", Opcodes.ARETURN);
        add("net/newworld/navigation/Navigation0553GeologySnapshot",
                "sendSnapshot(Ljava/lang/Object;Ljava/util/List;)V",
                "net/newworld/navigation/Navigation0630GeologyAnalysis", "sendSnapshot",
                "(Ljava/lang/Object;Ljava/util/List;)V", Opcodes.RETURN);
        add("net/newworld/network/UpgradeValues", "transferLimit(II)I",
                "net/newworld/config/NewWorldTuning", "networkNodeTransferLimit", "(II)I", Opcodes.IRETURN);
        add("net/newworld/network/UpgradeValues", "capacityLimit(II)I",
                "net/newworld/config/NewWorldTuning", "networkNodeCapacityLimit", "(II)I", Opcodes.IRETURN);
    }

    private NewWorldClassPatcher() {}

    public static void main(String[] args) throws Exception {
        if (args.length != 2) throw new IllegalArgumentException("Usage: NewWorldClassPatcher <baseline.jar> <payload-directory>");
        Path baseline = Path.of(args[0]);
        Path payload = Path.of(args[1]);
        Set<String> classNames = new HashSet<>(TARGETS.keySet());
        classNames.add(RADAR_UI_OWNER);
        classNames.add(ROOM_PROTECTION_OWNER);
        classNames.add(EMERGENCY_POLICY_OWNER);
        classNames.add(REPLICATION_OWNER);
        classNames.add(GEOLOGY_UI_OWNER);
        classNames.add(TRUE_SINGLE_GEOLOGY_UI_OWNER);
        classNames.add(PLAYER_GUI_OWNER);
        classNames.add(DISCOVERY_DATA_OWNER);
        classNames.add(DISCOVERY_VALUE_OWNER);
        classNames.add(DISCOVERY_META_OWNER);
        classNames.add(GEOLOGY_SCAN_OWNER);
        try (JarFile jar = new JarFile(baseline.toFile())) {
            for (String className : classNames) {
                String entryName = className + ".class";
                var entry = jar.getJarEntry(entryName);
                if (entry == null) throw new IOException("Missing baseline class: " + entryName);
                byte[] original;
                try (InputStream input = jar.getInputStream(entry)) {
                    original = input.readAllBytes();
                }
                byte[] patched = patch(className, original);
                Path output = payload.resolve(entryName.replace('/', java.io.File.separatorChar));
                Files.createDirectories(output.getParent());
                Files.write(output, patched);
            }
        }
    }

    private static byte[] patch(String className, byte[] original) {
        ClassNode node = new ClassNode(Opcodes.ASM9);
        new ClassReader(original).accept(node, 0);
        Map<String, Target> classTargets = TARGETS.getOrDefault(className, Map.of());
        int replacements = 0;
        for (MethodNode method : node.methods) {
            Target target = classTargets.get(method.name + method.desc);
            if (target == null) continue;
            InsnList code = new InsnList();
            int local = (method.access & Opcodes.ACC_STATIC) == 0 ? 1 : 0;
            if ((method.access & Opcodes.ACC_STATIC) == 0) code.add(new VarInsnNode(Opcodes.ALOAD, 0));
            for (Type argument : Type.getArgumentTypes(method.desc)) {
                code.add(new VarInsnNode(argument.getOpcode(Opcodes.ILOAD), local));
                local += argument.getSize();
            }
            code.add(new MethodInsnNode(Opcodes.INVOKESTATIC, target.helperOwner, target.helperName,
                    target.helperDescriptor, false));
            code.add(new InsnNode(target.returnOpcode));
            method.instructions = code;
            method.tryCatchBlocks.clear();
            if (method.localVariables != null) method.localVariables.clear();
            method.maxStack = Math.max(2, local);
            method.maxLocals = Math.max(method.maxLocals, local);
            replacements++;
        }
        int expected = classTargets.size();
        if (RADAR_UI_OWNER.equals(className)) {
            replacements += wrapRadarFilterOverlay(node);
            expected++;
        }
        if (ROOM_PROTECTION_OWNER.equals(className)) {
            replacements += wrapConfigMethod(node, "isProtected",
                    "(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z",
                    "isProtectedConfigBase", "roomProtected", "(Ljava/lang/Object;Ljava/lang/Object;)Z", Opcodes.IRETURN);
            expected++;
        }
        if (EMERGENCY_POLICY_OWNER.equals(className)) {
            replacements += wrapConfigMethod(node, "limit", "(Ljava/lang/Object;I)I",
                    "limitConfigBase", "networkEmergencyLimit", "(Ljava/lang/Object;I)I", Opcodes.IRETURN);
            expected++;
        }
        if (REPLICATION_OWNER.equals(className)) {
            replacements += patchReplicationConstants(node);
            expected += 2;
        }
        if (NAVIGATION_UPGRADE_OWNER.equals(className)) {
            replacements += wrapConfigMethod(node, "scanBatch", "(Ljava/lang/Object;)V",
                    "scanBatchConfigBase", "navigationScanBatch", "(Ljava/lang/Object;)V", Opcodes.RETURN);
            expected++;
        }
        if (GEOLOGY_UI_OWNER.equals(className)) {
            replacements += wrapGeologyFilterOverlay(node);
            expected++;
        }
        if (TRUE_SINGLE_GEOLOGY_UI_OWNER.equals(className)) {
            replacements += wrapTrueSingleGeologyFilterOverlay(node);
            expected++;
        }
        if (GEOLOGY_ANALYSIS_UI_OWNER.equals(className)) {
            replacements += patchGeologyLockedFilterMessage(node);
            expected++;
        }
        if (GEOLOGY_TIMED_OWNER.equals(className)) {
            replacements += wrapGeologyTimedRun(node);
            expected++;
        }
        if (GEOLOGY_SCAN_OWNER.equals(className)) {
            replacements += wrapGeologyScanRecord(node);
            replacements += wrapGeologyScanSend(node);
            expected += 2;
        }
        if (GEOLOGY_CLIENT_STATE_OWNER.equals(className)) {
            replacements += wrapGeologyClientAccept(node);
            expected++;
        }
        if (PLAYER_GUI_OWNER.equals(className)) {
            replacements += patchPlayerGui(node);
            replacements += wrapPlayerMouseClicked(node);
            expected += 8;
        }
        if (DISCOVERY_DATA_OWNER.equals(className)) {
            replacements += wrapDiscoveryRecord(node);
            expected++;
        }
        if (DISCOVERY_VALUE_OWNER.equals(className)) {
            replacements += addDiscoveryFields(node);
            expected += 2;
        }
        if (DISCOVERY_META_OWNER.equals(className)) {
            replacements += wrapDiscoveryMetaPost(node, "loadMeta",
                    "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)V", "loadMeta0610Base", "afterLoad");
            replacements += wrapDiscoveryMetaPost(node, "saveMeta",
                    "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)V", "saveMeta0610Base", "afterSave");
            replacements += wrapDiscoveryMetaPost(node, "saveSchema",
                    "(Ljava/lang/Object;)V", "saveSchema0610Base", "afterSaveSchema");
            expected += 3;
        }
        if (replacements != expected) {
            throw new IllegalStateException("Expected " + expected + " replacements in " + className
                    + " but applied " + replacements);
        }
        ClassWriter writer = new ClassWriter(0);
        node.accept(writer);
        return writer.toByteArray();
    }

    private static int wrapRadarFilterOverlay(ClassNode node) {
        MethodNode original = null;
        for (MethodNode method : node.methods) {
            if (RADAR_OVERLAY_NAME.equals(method.name) && RADAR_OVERLAY_DESC.equals(method.desc)) {
                original = method;
                break;
            }
        }
        if (original == null) return 0;

        original.name = RADAR_OVERLAY_BASE_NAME;
        String[] exceptions = original.exceptions == null ? null : original.exceptions.toArray(new String[0]);
        MethodNode wrapper = new MethodNode(original.access, RADAR_OVERLAY_NAME, RADAR_OVERLAY_DESC,
                original.signature, exceptions);
        wrapper.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        wrapper.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
        wrapper.instructions.add(new VarInsnNode(Opcodes.ILOAD, 2));
        wrapper.instructions.add(new VarInsnNode(Opcodes.ILOAD, 3));
        wrapper.instructions.add(new VarInsnNode(Opcodes.ALOAD, 4));
        wrapper.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                "net/newworld/navigation/Navigation0592RadarFilterOverlayFix", "render",
                "(Ljava/lang/Object;Ljava/lang/Object;IILjava/lang/Object;)V", false));
        wrapper.instructions.add(new InsnNode(Opcodes.RETURN));
        wrapper.maxStack = 5;
        wrapper.maxLocals = 5;
        node.methods.add(wrapper);
        return 1;
    }

    private static int wrapGeologyFilterOverlay(ClassNode node) {
        String descriptor = "(Ljava/lang/Object;Ljava/lang/Object;)V";
        MethodNode original = null;
        for (MethodNode method : node.methods) {
            if ("drawExistingPopup".equals(method.name) && descriptor.equals(method.desc)) {
                original = method;
                break;
            }
        }
        if (original == null) return 0;

        original.name = "drawExistingPopup0601Base";
        String[] exceptions = original.exceptions == null ? null : original.exceptions.toArray(new String[0]);
        MethodNode wrapper = new MethodNode(original.access, "drawExistingPopup", descriptor,
                original.signature, exceptions);
        wrapper.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        wrapper.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
        wrapper.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                "net/newworld/navigation/Navigation0601GeologyFilterOverlayFix", "render", descriptor, false));
        wrapper.instructions.add(new InsnNode(Opcodes.RETURN));
        wrapper.maxStack = 2;
        wrapper.maxLocals = 2;
        node.methods.add(wrapper);
        return 1;
    }

    private static int wrapTrueSingleGeologyFilterOverlay(ClassNode node) {
        String descriptor = "(Ljava/lang/Object;Ljava/lang/Object;)V";
        MethodNode original = find(node, "drawFilterPopup", descriptor);
        if (original == null) return 0;
        original.name = "drawFilterPopup0632Base";
        MethodNode wrapper = wrapper(original, "drawFilterPopup");
        wrapper.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        wrapper.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
        wrapper.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                "net/newworld/navigation/Navigation0601GeologyFilterOverlayFix", "renderTrueSingle",
                descriptor, false));
        wrapper.instructions.add(new InsnNode(Opcodes.RETURN));
        wrapper.maxStack = 2;
        wrapper.maxLocals = 2;
        node.methods.add(wrapper);
        return 1;
    }

    private static int wrapGeologyTimedRun(ClassNode node) {
        String descriptor = "(Lnet/newworld/navigation/Navigation0540GeologyTimedScan$Job;)V";
        MethodNode original = null;
        for (MethodNode method : node.methods) {
            if ("runImmediate".equals(method.name) && descriptor.equals(method.desc)) {
                original = method;
                break;
            }
        }
        if (original == null) return 0;

        original.name = "runImmediate0630Base";
        String[] exceptions = original.exceptions == null ? null : original.exceptions.toArray(new String[0]);
        MethodNode wrapper = new MethodNode(original.access, "runImmediate", descriptor,
                original.signature, exceptions);
        wrapper.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        wrapper.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                "net/newworld/navigation/Navigation0630GeologyAnalysis", "runImmediate",
                "(Ljava/lang/Object;)V", false));
        wrapper.instructions.add(new InsnNode(Opcodes.RETURN));
        wrapper.maxStack = 1;
        wrapper.maxLocals = 1;
        node.methods.add(wrapper);
        return 1;
    }

    private static int patchGeologyLockedFilterMessage(ClassNode node) {
        int replacements = 0;
        for (MethodNode method : node.methods) {
            for (AbstractInsnNode instruction = method.instructions.getFirst(); instruction != null; ) {
                AbstractInsnNode next = instruction.getNext();
                if (instruction instanceof LdcInsnNode constant
                        && "NO DEPOSIT FAMILIES IN CURRENT SCAN".equals(constant.cst)) {
                    method.instructions.set(instruction, new MethodInsnNode(Opcodes.INVOKESTATIC,
                            "net/newworld/navigation/Navigation0630GeologyAnalysis", "lockedFilterMessage",
                            "()Ljava/lang/String;", false));
                    replacements++;
                }
                instruction = next;
            }
        }
        return replacements;
    }

    private static int wrapGeologyScanRecord(ClassNode node) {
        String descriptor = "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;Lnet/newworld/navigation/Navigation0520GeologyScanRuntime$Hit;)V";
        MethodNode original = find(node, "record", descriptor);
        if (original == null) return 0;
        original.name = "record0631Base";
        MethodNode wrapper = wrapper(original, "record");
        wrapper.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        wrapper.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
        wrapper.instructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
        wrapper.instructions.add(new VarInsnNode(Opcodes.ALOAD, 3));
        wrapper.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                "net/newworld/navigation/Navigation0630GeologyAnalysis", "captureScanRecord",
                "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;)V", false));
        wrapper.instructions.add(new InsnNode(Opcodes.RETURN));
        wrapper.maxStack = 4;
        wrapper.maxLocals = 4;
        node.methods.add(wrapper);
        return 1;
    }

    private static int wrapGeologyScanSend(ClassNode node) {
        String descriptor = "(Ljava/lang/Object;I)V";
        MethodNode original = find(node, "send", descriptor);
        if (original == null) return 0;
        original.name = "send0631Base";
        MethodNode wrapper = wrapper(original, "send");
        wrapper.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        wrapper.instructions.add(new VarInsnNode(Opcodes.ILOAD, 1));
        wrapper.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                "net/newworld/navigation/Navigation0630GeologyAnalysis", "sendScanPacket",
                descriptor, false));
        wrapper.instructions.add(new InsnNode(Opcodes.RETURN));
        wrapper.maxStack = 2;
        wrapper.maxLocals = 2;
        node.methods.add(wrapper);
        return 1;
    }

    private static int wrapGeologyClientAccept(ClassNode node) {
        String descriptor = "(I)V";
        MethodNode original = find(node, "acceptLegacy", descriptor);
        if (original == null) return 0;
        original.name = "acceptLegacy0631Base";
        MethodNode wrapper = wrapper(original, "acceptLegacy");
        wrapper.instructions.add(new VarInsnNode(Opcodes.ILOAD, 0));
        wrapper.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                "net/newworld/navigation/Navigation0630GeologyAnalysis", "acceptLegacy",
                descriptor, false));
        wrapper.instructions.add(new InsnNode(Opcodes.RETURN));
        wrapper.maxStack = 1;
        wrapper.maxLocals = 1;
        node.methods.add(wrapper);
        return 1;
    }

    private static MethodNode find(ClassNode node, String name, String descriptor) {
        for (MethodNode method : node.methods) {
            if (name.equals(method.name) && descriptor.equals(method.desc)) return method;
        }
        return null;
    }

    private static MethodNode wrapper(MethodNode original, String name) {
        String[] exceptions = original.exceptions == null ? null : original.exceptions.toArray(new String[0]);
        return new MethodNode(original.access, name, original.desc, original.signature, exceptions);
    }

    private static int wrapConfigMethod(ClassNode node, String methodName, String descriptor, String baseName,
                                        String helperName, String helperDescriptor, int returnOpcode) {
        MethodNode original = null;
        for (MethodNode method : node.methods) {
            if (methodName.equals(method.name) && descriptor.equals(method.desc)) { original = method; break; }
        }
        if (original == null) return 0;
        original.name = baseName;
        String[] exceptions = original.exceptions == null ? null : original.exceptions.toArray(new String[0]);
        MethodNode wrapper = new MethodNode(original.access, methodName, descriptor, original.signature, exceptions);
        int local = 0;
        for (Type argument : Type.getArgumentTypes(descriptor)) {
            wrapper.instructions.add(new VarInsnNode(argument.getOpcode(Opcodes.ILOAD), local));
            local += argument.getSize();
        }
        wrapper.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                "net/newworld/config/NewWorldTuning", helperName, helperDescriptor, false));
        wrapper.instructions.add(new InsnNode(returnOpcode));
        wrapper.maxStack = Math.max(2, local);
        wrapper.maxLocals = local;
        node.methods.add(wrapper);
        return 1;
    }

    private static int patchReplicationConstants(ClassNode node) {
        MethodNode process = null;
        for (MethodNode method : node.methods) {
            if ("process".equals(method.name) && "(Ljava/lang/Object;Ljava/lang/Object;)V".equals(method.desc)) {
                process = method;
                break;
            }
        }
        if (process == null) return 0;
        int replacements = 0;
        for (AbstractInsnNode instruction = process.instructions.getFirst(); instruction != null; ) {
            AbstractInsnNode next = instruction.getNext();
            if (instruction instanceof LdcInsnNode constant && constant.cst instanceof Long value) {
                String helper = value == 5L ? "replicationIntervalTicks" : (value == 64L ? "replicationBatchSize" : null);
                if (helper != null) {
                    process.instructions.set(instruction, new MethodInsnNode(Opcodes.INVOKESTATIC,
                            "net/newworld/config/NewWorldTuning", helper, "()J", false));
                    replacements++;
                }
            }
            instruction = next;
        }
        return replacements;
    }

    private static int patchPlayerGui(ClassNode node) {
        int replacements = 0;
        for (MethodNode method : node.methods) {
            for (AbstractInsnNode instruction = method.instructions.getFirst(); instruction != null; ) {
                AbstractInsnNode next = instruction.getNext();
                if (instruction instanceof LdcInsnNode constant) {
                    if (constant.cst instanceof String text && "STRUCTURE RANGE: 96 blocks".equals(text)) {
                        method.instructions.set(instruction, new MethodInsnNode(Opcodes.INVOKESTATIC,
                                "net/newworld/config/NewWorldTuning", "playerSurveyDetailLine",
                                "()Ljava/lang/String;", false));
                        replacements++;
                    } else if (constant.cst instanceof String text && "OFFLINE // next phase".equals(text)) {
                        constant.cst = "Identify physical deposits nearby";
                        replacements++;
                    } else if (constant.cst instanceof String text && "Geological survey is not online yet.".equals(text)) {
                        constant.cst = "SCANNING // Geological survey in progress...";
                        replacements++;
                    } else if (constant.cst instanceof Integer value && value == -1342177280) {
                        method.instructions.set(instruction, new MethodInsnNode(Opcodes.INVOKESTATIC,
                                "net/newworld/config/NewWorldTuning", "guiPlayerBackdropArgb", "()I", false));
                        replacements++;
                    } else if (constant.cst instanceof Integer value && value == -15065053) {
                        constant.cst = -15251110;
                        replacements++;
                    } else if (constant.cst instanceof Integer value && value == -9735048) {
                        constant.cst = -1;
                        replacements++;
                    } else if (constant.cst instanceof Integer value && value == -11511202) {
                        constant.cst = -4264969;
                        replacements++;
                    }
                }
                instruction = next;
            }
        }
        return replacements;
    }

    private static int wrapPlayerMouseClicked(ClassNode node) {
        String descriptor = "(DDI)Z";
        MethodNode original = null;
        for (MethodNode method : node.methods) {
            if ("mouseClicked".equals(method.name) && descriptor.equals(method.desc)) {
                original = method;
                break;
            }
        }
        if (original == null) return 0;

        original.name = "mouseClicked0620Base";
        String[] exceptions = original.exceptions == null ? null : original.exceptions.toArray(new String[0]);
        MethodNode wrapper = new MethodNode(original.access, "mouseClicked", descriptor,
                original.signature, exceptions);
        wrapper.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        wrapper.instructions.add(new VarInsnNode(Opcodes.DLOAD, 1));
        wrapper.instructions.add(new VarInsnNode(Opcodes.DLOAD, 3));
        wrapper.instructions.add(new VarInsnNode(Opcodes.ILOAD, 5));
        wrapper.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                "net/newworld/player/PlayerGeologicalSurveyGui0620", "mouseClicked",
                "(Ljava/lang/Object;DDI)Z", false));
        wrapper.instructions.add(new InsnNode(Opcodes.IRETURN));
        wrapper.maxStack = 6;
        wrapper.maxLocals = 6;
        node.methods.add(wrapper);
        return 1;
    }

    private static int wrapDiscoveryRecord(ClassNode node) {
        String descriptor = "(Ljava/lang/String;Lnet/newworld/navigation/NavigationDiscoverySavedData$Discovery;)V";
        MethodNode original = null;
        for (MethodNode method : node.methods) {
            if ("record".equals(method.name) && descriptor.equals(method.desc)) { original = method; break; }
        }
        if (original == null) return 0;

        original.name = "record0610Base";
        String[] exceptions = original.exceptions == null ? null : original.exceptions.toArray(new String[0]);
        MethodNode wrapper = new MethodNode(original.access, "record", descriptor, original.signature, exceptions);
        wrapper.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        wrapper.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
        wrapper.instructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
        wrapper.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                "net/newworld/navigation/Navigation0610DiscoveryRuntime", "record",
                "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)V", false));
        wrapper.instructions.add(new InsnNode(Opcodes.RETURN));
        wrapper.maxStack = 3;
        wrapper.maxLocals = 3;
        node.methods.add(wrapper);
        return 1;
    }

    private static int addDiscoveryFields(ClassNode node) {
        int added = 0;
        if (node.fields.stream().noneMatch(field -> "analysisLevel".equals(field.name))) {
            node.fields.add(new FieldNode(Opcodes.ACC_PUBLIC, "analysisLevel", "I", null, null));
            added++;
        }
        if (node.fields.stream().noneMatch(field -> "lastSeenAt".equals(field.name))) {
            node.fields.add(new FieldNode(Opcodes.ACC_PUBLIC, "lastSeenAt", "J", null, null));
            added++;
        }
        return added;
    }

    private static int wrapDiscoveryMetaPost(ClassNode node, String methodName, String descriptor,
                                              String baseName, String helperName) {
        MethodNode original = null;
        for (MethodNode method : node.methods) {
            if (methodName.equals(method.name) && descriptor.equals(method.desc)) { original = method; break; }
        }
        if (original == null) return 0;

        original.name = baseName;
        String[] exceptions = original.exceptions == null ? null : original.exceptions.toArray(new String[0]);
        MethodNode wrapper = new MethodNode(original.access, methodName, descriptor, original.signature, exceptions);
        int local = 0;
        for (Type argument : Type.getArgumentTypes(descriptor)) {
            wrapper.instructions.add(new VarInsnNode(argument.getOpcode(Opcodes.ILOAD), local));
            local += argument.getSize();
        }
        wrapper.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, DISCOVERY_META_OWNER,
                baseName, descriptor, false));
        local = 0;
        for (Type argument : Type.getArgumentTypes(descriptor)) {
            wrapper.instructions.add(new VarInsnNode(argument.getOpcode(Opcodes.ILOAD), local));
            local += argument.getSize();
        }
        wrapper.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                "net/newworld/navigation/Navigation0610DiscoveryRuntime", helperName, descriptor, false));
        wrapper.instructions.add(new InsnNode(Opcodes.RETURN));
        wrapper.maxStack = Math.max(3, local);
        wrapper.maxLocals = local;
        node.methods.add(wrapper);
        return 1;
    }

    private static void add(String owner, String methodKey, String helperOwner, String helperName,
                            String helperDescriptor, int returnOpcode) {
        TARGETS.computeIfAbsent(owner, unused -> new HashMap<>())
                .put(methodKey, new Target(helperOwner, helperName, helperDescriptor, returnOpcode));
    }
}
