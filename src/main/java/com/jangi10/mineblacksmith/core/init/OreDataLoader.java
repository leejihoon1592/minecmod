package com.jangi10.mineblacksmith.core.init;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jangi10.mineblacksmith.core.data.OreData;
import com.jangi10.mineblacksmith.core.registry.OreRegistry;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;

public class OreDataLoader {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void load(File configDir) {
        OreRegistry.clear();

        File root = new File(configDir, "mineblacksmith/ores");
        if (!root.exists()) root.mkdirs();

        // 기본 ore 매핑(기초 광석 -> 주괴) 자동 생성
        ConfigGenerator.createDefaultOreConfigs(configDir);

        File[] files = root.listFiles(f -> f.getName().endsWith(".json"));
        if (files == null) return;

        for (File file : files) {
            try (Reader reader = new FileReader(file)) {
                OreDataJson json = GSON.fromJson(reader, OreDataJson.class);
                OreData data = new OreData(json.oreItemId, json.resultIngotId);

                if (json.impurities != null) {
                    for (ImpurityJson i : json.impurities) {
                        if (i == null) continue;
                        data.addImpurity(i.id, i.minTemp, i.maxTemp);
                    }
                }

                OreRegistry.register(data);

            } catch (Exception e) {
                System.err.println("[MineBlacksmith] Failed to load ore: " + file.getName());
                e.printStackTrace();
            }
        }
    }

    // ------------------------

    private static class OreDataJson {
        String oreItemId;
        String resultIngotId;
        ImpurityJson[] impurities;
    }

    private static class ImpurityJson {
        String id;
        int minTemp;
        int maxTemp;
    }

    // ==============================
// 용탕/배출(주조) 세션값  (문서 기준)
// - 조각 단위(fragment) 저장
// - 배출 파이프가 기본 OPEN, 우클릭 시 LOCK
// ==============================
    private String meltIngotId;     // 현재 용탕의 "메인 금속" (결과 주괴 id)
    private int meltFragments;      // 현재 용탕 조각 수(0~)
    private boolean flowLocked;     // 배출 잠금 상태(LOCK)

    public String getMeltIngotId() { return meltIngotId; }
    public void setMeltIngotId(String meltIngotId) { this.meltIngotId = meltIngotId; }

    public int getMeltFragments() { return meltFragments; }
    public void setMeltFragments(int meltFragments) { this.meltFragments = Math.max(0, meltFragments); }

    public boolean isFlowLocked() { return flowLocked; }
    public void setFlowLocked(boolean flowLocked) { this.flowLocked = flowLocked; }

    /** 용탕 조각 추가(같은 금속만 누적) */
    public boolean addMelt(String ingotId, int fragments) {
        if (fragments <= 0) return false;
        if (ingotId == null || ingotId.isBlank()) return false;

        if (meltIngotId == null || meltIngotId.isBlank()) {
            meltIngotId = ingotId;
        } else if (!meltIngotId.equals(ingotId)) {
            // 다른 금속이 섞이는 건 확장 설계(2차/3차 합금)에서 처리
            return false;
        }

        meltFragments += fragments;
        return true;
    }

    /** 용탕에서 1조각 배출 */
    public boolean drainOneFragment() {
        if (meltFragments <= 0) return false;
        meltFragments -= 1;
        if (meltFragments == 0) {
            meltIngotId = null;
        }
        return true;
    }

}
