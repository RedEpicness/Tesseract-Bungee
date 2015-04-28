package me.redepicness.bungee.utility.commands;

import me.redepicness.bungee.database.CustomPlayer;
import me.redepicness.bungee.database.Rank;
import me.redepicness.bungee.utility.Spyer;
import me.redepicness.bungee.utility.Spyer.SpyType;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;

public class Spy extends Command{

    public Spy() {
        super("spy");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        CustomPlayer player = CustomPlayer.get(commandSender.getName());
        if(!player.hasPermission(true, Rank.ADMIN)) return;
        if(args.length == 0){
            player.message(ChatColor.RED+"Not enough arguments! Usage '"+ChatColor.GOLD+"/spy <name/server>"+ChatColor.RED+"'");
            return;
        }
        if(ProxyServer.getInstance().getServers().keySet().contains(args[0]) && ProxyServer.getInstance().getServerInfo(args[0]).getName().equals(args[0])){
            if(Spyer.isSpying(player.getName(), SpyType.SERVER, args[0])){
                Spyer.removeSpy(player.getName(), SpyType.SERVER, args[0]);
                player.message(ChatColor.GREEN+"Stopped spying on '"+ChatColor.GOLD+args[0]+ChatColor.GREEN+"'");
            }
            else{
                Spyer.addSpy(player.getName(), SpyType.SERVER, args[0]);
                player.message(ChatColor.GREEN + "Started spying on '" + ChatColor.GOLD + args[0] + ChatColor.GREEN+ "'");
            }
        }
        else if(ProxyServer.getInstance().getPlayer(args[0]) != null){
            if(Spyer.isSpying(player.getName(), SpyType.USER, args[0])){
                Spyer.removeSpy(player.getName(), SpyType.USER, args[0]);
                player.message(ChatColor.GREEN+"Stopped spying on '"+ChatColor.GOLD+args[0]+ChatColor.GREEN+"'");
            }
            else{
                Spyer.addSpy(player.getName(), SpyType.USER, args[0]);
                player.message(ChatColor.GREEN + "Started spying on '" + ChatColor.GOLD + args[0] + ChatColor.GREEN+ "'");
            }
        }
        else{
            player.message(ChatColor.RED+"No server or player with the name '"+ChatColor.GOLD+args[0]+ChatColor.RED+"' online!");
        }
    }
}
