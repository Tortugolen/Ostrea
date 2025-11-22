package com.tortugolen.ostrea.GUIs.Menus;

import com.tortugolen.ostrea.BlockEntities.MechanicalOysterBlockEntity;
import com.tortugolen.ostrea.Init.InitBlocks;
import com.tortugolen.ostrea.Init.InitMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class MechanicalOysterMenu extends AbstractContainerMenu {
    public final MechanicalOysterBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;
    private final int SLOT_DISPLACEMENT = 9;


    public MechanicalOysterMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(6));
    }

    public MechanicalOysterMenu(int pContainerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(InitMenus.MECHANICAL_OYSTER_MENU.get(), pContainerId);
        checkContainerSize(inv, 6);
        blockEntity = ((MechanicalOysterBlockEntity) entity);
        this.level = inv.player.level();
        this.data = data;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
            this.addSlot(new SlotItemHandler(iItemHandler, 0, 26, 47 - SLOT_DISPLACEMENT));
            this.addSlot(new SlotItemHandler(iItemHandler, 1, 134, 47 - SLOT_DISPLACEMENT) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return false;
                }});
            this.addSlot(new SlotItemHandler(iItemHandler, 2, 80, 59 - SLOT_DISPLACEMENT));
            this.addSlot(new SlotItemHandler(iItemHandler, 3, 134, 73 - SLOT_DISPLACEMENT) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return false;
                }});
            this.addSlot(new SlotItemHandler(iItemHandler, 4, 71, 18 - SLOT_DISPLACEMENT));
            this.addSlot(new SlotItemHandler(iItemHandler, 5, 89, 18 - SLOT_DISPLACEMENT));
        });

        addDataSlots(data);
    }

    public int getScaledProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);

        if (maxProgress <= 0) return 0;
        return progress * 59 / maxProgress;
    }

    public int getScaledAmountCalciumCarbonate() {
        int progress = this.data.get(2);
        int maxProgress = this.data.get(3);

        if (maxProgress <= 0) return 0;
        return progress * 52 / maxProgress;
    }

    public int getScaledCyclesCalciumCarbonate() {
        int progress = this.data.get(4);
        int maxProgress = this.data.get(5);

        if (maxProgress <= 0) return 0;
        return progress * 34 / maxProgress;
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
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), pPlayer, InitBlocks.MECHANICAL_OYSTER.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18 + 8));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142 + 8));
        }
    }
}
