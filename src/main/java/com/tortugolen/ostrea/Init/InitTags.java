package com.tortugolen.ostrea.Init;

import com.tortugolen.ostrea.Ostrea;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class InitTags {
    public static class Items {
        public static final TagKey<Item> COPPER_NUGGETS = tag("nuggets/copper");
        public static final TagKey<Item> SHELLS = tag("shells");

        //MOM's

        public static final TagKey<Item> ROUND_MOM = tag("mom/round_mom");
        public static final TagKey<Item> TIP_MOM = tag("mom/tip_mom");
        public static final TagKey<Item> CALCIUM_CARBONATE_MOM = tag("mom/calcium_carbonate_mom");
        public static final TagKey<Item> IRON_MOM = tag("mom/iron_mom");
        public static final TagKey<Item> COPPER_MOM = tag("mom/copper_mom");
        public static final TagKey<Item> GOLD_MOM = tag("mom/gold_mom");
    }

    private static TagKey<Item> tag(String name) {
        return ItemTags.create(new ResourceLocation(Ostrea.MOD_ID, name));
    }
}
