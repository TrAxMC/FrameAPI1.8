package de.framedev.frameapi.materials;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryManager {

    private Inventory inventory;
    private int inventorysize;
    private String inventoryname;

    public InventoryManager() {}

    public InventoryManager setSize(int size) {
        this.inventorysize = size;
        return this;}

    public InventoryManager setName(String name) {
        this.inventoryname = name;
        return this;}

    public InventoryManager build() {
        inventory = Bukkit.getServer().createInventory(null, 9*this.inventorysize, this.inventoryname);
        setInventory(inventory);
        return this;
    }

    public InventoryManager setItem(ItemStack item,int position) {
        inventory.setItem(position, item);
        return this;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int getInventorysize() {
        return inventorysize;
    }

    public String getInventoryname() {
        return inventoryname;
    }

    public InventoryManager showInv(Player player) {
        player.openInventory(inventory);
        return this;
    }

    public InventoryManager FillNull() {
        int size = 9*this.inventorysize;
        for (int i = 0; i <size; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE,(short)15).setName(" ").build());
            }
        }

        return this;
    }

    public InventoryManager updateInventory(Player player) {
        player.updateInventory();
        return this;
    }

    private void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
