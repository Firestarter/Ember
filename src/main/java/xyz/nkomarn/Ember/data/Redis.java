package xyz.nkomarn.Ember.data;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Database class which allows for easy pooled access
 * to Redis database connections using Jedis.
 */
public class Redis {
    private static JedisPool pool;

    /**
     * Connects the pool to the remote database using provided credentials.
     * @param host The address of the Redis database.
     * @param port The port of the Redis database.
     * @param password The password of the Redis database.
     * @param maxConnections The maximum amount of connections in the pool.
     * @param timeout The timeout for an individual connection.
     * @return Whether a connection has been successfully established.
     */
    public static boolean connect(final String host, final int port, final String password, final int maxConnections,
                                  final int timeout) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxConnections);
        pool = new JedisPool(config, host, port, timeout, password);
        return true;
    }

    /**
     * Closes the Redis connection pool.
     */
    public static void close() {
        if (!pool.isClosed()) pool.close();
    }

    /**
     * Returns the connection pool which can be used to fetch a Jedis instance.
     * @return The Redis connection pool.
     */
    public static JedisPool getPool() {
        return pool;
    }

    /**
     * Returns a Jedis instance from the pool.
     * @return A Jedis instance (resource);
     */
    public static Jedis getResource() {
        return pool.getResource();
    }
}
