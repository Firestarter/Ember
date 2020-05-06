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
    public void onPlayerJoin(PostLoginEvent event) {
        ProxyServer.getInstance().getScheduler().runAsync(Ember.getEmber(), () -> {
            try (Connection connection = PlayerData.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement("INSERT INTO `playerdata` (`uuid`, " +
                        "`joined`) VALUES (?, ?) ON DUPLICATE KEY UPDATE `uuid` = `uuid`;")) {
                    statement.setString(1 , event.getPlayer().getUniqueId().toString());
                    statement.setLong(2, System.currentTimeMillis());
                    statement.execute();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
