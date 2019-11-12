package xyz.nkomarn.Ember.task;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bson.Document;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import xyz.nkomarn.Ember.Ember;

public class PlaytimeCounter implements Runnable {
    @Override
    public void run() {
        for (ProxiedPlayer player : Ember.instance.getProxy().getPlayers()) {
            final String uuid = player.getUniqueId().toString();
            Ember.playerData.async().updateOne(Filters.eq("uuid", uuid),
                    new Document("$inc", new BasicDBObject().append("playtime", 1)))
                    .subscribe(new Subscriber<UpdateResult>() {
                        public void onSubscribe(Subscription subscription) { }
                        public void onNext(UpdateResult updateResult) { }
                        public void onError(Throwable throwable) { }
                        public void onComplete() { }
                    });
        }
    }
}
