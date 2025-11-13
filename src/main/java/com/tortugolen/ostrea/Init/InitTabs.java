package com.tortugolen.ostrea.Init;

import com.tortugolen.ostrea.Ostrea;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class InitTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Ostrea.MOD_ID);

    public static final RegistryObject<CreativeModeTab> OSTREA = TABS.register("ostrea",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("tab.ostrea.ostrea"))
                    .icon(() -> new ItemStack(InitItems.PEARL.get()))
                    .displayItems((pParameters, pOutput) -> {

                        pOutput.accept(InitItems.CALCIUM_CARBONATE.get());
                        pOutput.accept(InitItems.NACRE.get());
                        pOutput.accept(InitItems.AMORPHOUS_PEARL.get());
                        pOutput.accept(InitItems.PEARL.get());
                        pOutput.accept(InitItems.PEARL_TIP.get());
                        pOutput.accept(InitItems.IRON_PEARL.get());
                        pOutput.accept(InitItems.IRON_PEARL_TIP.get());
                        pOutput.accept(InitItems.COPPER_PEARL.get());
                        pOutput.accept(InitItems.COPPER_PEARL_TIP.get());
                        pOutput.accept(InitItems.GOLD_PEARL.get());
                        pOutput.accept(InitItems.GOLD_PEARL_TIP.get());
                        pOutput.accept(InitItems.OYSTER_ITEM.get());
                        pOutput.accept(InitItems.MECHANICAL_OYSTER_ITEM.get());
                        pOutput.accept(InitItems.SHELLFISH_KNIFE.get());
                        pOutput.accept(InitItems.COPPER_NUGGET.get());
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

                    })
                    .build());
}