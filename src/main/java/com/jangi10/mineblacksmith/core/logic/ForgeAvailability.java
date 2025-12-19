package com.jangi10.mineblacksmith.core.logic;

/**
 * [가공 가능 여부]
 * 기획서 컨셉:
 * - Yellow/Orange : 가공 불가
 * - Red          : 정상 가공 가능
 * - White        : 과열, 가공 불가
 */
public class ForgeAvailability {

    public static boolean canForgeByColor(TemperatureColor color) {
        return color == TemperatureColor.RED;
    }

    private ForgeAvailability() {}
}
