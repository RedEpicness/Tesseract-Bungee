package me.redepicness.bungee.utility.commands;

import me.redepicness.bungee.database.CustomPlayer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;

public class Connect extends Command {

    public Connect(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        CustomPlayer player = CustomPlayer.get(commandSender.getName());
        if(player.isConsole()) return;
        player.getProxiedPlayer().connect(ProxyServer.getInstance().getServerInfo(this.getName()));
    }
}
