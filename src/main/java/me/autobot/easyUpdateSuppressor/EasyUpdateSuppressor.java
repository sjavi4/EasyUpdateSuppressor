package me.autobot.easyUpdateSuppressor;

import me.autobot.easyUpdateSuppressor.Command.ToggleSuppression;
import me.autobot.easyUpdateSuppressor.Event.OnBlockBreak;
import me.autobot.easyUpdateSuppressor.Event.OnBlockPlace;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class EasyUpdateSuppressor extends JavaPlugin {
    public static Method getHandle;
    public static Method level;
    public static Field neighborUpdater;
    public static Field update;

    public static JavaPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new OnBlockBreak(), this);
        pluginManager.registerEvents(new OnBlockPlace(), this);
        getCommand("togglesuppression").setExecutor(new ToggleSuppression());

        try {
            GetNMS();
        } catch (NoSuchFieldException | ClassNotFoundException | NoSuchMethodException e) {
            getLogger().severe(e.getLocalizedMessage());
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    void GetNMS() throws ClassNotFoundException, NoSuchMethodException, NoSuchFieldException {
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

}
