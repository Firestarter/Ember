package xyz.nkomarn.Ember.command;

import com.mongodb.client.model.Filters;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.bson.Document;
import xyz.nkomarn.Ember.Ember;
import xyz.nkomarn.Ember.util.Config;
import xyz.nkomarn.Kerosene.database.subscribers.BasicSubscriber;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Playtime extends Command {

    public Playtime() {
        super("playtime", "", "joined", "played");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        final String border = Config.getString("playtime.border");
        final String playtime = Config.getString("playtime.playtime");
        final String joined = Config.getString("playtime.joined");

        if (!(sender instanceof ProxiedPlayer)) return;
        final ProxiedPlayer player = (ProxiedPlayer) sender;

        Ember.playerData.async().find(Filters.eq("uuid", player.getUniqueId().toString()))
                .subscribe(new BasicSubscriber<Document>() {
                    public void onNext(Document doc) {
                        final DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
                        final int playtimeMinutes = doc.getInteger("playtime");
                        final long joinedTime = doc.getLong("joined");
                        final TextComponent textComponent = new TextComponent(ChatColor.translateAlternateColorCodes('&',
                                border + "\n" + playtime.replace("[playtime]", intToTimeString(playtimeMinutes))
                                        + joined.replace("[joined]", dateFormat.format(joinedTime)) + border));
                        player.sendMessage(textComponent);
                    }
                });
    }

    // Converts an integer into a time string
    private String intToTimeString(int time) {
        return time / 24 / 60 + " days, " + time / 60 % 24 + " hours, and " + time % 60 + " minutes";
    }
}
