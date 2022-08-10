package com.github.domcoon.groups.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import com.github.domcoon.groups.model.group.Group;
import com.github.domcoon.groups.model.group.GroupManager;
import com.github.domcoon.groups.model.node.GroupNode;
import com.github.domcoon.groups.model.user.User;
import com.github.domcoon.groups.model.user.UserManager;
import com.github.domcoon.groups.util.DurationUtil;
import org.bukkit.entity.Player;

@CommandAlias("rank")
public class RankCommand extends BaseCommand {
    private final UserManager userManager;
    private final GroupManager groupManager;

    public RankCommand(UserManager userManager, GroupManager groupManager) {
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

        player.sendMessage("Your Rank is: " + group.getName());
        player.sendMessage("Your Prefix is: " + group.getPrefix());
        player.sendMessage("Your Weight is: " + group.getWeight());
        player.sendMessage("Time left is: " + DurationUtil.formatDuration(groupNode.getTimeLeft(), "FOREVER"));
    }
}
