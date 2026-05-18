package me.autobot.easyUpdateSuppressor.Scheduler;

import me.autobot.easyUpdateSuppressor.EasyUpdateSuppressor;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class SpigotScheduler implements IScheduler {
    @Override
    public void RunTaskOnSameTick(Runnable r, Location l) {
        Bukkit.getScheduler().runTaskLater(EasyUpdateSuppressor.instance, r, 0L);
    }
}
