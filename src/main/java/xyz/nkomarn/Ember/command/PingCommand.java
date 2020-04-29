package xyz.nkomarn.Ember.command;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;
import xyz.nkomarn.Ember.Ember;
import xyz.nkomarn.Ember.util.ChatColor;

public class PingCommand implements Command {
    @Override
    public void execute(@NonNull CommandSource sender, @NonNull String[] args) {
        if (!(sender instanceof Player)) return;

        if (args.length < 1) {
            TextComponent textComponent = TextComponent.of(ChatColor.translate('&', String.format(
                    "&6&l» &7Your current ping is %s ms.", ((Player) sender).getPing()
            )));
            sender.sendMessage(textComponent);
        } else {
            Player onlinePlayer = Ember.getProxy().getPlayer(args[0]).orElse(null);
            if (onlinePlayer == null) {
                TextComponent textComponent = TextComponent.of(ChatColor.translate('&',
                        "&c&l» &7That player isn't online."
                ));
                sender.sendMessage(textComponent);
            } else {
                TextComponent textComponent = TextComponent.of(ChatColor.translate('&', String.format(
                        "&6&l» &7%s's ping is %s ms.", onlinePlayer.getUsername(), onlinePlayer.getPing()
                )));
                sender.sendMessage(textComponent);
            }
        }
    }
}
