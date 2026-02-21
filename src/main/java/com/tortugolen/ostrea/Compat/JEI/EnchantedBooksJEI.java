package com.tortugolen.ostrea.Compat.JEI;

import com.tortugolen.ostrea.Init.InitEnchantments;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

import java.util.ArrayList;
import java.util.List;

public class EnchantedBooksJEI {
    public static List<ItemStack> getAllEnchantedBooks() {
        List<ItemStack> books = new ArrayList<>();

        addBook(books, InitEnchantments.SHELL_OPENER.get(), 1);
        addBook(books, InitEnchantments.SELECTIVE_BLESSING.get(), 1);
        addBook(books, InitEnchantments.REDUCTION.get(), 1);
        addBook(books, InitEnchantments.REDUCTION.get(), 2);
        addBook(books, InitEnchantments.REDUCTION.get(), 3);
        //addBook(books, InitEnchantments.PASSIVE_ANNULMENT.get(), 1);
        //addBook(books, InitEnchantments.PASSIVE_ANNULMENT.get(), 2);
        //addBook(books, InitEnchantments.PASSIVE_ANNULMENT.get(), 3);
        //addBook(books, InitEnchantments.PASSIVE_ANNULMENT.get(), 4);
        //addBook(books, InitEnchantments.PASSIVE_ANNULMENT.get(), 5);

        return books;
    }

    private static void addBook(List<ItemStack> list, Enchantment enchantment, int level) {
        ItemStack book = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, level));
        list.add(book);
    }
}
