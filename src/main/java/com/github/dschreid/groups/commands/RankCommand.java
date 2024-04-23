package com.github.dschreid.groups.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import com.github.dschreid.groups.GroupsPlugin;
import com.github.dschreid.groups.lang.LangKeys;
import com.github.dschreid.groups.model.group.Group;
import com.github.dschreid.groups.model.group.GroupManager;
import com.github.dschreid.groups.model.node.GroupNode;
import com.github.dschreid.groups.model.user.User;
import com.github.dschreid.groups.model.user.UserManager;
import com.github.dschreid.groups.placeholders.PlaceholderPair;
import com.github.dschreid.groups.util.DurationUtil;
import org.bukkit.entity.Player;

@CommandAlias("rank")
//@CommandPermission("groups.user.rank")
public class RankCommand extends ABaseCommand {
    private final GroupsPlugin plugin;
    private final UserManager userManager;
    private final GroupManager groupManager;

    public RankCommand(GroupsPlugin plugin, UserManager userManager, GroupManager groupManager) {
        this.plugin = plugin;
        this.userManager = userManager;
        this.groupManager = groupManager;
    }

    @Default
    public void showRank(Player player) {
        this.show(player);
    }

    private void show(Player player) {
        User user = userManager.get(player.getUniqueId());
        GroupNode groupNode = groupManager.getGroupNode(user);
        Group group = groupManager.get(groupNode.getGroup());

        if (group == null) {
            player.sendMessage("You have no rank");
            return;
        }
        PlaceholderPair[] placeholders = {
                PlaceholderPair.of("{user}", user.getUsername()),
                PlaceholderPair.of("{group}", group.getName()),
                PlaceholderPair.of("{prefix}", group.getPrefix()),
                PlaceholderPair.of("{weight}", group.getWeight() + ""),
                PlaceholderPair.of(
                        "{time-left}", DurationUtil.formatDuration(groupNode.getTimeLeft(), "&eFOREVER")),
        };
        this.plugin.sendLocalizedMessage(player, LangKeys.USER_RANK_INFO, placeholders);
    }
}
