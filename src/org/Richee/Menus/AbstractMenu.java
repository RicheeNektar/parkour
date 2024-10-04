package org.Richee.Menus;

import org.Richee.Core;
import org.Richee.Severity;
import org.Richee.Subscribers.InventoryClickSubscriber;
import org.Richee.Translations.Translator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public abstract class AbstractMenu implements EventListener, InventoryHolder {
    protected Player player;

    private Inventory inventory;
    private final HashMap<Integer, Function<Player, Void>> callbacks = new HashMap<>();

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
        for (var method : this.getClass().getDeclaredMethods()) {
            var annotations = method.getDeclaredAnnotationsByType(ItemAction.class);

            if (annotations.length > 0) {
                var annotation = annotations[0];

                this.addItem(
                    annotation.slot(),
                    annotation.material(),
                    annotation.label(),
                    player -> {
                        try {
                            method.invoke(this, player);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            player.sendMessage("menu.generic.error");
                            Core.logException(e);
                        }
                        return null;
                    }
                );
            }
        }
    }

    protected void addItem(int slot, Material material, String label) {
        addItem(slot, material, label, null, new String[] {});
    }

    protected void addItem(int slot, Material material, String label, Function<Player, Void> callback) {
        addItem(slot, material, label, callback, new String[] {});
    }

    protected void addItem(int slot, Material material, String label, Function<Player, Void> callback, String[] lore) {
        var item = new ItemStack(material);
        var meta = item.getItemMeta();
        meta.setDisplayName(Translator.id(label));
        item.setItemMeta(meta);
        meta.setLore(List.of(lore));
        getInventory().setItem(slot, item);

        if (callback != null) {
            Core.log(Severity.DEBUG, slot + label);
            callbacks.put(slot, callback);
        }
    }

    @Override
    public Inventory getInventory() {
        if (inventory == null) {
            inventory = Bukkit.createInventory(this, this.size, Translator.id(this.name));
        }
        return inventory;
    }

    public void onInventoryClick(Player p, int slot) throws Exception {
        if (callbacks.containsKey(slot)) {
            var callback = callbacks.get(slot);

            if (null != callback) {
                callback.apply(p);
            }
        }
    }
}
