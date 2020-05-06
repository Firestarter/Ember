package xyz.nkomarn.Ember;

import net.md_5.bungee.api.plugin.Plugin;
import xyz.nkomarn.Ember.command.*;
import xyz.nkomarn.Ember.data.PlayerData;
import xyz.nkomarn.Ember.listener.PlayerJoinListener;
import xyz.nkomarn.Ember.listener.VoteListener;
import xyz.nkomarn.Ember.util.Config;

public class Ember extends Plugin {
    private static Ember ember;

    public void onEnable() {
        ember = this;
        Config.loadConfig();

        if (!PlayerData.connect(Config.getString("database.url"),
                Config.getString("database.username"),
                Config.getString("database.password"))) {
            getLogger().severe("Failed to connect to database.");
            return;
        }

        getProxy().getPluginManager().registerListener(this, new PlayerJoinListener());
        getProxy().getPluginManager().registerListener(this, new VoteListener());

        getProxy().getPluginManager().registerCommand(this, new DiscordCommand());
        getProxy().getPluginManager().registerCommand(this, new PingCommand());
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
