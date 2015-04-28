package me.redepicness.bungee.utility.commands;

import me.redepicness.bungee.database.CustomPlayer;
import me.redepicness.bungee.utility.Utility;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;

import static me.redepicness.bungee.database.Rank.ADMIN;
import static me.redepicness.bungee.database.Rank.isValid;

public class Rank extends Command {

    public Rank() {
        super("rank");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        CustomPlayer player = CustomPlayer.get(sender.getName());
        if(!player.hasPermission(true, /*Rank.*/ADMIN)) return;
        if(args.length < 3){
            player.message(ChatColor.RED+"Not enough arguments. Usage: '"+ ChatColor.GOLD+"/rank <operation> <name> <rank>"+ChatColor.RED+"'");
            return;
        }
        if(!args[0].equals("add") && !args[0].equals("remove")){
            player.message(ChatColor.RED+"Invalid operation '"+ ChatColor.GOLD+args[0]+ChatColor.RED+"' Options: add, remove");
            return;
        }
        CustomPlayer target = CustomPlayer.get(args[1]);
        if(!target.exists()){
            player.message(ChatColor.RED+"No player with the name '"+ChatColor.GOLD+target.getName()+ChatColor.RED+"' found in the database!");
            return;
        }
        if(/*Rank.*/!isValid(args[2])){
            player.message(ChatColor.RED+"Invalid rank '"+ ChatColor.GOLD+args[2]+ChatColor.RED+"' Options: "+ Arrays.toString(me.redepicness.bungee.database.Rank.values()));
            return;
        }
        me.redepicness.bungee.database.Rank rank = me.redepicness.bungee.database.Rank.valueOf(args[2]);
        if(args[0].equals("add")){
            if(target.hasRank(rank)){
                player.message(ChatColor.RED+"This player already has this rank!");
                return;
            }
            target.addRank(rank);
            Utility.sendToAdmin(player.getFormattedName() + ChatColor.AQUA + " added "+rank.withColors()+ChatColor.AQUA+" to " + target.getFormattedName() + ChatColor.AQUA+".");
            player.message(ChatColor.GREEN+"Added '"+rank.withColors()+ChatColor.GREEN+"' to '"+target.getFormattedName()+ChatColor.GREEN+"'.");
            String ranks = "";
            for(me.redepicness.bungee.database.Rank rank1 : target.getRanks()){
                ranks += ChatColor.GREEN+", "+rank1.withColors();
            }
            player.message(target.getFormattedName()+ChatColor.GREEN+"'s ranks: " + ranks.substring(4));
        }
        else if(args[0].equals("remove")){
            if(!target.hasRank(rank)){
                player.message(ChatColor.RED+"This player doesn't have this rank!");
                return;
            }
            target.removeRank(rank);
            Utility.sendToAdmin(player.getFormattedName() + ChatColor.AQUA + " removed " + rank.withColors() + ChatColor.AQUA+  " from " + target.getFormattedName() + ChatColor.AQUA + ".");
            player.message(ChatColor.GREEN+"Removed '"+rank.withColors()+ChatColor.GREEN+"' from '"+target.getFormattedName()+ChatColor.GREEN+"'.");
            String ranks = "";
            for(me.redepicness.bungee.database.Rank rank1 : target.getRanks()){
                ranks += ChatColor.GREEN+", "+rank1.withColors();
            }
            player.message(target.getFormattedName() + ChatColor.GREEN + "'s ranks: " + ranks.substring(4));
        }
    }
}
