package xyz.nkomarn.Ember.command;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;
import ninja.leaping.configurate.ConfigurationNode;
import org.checkerframework.checker.nullness.qual.NonNull;
import xyz.nkomarn.Ember.Ember;
import xyz.nkomarn.Ember.data.PlayerData;
import xyz.nkomarn.Ember.util.ChatColor;
import xyz.nkomarn.Ember.util.Config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;

public class VoteCommand implements Command {
    @Override
    public void execute(@NonNull CommandSource sender, @NonNull String[] args) {
        if (!(sender instanceof Player)) return;

        Ember.getProxy().getScheduler().buildTask(Ember.getEmber(), () -> {
            try (Connection connection = PlayerData.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement("SELECT `votes` FROM `playerdata` " +
                        "WHERE `uuid` = ?;")) {
                    statement.setString(1, ((Player) sender).getUniqueId().toString());
                    try (ResultSet result = statement.executeQuery()) {
                        if (result.next()) {
                            ConfigurationNode voteNode = Config.getRoot().getNode("messages").getNode("vote");
                            TextComponent textComponent = TextComponent.builder(ChatColor.translate('&', voteNode
                                    .getNode("message").getString().replace("[votes]", NumberFormat.getNumberInstance(Locale.US)
                                            .format(result.getInt(1)))))
                                    .hoverEvent(HoverEvent.showText(TextComponent.of("Click to open the vote page.").color(TextColor.GRAY)))
                                    .clickEvent(ClickEvent.openUrl(voteNode.getNode("link").getString()))
                                    .build();
                            sender.sendMessage(textComponent);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).schedule();
    }
}
