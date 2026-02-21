package com.tortugolen.ostrea.Base;

import com.tortugolen.ostrea.Enchantments.PassiveAnnulmentEnchantment;
import com.tortugolen.ostrea.Enchantments.ReductionEnchantment;
import com.tortugolen.ostrea.Enchantments.SelectiveBlessingEnchantment;
import com.tortugolen.ostrea.Enchantments.ShellOpenerEnchantment;
import com.tortugolen.ostrea.Init.InitItems;
import com.tortugolen.ostrea.Ostrea;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = Ostrea.MOD_ID)
public class AnvilEnchantmentsEvent {
    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();

        if (!right.is(Items.ENCHANTED_BOOK)) return;

        ListTag enchantments = EnchantedBookItem.getEnchantments(right);
        for (int i = 0; i < enchantments.size(); i++) {
            CompoundTag pTag = enchantments.getCompound(i);
            Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(pTag.getString("id")));

            if (enchantment instanceof ShellOpenerEnchantment) {
                if (!left.is(InitItems.SHELLFISH_KNIFE.get()) && !left.is(InitItems.NACRE_DAGGER.get())) {
                    event.setOutput(ItemStack.EMPTY);
                    event.setCanceled(true);
                    return;
                }
            }

            if (enchantment instanceof SelectiveBlessingEnchantment) {
                if (!left.is(InitItems.PEARL_NECKLACE.get()) && !left.is(InitItems.PEARL_RING.get()) && !left.is(InitItems.PEARL_BRACELET.get()) && !left.is(InitItems.PEARLED_MIRROR.get())) {
                    event.setOutput(ItemStack.EMPTY);
                    event.setCanceled(true);
                    return;
                }
            }

            if (enchantment instanceof ReductionEnchantment || enchantment instanceof PassiveAnnulmentEnchantment) {
                if (!left.is(InitItems.PEARL_NECKLACE.get())) {
                    event.setOutput(ItemStack.EMPTY);
                    event.setCanceled(true);
                    return;
                }
            }
        }
    }
}