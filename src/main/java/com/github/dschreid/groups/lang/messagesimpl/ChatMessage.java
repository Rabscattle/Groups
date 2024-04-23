package com.github.dschreid.groups.lang.messagesimpl;

import com.github.dschreid.groups.lang.Message;
import com.github.dschreid.groups.placeholders.PlaceholderPair;
import com.github.dschreid.groups.util.PlaceholderUtil;
import com.google.common.base.Strings;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ChatMessage implements Message {
    private final String messageTemplate;

    public ChatMessage(String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    @Override
    public void sendMessage(CommandSender sender, PlaceholderPair[] values) {
        if (!Strings.isNullOrEmpty(messageTemplate)) {
            String message = PlaceholderUtil.replacePlaceholders(this.messageTemplate, values);
            message = ChatColor.translateAlternateColorCodes('&', message);
            sender.sendMessage(message);
        }
    }

    public String getContent() {
        return messageTemplate;
    }
}
