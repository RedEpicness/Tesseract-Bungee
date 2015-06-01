package me.redepicness.bungee.utility.commands;

import me.redepicness.bungee.database.*;
import me.redepicness.bungee.database.Rank;
import me.redepicness.bungee.utility.Utility;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class Kick extends Command{

    public Kick() {
        super("kick");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        CustomPlayer player = CustomPlayer.get(commandSender.getName());
        if(!player.hasPermission(true, Rank.HELPER)) return;
        if(args.length == 0){
            player.message(ChatColor.RED+"Not enough arguments. Usage: '"+ChatColor.GOLD+"/kick <name> <reason>"+ChatColor.RED+"'");
            return;
        }
        CustomPlayer target = CustomPlayer.get(args[0]);
        if(!target.isOnline()){
            player.message(ChatColor.RED+"No player with the name '"+ChatColor.GOLD+target.getName()+ChatColor.RED+"' online!");
            return;
        }
        if(!player.hasPermission(Rank.ADMIN) && target.hasPermission(Rank.ADMIN)){
            player.message(ChatColor.RED+"You are not allowed to kick admins!");
            return;
        }
        if(!player.hasPermission(Rank.JR_DEV) && target.hasPermission(Rank.JR_DEV)){
            player.message(ChatColor.RED+"You are not allowed to kick Jr Devs!");
            return;
        }
        if(!player.hasPermission(Rank.MODERATOR) && target.hasPermission(Rank.MODERATOR)){
            player.message(ChatColor.RED+"You are not allowed to kick moderators!");
            return;
        }
        String reason = "You were kicked by a staff member!";
        if(args.length > 1){
            reason = "";
            for (String s : args){
                reason += " "+s;
            }
            reason = reason.substring(args[0].length()+1).trim();
        }
        Infraction.getNewKick(player.getName(), target.getName(), reason);
        target.updateInfractions();
        Utility.sendToStaff(player.getColoredName()+ChatColor.AQUA+" kicked "+target.getColoredName()+ChatColor.AQUA+"!");
        target.getProxiedPlayer().disconnect(ChatColor.RED+"You were kicked from the server! Reason:\n"+ChatColor.GOLD+reason);
    }
}
