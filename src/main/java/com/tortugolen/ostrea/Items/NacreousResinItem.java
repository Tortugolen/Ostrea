package com.tortugolen.ostrea.Items;

import com.tortugolen.ostrea.Init.InitTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class NacreousResinItem extends Item {
        private static final Map<Block, Block> CRACKED_BLOCKS = new HashMap<>();

        static {
            CRACKED_BLOCKS.put(Blocks.CRACKED_STONE_BRICKS, Blocks.STONE_BRICKS);
            CRACKED_BLOCKS.put(Blocks.CRACKED_DEEPSLATE_BRICKS, Blocks.DEEPSLATE_BRICKS);
            CRACKED_BLOCKS.put(Blocks.CRACKED_DEEPSLATE_TILES, Blocks.DEEPSLATE_TILES);
            CRACKED_BLOCKS.put(Blocks.CRACKED_NETHER_BRICKS, Blocks.NETHER_BRICKS);
            CRACKED_BLOCKS.put(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS, Blocks.POLISHED_BLACKSTONE_BRICKS);
        }

    public NacreousResinItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level pLevel = pContext.getLevel();
        Player pPlayer = pContext.getPlayer();
        ItemStack pStack = pContext.getItemInHand();
        BlockPos pPos = pContext.getClickedPos();
        BlockState pState = pLevel.getBlockState(pPos);
        Block pCrackedBlock = pState.getBlock();
        Block pRepairedBlock = CRACKED_BLOCKS.get(pCrackedBlock);

        boolean repaired = false;

        if (!pLevel.isClientSide) {
            if (CRACKED_BLOCKS.containsKey(pCrackedBlock)) {
                if (pLevel instanceof ServerLevel pServerLevel && pPlayer instanceof ServerPlayer pServerPlayer) {
                    InitTriggers.ITEM_USED_ON_BLOCK.trigger(pServerPlayer, pStack, pServerLevel, pPos);
                }

                pLevel.setBlock(pPos, pRepairedBlock.defaultBlockState(), 3);
                pLevel.playSound(null, pPos.getX(), pPos.getY(), pPos.getZ(), SoundEvents.SMALL_AMETHYST_BUD_PLACE, SoundSource.BLOCKS, 1F, 1F);
                pLevel.playSound(null, pPos.getX(), pPos.getY(), pPos.getZ(), SoundEvents.HONEYCOMB_WAX_ON, SoundSource.BLOCKS, 1F, 1F);

                if (pLevel instanceof ServerLevel pServerLevel) {
                    pServerLevel.sendParticles(ParticleTypes.WAX_ON, pPos.getX() + 0.5D, pPos.getY() + 0.5D, pPos.getZ() + 0.5D, 20, 0.375D, 0.375D, 0.375D, 0.5D);
                }

                if (!pPlayer.isCreative()) {
                    pStack.shrink(1);
                }

                repaired = true;
            }
        }

        if (!repaired) {
            return InteractionResult.FAIL;
        }

        return InteractionResult.SUCCESS;
    }
}
