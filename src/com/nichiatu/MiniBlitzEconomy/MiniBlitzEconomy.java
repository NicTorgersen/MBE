package com.nichiatu.MiniBlitzEconomy;

import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.mcstats.MetricsLite;

import com.nichiatu.MiniBlitzEconomy.commands.MBECommand;
import com.nichiatu.MiniBlitzEconomy.economy.EconomyManager;
import com.nichiatu.MiniBlitzEconomy.listeners.MainListener;

public class MiniBlitzEconomy extends JavaPlugin {
    
    public boolean debug;
    public Logger log;
    public EconomyManager economy;
    
    @Override
    public void onLoad () {
        saveDefaultConfig();
    }
    
    @Override
    public void onEnable () {
        
        this.debug = this.getConfig().getBoolean("debug");
        this.log = this.getLogger();
        this.economy = new EconomyManager(this);
        
        if (debug == false) {
            log.info(this.getDataFolder().toString());
        }
        
        Bukkit.getPluginManager().registerEvents(new MainListener(this), this);
        getCommand("mbe").setExecutor(new MBECommand(this));
        
        try {
            MetricsLite m = new MetricsLite(this);
            m.start();
        } catch (IOException e) {
            log.info("Metrics failed to start.");
            e.printStackTrace();
        }
        
    }
    @Override
    public void onDisable () {
        this.economy = null;
        this.log = null;
    }
    
    
    /*
     * to do
     * - this seems resource heavy
     * - needs rewrite
     */
    public void RenderScoreboard (OfflinePlayer player) {
        EconomyManager em = new EconomyManager(this);
        
        if (!em.hasAccount(player.getName())) {
            return;
        }
        
        ScoreboardManager sm = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = sm.getNewScoreboard();
        
        Objective econ = scoreboard.registerNewObjective("economy", "dummy");
        
        econ.setDisplaySlot(DisplaySlot.SIDEBAR);
        econ.setDisplayName(ChatColor.GREEN + "Stats");
        
        Team team = scoreboard.registerNewTeam("economies");
        team.addPlayer(Bukkit.getOfflinePlayer(ChatColor.GOLD + "Gems:"));
        team.addPlayer(Bukkit.getOfflinePlayer(ChatColor.GOLD + "Tokens:"));
        
        Score score = econ.getScore(Bukkit.getOfflinePlayer(ChatColor.GOLD + "Gems:"));
        Score score2 = econ.getScore(Bukkit.getOfflinePlayer(ChatColor.GOLD + "Tokens:"));
        
        score.setScore(em.getBalance(player.getName(), "gems"));
        score2.setScore(em.getBalance(player.getName(), "tokens"));
        
        if (player.isOnline()) {
            Player p = (Player)player;
            p.setScoreboard(scoreboard);
        }
    }
}
