package com.jangi10.mineblacksmith.core.logic;

import com.jangi10.mineblacksmith.core.data.FuelData;
import com.jangi10.mineblacksmith.core.data.HeatPhysicsConfig;

public class HeatPhysics {

    // 기본값으로 초기화 (Config 로드 전까지 사용)
    private static HeatPhysicsConfig config = new HeatPhysicsConfig();

    /**
     * ConfigLoader에서 로드된 설정을 주입합니다.
     */
    public static void setConfig(HeatPhysicsConfig newConfig) {
        config = newConfig;
    }

    public static double calculateNextTemperature(double currentTemp, FuelData fuel, boolean isBellowsActive) {
        // 연료가 없으면 설정된 냉각 속도로 식음
        if (fuel == null) {
            return Math.max(0, currentTemp - config.coolingSpeed);
        }

        double targetTemp;
        double riseRate = config.heatRiseSpeed;

        if (isBellowsActive) {
            targetTemp = fuel.getMaxTempLimit();
            riseRate += config.bellowsBoost; // 설정된 부스트 값 적용
        } else {
            targetTemp = fuel.getBaseTargetTemp();
        }

        if (currentTemp < targetTemp) {
            return Math.min(targetTemp, currentTemp + riseRate);
        } else if (currentTemp > targetTemp) {
            return Math.max(targetTemp, currentTemp - config.coolingSpeed);
        }

        return currentTemp;
    }
}