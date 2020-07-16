package cc.koven.core.struct.cache.packets;

import cc.koven.core.kCore;
import cc.koven.core.struct.cache.CacheHandler;
import cc.koven.core.struct.db.obj.Packet;
import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public class AddCachePacket {

    private kCore kCore;

    @Packet("add-cache")
    public void onCacheUpdate(Map<String, Object> messageMap) {
        String uuid = (String) messageMap.get("uuid");
        String username = (String) messageMap.get("username");

        CacheHandler cacheHandler = kCore.getCacheHandler();
        cacheHandler.addUUID(UUID.fromString(uuid), username);

    }
}
