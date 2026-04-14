package com.autodeposit;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import java.util.HashSet;
import java.util.Set;

public class DepositHelper {
    private static Set<Item> targetItems = new HashSet<>();

    public static void updateTargetItems(ModConfig config) {
        targetItems.clear();
        for (String itemId : config.autoDepositItems) {
            Identifier id = Identifier.tryParse(itemId);
            if (id != null && Registries.ITEM.containsId(id)) {
                targetItems.add(Registries.ITEM.get(id));
            }
        }
    }

    public static void tryDeposit(PlayerInventory playerInventory, Inventory chestInventory) {
        for (int i = 0; i < playerInventory.size(); i++) {
            ItemStack stack = playerInventory.getStack(i);
            if (!stack.isEmpty() && targetItems.contains(stack.getItem())) {
                ItemStack remaining = tryAddToChest(chestInventory, stack);
                playerInventory.setStack(i, remaining);
                if (remaining.isEmpty()) break;
            }
        }
    }

    private static ItemStack tryAddToChest(Inventory chest, ItemStack stack) {
        int remaining = stack.getCount();
        for (int slot = 0; slot < chest.size(); slot++) {
            ItemStack chestStack = chest.getStack(slot);
            if (chestStack.isEmpty()) {
                int transfer = Math.min(remaining, chest.getMaxCountPerStack());
                chest.setStack(slot, stack.copyWithCount(transfer));
                remaining -= transfer;
                if (remaining == 0) return ItemStack.EMPTY;
            } else if (ItemStack.canCombine(chestStack, stack) && chestStack.getCount() < chestStack.getMaxCount()) {
                int space = chestStack.getMaxCount() - chestStack.getCount();
                int transfer = Math.min(remaining, space);
                chestStack.increment(transfer);
                remaining -= transfer;
                if (remaining == 0) return ItemStack.EMPTY;
            }
        }
        return stack.copyWithCount(remaining);
    }
             }
