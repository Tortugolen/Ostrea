package com.tortugolen.ostrea.BlockEntities;

import com.tortugolen.ostrea.GUIs.Menus.CrusherMenu;
import com.tortugolen.ostrea.Init.InitBlockEntities;
import com.tortugolen.ostrea.Recipes.CrushRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CrusherBlockEntity extends BlockEntity implements MenuProvider {
    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;

    private final ContainerData data;
    private int progress = 0;
    private int maxProgress = 200;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    private final ItemStackHandler itemHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            setChanged();
        }
    };

    public CrusherBlockEntity(BlockPos pPos, BlockState pState) {
        super(InitBlockEntities.CRUSHER_BE.get(), pPos, pState);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> CrusherBlockEntity.this.progress;
                    case 1 -> CrusherBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> CrusherBlockEntity.this.progress = pValue;
                    case 1 -> CrusherBlockEntity.this.maxProgress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.ostrea.crusher");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new CrusherMenu(id, inventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("inventory", itemHandler.serializeNBT());
        tag.putInt("crusher.progress", progress);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("inventory")) {
            itemHandler.deserializeNBT(tag.getCompound("inventory"));
        }
        progress = tag.getInt("crusher.progress");
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (hasRecipe()) {
            increaseCraftingProgress();
            setChanged(level, pos, state);

            if (hasProgressFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }
        setChanged(level, pos, state);
    }

    private void resetProgress() {
        progress = 0;
    }

    private void craftItem() {
        Optional<CrushRecipes> recipe = getCurrentRecipe();
        if (recipe.isEmpty()) return;

        ItemStack result = recipe.get().getResultItem(level.registryAccess());
        this.itemHandler.extractItem(INPUT_SLOT, 1, false);
        if (this.itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty()) {
            itemHandler.setStackInSlot(OUTPUT_SLOT, result.copy());
        } else if (this.itemHandler.getStackInSlot(OUTPUT_SLOT).getItem() == result.getItem()) {
            this.itemHandler.getStackInSlot(OUTPUT_SLOT).grow(result.getCount());
            itemHandler.setStackInSlot(OUTPUT_SLOT, this.itemHandler.getStackInSlot(OUTPUT_SLOT));
        }
        setChanged();
    }

    private boolean hasRecipe() {
        Optional<CrushRecipes> recipe = getCurrentRecipe();
        if (recipe.isEmpty()) return false;

        ItemStack result = recipe.get().getResultItem(level.registryAccess());
        return canInsertItemIntoOutputSlot(result.getItem());
    }

    private Optional<CrushRecipes> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, this.itemHandler.getStackInSlot(i));
        }

        return this.level.getRecipeManager().getRecipeFor(CrushRecipes.Type.INSTANCE, inventory, level);
    }

    private boolean canInsertItemIntoOutputSlot(Item item) {
        ItemStack outputStack = itemHandler.getStackInSlot(OUTPUT_SLOT);
        if (outputStack.isEmpty()) {
            return true;
        }
        return outputStack.getItem() == item && outputStack.getCount() < outputStack.getMaxStackSize();
    }

    private boolean hasProgressFinished() {
        return progress >= maxProgress;
    }

    private void increaseCraftingProgress() {
        progress++;
    }
}
