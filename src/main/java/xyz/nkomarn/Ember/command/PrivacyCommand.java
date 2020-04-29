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

public class PrivacyCommand implements Command {
    @Override
    public void execute(@NonNull CommandSource sender, @NonNull String[] args) {
        ConfigurationNode privacyNode = Config.getRoot().getNode("messages").getNode("privacy");
        TextComponent textComponent = TextComponent.builder(ChatColor.translate('&', privacyNode.getNode("message").getString()))
                .hoverEvent(HoverEvent.showText(TextComponent.of("Click to read the privacy policy.").color(TextColor.GRAY)))
                .clickEvent(ClickEvent.openUrl(privacyNode.getNode("link").getString()))
                .build();
        sender.sendMessage(textComponent);
    }
}
