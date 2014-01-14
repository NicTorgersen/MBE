package com.nichiatu.MiniBlitzEconomy.config;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.nichiatu.MiniBlitzEconomy.MiniBlitzEconomy;

public class Config {
    
    @SuppressWarnings("unused")
    private MiniBlitzEconomy plugin;
    private boolean debug;
    private Logger log;
    private File config;
    private FileConfiguration fileConfiguration;
    private String configName = null;
    private File datafolder;

    public Config (MiniBlitzEconomy plugin, String folder, String configName) {
        this.plugin = plugin;
        this.debug = plugin.debug;
        this.log = plugin.getLogger();
        this.datafolder = plugin.getDataFolder();
        
        setConfigName(folder, configName);
    }
    
    /*
     * Returns the currently active config's name
     */
    public String getConfigName () {
        return this.configName;
    }
    
    /*
     * Name the config
     * 
     * String configName
     */
    public void setConfigName (String folder, String configName) {
        this.configName = folder + "/" + configName + ".yml";
    }
    
    public void setFile (File file) {
        this.config = file;
    }
    
    public void setFileConfiguration (FileConfiguration file) {
        this.fileConfiguration = file;
    }
    
    /*
     * Reload this config
     */
    public void reloadConfig () {
        if (config == null) {
            if (datafolder == null)
                throw new IllegalStateException();
            config = new File(datafolder, configName);
        }
        this.fileConfiguration = YamlConfiguration.loadConfiguration(config);
    }
    
    /*
     * Get the instance of this FileConfiguration instance
     */
    public FileConfiguration getConfig () {
        if (fileConfiguration == null) {
            this.reloadConfig();
        }
        return fileConfiguration;
    }
    
    /*
     * Save the config
     */
    public void saveConfig () {
        if (fileConfiguration == null || config == null) {
            
            if (debug) {
                log.info("Can't save config - null");
            }
            
            return;
        } else {
            try {
                getConfig().save(config);
            } catch (IOException e) {
                log.severe("Something happened while saving " + configName);
            }
            if (debug) {
                log.info("Saved " + configName + " successfully.");
            }
        }
    }
}
