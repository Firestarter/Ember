package xyz.nkomarn.Ember.task;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Filters;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bson.Document;
import xyz.nkomarn.Ember.Ember;

public class PlaytimeCounter implements Runnable {
    @Override
    public void run() {
        for (ProxiedPlayer player : Ember.instance.getProxy().getPlayers()) {
            final String uuid = player.getUniqueId().toString();
            Ember.instance.getProxy().getScheduler().runAsync(Ember.instance, () -> {
                Ember.playerData.sync().updateOne(Filters.eq("uuid", uuid),
                        new Document("$inc", new BasicDBObject().append("playtime", 1)));
            });
        }
    }
}
