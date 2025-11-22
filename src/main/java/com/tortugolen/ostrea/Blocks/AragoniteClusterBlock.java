package com.tortugolen.ostrea.Blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AragoniteClusterBlock extends AragoniteShardBlock {
    public AragoniteClusterBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(BlockStateProperties.FACING);
        switch (facing) {
            case UP:
                return Block.box(5, 0, 5, 11, 14, 11);
            case DOWN:
                return Block.box(5, 2, 5, 11, 16, 11);
            case NORTH:
                return Block.box(5, 5, 2, 11, 11, 16);
            case SOUTH:
                return Block.box(5, 5, 0, 11, 11, 14);
            case WEST:
                return Block.box(2, 5, 5, 16, 11, 11);
            case EAST:
                return Block.box(0, 5, 5, 14, 11, 11);
            default:
                return null;
        }
    }
}
