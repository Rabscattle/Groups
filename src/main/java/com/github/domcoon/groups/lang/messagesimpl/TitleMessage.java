package com.github.domcoon.groups.lang.messagesimpl;

import com.github.domcoon.groups.lang.Message;
import com.github.domcoon.groups.placeholders.PlaceholderPair;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class TitleMessage implements Message {
    private String title;
    private String subtitle;
    private int fadeIn;
    private int stay;
    private int fadeOut;

    public TitleMessage(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        this.title = title;
        this.subtitle = subtitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    public TitleMessage(ConfigurationSection section) {
        this.title = section.getString("title");
        this.subtitle = section.getString("subtitle", "");
        this.fadeIn = section.getInt("fade-in", 0);
        this.stay = section.getInt("stay", 20);
        this.fadeOut = section.getInt("fade-out", 0);
    }

    @Override
    public void sendMessage(CommandSender sender, PlaceholderPair[] values) {
        if (!(sender instanceof Player)) return;
        ((Player) sender).sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }
}
