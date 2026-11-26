package com.pathshala.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {

    private static final Properties PROPS = load();

    private ConfigReader() {
    }

    private static Properties load() {
        Properties props = new Properties();
        try (InputStream in = ConfigReader.class.getResourceAsStream("/config.properties")) {
            props.load(in);
        } catch (IOException | NullPointerException e) {
            throw new IllegalStateException("config.properties not found on the classpath", e);
        }
        return props;
    }

    /** A -Dkey=value system property wins over config.properties. */
    public static String get(String key) {
        return System.getProperty(key, PROPS.getProperty(key));
    }

    public static String baseUrl() {
        return get("baseUrl");
    }

    public static String browser() {
        return get("browser");
    }

    public static boolean headless() {
        return Boolean.parseBoolean(get("headless"));
    }
}
