package com.jangi10.mineblacksmith.core.data;

import com.jangi10.mineblacksmith.core.logic.TemperatureColor;
import com.jangi10.mineblacksmith.core.logic.TemperatureWindowResult;

/**
 * [FurnaceStatus]
 * - 노 상태를 UI/로그/디버그에서 쓰기 쉽게 묶어서 반환
 */
public class FurnaceStatus {

    private final double temperature;
    private final TemperatureColor color;

    private final double baseHeatTemp;
    private final int forgingDifficulty;
    private final TemperatureWindowResult forgeWindow;

    private final int failCount;

    public FurnaceStatus(double temperature,
                         TemperatureColor color,
                         double baseHeatTemp,
                         int forgingDifficulty,
                         TemperatureWindowResult forgeWindow,
                         int failCount) {
        this.temperature = temperature;
        this.color = color;
        this.baseHeatTemp = baseHeatTemp;
        this.forgingDifficulty = forgingDifficulty;
        this.forgeWindow = forgeWindow;
        this.failCount = failCount;
    }

    public double getTemperature() { return temperature; }
    public TemperatureColor getColor() { return color; }

    public double getBaseHeatTemp() { return baseHeatTemp; }
    public int getForgingDifficulty() { return forgingDifficulty; }
    public TemperatureWindowResult getForgeWindow() { return forgeWindow; }

    public int getFailCount() { return failCount; }
}
