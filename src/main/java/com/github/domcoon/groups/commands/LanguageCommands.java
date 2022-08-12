package com.github.domcoon.groups.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import com.github.domcoon.groups.GroupsPlugin;
import com.github.domcoon.groups.PrefixedException;
import com.github.domcoon.groups.lang.LangKeys;
import com.github.domcoon.groups.lang.MessageFactory;
import org.bukkit.entity.Player;

@CommandAlias("language")
public class LanguageCommands extends BaseCommand {
    private final GroupsPlugin plugin;
    private final MessageFactory messageFactory;

    public LanguageCommands(GroupsPlugin plugin, MessageFactory messageFactory) {
        this.plugin = plugin;
        this.messageFactory = messageFactory;
    }

    @Subcommand("set")
    @CommandPermission("language.set.self")
    @Syntax("<language-key>")
    public void setLanguage(Player player, String language) {
        try {
            this.messageFactory.setLanguage(player, language);
            this.plugin.sendLocalizedMessage(player, LangKeys.LANGUAGE_SET);
        } catch (PrefixedException ex) {
            this.plugin.sendLocalizedMessage(player, ex.getMessage(), ex.getValues());
        }
    }
}
