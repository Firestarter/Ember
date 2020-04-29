package xyz.nkomarn.Ember.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.proxy.Player;
import com.vexsoftware.votifier.velocity.event.VotifierEvent;
import xyz.nkomarn.Ember.Ember;
import xyz.nkomarn.Ember.data.PlayerData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class VoteListener {
    @Subscribe
    public void onVote(final VotifierEvent event) {
        Player player = Ember.getProxy().getPlayer(event.getVote().getUsername()).orElse(null);
        if (player == null) return;

        Ember.getProxy().getScheduler().buildTask(Ember.getEmber(), () -> {
            try (Connection connection = PlayerData.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement("UPDATE `playerdata` SET `votes` = " +
                        "`votes` + 1 WHERE `uuid` = ?;")) {
                    statement.setString(1, player.getUniqueId().toString());
                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).schedule();
    }
}
