package com.example.javaapp.utils;

public class Constants {
    private Constants() {
        throw new AssertionError("Constants is not instantiable");
    }

    public static final String DB_URL = System.getenv("DB_URL");
}
