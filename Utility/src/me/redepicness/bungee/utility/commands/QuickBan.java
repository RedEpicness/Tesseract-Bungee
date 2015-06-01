package me.redepicness.bungee.utility.commands;

import me.redepicness.bungee.database.CustomPlayer;
import me.redepicness.bungee.utility.Utility;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class QuickBan extends Command{

    private static ArrayList<String> banned = new ArrayList<>();

    public static boolean canJoin(String name){
        return banned.contains(name);
    }

    public static void removeQuickBan(String name){
        if(banned.contains(name)){
            banned.remove(name);
        }
    }

    public QuickBan() {
        super("quickban", "", "qban", "qb");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        CustomPlayer player = CustomPlayer.get(commandSender.getName());
        if(!player.hasPermission(true, me.redepicness.bungee.database.Rank.MODERATOR)) return;
        if(args.length == 0){
            player.message(ChatColor.RED+"Not enough arguments. Usage: '"+ChatColor.GOLD+"/quickban <name>"+ChatColor.RED+"'");
            return;
        }
        CustomPlayer target = CustomPlayer.get(args[0]);
        if(!target.isOnline()){
            player.message(ChatColor.RED+"No player with the name '"+ChatColor.GOLD+target.getName()+ChatColor.RED+"' is online!");
            return;
        }
        if(!player.hasPermission(me.redepicness.bungee.database.Rank.ADMIN) && target.hasPermission(me.redepicness.bungee.database.Rank.ADMIN)){
            player.message(ChatColor.RED+"You are not allowed to ban admins!");
            return;
        }
        if(!player.hasPermission(me.redepicness.bungee.database.Rank.JR_DEV) && target.hasPermission(me.redepicness.bungee.database.Rank.JR_DEV)){
            player.message(ChatColor.RED + "You are not allowed to ban Jr Devs!");
            return;
        }
        Utility.sendToStaff(player.getColoredName() + ChatColor.AQUA + " quick-banned " + target.getColoredName() + ChatColor.AQUA + "!");
        Utility.sendToStaff(player.getColoredName() + ChatColor.AQUA + "You have "+ChatColor.GOLD+"2 minutes"+ChatColor.AQUA+" to ban them!");
        target.getProxiedPlayer().disconnect(ChatColor.RED + "You are banned from the server!");
        banned.add(target.getName());
        BungeeCord.getInstance().getScheduler().schedule(BungeeCord.getInstance().getPluginManager().getPlugin("Utility"), () -> {
            if(banned.contains(target.getName())){
                banned.remove(target.getName());
                Utility.sendToStaff(target.getColoredName()+"'s"+ChatColor.AQUA+" quick-ban expired!");
            }
        }, 2, TimeUnit.MINUTES);
    }
}
