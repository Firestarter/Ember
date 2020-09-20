package com.firestartermc.ember;

import com.firestartermc.ember.command.*;
import com.firestartermc.ember.data.redis.Redis;
import com.firestartermc.ember.listener.PingListener;
import com.firestartermc.ember.listener.PlayerLeaveListener;
import net.md_5.bungee.api.plugin.Plugin;
import com.firestartermc.ember.data.DataManager;
import com.firestartermc.ember.data.PlayerData;
import com.firestartermc.ember.listener.PlayerJoinListener;
import com.firestartermc.ember.listener.VoteListener;
import com.firestartermc.ember.util.Config;

public class Ember extends Plugin {

    private static Ember ember;
    private Redis redis;
    private DataManager dataManager;

    public void onEnable() {
        ember = this;
        Config.loadConfig();

        if (!PlayerData.connect(Config.getString("database.url"),
                Config.getString("database.username"),
                Config.getString("database.password"))) {
            getLogger().severe("Failed to connect to database.");
            return;
        }

        this.redis = new Redis(Config.getString("redis.uri"));
        this.redis.subscribe("firestarter::vanish-update");

        this.dataManager = new DataManager(this.redis, getProxy());
        this.dataManager.init();

        getProxy().getPluginManager().registerListener(this, new PlayerJoinListener());
        getProxy().getPluginManager().registerListener(this, new PlayerLeaveListener(this.dataManager));
        getProxy().getPluginManager().registerListener(this, new VoteListener());
        getProxy().getPluginManager().registerListener(this, new PingListener(this.dataManager));

        getProxy().getPluginManager().registerCommand(this, new DiscordCommand());
        getProxy().getPluginManager().registerCommand(this, new PingCommand(this.dataManager));
        getProxy().getPluginManager().registerCommand(this, new PrivacyCommand());
        getProxy().getPluginManager().registerCommand(this, new RedditCommand());
        getProxy().getPluginManager().registerCommand(this, new VoteCommand());
    }

    public void onDisable() {
        PlayerData.close();
    }

    /**
     * Fetches an instance of the Ember plugin.
     * @return Ember plugin instance.
     */
    public static Ember getEmber() {
        return ember;
    }
}
