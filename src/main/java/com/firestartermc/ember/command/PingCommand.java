package com.firestartermc.ember.command;

import com.firestartermc.ember.data.DataManager;
import com.google.common.collect.ImmutableList;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import com.firestartermc.ember.util.Config;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.stream.Collectors;

public class PingCommand extends Command implements TabExecutor {

    private final DataManager dataManager;

    public PingCommand(DataManager dataManager) {
        super("ping");
        this.dataManager = dataManager;
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

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        return this.dataManager.getVisiblePlayers().stream()
                .map(ProxiedPlayer::getName)
                .collect(Collectors.toSet());
    }
}
