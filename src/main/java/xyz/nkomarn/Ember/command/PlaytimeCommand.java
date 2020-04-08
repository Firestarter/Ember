package xyz.nkomarn.Ember.command;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class PlaytimeCommand extends Command {
    private final DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");

    public PlaytimeCommand() {
        super("playtime", "", "joined", "played");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        final String border = Config.getString("messages.playtime.border");
        final String playtime = Config.getString("messages.playtime.playtime");
        final String joined = Config.getString("messages.playtime.joined");

        if (!(sender instanceof ProxiedPlayer)) return;
        final ProxiedPlayer player = (ProxiedPlayer) sender;

        ProxyServer.getInstance().getScheduler().runAsync(Ember.getEmber(), () -> {
            Connection connection = null;

            try {
                connection = PlayerData.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT `playtime`, `joined` " +
                        "FROM `playerdata` WHERE `uuid` = ?;");
                statement.setString(1, player.getUniqueId().toString());
                ResultSet result = statement.executeQuery();

                while (result.next()) {
                    final TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&',
                            String.format(
                                    "%s\n%s%s%s", border, playtime.replace("[playtime]",
                                            intToTimeString(result.getInt(1))), joined.replace("[joined]",
                                            dateFormat.format(result.getLong(2))), border
                            )));
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

    private String intToTimeString(final int time) {
        return time / 24 / 60 + " days, " + time / 60 % 24 + " hours, and " + time % 60 + " minutes";
    }
}
