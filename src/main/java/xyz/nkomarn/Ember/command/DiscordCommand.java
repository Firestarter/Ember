package xyz.nkomarn.Ember.command;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import xyz.nkomarn.Ember.util.Config;

public class DiscordCommand extends Command {
    public DiscordCommand() {
        super("discord");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        TextComponent textComponent = new TextComponent(ChatColor.translateAlternateColorCodes('&',
                Config.getString("messages.discord.message")));
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, Config.getString("messages.discord.link")));
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(ChatColor.GRAY + "Click to use the Discord invite.").create()));
        sender.sendMessage(textComponent);
    }
}
