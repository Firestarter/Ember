package xyz.nkomarn.Ember.command;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import xyz.nkomarn.Ember.Ember;
import xyz.nkomarn.Ember.data.PlayerData;
import xyz.nkomarn.Ember.util.Config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;

public class VoteCommand extends Command {
    public VoteCommand() {
        super("vote");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new ComponentBuilder(ChatColor.RED + "This command can only be used by players.").create());
        } else {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            ProxyServer.getInstance().getScheduler().runAsync(Ember.getEmber(), () -> {
                try (Connection connection = PlayerData.getConnection()) {
                    try (PreparedStatement statement = connection.prepareStatement("SELECT `votes` FROM `playerdata` " +
                            "WHERE `uuid` = ?;")) {
                        statement.setString(1, player.getUniqueId().toString());
                        try (ResultSet result = statement.executeQuery()) {
                            if (result.next()) {
                                TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&',
                                        String.format(Config.getString("messages.vote.message"), NumberFormat.getNumberInstance(Locale.US)
                                                .format(result.getInt(1))))
                                );
                                message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                        new ComponentBuilder(ChatColor.GRAY + "Click to open the voting page.").create()));
                                message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, Config.getString("messages.vote.link")));
                                player.sendMessage(message);
                            }
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
