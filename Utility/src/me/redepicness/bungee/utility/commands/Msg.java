package me.redepicness.bungee.utility.commands;

import me.redepicness.bungee.database.CustomPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class Msg extends Command{

    public Msg() {
        super("msg", "", "message", "tell", "w", "whisper", "pm");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        CustomPlayer player = CustomPlayer.get(commandSender.getName());
        if(args.length < 2){
            player.message(ChatColor.RED+"Not enough arguments. Usage: '"+ ChatColor.GOLD+"/msg <name> <message>"+ChatColor.RED+"'");
            return;
        }
        CustomPlayer target = CustomPlayer.get(args[0]);
        if(!target.isOnline()){
            player.message(ChatColor.RED+"'"+ChatColor.GOLD+target.getName()+ChatColor.RED+"' is not online!");
            return;
        }
        String message = "";
        for(String s : args){
            message += " "+s;
        }
        message = message.substring(target.getName().length()+2);
        target.message(ChatColor.GRAY+"From "+player.getFormattedName()+ChatColor.GRAY+": "+message);
        player.message(ChatColor.GRAY+"To "+target.getFormattedName()+ChatColor.GRAY+": "+message);
        target.setLastMessage(player.getName());
    }
}
