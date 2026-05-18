package me.autobot.easyUpdateSuppressor.Suppressor;

import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.BiConsumer;

public class SpigotSuppressor implements ISuppressor {

    private final Method getHandle;
    private final Method level;
    private final Field neighborUpdater;
    private final Field update;
    public SpigotSuppressor() throws ClassNotFoundException, NoSuchMethodException, NoSuchFieldException {
        Class<?> clazz;
        clazz = Class.forName("org.bukkit.craftbukkit.entity.CraftEntity");
        getHandle = clazz.getMethod("getHandle");

        clazz = Class.forName("net.minecraft.world.entity.Entity");
        level = clazz.getMethod("level");

        clazz = Class.forName("net.minecraft.world.level.Level");
        neighborUpdater = clazz.getDeclaredField("neighborUpdater");
        neighborUpdater.setAccessible(true);

        clazz = Class.forName("net.minecraft.world.level.redstone.CollectingNeighborUpdater");
        update = clazz.getDeclaredField("count");
        update.setAccessible(true);
    }

    @Override
    public void Suppress(Player p, BiConsumer<Field, Object> consumer) {
        try {
            Object craftEntity = getHandle.invoke(p);
            Object serverLevel = level.invoke(craftEntity);
            Object updater = neighborUpdater.get(serverLevel);
            consumer.accept(update, updater);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
