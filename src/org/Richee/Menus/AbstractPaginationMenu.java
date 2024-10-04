package org.Richee.Menus;

import org.Richee.Translations.Translator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class AbstractPaginationMenu<T> extends AbstractMenu {
    private final T[] objects;
    private int currentPage;
    private int rows;

    public AbstractPaginationMenu(String title, T[] objects) {
        super(title, 18);
        this.objects = objects;
    }

    public AbstractPaginationMenu(String title, T[] objects, int rows) {
        super(title, (rows + 1) * 9);
        this.rows = rows;
        this.objects = objects;
    }

    @Override
    public void open(Player player) {
        this.open(player, 0);
    }

    public void open(Player player, int targetPage) {
        currentPage = targetPage;
        super.open(player);
    }

    @Override
    protected void build() {
        super.build();

        var inv = getInventory();
        var pageSize = rows * 9;
        var totalPages = objects.length / pageSize;
        var page = Math.min(totalPages, Math.max(currentPage, 0));

        ItemMeta meta;

        if ( page > 1 ) {
            this.addItem(
                pageSize,
                Material.ARROW,
                "menu.pagination.previous_page",
                player -> {
                    open(player, currentPage - 1);
                    return null;
                }
            );
        }

        var pageIndicator = new ItemStack(Material.COMPASS);
        meta = pageIndicator.getItemMeta();
        meta.setDisplayName(Translator.id("menu.pagination.page_indicator", page, totalPages));
        pageIndicator.setItemMeta(meta);

        inv.setItem(pageSize + 4, pageIndicator);

        if ( page < totalPages ) {
            this.addItem(
                pageSize + 8,
                Material.ARROW,
                "menu.pagination.next_page",
                player -> {
                    open(player, currentPage + 1);
                    return null;
                }
            );
        }

        for (int i = page * pageSize; i < Math.min(objects.length, (page + 1) * pageSize); i++) {
            var o = objects[i];
            var slot = i;

            this.addItem(
                i % pageSize,
                getMaterialForObject(o),
                o.toString(),
                ignored -> {
                    callback(objects[slot]);
                    return null;
                }
            );
        }
    }

    public abstract void callback(T t);
    public abstract Material getMaterialForObject(T t);
}
