package me.redepicness.bungee.utility.commands;

import me.redepicness.bungee.database.CustomPlayer;
import me.redepicness.bungee.database.Rank;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class Lookup extends Command{

    public Lookup() {
        super("lookup");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        CustomPlayer player = CustomPlayer.get(sender.getName());
        if(!player.hasPermission(true, Rank.HELPER)) return;
        if(args.length == 0){
            player.message(ChatColor.RED+"Not enough arguments. Usage: '"+ChatColor.GOLD+"/lookup <name>"+ChatColor.RED+"'");
            return;
        }
        CustomPlayer target = CustomPlayer.get(args[0]);
        if(!target.exists()){
            player.message(ChatColor.RED+"No player with the name '"+ChatColor.GOLD+target.getName()+ChatColor.RED+"' found in the database!");
            return;
        }
        player.message(ChatColor.GOLD+"Lookup for '"+target.getColoredName()+ChatColor.GOLD+"':");
        player.message(ChatColor.GOLD+"First Login: "+ (target.getFirstLogin() == 0 ?
                ChatColor.GRAY+"Unknown" : ChatColor.AQUA+ DateFormat.getInstance().format(new Date(target.getFirstLogin()+4*60*60*1000))+" GMT"));
        long lastlogin = Calendar.getInstance().getTimeInMillis()-target.getLastLogin();
        long days = lastlogin/(24*60*60*1000);
        lastlogin = lastlogin%(24*60*60*1000);
        long hours = lastlogin/(60*60*1000);
        lastlogin = lastlogin%(60*60*1000);
        long minutes = lastlogin/(60*1000);
        String ago = (days == 0 ? "" : " "+days+" day"+(days > 1 ? "s":""))+
                (hours == 0 ? "" : " "+hours+" hour"+(hours > 1 ? "s":""))+
                (minutes == 0 ? "" : " "+minutes+" minute"+(minutes > 1 ? "s":""));
        player.message(ChatColor.GOLD+"Last Login: "+ChatColor.AQUA+(ago.equals("")?"Few seconds":ago)+" ago");
        if(target.isOnline()){
            player.message(ChatColor.GOLD+"Server: "+ChatColor.GRAY+target.getProxiedPlayer().getServer().getInfo().getName());
        }
        String ranks = "";
        for(Rank rank : target.getRanks()){
            ranks += ChatColor.GOLD+", "+rank.withColors();
        }
        player.message(ChatColor.GOLD+"Ranks: "+ranks.substring(4));
        if( !target.getFriends().isEmpty()){
            String friends = "";
            for(String name : target.getFriends()){
                friends += ChatColor.GOLD+", "+CustomPlayer.get(name).getColoredName();
            }
            player.message(ChatColor.GOLD+"Friends: "+friends.substring(4));
        }
        if( !target.getFriendRequests().isEmpty()){
            String friendRequests = "";
            for(String name : target.getFriendRequests()){
                friendRequests += ChatColor.GOLD+", "+CustomPlayer.get(name).getColoredName();
            }
            player.message(ChatColor.GOLD + "Friend Requests: " + friendRequests.substring(4));
        }
    }
}
