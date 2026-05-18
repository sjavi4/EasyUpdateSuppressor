package me.autobot.easyUpdateSuppressor.Scheduler;

import me.autobot.easyUpdateSuppressor.EasyUpdateSuppressor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Consumer;

public class FoliaScheduler implements IScheduler {

    private final Object regionScheduler;
    private final Method execute;

    public FoliaScheduler() throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        Method getRegionScheduler = Bukkit.getServer().getClass().getDeclaredMethod("getRegionScheduler");
        getRegionScheduler.setAccessible(true);
        regionScheduler = getRegionScheduler.invoke(Bukkit.getServer());

        Class<?> clazz = Class.forName("io.papermc.paper.threadedregions.scheduler.RegionScheduler");
        execute = clazz.getDeclaredMethod("runDelayed", Plugin.class, Location.class, Consumer.class, long.class);
        execute.setAccessible(true);
    }

    @Override
    public void RunTaskOnSameTick(Runnable r, Location l) {
        try {
            Consumer<?> consumer = (c)->r.run();
            execute.invoke(regionScheduler, EasyUpdateSuppressor.instance, l, consumer, 1L); // Caution 1 Tick
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
