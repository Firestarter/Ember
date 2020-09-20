package com.firestartermc.ember.data.redis;

import io.lettuce.core.ScriptOutputType;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Flux;

public class RedisScript {

    private final Redis redis;
    private final String script;
    private final String hash;

    RedisScript(Redis redis, @NotNull String script, @NotNull String hash) {
        this.redis = redis;
        this.script = script;
        this.hash = hash;
    }

    public @NotNull <T> Flux<T> evalCast() {
        return this.eval().map(o -> (T) o);
    }

    public @NotNull <T> Flux<T> evalCast(@NotNull String[] keys, @NotNull String[] args) {
        return this.eval(keys, args).map(o -> (T) o);
    }

    public @NotNull Flux<Object> eval() {
        return this.eval(new String[0], new String[0]);
    }

    public @NotNull Flux<Object> eval(@NotNull String[] keys, @NotNull String[] args) {
        return this.redis.reactive()
                .evalsha(this.hash, ScriptOutputType.MULTI, keys, args)
                .onErrorResume((one) ->
                        this.redis.reactive()
                                .eval(this.script, ScriptOutputType.MULTI, keys, args)
                );
    }

}
