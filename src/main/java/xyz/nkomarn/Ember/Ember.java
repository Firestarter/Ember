package xyz.nkomarn.Ember;

import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import xyz.nkomarn.Ember.command.DiscordCommand;
import xyz.nkomarn.Ember.command.PlaytimeCommand;
import xyz.nkomarn.Ember.command.RedditCommand;
import xyz.nkomarn.Ember.command.VoteCommand;
import xyz.nkomarn.Ember.data.PlayerData;
import xyz.nkomarn.Ember.listener.PlayerJoinListener;
import xyz.nkomarn.Ember.listener.VoteListener;
import xyz.nkomarn.Ember.task.PlaytimeCounter;
import xyz.nkomarn.Ember.util.Config;

import java.util.concurrent.TimeUnit;

public class Ember extends Plugin implements Listener {
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
        getProxy().getPluginManager().registerCommand(this, new RedditCommand());
        getProxy().getPluginManager().registerCommand(this, new VoteCommand());
        getProxy().getPluginManager().registerCommand(this, new PlaytimeCommand());
        getProxy().getScheduler().schedule(this, new PlaytimeCounter(), 0, 1, TimeUnit.MINUTES);
    }

    public void onDisable() { }

    /**
     * Fetches an instance of the Ember plugin.
     * @return Ember plugin instance.
     */
    public static Ember getEmber() {
        return ember;
    }
}
