package xyz.nkomarn.Ember.command;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import xyz.nkomarn.Ember.util.Config;

public class PingCommand extends Command {
    public PingCommand() {
        super("ping");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new ComponentBuilder(ChatColor.RED + "This command can only be used by players.").create());
        } else if (args.length < 1) {
            TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&', String.format(
                    Config.getString("messages.ping.your-ping"), ((ProxiedPlayer) sender).getPing()
            )));
            sender.sendMessage(message);
        } else {
            ProxiedPlayer otherPlayer = ProxyServer.getInstance().getPlayer(args[0]);
            if (otherPlayer != null) {
                TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&', String.format(
                        Config.getString("messages.ping.others-ping"), otherPlayer.getName(), otherPlayer.getPing()
                )));
                sender.sendMessage(message);
            } else {
                TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&',
                        Config.getString("messages.ping.not-online")));
                sender.sendMessage(message);
            }
        }
    }
}
