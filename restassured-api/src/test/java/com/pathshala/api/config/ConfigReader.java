package com.pathshala.api.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {

    private static final Properties PROPS = load();

    private ConfigReader() {
    }

    public static String get(String key) {
        return System.getProperty(key, PROPS.getProperty(key));
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    private static Properties load() {
        Properties props = new Properties();
        try (InputStream in = ConfigReader.class.getResourceAsStream("/config.properties")) {
            props.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read config.properties", e);
        }
        return props;
    }
}
