package com.tortugolen.ostrea.Triggers;

import com.google.gson.JsonObject;
import com.tortugolen.ostrea.Ostrea;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;

public class AbstractTrigger extends SimpleCriterionTrigger<AbstractTrigger.Instance> {
    public static final ResourceLocation ID = new ResourceLocation(Ostrea.MOD_ID, "abstract");
    public static final AbstractTrigger INSTANCE = new AbstractTrigger();

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    protected Instance createInstance(JsonObject pJson, ContextAwarePredicate pPredicate, DeserializationContext pContext) {
        return new Instance(pPredicate);
    }

    public void trigger(ServerPlayer pPlayer) {
        this.trigger(pPlayer, instance -> true);
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        public Instance(ContextAwarePredicate pPredicate) {
            super(AbstractTrigger.ID, pPredicate);
        }
    }
}
