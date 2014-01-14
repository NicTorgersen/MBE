package com.nichiatu.MiniBlitzEconomy;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Formatter {
    public static void sendFormattedText (Player p, String input) {
        if (p == null) {
            return;
        }
        String[] newString = input.split("\\n");
        String pluginName = ChatColor.GOLD + "[MiniBlitz]";
        for(String piece : newString) {
            p.sendMessage(pluginName + " " + piece);
        }
    }
}
