package net.newworld.core;

import java.lang.reflect.Method;
import java.util.function.Supplier;

/** Selects the ResourceLocation overload deterministically. */
public final class FETierRegistration0581Fix {
    private FETierRegistration0581Fix() {}

    public static Method findRegister(Class<?> registryClass) {
        try {
            Class<?> resourceLocation = Class.forName("net.minecraft.resources.ResourceLocation");
            for (Method method : registryClass.getMethods()) {
                Class<?>[] parameters = method.getParameterTypes();
                if (!method.getName().equals("register") || parameters.length != 2) continue;
                if (parameters[0] == resourceLocation && Supplier.class.isAssignableFrom(parameters[1])) {
                    method.setAccessible(true);
                    return method;
                }
            }
            throw new IllegalStateException("DeferredRegister.register(ResourceLocation, Supplier) was not found on "
                    + registryClass.getName());
        } catch (ClassNotFoundException failure) {
            throw new IllegalStateException("Minecraft ResourceLocation class was not available", failure);
        }
    }
}
