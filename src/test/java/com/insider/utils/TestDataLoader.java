package com.insider.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;

public class TestDataLoader {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static <T> T load(String resourcePath, Class<T> clazz) {
        try (InputStream is = TestDataLoader.class
                .getClassLoader()
                .getResourceAsStream(resourcePath)) {

            if (is == null) {
                throw new RuntimeException("Test data file not found: " + resourcePath);
            }

            return MAPPER.readValue(is, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load test data from: " + resourcePath, e);
        }
    }
}
