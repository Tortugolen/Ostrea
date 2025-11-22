package com.tortugolen.ostrea.Features;

import com.mojang.serialization.Codec;
import com.tortugolen.ostrea.Blocks.OysterBlock;
import com.tortugolen.ostrea.BlockEntities.OysterBlockEntity;
import com.tortugolen.ostrea.Init.InitBlocks;
import com.tortugolen.ostrea.Init.InitItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

public class OysterFeature extends Feature<RandomPatchConfiguration> {
    int MIN_OYSTERS_PER_PATCH = 2;
    int MAX_OYSTERS_PER_PATCH = 8;
    int X_OYSTER_DISPERSION = 4;
    int Y_OYSTER_DISPERSION = 4;
    int Z_OYSTER_DISPERSION = 4;

    public OysterFeature(Codec<RandomPatchConfiguration> pCodec) {
        super(pCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<RandomPatchConfiguration> pContext) {
        BlockPos pPos = pContext.origin();
        WorldGenLevel pLevel = pContext.level();
        RandomSource pRandom = pContext.random();

        int seaLevel = pLevel.getSeaLevel();
        int oysterCount = MIN_OYSTERS_PER_PATCH + pRandom.nextInt(MAX_OYSTERS_PER_PATCH - MIN_OYSTERS_PER_PATCH);
        int oysterPlaced = 0;

        for (int i = 0; i < oysterCount * 10; i++) {
            int x = pPos.getX() + pRandom.nextInt(X_OYSTER_DISPERSION * 2 + 1) - X_OYSTER_DISPERSION;
            int y = pPos.getY() + pRandom.nextInt(Y_OYSTER_DISPERSION * 2 + 1) - Y_OYSTER_DISPERSION;
            int z = pPos.getZ() + pRandom.nextInt(Z_OYSTER_DISPERSION * 2 + 1) - Z_OYSTER_DISPERSION;

            y = Mth.clamp(y, pLevel.getMinBuildHeight() + 10, seaLevel - 1);
            BlockPos pPosition = new BlockPos(x, y, z);

            if (pLevel.getBlockState(pPosition).is(Blocks.WATER)) {
                if (pLevel.getBlockState(pPosition.below()).is(BlockTags.BASE_STONE_OVERWORLD) || pLevel.getBlockState(pPosition.below()).is(Blocks.SAND) || pLevel.getBlockState(pPosition.below()).is(Blocks.GRAVEL)) {
                    Direction facing = Direction.Plane.HORIZONTAL.getRandomDirection(pRandom);
                    BlockState oysterState = InitBlocks.OYSTER.get().defaultBlockState().setValue(OysterBlock.FACING, facing).setValue(OysterBlock.WATERLOGGED, true);
                    pLevel.setBlock(pPosition, oysterState, 3);

                    if (pLevel.getBlockEntity(pPosition) instanceof OysterBlockEntity oysterBlockEntity) {
                        oysterBlockEntity.onLoad();

                        boolean j = pRandom.nextBoolean();
                        boolean k = !j && pRandom.nextBoolean();
                        boolean l = !j && !k && pRandom.nextBoolean();

                        if (j) {
                            oysterBlockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> handler.insertItem(0, new ItemStack(InitItems.PEARL.get()), false));
                        } else if (k) {
                            oysterBlockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> handler.insertItem(0, new ItemStack(InitItems.NACRE.get()), false));
                        } else if (l) {
                            oysterBlockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> handler.insertItem(0, new ItemStack(Items.SAND), false));
                        }

                        oysterBlockEntity.setChanged();
                    }
                    oysterPlaced++;
                }
            }
            if (oysterPlaced >= oysterCount) break;
        }
        return oysterPlaced > 0;
    }
}
