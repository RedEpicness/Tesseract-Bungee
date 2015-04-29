package me.redepicness.bungee.utility;

import me.redepicness.bungee.database.CustomPlayer;
import me.redepicness.bungee.database.Rank;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Utility {

    private static String staffPrefix = ChatColor.GOLD+"[S] "+ChatColor.AQUA;
    private static String adminPrefix = ChatColor.YELLOW+"[A] "+ChatColor.AQUA;
    private static String builderPrefix = ChatColor.DARK_AQUA+"[B] "+ChatColor.AQUA;

    public static void sendToStaff(String message){
        for(ProxiedPlayer p : ProxyServer.getInstance().getPlayers()){
            CustomPlayer player = CustomPlayer.get(p.getName());
            if(player.hasPermission(Rank.HELPER)){
                player.message(staffPrefix + message);
            }
        }
    }

    public static void sendToAdmin(String message) {
        for(ProxiedPlayer p : ProxyServer.getInstance().getPlayers()){
            CustomPlayer player = CustomPlayer.get(p.getName());
            if(player.hasPermission(Rank.ADMIN)){
                player.message(adminPrefix + message);
            }
        }
    }

    public static void sendToBuilder(String message) {
        for(ProxiedPlayer p : ProxyServer.getInstance().getPlayers()){
            CustomPlayer player = CustomPlayer.get(p.getName());
            if(player.hasPermission(Rank.BUILDER)){
                player.message(builderPrefix + message);
            }
        }
    }

    public static void log(String message){
        ProxyServer.getInstance().getConsole().sendMessage(message);
    }

}
