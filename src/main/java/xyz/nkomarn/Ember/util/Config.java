package xyz.nkomarn.Ember.util;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;

import java.io.*;

/**
 * Utility class to easily manage the configuration.
 */
public class Config {
    private static ConfigurationNode root;

    public static boolean load() {
        File configFile = new File("plugins/Ember/config.yml");
        configFile.mkdirs();
        YAMLConfigurationLoader loader = YAMLConfigurationLoader.builder().setPath(configFile.toPath()).build();
        try {
            root = loader.load();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ConfigurationNode getRoot() {
        return root;
    }
}
