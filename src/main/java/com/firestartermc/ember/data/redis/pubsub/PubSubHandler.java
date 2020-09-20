package com.firestartermc.ember.data.redis.pubsub;

@FunctionalInterface
public interface PubSubHandler {

    void handle(String channel, String message);

}
