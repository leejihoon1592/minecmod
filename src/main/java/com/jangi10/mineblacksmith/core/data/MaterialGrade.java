package com.jangi10.mineblacksmith.core.data;

/**
 * 기획서 등급 체계 확장판
 * (난이도 구간별 상한은 GradeDeterminer에서 제어)
 */
public enum MaterialGrade {
    FLAWED,     // 흠있는
    COMMON,     // 일반
    UNCOMMON,   // 고급
    RARE,       // 희귀
    ANCIENT,    // 고대
    HEROIC,     // 영웅
    UNIQUE,     // 유일
    RELIC,      // 유물
    WONDROUS,   // 경이
    EPIC,       // 서사
    LEGENDARY,  // 전설
    MYTHIC      // (보류/확장용) 신화 - 최종/특수 등급
}
