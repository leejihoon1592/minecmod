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

        File[] files = root.listFiles(f -> f.getName().endsWith(".json"));
        if (files == null) return;

        for (File file : files) {
            try (Reader reader = new FileReader(file)) {
                OreDataJson json = GSON.fromJson(reader, OreDataJson.class);
                OreData data = new OreData(json.oreItemId, json.resultIngotId);

                if (json.impurities != null) {
                    json.impurities.forEach(i ->
                            data.addImpurity(i.id, i.minTemp, i.maxTemp)
                    );
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
}
