package com.tortugolen.ostrea.Triggers;

import com.google.gson.JsonObject;
import com.tortugolen.ostrea.Ostrea;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class ItemUsedOnBlockTrigger extends SimpleCriterionTrigger<ItemUsedOnBlockTrigger.TriggerInstance> {
    public static final ResourceLocation ID = new ResourceLocation(Ostrea.MOD_ID, "item_used_on_block");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    protected TriggerInstance createInstance(JsonObject json, ContextAwarePredicate pPlayer, DeserializationContext pContext) {
        ItemPredicate item = ItemPredicate.fromJson(json.get("item"));
        BlockPredicate block = BlockPredicate.fromJson(json.get("block"));

        return new TriggerInstance(pPlayer, item, block);
    }

    public void trigger(ServerPlayer pPlayer, ItemStack pStack, ServerLevel pLevel, BlockPos pPos) {
        this.trigger(pPlayer, instance -> instance.matches(pStack, pLevel, pPos));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final ItemPredicate item;
        private final BlockPredicate block;

        public TriggerInstance(ContextAwarePredicate pPlayer, ItemPredicate item, BlockPredicate block) {
            super(ID, pPlayer);
            this.item = item;
            this.block = block;
        }

        public boolean matches(ItemStack stack, ServerLevel pLevel, BlockPos pPos) {
            return item.matches(stack) && block.matches(pLevel, pPos);
        }
    }
}
