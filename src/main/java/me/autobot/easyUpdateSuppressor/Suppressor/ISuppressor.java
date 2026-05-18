package me.autobot.easyUpdateSuppressor.Suppressor;

import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.function.BiConsumer;

public interface ISuppressor {

    void Suppress(Player p, BiConsumer<Field, Object> consumer);
}
