package xyz.nkomarn.Ember;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;
import org.bson.Document;
import xyz.nkomarn.Ember.command.Discord;
import xyz.nkomarn.Ember.command.Playtime;
import xyz.nkomarn.Ember.command.Vote;
import xyz.nkomarn.Ember.listener.JoinEvent;
import xyz.nkomarn.Ember.task.PlaytimeCounter;
import xyz.nkomarn.Ember.util.Config;
import xyz.nkomarn.Kerosene.database.Database;
import xyz.nkomarn.Kerosene.database.SyncAsyncCollection;

import java.io.*;
import java.lang.annotation.Documented;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Ember extends Plugin implements Listener {

    public static Ember instance;
    private static HashMap<ServerInfo, Boolean> status = new HashMap<>();;
    private static HashMap<UUID, ServerInfo> lastServer = new HashMap<>();

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

        /*getProxy().registerChannel("BungeeCord");
        getProxy().getPluginManager().registerListener(this, this);
        getProxy().getScheduler().schedule(this, new Runnable() {

            @Override
            public void run() {
                // Update server status
                for (ServerInfo server : getProxy().getServers().values()) {
                    server.ping(new Callback<ServerPing>() {
                        @Override
                        public void done(ServerPing serverPing, Throwable error) {
                            if (error != null) {
                                status.put(server, false);
                            } else {
                                status.put(server, true);
                            }
                        }
                    });
                }

                // Send players on fallback to their previous server
                ServerInfo fallback = getProxy().getServerInfo("fallback");

                for (ProxiedPlayer player : fallback.getPlayers()) {
                    if (!lastServer.containsKey(player.getUniqueId())) {
                        player.sendMessage(ChatMessageType.CHAT,
                                TextComponent.fromLegacyText(ChatColor.GOLD + "Connect to a server using /server (server name)."));
                        continue;
                    };
                    ServerInfo previousServer = lastServer.get(player.getUniqueId());

                    // Send message notifying server switch if previous was fallback
                    if (previousServer == fallback) {
                        player.sendMessage(ChatMessageType.ACTION_BAR,
                                TextComponent.fromLegacyText(ChatColor.GOLD + "Use /server to connect to your desired server."));
                    }

                    // If server is offline, sent notification. Otherwise, connect.
                    if (status.get(previousServer)) {
                        player.sendMessage(ChatMessageType.ACTION_BAR,
                                TextComponent.fromLegacyText(ChatColor.GREEN + "Connecting you to your previous server."));
                        player.connect(previousServer);
                    } else {
                        player.sendMessage(ChatMessageType.ACTION_BAR,
                                TextComponent.fromLegacyText(ChatColor.GOLD + "Your previous server is currently offline."));
                    }
                }
            }

        }, 1, 10, TimeUnit.SECONDS);*/
    }

    public void onDisable() {
        
    }

    /*@EventHandler
    public void onServerSwitch(ServerConnectEvent e) {
        if (e.getTarget().getName().equals("fallback")) {
            return;
        }
        lastServer.put(e.getPlayer().getUniqueId(), e.getTarget());
        getLogger().log(Level.INFO, String.format("%s's last server was %s.",
                e.getPlayer().getName(), lastServer.get(e.getPlayer().getUniqueId())));
    }*/
}
