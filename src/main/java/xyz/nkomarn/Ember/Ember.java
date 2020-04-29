package xyz.nkomarn.Ember;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import ninja.leaping.configurate.ConfigurationNode;
import org.slf4j.Logger;
import xyz.nkomarn.Ember.command.*;
import xyz.nkomarn.Ember.data.PlayerData;
import xyz.nkomarn.Ember.data.Redis;
import xyz.nkomarn.Ember.listener.ChatListener;
import xyz.nkomarn.Ember.listener.PostLoginListener;
import xyz.nkomarn.Ember.listener.VoteListener;
import xyz.nkomarn.Ember.util.Config;

@Plugin(id = "ember", name = "Ember", version = "3.1", authors = {"TechToolbox (@nkomarn)"},
    description = "The core for Firestarter proxies.")
public class Ember {
    private static Ember ember;
    private static ProxyServer proxy;
    private static Logger logger;

    @Inject
    public Ember(ProxyServer proxy, Logger logger) {
        ember = this;
        Ember.proxy = proxy;
        Ember.logger = logger;
    }

    public static ProxyServer getProxy() {
        return proxy;
    }

    public static Logger getLogger() {
        return logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        Config.load();

        final ConfigurationNode databaseNode = Config.getRoot().getNode("database");
        if (!PlayerData.connect(databaseNode.getNode("url").getString(),
                databaseNode.getNode("username").getString(),
                databaseNode.getNode("password").getString())) {
            logger.error("Failed to connect to database.");
            return;
        }

        final ConfigurationNode redisNode = Config.getRoot().getNode("redis");
        if (!Redis.connect(redisNode.getNode("host").getString(),
                redisNode.getNode("port").getInt(),
                redisNode.getNode("password").getString(),
                redisNode.getNode("pool-size").getInt(),
                redisNode.getNode("timeout").getInt())) {
            logger.error("Failed to connect to Redis.");
            return;
        }

        EventManager eventManager = proxy.getEventManager();
        eventManager.register(this, new ChatListener());
        eventManager.register(this, new PostLoginListener());
        eventManager.register(this, new VoteListener());

        CommandManager commandManager = proxy.getCommandManager();
        commandManager.register(new DiscordCommand(), "discord");
        commandManager.register(new PingCommand(), "ping");
        commandManager.register(new PrivacyCommand(), "privacy");
        commandManager.register(new RedditCommand(), "reddit");
        commandManager.register(new StaffCommand(), "staff");
        commandManager.register(new VoteCommand(), "vote");
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
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
