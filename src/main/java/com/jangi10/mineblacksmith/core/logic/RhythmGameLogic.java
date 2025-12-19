package com.jangi10.mineblacksmith.core.logic;

public class RhythmGameLogic {

    private static final long BASE_CYCLE_MS = 1500; // 기본 왕복 시간 1.5초 (1500ms)

    /**
     * 현재 시간에 따른 커서 위치(0.0 ~ 1.0)를 계산합니다.
     * @param gameTimeMs 게임 시작 후 경과 시간 (밀리초)
     * @param difficulty 제작 난이도 (속도 조절용)
     * @return 0.0(좌) ~ 0.5(중앙) ~ 1.0(우) 값
     */
    public static float getCursorPosition(long gameTimeMs, int difficulty) {
        // 난이도별 주기 계산 (난이도가 높을수록 시간이 짧아짐)
        // 예: 난이도 1당 0.1초씩 빨라짐 (최소 0.5초 제한)
        long cycleDuration = Math.max(500, BASE_CYCLE_MS - (difficulty * 100));

        // 왕복 이동 (Ping-Pong 알고리즘)
        // 0 -> 1 -> 0 반복
        float progress = (gameTimeMs % cycleDuration) / (float) cycleDuration;

        // 0.0 ~ 0.5 구간은 0->1 이동, 0.5 ~ 1.0 구간은 1->0 이동으로 변환
        if (progress <= 0.5f) {
            return progress * 2.0f; // 0 -> 1
        } else {
            return 2.0f - (progress * 2.0f); // 1 -> 0
        }
    }

    /**
     * 사용자가 클릭했을 때의 커서 위치를 기반으로 판정을 내립니다.
     * @param cursorPos 현재 커서 위치 (0.0 ~ 1.0)
     * @return 판정 결과 (Fantastic ~ Bad)
     */
    public static RhythmJudgement judgeHit(float cursorPos) {
        // 목표 지점은 정중앙 (0.5)
        float distance = Math.abs(cursorPos - 0.5f);

        if (distance <= RhythmJudgement.FANTASTIC.getRangeThreshold()) {
            return RhythmJudgement.FANTASTIC;
        } else if (distance <= RhythmJudgement.PERFECT.getRangeThreshold()) {
            return RhythmJudgement.PERFECT;
        } else if (distance <= RhythmJudgement.GOOD.getRangeThreshold()) {
            return RhythmJudgement.GOOD;
        } else {
            return RhythmJudgement.BAD;
        }
    }
}