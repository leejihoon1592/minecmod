package com.jangi10.mineblacksmith;

import java.util.List;

/**
 * 금속 ID 목록(텍스처/데이터 기준)
 * - block ore:   textures/block/ore/stone/<id>_ore.png
 * - raw block:   textures/block/raw_block/raw_<id>_block.png
 * - item raw:    textures/item/raw/raw_<id>.png
 * - item ingot:  textures/item/ingot/<id>_ingot.png
 * - item nugget: textures/item/nugget/<id>_nugget.png
 */
public final class ModMetalIds {
    public static final List<String> METAL_IDS = List.of(
            "accutyrone", "biklis", "brackstal", "charvin", "clinsor", "conductite", "criminaltalloi", "duracilium", "durathorn", "edgastium", "enduri", "flexarite", "ginspa", "granteron", "liselborn", "lumesor", "sprinal", "stevalite", "tenaloct", "voltarite"
    );

    private ModMetalIds() {}
}
