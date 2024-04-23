package com.github.dschreid.groups.lang;

import com.github.dschreid.groups.placeholders.PlaceholderPair;
import org.bukkit.command.CommandSender;

public interface Message {
    void sendMessage(CommandSender sender, PlaceholderPair[] values);
}
