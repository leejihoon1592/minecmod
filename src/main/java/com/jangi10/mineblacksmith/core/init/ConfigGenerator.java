package com.jangi10.mineblacksmith.core.init;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jangi10.mineblacksmith.core.data.ForgingStats;
import com.jangi10.mineblacksmith.core.data.FuelData;
import com.jangi10.mineblacksmith.core.data.IngotData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigGenerator {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void createDefaultIngotConfigs(String directoryPath) {
        File ironFile = new File(directoryPath, "iron_ingot.json");
        if (ironFile.exists()) return;

        try (FileWriter writer = new FileWriter(ironFile)) {
            double hardness = 5.0;
            double durability = 5.0;
            double elasticity = 5.0;
            double polishing = 5.0;
            double conductivity = 5.0;

            double calculatedWeight = calculateWeight(hardness, durability, elasticity, polishing, conductivity);

            ForgingStats stats = new ForgingStats(
                    hardness, durability, elasticity, polishing, conductivity, calculatedWeight
            );

            IngotData ironData = new IngotData(
                    "iron_ingot",
                    "Iron Ingot",
                    stats,
                    1480.0,
                    2862.0,
                    1,
                    0xE6E6E6
            );

            GSON.toJson(ironData, writer);
            System.out.println("[ConfigGenerator] 기본 iron_ingot.json 생성 완료");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 기초 금속(모드 전용) 기본 주괴 JSON들을 생성한다.
     * - 이미 파일이 존재하면 건드리지 않는다.
     */
    public static void createDefaultBaseMetalIngotConfigs(String directoryPath) {
        File dir = new File(directoryPath);
        if (!dir.exists()) dir.mkdirs();

        for (DefaultMetalData.Metal m : DefaultMetalData.BASE_METALS) {
            String prefix = m.idPrefix();
            File file = new File(dir, prefix + "_ingot.json");
            if (file.exists()) continue;

            try (FileWriter writer = new FileWriter(file)) {
                // 엑셀 수식본 기반: weight는 기존 calculateWeight로 계산(게임 내 수식과 일치)
                double hardness = m.strength();
                double durability = m.durability();
                double elasticity = m.elasticity();
                double polishing = m.polishing();
                double conductivity = m.conductivity();

                double calculatedWeight = calculateWeight(
                        hardness, durability, elasticity, polishing, conductivity
                );

                ForgingStats stats = new ForgingStats(
                        hardness,
                        durability,
                        elasticity,
                        polishing,
                        conductivity,
                        calculatedWeight
                );

                // id는 Furnace/Ore/Alloy 시스템의 "키"로도 사용됨
                String ingotKey = "mineblacksmith:" + prefix + "_ingot";
                String displayName = m.koName();

                double melting = m.meltingPoint();
                // 끓는점은 기획서에서 확정되기 전까지 안전한 임시값(녹는점 + 1200)
                double boiling = melting + 1200.0;

                IngotData data = new IngotData(
                        ingotKey,
                        displayName,
                        stats,
                        melting,
                        boiling,
                        Math.max(1, m.difficulty()),
                        m.baseColor()
                );

                GSON.toJson(data, writer);
                System.out.println("[ConfigGenerator] 기본 주괴 생성: " + file.getName());
            } catch (Exception e) {
                System.err.println("[ConfigGenerator] 기본 주괴 생성 실패: " + file.getName());
                e.printStackTrace();
            }
        }
    }

    /**
     * 기초 광석(블록 아이템) -> 결과 주괴 매핑 config를 생성한다.
     * - config/mineblacksmith/ores/*.json
     */
    public static void createDefaultOreConfigs(File configDir) {
        if (configDir == null) configDir = new File("config");
        File root = new File(configDir, "mineblacksmith/ores");
        if (!root.exists()) root.mkdirs();

        for (DefaultMetalData.Metal m : DefaultMetalData.BASE_METALS) {
            String prefix = m.idPrefix();
            File file = new File(root, prefix + "_ore.json");
            if (file.exists()) continue;

            try (FileWriter writer = new FileWriter(file)) {
                // OreDataLoader가 읽는 형태와 동일한 JSON
                // - oreItemId: 블록 아이템(설치 가능한 ore block item)
                // - resultIngotId: FurnaceLogic 결과로 사용하는 IngotData key
                String oreItemId = "mineblacksmith:" + prefix + "_ore";
                String resultIngotId = "mineblacksmith:" + prefix + "_ingot";

                String json = "{\n" +
                        "  \"oreItemId\": \"" + oreItemId + "\",\n" +
                        "  \"resultIngotId\": \"" + resultIngotId + "\",\n" +
                        "  \"impurities\": []\n" +
                        "}\n";
                writer.write(json);
                System.out.println("[ConfigGenerator] 기본 광석 매핑 생성: " + file.getName());
            } catch (Exception e) {
                System.err.println("[ConfigGenerator] 기본 광석 매핑 생성 실패: " + file.getName());
                e.printStackTrace();
            }
        }
    }

    public static void createDefaultFuelConfigs(File fuelsDir) {
        if (!fuelsDir.exists()) fuelsDir.mkdirs();

        writeFuelIfAbsent(fuelsDir, "wood.json", ModFuels.WOOD);
        writeFuelIfAbsent(fuelsDir, "charcoal.json", ModFuels.CHARCOAL);
        writeFuelIfAbsent(fuelsDir, "coal.json", ModFuels.COAL);
        writeFuelIfAbsent(fuelsDir, "coke.json", ModFuels.COKE);
    }

    private static void writeFuelIfAbsent(File dir, String fileName, FuelData data) {
        File file = new File(dir, fileName);
        if (file.exists()) return;

        try (FileWriter writer = new FileWriter(file)) {
            GSON.toJson(data, writer);
            System.out.println("[ConfigGenerator] 기본 연료 생성: " + file.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * "합금 시뮬레이션 주괴온도 수식 만들기.xlsx" 의 무게 수식과 동일한 형태.
     * (기초 금속/합금 공통으로 쓰기 위해 여기에 둔다)
     */
    private static double calculateWeight(double hardness, double durability, double elasticity,
                                          double polishing, double conductivity) {

        // M: (내구+강도+탄성) - 0.5*(연마+전도)
        double m = (durability + hardness + elasticity) - 0.5 * (polishing + conductivity);

        // N: IF((내구+강도+탄성)<=10,2, IF>=20,-4, IF>=15,-2, 0)
        double sumA = durability + hardness + elasticity;
        double n;
        if (sumA <= 10) n = 2;
        else if (sumA >= 20) n = -4;
        else if (sumA >= 15) n = -2;
        else n = 0;

        // O: IF((연마+전도)<=10,2, IF>=15,-2, 0)
        double sumB = polishing + conductivity;
        double o;
        if (sumB <= 10) o = 2;
        else if (sumB >= 15) o = -2;
        else o = 0;

        // P: M + N + O
        double p = m + n + o;

        // Q: IF(AND(P>=1,P<=6),4, IF(7-10,2, IF(13-15,-2, IF(P>=16,-4,0))))
        double q;
        if (p >= 1 && p <= 6) q = 4;
        else if (p >= 7 && p <= 10) q = 2;
        else if (p >= 13 && p <= 15) q = -2;
        else if (p >= 16) q = -4;
        else q = 0;

        double baseWeight = Math.max(1, p + q);
        return Math.ceil(baseWeight) * 0.1;
    }

    private ConfigGenerator() {}
}
