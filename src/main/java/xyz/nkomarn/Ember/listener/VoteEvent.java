package xyz.nkomarn.Ember.listener;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Filters;
import com.vexsoftware.votifier.model.VotifierEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.bson.Document;
import xyz.nkomarn.Ember.Ember;

import java.util.concurrent.ForkJoinPool;

public class VoteEvent implements Listener {

    @EventHandler
    public void onVote(VotifierEvent event) {
        ForkJoinPool.commonPool().submit(() -> {
            final ProxiedPlayer player = Ember.instance.getProxy().getPlayer(event.getVote().getUsername());
            final String uuid = player.getUniqueId().toString();
            Ember.playerData.sync().updateOne(Filters.eq("uuid", uuid), new Document("$inc",
                    new BasicDBObject().append("votes", 1)));

            if (!player.isConnected()) return;

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("vote");
            player.getServer().getInfo().sendData("firestarter", out.toByteArray());
        });
    }

}
