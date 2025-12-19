package com.jangi10.mineblacksmith.core.logic;

/**
 * [제작 난이도별 단조 가능 온도 캡/하강속도]
 * - 기준 가열 온도(T_base)에 "비율"을 곱해서 허용 구간을 만든다.
 * - 기획서 표 그대로 반영 :contentReference[oaicite:3]{index=3}
 *
 * 1~4  : 70%~90%, 하강속도 10°C/s
 * 5~8  : 73%~87%, 하강속도 15°C/s
 * 9~11 : 75%~85%, 하강속도 20°C/s
 * 12+  : 76%~84%, 하강속도 25°C/s
 */
public class DifficultyTemperatureProfile {

    private final double minRatio;
    private final double maxRatio;
    private final double coolingPerSecond;

    public DifficultyTemperatureProfile(double minRatio, double maxRatio, double coolingPerSecond) {
        this.minRatio = minRatio;
        this.maxRatio = maxRatio;
        this.coolingPerSecond = coolingPerSecond;
    }

    public double minRatio() { return minRatio; }
    public double maxRatio() { return maxRatio; }
    public double coolingPerSecond() { return coolingPerSecond; }

    public static DifficultyTemperatureProfile of(int difficulty) {
        if (difficulty <= 4)  return new DifficultyTemperatureProfile(0.70, 0.90, 10.0);
        if (difficulty <= 8)  return new DifficultyTemperatureProfile(0.73, 0.87, 15.0);
        if (difficulty <= 11) return new DifficultyTemperatureProfile(0.75, 0.85, 20.0);
        return new DifficultyTemperatureProfile(0.76, 0.84, 25.0);
    }
}
