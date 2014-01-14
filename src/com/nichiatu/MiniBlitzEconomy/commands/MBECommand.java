package com.nichiatu.MiniBlitzEconomy.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.nichiatu.MiniBlitzEconomy.Formatter;
import com.nichiatu.MiniBlitzEconomy.MiniBlitzEconomy;
import com.nichiatu.MiniBlitzEconomy.economy.EconomyManager;

public class MBECommand implements CommandExecutor {

    private MiniBlitzEconomy plugin;

    public MBECommand (MiniBlitzEconomy plugin) {
        this.plugin = plugin;
    }
    
    /*
     * Such spaghetti...
     */
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        
        if (label.equalsIgnoreCase("mbe") && args.length < 1) {
            return false;
        }
        
        EconomyManager em = new EconomyManager(plugin);
        Player cs = null;
        
        if (sender instanceof Player) {
            cs = (Player)sender;
        }
        
        if (args[0].equalsIgnoreCase("balance") && sender.hasPermission("mbe.balance")) {
            
            if (!(sender instanceof Player)) {
                sender.sendMessage("You need to be a player to use this command.");
                return true;
            }
            
            String balance = "Gems: " + em.getBalance(sender.getName(), "gems") + "\nTokens: " + em.getBalance(sender.getName(), "tokens");
            Formatter.sendFormattedText(cs, balance);
            plugin.RenderScoreboard((Player)sender);
            return true;
        }
        
        if (args.length != 4) {
            Formatter.sendFormattedText(cs, "Not enough arguments.");
            Formatter.sendFormattedText(cs, "Arguments given: " + args.length);
            return true;
        }
        
        // GEMS
        
