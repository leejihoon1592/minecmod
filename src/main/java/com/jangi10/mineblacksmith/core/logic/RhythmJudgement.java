package com.jangi10.mineblacksmith.core.logic;

public enum RhythmJudgement {
    FANTASTIC(15, 0.05f, 0xFFFFFFFF), // ±5% 오차 (흰색)
    PERFECT(10, 0.15f, 0xFFFFFF00),   // ±15% 오차 (노랑)
    GOOD(6, 0.30f, 0xFFFFAA00),       // ±30% 오차 (주황)
    BAD(2, 1.00f, 0xFF000000),        // 범위 밖 (검정/실패)
    MISS(0, 0.00f, 0xFFFF0000);       // 클릭 안함 (미스)

    private final int score;          // 획득 점수
    private final float rangeThreshold; // 판정 범위 (중앙 0.5 기준 거리)
    private final int colorCode;      // UI 표시 색상

    RhythmJudgement(int score, float rangeThreshold, int colorCode) {
        this.score = score;
        this.rangeThreshold = rangeThreshold;
        this.colorCode = colorCode;
    }

    public int getScore() { return score; }
    public float getRangeThreshold() { return rangeThreshold; }
    public int getColorCode() { return colorCode; }
}