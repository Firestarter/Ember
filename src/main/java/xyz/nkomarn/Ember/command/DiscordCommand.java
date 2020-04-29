package xyz.nkomarn.Ember.command;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;
import ninja.leaping.configurate.ConfigurationNode;
import org.checkerframework.checker.nullness.qual.NonNull;
import xyz.nkomarn.Ember.util.ChatColor;
import xyz.nkomarn.Ember.util.Config;

public class DiscordCommand implements Command {
    @Override
    public void execute(@NonNull CommandSource sender, @NonNull String[] args) {
        ConfigurationNode discordNode = Config.getRoot().getNode("messages").getNode("discord");
        TextComponent textComponent = TextComponent.builder(ChatColor.translate('&', discordNode.getNode("message").getString()))
                .hoverEvent(HoverEvent.showText(TextComponent.of("Click to join the Discord server.").color(TextColor.GRAY)))
                .clickEvent(ClickEvent.openUrl(discordNode.getNode("link").getString()))
                .build();
        sender.sendMessage(textComponent);
    }
}
