package me.redepicness.bungee.utility.commands;

import me.redepicness.bungee.database.CustomPlayer;
import me.redepicness.bungee.database.Rank;
import me.redepicness.bungee.utility.Utility;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class A extends Command{

    public A(){
        super("a");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        CustomPlayer player = new CustomPlayer(commandSender.getName());
        if(!player.hasPermission(true, Rank.ADMIN)) return;
        String text = "";
        for(String s : strings){
            text += s+" ";
        }
        Utility.sendToAdmin(player.getFormattedName() + ChatColor.WHITE + ": " + ChatColor.AQUA + text);
    }

}
