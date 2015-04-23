package me.redepicness.bungee.serverstatus;

import me.redepicness.bungee.serverstatus.commands.SS;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.util.LinkedList;

public class main extends Plugin implements Listener{

    public static final LinkedList<ProxiedPlayer> passConnect = new LinkedList<>();

    @Override
    public void onEnable() {
        getProxy().getPluginManager().registerCommand(this, new SS());
        getProxy().getPluginManager().registerListener(this, this);
        ServerStatus.initialize();
    }

    @EventHandler
    public void onServerConnect(ServerConnectEvent e) {
        if (e.getPlayer().getServer() != null) {
            if (passConnect.contains(e.getPlayer())) {
                passConnect.remove(e.getPlayer());
                return;
            }
            e.setCancelled(true);
            Connector.connect(e.getPlayer(), e.getTarget().getName());
        }
    }

}
