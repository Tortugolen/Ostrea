package com.tortugolen.ostrea.Blocks;

import com.tortugolen.ostrea.Init.InitItems;
import com.tortugolen.ostrea.Init.InitTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.registries.ForgeRegistries;

public class InscribedBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty INKY = BooleanProperty.create("inky");
    private final String glyph;

    public InscribedBlock(Properties pProperties, String glyph) {
        super(pProperties);
        this.glyph = glyph;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite()).setValue(INKY, false);
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
        pBuilder.add(FACING, INKY);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        ItemStack itemStack = pPlayer.getItemInHand(pHand);
            if (!pLevel.isClientSide()) {
                if (!pState.getValue(INKY)) {
                    if (itemStack.is(Items.INK_SAC) || itemStack.is(Items.BLACK_DYE)) {
                        if (!pPlayer.isCreative()) {
                            itemStack.shrink(1);
                        }
                        pLevel.setBlock(pPos, pState.setValue(INKY, true), 3);
                        pLevel.playSound(null, pPos, SoundEvents.SLIME_SQUISH, SoundSource.BLOCKS, 1F, 1F);
                        return InteractionResult.SUCCESS;
                    }
                } if (pState.getValue(INKY)) {
                    if (itemStack.is(InitItems.PARCHMENT.get())) {
                        if (!pPlayer.isCreative()) {
                            itemStack.shrink(1);
                        }
                        pLevel.setBlock(pPos, pState.setValue(INKY, false), 3);
                        pLevel.playSound(null, pPos, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, SoundSource.BLOCKS, 1F, 1F);
                        Item glyphItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation("ostrea:" + glyph + "_mom"));
                        if (glyphItem != null) {
                            pPlayer.addItem(new ItemStack(glyphItem));
                        }
                        if (pPlayer instanceof ServerPlayer pServerPlayer) {
                            InitTriggers.ABSTRACT6.trigger(pServerPlayer);
                        }
                        return InteractionResult.SUCCESS;
                        }
                    if (itemStack.is(Items.BRUSH)) {
                        if (!pPlayer.isCreative()) {
                            itemStack.hurtAndBreak(1, pPlayer, player -> player.broadcastBreakEvent(pHand));
                        }
                        pLevel.setBlock(pPos, pState.setValue(INKY, false), 3);
                        pLevel.playSound(null, pPos, SoundEvents.BRUSH_GENERIC, SoundSource.BLOCKS, 1F, 1F);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        return InteractionResult.PASS;
    }
}
