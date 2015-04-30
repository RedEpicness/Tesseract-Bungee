package me.redepicness.bungee.utility.commands;

import me.redepicness.bungee.database.CustomPlayer;
import me.redepicness.bungee.database.Rank;
import me.redepicness.bungee.utility.main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Filter extends Command{

    public Filter(){
        super("filter", "", "f");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        CustomPlayer player = CustomPlayer.get(sender.getName());
        if(!player.hasPermission(true, Rank.HELPER)) return;
        if(args.length == 0){
            player.message(ChatColor.GOLD + "Possible arguments: add, remove, list");
            return;
        }
        if(args.length == 1){
            switch(args[0]){
                case "list":
                    player.message(ChatColor.GREEN + "List of blocked words:");
                    String list = "";
                    for(String phrase : main.blockedPhrases){
                        list += phrase + ", ";
                    }
                    player.message(ChatColor.GOLD + list);
                    return;
                case "add":
                case "remove":
                    player.message(ChatColor.GOLD + "Usage: /filter " + args[0] + " <phrase>");
                    return;
            }
            player.message(ChatColor.GOLD + "Possible arguments: add, remove, list");
            return;
        }
        if(args.length > 1){
            switch (args[0]) {
                case "add": {
                    String phrase = args[1];
                    for (int a = 2; a < args.length; a++) {
                        phrase += " " + args[a];
                    }
                    if (main.blockedPhrases.contains(phrase)) {
                        player.message(ChatColor.RED + "'" + phrase + "' is already blocked!");
                    } else {
                        main.blockedPhrases.add(phrase);
                        player.message(ChatColor.GREEN + "'" + phrase + "' was added to the blocked list!");
                        main.filterConfig.set("blocked", main.blockedPhrases);
                        try {
                            ConfigurationProvider.getProvider(YamlConfiguration.class).save(main.filterConfig, new File("blocked.yml"));
                        } catch (IOException e) {
                            throw new RuntimeException("Could not save blocked.yml", e);
                        }
                    }
                    break;
                }
                case "remove": {
                    String phrase = args[1];
                    for (int a = 2; a < args.length; a++) {
                        phrase += " " + args[a];
                    }
                    if (!main.blockedPhrases.contains(phrase)) {
                        player.message(ChatColor.RED + "'" + phrase + "' is not blocked!");
                    } else {
                        main.blockedPhrases.remove(phrase);
                        player.message(ChatColor.GREEN + "'" + phrase + "' was removed from the blocked list!");
                        main.filterConfig.set("blocked", main.blockedPhrases);
                        try {
                            ConfigurationProvider.getProvider(YamlConfiguration.class).save(main.filterConfig, new File("blocked.yml"));
                        } catch (IOException e) {
                            throw new RuntimeException("Could not save blocked.yml", e);
                        }
                    }
                    break;
                }
                default:
                    player.message(ChatColor.GOLD + "Possible arguments: add, remove, list");
                    break;
            }
        }
    }
}
