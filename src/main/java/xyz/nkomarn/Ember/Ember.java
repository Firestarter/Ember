package xyz.nkomarn.Ember;

import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import org.bson.Document;
import xyz.nkomarn.Ember.command.Discord;
import xyz.nkomarn.Ember.command.Playtime;
import xyz.nkomarn.Ember.command.Reddit;
import xyz.nkomarn.Ember.command.Vote;
import xyz.nkomarn.Ember.listener.PlayerJoinListener;
import xyz.nkomarn.Ember.listener.VoteListener;
import xyz.nkomarn.Ember.task.PlaytimeCounter;
import xyz.nkomarn.Ember.util.Config;
import xyz.nkomarn.Kerosene.database.FlexibleCollection;
import xyz.nkomarn.Kerosene.database.MongoDatabase;

import java.util.concurrent.TimeUnit;

public class Ember extends Plugin implements Listener {
    private static Ember ember;
    private static FlexibleCollection<Document> playerData;

    public void onEnable() {
        ember = this;
        Config.loadConfig();

        final String databaseName = Config.getString("database");
        playerData = MongoDatabase.getFlexibleCollection(databaseName, "players");

        getProxy().getPluginManager().registerListener(this, new PlayerJoinListener());
        getProxy().getPluginManager().registerListener(this, new VoteListener());
        getProxy().getPluginManager().registerCommand(this, new Discord());
        getProxy().getPluginManager().registerCommand(this, new Reddit());
        getProxy().getPluginManager().registerCommand(this, new Vote());
        getProxy().getPluginManager().registerCommand(this, new Playtime());
        getProxy().getScheduler().schedule(this, new PlaytimeCounter(), 0, 1, TimeUnit.MINUTES);
        getProxy().registerChannel("firestarter:data");
    }

    public void onDisable() { }

    public static Ember getEmber() {
        return ember;
    }

    public static FlexibleCollection<Document> getPlayerData() {
        return playerData;
    }
}
