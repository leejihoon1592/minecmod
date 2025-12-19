package com.jangi10.mineblacksmith.core.data;

/**
 * 온도 시스템의 물리 법칙 계수들을 정의하는 설정 객체입니다.
 * physics.json 파일에서 로드됩니다.
 */
public class HeatPhysicsConfig {
    // 틱당 온도 상승 속도 (기본 5.0)
    public double heatRiseSpeed = 5.0;

    // 틱당 자연 냉각 속도 (기본 2.0)
    public double coolingSpeed = 2.0;

    // 풀무질 시 추가 상승 온도 (기본 15.0)
    public double bellowsBoost = 15.0;

    // 풀무질 1회당 온도 유지 시간(틱) (추가 제안)
    public int bellowsEffectDuration = 60;

    // 기본 생성자 (JSON Parsing용)
    public HeatPhysicsConfig() {}
}