package xyz.nkomarn.Ember.command;

import com.mongodb.client.model.Filters;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.bson.Document;
import xyz.nkomarn.Ember.Ember;
import xyz.nkomarn.Ember.util.Config;
import xyz.nkomarn.Kerosene.database.subscribers.SimpleSubscriber;

import java.text.NumberFormat;
import java.util.Locale;

public class Vote extends Command {
    public Vote() {
        super("vote");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        final String message = Config.getString("vote.message");
        final String link = Config.getString("vote.link");

        if (!(sender instanceof ProxiedPlayer)) return;
        final ProxiedPlayer player = (ProxiedPlayer) sender;

        Ember.getPlayerData().async().find(Filters.eq("_id", player.getUniqueId().toString()))
                .subscribe(new SimpleSubscriber<Document>() {
                    public void onNext(final Document doc) {
                        final int votes = doc.getInteger("votes");
                        final TextComponent textComponent = new TextComponent(ChatColor
                                .translateAlternateColorCodes('&',
                                        message.replace("[votes]", NumberFormat.getNumberInstance(Locale.US)
                                                .format(votes))));
                        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, link));
                        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GOLD
                            + "Click to open the vote page.").create()));
                        sender.sendMessage(textComponent);
                    }
                }
        );
    }
}
