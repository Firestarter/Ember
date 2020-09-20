package com.firestartermc.ember.data;

import com.firestartermc.ember.data.redis.Redis;
import com.firestartermc.ember.data.redis.pubsub.PubSubHandler;
import com.firestartermc.ember.util.Config;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DataManager {

    private final Redis redis;
    private final ProxyServer proxyServer;

    private Set<UUID> vanishedPlayers;
    private Set<String> hiddenServers;

    public DataManager(Redis redis, ProxyServer proxyServer) {
        this.proxyServer = proxyServer;
        this.redis = redis;
        this.redis.addHandler(new RedisVanishUpdatePubSubHandler());

        this.vanishedPlayers = new HashSet<>();
        this.hiddenServers = new HashSet<>();
    }

    public void init() {
        this.vanishedPlayers = redis.sync()
            .smembers("firestarter::vanished")
            .stream()
            .map(UUID::fromString)
            .collect(Collectors.toSet());
        this.hiddenServers = new HashSet<>(Config.getStringList("hidden-servers"));
    }

    public Set<ProxiedPlayer> getVisiblePlayers() {
        return this.proxyServer.getPlayers().stream()
                .filter(p -> !this.vanishedPlayers.contains(p.getUniqueId()))
                .filter(p -> !this.hiddenServers.contains(p.getServer().getInfo().getName()))
                .collect(Collectors.toSet());
    }

    public int getHiddenPlayerCount() {
        long hiddenServerPlayerCount = this.proxyServer.getPlayers().stream()
                .filter(p -> !this.vanishedPlayers.contains(p.getUniqueId()))
                .filter(p -> this.hiddenServers.contains(p.getServer().getInfo().getName()))
                .count();
        return Math.max(this.vanishedPlayers.size() + (int) hiddenServerPlayerCount, 0);
    }

    public void removeData(ProxiedPlayer player) {
        this.vanishedPlayers.remove(player.getUniqueId());
    }

    private class RedisVanishUpdatePubSubHandler implements PubSubHandler {
        @Override
        public void handle(String channel, String message) {
            if (!channel.equals("firestarter::vanish-update")) return;

            UUID uuid = UUID.fromString(message.split("::")[1]);
            if (message.startsWith("add::")) {
                vanishedPlayers.add(uuid);
            } else if (message.startsWith("remove::")) {
                vanishedPlayers.remove(uuid);
            }
        }
    } 
}
