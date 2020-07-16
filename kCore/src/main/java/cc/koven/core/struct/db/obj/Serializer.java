package cc.koven.core.struct.db.obj;

import cc.koven.core.kCore;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class Serializer {

    public static String serialize(Map<String, Object> map) {
        return kCore.getGson().toJson(map);
    }

    public static Map<String, Object> deserialize(String string) {
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        return kCore.getGson().fromJson(string, type);
    }
}
