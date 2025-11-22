package com.tortugolen.ostrea.Features;

import com.mojang.serialization.Codec;
import com.tortugolen.ostrea.Init.InitBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;

public class AragoniteFeature extends Feature<RandomPatchConfiguration> {
    int MIN_SHARDS_PER_PATCH = 4;
    int MAX_SHARDS_PER_PATCH = 8;
    int MIN_CLUSTERS_PER_PATCH = 4;
    int MAX_CLUSTERS_PER_PATCH = 8;
    int X_ARAGONITE_DISPERSION = 8;
    int Y_ARAGONITE_DISPERSION = 8;
    int Z_ARAGONITE_DISPERSION = 8;
    int MAX_EMPTY_DISTANCE = 8;

    public AragoniteFeature(Codec<RandomPatchConfiguration> pCodec) {
        super(pCodec);
    }

    private Direction findSupport(WorldGenLevel pLevel, BlockPos pPos) {
        for (Direction pDirection : Direction.values()) {
            BlockPos adjacentBlock = pPos.relative(pDirection);
            BlockState adjacentState = pLevel.getBlockState(adjacentBlock);
            if (adjacentState.is(BlockTags.BASE_STONE_OVERWORLD) || adjacentState.is(Blocks.CALCITE)) {
                return pDirection;
            }
        }
        return null;
    }

    @Override
    public boolean place(FeaturePlaceContext<RandomPatchConfiguration> pContext) {
        BlockPos pPos = pContext.origin();
        WorldGenLevel pLevel = pContext.level();
        RandomSource pRandom = pContext.random();

        int shardsCount = MIN_SHARDS_PER_PATCH + pRandom.nextInt(MAX_SHARDS_PER_PATCH - MIN_SHARDS_PER_PATCH + 1);
        int clustersCount = MIN_CLUSTERS_PER_PATCH + pRandom.nextInt(MAX_CLUSTERS_PER_PATCH - MIN_CLUSTERS_PER_PATCH + 1);
        int aragoniteCount = shardsCount + clustersCount;
        int shardsPlaced = 0;
        int clustersPlaced = 0;
        int aragonitePlaced = 0;

        for (int i = 0; i < aragoniteCount * 10; i++) {
            int x = pPos.getX() + pRandom.nextInt(X_ARAGONITE_DISPERSION * 2 + 1) - X_ARAGONITE_DISPERSION;
            int y = pPos.getY() + pRandom.nextInt(Y_ARAGONITE_DISPERSION * 2 + 1) - Y_ARAGONITE_DISPERSION;
            int z = pPos.getZ() + pRandom.nextInt(Z_ARAGONITE_DISPERSION * 2 + 1) - Z_ARAGONITE_DISPERSION;

            y = Mth.clamp(y, pLevel.getMinBuildHeight(), 50);

            BlockPos newPos = new BlockPos(x, y, z);
            Direction supportFace = findSupport(pLevel, newPos);

            if (supportFace == null) continue;

            BlockState supportState = pLevel.getBlockState(newPos.relative(supportFace));

            if (!pLevel.getBlockState(newPos).isAir()) continue;
            if (!supportState.is(BlockTags.BASE_STONE_OVERWORLD) && !supportState.is(Blocks.CALCITE)) continue;

            boolean placeShard = pRandom.nextBoolean();

            BlockState aragoniteState;
            if (placeShard) {
                aragoniteState = InitBlocks.ARAGONITE_SHARD.get().defaultBlockState().setValue(BlockStateProperties.FACING, supportFace.getOpposite()).setValue(BlockStateProperties.WATERLOGGED, false);
            } else {
                aragoniteState = InitBlocks.ARAGONITE_CLUSTER.get().defaultBlockState().setValue(BlockStateProperties.FACING, supportFace.getOpposite()).setValue(BlockStateProperties.WATERLOGGED, false);
            }

            pLevel.setBlock(newPos, aragoniteState, 2);

            if (aragoniteState.is(InitBlocks.ARAGONITE_SHARD.get())) shardsPlaced++;
            else clustersPlaced++;

            aragonitePlaced = shardsPlaced + clustersPlaced;

            for (int j = 1; j <= MAX_EMPTY_DISTANCE; j++) {

                BlockPos jPos;
                switch (supportFace) {
                    case UP: jPos = newPos.above(j); break;
                    case DOWN: jPos = newPos.below(j); break;
                    case NORTH: jPos = newPos.north(j); break;
                    case SOUTH: jPos = newPos.south(j); break;
                    case EAST: jPos = newPos.east(j); break;
                    case WEST: jPos = newPos.west(j); break;
                    default: jPos = newPos; break;
                }

                BlockState jState = pLevel.getBlockState(jPos);

                if (jState.is(BlockTags.BASE_STONE_OVERWORLD) || jState.is(Blocks.CALCITE)) {

                    BlockPos placePos = jPos.relative(supportFace.getOpposite());

                    if (!pLevel.getBlockState(placePos).isAir()) {
                        break;
                    }

                    boolean placeOppositeShard = pRandom.nextBoolean();

                    BlockState aragoniteOppositeState;
                    if (placeOppositeShard) {
                        aragoniteOppositeState = InitBlocks.ARAGONITE_SHARD.get().defaultBlockState().setValue(BlockStateProperties.FACING, supportFace).setValue(BlockStateProperties.WATERLOGGED, false);
                    } else {
                        aragoniteOppositeState = InitBlocks.ARAGONITE_CLUSTER.get().defaultBlockState().setValue(BlockStateProperties.FACING, supportFace).setValue(BlockStateProperties.WATERLOGGED, false);
                    }

                    if (jState.is(Blocks.CALCITE) || pRandom.nextBoolean()) {
                        pLevel.setBlock(placePos, aragoniteOppositeState, 2);

                        if (aragoniteOppositeState.is(InitBlocks.ARAGONITE_SHARD.get())) shardsPlaced++;
                        else clustersPlaced++;

                        aragonitePlaced = shardsPlaced + clustersPlaced;
                    }
                    break;
                }
            }

            if (aragonitePlaced >= aragoniteCount) break;
        }

        return aragonitePlaced > 0;
    }
}
