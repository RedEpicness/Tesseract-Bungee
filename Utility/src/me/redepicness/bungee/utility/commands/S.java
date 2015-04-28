package me.redepicness.bungee.utility.commands;

import me.redepicness.bungee.database.CustomPlayer;
import me.redepicness.bungee.database.Rank;
import me.redepicness.bungee.utility.Utility;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class S extends Command {

    public S(){
        super("s");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        CustomPlayer player = CustomPlayer.get(commandSender.getName());
        if(!player.hasPermission(true, Rank.HELPER)) return;
        String text = "";
        for(String s : strings){
            text += s+" ";
        }
        Utility.sendToStaff(player.getFormattedName()+ ChatColor.WHITE+": "+ChatColor.AQUA+text);
    }
}
