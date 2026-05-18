package me.autobot.easyUpdateSuppressor.Scheduler;

import org.bukkit.Location;

public interface IScheduler {

    void RunTaskOnSameTick(Runnable r, Location l);

}
