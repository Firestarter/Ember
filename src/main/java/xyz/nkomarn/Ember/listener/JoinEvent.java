package xyz.nkomarn.Ember.listener;

import com.mongodb.client.model.Filters;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.bson.Document;
import xyz.nkomarn.Ember.Ember;

import java.util.concurrent.ForkJoinPool;
import java.util.logging.Level;

public class JoinEvent implements Listener {

    @EventHandler
    public void onPlayerJoin(PostLoginEvent event) {
        final String uuid = event.getPlayer().getUniqueId().toString();
        final Document existingDocument = Ember.playerData.sync().find(Filters.eq("uuid", uuid)).first();
        if (existingDocument != null) return;

        // Create a new player data document
        final Document playerDocument = new Document("uuid", uuid)
                .append("joined", System.currentTimeMillis())
                .append("playtime", 0)
                .append("deaths", 0)
                .append("votes", 0)
                .append("backpack", "");
        ForkJoinPool.commonPool().submit(() -> Ember.playerData.sync().insertOne(playerDocument));
        Ember.instance.getLogger().log(Level.INFO, event.getPlayer().getName()
                + " joined for the first time.");
    }

}
