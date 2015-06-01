package me.redepicness.bungee.utility.commands;

import me.redepicness.bungee.database.*;
import me.redepicness.bungee.database.Infraction.InfractionType;
import me.redepicness.bungee.utility.Utility;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import java.util.Calendar;

public class TempMute extends Command{

    public TempMute(){
        super("tempmute", "", "tmute");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        CustomPlayer player = CustomPlayer.get(commandSender.getName());
        if(!player.hasPermission(true, me.redepicness.bungee.database.Rank.MODERATOR)) return;
        if(args.length < 2){
            player.message(ChatColor.RED+"Not enough arguments. Usage: '"+ChatColor.GOLD+"/mute <name> <reason>"+ChatColor.RED+"'");
            return;
        }
        CustomPlayer target = CustomPlayer.get(args[0]);
        if(!target.isOnline()){
            player.message(ChatColor.RED+"No player with the name '"+ChatColor.GOLD+target.getName()+ChatColor.RED+"' is online!");
            return;
        }
        if(!player.hasPermission(me.redepicness.bungee.database.Rank.ADMIN) && target.hasPermission(me.redepicness.bungee.database.Rank.ADMIN)){
            player.message(ChatColor.RED+"You are not allowed to temp-mute admins!");
            return;
        }
        if(!player.hasPermission(me.redepicness.bungee.database.Rank.JR_DEV) && target.hasPermission(me.redepicness.bungee.database.Rank.JR_DEV)){
            player.message(ChatColor.RED+"You are not allowed to temp-mute Jr Devs!");
            return;
        }
        boolean overridden = false;
        if(target.getActiveInfraction(InfractionType.MUTE) != null){
            target.getActiveInfraction(InfractionType.MUTE).expire(player.getName()+" (override)");
            overridden = true;
        }
        if(target.getActiveInfraction(InfractionType.TEMP_MUTE) != null){
            target.getActiveInfraction(InfractionType.TEMP_MUTE).expire(player.getName()+" (override)");
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
        String reason = "You were muted by a staff member!";
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
            Utility.sendToStaff(player.getColoredName() + ChatColor.AQUA + " temp-muted " + target.getColoredName() + ChatColor.AQUA + "! (Override)");
        }
        else{
            Utility.sendToStaff(player.getColoredName() + ChatColor.AQUA + " temp-muted " + target.getColoredName() + ChatColor.AQUA + "!");
        }
        QuickMute.removeQuickMute(target.getName());
        long timeRemaining =  (infraction.getWhen() + infraction.getDuration() * 1000)- Calendar.getInstance().getTimeInMillis();
        long days = timeRemaining / (24 * 60 * 60 * 1000);
        timeRemaining = timeRemaining % (24 * 60 * 60 * 1000);
        long hours = timeRemaining / (60 * 60 * 1000);
        timeRemaining = timeRemaining % (60 * 60 * 1000);
        long minutes = timeRemaining / (60 * 1000);
        String remaining = (days == 0 ? "" : " " + days + " day" + (days > 1 ? "s" : "")) +
                (hours == 0 ? "" : " " + hours + " hour" + (hours > 1 ? "s" : "")) +
                (minutes == 0 ? "" : " " + minutes + " minute" + (minutes > 1 ? "s" : ""));
        target.message(ChatColor.RED + "You were muted! Your mute expires in" + remaining + "!");
        target.message(ChatColor.RED+"Reason:" + ChatColor.GOLD + reason);
    }

}
