package com.tortugolen.ostrea.Blocks;

import com.tortugolen.ostrea.Init.InitBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class AragoniteShardBlock extends AmethystBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public AragoniteShardBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(BlockStateProperties.FACING);
        switch (facing) {
            case UP:
                return Block.box(5, 0, 5, 11, 12, 11);
            case DOWN:
                return Block.box(5, 4, 5, 11, 16, 11);
            case NORTH:
                return Block.box(5, 5, 4, 11, 11, 16);
            case SOUTH:
                return Block.box(5, 5, 0, 11, 11, 12);
            case WEST:
                return Block.box(4, 5, 5, 16, 11, 11);
            case EAST:
                return Block.box(0, 5, 5, 12, 11, 11);
            default:
                return null;
        }
    }

    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        Direction direction = pState.getValue(FACING);
        BlockPos blockpos = pPos.relative(direction.getOpposite());
        return pLevel.getBlockState(blockpos).isFaceSturdy(pLevel, blockpos, direction);
    }

    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pPos, BlockPos pNeighborPos) {
        if (pState.getValue(WATERLOGGED)) {
            pLevel.scheduleTick(pPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
        }

        return pDirection == pState.getValue(FACING).getOpposite() && !pState.canSurvive(pLevel, pPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(pState, pDirection, pNeighborState, pLevel, pPos, pNeighborPos);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        LevelAccessor levelaccessor = pContext.getLevel();
        BlockPos blockpos = pContext.getClickedPos();
        return this.defaultBlockState().setValue(WATERLOGGED, Boolean.valueOf(levelaccessor.getFluidState(blockpos).getType() == Fluids.WATER)).setValue(FACING, pContext.getClickedFace());
    }

    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    public FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(WATERLOGGED, FACING);
    }

    private boolean isValidBlock(Level pLevel, BlockPos pPos) {
        BlockState pState = pLevel.getBlockState(pPos);
        return pState.getBlock() == InitBlocks.ARAGONITE_CLUSTER.get() || pState.getBlock() == Blocks.POINTED_DRIPSTONE;
    }

    private boolean isWaterlogged(Level pLevel, BlockPos pPos) {
        BlockState pState = pLevel.getBlockState(pPos);
        return pState.hasProperty(BlockStateProperties.WATERLOGGED) && pState.getValue(BlockStateProperties.WATERLOGGED);
    }

    private boolean isFacingDown(Level pLevel, BlockPos pPos) {
        BlockState pState = pLevel.getBlockState(pPos);
        return pState.hasProperty(BlockStateProperties.FACING) && pState.getValue(BlockStateProperties.FACING) == Direction.DOWN;
    }

    private boolean isFacingUp(Level pLevel, BlockPos pPos) {
        BlockState pState = pLevel.getBlockState(pPos);
        return pState.hasProperty(BlockStateProperties.FACING) && pState.getValue(BlockStateProperties.FACING) == Direction.UP;
    }

    private boolean checkCalciteBlock(Level pLevel, BlockPos pPos) {
        return pLevel.getBlockState(pPos.above()).getBlock() == Blocks.CALCITE;
    }

    private boolean checkAragoniteClusterAndBlocks(Level pLevel, BlockPos pPos) {
        int maxEmptyDistance = 4;
        for (int i = 1; i <= maxEmptyDistance; i++) {
            BlockPos iPos = pPos.above(i);
            BlockPos iPosAbove1 = pPos.above(i + 1);
            BlockPos iPosAbove2 = pPos.above(i + 2);
            BlockState iState = pLevel.getBlockState(iPos);
            BlockState iStateAbove1 = pLevel.getBlockState(iPosAbove1);
            BlockState iStateAbove2 = pLevel.getBlockState(iPosAbove2);

            if (!iState.isAir()) {
                return isValidBlock(pLevel, iPos)
                        && iStateAbove1.getBlock() == Blocks.CALCITE
                        && iStateAbove2.getBlock() == Blocks.WATER;
            }
        }
        return false;
    }

    private boolean checkWaterLiquid(Level pLevel, BlockPos pPos) {
        return pLevel.getBlockState(pPos.above().above()).getBlock() == Blocks.WATER;
    }

    private boolean canGrowDown(Level pLevel, BlockPos pPos) {
        return !isWaterlogged(pLevel, pPos) && isFacingDown(pLevel, pPos) && checkCalciteBlock(pLevel, pPos) && checkWaterLiquid(pLevel, pPos);
    }

    private boolean canGrowUp(Level pLevel, BlockPos pPos) {
        return !isWaterlogged(pLevel, pPos) && isFacingUp(pLevel, pPos) && checkAragoniteClusterAndBlocks(pLevel, pPos);
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pRandom.nextFloat() < 0.011377778F && pLevel.getBlockState(pPos).is(InitBlocks.ARAGONITE_SHARD.get())) {
            if (canGrowDown(pLevel, pPos) || canGrowUp(pLevel, pPos)) {
                BlockState shardState = InitBlocks.ARAGONITE_CLUSTER.get().defaultBlockState().setValue(BlockStateProperties.FACING, pState.getValue(BlockStateProperties.FACING)).setValue(BlockStateProperties.WATERLOGGED, pState.getValue(BlockStateProperties.WATERLOGGED));
                pLevel.setBlock(pPos, shardState, 2);
            }
        }
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pLevel instanceof ServerLevel) {
            return;
        }

        if (canGrowDown(pLevel, pPos)) {
            if (pRandom.nextFloat() < 0.1F) {
                Vec3 vec3 = pState.getOffset(pLevel, pPos);
                double x = pPos.getX() + 0.5 + vec3.x;
                double y = pPos.getY() + 0.0625 + vec3.y;
                double z = pPos.getZ() + 0.5 + vec3.z;

                if (pState.getBlock() == InitBlocks.ARAGONITE_SHARD.get()) {
                    y += 0.125;
                }

                pLevel.addParticle(ParticleTypes.DRIPPING_DRIPSTONE_WATER, x, y, z, 0.0, -0.05, 0.0);
            }
        }
    }
}
