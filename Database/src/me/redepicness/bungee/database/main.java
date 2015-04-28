package me.redepicness.bungee.database;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

public class main extends Plugin implements Listener{

    @Override
    public void onEnable() {
        try{
            Database.init();
        }
        catch (Exception e){
            e.printStackTrace();
            ProxyServer.getInstance().stop("Error connecting to database, shutting down proxy!");
            return;
        }
        CustomPlayer.init();
    }

    @Override
    public void onDisable() {
        Database.end();
    }
}
