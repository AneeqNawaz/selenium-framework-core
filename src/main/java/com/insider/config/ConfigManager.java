package com.insider.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

    private static final Properties props = new Properties();

    static {
        try (InputStream input = ConfigManager.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new RuntimeException("config.properties not found in classpath");
            }
            props.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    private static String get(String key, String envVar, String defaultVal) {
        String sys = System.getProperty(key);
        if (sys != null && !sys.isBlank()) {
            return sys.trim();
        }

        String env = System.getenv(envVar);
        if (env != null && !env.isBlank()) {
            return env.trim();
        }

        return props.getProperty(key, defaultVal);
    }

    public static String getBrowser() {
        return get("browser", "BROWSER", "chrome");
    }

    public static String getEnv() {
        return get("env", "ENV", "prod");
    }

    public static String getBaseUrl() {
        String env = getEnv(); // prod / stage
        return props.getProperty("baseUrl." + env, "https://useinsider.com/");
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(get("headless", "HEADLESS", "false"));
    }

    public static int getImplicitWaitSeconds() {
        return Integer.parseInt(get("implicitWait", "IMPLICIT_WAIT", "5"));
    }
}
