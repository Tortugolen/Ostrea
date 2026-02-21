package com.tortugolen.ostrea.GUIs.Menus;

import com.tortugolen.ostrea.BlockEntities.SoulTableBlockEntity;
import com.tortugolen.ostrea.Init.InitBlocks;
import com.tortugolen.ostrea.Init.InitMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class SoulTableMenu extends AbstractContainerMenu {
    public final SoulTableBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    public SoulTableMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(10));
    }

    public SoulTableMenu(int pContainerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(InitMenus.SOUL_TABLE_MENU.get(), pContainerId);
        checkContainerSize(inv, 6);
        blockEntity = ((SoulTableBlockEntity) entity);
        this.level = inv.player.level();
        this.data = data;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
            this.addSlot(new SlotItemHandler(iItemHandler, 0, 44, 17));
            this.addSlot(new SlotItemHandler(iItemHandler, 1, 44, 53) {
                @Override
                public boolean mayPlace(ItemStack pStack) {
                    return false;
                }});
            this.addSlot(new SlotItemHandler(iItemHandler, 2, 80, 30));
            this.addSlot(new SlotItemHandler(iItemHandler, 3, 80, 53) {
                @Override
                public boolean mayPlace(ItemStack pStack) {
                    return false;
                }});
            this.addSlot(new SlotItemHandler(iItemHandler, 4, 116, 17));
            this.addSlot(new SlotItemHandler(iItemHandler, 5, 116, 53));
        });

        addDataSlots(data);
    }

    public int getScaledProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);

        if (maxProgress <= 0) return 0;
        return progress * 26 / maxProgress;
    }

    public int getScaledSouls() {
        int souls = this.data.get(2);
        int maxSouls = this.data.get(3);

        if (maxSouls <= 0) return 0;
        return souls * 64 / maxSouls;
    }

    public int getScaledSoulsProgress() {
        int soulsProgress = this.data.get(6);
        int maxSoulsProgress = this.data.get(7);

        if (maxSoulsProgress <= 0) return 0;
        return soulsProgress * 16 / maxSoulsProgress;
    }

    public int getScaledEffectSouls() {
        int effectSouls = this.data.get(4);
        int maxEffectSouls = this.data.get(5);

        if (maxEffectSouls <= 0) return 0;
        return effectSouls * 64 / maxEffectSouls;
    }

    public int getScaledEffectSoulsProgress() {
        int effectSoulsProgress = this.data.get(8);
        int maxEffectSoulsProgress = this.data.get(9);

        if (maxEffectSoulsProgress <= 0) return 0;
        return effectSoulsProgress * 22 / maxEffectSoulsProgress;
    }

    public int getEffectSoulsBarEffect() {
        MobEffect effect = blockEntity.getEffect();

        if (effect == MobEffects.REGENERATION) return 4;

        return 0;
    }

    public boolean isCrafting() {
        return data.get(0) > 0;
    }
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    private static final int TE_INVENTORY_SLOT_COUNT = 6;
    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            return ItemStack.EMPTY;
        }
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), pPlayer, InitBlocks.SOUL_TABLE.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}
