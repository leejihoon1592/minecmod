package com.jangi10.mineblacksmith.core.logic;

public class RefiningLogic {

    // 기획서 기준: 10초 유지 → 20 ticks * 10 = 200
    public static final int REQUIRED_TICKS = 200;

    /**
     * 정제 진행도 갱신 (매 tick 호출)
     *
     * @param currentTemp 현재 노 온도
     * @param minTemp     불순물 제거 최소 온도
     * @param maxTemp     불순물 제거 최대 온도
     * @param currentProgress 현재 진행도
     */
    public static RefiningResult processTick(
            double currentTemp,
            int minTemp,
            int maxTemp,
            int currentProgress
    ) {
        // 온도 범위 판정
        boolean inRange = currentTemp >= minTemp && currentTemp <= maxTemp;

        int newProgress = currentProgress;

        if (inRange) {
            newProgress++;
        } else {
            if (newProgress > 0) newProgress--;
        }

        boolean finished = newProgress >= REQUIRED_TICKS;

        return new RefiningResult(newProgress, RefiningStatus.IN_RANGE, finished);
    }
}
