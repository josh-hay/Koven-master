package cc.koven.core.struct.db;

import cc.koven.core.kCore;
import cc.koven.core.struct.config.FileManager;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Collections;

@Getter
public class MongoHandler {

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    public MongoHandler(kCore kCore) {
        FileManager fileManager = kCore.getFileManager();
        ConfigurationSection mongoConfig = fileManager.getConfig().getConfigurationSection("mongo");
        String ip = mongoConfig.getString("mongo.ip");
        int port = mongoConfig.getInt("mongo.port");
        String database = mongoConfig.getString("mongo.database");
        boolean auth = mongoConfig.getBoolean("mongo.auth.enabled");
        String username = mongoConfig.getString("mongo.auth.username");
        String password = mongoConfig.getString("mongo.auth.password");

        if (auth) {
            mongoClient = new MongoClient(new ServerAddress(ip, port), Collections.singletonList(MongoCredential.createCredential(username, database, password.toCharArray())));
        } else {
            mongoClient = new MongoClient(new ServerAddress(ip, port));
        }
        mongoDatabase = mongoClient.getDatabase(database);
        mongoClient.getAddress();
    }

    public MongoCollection getCollection(String collection) {
        return mongoDatabase.getCollection(collection);
    }

    public boolean collectionExists(String collection) {
        boolean exists = false;
        for (String collections : mongoDatabase.listCollectionNames()) {
            if (collection.equalsIgnoreCase(collections)) exists = true;
        }
        return exists;
    }
}
