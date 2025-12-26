package com.jangi10.mineblacksmith.core.init;

import java.util.List;

/**
 * 기획서/엑셀(금속목록) 기반 "기초 금속" 기본값.
 *
 * - 여기 값은 "기본 생성"(config 자동 생성)용이고,
 *   실제 밸런싱은 config/mineblacksmith/ingots/*.json 을 유저가 수정해서 진행한다.
 *
 * - id는 ModMetalIds의 접두(prefix)와 동일하게 맞춘다.
 */
public final class DefaultMetalData {

    /**
     * @param idPrefix     예: "accutyrone" (=> mineblacksmith:accutyrone_ingot)
     * @param koName       예: "아큐타이론"
     * @param durability   내구성
     * @param polishing    연마
     * @param strength     강도
     * @param elasticity   탄성
     * @param conductivity 전도성
     * @param meltingPoint 녹는점(°C)
     * @param difficulty   제작 난이도(정수 티어)
     * @param baseColor    기본 색(ARGB가 아니라 RGB 0xRRGGBB)
     */
    public record Metal(
            String idPrefix,
            String koName,
            double durability,
            double polishing,
            double strength,
            double elasticity,
            double conductivity,
            double meltingPoint,
            int difficulty,
            int baseColor
    ) {}

    /**
     * 금속목록 시트(확정 수식본)에서 뽑아온 값.
     * - 녹는점: ROUNDUP(1000 + 내구*38 + 강도*38 + 탄성*25 + 연마*15 - 전도*20, -1)
     * - 난이도: (내구+연마+강도+탄성+전도+무게)/5 를 CEILING, 이후 게임에서는 정수 티어로 올림 처리
     */
    public static final List<Metal> BASE_METALS = List.of(
            new Metal("duracilium", "듀라시움", 8.0d, 7.0d, 5.0d, 3.5d, 1.5d, 1660d, 6, 0xE6E6E6),
            new Metal("enduri", "엔듀리온", 9.0d, 1.0d, 7.0d, 4.5d, 3.5d, 1670d, 6, 0xE6E6E6),
            new Metal("tenaloct", "테나록", 8.0d, 3.5d, 1.5d, 7.0d, 5.0d, 1490d, 6, 0xE6E6E6),
            new Metal("stevalite", "스테발라이트", 9.0d, 4.5d, 3.0d, 1.5d, 7.0d, 1430d, 6, 0xE6E6E6),
            new Metal("accutyrone", "아큐타이론", 6.5d, 9.0d, 4.5d, 3.5d, 1.5d, 1620d, 6, 0xE6E6E6),
            new Metal("charvin", "샤르빈", 1.5d, 9.0d, 6.5d, 4.5d, 3.5d, 1490d, 6, 0xE6E6E6),
            new Metal("clinsor", "클린서", 3.0d, 9.0d, 1.5d, 7.0d, 4.5d, 1400d, 6, 0xE6E6E6),
            new Metal("edgastium", "에드가스티움", 5.0d, 8.0d, 3.5d, 1.5d, 7.0d, 1350d, 6, 0xE6E6E6),
            new Metal("criminaltalloi", "포르탈로이", 7.0d, 4.5d, 8.5d, 3.5d, 1.5d, 1720d, 6, 0xE6E6E6),
            new Metal("brackstal", "브랙스탈", 1.0d, 6.5d, 9.0d, 5.0d, 3.5d, 1540d, 6, 0xE6E6E6),
            new Metal("durathorn", "듀라쏜", 3.5d, 1.5d, 8.5d, 6.5d, 5.0d, 1550d, 6, 0xE6E6E6),
            new Metal("granteron", "그란테론", 4.0d, 3.5d, 9.0d, 1.5d, 7.0d, 1450d, 6, 0xE6E6E6),
            new Metal("flexarite", "플렉사라이트", 6.0d, 5.0d, 3.5d, 9.0d, 1.5d, 1640d, 6, 0xE6E6E6),
            new Metal("sprinal", "스프리날", 1.0d, 7.0d, 5.0d, 8.5d, 3.5d, 1480d, 6, 0xE6E6E6),
            new Metal("liselborn", "리셀본", 3.5d, 1.0d, 7.0d, 8.5d, 5.0d, 1530d, 6, 0xE6E6E6),
            new Metal("biklis", "비렐리스", 4.5d, 3.5d, 1.5d, 8.5d, 7.0d, 1360d, 6, 0xE6E6E6),
            new Metal("conductite", "콘덕타이트", 7.0d, 5.0d, 2.5d, 1.5d, 9.0d, 1300d, 6, 0xE6E6E6),
            new Metal("voltarite", "볼타라이트", 1.5d, 7.0d, 4.5d, 3.0d, 9.0d, 1230d, 6, 0xE6E6E6),
            new Metal("lumesor", "루메서", 3.5d, 1.5d, 6.5d, 4.5d, 9.0d, 1340d, 6, 0xE6E6E6),
            new Metal("ginspa", "진스파", 5.0d, 3.5d, 1.0d, 7.0d, 8.5d, 1290d, 6, 0xE6E6E6)
    );

    private DefaultMetalData() {}
}
