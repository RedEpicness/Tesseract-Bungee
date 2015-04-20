package me.redepicness.bungee.database;

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

    public boolean hasRank(Rank rank) {
        if(ranks == null){
            getRanks();
        }
        return ranks.contains(rank);
    }

    public ArrayList<Rank> getRanks(){
        if(ranks != null) return ranks;
        String rank = Database.getProperty(name, "Ranks");
        ranks = new ArrayList<Rank>();
        for(String r : rank.split(",")){
            ranks.add(Rank.valueOf(r.toUpperCase()));
        }
        return ranks;
    }

}
