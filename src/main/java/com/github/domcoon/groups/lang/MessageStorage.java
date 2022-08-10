package com.github.domcoon.groups.lang;

import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class MessageStorage {
    private final String language;
    private final Map<String, Messages> messages = new HashMap<>();

    public MessageStorage(String language) {
        this.language = language;
    }

    public void sendMessage(CommandSender sender, String messageKey) {
        if (!messages.containsKey(messageKey)) {
            sender.sendMessage(messageKey);
            return;
        }

        messages.get(messageKey).sendMessage(sender);
    }
}