package com.pathshala.bdd.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

public final class ConfigReader {

    private static final Properties PROPS = load();

    private ConfigReader() {
    }

    public static String get(String key) {
        return System.getProperty(key, PROPS.getProperty(key));
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    private static Properties load() {
        Properties props = new Properties();
        try (InputStream in = ConfigReader.class.getResourceAsStream("/config.properties")) {
            props.load(in);
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read config.properties", e);
        }
        return props;
    }
}
