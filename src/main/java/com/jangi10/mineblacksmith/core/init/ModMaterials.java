package com.jangi10.mineblacksmith.core.init;

import com.jangi10.mineblacksmith.core.data.IngotData;
import com.jangi10.mineblacksmith.core.registry.MaterialRegistry;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * [ModMaterials - 호환 래퍼]
 * 기존 코드 호환을 위해 유지.
 * 실 저장소는 MaterialRegistry 단일화.
 */
public class ModMaterials {

    public static void register(IngotData data) {
        MaterialRegistry.register(data);
    }

    public static Optional<IngotData> getMaterial(String id) {
        return MaterialRegistry.getMaterial(id);
    }

    /**
     * 기존 코드 호환용. 내부는 MaterialRegistry.values()를 Map으로 변환해서 반환.
     */
    public static Map<String, IngotData> getAllMaterials() {
        return MaterialRegistry.getAllMaterials()
                .stream()
                .collect(Collectors.toMap(IngotData::getId, x -> x));
    }

    private ModMaterials() {}
}
