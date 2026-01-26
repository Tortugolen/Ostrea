package com.tortugolen.ostrea.LootModifiers;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class AddItemModifier extends LootModifier {
    private final Item item;
    private final int minCount;
    private final int maxCount;
    private final boolean lootingBonus;
    private final int lootingMinCount;
    private final int lootingMaxCount;

    public static final Supplier<Codec<AddItemModifier>> CODEC = Suppliers.memoize(()
            -> RecordCodecBuilder.create(inst -> codecStart(inst)
            .and(ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(m -> m.item))
            .and(Codec.INT.optionalFieldOf("min_count", 1).forGetter(m -> m.minCount))
            .and(Codec.INT.optionalFieldOf("max_count", 1).forGetter(m -> m.maxCount))
            .and(Codec.BOOL.optionalFieldOf("looting_bonus", false).forGetter(m -> m.lootingBonus))
            .and(Codec.INT.optionalFieldOf("looting_min_count", 0).forGetter(m -> m.lootingMinCount))
            .and(Codec.INT.optionalFieldOf("looting_max_count", 0).forGetter(m -> m.lootingMaxCount))
            .apply(inst, AddItemModifier::new)));

    public AddItemModifier(LootItemCondition[] conditionsIn, Item item, int minCount, int maxCount, boolean lootingBonus, int lootingMinCount, int lootingMaxCount) {
        super(conditionsIn);
        this.item = item;
        this.minCount = minCount;
        this.maxCount = Math.max(minCount, maxCount);
        this.lootingBonus = lootingBonus;
        this.lootingMinCount = lootingMinCount;
        this.lootingMaxCount = lootingMaxCount;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext pContext) {
        var pRandom = pContext.getRandom();
        int count = pRandom.nextInt(this.maxCount - this.minCount + 1) + this.minCount;
        int lootingLevel = pContext.getLootingModifier();

        if (lootingBonus && lootingMaxCount > 0 && lootingLevel > 0) {
            for (int i = 0; i < lootingLevel; i++) {
                count += pRandom.nextInt(lootingMaxCount - lootingMinCount + 1) + lootingMinCount;
            }
        }

        generatedLoot.add(new ItemStack(this.item, count));

        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}