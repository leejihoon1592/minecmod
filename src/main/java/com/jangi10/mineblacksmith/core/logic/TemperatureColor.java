package com.jangi10.mineblacksmith.core.logic;

/**
 * [온도 색상 상태]
 * 기획서: 4단계 색상 상태 :contentReference[oaicite:2]{index=2}
 */
public enum TemperatureColor {
    YELLOW, // 40% ~ 60% (가공 불가)
    ORANGE, // 60% ~ 70% (가공 불가)
    RED,    // 70% ~ 90% (정상 가공 가능)
    WHITE,  // 90% ~ 100% (과열, 가공 불가)
    COLD    // 0% ~ 40% (아직 차가움/미가열)
}
