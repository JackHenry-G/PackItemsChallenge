package com.jackgoggin.packinghelper.Service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.thymeleaf.util.MapUtils;

import com.jackgoggin.packinghelper.Model.PackSize;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ItemPackerService {

    private static final PackSize SMALLEST_PACK_SIZE = PackSize.SMALLEST;
    private static final PackSize LARGEST_PACK_SIZE = PackSize.LARGEST;

    /**
     * Packages an order of X amount of items in an optimal distribution of packs.
     * 
     * @param amountOfItemsOrdered The total number of items to be packaged
     * @return A new map containing pack sizes and quantities
     */
    public Map<PackSize, Integer> packageItems(int amountOfItemsOrdered) {
        if (amountOfItemsOrdered <= 0) {
            String errorMessage = "Order amount [" + amountOfItemsOrdered + "] must be above zero!";
            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        Map<PackSize, Integer> packs = new HashMap<>();
        calculatePacks(amountOfItemsOrdered, packs);
        Map<PackSize, Integer> packagedOrder = minimizePacksUsed(packs);
        log.info("\nOrder size: {}\nPack sent: {}", amountOfItemsOrdered, packagedOrder);
        return packagedOrder;

    }

    /**
     * Calculates the optimal distribution of item packs for a given order of items.
     * Packs are chosen starting from the largest available down to the smallest,
     * ensuring the least number of packs are used.
     * 
     * This function iterates through pack sizes in descending order. If the number
     * of items in the order is larger than the pack size at the current index,
     * it will use that pack size and subtract from the item amount until 0 or
     * negative amounts are left.
     * 
     * @param itemsLeft The total number of items to be packed.
     * @param packs     A map to store the pack sizes and their respective
     *                  quantities.
     */
    private void calculatePacks(int itemsLeft, Map<PackSize, Integer> packs) {
        while (itemsLeft > 0) {
            if (itemsLeft <= SMALLEST_PACK_SIZE.getAmountOfItemsInPack()) {
                packs.put(SMALLEST_PACK_SIZE, packs.getOrDefault(SMALLEST_PACK_SIZE, 0) + 1);
                return; // order fulfilled, all packs chosen
            } else {
                for (int i = PackSize.values().length - 1; i >= 0; i--) {
                    PackSize pack = PackSize.values()[i];
                    if (itemsLeft >= pack.getAmountOfItemsInPack()) {
                        packs.put(pack, packs.getOrDefault(pack, 0) + 1);
                        itemsLeft -= pack.getAmountOfItemsInPack();
                        break; // Move to processing the new amount of items now that a pack has been chosen
                    }
                }
            }
        }
    }

    /**
     * Minimizes the number of packs used by consolidating multiple packs of the
     * same size
     * where possible. For example, two packs of 250 items could be replaced by one
     * pack
     * of 500 items if it's more efficient.
     * 
     * @param packs A map of pack sizes and their respective quantities.
     * @return A new map with minimized pack sizes and quantities.
     */
    private Map<PackSize, Integer> minimizePacksUsed(Map<PackSize, Integer> existingPackedItems) {
        Map<PackSize, Integer> optimizedPackedItems = new HashMap<>();

        for (Map.Entry<PackSize, Integer> entry : existingPackedItems.entrySet()) {
            PackSize packSize = entry.getKey();
            int packCount = entry.getValue();
            if (packSize != LARGEST_PACK_SIZE && packCount > 1) {
                int itemsOrdered = packSize.getAmountOfItemsInPack() * packCount;
                calculatePacks(itemsOrdered, optimizedPackedItems);
            }
        }

        return !MapUtils.isEmpty(optimizedPackedItems) ? optimizedPackedItems : existingPackedItems;
    }

}
