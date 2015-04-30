package me.redepicness.bungee.friends;

import me.redepicness.bungee.database.CustomPlayer;
import me.redepicness.bungee.friends.commands.Friend;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class main extends Plugin implements Listener{

    @Override
    public void onEnable() {
        getProxy().getPluginManager().registerCommand(this, new Friend());
        getProxy().getPluginManager().registerListener(this, this);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPostLogin(PostLoginEvent e){
        CustomPlayer player = CustomPlayer.get(e.getPlayer().getName());
        if(player.getFriendRequests().size() > 0){
            e.getPlayer().sendMessage(ChatColor.AQUA + "You have " + ChatColor.GOLD + player.getFriendRequests().size() + ChatColor.AQUA + " pending friend requests!");
            e.getPlayer().sendMessage(ChatColor.AQUA+"View them using '"+ChatColor.GOLD+"/friend requests"+ChatColor.AQUA+"'");
        }
        for (String name : player.getFriends()){
            CustomPlayer friend = CustomPlayer.get(name);
            if(friend.isOnline()){
                friend.message(ChatColor.DARK_PURPLE+"[F] "+player.getFormattedName() + ChatColor.YELLOW + " has " + ChatColor.GREEN + "joined" + ChatColor.YELLOW + " the network!");
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDisconnect(PlayerDisconnectEvent e){
        CustomPlayer player = CustomPlayer.get(e.getPlayer().getName());
        for (String name : player.getFriends()){
            CustomPlayer friend = CustomPlayer.get(name);
            if(friend.isOnline()){
                friend.message(ChatColor.DARK_PURPLE+"[F] "+player.getFormattedName() + ChatColor.YELLOW + " has " + ChatColor.RED + "left" + ChatColor.YELLOW + " the network!");
            }
        }
    }

}