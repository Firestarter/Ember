package com.firestartermc.ember.listener;

import com.firestartermc.ember.Ember;
import com.firestartermc.ember.data.PlayerData;
import com.vexsoftware.votifier.bungee.events.VotifierEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class VoteListener implements Listener {

    @EventHandler
    public void onVote(final VotifierEvent event) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(event.getVote().getUsername());
        if (!player.isConnected()) return;

        ProxyServer.getInstance().getScheduler().runAsync(Ember.getEmber(), () -> {
            try (Connection connection = PlayerData.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement("UPDATE `playerdata` SET `votes` = " +
                        "`votes` + 1 WHERE `uuid` = ?;")) {
                    statement.setString(1, player.getUniqueId().toString());
                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

}