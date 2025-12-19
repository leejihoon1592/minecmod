package com.jangi10.mineblacksmith.core.logic;

public enum RefiningStatus {
    TOO_COLD,   // 온도가 너무 낮음
    IN_RANGE,   // 적정 온도 범위 (정제 진행 중 - 붉은 발광 효과)
    TOO_HOT,    // 온도가 너무 높음 (과열)
    COMPLETE    // 정제 완료
}