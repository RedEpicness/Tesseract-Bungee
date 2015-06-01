package me.redepicness.bungee.utility.commands;

import me.redepicness.bungee.database.*;
import me.redepicness.bungee.database.Infraction.InfractionType;
import me.redepicness.bungee.database.Rank;
import me.redepicness.bungee.utility.Utility;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import java.util.Calendar;

public class TempBan extends Command{

    public TempBan(){
        super("tempban", "", "tban");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        CustomPlayer player = CustomPlayer.get(commandSender.getName());
        if(!player.hasPermission(true, Rank.MODERATOR)) return;
        if(args.length < 2){
            player.message(ChatColor.RED+"Not enough arguments. Usage: '"+ChatColor.GOLD+"/ban <name> <reason>"+ChatColor.RED+"'");
            return;
        }
        CustomPlayer target = CustomPlayer.get(args[0]);
        if(!target.exists()){
            player.message(ChatColor.RED+"No player with the name '"+ChatColor.GOLD+target.getName()+ChatColor.RED+"' found in the database!");
            return;
        }
        if(!player.hasPermission(me.redepicness.bungee.database.Rank.ADMIN) && target.hasPermission(me.redepicness.bungee.database.Rank.ADMIN)){
            player.message(ChatColor.RED+"You are not allowed to temp-ban admins!");
            return;
        }
        if(!player.hasPermission(me.redepicness.bungee.database.Rank.JR_DEV) && target.hasPermission(me.redepicness.bungee.database.Rank.JR_DEV)){
            player.message(ChatColor.RED+"You are not allowed to temp-ban Jr Devs!");
            return;
        }
        boolean overridden = false;
        if(target.getActiveInfraction(InfractionType.BAN) != null){
            target.getActiveInfraction(InfractionType.BAN).expire(player.getName()+" (override)");
            overridden = true;
        }
        if(target.getActiveInfraction(InfractionType.TEMP_BAN) != null){
            target.getActiveInfraction(InfractionType.TEMP_BAN).expire(player.getName()+" (override)");
            overridden = true;
        }
        int duration;
        try{
            duration = Integer.parseInt(args[1].substring(0, args[1].length()-1));
        }
        catch (IllegalArgumentException e){
            player.message(ChatColor.RED+"Invalid time provided! (NaN)");
            return;
        }
        switch (args[1].substring(args[1].length()-1)){
            case "s":
                break;
            case "d":
                duration *= 24;
            case "h":
                duration *= 60;
            case "m":
                duration *= 60;
                break;
            default:
                player.message(ChatColor.RED+"Invalid time unit provided!");
                return;
        }
        String reason = "You were banned by a staff member!";
        if(args.length > 2){
            reason = "";
            for (String s : args){
                reason += " "+s;
            }
            reason = reason.trim().substring(args[0].length()+args[1].length()+1).trim();
        }
        Infraction infraction = Infraction.getNewTempBan(player.getName(), target.getName(), duration, reason);
        target.updateInfractions();
        if(overridden){
            Utility.sendToStaff(player.getColoredName() + ChatColor.AQUA + " temp-banned " + target.getColoredName() + ChatColor.AQUA + "! (Override)");
        }
        else{
            Utility.sendToStaff(player.getColoredName() + ChatColor.AQUA + " temp-banned " + target.getColoredName() + ChatColor.AQUA + "!");
        }
        QuickBan.removeQuickBan(target.getName());
        if(target.isOnline()){
            long timeRemaining =  (infraction.getWhen() + infraction.getDuration() * 1000)- Calendar.getInstance().getTimeInMillis();
            long days = timeRemaining / (24 * 60 * 60 * 1000);
            timeRemaining = timeRemaining % (24 * 60 * 60 * 1000);
            long hours = timeRemaining / (60 * 60 * 1000);
            timeRemaining = timeRemaining % (60 * 60 * 1000);
            long minutes = timeRemaining / (60 * 1000);
            String remaining = (days == 0 ? "" : " " + days + " day" + (days > 1 ? "s" : "")) +
                    (hours == 0 ? "" : " " + hours + " hour" + (hours > 1 ? "s" : "")) +
                    (minutes == 0 ? "" : " " + minutes + " minute" + (minutes > 1 ? "s" : ""));
            target.getProxiedPlayer().disconnect(ChatColor.RED + "You were banned from this server! Your ban expires in" + remaining + "! Reason:\n" + ChatColor.GOLD + reason);
        }
    }

}
