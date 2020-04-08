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
        final String messageFormat = Config.getString("messages.vote.message");
        final String voteLink = Config.getString("messages.vote.link");

        if (!(sender instanceof ProxiedPlayer)) return;
        final ProxiedPlayer player = (ProxiedPlayer) sender;

        ProxyServer.getInstance().getScheduler().runAsync(Ember.getEmber(), () -> {
            Connection connection = null;

            try {
                connection = PlayerData.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT `votes` FROM `playerdata` " +
                        "WHERE `uuid` = ?;");
                statement.setString(1, player.getUniqueId().toString());
                ResultSet result = statement.executeQuery();

                while (result.next()) {
                    final TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&',
                            messageFormat.replace("[votes]", NumberFormat.getNumberInstance(Locale.US)
                                    .format(result.getInt(1)))
                    ));
                    message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, voteLink));
                    message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new ComponentBuilder(ChatColor.GOLD + "Click to open the vote page.").create()));
                    player.sendMessage(message);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
