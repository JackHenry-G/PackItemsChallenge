package com.jackgoggin.packinghelper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jackgoggin.packinghelper.PackHelper.Pack;

public class PackHelperTest {

    PackHelper packHelper;

    @BeforeEach
    void setup() {
        packHelper = new PackHelper();
    }

    @Test
    void testGetCorrectAmountOfPacksForOneItem() {
        HashMap<Pack, Integer> expectedPackMap = new HashMap<Pack, Integer>();
        expectedPackMap.put(Pack.SMALLEST, 1);

        HashMap<Pack, Integer> actualPackMap = packHelper.getCorrectAmountOfPacks(1);

        assertMapContains(expectedPackMap, actualPackMap);

    }

    @Test
    void testGetCorrectAmountOfPacksExact() {
        HashMap<Pack, Integer> expectedPackMap = new HashMap<Pack, Integer>();
        expectedPackMap.put(Pack.SMALLEST, 1);

        HashMap<Pack, Integer> actualPackMap = packHelper.getCorrectAmountOfPacks(250);

        assertMapContains(expectedPackMap, actualPackMap);

    }

    @Test
    void testGetCorrectAmountOfPacksForExactThreshold() {
        HashMap<Pack, Integer> expectedPackMap = new HashMap<Pack, Integer>();
        expectedPackMap.put(Pack.MEDIUM, 1);

        assertMapContains(expectedPackMap, packHelper.getCorrectAmountOfPacks(251));
    }

    @Test
    void testGetCorrectAmountOfPacksForOneOverThreshold() {
        HashMap<Pack, Integer> expectedPackMap = new HashMap<Pack, Integer>();
        expectedPackMap.put(Pack.SMALLEST, 1);
        expectedPackMap.put(Pack.MEDIUM, 1);
        assertMapContains(expectedPackMap, packHelper.getCorrectAmountOfPacks(501));
    }

    @Test
    void testGetCorrectAmountOfPacksForHIGH() {
        HashMap<Pack, Integer> expectedPackMap = new HashMap<Pack, Integer>();
        expectedPackMap.put(Pack.SMALLEST, 1);
        expectedPackMap.put(Pack.XLARGE, 1);
        expectedPackMap.put(Pack.LARGEST, 2);
        assertMapContains(expectedPackMap, packHelper.getCorrectAmountOfPacks(12001));
    }

    private void assertMapContains(Map<Pack, Integer> expectedPackMap, Map<Pack, Integer> actualPackMap) {
        for (Map.Entry<Pack, Integer> pack : expectedPackMap.entrySet()) {
            Pack expectedPackSize = pack.getKey();
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
