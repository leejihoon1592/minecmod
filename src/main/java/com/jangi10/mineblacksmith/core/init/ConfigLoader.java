package com.jangi10.mineblacksmith.core.init;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jangi10.mineblacksmith.core.data.FuelData;
import com.jangi10.mineblacksmith.core.data.HeatPhysicsConfig;
import com.jangi10.mineblacksmith.core.logic.HeatPhysics;
import com.jangi10.mineblacksmith.core.registry.FuelRegistry;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;

public class ConfigLoader {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void loadAll(File configDir) {
        File rootDir = new File(configDir, "mineblacksmith");
        if (!rootDir.exists()) rootDir.mkdirs();

        // 1) physics.json
        loadPhysicsConfig(new File(rootDir, "physics.json"));

        // 2) fuels 폴더 (+ 기본 연료 자동 생성)
        File fuelsDir = new File(rootDir, "fuels");
        ConfigGenerator.createDefaultFuelConfigs(fuelsDir);
        loadFuels(fuelsDir);
    }

    private static void loadPhysicsConfig(File file) {
        if (!file.exists()) {
            System.out.println("[MineBlacksmith] physics.json not found. Using defaults.");
            return;
        }
        try (Reader reader = new FileReader(file)) {
            HeatPhysicsConfig config = GSON.fromJson(reader, HeatPhysicsConfig.class);
            HeatPhysics.setConfig(config);
            System.out.println("[MineBlacksmith] Physics config loaded.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadFuels(File dir) {
        File[] files = dir.listFiles((d, name) -> name.endsWith(".json"));
        if (files == null) return;

        for (File file : files) {
            try (Reader reader = new FileReader(file)) {
                FuelData data = GSON.fromJson(reader, FuelData.class);
                if (data == null || data.getTargetItemId() == null) continue;
                FuelRegistry.register(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("[MineBlacksmith] Fuels loaded.");
    }

    private ConfigLoader() {}
}
