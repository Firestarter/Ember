package xyz.nkomarn.Ember.task;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import xyz.nkomarn.Ember.data.PlayerData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PlaytimeCounter implements Runnable {
    @Override
    public void run() {
        Connection connection = null;

        try {
            connection = PlayerData.getConnection();
            for (final ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                PreparedStatement statement = connection.prepareStatement("UPDATE `playerdata` SET " +
                        "`playtime` = `playtime` + 1 WHERE `uuid` = ?;");
                statement.setString(1, player.getUniqueId().toString());
                statement.executeUpdate();
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
    }
}
