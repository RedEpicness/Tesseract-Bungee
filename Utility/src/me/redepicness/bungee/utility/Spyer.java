package me.redepicness.bungee.utility;

import me.redepicness.bungee.database.CustomPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Spyer implements Listener{

    private static Map<String, LinkedList<String>> servers = new HashMap<>();
    private static Map<String, LinkedList<String>> users = new HashMap<>();
    private static String prefix = ChatColor.DARK_GREEN+"[Spy] "+ChatColor.RESET;

    //TODO ADD CHECKING OF DATA SAVES

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(ChatEvent e){
        if(e.isCommand()) return;
        ProxiedPlayer p = null;
        for(ProxiedPlayer pl : ProxyServer.getInstance().getPlayers()){
            if(pl.getAddress().equals(e.getSender().getAddress())){
                p = pl;
            }
        }
        ServerInfo info = null;
        for(ServerInfo s : ProxyServer.getInstance().getServers().values()){
            if(s.getAddress().equals(e.getReceiver().getAddress())){
                info = s;
            }
        }
        assert info != null && p != null;
        CustomPlayer sender = CustomPlayer.get(p.getName());
        if(servers.containsKey(info.getName())){
            for(String s : servers.get(info.getName())){
                ProxiedPlayer spyer = ProxyServer.getInstance().getPlayer(s);
                spyer.sendMessage(ChatColor.GOLD+"["+info.getName()+"] "+sender.getFormattedName()+": "+e.getMessage());
            }
        }
        if(users.containsKey(sender.getName())){
            for(String s : users.get(sender.getName())){
                ProxiedPlayer spyer = ProxyServer.getInstance().getPlayer(s);
                spyer.sendMessage(ChatColor.DARK_PURPLE+"["+info.getName()+"] "+sender.getFormattedName()+": "+e.getMessage());
            }
        }
    }

    @EventHandler
    public void serverSwitch(ServerSwitchEvent e){
        CustomPlayer player = CustomPlayer.get(e.getPlayer().getName());
        if(users.containsKey(player.getName())){
            for(String name : users.get(player.getName())){
                ProxyServer.getInstance().getPlayer(name).sendMessage(prefix+player.getFormattedName()+ChatColor.DARK_GREEN+
                        " has "+ChatColor.YELLOW+"switched"+ChatColor.DARK_GREEN+" to '"+ChatColor.AQUA+e.getPlayer().getServer().getInfo().getName()+ChatColor.DARK_GREEN+"'!");
            }
        }
    }


    @EventHandler
    public void dc(PlayerDisconnectEvent e){
        CustomPlayer player = CustomPlayer.get(e.getPlayer().getName());
        if(users.containsKey(player.getName())){
            for(String name : users.get(player.getName())){
                ProxyServer.getInstance().getPlayer(name).sendMessage(prefix+player.getFormattedName()+ChatColor.DARK_GREEN+" has "+ChatColor.RED+"left"+ChatColor.DARK_GREEN+" the network!");
            }
            users.remove(player.getName());
        }
        users.keySet().stream().filter(key -> users.get(key).contains(e.getPlayer().getName())).forEach(key -> {
            users.get(key).remove(e.getPlayer().getName());
            if (users.get(key).size() == 0) {
                users.remove(key);
            }
        });
        servers.keySet().stream().filter(key -> servers.get(key).contains(e.getPlayer().getName())).forEach(key -> {
            servers.get(key).remove(e.getPlayer().getName());
            if (servers.get(key).size() == 0) {
                servers.remove(key);
            }
        });
    }

    public static boolean isSpying(String username, SpyType spyType, String target){
        switch (spyType) {
            case USER:
                return users.containsKey(target) && users.get(target).contains(username);
            case SERVER:
                return servers.containsKey(target) && servers.get(target).contains(username);
            default:
                return false;
        }
    }

    public static void addSpy(String username, SpyType spyType, String target){
        switch (spyType){
            case USER:
                if(!users.containsKey(target)) {
                    users.put(target, new LinkedList<>());
                }
                users.get(target).add(username);
                /*ProxiedPlayer p = ProxyServer.getInstance().getPlayer(target);
                Spyer.addSpy(username, SpyType.SERVER, p.getServer().getInfo().getName());*/
                break;
            case SERVER:
                if(!servers.containsKey(target)) {
                    servers.put(target, new LinkedList<>());
                }
                servers.get(target).add(username);
                break;
        }
    }

    public static void removeSpy(String username, SpyType spyType, String target){
        switch (spyType){
            case USER:
                users.get(target).remove(username);
                if(users.get(target).size() == 0) {
                    users.remove(target);
                }
                /*ProxiedPlayer p = ProxyServer.getInstance().getPlayer(target);
                Spyer.removeSpy(username, SpyType.SERVER, p.getServer().getInfo().getName());*/
                break;
            case SERVER:
                servers.get(target).remove(username);
                if(servers.get(target).size() == 0) {
                    servers.remove(target);
                }
                break;
        }
    }

    public enum SpyType {

        USER, SERVER

    }
}
