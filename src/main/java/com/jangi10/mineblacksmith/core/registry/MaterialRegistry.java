package com.jangi10.mineblacksmith.core.registry;

import com.jangi10.mineblacksmith.core.data.IngotData;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class MaterialRegistry {

    private static final Map<String, IngotData> MATERIALS = new ConcurrentHashMap<>();

    public static void register(IngotData data) {
        if (data == null) return;

        String key = data.getId();
        if (key == null || key.isBlank()) return;

        MATERIALS.put(key, data);
    }

    public static Optional<IngotData> getMaterial(String id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(MATERIALS.get(id));
    }

    public static Collection<IngotData> getAllMaterials() {
        return Collections.unmodifiableCollection(MATERIALS.values());
    }

    public static boolean isEmpty() {
        return MATERIALS.isEmpty();
    }

    public static void clear() {
        MATERIALS.clear();
    }

    private MaterialRegistry() {}
}
