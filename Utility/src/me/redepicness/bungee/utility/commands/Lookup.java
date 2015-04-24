package me.redepicness.bungee.utility.commands;

import me.redepicness.bungee.database.CustomPlayer;
import me.redepicness.bungee.database.Rank;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class Lookup extends Command{

    public Lookup() {
        super("lookup");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        CustomPlayer player = new CustomPlayer(sender.getName());
        if(!player.hasPermission(true, Rank.HELPER)) return;
        if(args.length == 0){
            player.message(ChatColor.RED+"Not enough arguments. Usage: '"+ChatColor.GOLD+"/lookup <name>"+ChatColor.RED+"'");
            return;
        }
        CustomPlayer target = new CustomPlayer(args[0]);
        if(!target.exists()){
            player.message(ChatColor.RED+"No player with the name '"+ChatColor.GOLD+target.getName()+ChatColor.RED+"' found in the database!");
            return;
        }
        player.message(ChatColor.GOLD+"Lookup for '"+ChatColor.AQUA+target.getName()+ChatColor.GOLD+"':");
        if(target.isOnline()){
            player.message(ChatColor.GOLD+"Server: "+ChatColor.GRAY+target.getProxiedPlayer().getServer().getInfo().getName());
        }
        String ranks = "";
        for(Rank rank : target.getRanks()){
            ranks += ChatColor.GOLD+", "+rank.withColors();
        }
        player.message(ChatColor.GOLD+"Ranks: "+ranks.substring(4));
    }
}
