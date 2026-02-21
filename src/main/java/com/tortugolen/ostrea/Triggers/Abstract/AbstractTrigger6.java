package com.tortugolen.ostrea.Triggers.Abstract;

import com.google.gson.JsonObject;
import com.tortugolen.ostrea.Ostrea;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class AbstractTrigger6 extends SimpleCriterionTrigger<AbstractTrigger6.Instance> {
    public static final ResourceLocation ID = new ResourceLocation(Ostrea.MOD_ID, "abstract6");
    public static final AbstractTrigger6 INSTANCE = new AbstractTrigger6();

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
            super(AbstractTrigger6.ID, pPredicate);
        }
    }
}
