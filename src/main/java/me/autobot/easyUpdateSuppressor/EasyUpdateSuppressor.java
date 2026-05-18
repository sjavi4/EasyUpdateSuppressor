package me.autobot.easyUpdateSuppressor;

import me.autobot.easyUpdateSuppressor.Command.ToggleSuppression;
import me.autobot.easyUpdateSuppressor.Event.OnBlockBreak;
import me.autobot.easyUpdateSuppressor.Event.OnBlockPlace;
import me.autobot.easyUpdateSuppressor.Scheduler.FoliaScheduler;
import me.autobot.easyUpdateSuppressor.Scheduler.IScheduler;
import me.autobot.easyUpdateSuppressor.Scheduler.SpigotScheduler;
import me.autobot.easyUpdateSuppressor.Suppressor.FoliaSuppressor;
import me.autobot.easyUpdateSuppressor.Suppressor.ISuppressor;
import me.autobot.easyUpdateSuppressor.Suppressor.SpigotSuppressor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;

public final class EasyUpdateSuppressor extends JavaPlugin {

    public static EasyUpdateSuppressor instance;

    public ISuppressor suppressor;
    public IScheduler scheduler;

    @Override
    public void onEnable() {
        instance = this;
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new OnBlockBreak(), this);
        pluginManager.registerEvents(new OnBlockPlace(), this);
        getCommand("togglesuppression").setExecutor(new ToggleSuppression());

        try {
            if (isFolia()) {
                suppressor = new FoliaSuppressor();
                scheduler = new FoliaScheduler();
            }
            else {
                suppressor = new SpigotSuppressor();
                scheduler = new SpigotScheduler();
            }
        }
        catch (NoSuchFieldException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private static boolean isFolia() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
