package com.jackgoggin.packinghelper;

import java.util.HashMap;
import java.util.Map;

import org.thymeleaf.util.MapUtils;

public class PackHelper {

    enum Pack {
        SMALLEST(250),
        MEDIUM(500),
        LARGE(1000),
        XLARGE(2000),
        LARGEST(5000);

        private final int minimumAmtOfItems;

        Pack(int minimumAmtOfItems) {
            this.minimumAmtOfItems = minimumAmtOfItems;
        }

        public int getMinimumAmtOfItems() {
            return minimumAmtOfItems;
        }
    }

    PackHelper() {

    }

    public HashMap<Pack, Integer> getCorrectAmountOfPacks(int orderSize) {

        HashMap<Pack, Integer> packs = new HashMap<>();

        System.out.println("Order size = " + orderSize);
        recurseThroughPacks(orderSize, packs);

        return optimizePacks(packs);

    }

    private static void recurseThroughPacks(int orderSize, HashMap<Pack, Integer> packs) {
        System.out.println("Updated orderSize: " + orderSize);
        // no more items to fulfill so break processing
        if (orderSize <= 0) {
            return;
        }

        if (orderSize <= Pack.SMALLEST.minimumAmtOfItems) {
            packs.put(Pack.SMALLEST, packs.getOrDefault(Pack.SMALLEST, 0) + 1);
        } else {
            for (int i = Pack.values().length - 1; i >= 0; i--) {
                Pack pack = Pack.values()[i];
                if (orderSize >= pack.getMinimumAmtOfItems()) {
                    System.out.println("Min amt of items: " + pack.getMinimumAmtOfItems());
                    System.out.println("OrderSize = " + orderSize + " - minItems: " + pack.getMinimumAmtOfItems());

                    packs.put(pack, packs.getOrDefault(pack, 0) + 1);

                    recurseThroughPacks(orderSize - pack.getMinimumAmtOfItems(), packs);
                    System.out.println("Updated order: " + packs);

                    return; // avoid unneccesary iterations once a pack is used
                }
            }
        }

    }

    private static HashMap<Pack, Integer> optimizePacks(HashMap<Pack, Integer> packs) {
        HashMap<Pack, Integer> newPacks = new HashMap<>();

        for (Map.Entry<Pack, Integer> entry : packs.entrySet()) {
            if (entry.getValue() > 1 && entry.getKey() != Pack.LARGEST) {

                recurseThroughPacks(entry.getKey().getMinimumAmtOfItems() * entry.getValue(), newPacks);
            }
        }
        System.out.println("NEW PACKS!!!!\n" + newPacks);

        if (!MapUtils.isEmpty(newPacks)) {
            return newPacks;
        }
        return packs;
    }

}
