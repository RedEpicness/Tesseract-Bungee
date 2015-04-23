package me.redepicness.bungee.serverstatus;

import me.redepicness.bungee.database.CustomPlayer;
import me.redepicness.bungee.database.Rank;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Connector {

    public static void connect(final ProxiedPlayer player, final String server) {
        final ServerStatus.StatusInfo statusInfo = ServerStatus.getStatusInfo(server);
        final ServerInfo serverInfo = ProxyServer.getInstance().getServers().get(server);
        serverInfo.ping((ServerPing ping, Throwable throwable) -> {
            CustomPlayer p = new CustomPlayer(player.getName());
            if (ping == null) {
                statusInfo.setStatus(ServerStatus.Status.OFFLINE);
                ServerStatus.updateStatus(server, statusInfo);
                player.sendMessage(ChatColor.RED + server + " is currently offline!");
                return;
            }
            else if (statusInfo.getStatus().equals(ServerStatus.Status.OFFLINE)) {
                statusInfo.setStatus(ServerStatus.Status.ONLINE);
                ServerStatus.updateStatus(server, statusInfo);
            }
            ServerStatus.StatusInfo updatedInfo = ServerStatus.getStatusInfo(server);
            switch (updatedInfo.getStatus()) {
                case ONLINE:
                    if (ping.getPlayers().getOnline() < ServerStatus.getVirtualSlots(server)) {
                        main.passConnect.add(player);
                        player.connect(serverInfo);
                    }
                    else if (p.hasPermission(Rank.HELPER, Rank.BUILDER)) {
                        main.passConnect.add(player);
                        player.connect(serverInfo);
                    }
                    else {
                        player.sendMessage(ChatColor.RED + "The server is full! Try joining later!");
                    }
                    break;
                case DEVELOPMENT:
                case MAINTENANCE:
                    for(Rank rank : updatedInfo.getRanks()){
                        if(rank.isStaffRank() && p.hasPermission(rank)){
                            main.passConnect.add(player);
                            player.connect(serverInfo);
                            break;
                        }
                    }
                    player.sendMessage(ChatColor.GOLD + "The server is currently in "+updatedInfo.getStatus().toString().toLowerCase()+" mode!");
                    break;
            }
        });
    }
}
