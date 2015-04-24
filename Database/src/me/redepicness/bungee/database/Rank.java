package me.redepicness.bungee.database;

import net.md_5.bungee.api.ChatColor;

public enum Rank {

    DEFAULT, BUILDER, HELPER, MODERATOR, JR_DEV, ADMIN;

    public boolean isStaffRank() {
        return this == HELPER || this == MODERATOR || this == JR_DEV || this == ADMIN;
    }

    public boolean isPaidRank() {
        return false;
    }

    public static boolean isValid(String name){
        try {
            Rank.valueOf(name);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    public String withColors(){
        switch (this){
            case DEFAULT:
                return ChatColor.WHITE+this.toString()+ChatColor.RESET;
            case BUILDER:
                return ChatColor.DARK_AQUA+this.toString()+ChatColor.RESET;
            case HELPER:
                return ChatColor.BLUE+this.toString()+ChatColor.RESET;
            case MODERATOR:
                return ChatColor.DARK_GREEN+this.toString()+ChatColor.RESET;
            case JR_DEV:
                return ChatColor.GREEN+this.toString()+ChatColor.RESET;
            case ADMIN:
                return ChatColor.RED+this.toString()+ChatColor.RESET;
            default:
                return this.toString();
        }
    }

}
