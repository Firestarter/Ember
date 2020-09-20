package com.firestartermc.ember.data.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.pubsub.RedisPubSubListener;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;
import com.firestartermc.ember.data.redis.pubsub.PubSubHandler;

import java.util.ArrayList;
import java.util.List;

public class Redis {

    private final RedisClient client;
    private final StatefulRedisConnection<String, String> connection;
    private final RedisPubSubAsyncCommands<String, String> pubSub;
    public final List<PubSubHandler> pubSubHandlers;

    public Redis(@NotNull String uri) {
        RedisURI redisUri = RedisURI.create(uri);

        this.client = RedisClient.create(redisUri);
        this.connection = client.connect();
        this.pubSub = client.connectPubSub().async();
        this.pubSub.getStatefulConnection().addListener(new InternalPubSubHandler());
        this.pubSubHandlers = new ArrayList<>();
    }

    public @NotNull RedisCommands<String, String> sync() {
        return connection.sync();
    }

    public @NotNull RedisAsyncCommands<String, String> async() {
        return connection.async();
    }

    public @NotNull RedisReactiveCommands<String, String> reactive() {
        return connection.reactive();
    }

    public void subscribe(@NotNull String... channels) {
        pubSub.subscribe(channels);
    }

    public void unsubscribe(@NotNull String... channels) {
        pubSub.unsubscribe(channels);
    }

    public void addHandler(PubSubHandler pubSubHandler) {
        this.pubSubHandlers.add(pubSubHandler);
    }

    public void removeHandler(PubSubHandler pubSubHandler) {
        this.pubSubHandlers.remove(pubSubHandler);
    }

    public @NotNull Mono<RedisScript> loadScript(@NotNull String script) {
        return reactive().scriptLoad(script)
                .map(hash -> new RedisScript(this, script, hash));
    }

    public void shutdown() {
        if (connection == null) {
            return;
        }

        connection.close();
    }

    private class InternalPubSubHandler implements RedisPubSubListener<String, String> {

        @Override
        public void message(String channel, String message) {
            if (message.trim().length() < 1) {
                return;
            }

            pubSubHandlers.forEach(pubSubHandler -> pubSubHandler.handle(channel, message));
        }

        @Override
        public void message(String pattern, String channel, String message) {
        }

        @Override
        public void subscribed(String channel, long count) {
        }

        @Override
        public void psubscribed(String pattern, long count) {
        }

        @Override
        public void unsubscribed(String channel, long count) {
        }

        @Override
        public void punsubscribed(String pattern, long count) {
        }

    }
}
