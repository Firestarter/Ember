package xyz.nkomarn.Ember.command;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.util.MessagePosition;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import org.checkerframework.checker.nullness.qual.NonNull;
import xyz.nkomarn.Ember.listener.ChatListener;

public class StaffCommand implements Command {
    @Override
    public void execute(@NonNull CommandSource sender, @NonNull String[] args) {
        if (sender instanceof Player && sender.hasPermission("channel.staff")) {
            Player player = (Player) sender;
            if (ChatListener.TOGGLED_PLAYERS.contains(player.getUniqueId())) {
                ChatListener.TOGGLED_PLAYERS.remove(player.getUniqueId());
                player.sendMessage(TextComponent.builder("Toggled out of staff chat.")
                        .color(TextColor.LIGHT_PURPLE).build(), MessagePosition.ACTION_BAR);
            } else {
                ChatListener.TOGGLED_PLAYERS.add(player.getUniqueId());
                player.sendMessage(TextComponent.builder("Toggled into staff chat.")
                        .color(TextColor.LIGHT_PURPLE).build(), MessagePosition.ACTION_BAR);
            }
        }
    }
}
