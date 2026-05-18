package me.autobot.easyUpdateSuppressor.Event;

import me.autobot.easyUpdateSuppressor.Command.ToggleSuppression;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import static me.autobot.easyUpdateSuppressor.EasyUpdateSuppressor.*;

public class OnBlockPlace implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void OnPlayerBlockPlace(BlockPlaceEvent event) {
        if (event.isCancelled())
            return;

        if (!ToggleSuppression.TOGGLE.containsKey(event.getPlayer().getUniqueId()))
            return;

        if (!ToggleSuppression.TOGGLE.get(event.getPlayer().getUniqueId()))
            return;

        instance.suppressor.Suppress(event.getPlayer(), (update, updator) -> {
            try {
                update.set(updator, Integer.MAX_VALUE >> 1);
                instance.scheduler.RunTaskOnSameTick(()->{
                    try {
                        update.set(updator, 0);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }, event.getPlayer().getLocation());
                instance.getLogger().warning(String.format("Player %s (%s) Placed Block via Update Suppression.", event.getPlayer().getName(), event.getPlayer().getUniqueId()));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
