package com.jangi10.mineblacksmith.core.logic;

public enum WeaponPartType {
    // 무기 부품
    BLADE("칼날"),          // 검류 핵심
    GUARD("가드"),
    POMMEL("폼멜"),
    HEAD("머리"),           // 도끼/둔기 핵심
    HANDLE("자루"),
    BINDING("매듭/결속"),
    BOW_LIMB("활대"),       // 활 핵심
    BOW_STRING("활시위"),

    // 방어구 부품 (판금/사슬)
    ARMOR_PLATE("판금"),    // 판금갑옷 핵심
    ARMOR_CHAIN("사슬"),    // 사슬갑옷 핵심
    LINING("안감");         // 공통

    private final String name;

    WeaponPartType(String name) {
        this.name = name;
    }

    public String getName() { return name; }
}