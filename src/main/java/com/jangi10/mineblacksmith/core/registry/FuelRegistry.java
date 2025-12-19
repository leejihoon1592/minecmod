package com.jangi10.mineblacksmith.core.registry;

import com.jangi10.mineblacksmith.core.data.FuelData;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class FuelRegistry {

    private static final Map<String, FuelData> FUELS = new ConcurrentHashMap<>();

    public static void register(FuelData data) {
        if (data == null) return;

        String key = data.getId();
        if (key == null || key.isBlank()) return;

        FUELS.put(key, data);
    }

    public static Optional<FuelData> get(String id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(FUELS.get(id));
    }

    public static Collection<FuelData> getAll() {
        return Collections.unmodifiableCollection(FUELS.values());
    }

    public static boolean isEmpty() {
        return FUELS.isEmpty();
    }

    public static void clear() {
        FUELS.clear();
    }

    private FuelRegistry() {}
}
