package com.tortugolen.ostrea.BlockEntities;

import com.tortugolen.ostrea.GUIs.Menus.MechanicalOysterMenu;
import com.tortugolen.ostrea.Init.InitBlockEntities;
import com.tortugolen.ostrea.Init.InitItems;
import com.tortugolen.ostrea.Recipes.ExtendedPearlizationRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MechanicalOysterBlockEntity extends BlockEntity implements MenuProvider {

    private static final Map<Item, Integer> CALCIUM_CARBONATE_ITEMS = new HashMap<>();
    private static final Map<Item, Integer> PURITY = new HashMap<>();
    private static final Map<Item, Integer> IMPURITY_COUNT = new HashMap<>();

    static {
        CALCIUM_CARBONATE_ITEMS.put(InitItems.CALCIUM_CARBONATE.get(), 1);
        CALCIUM_CARBONATE_ITEMS.put(InitItems.ARAGONITE_POWDER.get(), 1);
    }
    static {
        PURITY.put(InitItems.CALCIUM_CARBONATE.get(), 100);
        PURITY.put(InitItems.ARAGONITE_POWDER.get(), 50);
    }
    static {
        IMPURITY_COUNT.put(InitItems.CALCIUM_CARBONATE.get(), 0);
        IMPURITY_COUNT.put(InitItems.ARAGONITE_POWDER.get(), 1);
    }

    private static final Set<Item> MODIFIERS = new HashSet<>();

    static {
        MODIFIERS.add(InitItems.ROUND_AMOM.get());
        MODIFIERS.add(InitItems.ROUND_MOM.get());
        MODIFIERS.add(InitItems.TIP_AMOM.get());
        MODIFIERS.add(InitItems.TIP_MOM.get());
        MODIFIERS.add(InitItems.CALCIUM_CARBONATE_AMOM.get());
        MODIFIERS.add(InitItems.CALCIUM_CARBONATE_MOM.get());
        MODIFIERS.add(InitItems.IRON_AMOM.get());
        MODIFIERS.add(InitItems.IRON_MOM.get());
        MODIFIERS.add(InitItems.COPPER_AMOM.get());
        MODIFIERS.add(InitItems.COPPER_MOM.get());
        MODIFIERS.add(InitItems.GOLD_AMOM.get());
        MODIFIERS.add(InitItems.GOLD_MOM.get());
    }

    private static final int INGREDIENT_SLOT = 0;
    private static final int RESULT_SLOT = 1;
    private static final int CALCIUM_CARBONATE_SLOT = 2;
    private static final int WASTE_SLOT = 3;
    private static final int MODIFIER_SLOT_1 = 4;
    private static final int MODIFIER_SLOT_2 = 5;

    protected final ContainerData data;
    private int progress;
    private int maxProgress = 72000;
    private int amountCalciumCarbonate;
    private int maxAmountCalciumCarbonate = 8;
    private int cyclesCalciumCarbonate;
    private int maxCyclesCalciumCarbonate = 9000;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    private final ItemStackHandler itemHandler = new ItemStackHandler(6) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            setChanged();
        }
    };

    public MechanicalOysterBlockEntity(BlockPos pPos, BlockState pState) {
        super(InitBlockEntities.MECHANICAL_OYSTER_BE.get(), pPos, pState);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> progress;
                    case 1 -> maxProgress;
                    case 2 -> amountCalciumCarbonate;
                    case 3 -> maxAmountCalciumCarbonate;
                    case 4 -> cyclesCalciumCarbonate;
                    case 5 -> maxCyclesCalciumCarbonate;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> progress = pValue;
                    case 1 -> maxProgress = pValue;
                    case 2 -> amountCalciumCarbonate = pValue;
                    case 3 -> maxAmountCalciumCarbonate = pValue;
                    case 4 -> cyclesCalciumCarbonate = pValue;
                    case 5 -> maxCyclesCalciumCarbonate = pValue;
                }
            }

            @Override
            public int getCount() {
                return 6;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.ostrea.mechanical_oyster");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new MechanicalOysterMenu(id, inventory, this, this.data);
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
        tag.putInt("mechanical_oyster.progress", progress);
        tag.putInt("mechanical_oyster.amountCalciumCarbonate", amountCalciumCarbonate);
        tag.putInt("mechanical_oyster.cyclesCalciumCarbonate", cyclesCalciumCarbonate);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("inventory")) {
            itemHandler.deserializeNBT(tag.getCompound("inventory"));
        }
        progress = tag.getInt("mechanical_oyster.progress");
        amountCalciumCarbonate = tag.getInt("mechanical_oyster.amountCalciumCarbonate");
        cyclesCalciumCarbonate = tag.getInt("mechanical_oyster.cyclesCalciumCarbonate");
    }

    public void tick(Level pLevel, BlockPos pPosition, BlockState pState) {
        if (hasRecipe() && isWaterlogged()) {
            increaseCraftingProgress();
            setChanged(pLevel, pPosition, pState);
            startCalciumCarbonateCycles();
            setChanged(pLevel, pPosition, pState);

            if (hasProgressFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            failedProgress(pLevel, pPosition);
            resetProgress();
        }

        calciumCarbonateQuantity();
        continueCalciumCarbonateCycles();
        setChanged(pLevel, pPosition, pState);
    }

    private void calciumCarbonateQuantity() {
        ItemStack slotStack = itemHandler.getStackInSlot(CALCIUM_CARBONATE_SLOT);
        if (!CALCIUM_CARBONATE_ITEMS.containsKey(slotStack.getItem())) return;

        int amount = CALCIUM_CARBONATE_ITEMS.get(slotStack.getItem());
        if (amountCalciumCarbonate + amount <= maxAmountCalciumCarbonate) {
            if (itemHandler.getStackInSlot(WASTE_SLOT).getCount() < 64) {
                itemHandler.extractItem(CALCIUM_CARBONATE_SLOT, 1, false);

                if (canInsertItemIntoWasteSlot(InitItems.IMPURITIES.get())) {
                    float IMPURITIES = ((100 - PURITY.getOrDefault(slotStack.getItem(), 100)) / 100F);
                    if (IMPURITIES != 0 && RandomSource.create().nextFloat() < IMPURITIES) {
                        if (itemHandler.getStackInSlot(WASTE_SLOT).isEmpty()) {
                            itemHandler.setStackInSlot(WASTE_SLOT, new ItemStack(InitItems.IMPURITIES.get(), IMPURITY_COUNT.get(slotStack.getItem())));
                        } else if (itemHandler.getStackInSlot(WASTE_SLOT).getItem() == InitItems.IMPURITIES.get()) {
                            itemHandler.getStackInSlot(WASTE_SLOT).grow(IMPURITY_COUNT.get(slotStack.getItem()));
                            itemHandler.setStackInSlot(WASTE_SLOT, itemHandler.getStackInSlot(WASTE_SLOT));
                        }
                    }
                }
                amountCalciumCarbonate += amount;
                setChanged();
            }
        }
    }

    private void startCalciumCarbonateCycles() {
        if (cyclesCalciumCarbonate == 0 && amountCalciumCarbonate > 0) {
            amountCalciumCarbonate--;
            cyclesCalciumCarbonate = maxCyclesCalciumCarbonate;
            setChanged();
        }
    }

    private void continueCalciumCarbonateCycles() {
        if (cyclesCalciumCarbonate > 0 && cyclesCalciumCarbonate <= maxCyclesCalciumCarbonate) {
            cyclesCalciumCarbonate--;
        }
    }

    private void resetProgress() {
        progress = 0;
    }

    private void craftItem() {
        Optional<ExtendedPearlizationRecipes> recipe = getCurrentRecipe();
        if (recipe.isEmpty()) return;
        ItemStack result = recipe.get().getResultItem(level.registryAccess());
        ItemStack outputStack = itemHandler.getStackInSlot(RESULT_SLOT);
        itemHandler.extractItem(INGREDIENT_SLOT, 1, false);
        itemHandler.extractItem(MODIFIER_SLOT_1, 1, false);
        itemHandler.extractItem(MODIFIER_SLOT_2, 1, false);
        if (outputStack.isEmpty()) {
            itemHandler.setStackInSlot(RESULT_SLOT, new ItemStack(result.getItem()));
        } else if (outputStack.getItem() == result.getItem()) {
            outputStack.grow(1);
            itemHandler.setStackInSlot(RESULT_SLOT, outputStack);
        }
        setChanged();
    }

    private boolean hasRecipe() {
        Optional<ExtendedPearlizationRecipes> recipe = getCurrentRecipe();
        if (recipe.isEmpty()) {
            return false;
        }
        ItemStack result = recipe.get().getResultItem(level.registryAccess());
        return canInsertItemIntoOutputSlot(result.getItem());
    }

    private Optional<ExtendedPearlizationRecipes> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        return level.getRecipeManager().getRecipeFor(ExtendedPearlizationRecipes.Type.INSTANCE, inventory, level);
    }

    private void failedProgress(Level pLevel, BlockPos pPosition) {
        if (progress >= maxProgress / 2) {
            ItemStack ingredientStack = itemHandler.getStackInSlot(INGREDIENT_SLOT);
            if (ingredientStack.isEmpty()) return;
            itemHandler.extractItem(INGREDIENT_SLOT, 1, false);
            ItemStack resultSlot = itemHandler.getStackInSlot(RESULT_SLOT);
            if (resultSlot.isEmpty()) {
                itemHandler.setStackInSlot(RESULT_SLOT, new ItemStack(InitItems.AMORPHOUS_PEARL.get()));
            } else if (resultSlot.getItem() == InitItems.AMORPHOUS_PEARL.get() && resultSlot.getCount() < resultSlot.getMaxStackSize()) {
                resultSlot.grow(1);
                itemHandler.setStackInSlot(RESULT_SLOT, resultSlot);
            } else {
                pLevel.addFreshEntity(new net.minecraft.world.entity.item.ItemEntity(pLevel, pPosition.getX() + 0.5F, pPosition.getY() + 1F, pPosition.getZ() + 0.5F, new ItemStack(InitItems.AMORPHOUS_PEARL.get())));
            }
            setChanged();
        }
    }

    private boolean isWaterlogged() {
        BlockState state = this.getBlockState();
        return state.hasProperty(BlockStateProperties.WATERLOGGED)
            && state.getValue(BlockStateProperties.WATERLOGGED);
    }

    private boolean canInsertItemIntoOutputSlot(Item item) {
        ItemStack outputStack = itemHandler.getStackInSlot(RESULT_SLOT);
        if (outputStack.isEmpty()) {
            return true;
        }
        return outputStack.getItem() == item && outputStack.getCount() < outputStack.getMaxStackSize();
    }

    private boolean canInsertItemIntoWasteSlot(Item item) {
        ItemStack outputStack = itemHandler.getStackInSlot(WASTE_SLOT);
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

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        setChanged();
    }

    public boolean getActiveModifier1() {
        ItemStack stack = this.itemHandler.getStackInSlot(4);
        return MODIFIERS.contains(stack.getItem());
    }

    public boolean getActiveModifier2() {
        ItemStack stack = this.itemHandler.getStackInSlot(5);
        return MODIFIERS.contains(stack.getItem());
    }
}
