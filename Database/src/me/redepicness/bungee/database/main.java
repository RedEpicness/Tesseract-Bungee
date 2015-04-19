package me.redepicness.bungee.database;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class main extends Plugin implements Listener{

    @Override
    public void onEnable() {
        try{
            Database.init();
        }
        catch (Exception e){
            ProxyServer.getInstance().stop("Error connecting to database, shutting down proxy!");
            return;
        }
        getProxy().getPluginManager().registerListener(this, this);
    }

    @EventHandler
    public void onJoin(PostLoginEvent e){
        CustomPlayer player = new CustomPlayer(e.getPlayer().getName());
        getProxy().broadcast(player.getName());
        for(Rank rank : player.getRanks()){
            getProxy().broadcast(rank.toString());
        }
    }

}