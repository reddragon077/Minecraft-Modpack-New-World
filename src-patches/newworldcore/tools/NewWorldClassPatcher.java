import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
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
        try (JarFile jar = new JarFile(baseline.toFile())) {
            for (String className : TARGETS.keySet()) {
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
        Map<String, Target> classTargets = TARGETS.get(className);
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
        if (replacements != classTargets.size()) {
            throw new IllegalStateException("Expected " + classTargets.size() + " replacements in " + className
                    + " but applied " + replacements);
        }
        ClassWriter writer = new ClassWriter(0);
        node.accept(writer);
        return writer.toByteArray();
    }

    private static void add(String owner, String methodKey, String helperOwner, String helperName,
                            String helperDescriptor, int returnOpcode) {
        TARGETS.computeIfAbsent(owner, unused -> new HashMap<>())
                .put(methodKey, new Target(helperOwner, helperName, helperDescriptor, returnOpcode));
    }
}
