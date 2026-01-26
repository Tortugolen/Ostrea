package com.tortugolen.ostrea.BlockEntities;

import com.tortugolen.ostrea.Init.InitBlockEntities;
import com.tortugolen.ostrea.Init.InitItems;
import com.tortugolen.ostrea.Recipes.AbstractCultRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ContainerData;
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

public class DeepslateAltarBlockEntity extends BlockEntity {
    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 1200;
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            setChanged();
        }
    };

    public DeepslateAltarBlockEntity(BlockPos pPos, BlockState pState) {
        super(InitBlockEntities.DEEPSLATE_ALTAR_BE.get(), pPos, pState);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> DeepslateAltarBlockEntity.this.progress;
                    case 1 -> DeepslateAltarBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> DeepslateAltarBlockEntity.this.progress = pValue;
                    case 1 -> DeepslateAltarBlockEntity.this.maxProgress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
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
        tag.putInt("deepslate_altar.progress", progress);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("inventory")) {
            itemHandler.deserializeNBT(tag.getCompound("inventory"));
        }
        progress = tag.getInt("deepslate_altar.progress");
    }

    public ItemStack getItem() {
        return itemHandler.getStackInSlot(0);
    }

    public void setItem(ItemStack pStack) {
        itemHandler.setStackInSlot(0, pStack);
        setChanged();

        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
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

    private BlockPos relativeWithDistance(BlockPos pos, Direction dir, int dist) {
        return pos.offset(dir.getStepX() * dist, dir.getStepY() * dist, dir.getStepZ() * dist);
    }

    private BlockPos relativeDiagonalWithDistance(BlockPos pos, Direction d1, Direction d2, int dist) {
        return pos.offset(
                d1.getStepX() * dist + d2.getStepX() * dist,
                d1.getStepY() * dist + d2.getStepY() * dist,
                d1.getStepZ() * dist + d2.getStepZ() * dist
        );
    }

    public @Nullable NacreAltarBlockEntity getAltarAt(Level pLevel, Direction dir, int dist) {
        if (pLevel == null) return null;

        BlockPos pPos = worldPosition.relative(dir, dist);
        BlockEntity be = pLevel.getBlockEntity(pPos);

        if (be instanceof NacreAltarBlockEntity altar)
            return altar;

        return null;
    }

    public @Nullable NacreAltarBlockEntity getAltarAtDiagonal(Level pLevel, Direction d1, Direction d2, int dist) {
        if (pLevel == null) return null;

        BlockPos pPos = worldPosition.relative(d1, getDiagonalOffset()).relative(d2, getDiagonalOffset());
        BlockEntity be = pLevel.getBlockEntity(pPos);

        if (be instanceof NacreAltarBlockEntity altar)
            return altar;

        return null;
    }

    public ItemStack getItemAt(Direction dir, int size) {
        NacreAltarBlockEntity altar = getAltarAt(level, dir, getCardinalOffset(size));
        return altar != null ? altar.getItem() : ItemStack.EMPTY;
    }

    public ItemStack getItemAtDiagonal(Direction d1, Direction d2) {
        NacreAltarBlockEntity altar = getAltarAtDiagonal(level, d1, d2, 0);
        return altar != null ? altar.getItem() : ItemStack.EMPTY;
    }

    public NonNullList<ItemStack> getMatrixItems(int size) {
        NonNullList<ItemStack> list = NonNullList.withSize(size, ItemStack.EMPTY);

        list.set(0, getItem());

        int c = getCardinalOffset(size);

        if (!hasAllAltarsAtDistance(c,
                new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST})) {
            return NonNullList.create();
        }

        list.set(1, getItemAt(Direction.NORTH, size));

        if (size == 9) {
            if (!hasAllDiagonalAltarsAtDistance(getDiagonalOffset())) {
                return NonNullList.create();
            }

            list.set(2, getItemAtDiagonal(Direction.NORTH, Direction.EAST));
            list.set(3, getItemAt(Direction.EAST, size));
            list.set(4, getItemAtDiagonal(Direction.SOUTH, Direction.EAST));
            list.set(5, getItemAt(Direction.SOUTH, size));
            list.set(6, getItemAtDiagonal(Direction.SOUTH, Direction.WEST));
            list.set(7, getItemAt(Direction.WEST, size));
            list.set(8, getItemAtDiagonal(Direction.NORTH, Direction.WEST));
        } else {
            list.set(2, getItemAt(Direction.EAST, size));
            list.set(3, getItemAt(Direction.SOUTH, size));
            list.set(4, getItemAt(Direction.WEST, size));
        }

        return list;
    }

    private boolean hasAllAltarsAtDistance(int distance, Direction[] directions) {
        for (Direction dir : directions) {
            BlockPos pos = worldPosition.relative(dir, distance);
            BlockEntity be = level.getBlockEntity(pos);
            if (!(be instanceof NacreAltarBlockEntity)) {
                return false;
            }
        }
        return true;
    }

    private boolean hasAllDiagonalAltarsAtDistance(int distance) {
        Direction[] diagonals = new Direction[]{
                Direction.NORTH,
                Direction.EAST,
                Direction.SOUTH,
                Direction.WEST
        };
        return getAltarAtDiagonal(level, Direction.NORTH, Direction.EAST, distance) != null
                && getAltarAtDiagonal(level, Direction.SOUTH, Direction.EAST, distance) != null
                && getAltarAtDiagonal(level, Direction.SOUTH, Direction.WEST, distance) != null
                && getAltarAtDiagonal(level, Direction.NORTH, Direction.WEST, distance) != null;
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if (pLevel == null || pLevel.isClientSide()) return;

        if (hasRecipe(pLevel, 9)) {
            increaseCraftingProgress();
            setChanged();

            if (hasProgressFinished()) {
                craftItem(pLevel, pPos);
                resetProgress();
            }

        } else if (hasRecipe(pLevel, 5)) {
            increaseCraftingProgress();
            setChanged();

            if (hasProgressFinished()) {
                craftItem(pLevel, pPos);
                resetProgress();
            }

        } else {
            resetProgress();
            setChanged();
        }
    }

    public boolean hasRecipe(Level pLevel, int size) {
        NonNullList<ItemStack> items = getMatrixItems(size);
        if (items.isEmpty()) return false;

        for (ItemStack stack : items) {
            if (stack.isEmpty()) {
                return false;
            }
        }

        SimpleContainer container = new SimpleContainer(items.size());
        for (int i = 0; i < items.size(); i++) container.setItem(i, items.get(i));

        Optional<AbstractCultRecipes> optionalRecipe = pLevel.getRecipeManager()
                .getRecipeFor(AbstractCultRecipes.Type.INSTANCE, container, pLevel);

        if (optionalRecipe.isEmpty()) return false;

        AbstractCultRecipes recipe = optionalRecipe.get();
        if (!recipe.getInputNbt().isEmpty()) {
            ItemStack centralItem = items.get(0);
            CompoundTag requiredNbt = recipe.getInputNbt();

            if (!centralItem.hasTag()) return false;

            CompoundTag itemTag = centralItem.getTag();
            for (String key : requiredNbt.getAllKeys()) {
                if (!itemTag.contains(key)) return false;

                byte tagType = requiredNbt.getTagType(key);

                switch (tagType) {
                    case 3:
                        if (itemTag.getInt(key) != requiredNbt.getInt(key)) return false;
                        break;
                    case 8:
                        if (!itemTag.getString(key).equals(requiredNbt.getString(key))) return false;
                        break;
                    case 5:
                        if (itemTag.getFloat(key) != requiredNbt.getFloat(key)) return false;
                        break;
                    case 10:
                        if (!itemTag.getCompound(key).equals(requiredNbt.getCompound(key))) return false;
                        break;
                    default:
                        if (!itemTag.get(key).equals(requiredNbt.get(key))) return false;
                }
            }
        }

        return true;
    }

    private void craftItem(Level pLevel, BlockPos pPos) {
        Optional<AbstractCultRecipes> optionalRecipe = getCurrentRecipe(9);
        int sizeToUse = 9;

        if (optionalRecipe.isEmpty()) {
            optionalRecipe = getCurrentRecipe(5);
            sizeToUse = 5;
        }

        if (optionalRecipe.isEmpty()) return;

        AbstractCultRecipes recipe = optionalRecipe.get();
        ItemStack result = recipe.getResultItem(level.registryAccess()).copy();

        if (!recipe.getResultNbt().isEmpty()) {
            CompoundTag resultTag = result.getOrCreateTag();
            resultTag.merge(recipe.getResultNbt());
        }

        ItemStack pStack = getItem().copy();

        if (isFullReceptaclePearlItem()) {
            CompoundTag pTag = pStack.getTag();

            if (pTag != null && pTag.contains("stored_effect", CompoundTag.TAG_STRING)) {
                String pEffect = pTag.getString("stored_effect");
                CompoundTag resultTag = result.getOrCreateTag();
                resultTag.putString("effect", pEffect);
            }
        }

        consumeIngredients(sizeToUse);

        setItem(result);

        pLevel.playSound(null, pPos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1F, 1F);

        spawnCraftingCompleteParticles();

        setChanged();
    }

    private void consumeIngredients(int size) {
        int cardinalOffset = getCardinalOffset(size);

        consumeAt(Direction.NORTH, cardinalOffset);
        consumeAt(Direction.EAST, cardinalOffset);
        consumeAt(Direction.SOUTH, cardinalOffset);
        consumeAt(Direction.WEST, cardinalOffset);

        if (size == 9) {
            consumeAtDiagonal(Direction.NORTH, Direction.EAST);
            consumeAtDiagonal(Direction.SOUTH, Direction.EAST);
            consumeAtDiagonal(Direction.SOUTH, Direction.WEST);
            consumeAtDiagonal(Direction.NORTH, Direction.WEST);
        }
    }

    private void consumeAt(Direction dir, int dist) {
        BlockPos pos = worldPosition.relative(dir, dist);
        BlockEntity be = level.getBlockEntity(pos);

        if (be instanceof NacreAltarBlockEntity altar) {
            ItemStack stack = altar.getItem();
            if (!stack.isEmpty()) {
                spawnItemTransferParticle(pos, worldPosition, stack);
                stack.shrink(1);
                altar.setItem(stack);
                altar.setChanged();
            }
        }
    }

    private void consumeAtDiagonal(Direction d1, Direction d2) {
        BlockPos pos = worldPosition
                .relative(d1, getDiagonalOffset())
                .relative(d2, getDiagonalOffset());

        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof NacreAltarBlockEntity altar) {
            ItemStack stack = altar.getItem();
            if (!stack.isEmpty()) {
                spawnItemTransferParticle(pos, worldPosition, stack);
                stack.shrink(1);
                altar.setItem(stack);
                altar.setChanged();
            }
        }
    }

    private void spawnItemTransferParticle(BlockPos fromPos, BlockPos toPos, ItemStack stack) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        double startX = fromPos.getX() + 0.5;
        double startY = fromPos.getY() + 1.0;
        double startZ = fromPos.getZ() + 0.5;

        double endX = toPos.getX() + 0.5;
        double endY = toPos.getY() + 1.0;
        double endZ = toPos.getZ() + 0.5;

        double velocityX = (endX - startX) * 0.12;
        double velocityY = (endY - startY) * 0.12;
        double velocityZ = (endZ - startZ) * 0.12;

        serverLevel.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, stack), startX, startY, startZ, 1, velocityX, velocityY, velocityZ, 0.0);
    }

    private void spawnCraftingCompleteParticles() {
        if (!(level instanceof ServerLevel serverLevel)) return;

        double centerX = worldPosition.getX() + 0.5;
        double centerY = worldPosition.getY() + 1.2;
        double centerZ = worldPosition.getZ() + 0.5;

        int particleCount = 10;
        double radius = 0.6;

        for (int i = 0; i < particleCount; i++) {
            double angle = 2 * Math.PI * i / particleCount;
            double x = centerX + radius * Math.cos(angle);
            double z = centerZ + radius * Math.sin(angle);
            double y = centerY + 0.2 * Math.sin(angle * 2);

            serverLevel.sendParticles(ParticleTypes.ENCHANT, x, y, z, 1, 0, 0, 0, 0);
        }
    }

    private void resetProgress() {
        progress = 0;
    }

    private boolean hasProgressFinished() {
        return progress >= maxProgress;
    }

    private void increaseCraftingProgress() {
        progress++;
    }

    private Optional<AbstractCultRecipes> getCurrentRecipe(int size) {
        NonNullList<ItemStack> items = getMatrixItems(size);
        if (items.isEmpty()) return Optional.empty();

        SimpleContainer container = new SimpleContainer(items.size());
        for (int i = 0; i < items.size(); i++) container.setItem(i, items.get(i));

        return level.getRecipeManager().getRecipeFor(AbstractCultRecipes.Type.INSTANCE, container, level);
    }

    private int getCardinalOffset(int size) {
        return size == 9 ? 3 : 2;
    }

    private int getDiagonalOffset() {
        return 2;
    }

    private boolean isFullReceptaclePearlItem() {
        ItemStack pStack = getItem();
        if (!pStack.hasTag()) return false;

        CompoundTag pTag = pStack.getTag();
        if (!pTag.contains("effect_amount", CompoundTag.TAG_INT)) return false;
        if (pTag.getInt("effect_amount") != 64) return false;
        if (!pStack.is(InitItems.RECEPTACLE_PEARL.get())) return false;

        return true;
    }

    private boolean isFullStatusferItem() {
        ItemStack pStack = getItem();
        if (!pStack.hasTag()) return false;

        CompoundTag pTag = pStack.getTag();
        if (!pTag.contains("effect", CompoundTag.TAG_STRING)) return false;
        if (!pStack.is(InitItems.STATUSFER.get())) return false;

        return true;
    }
}