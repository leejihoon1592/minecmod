package com.jangi10.mineblacksmith.core.registry;

import com.jangi10.mineblacksmith.core.data.OreData;

import java.util.HashMap;
import java.util.Map;

public class OreRegistry {

    private static final Map<String, OreData> ORES = new HashMap<>();

    public static void register(OreData data) {
        ORES.put(data.getOreItemId(), data);
    }

    public static OreData get(String oreItemId) {
        return ORES.get(oreItemId);
    }

    public static boolean has(String oreItemId) {
        return ORES.containsKey(oreItemId);
    }

    public static void clear() {
        ORES.clear();
    }

    private OreRegistry() {}
}
