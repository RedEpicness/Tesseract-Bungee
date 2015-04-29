package me.redepicness.bungee.utility.commands;

import me.redepicness.bungee.database.CustomPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class R extends Command{

    public R() {
        super("r", "", "reply");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        CustomPlayer player = CustomPlayer.get(commandSender.getName());
        if(!player.hasLastMessage()){
            player.message(ChatColor.RED+"Nobody to reply to!");
            return;
        }
        CustomPlayer target = CustomPlayer.get(player.getLastMessage());
        if(!target.isOnline()){
            player.message(ChatColor.RED+"'"+ChatColor.GOLD+target.getName()+ChatColor.RED+"' is not online!");
            return;
        }
        String message = "";
        for(String s : args){
            message += " "+s;
        }
        message = message.trim();
        target.message(ChatColor.GRAY+"From "+player.getFormattedName()+ChatColor.GRAY+": "+message);
        player.message(ChatColor.GRAY+"To "+target.getFormattedName()+ChatColor.GRAY+": "+message);
        target.setLastMessage(player.getName());
    }

}
