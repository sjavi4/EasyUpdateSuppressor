package me.autobot.easyUpdateSuppressor.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class ToggleSuppression implements CommandExecutor {

    public static final HashMap<UUID, Boolean> TOGGLE = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (sender instanceof Player player) {
            UUID uuid = player.getUniqueId();
            if (TOGGLE.containsKey(uuid)) {
                TOGGLE.put(uuid, !TOGGLE.get(uuid));
            }
            else {
                TOGGLE.put(uuid,true);
            }
            player.sendMessage(String.format("Suppression: %s", TOGGLE.get(uuid) ? "ON" : "OFF"));
        }
        return true;
    }
}
