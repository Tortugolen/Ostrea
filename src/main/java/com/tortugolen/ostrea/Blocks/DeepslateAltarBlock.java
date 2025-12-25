package com.tortugolen.ostrea.Blocks;

import com.tortugolen.ostrea.BlockEntities.DeepslateAltarBlockEntity;
import com.tortugolen.ostrea.Init.InitBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class DeepslateAltarBlock extends BaseEntityBlock {
    protected static final VoxelShape SHAPE = Shapes.or(
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
            Block.box(2.0D, 2.0D, 2.0D, 14.0D, 6.0D, 14.0D),
            Block.box(4.0D, 6.0D, 4.0D, 12.0D, 12.0D, 12.0D),
            Block.box(1.0D, 12.0D, 1.0D, 15.0D, 14.0D, 15.0D)
    );

    public DeepslateAltarBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    public static VoxelShape getSHAPE() {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new DeepslateAltarBlockEntity(pPos, pState);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof DeepslateAltarBlockEntity) {
                ((DeepslateAltarBlockEntity) blockEntity).drops();
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            DeepslateAltarBlockEntity altarBlockEntity = (DeepslateAltarBlockEntity) pLevel.getBlockEntity(pPos);
            if (altarBlockEntity == null) return InteractionResult.FAIL;

            if (!pPlayer.getItemInHand(pHand).isEmpty()) {
                if (altarBlockEntity.getItem().isEmpty()) {
                    ItemStack oneItem = pPlayer.getItemInHand(pHand).copy();
                    oneItem.setCount(1);
                    altarBlockEntity.setItem(oneItem);
                    if (!pPlayer.isCreative()) pPlayer.getItemInHand(pHand).shrink(1);
                    pLevel.playSound(null, pPos, SoundEvents.ALLAY_ITEM_GIVEN, SoundSource.BLOCKS, 1F, 1F);
                    return InteractionResult.SUCCESS;
                }
            }

            if (pPlayer.getItemInHand(pHand).isEmpty()) {
                if (!altarBlockEntity.getItem().isEmpty()) {
                    ItemStack itemInAltar = altarBlockEntity.getItem();
                    ItemStack oneItem = itemInAltar.copy();
                    oneItem.setCount(1);

                    itemInAltar.shrink(1);
                    if (itemInAltar.getCount() <= 0) {
                        altarBlockEntity.setItem(ItemStack.EMPTY);
                    } else {
                        altarBlockEntity.setItem(itemInAltar);
                    }

                    pPlayer.addItem(oneItem);
                    pLevel.playSound(null, pPos, SoundEvents.ALLAY_ITEM_TAKEN, SoundSource.BLOCKS, 1F, 1F);
                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if(pLevel.isClientSide()) {
            return null;
        }
        return createTickerHelper(pBlockEntityType, InitBlockEntities.DEEPSLATE_ALTAR_BE.get(),
                (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick(pLevel1, pPos, pState1));
    }
}
