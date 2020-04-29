package xyz.nkomarn.Ember.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import xyz.nkomarn.Ember.Ember;
import xyz.nkomarn.Ember.data.PlayerData;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PostLoginListener {
    @Subscribe
    public void onPostLogin(PostLoginEvent event) {
        Ember.getProxy().getScheduler().buildTask(Ember.getEmber(), () -> {
            try (Connection connection = PlayerData.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement("INSERT INTO `playerdata` (`uuid`, " +
                        "`joined`) VALUES (?, ?) ON DUPLICATE KEY UPDATE `uuid` = `uuid`;")) {
                    statement.setObject(1, event.getPlayer().getUniqueId().toString());
                    statement.setLong(2, System.currentTimeMillis());
                    statement.execute();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).schedule();
    }
}
