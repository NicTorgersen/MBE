package com.nichiatu.MiniBlitzEconomy.listeners;

import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.nichiatu.MiniBlitzEconomy.MiniBlitzEconomy;
import com.nichiatu.MiniBlitzEconomy.economy.EconomyManager;

public class MainListener implements Listener {
    
    private MiniBlitzEconomy plugin;
    private boolean debug;
    private Logger log;
    private EconomyManager economy;

    public MainListener (MiniBlitzEconomy plugin) {
        this.plugin = plugin;
        this.debug = plugin.debug;
        this.log = plugin.log;
        this.economy = plugin.economy;
    }
    
    @EventHandler
    public void onLogin (PlayerJoinEvent event) {
        
        Player player = event.getPlayer();
        String playername = player.getName().toLowerCase();
        
        if (economy.hasAccount(playername)) {
            
            if (debug) {
                log.info("Player " + playername + " had an account.");
            }
            
        } else {
            if (debug) {
                log.info("Creating account for " + playername);
            }
            economy.createAccount(playername);
        }
        
        this.plugin.RenderScoreboard(player);
    }
}
