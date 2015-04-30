package me.redepicness.bungee.friends.commands;

import me.redepicness.bungee.database.CustomPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;

public class Friend extends Command{

    public Friend() {
        super("f", "", "friends", "friend");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        CustomPlayer player = CustomPlayer.get(commandSender.getName());
        if(args.length < 1){
            player.message(ChatColor.RED+"Not enough arguments. Usage: '"+ ChatColor.GOLD+"/friend <operation>"+ChatColor.RED+"'");
            return;
        }
        switch (args[0]) {
            case "list":
                ArrayList<CustomPlayer> online = new ArrayList<>();
                ArrayList<CustomPlayer> offline = new ArrayList<>();
                for (String name : player.getFriends()) {
                    CustomPlayer friend = CustomPlayer.get(name);
                    if (friend.isOnline()) {
                        online.add(friend);
                    } else {
                        offline.add(friend);
                    }
                }
                player.message(ChatColor.AQUA+"Your friends:");
                for(CustomPlayer friend : online){
                    player.message(ChatColor.AQUA+"- "+friend.getColoredName()+ChatColor.AQUA+" is "+
                            ChatColor.GREEN+"online"+ChatColor.GRAY+" ("+friend.getProxiedPlayer().getServer().getInfo().getName()+")");
                }
                for(CustomPlayer friend : offline){
                    player.message(ChatColor.AQUA+"- "+friend.getColoredName()+ChatColor.AQUA+" is "+
                            ChatColor.RED+"offline");
                }
                break;
            case "requests":
                player.message(ChatColor.AQUA+"Pending requests:");
                for(String name : player.getFriendRequests()){
                    player.message(ChatColor.GOLD+"- "+CustomPlayer.get(name).getColoredName());
                }
                break;
            case "add":
                if (args.length < 2) {
                    player.message(ChatColor.RED + "Not enough arguments. Usage: '" + ChatColor.GOLD + "/friend " + args[0] + " <name>" + ChatColor.RED + "'");
                    return;
                }
                CustomPlayer target = CustomPlayer.get(args[1]);
                if(!target.exists()){
                    player.message(ChatColor.RED+"No player with the name '"+ChatColor.GOLD+target.getName()+ChatColor.RED+"' has joined the server!");
                    return;
                }
                if(player.hasFriend(target.getName())){
                    player.message(ChatColor.RED+"You're already friends with that player!");
                    return;
                }
                target.requestFriend(player.getName());
                player.message(ChatColor.GREEN + "Sent friend request to '" + target.getFormattedName() + ChatColor.GREEN + "'");
                if(target.isOnline()){
                    target.message(player.getFormattedName()+ChatColor.GOLD+" sent you a friend request.");
                    target.message(ChatColor.GOLD+"Accept by doing: '"+ChatColor.AQUA+"/friend accept "+player.getName()+ChatColor.GOLD+"'");
                }
                break;
            case "remove":
                if (args.length < 2) {
                    player.message(ChatColor.RED + "Not enough arguments. Usage: '" + ChatColor.GOLD + "/friend " + args[0] + " <name>" + ChatColor.RED + "'");
                    return;
                }
                CustomPlayer target1 = CustomPlayer.get(args[1]);
                if(!target1.exists()){
                    player.message(ChatColor.RED+"No player with the name '"+ChatColor.GOLD+target1.getName()+ChatColor.RED+"' has joined the server!");
                    return;
                }
                if(!player.hasFriend(target1.getName())){
                    player.message(ChatColor.RED+"You're not friends with that player!");
                    return;
                }
                player.removeFriend(target1.getName());
                target1.removeFriend(player.getName());
                player.message(ChatColor.GREEN + "Removed '" + target1.getFormattedName() + ChatColor.GREEN + "' from your friends!");
                if(target1.isOnline()){
                    target1.message(ChatColor.RED+"You are no longer friends with '"+player.getFormattedName()+ChatColor.RED+"'");
                }
                break;
            case "accept":
                if (args.length < 2) {
                    player.message(ChatColor.RED + "Not enough arguments. Usage: '" + ChatColor.GOLD + "/friend " + args[0] + " <name>" + ChatColor.RED + "'");
                    return;
                }
                CustomPlayer target2 = CustomPlayer.get(args[1]);
                if(!target2.exists()){
                    player.message(ChatColor.RED+"No player with the name '"+ChatColor.GOLD+target2.getName()+ChatColor.RED+"' has joined the server!");
                    return;
                }
                if(!player.hasFRequestFrom(target2.getName())){
                    player.message(ChatColor.RED+"You don't have a friend request pending from that player!");
                    return;
                }
                player.acceptRequest(target2.getName());
                player.message(ChatColor.GREEN + "You are now friends with '" + target2.getFormattedName() + ChatColor.GREEN + "'!");
                if(target2.isOnline()){
                    target2.message(ChatColor.GREEN+"You are now friends with '"+player.getFormattedName()+ChatColor.GREEN+"'!");
                }
                break;
            case "decline":
                if (args.length < 2) {
                    player.message(ChatColor.RED + "Not enough arguments. Usage: '" + ChatColor.GOLD + "/friend " + args[0] + " <name>" + ChatColor.RED + "'");
                    return;
                }
                CustomPlayer target3 = CustomPlayer.get(args[1]);
                if(!target3.exists()){
                    player.message(ChatColor.RED+"No player with the name '"+ChatColor.GOLD+target3.getName()+ChatColor.RED+"' has joined the server!");
                    return;
                }
                if(!player.hasFRequestFrom(target3.getName())){
                    player.message(ChatColor.RED+"You don't have a friend request pending from that player!");
                    return;
                }
                player.removeRequest(target3.getName());
                player.message(ChatColor.GREEN + "Declined friend request by '" + target3.getFormattedName() + ChatColor.GREEN + "'!");
                if(target3.isOnline()){
                    target3.message(ChatColor.RED+"'"+player.getFormattedName()+ChatColor.RED+"' declined your friend request!");
                }
                break;
        }
    }
}
