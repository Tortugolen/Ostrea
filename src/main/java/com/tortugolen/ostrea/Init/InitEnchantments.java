package com.tortugolen.ostrea.Init;

import com.tortugolen.ostrea.Enchantments.ReductionEnchantment;
import com.tortugolen.ostrea.Enchantments.SelectiveBlessingEnchantment;
import com.tortugolen.ostrea.Enchantments.ShellOpenerEnchantment;
import com.tortugolen.ostrea.Ostrea;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class InitEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Ostrea.MOD_ID);

    public static final EnchantmentCategory SHELL_OPENER_CATEGORY = EnchantmentCategory.create("shellfish_knife", (item -> item == InitItems.SHELLFISH_KNIFE.get() || item == InitItems.NACRE_DAGGER.get()));
    public static final EnchantmentCategory PEARL_NECKLACE_CATEGORY = EnchantmentCategory.create("pearl_necklace", (item -> item == InitItems.PEARL_NECKLACE.get()));
    public static final EnchantmentCategory PEARL_JEWEL_CATEGORY = EnchantmentCategory.create("pearl_jewel", (item -> item == InitItems.PEARL_RING.get() || item == InitItems.PEARL_NECKLACE.get() || item == InitItems.PEARL_BRACELET.get() || item == InitItems.PEARLED_MIRROR.get()));

    public static RegistryObject<Enchantment> SHELL_OPENER = ENCHANTMENTS.register("shell_opener", () -> new ShellOpenerEnchantment(Enchantment.Rarity.RARE, SHELL_OPENER_CATEGORY, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND));
    public static RegistryObject<Enchantment> SELECTIVE_BLESSING = ENCHANTMENTS.register("selective_blessing", () -> new SelectiveBlessingEnchantment(Enchantment.Rarity.VERY_RARE, PEARL_JEWEL_CATEGORY, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND, EquipmentSlot.HEAD, EquipmentSlot.CHEST));
    public static RegistryObject<Enchantment> REDUCTION = ENCHANTMENTS.register("reduction", () -> new ReductionEnchantment(Enchantment.Rarity.RARE, PEARL_NECKLACE_CATEGORY, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND, EquipmentSlot.HEAD, EquipmentSlot.CHEST));
}
