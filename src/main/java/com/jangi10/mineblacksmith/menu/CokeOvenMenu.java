package com.jangi10.mineblacksmith.menu;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.FurnaceMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * CokeOvenMenu
 * - 바닐라 FurnaceMenu UI/동작 재사용
 * - 단, 입력 슬롯(0번)만 "석탄/석탄블록"으로 제한
 */
public class CokeOvenMenu extends FurnaceMenu {

    public CokeOvenMenu(int id, Inventory playerInv, Container container, ContainerData data) {
        // ✅ 여기서 FurnaceMenu 기본 슬롯 세팅(3슬롯 + 플레이어 인벤) 전부 만들어짐
        super(id, playerInv, container, data);

        // ✅ 핵심: 0번 슬롯을 "필터 슬롯"으로 교체
        // super가 만든 슬롯 리스트는 this.slots 에 들어있고, 0번이 입력 슬롯임
        this.slots.set(0, new Slot(container, 0, 56, 17) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(Items.COAL) || stack.is(Items.COAL_BLOCK);
            }
        });

        // (선택) 연료 슬롯도 제한하고 싶으면 아래처럼 1번도 교체 가능
        // this.slots.set(1, new Slot(container, 1, 56, 53) {
        //     @Override public boolean mayPlace(ItemStack stack) {
        //         // 연료: coal/charcoal/coke/coke_block만 허용하고 싶을 때
        //         return stack.is(Items.COAL) || stack.is(Items.CHARCOAL);
        //     }
        // });
    }
}
