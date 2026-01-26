package com.tortugolen.ostrea.Init;

import com.tortugolen.ostrea.Base.EffectStatusfers;
import com.tortugolen.ostrea.Items.StatusferItem;
import com.tortugolen.ostrea.Ostrea;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class InitTabs implements EffectStatusfers {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Ostrea.MOD_ID);

    public static final RegistryObject<CreativeModeTab> OSTREA = TABS.register("ostrea",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("tab.ostrea.ostrea"))
                    .icon(() -> new ItemStack(InitItems.PEARL.get()))
                    .displayItems((pParameters, pOutput) -> {

                        pOutput.accept(InitItems.CALCIUM_CARBONATE.get());
                        pOutput.accept(InitItems.NACRE.get());
                        pOutput.accept(InitItems.AMORPHOUS_PEARL.get());
                        pOutput.accept(InitItems.RECEPTACLE_PEARL.get());
                        pOutput.accept(InitItems.PEARL.get());
                        pOutput.accept(InitItems.IRON_PEARL.get());
                        pOutput.accept(InitItems.COPPER_PEARL.get());
                        pOutput.accept(InitItems.GOLD_PEARL.get());
                        pOutput.accept(InitItems.STATUSFER.get());
                        pOutput.accept(InitItems.PEARL_TIP.get());
                        pOutput.accept(InitItems.IRON_PEARL_TIP.get());
                        pOutput.accept(InitItems.COPPER_PEARL_TIP.get());
                        pOutput.accept(InitItems.GOLD_PEARL_TIP.get());
                        pOutput.accept(InitItems.OYSTER_ITEM.get());
                        pOutput.accept(InitItems.MECHANICAL_OYSTER_ITEM.get());

                        pOutput.accept(InitItems.ARAGONITE_CLUSTER_ITEM.get());
                        pOutput.accept(InitItems.ARAGONITE_SHARD_ITEM.get());
                        pOutput.accept(InitItems.ARAGONITE_POWDER.get());

                        pOutput.accept(InitItems.GOLD_RING.get());
                        pOutput.accept(InitItems.GOLD_NECKLACE.get());
                        pOutput.accept(InitItems.GOLD_BRACELET.get());
                        pOutput.accept(InitItems.AMETHYST_MIRROR.get());

                        pOutput.accept(InitItems.PEARL_RING.get());
                        pOutput.accept(InitItems.PEARL_NECKLACE.get());
                        pOutput.accept(InitItems.PEARL_BRACELET.get());
                        pOutput.accept(InitItems.PEARLED_MIRROR.get());

                        pOutput.accept(InitItems.GEM_POLISHING_SMITHING_TEMPLATE.get());
                        pOutput.accept(InitItems.SHELLFISH_KNIFE.get());
                        pOutput.accept(InitItems.NACRE_DAGGER.get());
                        pOutput.accept(InitItems.AXOLOTL_MEMBRANE.get());
                        pOutput.accept(InitItems.AXOLOTL_HELMET.get());
                        pOutput.accept(InitItems.IMPROVED_AXOLOTL_HELMET.get());
                        pOutput.accept(InitItems.NACREOUS_RESIN.get());
                        pOutput.accept(InitItems.COPPER_NUGGET.get());
                        pOutput.accept(InitItems.IMPURITIES.get());

                        pOutput.accept(InitItems.PARCHMENT.get());
                        pOutput.accept(InitItems.ROUND_MOM.get());
                        pOutput.accept(InitItems.TIP_MOM.get());
                        pOutput.accept(InitItems.CALCIUM_CARBONATE_MOM.get());
                        pOutput.accept(InitItems.IRON_MOM.get());
                        pOutput.accept(InitItems.COPPER_MOM.get());
                        pOutput.accept(InitItems.GOLD_MOM.get());

                        pOutput.accept(InitItems.ROUND_AMOM.get());
                        pOutput.accept(InitItems.TIP_AMOM.get());
                        pOutput.accept(InitItems.CALCIUM_CARBONATE_AMOM.get());
                        pOutput.accept(InitItems.IRON_AMOM.get());
                        pOutput.accept(InitItems.COPPER_AMOM.get());
                        pOutput.accept(InitItems.GOLD_AMOM.get());

                        pOutput.accept(InitItems.ROUND_INSCRIBED_STONE_ITEM.get());
                        pOutput.accept(InitItems.TIP_INSCRIBED_STONE_ITEM.get());
                        pOutput.accept(InitItems.CALCIUM_CARBONATE_INSCRIBED_STONE_ITEM.get());
                        pOutput.accept(InitItems.IRON_INSCRIBED_STONE_ITEM.get());
                        pOutput.accept(InitItems.COPPER_INSCRIBED_STONE_ITEM.get());
                        pOutput.accept(InitItems.GOLD_INSCRIBED_STONE_ITEM.get());

                        pOutput.accept(InitItems.NACRE_BLOCK_ITEM.get());
                        pOutput.accept(InitItems.NACRE_SLAB_ITEM.get());
                        pOutput.accept(InitItems.NACRE_STAIRS_ITEM.get());
                        pOutput.accept(InitItems.NACRE_BRICKS_ITEM.get());
                        pOutput.accept(InitItems.SMOOTH_NACRE_BLOCK_ITEM.get());
                        //pOutput.accept(InitItems.JEWELRY_TABLE_ITEM.get());
                        pOutput.accept(InitItems.NACRE_ALTAR_ITEM.get());
                        pOutput.accept(InitItems.DEEPSLATE_ALTAR_ITEM.get());
                        pOutput.accept(InitItems.CRUSHER_ITEM.get());

                        EffectStatusfers.addRegenerationStatusfer(pOutput, "minecraft:regeneration");

                        pOutput.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(InitEnchantments.SHELL_OPENER.get(), 1)));
                        pOutput.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(InitEnchantments.SELECTIVE_BLESSING.get(), 1)));
                        pOutput.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(InitEnchantments.REDUCTION.get(), 1)));
                        pOutput.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(InitEnchantments.REDUCTION.get(), 2)));
                        pOutput.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(InitEnchantments.REDUCTION.get(), 3)));
                        pOutput.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(InitEnchantments.PASSIVE_ANNULMENT.get(), 1)));
                        pOutput.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(InitEnchantments.PASSIVE_ANNULMENT.get(), 2)));
                        pOutput.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(InitEnchantments.PASSIVE_ANNULMENT.get(), 3)));
                        pOutput.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(InitEnchantments.PASSIVE_ANNULMENT.get(), 4)));
                        pOutput.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(InitEnchantments.PASSIVE_ANNULMENT.get(), 5)));

                    })
                    .build());
}