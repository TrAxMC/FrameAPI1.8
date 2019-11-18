package de.framedev.frameapi.interfaces;/*
 * This Plugin was Created by FrameDev
 * Copyrighted by FrameDev
 * 15.11.2019, 18:53
 */

public enum EnergyEinheiten {

    VOLT("Volt",140),
    AMPERE("Ampere",540),
    MILIAMPERE("Miliampere",100),
    WATT("Watt",10),
    OHM("Ohm",120);

    String name;
    int amount;
    EnergyEinheiten(String name,int amount) {
        this.amount = amount;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
