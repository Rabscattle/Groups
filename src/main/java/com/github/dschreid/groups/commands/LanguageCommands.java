package com.github.dschreid.groups.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import com.github.dschreid.groups.GroupsPlugin;
import com.github.dschreid.groups.PrefixedException;
import com.github.dschreid.groups.events.LanguageChangeEvent;
import com.github.dschreid.groups.lang.LangKeys;
import com.github.dschreid.groups.lang.MessageFactory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("language")
public class LanguageCommands extends ABaseCommand {
    private final GroupsPlugin plugin;
    private final MessageFactory messageFactory;

    public LanguageCommands(GroupsPlugin plugin, MessageFactory messageFactory) {
        this.plugin = plugin;
        this.messageFactory = messageFactory;
    }

    @Subcommand("set")
    //@CommandPermission("language.set.self")
    @Syntax("<language-key>")
    public void setLanguage(Player player, String language) {
        try {
            this.messageFactory.setLanguage(player, language);
            this.plugin.sendLocalizedMessage(player, LangKeys.LANGUAGE_SET);
            LanguageChangeEvent event = new LanguageChangeEvent(player, language);
            Bukkit.getPluginManager().callEvent(event);
        } catch (PrefixedException ex) {
            this.plugin.sendLocalizedMessage(player, ex.getMessage(), ex.getValues());
        }
    }
}
