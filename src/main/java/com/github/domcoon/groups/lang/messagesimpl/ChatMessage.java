package com.github.domcoon.groups.lang.messagesimpl;

import com.github.domcoon.groups.lang.Message;
import com.google.common.base.Strings;
import org.bukkit.command.CommandSender;

public class ChatMessage implements Message {
    private final String messageTemplate;

    public ChatMessage(String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    @Override
    public void sendMessage(CommandSender sender) {
        if (!Strings.isNullOrEmpty(messageTemplate))
            sender.sendMessage(this.messageTemplate);
    }
}
