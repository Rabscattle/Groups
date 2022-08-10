package com.github.domcoon.groups.lang;

import com.github.domcoon.groups.placeholders.PlaceholderPair;
import org.bukkit.command.CommandSender;

public interface Message {
    void sendMessage(CommandSender sender, PlaceholderPair[] values);
}
