package com.jangi10.mineblacksmith.core.init;

import com.jangi10.mineblacksmith.core.data.FuelData;
import com.jangi10.mineblacksmith.core.registry.FuelRegistry;

import java.io.File;

/**
 * [BlacksmithCoreBootstrap]
 * 코어 초기화는 여기 한 군데서만 수행한다.
 *
 * ✅ 초기화 순서(고정)
 * 1) config/mineblacksmith/physics.json 로드 (있으면)
 * 2) config/mineblacksmith/fuels/*.json 기본 생성 + 로드 + FuelRegistry 등록
 * 3) config/mineblacksmith/ingots/*.json 기본 생성 + 로드 + MaterialRegistry 등록
 * 4) sanity check (값이 이상한 데이터 무시/보정은 "나중에" 확장)
 */
public class BlacksmithCoreBootstrap {

    private static boolean bootstrapped = false;

    /** 중복 호출 방지 */
    public static synchronized void bootstrap(File configDir) {
        if (bootstrapped) {
            System.out.println("[MineBlacksmith] Core bootstrap skipped (already bootstrapped).");
            return;
        }
        bootstrapped = true;

        if (configDir == null) {
            System.out.println("[MineBlacksmith] configDir is null. Using ./config");
            configDir = new File("config");
        }

        // 1) 물리/연료 로드(기본 생성 포함)
        ConfigLoader.loadAll(configDir);

        // 2) 주괴/재료 로드(기본 생성 포함)
        MaterialDataLoader.loadIngots();

        // 3) 기본 연료 안전망(혹시 fuels 로딩이 비어있을 때)
        ensureDefaultFuelsIfEmpty();

        // 4) 광석(Ore) 로드 (기초 광석 → 재련용 데이터)
        OreDataLoader.load(configDir);


        System.out.println("[MineBlacksmith] Core bootstrap complete.");
    }

    private static void ensureDefaultFuelsIfEmpty() {
        // FuelRegistry 내부 구조를 모르는 상태라 "조회 메서드"가 없으면 여기선 최소 안전망만 깔아둠.
        // FuelRegistry가 비어있어도 계산이 진행되면 NPE 위험이 커서, 기본 4개는 확실히 넣는다.
        registerFuelSafely(ModFuels.WOOD);
        registerFuelSafely(ModFuels.CHARCOAL);
        registerFuelSafely(ModFuels.COAL);
        registerFuelSafely(ModFuels.COKE);

    }

    private static void registerFuelSafely(FuelData data) {
        try {
            if (data != null) FuelRegistry.register(data);
        } catch (Exception ignored) {
            // 중복 등록이 터지면 무시(부트스트랩 안전성)
        }
    }

    private BlacksmithCoreBootstrap() {}
}
