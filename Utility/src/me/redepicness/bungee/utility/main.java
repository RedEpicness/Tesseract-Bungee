package me.redepicness.bungee.utility;

import me.redepicness.bungee.database.CustomPlayer;
import me.redepicness.bungee.database.Rank;
import me.redepicness.bungee.utility.commands.*;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PermissionCheckEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class main extends Plugin implements Listener{

    @Override
    public void onEnable() {
        getProxy().getPluginManager().registerCommand(this, new Staff());
        getProxy().getPluginManager().registerCommand(this, new S());
        getProxy().getPluginManager().registerCommand(this, new A());
        getProxy().getPluginManager().registerCommand(this, new B());
        getProxy().getPluginManager().registerCommand(this, new R());
        getProxy().getPluginManager().registerCommand(this, new Connect("hub"));
        getProxy().getPluginManager().registerCommand(this, new Connect("rts"));
        getProxy().getPluginManager().registerCommand(this, new Connect("build"));
        getProxy().getPluginManager().registerCommand(this, new Msg());
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

    @EventHandler
    public void tab(TabCompleteEvent e){
        e.getSuggestions().clear();
        String[] args;
        if(e.getCursor().endsWith(" ")){
            args = (e.getCursor()+"*").split(" ");
            args[args.length - 1] = "";
        }
        else{
            args = (e.getCursor()).split(" ");
        }
        if(e.getCursor().startsWith("/")){
            if (args.length < 2) return;
            switch (args[0].substring(1)) {
                case "spy":
                    ProxyServer.getInstance().getServers().values().stream().filter(s -> s.getName().startsWith(args[1])).forEach(s -> e.getSuggestions().add(s.getName()));
                    break;
                case "rank":
                    if (args.length == 3) {
                        if ("add".startsWith(args[2])) e.getSuggestions().add("add");
                        if ("remove".startsWith(args[2])) e.getSuggestions().add("remove");
                        return;
                    }
                    if (args.length == 4) {
                        for (Rank r : Rank.values()) {
                            if (r.toString().startsWith(args[3])) e.getSuggestions().add(r.toString());
                        }
                        return;
                    }
                    break;
                case "ss":
                    if (args.length == 2){
                        ProxyServer.getInstance().getServers().values().stream().filter(s -> s.getName().startsWith(args[1]))
                                .forEach(s -> e.getSuggestions().add(s.getName()));
                        if("*".startsWith(args[1])) e.getSuggestions().add("*");
                        return;
                    }
                    if(args.length == 3){
                        if ("set".startsWith(args[2])) e.getSuggestions().add("set");
                        if ("setslots".startsWith(args[2])) e.getSuggestions().add("setslots");
                        return;
                    }
                    if(args.length == 4){
                        if(args[2].equals("setslots")) return;
                        if ("MAINTENANCE".startsWith(args[3])) e.getSuggestions().add("MAINTENANCE");
                        if ("DEVELOPMENT".startsWith(args[3])) e.getSuggestions().add("DEVELOPMENT");
                        if ("ONLINE".startsWith(args[3])) e.getSuggestions().add("ONLINE");
                        return;
                    }
                    break;
            }
        }
        getProxy().getPlayers().stream().filter(player -> player.getName().toLowerCase().startsWith(args[args.length - 1])).forEach(player -> e.getSuggestions().add(player.getName()));
    }

}