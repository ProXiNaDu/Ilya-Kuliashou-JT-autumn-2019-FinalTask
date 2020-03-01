/*
 * Configuration Manager
 *
 * Version 1.0
 *
 * Date 16 Nov 2019
 */


package com.epam.travel.managers;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ResourceBundle;

/**
 * A class that manages application configurations.
 *
 * @version 1.0 16 Nov 2019
 * @author Ilya Kuliashou
 */
public class ConfigurationManager {
    /**
     * Bundle name.
     */
    private static final String BUNDLE_NAME =
            "com.epam.travel.resources.configuration";

    /**
     * Logger.
     */
    private static Logger log = LogManager.getLogger(ConfigurationManager.class);
    /**
     * Configuration manager instance.
     */
    private static ConfigurationManager instance;

    /**
     * Resource bundle.
     */
    private ResourceBundle resourceBundle;

    /**
     * Creates a new configuration manager.
     */
    private ConfigurationManager() {
        log.info("Configuration manager instance created.");
    }

    /**
     * Creates a new configuration manager instance or returns an existing one.
     * @return configuration manager instance.
     */
    public static ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
            instance.resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME);
        }

        return instance;
    }

    /**
     * Returns property value.
     * @param key property name.
     * @return property value.
     */
    public String getProperty(String key) {
        return (String)resourceBundle.getObject(key);
    }
}
