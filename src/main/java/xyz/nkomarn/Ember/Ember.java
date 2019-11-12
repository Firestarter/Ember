package xyz.nkomarn.Ember;

import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import org.bson.Document;
import xyz.nkomarn.Ember.command.Discord;
import xyz.nkomarn.Ember.command.Playtime;
import xyz.nkomarn.Ember.command.Vote;
import xyz.nkomarn.Ember.listener.JoinEvent;
import xyz.nkomarn.Ember.task.PlaytimeCounter;
import xyz.nkomarn.Ember.util.Config;
import xyz.nkomarn.Kerosene.database.Database;
import xyz.nkomarn.Kerosene.database.SyncAsyncCollection;

import java.util.concurrent.TimeUnit;

public class Ember extends Plugin implements Listener {

    public static Ember instance;
    public static SyncAsyncCollection<Document> playerData;

    public void onEnable() {
        instance = this;
        Config.loadConfig();

        final String databaseName = Config.getString("database.name");
        playerData = Database.getSyncAsyncCollection(databaseName, "players");

        getProxy().getPluginManager().registerListener(this, new JoinEvent());

        getProxy().getPluginManager().registerCommand(this, new Discord());
        getProxy().getPluginManager().registerCommand(this, new Vote());
        getProxy().getPluginManager().registerCommand(this, new Playtime());

        getProxy().getScheduler().schedule(this, new PlaytimeCounter(), 0, 1, TimeUnit.MINUTES);
    }

    public void onDisable() {
        
    }
}
