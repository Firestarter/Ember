package xyz.nkomarn.Ember.listener;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import xyz.nkomarn.Ember.Ember;
import xyz.nkomarn.Ember.data.PlayerData;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(final PostLoginEvent event) {
        ProxyServer.getInstance().getScheduler().runAsync(Ember.getEmber(), () -> {
            Connection connection = null;

            try {
                connection = PlayerData.getConnection();
                PreparedStatement statement = connection.prepareStatement("INSERT INTO `playerdata` (`uuid`, " +
                        "`joined`, `playtime`, `votes`, `donor`) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY " +
                        "UPDATE `uuid` = `uuid`;");
                statement.setObject(1, event.getPlayer().getUniqueId().toString());
                statement.setLong(2, System.currentTimeMillis());
                statement.setInt(3, 0);
                statement.setInt(4, 0);
                statement.setBoolean(5, false);
                statement.execute();
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
