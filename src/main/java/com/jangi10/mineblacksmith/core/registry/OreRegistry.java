package com.jangi10.mineblacksmith.core.registry;

import com.jangi10.mineblacksmith.core.data.OreData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * [OreRegistry]
 * - oreId -> OreData 저장소
 * - 기획서: 광물은 불순물 최대 3개 포함 가능, 최종 주괴 ID를 가진다. :contentReference[oaicite:5]{index=5}
 */
public class OreRegistry {

    private static final Map<String, OreData> REGISTRY = new HashMap<>();

    public static void register(OreData data) {
        if (REGISTRY.containsKey(data.getOreId())) {
            throw new IllegalArgumentException("Duplicate ore ID: " + data.getOreId());
        }
        REGISTRY.put(data.getOreId(), data);
    }

    public static Optional<OreData> getOre(String oreId) {
        return Optional.ofNullable(REGISTRY.get(oreId));
    }

    public static Collection<OreData> getAllOres() {
        return REGISTRY.values();
    }

    private OreRegistry() {}
}
