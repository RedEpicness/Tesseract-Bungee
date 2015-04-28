package me.redepicness.bungee.utility.commands;

import me.redepicness.bungee.database.CustomPlayer;
import me.redepicness.bungee.database.Rank;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Staff extends Command{

    public Staff(){
        super("staff");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        CustomPlayer player = CustomPlayer.get(commandSender.getName());
        if(!player.hasPermission(true, Rank.HELPER)) return;
        player.message(ChatColor.GOLD+"Online staff:");
        for(ProxiedPlayer p : ProxyServer.getInstance().getPlayers()){
            CustomPlayer customPlayer = CustomPlayer.get(p.getName());
            if(customPlayer.hasPermission(Rank.HELPER, Rank.BUILDER)){
                player.message(ChatColor.GRAY+"- "+customPlayer.getFormattedName()+ChatColor.GRAY+" ("+p.getServer().getInfo().getName()+")");
            }
        }
    }
}
