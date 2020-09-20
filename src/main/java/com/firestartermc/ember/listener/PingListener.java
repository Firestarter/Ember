package com.firestartermc.ember.listener;

import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import com.firestartermc.ember.data.DataManager;

public class PingListener implements Listener {

    private final DataManager dataManager;

    public PingListener(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @EventHandler(priority = 5)
    public void onProxyPinged(ProxyPingEvent event) {
        ServerPing serverPing = event.getResponse();
        ServerPing.Players players = serverPing.getPlayers();
        players.setOnline(players.getOnline() - this.dataManager.getHiddenPlayerCount());
        serverPing.setPlayers(players);
        event.setResponse(serverPing);
    }
}
