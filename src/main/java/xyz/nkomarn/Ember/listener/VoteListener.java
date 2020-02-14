package xyz.nkomarn.Ember.listener;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Filters;
import com.vexsoftware.votifier.bungee.events.VotifierEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.bson.Document;
import xyz.nkomarn.Ember.Ember;

import java.util.concurrent.ForkJoinPool;

public class VoteListener implements Listener {
    @EventHandler
    public void onVote(VotifierEvent event) {
        ForkJoinPool.commonPool().submit(() -> {
            final String username = event.getVote().getUsername();
            ProxyServer.getInstance().getLogger().info(String.format("Received vote from %s.", username));
            final ProxiedPlayer player = Ember.getEmber().getProxy().getPlayer(username);
            final String uuid = player.getUniqueId().toString();
            Ember.getPlayerData().sync().updateOne(Filters.eq("_id", uuid), new Document("$inc",
                    new BasicDBObject().append("votes", 1)));

            if (!player.isConnected()) return;

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("vote");
            player.getServer().getInfo().sendData("firestarter:data", out.toByteArray());
        });
    }
}
