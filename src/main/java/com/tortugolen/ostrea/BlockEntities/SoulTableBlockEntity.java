package com.tortugolen.ostrea.BlockEntities;

import com.tortugolen.ostrea.GUIs.Menus.SoulTableMenu;
import com.tortugolen.ostrea.Init.InitBlockEntities;
import com.tortugolen.ostrea.Init.InitItems;
import com.tortugolen.ostrea.Init.InitTags;
import com.tortugolen.ostrea.Recipes.InfuseRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.effect.MobEffect;
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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SoulTableBlockEntity extends BlockEntity implements MenuProvider {
    public record SoulData(int souls, @Nullable Item remainder) {}

    private static final Map<Item, SoulData> SOULS = new HashMap<>();

    static {
        SOULS.put(Items.EXPERIENCE_BOTTLE, new SoulData(2, Items.GLASS_BOTTLE));
        SOULS.put(Items.ENCHANTED_BOOK, new SoulData(4, Items.BOOK));
        SOULS.put(Items.ENCHANTED_GOLDEN_APPLE, new SoulData(16, Items.GOLDEN_APPLE));
    }

    private final ContainerData data;
    private int progress = 0;
    private int maxProgress = 1200;
    private int souls = 0;
    private int maxSouls = 64;
    private int effectSouls = 0;
    private int maxEffectSouls = 64;
    private int soulsProgress = 0;
    private int maxSoulsProgress = 10;
    private int effectSoulsProgress = 0;
    private int maxEffectSoulsProgress = 300;
    private MobEffect effect;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    private final ItemStackHandler itemHandler = new ItemStackHandler(6) {
        @Override
        protected void onContentsChanged(int slot) {
            if (!level.isClientSide) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
            setChanged();
        }
    };

    public SoulTableBlockEntity(BlockPos pPos, BlockState pState) {
        super(InitBlockEntities.SOUL_TABLE_BE.get(), pPos, pState);
        data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> SoulTableBlockEntity.this.progress;
                    case 1 -> SoulTableBlockEntity.this.maxProgress;
                    case 2 -> SoulTableBlockEntity.this.souls;
                    case 3 -> SoulTableBlockEntity.this.maxSouls;
                    case 4 -> SoulTableBlockEntity.this.effectSouls;
                    case 5 -> SoulTableBlockEntity.this.maxEffectSouls;
                    case 6 -> SoulTableBlockEntity.this.soulsProgress;
                    case 7 -> SoulTableBlockEntity.this.maxSoulsProgress;
                    case 8 -> SoulTableBlockEntity.this.effectSoulsProgress;
                    case 9 -> SoulTableBlockEntity.this.maxEffectSoulsProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> SoulTableBlockEntity.this.progress = pValue;
                    case 1 -> SoulTableBlockEntity.this.maxProgress = pValue;
                    case 2 -> SoulTableBlockEntity.this.souls = pValue;
                    case 3 -> SoulTableBlockEntity.this.maxSouls = pValue;
                    case 4 -> SoulTableBlockEntity.this.effectSouls = pValue;
                    case 5 -> SoulTableBlockEntity.this.maxEffectSouls = pValue;
                    case 6 -> SoulTableBlockEntity.this.soulsProgress = pValue;
                    case 7 -> SoulTableBlockEntity.this.maxSoulsProgress = pValue;
                    case 8 -> SoulTableBlockEntity.this.effectSoulsProgress = pValue;
                    case 9 -> SoulTableBlockEntity.this.maxEffectSoulsProgress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 10;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.ostrea.soul_table");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new SoulTableMenu(id, inventory, this, data);
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
        Containers.dropContents(level, worldPosition, inventory);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("inventory", itemHandler.serializeNBT());
        tag.putInt("soul_table.progress", progress);
        tag.putInt("soul_table.souls", souls);
        tag.putInt("soul_table.effect_souls", effectSouls);
        tag.putInt("soul_table.souls_progress", soulsProgress);
        tag.putInt("soul_table.effect_souls_progress", effectSoulsProgress);

        if (effect != null) {
            tag.putString("soul_table.effect", ForgeRegistries.MOB_EFFECTS.getKey(effect).toString());
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("inventory")) {
            itemHandler.deserializeNBT(tag.getCompound("inventory"));
        }
        progress = tag.getInt("soul_table.progress");
        souls = tag.getInt("soul_table.souls");
        effectSouls = tag.getInt("soul_table.effect_souls");
        soulsProgress = tag.getInt("soul_table.souls_progress");
        effectSoulsProgress = tag.getInt("soul_table.effect_souls_progress");

        if (tag.contains("soul_table.effect")) {
            effect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(tag.getString("soul_table.effect")));
        }
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Nullable
    public MobEffect getEffect() {
        return effect;
    }

    public ItemStack getRenderMainItem() {
        return itemHandler.getStackInSlot(5);
    }

    public ItemStack getRenderStatusferItem() {
        return itemHandler.getStackInSlot(2);
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if (hasRecipe()) {
            progress++;
            spawnParticles();
            setChanged(level, pPos, pState);

            if (progress >= maxProgress) {
                craftItem(pLevel, pPos);
                progress = 0;
            }
        } else {
            progress = 0;
        }
        if (effectSouls == 0) {
            effect = null;
        }
        getSouls();
        getEffectSouls();
        setChanged(level, pPos, pState);
    }

    private void craftItem(Level pLevel, BlockPos pPos) {
        Optional<InfuseRecipes> recipe = getCurrentRecipe();
        if (recipe.isEmpty()) return;

        ItemStack result = recipe.get().getResultItem(level.registryAccess());

        effect = recipe.get().getEffect();
        itemHandler.extractItem(4, 1, false);
        itemHandler.extractItem(5, 1, false);
        itemHandler.setStackInSlot(5, result);
        effectSouls = 0;

        pLevel.playSound(null, pPos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1F, 1F);
        setChanged();
    }

    private boolean hasRecipe() {
        Optional<InfuseRecipes> recipe = getCurrentRecipe();
        if (recipe.isEmpty()) return false;

        return effect == recipe.get().getEffect() && effectSouls == maxEffectSouls;
    }

    private Optional<InfuseRecipes> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        return level.getRecipeManager().getRecipeFor(InfuseRecipes.Type.INSTANCE, inventory, level);
    }

    private void getSouls() {
        if (souls < maxSouls) {
            ItemStack pStack = itemHandler.getStackInSlot(0);

            if (SOULS.containsKey(pStack.getItem())) {
                int itemSouls = SOULS.get(pStack.getItem()).souls();
                Item itemRemainder = SOULS.get(pStack.getItem()).remainder();

                if (itemRemainder == null || itemHandler.getStackInSlot(1).isEmpty() || (itemHandler.getStackInSlot(1).getItem() == itemRemainder && itemHandler.getStackInSlot(1).getCount() < itemHandler.getStackInSlot(1).getMaxStackSize())) {
                    soulsProgress++;

                    if (soulsProgress >= maxSoulsProgress) {
                        itemHandler.extractItem(0, 1, false);
                        souls = Math.min(maxSouls, souls + itemSouls);
                        soulsProgress = 0;

                        if (itemRemainder != null) {
                            ItemStack stackRemainder = new ItemStack(itemRemainder, 1);

                            if (itemHandler.getStackInSlot(1).isEmpty()) {
                                itemHandler.setStackInSlot(1, stackRemainder);
                            } else {
                                itemHandler.getStackInSlot(1).grow(1);
                            }
                        }
                    }
                } else {
                    soulsProgress = 0;
                }
            } else {
                soulsProgress = 0;
            }
        } else {
            soulsProgress = 0;
        }
    }

    private void getEffectSouls() {
        if (effectSouls < maxEffectSouls && souls >= 16) {
            ItemStack statusferInput = itemHandler.getStackInSlot(2);

            if (statusferInput.is(InitTags.Items.EFFECT_STATUSFERS)) {
                CompoundTag pTag = statusferInput.getOrCreateTag();

                if (pTag.contains("effect")) {
                    String effectString = pTag.getString("effect");
                    MobEffect pEffect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(effectString));

                    if (pEffect != null) {
                        if (effect == null) {
                            effect = pEffect;
                            setChanged();

                            if (!level.isClientSide) {
                                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
                            }
                        }

                        if (pEffect == effect) {
                            effectSoulsProgress++;

                            if (effectSoulsProgress >= maxEffectSoulsProgress) {
                                itemHandler.extractItem(2, 1, false);
                                souls -= 16;
                                effectSouls = Math.min(maxEffectSouls, effectSouls + 16);
                                effectSoulsProgress = 0;

                                ItemStack statusfer = new ItemStack(InitItems.STATUSFER.get());
                                if (itemHandler.getStackInSlot(3).isEmpty()) {
                                    itemHandler.setStackInSlot(3, statusfer);
                                } else {
                                    itemHandler.getStackInSlot(3).grow(1);
                                }
                            }
                        } else {
                            effectSoulsProgress = 0;
                        }
                    } else {
                        effectSoulsProgress = 0;
                    }
                } else {
                    effectSoulsProgress = 0;
                }
            } else {
                effectSoulsProgress = 0;
            }
        } else {
            effectSoulsProgress = 0;
        }
    }

    private void spawnParticles() {
        if (level == null || !(level instanceof ServerLevel pLevel)) return;

        RandomSource pRandom = level.random;

        double centerX = worldPosition.getX() + 0.5;
        double centerY = worldPosition.getY() + 1;
        double centerZ = worldPosition.getZ() + 0.5;

        for (int i = 0; i < 3; i++) {
            double offsetX = (pRandom.nextDouble() - 0.5) * 1;
            double offsetY = pRandom.nextDouble() - 0.5;
            double offsetZ = (pRandom.nextDouble() - 0.5) * 1;

            double x = centerX + offsetX;
            double y = centerY + offsetY;
            double z = centerZ + offsetZ;

            double velocityX = (centerX - x) * 0.1;
            double velocityY = 0.1;
            double velocityZ = (centerZ - z) * 0.1;

            pLevel.sendParticles(ParticleTypes.ENCHANT, x, y, z, 1, velocityX, velocityY, velocityZ, 1.0);
        }
    }
}