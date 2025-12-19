package com.jangi10.mineblacksmith.core.logic;

/**
 * 기획서 기반 방어 공식 엔진
 *
 * - 실 감쇄율: 기본 80% + (강도총합*0.025 + 탄성총합*0.0167), 최대 95%
 * - 방어강도: floor(강도총합*0.025 + 탄성총합*0.0167), 최대 15
 * - 보호수치: (탄성*0.555 * 사용량) / 10 -> 소수점 버림, 총합 최대 20
 *
 * ※ "부위별 계산 후 합산"은 실제 아이템/슬롯 구조가 붙는 단계에서 적용.
 *   지금 코어에서는 총합 기반 계산 함수 제공.
 */
public class ArmorLogic {

    public static final double BASE_REDUCTION = 0.80; // 80%
    public static final double MAX_REDUCTION  = 0.95; // 95%

    public static final int MAX_DEFENSE_STABILITY = 15; // 방어강도 상한
    public static final int MAX_PROTECTION = 20;        // 보호수치 상한

    /** 실 감쇄율(0~1) */
    public static double calculateDamageReductionRatio(double totalStrength, double totalElasticity) {
        double bonus = (totalStrength * 0.025) + (totalElasticity * 0.0167);
        double ratio = BASE_REDUCTION + (bonus / 100.0); // 보너스는 % 단위 설계라 100으로 나눔
        if (ratio > MAX_REDUCTION) ratio = MAX_REDUCTION;
        if (ratio < 0) ratio = 0;
        return ratio;
    }

    /** 방어강도(정수) */
    public static int calculateDefenseStability(double totalStrength, double totalElasticity) {
        double v = (totalStrength * 0.025) + (totalElasticity * 0.0167);
        int out = (int) Math.floor(v);
        if (out > MAX_DEFENSE_STABILITY) out = MAX_DEFENSE_STABILITY;
        if (out < 0) out = 0;
        return out;
    }

    /**
     * 보호수치(정수)
     * @param elasticity 탄성(부위/총합 중 원하는 단위)
     * @param usageCount 사용량(예: 4부위면 4, 혹은 부위별이면 1)
     */
    public static int calculateProtection(double elasticity, int usageCount) {
        double adjustedElasticity = elasticity * 0.555;
        double raw = (adjustedElasticity * usageCount) / 10.0;
        int out = (int) Math.floor(raw);
        if (out > MAX_PROTECTION) out = MAX_PROTECTION;
        if (out < 0) out = 0;
        return out;
    }

    /**
     * 피해 적용 예시(코어 제공용)
     * - reductionRatio 적용 후
     * - protection(후처리 감쇄) 적용
     *
     * protection은 "추가 감쇄 수치" 개념이라, 여기서는 간단히
     * (1 - protection*0.01) 로 처리. (실제 구현 단계에서 인챈트 방식으로 바꿔도 됨)
     */
    public static double applyDamage(double incomingDamage,
                                     double totalStrength,
                                     double totalElasticity,
                                     int protection) {
        double ratio = calculateDamageReductionRatio(totalStrength, totalElasticity);
        double afterArmor = incomingDamage * (1.0 - ratio);

        double protMul = 1.0 - (Math.min(MAX_PROTECTION, Math.max(0, protection)) * 0.01);
        if (protMul < 0) protMul = 0;

        return afterArmor * protMul;
    }

    private ArmorLogic() {}
}
