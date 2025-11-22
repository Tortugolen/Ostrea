package com.tortugolen.ostrea.Init;

import com.tortugolen.ostrea.Blocks.*;
import com.tortugolen.ostrea.Ostrea;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class InitBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Ostrea.MOD_ID);

    public static final RegistryObject<Block> OYSTER = BLOCKS.register("oyster",
            () -> new OysterBlock(BlockBehaviour.Properties.copy(Blocks.SAND)
                    .instabreak()
                    .explosionResistance(3F)
                    .noOcclusion()
                    .sound(SoundType.CALCITE)));
    public static final RegistryObject<Block> MECHANICAL_OYSTER = BLOCKS.register("mechanical_oyster",
            () -> new MechanicalOysterBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .destroyTime(2.5F)
                    .explosionResistance(6F)
                    .noOcclusion()));
    public static final RegistryObject<Block> CRUSHER = BLOCKS.register("crusher",
            () -> new CrusherBlock(BlockBehaviour.Properties.copy(Blocks.STONECUTTER)));
    public static final RegistryObject<Block> ARAGONITE_CLUSTER = BLOCKS.register("aragonite_cluster",
            () -> new AragoniteClusterBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_ORANGE)
                    .forceSolidOn()
                    .noOcclusion()
                    .randomTicks()
                    .sound(SoundType.AMETHYST_CLUSTER)
                    .strength(1.5F)
                    .pushReaction(PushReaction.DESTROY)
                    .lightLevel((plightLevel) -> {
                return 5;
            })));
    public static final RegistryObject<Block> ARAGONITE_SHARD = BLOCKS.register("aragonite_shard",
            () -> new AragoniteShardBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_ORANGE)
                    .forceSolidOn()
                    .noOcclusion()
                    .randomTicks()
                    .instabreak()
                    .sound(SoundType.AMETHYST_CLUSTER)
                    .pushReaction(PushReaction.DESTROY)
                    .lightLevel((plightLevel) -> {
                return 1;
            })));

    //Inscribed Stones

    public static final RegistryObject<Block> ROUND_INSCRIBED_STONE = BLOCKS.register("round_inscribed_stone",
            () -> new InscribedBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS).requiresCorrectToolForDrops(), "round"));
    public static final RegistryObject<Block> TIP_INSCRIBED_STONE = BLOCKS.register("tip_inscribed_stone",
            () -> new InscribedBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS), "tip"));
    public static final RegistryObject<Block> CALCIUM_CARBONATE_INSCRIBED_STONE = BLOCKS.register("calcium_carbonate_inscribed_stone",
            () -> new InscribedBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS).requiresCorrectToolForDrops(), "calcium_carbonate"));
    public static final RegistryObject<Block> IRON_INSCRIBED_STONE = BLOCKS.register("iron_inscribed_stone",
            () -> new InscribedBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS).requiresCorrectToolForDrops(), "iron"));
    public static final RegistryObject<Block> COPPER_INSCRIBED_STONE = BLOCKS.register("copper_inscribed_stone",
            () -> new InscribedBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS).requiresCorrectToolForDrops(), "copper"));
    public static final RegistryObject<Block> GOLD_INSCRIBED_STONE = BLOCKS.register("gold_inscribed_stone",
            () -> new InscribedBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS).requiresCorrectToolForDrops(), "gold"));

}
