package com.jackgoggin.packinghelper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PackHelperTest {

    ItemPacker itemPacker;

    @BeforeEach
    void setup() {
        itemPacker = new ItemPacker();
    }

    @Test
    void testGetCorrectAmountOfPacksForOneItem() {
        HashMap<PackSize, Integer> expectedPackMap = new HashMap<PackSize, Integer>();
        expectedPackMap.put(PackSize.SMALLEST, 1);

        HashMap<PackSize, Integer> actualPackMap = itemPacker.packageItems(1);

        assertMapContains(expectedPackMap, actualPackMap);

    }

    @Test
    void testGetCorrectAmountOfPacksExact() {
        HashMap<PackSize, Integer> expectedPackMap = new HashMap<PackSize, Integer>();
        expectedPackMap.put(PackSize.SMALLEST, 1);

        HashMap<PackSize, Integer> actualPackMap = itemPacker.packageItems(250);

        assertMapContains(expectedPackMap, actualPackMap);

    }

    @Test
    void testGetCorrectAmountOfPacksForExactThreshold() {
        HashMap<PackSize, Integer> expectedPackMap = new HashMap<PackSize, Integer>();
        expectedPackMap.put(PackSize.MEDIUM, 1);

        assertMapContains(expectedPackMap, itemPacker.packageItems(251));
    }

    @Test
    void testGetCorrectAmountOfPacksForOneOverThreshold() {
        HashMap<PackSize, Integer> expectedPackMap = new HashMap<PackSize, Integer>();
        expectedPackMap.put(PackSize.SMALLEST, 1);
        expectedPackMap.put(PackSize.MEDIUM, 1);
        assertMapContains(expectedPackMap, itemPacker.packageItems(501));
    }

    @Test
    void testGetCorrectAmountOfPacksForHIGH() {
        HashMap<PackSize, Integer> expectedPackMap = new HashMap<PackSize, Integer>();
        expectedPackMap.put(PackSize.SMALLEST, 1);
        expectedPackMap.put(PackSize.XLARGE, 1);
        expectedPackMap.put(PackSize.LARGEST, 2);
        assertMapContains(expectedPackMap, itemPacker.packageItems(12001));
    }

    private void assertMapContains(Map<PackSize, Integer> expectedPackMap, Map<PackSize, Integer> actualPackMap) {
        for (Map.Entry<PackSize, Integer> pack : expectedPackMap.entrySet()) {
            PackSize expectedPackSize = pack.getKey();
            Integer expectedPackSizeAmount = pack.getValue();

            // check that the relevant pack size (SMALL/MEDIUM) is present
            assertTrue(actualPackMap.containsKey(expectedPackSize),
                    "Expected key " + pack.getKey() + " not found in actual map.");

            Integer actualPackSizeAmount = actualPackMap.get(expectedPackSize);
            // check that the present size, is in the right amount
            assertEquals(expectedPackSizeAmount, actualPackSizeAmount,
                    "\n\n !!! Expected - [" + expectedPackSize + " : " + actualPackSizeAmount + "]. Actual - ["
                            + expectedPackSize + " : " + expectedPackSizeAmount + "] !!!\n\n");
        }
    }

}
