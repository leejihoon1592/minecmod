package com.jangi10.mineblacksmith.core.logic;

import com.jangi10.mineblacksmith.core.data.MaterialGrade;

/**
 * 기획서 등급 컷 확정 반영판
 *
 * finalScore: 0.0 ~ 1.0 (달성비율)
 * difficulty: 제작난이도
 *
 * 난이도 11+: 전설까지
 * 난이도 8~10: 경이까지
 * 난이도 7 이하: 영웅까지
 */
public class GradeDeterminer {

    public static MaterialGrade determineGrade(int difficulty, double finalScore) {
        double p = clamp01(finalScore);

        if (difficulty >= 11) {
            // 11 이상 (전설까지 가능)
            if (p <= 0.14) return MaterialGrade.FLAWED;        // 흠있는 0~14
            if (p <= 0.24) return MaterialGrade.COMMON;        // 일반 15~24
            if (p <= 0.34) return MaterialGrade.UNCOMMON;      // 고급 25~34
            if (p <= 0.49) return MaterialGrade.RARE;          // 희귀 35~49
            if (p <= 0.69) return MaterialGrade.ANCIENT;       // 고대 50~69
            if (p <= 0.79) return MaterialGrade.HEROIC;        // 영웅 70~79
            // 아래는 enum에 없으면 '상위 등급 매핑'으로 처리
            if (p <= 0.85) return bestEffort("UNIQUE", MaterialGrade.MYTHIC);    // 유일 80~85
            if (p <= 0.93) return bestEffort("RELIC", MaterialGrade.MYTHIC);     // 유물 86~93
            if (p <= 0.97) return bestEffort("WONDROUS", MaterialGrade.MYTHIC);  // 경이 94~97
            if (p <= 0.99) return bestEffort("EPIC", MaterialGrade.MYTHIC);      // 서사 98~99
            return bestEffort("LEGENDARY", MaterialGrade.MYTHIC);                // 전설 100
        }

        if (difficulty >= 8) {
            // 8~10 (경이까지 가능)
            if (p <= 0.29) return MaterialGrade.FLAWED;        // 흠있는 0~29
            if (p <= 0.39) return MaterialGrade.COMMON;        // 일반 30~39
            if (p <= 0.49) return MaterialGrade.UNCOMMON;      // 고급 40~49
            if (p <= 0.68) return MaterialGrade.RARE;          // 희귀 50~68
            if (p <= 0.79) return MaterialGrade.ANCIENT;       // 고대 69~79
            if (p <= 0.89) return MaterialGrade.HEROIC;        // 영웅 80~89
            if (p <= 0.95) return bestEffort("UNIQUE", MaterialGrade.MYTHIC);   // 유일 90~95
            if (p <= 0.99) return bestEffort("RELIC", MaterialGrade.MYTHIC);    // 유물 96~99
            return bestEffort("WONDROUS", MaterialGrade.MYTHIC);                // 경이 100
        }

        // 7 이하 (영웅까지 가능)
        if (p <= 0.22) return MaterialGrade.FLAWED;            // 흠있는 0~22
        if (p <= 0.52) return MaterialGrade.COMMON;            // 일반 23~52
        if (p <= 0.77) return MaterialGrade.UNCOMMON;          // 고급 53~77
        if (p <= 0.97) return MaterialGrade.RARE;              // 희귀 78~97
        if (p <= 0.99) return MaterialGrade.ANCIENT;           // 고대 98~99
        return MaterialGrade.HEROIC;                            // 영웅 100
    }

    private static double clamp01(double v) {
        if (v < 0) return 0;
        if (v > 1) return 1;
        return v;
    }

    /**
     * MaterialGrade enum이 아직 "유일/유물/경이/서사/전설"을 포함하지 않을 수 있어서
     * 우선은 존재하면 그 enum을 사용하고, 없으면 fallback으로 MYTHIC(신화)로 매핑.
     * 다음 단계에서 MaterialGrade 확장할 때 여기만 그대로 살아남으면 자동으로 진짜 등급이 적용됨.
     */
    private static MaterialGrade bestEffort(String enumName, MaterialGrade fallback) {
        try {
            return MaterialGrade.valueOf(enumName);
        } catch (Exception ignored) {
            return fallback;
        }
    }

    private GradeDeterminer() {}
}