        if (args[0].equalsIgnoreCase("gems")) {
            if (args[1].equalsIgnoreCase("send") && (sender.hasPermission("mbe.gems.send") || sender.isOp())) {
                
                if (!(sender instanceof Player)) {
                    sender.sendMessage("You need to be a player to use this command.");
                    return true;
                }
                
                Player reciever = plugin.getServer().getPlayer(args[2]);
                int senderBalance = em.getBalance(sender.getName(), "gems");
                
                if (reciever != null && senderBalance > Integer.parseInt(args[3])) {
                    
                    if (Integer.parseInt(args[3]) < 0) {
                        Formatter.sendFormattedText(cs, "You need to specify an amount higher than 0");
                        return true;
                    }
                    
                    if (sender.getName().equalsIgnoreCase(reciever.getName())) {
                        Formatter.sendFormattedText(cs, "You can't send gems to yourself.");
                        return true;
                    }
                    
                    em.addGems(Integer.parseInt(args[3]), reciever.getName());
                    em.takeGems(Integer.parseInt(args[3]), sender.getName());
                    
                    String currencyName = (Integer.parseInt(args[3]) == 1) ? "gem" : "gems";
                    String senderResponse = "You just gave " + args[3] + " " + currencyName + " to " + reciever.getName();
                    String recieverResponse = "You just recieved " + args[3] + " " + currencyName + " from " + sender.getName();
                    
                    Formatter.sendFormattedText(cs, senderResponse);
                    Formatter.sendFormattedText(reciever, recieverResponse);
                    
                    plugin.RenderScoreboard((Player)sender);
                    plugin.RenderScoreboard(reciever);
                    
                    return true;
                }
            }
            if (args[1].equalsIgnoreCase("add") && (sender.hasPermission("mbe.gems.add") || sender.isOp())) {
                
                Player reciever = plugin.getServer().getPlayer(args[2]);
                
                if (reciever != null) {
                    
                    if (Integer.parseInt(args[3]) < 0) {
                        Formatter.sendFormattedText(cs, "You need to specify an amount higher than 0");
                        return true;
                    }
                    
                    em.addGems(Integer.parseInt(args[3]), reciever.getName());
                    
                    String currencyName = (Integer.parseInt(args[3]) == 1) ? "gem" : "gems";
                    String senderResponse = "You just gave " + args[3] + " " + currencyName + " to " + reciever.getName();
                    String recieverResponse = "You just recieved " + args[3] + " " + currencyName + " from " + sender.getName();
                    
                    Formatter.sendFormattedText(cs, senderResponse);
                    Formatter.sendFormattedText(reciever, recieverResponse);
                    
                    plugin.RenderScoreboard(reciever);
                    
                    return true;
                }
                
                sender.sendMessage(ChatColor.GOLD + "[MiniBlitz] You need to specify a reciever.");
                return true;
            }
            
            if (args[1].equalsIgnoreCase("take") && (sender.hasPermission("mbe.gems.take") || sender.isOp())) {
                Player loser = plugin.getServer().getPlayer(args[2]);
                
                if (loser != null) {
                    
                    if (Integer.parseInt(args[3]) < 0) {
                        Formatter.sendFormattedText(cs, "You need to specify an amount higher than 0");
                        return true;
                    }
                    if (Integer.parseInt(args[3]) > em.getBalance(loser.getName(), "gems")) {
                        Formatter.sendFormattedText(cs, loser.getName() + " doesn't have " + args[3] + " gems.");
                        return true;
                    }
                    
                    em.takeGems(Integer.parseInt(args[3]), loser.getName());
                    
                    String currencyName = (Integer.parseInt(args[3]) == 1) ? "gem" : "gems";
                    String senderResponse = "You just took " + args[3] + " " + currencyName + " from " + loser.getName();
                    String recieverResponse = "Someone took " + args[3] + " " + currencyName + " from you.\nProbably an admin: " + sender.getName();
                    
                    Formatter.sendFormattedText(cs, senderResponse);
                    Formatter.sendFormattedText(loser, recieverResponse);
                    
                    plugin.RenderScoreboard(loser);
                    
                    return true;
               }
                
                Formatter.sendFormattedText(cs, "You need to specify a reciever.");
                return true;
            }
            
            if (args[1].equalsIgnoreCase("set") && (sender.hasPermission("mbe.gems.set") || sender.isOp())) {
                Player loser = plugin.getServer().getPlayer(args[2]);
                
                if (loser != null) {
                    
                    if (Integer.parseInt(args[3]) < 0) {
                        Formatter.sendFormattedText(cs, "You need to specify an amount higher than 0");
                        return true;
                    }
                    
                    em.setGems(Integer.parseInt(args[3]), loser.getName());
                    
                    String senderResponse = "You just set someone's gems to " + args[3];
                    String recieverResponse = "Someone set your gems to " + args[3];
                    
                    Formatter.sendFormattedText(cs, senderResponse);
                    Formatter.sendFormattedText(loser, recieverResponse);
                    
                    plugin.RenderScoreboard(loser);
                    
                    return true;
               }
                
                Formatter.sendFormattedText(cs, "You need to specify a reciever.");
                return true;
            }
        
        }
        
        // TOKENS
        
