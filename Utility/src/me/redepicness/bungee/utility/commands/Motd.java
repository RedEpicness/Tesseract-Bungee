package me.redepicness.bungee.utility.commands;

import me.redepicness.bungee.database.CustomPlayer;
import me.redepicness.bungee.database.Rank;
import me.redepicness.bungee.utility.main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Motd extends Command{

    public Motd() {
        super("motd");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        CustomPlayer player = CustomPlayer.get(commandSender.getName());
        if(!player.hasPermission(true, Rank.ADMIN)) return;
        String motd = "";
        for(String s : strings){
            motd += " "+s;
        }
        motd = motd.trim();
        main.motdConfig.set("description", motd);
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(main.motdConfig, new File("motd.yml"));
        } catch (IOException e) {
            throw new RuntimeException("Could not save motd.yml", e);
        }
        player.message(ChatColor.GREEN+"Set MOTD to: "+motd.replace('&', ChatColor.COLOR_CHAR));
    }
}
