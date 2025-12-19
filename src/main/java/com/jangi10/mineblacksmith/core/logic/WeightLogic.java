package com.jangi10.mineblacksmith.core.logic;

public class WeightLogic {

    public static final double BASE_WEIGHT = 35.0; // kg
    public static final double HEAVY_PENALTY_PER_KG = -0.01; // +1kg당 -1%
    public static final double LIGHT_BONUS_PER_KG   =  0.02; // -1kg당 +2%

    /** (1 + 보정율) 형태의 배율 반환 */
    public static double getSpeedMultiplier(double currentWeightKg) {
        double diff = currentWeightKg - BASE_WEIGHT;

        double rate;
        if (diff > 0) {
            rate = diff * HEAVY_PENALTY_PER_KG;
        } else if (diff < 0) {
            rate = Math.abs(diff) * LIGHT_BONUS_PER_KG;
        } else {
            rate = 0;
        }
        return 1.0 + rate;
    }

    private WeightLogic() {}
}
