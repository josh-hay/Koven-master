package cc.koven.core.struct.cache;

import cc.koven.core.kCore;
import cc.koven.core.struct.db.MongoHandler;
import com.google.common.collect.Maps;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

import java.util.Map;
import java.util.UUID;

public class CacheHandler {

    private kCore kCore;
    private Map<UUID, String> uuidCache;

    public CacheHandler(kCore kCore) {
        this.kCore = kCore;
        uuidCache = Maps.newHashMap();
        load();

        kCore.getServer().getPluginManager().registerEvents(new CacheListener(kCore), kCore);
    }

    public void load() {
        MongoHandler mongoHandler = kCore.getMongoHandler();
        MongoCollection mongoCollection = mongoHandler.getCollection("cache");
        MongoCursor mongoCursor = mongoCollection.find().iterator();

        while (mongoCursor.hasNext()) {
            Document document = (Document) mongoCursor.next();

            addUUID(UUID.fromString(document.getString("uuid")), document.getString("username"));
        }
        if (getUUID("CONSOLE") == null) {
            addUUID(UUID.fromString("f78a4d8d-d51b-4b39-98a3-230f2de0c670"), "CONSOLE");
        }
    }

    public String getUsername(UUID uuid) {
        return uuidCache.get(uuid);
    }

    public UUID getUUID(String username) {
        return uuidCache.keySet().stream().filter(u -> uuidCache.get(u).equalsIgnoreCase(username)).findFirst().orElse(null);
    }

    public void addUUID(UUID uuid, String username) {
        uuidCache.put(uuid, username);
    }
}
