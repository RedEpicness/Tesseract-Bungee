package me.redepicness.bungee.utility.commands;

import me.redepicness.bungee.database.CustomPlayer;
import me.redepicness.bungee.database.Infraction.InfractionType;
import me.redepicness.bungee.utility.Utility;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class UnBan extends Command{

    public UnBan() {
        super("unban", "", "pardon");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        CustomPlayer player = CustomPlayer.get(commandSender.getName());
        if(!player.hasPermission(true, me.redepicness.bungee.database.Rank.MODERATOR)) return;
        if(args.length == 0){
            player.message(ChatColor.RED+"Not enough arguments. Usage: '"+ChatColor.GOLD+"/unban <name>"+ChatColor.RED+"'");
            return;
        }
        CustomPlayer target = CustomPlayer.get(args[0]);
        if(!target.exists()){
            player.message(ChatColor.RED+"No player with the name '"+ChatColor.GOLD+target.getName()+ChatColor.RED+"' found in the database!");
            return;
        }
        if(!player.hasPermission(me.redepicness.bungee.database.Rank.ADMIN) && target.hasPermission(me.redepicness.bungee.database.Rank.ADMIN)){
            player.message(ChatColor.RED+"You are not allowed to pardon admins!");
            return;
        }
        if(!player.hasPermission(me.redepicness.bungee.database.Rank.JR_DEV) && target.hasPermission(me.redepicness.bungee.database.Rank.JR_DEV)){
            player.message(ChatColor.RED+"You are not allowed to pardon Jr Devs!");
            return;
        }
        boolean unbanned = false;
        if(target.getActiveInfraction(InfractionType.BAN) != null){
            target.getActiveInfraction(InfractionType.BAN).expire(player.getName());
            unbanned = true;
        }
        if(target.getActiveInfraction(InfractionType.TEMP_BAN) != null){
            target.getActiveInfraction(InfractionType.TEMP_BAN).expire(player.getName());
            unbanned = true;
        }
        if(unbanned){
            Utility.sendToStaff(player.getColoredName() + ChatColor.AQUA + " pardoned " + target.getColoredName() + ChatColor.AQUA + "!");
        }
        else{
            player.message(ChatColor.RED+"'"+ChatColor.GOLD+target.getName()+ChatColor.RED+"' is not banned!");
        }
    }
}
