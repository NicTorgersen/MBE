package com.nichiatu.MiniBlitzEconomy.economy;

import java.io.File;
import com.nichiatu.MiniBlitzEconomy.MiniBlitzEconomy;
import com.nichiatu.MiniBlitzEconomy.config.Config;

public class EconomyManager {
    private MiniBlitzEconomy plugin;
    private Config pconfig;
    private String folder = "/accounts/";

    public EconomyManager (MiniBlitzEconomy plugin) {
        this.plugin = plugin;
    }
    
    /*
     * returns true if account for named player exists, otherwise false
     * 
     * String name (Name of a player)
     */
    public boolean hasAccount (String name) {
        
        // check path for the player config file
        // if it exists, return true
        
        File pathToAccount = new File(plugin.getDataFolder() + folder + name + ".yml");
        if (pathToAccount.exists()) {
            return true;
        }
        
        return false;
    }
    
    /*
     * Creates an account based on the player name
     * 
     * String name (Name of a player)
     */
    public void createAccount (String name) {
        
        // check path for the player config file
        // if it doesn't exist, create it
        
        File toConfig = new File(plugin.getDataFolder() + folder + name + ".yml");
        
        if (!toConfig.exists()) {
            this.pconfig = new Config(plugin, folder, name);
            
            pconfig.reloadConfig();
            
            pconfig.getConfig().options().copyDefaults(true);
            pconfig.getConfig().addDefault("balance.gems", 0);
            pconfig.getConfig().addDefault("balance.tokens", 0);
            
            pconfig.saveConfig();
        }
    }
    
    /*
     * Get's the balance of the current player (as an int, gems and tokens won't be decimal)
     * 
     * String name (Name of a player), String econ (gems or tokens)
     */
    public int getBalance (String name, String econ) {
        if (!this.hasAccount(name)) {
            this.createAccount(name);
        }
        if (this.pconfig == null) {
            this.pconfig = new Config(plugin, folder, name);
            pconfig.reloadConfig();
        }
        
        return pconfig.getConfig().getInt("balance." + econ);
    }
    
    /*
     * Adds gems based on integer input
     * 
     * int amount, String name
     */
    public void addGems (int amount, String name) {
        if (!this.hasAccount(name)) {
            this.createAccount(name);
        }
        
        if (this.pconfig == null) {
            this.pconfig = new Config(plugin, folder, name);
            pconfig.reloadConfig();
        }
        
        int currentAmount = pconfig.getConfig().getInt("balance.gems");
        amount += currentAmount;
        
        pconfig.getConfig().set("balance.gems", amount);
        pconfig.saveConfig();
    }
    
    /*
     * Removes gems based on integer input
     * 
     * int amount, String name
     */
    public void takeGems (int amount, String name) {
        if (!this.hasAccount(name)) {
            this.createAccount(name);
        }
        if (this.pconfig == null) {
            this.pconfig = new Config(plugin, folder, name);
            pconfig.reloadConfig();
        }
        
        int currentAmount = pconfig.getConfig().getInt("balance.gems");
        int brandNewAmount = 0;
        
        if (currentAmount > amount) {
            brandNewAmount = currentAmount - amount;
        } else {
            brandNewAmount = amount - currentAmount;
        }
        
        pconfig.getConfig().set("balance.gems", brandNewAmount);
        pconfig.saveConfig();
    }
    
    /*
     * Sets gems based on integer input
     * 
     * int amount, String name
     */
    public void setGems (int amount, String name) {
        if (!this.hasAccount(name)) {
            this.createAccount(name);
        }
        if (this.pconfig == null) {
            this.pconfig = new Config(plugin, folder, name);
            pconfig.reloadConfig();
        }
        
        pconfig.getConfig().set("balance.gems", amount);
        pconfig.saveConfig();
    }
    
    /*
     * Adds tokens based on integer input
     * 
     * int amount, String name
     */
    public void addTokens (int amount, String name) {
        if (!this.hasAccount(name)) {
            this.createAccount(name);
        }
        if (this.pconfig == null) {
            this.pconfig = new Config(plugin, folder, name);
            pconfig.reloadConfig();
        }
        
        int currentAmount = pconfig.getConfig().getInt("balance.tokens");
        amount += currentAmount;
        
        pconfig.getConfig().set("balance.tokens", amount);
        pconfig.saveConfig();
    }
    
    /*
     * Removes tokens based on integer input
     * 
     * int amount, String name
     */
    public void takeTokens (int amount, String name) {
        if (!this.hasAccount(name)) {
            this.createAccount(name);
        }
        if (this.pconfig == null) {
            this.pconfig = new Config(plugin, folder, name);
            pconfig.reloadConfig();
        }
        
        int currentAmount = pconfig.getConfig().getInt("balance.tokens");
        int brandNewAmount = 0;
        
        if (currentAmount > amount) {
            brandNewAmount = currentAmount - amount;
        } else {
            brandNewAmount = amount - currentAmount;
        }
        
        pconfig.getConfig().set("balance.tokens", brandNewAmount);
        pconfig.saveConfig();
    }
    
    /*
     * Sets tokens based on integer input
     * 
     * int amount, String name
     */
    public void setTokens (int amount, String name) {
        if (!this.hasAccount(name)) {
            this.createAccount(name);
        }
        if (this.pconfig == null) {
            this.pconfig = new Config(plugin, folder, name);
            pconfig.reloadConfig();
        }
        
        pconfig.getConfig().set("balance.tokens", amount);
        pconfig.saveConfig();
    }
    
    /*
     * Sets the designated (String toClear) gems or tokens to 0
     * 
     * String toClear, String name
     */
    public void clear (String toClear, String name) {
        if (!this.hasAccount(name) || !toClear.equals("tokens") || !toClear.equals("gems")) {
            return;
        }
        if (this.pconfig == null) {
            this.pconfig = new Config(plugin, folder, name);
            pconfig.reloadConfig();
        }
        
        pconfig.getConfig().set("balance." + toClear, 0);
        pconfig.saveConfig();
    }
    
}
