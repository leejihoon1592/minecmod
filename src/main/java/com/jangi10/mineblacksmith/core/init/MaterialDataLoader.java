package com.jangi10.mineblacksmith.core.init;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jangi10.mineblacksmith.core.data.IngotData;
import com.jangi10.mineblacksmith.core.registry.MaterialRegistry;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;

public class MaterialDataLoader {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String DIR_PATH = "config/mineblacksmith/ingots";

    public static void loadIngots() {
        File directory = new File(DIR_PATH);
        if (!directory.exists()) directory.mkdirs();

        // 기본 파일 생성
        ConfigGenerator.createDefaultIngotConfigs(DIR_PATH);

        File[] files = directory.listFiles((dir, name) -> name.endsWith(".json"));
        if (files == null || files.length == 0) {
            System.out.println("[MineBlacksmith] 로드할 ingots JSON 파일이 없습니다.");
            return;
        }

        int count = 0;
        for (File file : files) {
            try (Reader reader = new FileReader(file)) {
                IngotData data = GSON.fromJson(reader, IngotData.class);
                if (data == null || data.getId() == null) continue;

                MaterialRegistry.register(data);
                count++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("[MineBlacksmith] 주괴 데이터 로드 완료: " + count + "개");
    }

    private MaterialDataLoader() {}
}
