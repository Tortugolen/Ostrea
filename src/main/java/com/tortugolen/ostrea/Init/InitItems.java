package com.tortugolen.ostrea.Init;

import com.tortugolen.ostrea.Items.PearlTips.CopperPearlTipItem;
import com.tortugolen.ostrea.Items.PearlTips.GoldPearlTipItem;
import com.tortugolen.ostrea.Items.PearlTips.IronPearlTipItem;
import com.tortugolen.ostrea.Items.PearlTips.PearlTipItem;
import com.tortugolen.ostrea.Items.PurityTooltipItem;
import com.tortugolen.ostrea.Items.TooltipItem;
import com.tortugolen.ostrea.Ostrea;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class InitItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Ostrea.MOD_ID);

    public static final RegistryObject<Item> NACRE = ITEMS.register("nacre", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PEARL = ITEMS.register("pearl", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> IRON_PEARL = ITEMS.register("iron_pearl", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COPPER_PEARL = ITEMS.register("copper_pearl", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GOLD_PEARL = ITEMS.register("gold_pearl", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CALCIUM_CARBONATE = ITEMS.register("calcium_carbonate", () -> new PurityTooltipItem(new Item.Properties(), "§e100§r"));
    public static final RegistryObject<Item> PEARL_TIP = ITEMS.register("pearl_tip", () -> new PearlTipItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> IRON_PEARL_TIP = ITEMS.register("iron_pearl_tip", () -> new IronPearlTipItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> COPPER_PEARL_TIP = ITEMS.register("copper_pearl_tip", () -> new CopperPearlTipItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> GOLD_PEARL_TIP = ITEMS.register("gold_pearl_tip", () -> new GoldPearlTipItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> SHELLFISH_KNIFE = ITEMS.register("shellfish_knife", () -> new Item(new Item.Properties().durability(64)));
    public static final RegistryObject<Item> COPPER_NUGGET = ITEMS.register("copper_nugget", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> AMORPHOUS_PEARL = ITEMS.register("amorphous_pearl", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PARCHMENT = ITEMS.register("parchment", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> IMPURITIES = ITEMS.register("impurities", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ARAGONITE_POWDER = ITEMS.register("aragonite_powder", () -> new PurityTooltipItem(new Item.Properties(), "§e50§r"));

    //MechanicalOysterModifiers

    public static final RegistryObject<Item> ROUND_AMOM = ITEMS.register("round_amom", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> TIP_AMOM = ITEMS.register("tip_amom", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> CALCIUM_CARBONATE_AMOM = ITEMS.register("calcium_carbonate_amom", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> IRON_AMOM = ITEMS.register("iron_amom", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> COPPER_AMOM = ITEMS.register("copper_amom", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> GOLD_AMOM = ITEMS.register("gold_amom", () -> new Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> ROUND_MOM = ITEMS.register("round_mom", () -> new TooltipItem(new Item.Properties().stacksTo(1), "mom.tooltip.ostrea.round"));
    public static final RegistryObject<Item> TIP_MOM = ITEMS.register("tip_mom", () -> new TooltipItem(new Item.Properties().stacksTo(1), "mom.tooltip.ostrea.tip"));
    public static final RegistryObject<Item> CALCIUM_CARBONATE_MOM = ITEMS.register("calcium_carbonate_mom", () -> new TooltipItem(new Item.Properties().stacksTo(1), "mom.tooltip.ostrea.calcium_carbonate"));
    public static final RegistryObject<Item> IRON_MOM = ITEMS.register("iron_mom", () -> new TooltipItem(new Item.Properties().stacksTo(1), "mom.tooltip.ostrea.iron"));
    public static final RegistryObject<Item> COPPER_MOM = ITEMS.register("copper_mom", () -> new TooltipItem(new Item.Properties().stacksTo(1), "mom.tooltip.ostrea.copper"));
    public static final RegistryObject<Item> GOLD_MOM = ITEMS.register("gold_mom", () -> new TooltipItem(new Item.Properties().stacksTo(1), "mom.tooltip.ostrea.gold"));

    //BlockItems

    public static final RegistryObject<Item> OYSTER_ITEM = ITEMS.register("oyster", () -> new BlockItem(InitBlocks.OYSTER.get(), new Item.Properties()));
    public static final RegistryObject<Item> MECHANICAL_OYSTER_ITEM = ITEMS.register("mechanical_oyster", () -> new BlockItem(InitBlocks.MECHANICAL_OYSTER.get(), new Item.Properties()));
    public static final RegistryObject<Item> ROUND_INSCRIBED_STONE_ITEM = ITEMS.register("round_inscribed_stone", () -> new BlockItem(InitBlocks.ROUND_INSCRIBED_STONE.get(), new Item.Properties()));
    public static final RegistryObject<Item> TIP_INSCRIBED_STONE_ITEM = ITEMS.register("tip_inscribed_stone", () -> new BlockItem(InitBlocks.TIP_INSCRIBED_STONE.get(), new Item.Properties()));
    public static final RegistryObject<Item> CALCIUM_CARBONATE_INSCRIBED_STONE_ITEM = ITEMS.register("calcium_carbonate_inscribed_stone", () -> new BlockItem(InitBlocks.CALCIUM_CARBONATE_INSCRIBED_STONE.get(), new Item.Properties()));
    public static final RegistryObject<Item> IRON_INSCRIBED_STONE_ITEM = ITEMS.register("iron_inscribed_stone", () -> new BlockItem(InitBlocks.IRON_INSCRIBED_STONE.get(), new Item.Properties()));
    public static final RegistryObject<Item> COPPER_INSCRIBED_STONE_ITEM = ITEMS.register("copper_inscribed_stone", () -> new BlockItem(InitBlocks.COPPER_INSCRIBED_STONE.get(), new Item.Properties()));
    public static final RegistryObject<Item> GOLD_INSCRIBED_STONE_ITEM = ITEMS.register("gold_inscribed_stone", () -> new BlockItem(InitBlocks.GOLD_INSCRIBED_STONE.get(), new Item.Properties()));
    public static final RegistryObject<Item> CRUSHER_ITEM = ITEMS.register("crusher", () -> new BlockItem(InitBlocks.CRUSHER.get(), new Item.Properties()));
    public static final RegistryObject<Item> ARAGONITE_CLUSTER_ITEM = ITEMS.register("aragonite_cluster", () -> new BlockItem(InitBlocks.ARAGONITE_CLUSTER.get(), new Item.Properties()));
    public static final RegistryObject<Item> ARAGONITE_SHARD = ITEMS.register("aragonite_shard", () -> new BlockItem(InitBlocks.ARAGONITE_SHARD.get(), new Item.Properties()));
}
