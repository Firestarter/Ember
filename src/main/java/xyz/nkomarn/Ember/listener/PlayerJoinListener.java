package xyz.nkomarn.Ember.listener;

import com.mongodb.client.model.Filters;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.bson.Document;
import xyz.nkomarn.Ember.Ember;
import xyz.nkomarn.Ember.util.Config;
import xyz.nkomarn.Kerosene.util.Webhooks;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Level;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PostLoginEvent event) {
        ForkJoinPool.commonPool().submit(() -> {
            final String uuid = event.getPlayer().getUniqueId().toString();
            final Document existingDocument = Ember.getPlayerData().sync().find(Filters.eq("uuid", uuid)).first();
            if (existingDocument != null) return;

            // Create a new player data document
            final Document playerDocument = new Document("uuid", uuid)
                    .append("joined", System.currentTimeMillis())
                    .append("playtime", 0)
                    .append("deaths", 0)
                    .append("votes", 0)
                    .append("donor", false)
                    .append("backpack", "");
            Ember.getPlayerData().sync().insertOne(playerDocument);
            Ember.getEmber().getLogger().log(Level.INFO, event.getPlayer().getName() + " joined for the first time.");

            // Notify of new player in the Discord notifications channel
            Webhooks hook = new Webhooks(Config.getString("webhook.notifications"));
            hook.addEmbed(new Webhooks.EmbedObject()
                    .setDescription(":checkered_flag: " + event.getPlayer().getName() + " joined!")
                    .setColor(Color.WHITE));
            try {
                hook.execute();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
}
