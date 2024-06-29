package com.jackgoggin.packinghelper.Service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.thymeleaf.util.MapUtils;

import com.jackgoggin.packinghelper.Model.PackSize;

@Service
public class ItemPackerService {

    /**
     * Packages an order of X amount of items in an optimal distribution of packs.
     * 
     * @param orderSize The total number of items to be packaged
     * @return A new map containing pack sizes and quantities
     */
    public HashMap<PackSize, Integer> packageItems(int amountOfItemsOrdered) {
        HashMap<PackSize, Integer> packs = new HashMap<>();
        calculatePacks(amountOfItemsOrdered, packs);
        return minimizePacksUsed(packs);

    }

    /**
     * Recursively calculates the optimal distribution of item packs for a given
     * order of X amount of items.
     * Packs are chosen starting from the largest available down to the smallest,
     * ensuring
     * the least number of packs are used.
     * 
     * @param orderSize The total number of items to be packed.
     * @param packs     A map to store the pack sizes and their respective
     *                  quantities.
     */
    private void calculatePacks(int orderSize, HashMap<PackSize, Integer> packs) {
        // no more items to fulfill so break processing
        if (orderSize <= 0) {
            return;
        }

        PackSize smallestPackSize = PackSize.SMALLEST;
        if (orderSize <= smallestPackSize.getAmountOfItemsInPack()) {
            packs.put(smallestPackSize, packs.getOrDefault(smallestPackSize, 0) + 1);
        } else {
            for (int i = PackSize.values().length - 1; i >= 0; i--) {
                PackSize pack = PackSize.values()[i];
                if (orderSize >= pack.getAmountOfItemsInPack()) {
                    packs.put(pack, packs.getOrDefault(pack, 0) + 1);
                    calculatePacks(orderSize - pack.getAmountOfItemsInPack(), packs);
                    return; // avoid unneccesary iterations once a pack is used
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
    private HashMap<PackSize, Integer> minimizePacksUsed(HashMap<PackSize, Integer> packs) {
        HashMap<PackSize, Integer> newPacks = new HashMap<>();

        for (Map.Entry<PackSize, Integer> entry : packs.entrySet()) {
            if (entry.getValue() > 1 && entry.getKey() != PackSize.LARGEST) {
                calculatePacks(entry.getKey().getAmountOfItemsInPack() * entry.getValue(), newPacks);
            }
        }

        if (!MapUtils.isEmpty(newPacks)) {
            return newPacks;
        }
        return packs;
    }

}
