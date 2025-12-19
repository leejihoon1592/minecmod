package com.jangi10.mineblacksmith.core.logic;

import com.jangi10.mineblacksmith.core.data.FurnaceSession;
import com.jangi10.mineblacksmith.core.data.FurnaceStatus;

/**
 * [FurnaceStatusLogic]
 * - FurnaceSession -> FurnaceStatus 변환
 * - UI/HUD/디버그에서 그대로 사용 가능
 */
public class FurnaceStatusLogic {

    /**
     * @param session 노 세션
     * @param tMax     해당 재료의 최대온도(T_max). 색상 계산에 필요
     */
    public static FurnaceStatus build(FurnaceSession session, double tMax) {
        if (session == null) return null;

        TemperatureColor color = TemperatureLogic.getColor(session.getTemperature(), tMax);

        TemperatureWindowResult window = null;
        if (session.getBaseHeatTemp() > 0 && session.getForgingDifficulty() > 0) {
            window = TemperatureLogic.getForgeWindow(
                    session.getTemperature(),
                    session.getBaseHeatTemp(),
                    session.getForgingDifficulty()
            );
        }

        return new FurnaceStatus(
                session.getTemperature(),
                color,
                session.getBaseHeatTemp(),
                session.getForgingDifficulty(),
                window,
                session.getForgeWindowFailCount()
        );
    }

    private FurnaceStatusLogic() {}
}
