package com.tortugolen.ostrea.Blocks;

import com.tortugolen.ostrea.BlockEntities.MechanicalOysterBlockEntity;
import com.tortugolen.ostrea.BlockEntities.OysterBlockEntity;
import com.tortugolen.ostrea.Init.InitBlockEntities;
import com.tortugolen.ostrea.Init.InitEnchantments;
import com.tortugolen.ostrea.Init.InitItems;
import com.tortugolen.ostrea.Init.InitSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class MechanicalOysterBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final VoxelShape NORTH_AABB = Block.box(1, 0, 2, 15, 3, 15);
    public static final VoxelShape SOUTH_AABB = Block.box(1, 0, 1, 15, 3, 14);
    public static final VoxelShape EAST_AABB = Block.box(1, 0, 1, 14, 3, 15);
    public static final VoxelShape WEST_AABB = Block.box(2, 0, 1, 15, 3, 15);

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public MechanicalOysterBlock(Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, Boolean.valueOf(true)));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        return switch (direction) {
            default -> NORTH_AABB;
            case EAST -> EAST_AABB;
            case WEST -> WEST_AABB;
            case SOUTH -> SOUTH_AABB;
        };
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        FluidState fluidstate = pContext.getLevel().getFluidState(pContext.getClickedPos());
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite()).setValue(WATERLOGGED, Boolean.valueOf(fluidstate.is(FluidTags.WATER) && fluidstate.getAmount() == 8));
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, WATERLOGGED);
    }

    public BlockState updateShape(BlockState p_52111_, Direction p_52112_, BlockState p_52113_, LevelAccessor p_52114_, BlockPos p_52115_, BlockPos p_52116_) {
        if (p_52111_.getValue(WATERLOGGED)) {
            p_52114_.scheduleTick(p_52115_, Fluids.WATER, Fluids.WATER.getTickDelay(p_52114_));
        }
        if (!p_52111_.canSurvive(p_52114_, p_52115_)) {
            p_52114_.scheduleTick(p_52115_, this, 1);
        }
        return super.updateShape(p_52111_, p_52112_, p_52113_, p_52114_, p_52115_, p_52116_);
    }

    public FluidState getFluidState(BlockState p_52127_) {
        return p_52127_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_52127_);
    }

    @Override
    public boolean canSurvive(BlockState p_52538_, LevelReader p_52539_, BlockPos p_52540_) {
        return canSupportRigidBlock(p_52539_, p_52540_.below());
    }

    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pSource) {
        if (!pState.canSurvive(pLevel, pPos)) {
            pLevel.destroyBlock(pPos, true);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new MechanicalOysterBlockEntity(pPos, pState);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof MechanicalOysterBlockEntity) {
                ((MechanicalOysterBlockEntity) blockEntity).drops();
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if (entity instanceof MechanicalOysterBlockEntity) {
                NetworkHooks.openScreen(((ServerPlayer)pPlayer), (MechanicalOysterBlockEntity)entity, pPos);
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
        return createTickerHelper(pBlockEntityType, InitBlockEntities.MECHANICAL_OYSTER_BE.get(),
                (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick(pLevel1, pPos, pState1));
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        if (state.getValue(WATERLOGGED)) {
            return 3;
        }
        return 0;
    }
}
