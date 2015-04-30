package me.redepicness.bungee.utility;

import me.redepicness.bungee.database.CustomPlayer;
import me.redepicness.bungee.database.Rank;
import me.redepicness.bungee.utility.commands.*;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class main extends Plugin implements Listener{

    public static List<String> blockedPhrases;
    public static Map<String, String> lastMessage = new HashMap<>();
    public static Configuration filterConfig;
    public static Configuration motdConfig;

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
        getProxy().getPluginManager().registerCommand(this, new Motd());
        getProxy().getPluginManager().registerListener(this, this);
        getProxy().getPluginManager().registerListener(this, new Spyer());
        getProxy().getPluginManager().registerCommand(this, new Filter());
        try{
            filterConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File("blocked.yml"));
            motdConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File("motd.yml"));
        }
        catch (IOException e){
            throw new RuntimeException("Could not load configs!", e);
        }
        blockedPhrases = filterConfig.getStringList("blocked");
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
        if(lastMessage.containsKey(player.getName())) lastMessage.remove(player.getName());
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
    public void onProxyPing(ProxyPingEvent e){
        e.registerIntent(this);
        ServerPing ping = e.getResponse();
        ping.setDescription(ChatColor.RED+"Battle"+ChatColor.GOLD+"Realms\n"+ChatColor.RESET+motdConfig.getString("description").replace('&', ChatColor.COLOR_CHAR));
        ping.getPlayers().setSample(null);
        e.completeIntent(this);
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
                    if (args.length == 2) {
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
            getProxy().getPlayers().stream().filter(player -> player.getName().toLowerCase().startsWith(args[args.length - 1])).forEach(player -> e.getSuggestions().add(player.getName()));
        }
    }

    @EventHandler
    public void onChat(ChatEvent e){
        String message = e.getMessage();
        if(e.isCommand()) return;
        CustomPlayer player = null;
        ArrayList<String> playernames = new ArrayList<>();
        for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()){
            playernames.add(p.getName());
            if(p.getAddress().equals(e.getSender().getAddress())){
                player = CustomPlayer.get(p.getName());
            }
        }
        assert player != null;
        if(player.hasPermission(Rank.ADMIN)) return;
        if(lastMessage.containsKey(player.getName())){
            if(lastMessage.get(player.getName()).equals(e.getMessage())) {
                player.message(ChatColor.RED+"You are not allowed to send the same message twice!");
                e.setCancelled(true);
                return;
            }
        }
        else{
            lastMessage.put(player.getName(), e.getMessage());
        }
        boolean skipBlockedWordCheck = false;
        boolean skipTooManyCapitals = false;
        boolean skipSameCharacters = false;
        for(String phrase : blockedPhrases){
            if(message.toLowerCase().contains(phrase)){
                for(String name : playernames){
                    if(name.toLowerCase().contains(phrase.toLowerCase()) && message.toLowerCase().contains(name.toLowerCase())){
                        skipBlockedWordCheck = true;
                    }
                }
                if(!skipBlockedWordCheck){
                    player.message(ChatColor.RED+"Your message was blocked for containing '"+phrase+"'!");
                    e.setCancelled(true);
                    return;
                }
                skipBlockedWordCheck = false;
            }
        }
        int amount = 0;
        String capital = "";
        for(char character : message.toCharArray()){
            if(Character.isUpperCase(character) || character == ' '){
                amount++;
                capital += character;
                if(amount >= 5){
                    for(String name : playernames){
                        if(message.toLowerCase().contains(name.toLowerCase())){
                            int amount1 = 0;
                            for(char character1 : message.toCharArray()){
                                if(Character.isUpperCase(character1)  || character == ' '){
                                    amount1++;
                                    if(amount1 >= 5){
                                        if(name.contains(capital)){
                                            skipTooManyCapitals = true;
                                        }
                                    }
                                } else{
                                    amount1 = 0;
                                }
                            }
                        }
                    }
                    if(!skipTooManyCapitals) {
                        player.message(ChatColor.RED + "Your message was blocked for containing too many capital letters next to each other!");
                        e.setCancelled(true);
                        return;
                    }
                    skipTooManyCapitals = false;
                }
            }
            else{
                amount = 0;
                capital = "";
            }
        }
        char previous = ' ';
        int repetitions = 0;
        for(char character : message.toCharArray()){
            if(character == previous){
                repetitions++;
                if(repetitions >= 5){
                    String phrase = "" + character + character + character + character;
                    for (String name : playernames) {
                        if (message.toLowerCase().contains(name.toLowerCase()) && name.toLowerCase().contains(phrase.toLowerCase())){
                            skipSameCharacters = true;
                        }
                    }
                    if(!skipSameCharacters) {
                        player.message(ChatColor.RED+"Your message was blocked for containing too many same letters ("+character+") next to each other!");
                        e.setCancelled(true);
                        return;
                    }
                    skipSameCharacters = false;
                }
            }
            else {
                previous = character;
                repetitions = 0;
            }
        }
    }
}