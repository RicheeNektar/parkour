package org.Richee.Menus;

import org.Richee.Core;
import org.Richee.Subscribers.InventoryClickSubscriber;
import org.Richee.Translations.Translator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.EventListener;
import java.util.HashMap;
import java.util.function.Consumer;

public abstract class AbstractMenu implements EventListener, InventoryHolder {
    protected Player player;

    private Inventory inventory;
    private final HashMap<Integer, Consumer<Void>> consumers = new HashMap<>();

    private final String name;
    private final int size;

    public AbstractMenu(String name, int size) {
        this.name = name;
        this.size = size;
    }

    public void open(Player player) {
        this.player = player;
        build();
        InventoryClickSubscriber.registerMenu(this);
        player.openInventory(getInventory());
    }

    protected void build() {
        getInventory().clear();

        for (var method : this.getClass().getDeclaredMethods()) {
            var annotations = method.getDeclaredAnnotationsByType(ItemAction.class);

            if (annotations.length > 0) {
                var annotation = annotations[0];

                this.addItem(
                    annotation.slot(),
                    annotation.material(),
                    annotation.label(),
                    ignored -> {
                        try {
                            method.invoke(this);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            player.sendMessage("generic.error");
                            Core.logException(e);
                        }
                    }
                );
            }
        }
    }

    protected void addItem(int slot, Material material, String label, String lore, Consumer<Void> consumer) {
        addItem(slot, material, label, lore == null ? null : lore.split("\n"), consumer);
    }

    protected void addItem(int slot, Material material, String label, Consumer<Void> consumer) {
        addItem(slot, material, label, new String[0], consumer);
    }

    protected void addItem(int slot, Material material, String label, String[] lore) {
        addItem(slot, material, label, lore, null);
    }

    protected void addItem(int slot, Material material, String label) {
        addItem(slot, material, label, new String[0], null);

    }

    protected void addItem(int slot, Material material, String label, String[] lore, Consumer<Void> consumer) {
        var item = new ItemStack(material);
        var meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&r&f" + Translator.id(label)));

        if (lore != null && lore.length > 0) {
            meta.setLore(Arrays.asList(lore));
        }

        item.setItemMeta(meta);
        getInventory().setItem(slot, item);

        if (consumer != null) {
            consumers.put(slot, consumer);
        }
    }

    @Override
    public Inventory getInventory() {
        if (inventory == null) {
            inventory = Bukkit.createInventory(this, this.size, Translator.id(this.name));
        }
        return inventory;
    }

    public void onInventoryClick(int slot) {
        if (consumers.containsKey(slot)) {
            var consumer = consumers.get(slot);

            if (null != consumer) {
                consumer.accept(null);
            }
        }
    }
}
