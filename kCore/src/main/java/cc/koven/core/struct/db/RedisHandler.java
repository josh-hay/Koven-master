package cc.koven.core.struct.db;

import cc.koven.core.kCore;
import cc.koven.core.struct.config.FileManager;
import cc.koven.core.struct.db.obj.Packet;
import cc.koven.core.struct.db.obj.Serializer;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Getter;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.bukkit.configuration.ConfigurationSection;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class RedisHandler {

    private final JedisPool jedisPool;
    private Set<Object> listeners;

    public RedisHandler(kCore kCore) {
        FileManager fileManager = kCore.getFileManager();
        ConfigurationSection redisConfig = fileManager.getConfig().getConfigurationSection("redis");
        String host = redisConfig.getString("host");
        int port = redisConfig.getInt("port"),
                timeout = redisConfig.getInt("timeout");
        boolean usePassword = redisConfig.getBoolean("auth.enabled");
        String password = redisConfig.getString("auth.password");
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(20);
        this.jedisPool = new JedisPool(config, host, port, timeout);
        if(usePassword) {
            jedisPool.getResource().auth(password);
        }
        listeners = Sets.newHashSet();
    }

    public void initialize() {
        Set<String> subscribedChannels = Sets.newHashSet();
        Map<String, Pair<Object, Method>> map = Maps.newHashMap();

        listeners.forEach(listener -> {
            Set<Method> methods = Stream.of(listener.getClass().getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(Packet.class))
                    .collect(Collectors.toSet());
            for (Method method : methods) {
                Packet packet = method.getAnnotation(Packet.class);
                if (!subscribedChannels.contains(packet.value())) {
                    map.put(packet.value(), new ImmutablePair<>(listener, method));
                    subscribedChannels.add(packet.value());
                }
            }
        });

        CompletableFuture.runAsync(() -> {
            Jedis jedis = jedisPool.getResource();
            jedis.subscribe(new JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {
                    map.forEach((c, p) -> {
                        if (channel.equalsIgnoreCase(c)) {
                            try {
                                p.getValue().invoke(p.getKey(), Serializer.deserialize(message));
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }, subscribedChannels.toArray(new String[0]));
        });
    }

    public void sendSync(String channel, Map<String, Object> message) {
        Jedis jedis = jedisPool.getResource();
        jedis.publish(channel, Serializer.serialize(message));
    }

    public void sendAsync(String channel, Map<String, Object> message) {
        CompletableFuture.runAsync(() -> {
            Jedis jedis = jedisPool.getResource();
            jedis.publish(channel, Serializer.serialize(message));
        });
    }

    public void registerPacketListeners(Object... objects) {
        getListeners().addAll(Arrays.asList(objects));
    }
}
