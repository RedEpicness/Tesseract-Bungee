package me.redepicness.bungee.serverstatus.commands;

import me.redepicness.bungee.database.CustomPlayer;
import me.redepicness.bungee.database.Rank;
import me.redepicness.bungee.serverstatus.ServerStatus;
import me.redepicness.bungee.serverstatus.ServerStatus.Status;
import me.redepicness.bungee.serverstatus.ServerStatus.StatusInfo;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class SS extends Command {

    private String separator = ChatColor.AQUA+" | "+ChatColor.RESET;

    public SS() {
        super("status", "", "ss", "serverstatus");
    }

    public void execute(CommandSender sender, String[] args) {
        CustomPlayer player = new CustomPlayer(sender.getName());
        if(!player.hasPermission(true, Rank.ADMIN)) return;
        ProxiedPlayer p = ((ProxiedPlayer) sender);
        if (args.length == 0) {
            String name = p.getServer().getInfo().getName();
            StatusInfo statusInfo = ServerStatus.getStatusInfo(name);
            player.message(ChatColor.GOLD + name+": "+statusInfo.getStatus().withColors()
                    +separator + ChatColor.BLUE + p.getServer().getInfo().getPlayers().size() + "/" + ServerStatus.getVirtualSlots(name)
                    + separator+ statusInfo.getRankWithColors());
        }
        else if (args.length == 1) {
            if(args[0].equals("reload")){
                ServerStatus.reloadConfig();
                return;
            }
            if(args[0].equals("*")){
                for(String name : ProxyServer.getInstance().getServers().keySet()){
                    ServerStatus.StatusInfo statusInfo = ServerStatus.getStatusInfo(name);
                    player.message(ChatColor.GOLD + name+": "+statusInfo.getStatus().withColors()
                            +separator + ChatColor.BLUE + ProxyServer.getInstance().getServerInfo(name).getPlayers().size() + "/" + ServerStatus.getVirtualSlots(name)
                            + separator+ statusInfo.getRankWithColors());
                }
                return;
            }
            ServerStatus.StatusInfo statusInfo = ServerStatus.getStatusInfo(args[0]);
            if (statusInfo == null) {
                player.message(ChatColor.RED + args[0] + " is not a valid server!");
                return;
            }
            player.message(ChatColor.GOLD + args[0] +": "+statusInfo.getStatus().withColors()
                    +separator + ChatColor.BLUE + ProxyServer.getInstance().getServerInfo(args[0]).getPlayers().size() + "/" + ServerStatus.getVirtualSlots(args[0])
                    + separator+ statusInfo.getRankWithColors());
        }
        else if (args.length >= 2) {
            if (ServerStatus.getStatusInfo(args[0]) == null) {
                player.message(ChatColor.RED + args[0] + " is not a valid server!");
                return;
            }
            if (args[1].equals("set")) {
                if (args.length == 2) {
                    player.message(ChatColor.RED + "Usage: /ss <name> set <status> [Ranks]");
                    return;
                }
                if(!Status.isValid(args[2].toUpperCase())){
                    player.message(ChatColor.RED + args[2] + " is not a valid status!");
                    return;
                }
                ServerStatus.Status status = ServerStatus.Status.valueOf(args[2].toUpperCase());
                if (status.equals(ServerStatus.Status.OFFLINE)) {
                    player.message(ChatColor.RED + "You cannot set a server's status to offline!");
                    return;
                }
                ServerStatus.StatusInfo statusInfo = ServerStatus.getStatusInfo(args[0]);
                statusInfo.setStatus(status);
                switch (status) {
                    case ONLINE:
                        break;
                    case DEVELOPMENT:
                    case MAINTENANCE:
                        if (args.length == 4) {
                            statusInfo = new StatusInfo(statusInfo.getStatus(), args[3]);
                        } else {
                            statusInfo = new StatusInfo(statusInfo.getStatus(), "ADMIN");
                        }
                        break;

                }
                ServerStatus.updateStatus(args[0], statusInfo);
                player.message(ChatColor.GREEN + "Updated server status of '" + args[0] + "' to " + statusInfo.getStatus().toString() + "! ("+statusInfo.getRankString()+")");
            }
            else if (args[1].equals("setslots")) {
                if (args.length != 3) {
                    player.message(ChatColor.RED + "Usage: /ss <name> setslots <amount>");
                    return;
                }
                int amount;
                try {
                    amount = Integer.parseInt(args[2]);
                } catch (IllegalArgumentException e) {
                    player.message(ChatColor.RED + args[2] + " is not a valid number!");
                    return;
                }
                ServerStatus.setVirtualSlots(args[0], amount);
                player.message(ChatColor.GREEN + "Set slots of '" + args[0] + "' to " + amount + "!");
            }
        }
    }
}
