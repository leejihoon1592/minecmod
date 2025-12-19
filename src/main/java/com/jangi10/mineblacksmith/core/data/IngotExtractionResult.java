package com.jangi10.mineblacksmith.core.data;

/**
 * [IngotExtractionResult]
 * - 노 옆 주괴틀에서 "우클릭 추출" 시 반환할 결과
 * - 코어는 아이템을 직접 만들지 않고, 결과 데이터만 준다.
 *
 * 기획서: 불순물 1개당 스탯 -1~-3 / 무게 +1~+3 :contentReference[oaicite:8]{index=8}
 */
public class IngotExtractionResult {

    private final boolean success;
    private final String resultIngotId;

    private final int remainingImpurities;
    private final int statPenaltyTotal;     // 총 스탯 페널티(랜덤 합산)
    private final int weightBonusTotal;     // 총 무게 보너스(랜덤 합산)

    private final int moldDirtAfter;        // 추출 후 틀 더러움
    private final String message;

    public IngotExtractionResult(boolean success,
                                 String resultIngotId,
                                 int remainingImpurities,
                                 int statPenaltyTotal,
                                 int weightBonusTotal,
                                 int moldDirtAfter,
                                 String message) {
        this.success = success;
        this.resultIngotId = resultIngotId;
        this.remainingImpurities = remainingImpurities;
        this.statPenaltyTotal = statPenaltyTotal;
        this.weightBonusTotal = weightBonusTotal;
        this.moldDirtAfter = moldDirtAfter;
        this.message = message;
    }

    public boolean isSuccess() { return success; }
    public String getResultIngotId() { return resultIngotId; }
    public int getRemainingImpurities() { return remainingImpurities; }
    public int getStatPenaltyTotal() { return statPenaltyTotal; }
    public int getWeightBonusTotal() { return weightBonusTotal; }
    public int getMoldDirtAfter() { return moldDirtAfter; }
    public String getMessage() { return message; }
}
