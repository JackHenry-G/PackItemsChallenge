package com.jackgoggin.packinghelper;

public enum PackSize {
    SMALLEST(250),
    MEDIUM(500),
    LARGE(1000),
    XLARGE(2000),
    LARGEST(5000);

    private final int amountOfItemsInPack;

    PackSize(int amountOfItemsInPack) {
        this.amountOfItemsInPack = amountOfItemsInPack;
    }

    public int getAmountOfItemsInPack() {
        return amountOfItemsInPack;
    }
}
