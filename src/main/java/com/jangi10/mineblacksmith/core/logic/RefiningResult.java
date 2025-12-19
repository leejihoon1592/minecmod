package com.jangi10.mineblacksmith.core.logic;

public class RefiningResult {
    private final int progress;       // 갱신된 진행도 (0 ~ Max)
    private final RefiningStatus status; // 현재 상태
    private final boolean isFinished; // 완료 여부

    public RefiningResult(int progress, RefiningStatus status, boolean isFinished) {
        this.progress = progress;
        this.status = status;
        this.isFinished = isFinished;
    }

    public int getProgress() { return progress; }
    public RefiningStatus getStatus() { return status; }
    public boolean isFinished() { return isFinished; }
}