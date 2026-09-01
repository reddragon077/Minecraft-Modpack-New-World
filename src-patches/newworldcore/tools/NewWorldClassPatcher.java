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
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
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

    static {
        add("net/newworld/navigation/Navigation0475RadarFilteX", "scanOneTile(Ljava/lang/Object;)V",
                "net/newworld/navigation/Navigation0581DynamicStructureScanner", "scanOneTile", "(Ljava/lang/Object;)V", Opcodes.RETURN);
        add("net/newworld/navigation/Navigation0475RadarFilteX", "prepareClassification(Ljava/lang/Object;)V",
                "net/newworld/navigation/Navigation0581DynamicStructureScanner", "prepareClassification", "(Ljava/lang/Object;)V", Opcodes.RETURN);
        add("net/newworld/player/PlayerFieldSurvey0503Fix", "scanStructures(Ljava/lang/Object;)V",
                "net/newworld/player/PlayerFieldSurvey0581Fix", "scanStructures", "(Ljava/lang/Object;)V", Opcodes.RETURN);
        add("net/newworld/core/FETierRegistration", "findRegister(Ljava/lang/Class;)Ljava/lang/reflect/Method;",
                "net/newworld/core/FETierRegistration0581Fix", "findRegister", "(Ljava/lang/Class;)Ljava/lang/reflect/Method;", Opcodes.ARETURN);
    }

    private NewWorldClassPatcher() {}

    public static void main(String[] args) throws Exception {
        if (args.length != 2) throw new IllegalArgumentException("Usage: NewWorldClassPatcher <baseline.jar> <payload-directory>");
        Path baseline = Path.of(args[0]);
        Path payload = Path.of(args[1]);
        Set<String> classNames = new HashSet<>(TARGETS.keySet());
        classNames.add(RADAR_UI_OWNER);
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
            code.add(new VarInsnNode(Opcodes.ALOAD, 0));
            code.add(new MethodInsnNode(Opcodes.INVOKESTATIC, target.helperOwner, target.helperName,
                    target.helperDescriptor, false));
            code.add(new InsnNode(target.returnOpcode));
            method.instructions = code;
            method.tryCatchBlocks.clear();
            if (method.localVariables != null) method.localVariables.clear();
            method.maxStack = 1;
            method.maxLocals = 1;
            replacements++;
        }
        int expected = classTargets.size();
        if (RADAR_UI_OWNER.equals(className)) {
            replacements += wrapRadarFilterOverlay(node);
            expected++;
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

    private static void add(String owner, String methodKey, String helperOwner, String helperName,
                            String helperDescriptor, int returnOpcode) {
        TARGETS.computeIfAbsent(owner, unused -> new HashMap<>())
                .put(methodKey, new Target(helperOwner, helperName, helperDescriptor, returnOpcode));
    }
}
