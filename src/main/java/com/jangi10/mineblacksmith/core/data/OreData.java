package com.jangi10.mineblacksmith.core.data;

import java.util.ArrayList;
import java.util.List;

/**
 * 채광된 광석(Ore)의 데이터입니다.
 * 여러 개의 불순물(Impurity)과 최종적으로 변환될 주괴(Ingot)의 ID를 가집니다.
 */
public class OreData {
    private final String oreId;          // 광석 아이템 ID (예: "raw_iron")
    private final String resultIngotId;  // 정제 완료 시 나올 주괴 ID (MaterialRegistry에 등록된 ID)
    private final List<ImpurityData> impurities; // 포함된 불순물 목록 (최대 3개)

    public OreData(String oreId, String resultIngotId) {
        this.oreId = oreId;
        this.resultIngotId = resultIngotId;
        this.impurities = new ArrayList<>();
    }

    /**
     * 불순물을 추가합니다. (빌더 패턴처럼 사용 가능)
     */
    public OreData addImpurity(String name, double minTemp, double maxTemp) {
        if (impurities.size() >= 3) {
            // 기획서: 최대 3개의 불순물 조각
            throw new IllegalStateException("Ore cannot have more than 3 impurities.");
        }
        this.impurities.add(new ImpurityData(name, minTemp, maxTemp));
        return this;
    }

    public String getOreId() { return oreId; }
    public String getResultIngotId() { return resultIngotId; }
    public List<ImpurityData> getImpurities() { return impurities; }

    /**
     * 불순물 개수를 반환합니다. (정제 난이도 산정용)
     */
    public int getImpurityCount() {
        return impurities.size();
    }
}