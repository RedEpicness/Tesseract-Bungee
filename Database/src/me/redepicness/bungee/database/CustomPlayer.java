package me.redepicness.bungee.database;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;

public class CustomPlayer{

    private String name;
    private ArrayList<Rank> ranks = null;

    public CustomPlayer(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isConsole(){
        return name.equals("CONSOLE");
    }

    public String getFormattedName(){
        if(isConsole()) return ChatColor.GRAY+name;
        if(hasRank(Rank.ADMIN))
            return ChatColor.RED+"Admin "+name+ChatColor.RESET;
        if(hasRank(Rank.MODERATOR))
            return ChatColor.DARK_GREEN+"Mod "+name+ChatColor.RESET;
        if(hasRank(Rank.HELPER))
            return ChatColor.BLUE+"Helper "+name+ChatColor.RESET;
        if(hasRank(Rank.JR_DEV))
            return ChatColor.GREEN+"Jr Dev "+name+ChatColor.RESET;
        if(hasRank(Rank.BUILDER))
            return ChatColor.DARK_AQUA+"Builder "+name+ChatColor.RESET;
        return name;
    }

    public boolean hasRank(Rank rank) {
        if(isConsole()) return true;
        if(!exists()) Database.generateNewUser(name, getProxiedPlayer().getUUID());
        if(ranks == null) getRanks();
        assert ranks != null;
        return ranks.contains(rank);
    }

    public ArrayList<Rank> getRanks(){
        if(isConsole()) {
            ArrayList<Rank> ranks = new ArrayList<>();
            ranks.add(Rank.ADMIN);
            return ranks;
        }
        if(!exists()) Database.generateNewUser(name, getProxiedPlayer().getUUID());
        if(ranks != null) return ranks;
        String rank = Database.getProperty(name, "Ranks");
        ranks = new ArrayList<>();
        if(rank == null) ranks.add(Rank.DEFAULT);
        else for(String r : rank.split(":")){
            ranks.add(Rank.valueOf(r.toUpperCase()));
        }
        return ranks;
    }

    public void addRank(Rank rank){
        ranks.add(rank);
        Database.updateProperty(name, "Ranks", getRankString());
    }

    public void removeRank(Rank rank){
        ranks.remove(rank);
        if(ranks.isEmpty()){
            ranks.add(Rank.DEFAULT);
            Database.updateProperty(name, "Ranks", null);
            return;
        }
        Database.updateProperty(name, "Ranks", getRankString());
    }

    private String getRankString(){
        String rankstring = "";
        for(Rank rank : ranks){
            rankstring += ":"+rank.toString();
        }
        return rankstring.substring(1);
    }

    public boolean hasPermission(Rank... rankList){
        return hasPermission(false, rankList);
    }

    public boolean hasPermission(boolean inform, Rank... rankList){
        if(isConsole()) return true;
        if(!exists()) Database.generateNewUser(name, getProxiedPlayer().getUUID());
        if(ranks == null) getRanks();
        assert ranks != null;
        if(rankList.length > 1){
            boolean pass = false;
            for(Rank rank : rankList){
                if(hasPermission(false, rank)){
                    pass = true;
                }
            }
            if(inform && !pass) noPermission();
            return pass;
        }
        if(rankList.length == 0){
            if(inform) noPermission();
            return false;
        }
        if(ranks.contains(Rank.ADMIN)) return true;
        boolean pass = false;
        switch (rankList[0]){
            case DEFAULT:
                pass = true;
                break;
            case BUILDER:
                pass = ranks.contains(Rank.BUILDER);
                break;
            case HELPER:
                pass = ranks.contains(Rank.HELPER) || ranks.contains(Rank.MODERATOR) || ranks.contains(Rank.JR_DEV);
                break;
            case MODERATOR:
                pass = ranks.contains(Rank.MODERATOR) || ranks.contains(Rank.JR_DEV);
                break;
            case JR_DEV:
                pass = ranks.contains(Rank.JR_DEV);
                break;
        }
        if(inform && !pass) noPermission();
        return pass;
    }

    public void message(String... message){
        if(isConsole()){
            ProxyServer.getInstance().getConsole().sendMessages(message);
            return;
        }
        getProxiedPlayer().sendMessages(message);
    }

    public ProxiedPlayer getProxiedPlayer(){
        if(isConsole()) return null;
        return ProxyServer.getInstance().getPlayer(name);
    }

    public boolean isOnline(){
        return getProxiedPlayer() != null;
    }

    public boolean exists() {
        if(isConsole()) return true;
        try{
            Database.getProperty(name, "Name");
        }
        catch(IllegalArgumentException e){
            return false;
        }
        return true;
    }

    private void noPermission() {
        message(ChatColor.RED+"You do not have permission to do this!");
    }
}
