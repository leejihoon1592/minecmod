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

    private static double calculateWeight(double hardness, double durability, double elasticity,
                                          double polishing, double conductivity) {

        double val1 = (durability + hardness + elasticity) - 0.5 * (polishing + conductivity);

        double sumStats = durability + hardness + elasticity + polishing + conductivity;
        double val2 = 0;
        double val3 = 0;

        double val4 = val1 + val2 + val3;

        double val5 = 0;
        if (val4 >= 1 && val4 <= 6) {
            val5 = 4;
        } else if (val4 >= 7 && val4 <= 10) {
            val5 = 2;
        } else if (val4 >= 13 && val4 <= 15) {
            val5 = -2;
        }

        double baseWeight = Math.max(1, val4 + val5);
        return Math.ceil(baseWeight) * 0.1;
    }

    private ConfigGenerator() {}
}