        if (args[0].equalsIgnoreCase("tokens")) {
            if (args[1].equalsIgnoreCase("send") && (sender.hasPermission("mbe.tokens.send") || sender.isOp())) {
                
                if (!(sender instanceof Player)) {
                    sender.sendMessage("You need to be a player to use this command.");
                    return true;
                }
                
                Player reciever = plugin.getServer().getPlayer(args[2]);
                int senderBalance = em.getBalance(sender.getName(), "tokens");
                
                if (reciever == null) {
                    Formatter.sendFormattedText(cs, "This is not a player: " + args[2]);
                    return true;
                }
                
                if (sender.getName().equalsIgnoreCase(reciever.getName())) {
                    Formatter.sendFormattedText(cs, "You can't send tokens to yourself.");
                    return true;
                }
                
                if (senderBalance > Integer.parseInt(args[3])) {
                    
                    if (Integer.parseInt(args[3]) < 0 ) {
                        Formatter.sendFormattedText(cs, "You need to specify an amount higher than 0");
                        return true;
                    }
                    
                    em.addTokens(Integer.parseInt(args[3]), reciever.getName());
                    em.takeTokens(Integer.parseInt(args[3]), sender.getName());
                    
                    plugin.RenderScoreboard(cs);
                    plugin.RenderScoreboard(reciever);
                    
                    String currencyName = (Integer.parseInt(args[3]) == 1) ? "token" : "tokens";
                    String senderResponse = "You just gave " + args[3] + " " + currencyName + " to " + reciever.getName();
                    String recieverResponse = "You just recieved " + args[3] + " " + currencyName + " from " + sender.getName();
                    
                    Formatter.sendFormattedText(cs, senderResponse);
                    Formatter.sendFormattedText(reciever, recieverResponse);
                    
                    return true;
                }
                
                Formatter.sendFormattedText(cs, "You don't have enough tokens!\nWhen in doubt\n/mbe balance");
                
                return true;
            }
            
            if (args[1].equalsIgnoreCase("add") && (sender.hasPermission("mbe.tokens.add") || sender.isOp())) {
                
                Player reciever = plugin.getServer().getPlayer(args[2]);
                
                if (reciever != null) {
                    
                    if (Integer.parseInt(args[3]) < 0) {
                        Formatter.sendFormattedText(cs, "You need to specify an amount higher than 0");
                        return true;
                    }
                    
                    em.addTokens(Integer.parseInt(args[3]), reciever.getName());
                    
                    String currencyName = (Integer.parseInt(args[3]) == 1) ? "token" : "tokens";
                    String senderResponse = "You just gave " + args[3] + " " + currencyName + " to " + reciever.getName();
                    String recieverResponse = "You just recieved " + args[3] + " " + currencyName + " from " + sender.getName();
                    
                    Formatter.sendFormattedText(cs, senderResponse);
                    Formatter.sendFormattedText(reciever, recieverResponse);
                    
                    plugin.RenderScoreboard(reciever);
                    return true;
                }
            }
            
            if (args[1].equalsIgnoreCase("take") && (sender.hasPermission("mbe.tokens.take") || sender.isOp())) {
                Player loser = plugin.getServer().getPlayer(args[2]);
                
                if (loser != null) {
                    
                    if (Integer.parseInt(args[3]) < 0) {
                        Formatter.sendFormattedText(cs, "You need to specify an amount higher than 0");
                        return true;
                    }
                    if (Integer.parseInt(args[3]) > em.getBalance(loser.getName(), "tokens")) {
                        Formatter.sendFormattedText(cs, loser.getName() + " doesn't have " + args[3] + " tokens.");
                        return true;
                    }
                    
                    em.takeTokens(Integer.parseInt(args[3]), loser.getName());
                    
                    String currencyName = (Integer.parseInt(args[3]) == 1) ? "token" : "tokens";
                    String senderResponse = "You just took " + args[3] + " " + currencyName + " from " + loser.getName();
                    String recieverResponse = "Someone took " + args[3] + " " + currencyName + " from you.\nProbably an admin: " + sender.getName();
                    
                    Formatter.sendFormattedText(cs, senderResponse);
                    Formatter.sendFormattedText(loser, recieverResponse);
                    
                    plugin.RenderScoreboard(loser);
                    
                    return true;
               }
            }
            
            if (args[1].equalsIgnoreCase("set") && (sender.hasPermission("mbe.tokens.set") || sender.isOp())) {
                Player loser = plugin.getServer().getPlayer(args[2]);
                
                if (loser != null) {
                    
                    if (Integer.parseInt(args[3]) < 0) {
                        Formatter.sendFormattedText(cs, "You need to specify an amount higher than 0");
                        return true;
                    }
                    
                    em.setGems(Integer.parseInt(args[3]), loser.getName());
                    
                    String senderResponse = "You just set someone's tokens to " + args[3];
                    String recieverResponse = "Someone set your tokens to " + args[3];
                    
                    Formatter.sendFormattedText(cs, senderResponse);
                    Formatter.sendFormattedText(loser, recieverResponse);
                    
                    plugin.RenderScoreboard(loser);
                    
                    return true;
               }
                
                Formatter.sendFormattedText(cs, "You need to specify a reciever.");
                return true;
            }
        
        String output = "Usage: /mbe\nUsage: <gems | tokens | balance>\nUsage: [send] [player] [amount]\n \nExample: /mbe balance\nExample: /mbe tokens send nichiatu 1000";
        
        Formatter.sendFormattedText(cs, output);
        return true;
    }
    return true;
}
}
