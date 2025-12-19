package com.jangi10.mineblacksmith.core.data;

/**
 * [IngotMoldState]
 * - 기획서: 주괴 틀은 세척/교체 가능, 불순물 제거 실패 시 품질 저하 :contentReference[oaicite:6]{index=6}
 * - 코어에서는 '더러움(Dirty)'만 상태로 관리한다.
 */
public class IngotMoldState {

    // 0 = 깨끗, 값이 클수록 더러움
    private int dirtLevel;

    public IngotMoldState() {
        this(0);
    }

    public IngotMoldState(int dirtLevel) {
        this.dirtLevel = Math.max(0, dirtLevel);
    }

    public int getDirtLevel() {
        return dirtLevel;
    }

    public boolean isDirty() {
        return dirtLevel > 0;
    }

    /** 주괴 추출 시 더러움 누적 */
    public void addDirt(int amount) {
        dirtLevel = Math.max(0, dirtLevel + amount);
    }

    /** 세척 */
    public void clean() {
        dirtLevel = 0;
    }
}
