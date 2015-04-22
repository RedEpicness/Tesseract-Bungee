package me.redepicness.bungee.utility;

import me.redepicness.bungee.database.CustomPlayer;
import me.redepicness.bungee.database.Rank;
import me.redepicness.bungee.utility.commands.A;
import me.redepicness.bungee.utility.commands.S;
import me.redepicness.bungee.utility.commands.Staff;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.event.PermissionCheckEvent;
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
        getProxy().getPluginManager().registerListener(this, this);
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent e){
        CustomPlayer player = new CustomPlayer(e.getPlayer().getName());
        if(player.hasPermission(Rank.HELPER, Rank.BUILDER)){
            Utility.sendToStaff(player.getFormattedName() + ChatColor.AQUA + " has joined the network!");
        }
        System.out.println("Loaded ranks for "+player.getName()+": "+player.getRanks());
    }

    @EventHandler
    public void onPremissionCheck(PermissionCheckEvent e){
        CustomPlayer player = new CustomPlayer(e.getSender().getName());
        if(player.hasRank(Rank.ADMIN))
            e.setHasPermission(true);
        else
            e.setHasPermission(false);
    }

}