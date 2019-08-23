package com.santex.configs;

import com.google.inject.Injector;

import java.util.concurrent.ConcurrentHashMap;

public class Injectors {

    private Injectors() {
    }

    /**
     * Injector name
     */
    public static final String APP = "app";

    /**
     * Guice injector
     */
    static final ConcurrentHashMap<String, Injector> injectors = new ConcurrentHashMap<>();

    /**
     * Injectors getter & setter
     * @param name
     * @param injector
     */
    public static void addInjector(String name, Injector injector) {
        injectors.put(name, injector);
    }

    public static Injector getInjector(String name) {
        return injectors.get(name);
    }

    /**
     * Remove named injector.
     */
    public static void clearInjector(String name) {
        injectors.remove(name);
    }

    /**
     * Remove all injectors.
     */
    public static void clearInjectors() {
        injectors.clear();
    }
}
