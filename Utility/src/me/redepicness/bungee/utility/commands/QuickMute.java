package me.redepicness.bungee.utility.commands;

import me.redepicness.bungee.database.CustomPlayer;
import me.redepicness.bungee.utility.Utility;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class QuickMute extends Command{

    private static ArrayList<String> muted = new ArrayList<>();

    public static boolean canTalk(String name){
        return muted.contains(name);
    }

    public static void removeQuickMute(String name){
        if(muted.contains(name)){
            muted.remove(name);
        }
    }

    public QuickMute() {
        super("quickmute", "", "qmute", "qm");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        CustomPlayer player = CustomPlayer.get(commandSender.getName());
        if(!player.hasPermission(true, me.redepicness.bungee.database.Rank.MODERATOR)) return;
        if(args.length == 0){
            player.message(ChatColor.RED+"Not enough arguments. Usage: '"+ChatColor.GOLD+"/quickmute <name>"+ChatColor.RED+"'");
            return;
        }
        CustomPlayer target = CustomPlayer.get(args[0]);
        if(!target.isOnline()){
            player.message(ChatColor.RED+"No player with the name '"+ChatColor.GOLD+target.getName()+ChatColor.RED+"' is online!");
            return;
        }
        if(!player.hasPermission(me.redepicness.bungee.database.Rank.ADMIN) && target.hasPermission(me.redepicness.bungee.database.Rank.ADMIN)){
            player.message(ChatColor.RED+"You are not allowed to mute admins!");
            return;
        }
        if(!player.hasPermission(me.redepicness.bungee.database.Rank.JR_DEV) && target.hasPermission(me.redepicness.bungee.database.Rank.JR_DEV)){
            player.message(ChatColor.RED + "You are not allowed to mute Jr Devs!");
            return;
        }
        Utility.sendToStaff(player.getColoredName() + ChatColor.AQUA + " quick-muted " + target.getColoredName() + ChatColor.AQUA + "!");
        Utility.sendToStaff(player.getColoredName() + ChatColor.AQUA + "You have "+ChatColor.GOLD+"2 minutes"+ChatColor.AQUA+" to mute them!");
        target.message(ChatColor.RED + "You were muted!");
        muted.add(target.getName());
        BungeeCord.getInstance().getScheduler().schedule(BungeeCord.getInstance().getPluginManager().getPlugin("Utility"), () -> {
            if(muted.contains(target.getName())){
                muted.remove(target.getName());
                Utility.sendToStaff(target.getColoredName()+"'s"+ChatColor.AQUA+" quick-mute expired!");
            }
        }, 2, TimeUnit.MINUTES);
    }

}
