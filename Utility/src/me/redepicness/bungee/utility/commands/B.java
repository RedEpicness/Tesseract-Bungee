package me.redepicness.bungee.utility.commands;

import me.redepicness.bungee.database.*;
import me.redepicness.bungee.database.Rank;
import me.redepicness.bungee.utility.Utility;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class B extends Command{

    public B(){
        super("b");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        CustomPlayer player = CustomPlayer.get(commandSender.getName());
        if(!player.hasPermission(true, Rank.BUILDER)) return;
        String text = "";
        for(String s : strings){
            text += s+" ";
        }
        Utility.sendToBuilder(player.getFormattedName() + ChatColor.WHITE + ": " + ChatColor.AQUA + text);
    }

}
