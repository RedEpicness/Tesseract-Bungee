package me.redepicness.bungee.utility;

import me.redepicness.bungee.database.CustomPlayer;
import me.redepicness.bungee.database.Rank;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Utility {

    private static String staffPrefix = ChatColor.GOLD+"[S] "+ChatColor.AQUA;

    public static void sendToStaff(String message){
        for(ProxiedPlayer p : ProxyServer.getInstance().getPlayers()){
            CustomPlayer player = new CustomPlayer(p.getName());
            if(player.hasPermission(Rank.HELPER)){
                player.message(staffPrefix+message);
            }
        }
    }

}
