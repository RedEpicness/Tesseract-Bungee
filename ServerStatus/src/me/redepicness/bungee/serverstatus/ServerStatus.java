package me.redepicness.bungee.serverstatus;

import me.redepicness.bungee.database.Rank;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServerStatus {

    private static final Map<String, StatusInfo> statuses = new HashMap<>();
    private static final Map<String, Integer> slots = new HashMap<>();

    private static Configuration statusConfig;
    private static Configuration slotsConfig;

    public static void reloadConfig(){

        System.out.println("Reloading server status config!");

        statuses.clear();
        slots.clear();
        statusConfig = null;
        slotsConfig = null;

        try {
            slotsConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File("slots.yml"));
        }
        catch (Exception e) {
            throw new RuntimeException("Unable to load slots.yml!", e);
        }

        try {
            statusConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File("status.yml"));
        }
        catch (Exception e) {
            throw new RuntimeException("Unable to load status.yml!", e);
        }

        for (final String s : ProxyServer.getInstance().getServers().keySet()) {
            slots.put(s, slotsConfig.getInt(s));
            ProxyServer.getInstance().getServerInfo(s).ping((ping, throwable) -> {
                if (ping == null) {
                    statuses.put(s, new StatusInfo(Status.OFFLINE));
                }
                else {
                    StatusInfo info = new StatusInfo(Status.valueOf(statusConfig.getString(s+".status")), statusConfig.getString(s + ".ranks"));
                    statuses.put(s, info);
                }
            });
        }
        System.out.println("Reloaded server status config!");

    }

    public static void initialize() {
        try {
            slotsConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File("slots.yml"));
        }
        catch (Exception e) {
            throw new RuntimeException("Unable to load slots.yml!", e);
        }

        try {
            statusConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File("status.yml"));
        }
        catch (Exception e) {
            throw new RuntimeException("Unable to load status.yml!", e);
        }

        for (final String s : ProxyServer.getInstance().getServers().keySet()) {
            slots.put(s, slotsConfig.getInt(s));
            ProxyServer.getInstance().getServerInfo(s).ping((ServerPing ping, Throwable throwable) -> {
                if (ping == null) {
                    statuses.put(s, new StatusInfo(Status.OFFLINE));
                }
                else {
                    StatusInfo info = new StatusInfo(Status.valueOf(statusConfig.getString(s+".status")), statusConfig.getString(s+".ranks"));
                    statuses.put(s, info);
                }
            });
        }
    }

    public static StatusInfo getStatusInfo(String server) {
        return statuses.get(server).clone();
    }

    public static void updateStatus(String server, StatusInfo status) {
        if(!status.getStatus().equals(Status.OFFLINE)){
            if(status.getStatus().equals(Status.ONLINE) && getStatusInfo(server).getStatus().equals(Status.OFFLINE)){
                status = new StatusInfo(Status.valueOf(statusConfig.getString(server+".status")), statusConfig.getString(server+".ranks"));
                statuses.put(server, status);
            }
            else{
                statuses.put(server, status);
            }
            statusConfig.set(server + ".status", status.getStatus().toString());
            statusConfig.set(server + ".ranks", status.getRankString());
            try {
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(statusConfig, new File("status.yml"));
            }
            catch (Exception e) {
                throw new RuntimeException("Unable to save status.yml!", e);
            }
        }
        else {
            statuses.put(server, status);
        }
    }

    public static int getVirtualSlots(String server) {
        return slots.get(server);
    }

    public static void setVirtualSlots(String server, int amount) {
        slots.put(server, amount);
        slotsConfig.set(server, amount);
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(slotsConfig, new File("slots.yml"));
        }
        catch (Exception e) {
            throw new RuntimeException("Unable to save slots.yml!", e);
        }
    }

    public static class StatusInfo {

        private Status status;
        private ArrayList<Rank> ranks = new ArrayList<>();

        public StatusInfo(Status status) {
            this.status = status;
            ranks.add(Rank.DEFAULT);
        }

        public StatusInfo(Status status, String rankstring) {
            this.status = status;
            for(String r : rankstring.split(":")){
                ranks.add(Rank.valueOf(r));
            }
        }

        public Status getStatus() {
            return status;
        }

        public ArrayList<Rank> getRanks() {
            return ranks;
        }

        public String getRankString(){
            String rankstring = "";
            for(Rank rank : ranks){
                rankstring += ":"+rank.toString();
            }
            return rankstring.substring(1);
        }

        public String getRankWithColors(){
            String rankstring = "";
            for(Rank rank : ranks){
                rankstring += ", "+rank.withColors();
            }
            return rankstring.substring(2);
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public StatusInfo clone(){
            return new StatusInfo(status, getRankString());
        }

    }

    public enum Status {

        OFFLINE, ONLINE, MAINTENANCE, DEVELOPMENT;

        public static boolean isValid(String name){
            try {
                ServerStatus.Status.valueOf(name);
            } catch (IllegalArgumentException e) {
                return false;
            }
            return true;
        }

        public String withColors(){
            switch(this){
                case OFFLINE:
                    return ChatColor.DARK_RED+this.toString()+ChatColor.RESET;
                case ONLINE:
                    return ChatColor.GREEN+this.toString()+ChatColor.RESET;
                case MAINTENANCE:
                    return ChatColor.RED+this.toString()+ChatColor.RESET;
                case DEVELOPMENT:
                    return ChatColor.GOLD+this.toString()+ChatColor.RESET;
                default:
                    return this.toString();
            }
        }

    }

}
