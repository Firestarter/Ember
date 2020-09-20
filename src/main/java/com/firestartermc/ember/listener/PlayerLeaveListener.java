package com.firestartermc.ember.listener;

import com.firestartermc.ember.data.DataManager;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerLeaveListener implements Listener {

    private final DataManager dataManager;

    public PlayerLeaveListener(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @EventHandler
    public void onPlayerLeave(PlayerDisconnectEvent event) {
        // this.dataManager.removeData(event.getPlayer());
    }
}
