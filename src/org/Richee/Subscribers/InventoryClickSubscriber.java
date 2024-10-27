package org.Richee.Subscribers;

import org.Richee.Core;
import org.Richee.Menus.AbstractMenu;
import org.Richee.Prefix;
import org.Richee.Translations.Translator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InventoryClickSubscriber implements Listener {
    private static final HashMap<InventoryHolder, AbstractMenu> menus = new HashMap<>();
    private static final List<InventoryHolder> stillOpen = new ArrayList<>();

    public static void registerMenu(AbstractMenu menu) {
        menus.put(menu.getInventory().getHolder(), menu);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getCurrentItem() != null) {
            var holder = event.getClickedInventory().getHolder();

            if (menus.containsKey(holder)) {
                var menu = menus.get(holder);
                var p = (Player) event.getWhoClicked();

                // Prevent item pickup
                event.setCancelled(true);

                try {
                    menu.onInventoryClick(event.getRawSlot());
                } catch (Exception e) {
                    p.sendMessage(Translator.id(Prefix.ERROR, "generic.error"));
                    Core.logException(e);
                }

                if (
                    p.getOpenInventory() == null // Closed
                    || p.getOpenInventory().getTopInventory().getHolder() != holder // Opened different menu
                ) {
                    menus.remove(holder);

                } else if (!menus.containsKey(holder)) {
                    menus.put(holder, menu); // Prevent handler disconnecting when refreshing/reopening inventory
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        menus.remove(event.getInventory().getHolder());
    }
}
