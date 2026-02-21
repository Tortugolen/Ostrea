package com.tortugolen.ostrea.LootModifiers;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class AddEnchantedBookItemModifier extends LootModifier {
    private final Enchantment enchantment;
    private final int minLevel;
    private final int maxLevel;

    public static final Supplier<Codec<AddEnchantedBookItemModifier>> CODEC = Suppliers.memoize(()
            -> RecordCodecBuilder.create(inst -> codecStart(inst)
            .and(ForgeRegistries.ENCHANTMENTS.getCodec().fieldOf("enchantment").forGetter(m -> m.enchantment))
            .and(Codec.INT.optionalFieldOf("min_level", 1).forGetter(m -> m.minLevel))
            .and(Codec.INT.optionalFieldOf("max_level", 1).forGetter(m -> m.maxLevel))
            .apply(inst, AddEnchantedBookItemModifier::new)));

    public AddEnchantedBookItemModifier(LootItemCondition[] conditions, Enchantment enchantment, int minLevel, int maxLevel) {
        super(conditions);
        this.enchantment = enchantment;
        this.minLevel = minLevel;
        this.maxLevel = Math.max(minLevel, maxLevel);
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext pContext) {
        int level = pContext.getRandom().nextInt(this.maxLevel - this.minLevel + 1) + this.minLevel;
        generatedLoot.add(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, level)));
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}