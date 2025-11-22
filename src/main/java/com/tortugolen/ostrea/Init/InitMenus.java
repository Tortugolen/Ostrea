package com.tortugolen.ostrea.Init;

import com.tortugolen.ostrea.GUIs.Menus.CrusherMenu;
import com.tortugolen.ostrea.GUIs.Menus.MechanicalOysterMenu;
import com.tortugolen.ostrea.GUIs.Menus.OysterMenu;
import com.tortugolen.ostrea.Ostrea;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class InitMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Ostrea.MOD_ID);

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static final RegistryObject<MenuType<OysterMenu>> OYSTER_MENU = registerMenuType("oyster_menu", OysterMenu::new);
    public static final RegistryObject<MenuType<MechanicalOysterMenu>> MECHANICAL_OYSTER_MENU = registerMenuType("mechanical_oyster_menu", MechanicalOysterMenu::new);
    public static final RegistryObject<MenuType<CrusherMenu>> CRUSHER_MENU = registerMenuType("crusher_menu", CrusherMenu::new);

}
