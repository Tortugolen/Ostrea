package com.tortugolen.ostrea.Init;

import com.tortugolen.ostrea.Ostrea;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class InitTags {
    public static class Items {
        private static TagKey<Item> ItemTag(String id) {
            return ItemTags.create(new ResourceLocation(Ostrea.MOD_ID, id));
        }

        public static final TagKey<Item> OYSTER_OPENER = ItemTag("oyster_opener");
        public static final TagKey<Item> EFFECT_STATUSFERS = ItemTag("effect_statusfers");
    }
}
