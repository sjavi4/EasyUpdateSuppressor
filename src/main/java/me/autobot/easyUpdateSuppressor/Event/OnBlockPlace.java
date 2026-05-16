package me.autobot.easyUpdateSuppressor.Event;

import me.autobot.easyUpdateSuppressor.Command.ToggleSuppression;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.lang.reflect.InvocationTargetException;

import static me.autobot.easyUpdateSuppressor.EasyUpdateSuppressor.*;

public class OnBlockPlace implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void OnPlayerBlockPlace(BlockPlaceEvent event) throws InvocationTargetException, IllegalAccessException {
        if (event.isCancelled())
            return;

        if (!ToggleSuppression.TOGGLE.containsKey(event.getPlayer().getUniqueId()))
            return;

        if (!ToggleSuppression.TOGGLE.get(event.getPlayer().getUniqueId()))
            return;

        Object craftEntity = getHandle.invoke(event.getPlayer());
        Object serverLevel = level.invoke(craftEntity);
        Object updater = neighborUpdater.get(serverLevel);
        update.set(updater, Integer.MAX_VALUE - 10000);
        Bukkit.getScheduler().scheduleSyncDelayedTask(instance, ()->{
            try {
                update.set(updater, 0);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
        instance.getLogger().warning(String.format("Player %s (%s) Placed Block via Update Suppression.", event.getPlayer().getName(), event.getPlayer().getUniqueId()));
    }
}
