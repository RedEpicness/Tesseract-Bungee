package me.redepicness.bungee.utility.commands;

import me.redepicness.bungee.database.CustomPlayer;
import me.redepicness.bungee.database.Infraction;
import me.redepicness.bungee.database.Infraction.InfractionType;
import me.redepicness.bungee.utility.Utility;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class Ban extends Command{

    public Ban() {
        super("ban", "", "gtfo");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        CustomPlayer player = CustomPlayer.get(commandSender.getName());
        if(!player.hasPermission(true, me.redepicness.bungee.database.Rank.MODERATOR)) return;
        if(args.length == 0){
            player.message(ChatColor.RED+"Not enough arguments. Usage: '"+ChatColor.GOLD+"/ban <name> <reason>"+ChatColor.RED+"'");
            return;
        }
        CustomPlayer target = CustomPlayer.get(args[0]);
        if(!target.exists()){
            player.message(ChatColor.RED+"No player with the name '"+ChatColor.GOLD+target.getName()+ChatColor.RED+"' found in the database!");
            return;
        }
        if(!player.hasPermission(me.redepicness.bungee.database.Rank.ADMIN) && target.hasPermission(me.redepicness.bungee.database.Rank.ADMIN)){
            player.message(ChatColor.RED+"You are not allowed to ban admins!");
            return;
        }
        if(!player.hasPermission(me.redepicness.bungee.database.Rank.JR_DEV) && target.hasPermission(me.redepicness.bungee.database.Rank.JR_DEV)){
            player.message(ChatColor.RED+"You are not allowed to ban Jr Devs!");
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
        String reason = "You were banned by a staff member!";
        if(args.length > 1){
            reason = "";
            for (String s : args){
                reason += " "+s;
            }
            reason = reason.trim().substring(args[0].length()).trim();
        }
        Infraction.getNewBan(player.getName(), target.getName(), reason);
        target.updateInfractions();
        if(overridden){
            Utility.sendToStaff(player.getColoredName() + ChatColor.AQUA + " banned " + target.getColoredName() + ChatColor.AQUA + "! (Override)");
        }
        else{
            Utility.sendToStaff(player.getColoredName() + ChatColor.AQUA + " banned " + target.getColoredName() + ChatColor.AQUA + "!");
        }
        QuickBan.removeQuickBan(target.getName());
        if(target.isOnline()){
            target.getProxiedPlayer().disconnect(ChatColor.RED + "You were banned from the server! Reason:\n" + ChatColor.GOLD + reason);
        }
    }
}
