package xyz.nkomarn.Ember.util;

import com.google.common.io.ByteStreams;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import xyz.nkomarn.Ember.Ember;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to easily manage the configuration.
 */
public class Config {
    private static Configuration config;

    public static void loadConfig() {
        if (!Ember.getEmber().getDataFolder().exists()) {
            Ember.getEmber().getDataFolder().mkdir();
        }
        File configFile = new File(Ember.getEmber().getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                try (InputStream is = Ember.getEmber().getResourceAsStream("config.yml");
                     OutputStream os = new FileOutputStream(configFile)) {
                    ByteStreams.copy(is, os);
                }
            } catch (IOException e) {
                throw new RuntimeException("Unable to create configuration file", e);
            }
        }

        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class)
                    .load(new File(Ember.getEmber().getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fetches a boolean from the configuration
     * if location is not found, <code>false</code> is returned
     *
     * @param location Configuration location of the boolean
     */
    public static boolean getBoolean(final String location) {
        return config.getBoolean(location, false);
    }

    /**
     * Fetches a string from the configuration
     * if location is not found, <code>empty string</code> is returned
     *
     * @param location Configuration location of the string
     */
    public static String getString(final String location) {
        return config.getString(location, "");
    }

    /**
     * Fetches an integer from the configuration
     * if location is not found, <code>0</code> is returned
     *
     * @param location Configuration location of the integer
     */
    public static int getInteger(final String location) {
        return config.getInt(location, 0);
    }

    /**
     * Fetches a double from the configuration
     * if location is not found, <code>0.0</code> is returned
     *
     * @param location Configuration location of the double
     */
    public static double getDouble(final String location) {
        return config.getDouble(location, 0.0);
    }

    /**
     * Fetches a list from the configuration
     * if location is not found, <code>empty list</code> is returned
     *
     * @param location Configuration location of the list
     */
    public static List<String> getList(final String location) {
        return (List<String>) config.getList(location, new ArrayList<>());
    }
}
