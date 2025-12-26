package com.jangi10.mineblacksmith.core.data;

import java.util.ArrayList;
import java.util.List;

public class OreData {

    private final String oreItemId;      // 입력 광석 (raw_xxx)
    private final String resultIngotId;   // 결과 주괴
    private final List<Impurity> impurities = new ArrayList<>();

    public OreData(String oreItemId, String resultIngotId) {
        this.oreItemId = oreItemId;
        this.resultIngotId = resultIngotId;
    }

    public String getOreItemId() {
        return oreItemId;
    }

    public String getResultIngotId() {
        return resultIngotId;
    }

    public List<Impurity> getImpurities() {
        return impurities;
    }

    public void addImpurity(String id, int minTemp, int maxTemp) {
        impurities.add(new Impurity(id, minTemp, maxTemp));
    }

    // ------------------------

    public static class Impurity {
        public final String id;
        public final int minTemp;
        public final int maxTemp;

        public Impurity(String id, int minTemp, int maxTemp) {
            this.id = id;
            this.minTemp = minTemp;
            this.maxTemp = maxTemp;
        }
    }
}
