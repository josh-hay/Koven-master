package cc.koven.core.struct.cache;

import cc.koven.core.kCore;
import cc.koven.core.struct.db.MongoHandler;
import cc.koven.core.struct.db.RedisHandler;
import cc.koven.core.utils.generic.Maps;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import lombok.AllArgsConstructor;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
public class CacheListener implements Listener {

    private kCore kCore;

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        CacheHandler cacheHandler = kCore.getCacheHandler();

        if (cacheHandler.getUUID(player.getName()) == null || !cacheHandler.getUUID(player.getName()).equals(player.getUniqueId()) || !cacheHandler.getUsername(player.getUniqueId()).equalsIgnoreCase(player.getName())) {
            Map<String, Object> map = Maps.createMap("uuid", player.getUniqueId().toString(), "username", player.getName());

            MongoHandler mongoHandler = kCore.getMongoHandler();
            MongoCollection mongoCollection = mongoHandler.getCollection("cache");

            CompletableFuture.runAsync(() -> {
                Document document = (Document) mongoCollection.find(Filters.eq("uuid", player.getUniqueId().toString())).first();

                if (document != null) {
                    mongoCollection.deleteOne(document);
                }
                Map<String, Object> documentMap = Maps.createMap("uuid", player.getUniqueId().toString(), "username", player.getName());
                mongoCollection.insertOne(new Document(documentMap));
            });

            RedisHandler redisHandler = kCore.getRedisHandler();
            redisHandler.sendAsync("add-cache", map);
        }
    }
}
