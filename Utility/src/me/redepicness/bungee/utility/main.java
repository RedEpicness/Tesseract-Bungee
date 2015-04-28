package me.redepicness.bungee.utility;

import me.redepicness.bungee.database.CustomPlayer;
import me.redepicness.bungee.database.Rank;
import me.redepicness.bungee.utility.commands.*;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.event.PermissionCheckEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class main extends Plugin implements Listener{

    @Override
    public void onEnable() {
        getProxy().getPluginManager().registerCommand(this, new Staff());
        getProxy().getPluginManager().registerCommand(this, new S());
        getProxy().getPluginManager().registerCommand(this, new A());
        getProxy().getPluginManager().registerCommand(this, new me.redepicness.bungee.utility.commands.Rank());
        getProxy().getPluginManager().registerCommand(this, new Lookup());
        getProxy().getPluginManager().registerCommand(this, new Spy());
        getProxy().getPluginManager().registerListener(this, this);
        getProxy().getPluginManager().registerListener(this, new Spyer());
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent e){
        CustomPlayer player = CustomPlayer.get(e.getPlayer().getName());
        if(player.hasPermission(Rank.HELPER, Rank.BUILDER)){
            Utility.sendToStaff(player.getFormattedName() + ChatColor.AQUA + " has "+ChatColor.GREEN+"joined"+ChatColor.AQUA+" the network!");
        }
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent e){
        CustomPlayer player = CustomPlayer.get(e.getPlayer().getName());
        if(player.hasPermission(Rank.HELPER, Rank.BUILDER)){
            Utility.sendToStaff(player.getFormattedName() + ChatColor.AQUA + " has "+ChatColor.RED+"left"+ChatColor.AQUA+" the network!");
        }
        CustomPlayer.uncache(player.getName());
        Utility.log(ChatColor.GREEN + "Removed ranks for " + player.getFormattedName());
    }

    @EventHandler
    public void onPremissionCheck(PermissionCheckEvent e){
        CustomPlayer player = CustomPlayer.get(e.getSender().getName());
        if(player.hasRank(Rank.ADMIN))
            e.setHasPermission(true);
        else
            e.setHasPermission(false);
    }

}