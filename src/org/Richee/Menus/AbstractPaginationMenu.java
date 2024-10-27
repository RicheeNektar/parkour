package org.Richee.Menus;

import org.Richee.Translations.Translator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractPaginationMenu<T> extends AbstractMenu {
    protected List<T> objects;
    private final int rows;

    private int currentPage;

    public AbstractPaginationMenu(String title, T[] objects) {
        super(title, 18);
        this.objects = new ArrayList<>(Arrays.asList(objects));
        this.rows = 1;
    }

    public AbstractPaginationMenu(String title, T[] objects, int rows) {
        super(title, (rows + 1) * 9);
        this.objects = new ArrayList<>(Arrays.asList(objects));
        this.rows = rows;
    }

    public AbstractPaginationMenu(String title, List<T> objects) {
        super(title, 18);
        this.objects = objects;
        this.rows = 1;
    }

    public AbstractPaginationMenu(String title, List<T> objects, int rows) {
        super(title, (rows + 1) * 9);
        this.objects = objects;
        this.rows = rows;
    }

    @Override
    public void open(Player player) {
        this.open(player, 0);
    }

    public void open(Player player, int targetPage) {
        currentPage = targetPage;
        super.open(player);
    }

    protected void reopen() {
        open(player, currentPage);
    }

    @Override
    protected void build() {
        super.build();

        var inv = getInventory();
        var pageSize = rows * 9;
        var totalPages = objects.size() / pageSize;
        var page = Math.min(totalPages, Math.max(currentPage, 0));

        ItemMeta meta;

        if ( page > 1 ) {
            this.addItem(
                pageSize,
                Material.ARROW,
                "menu.pagination.previous_page",
                ignored -> open(player, currentPage - 1)
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
                ignored -> open(player, currentPage + 1)
            );
        }

        for (int i = page * pageSize; i < Math.min(objects.size(), (page + 1) * pageSize); i++) {
            var o = objects.get(i);
            var slot = i; // i cannot be used in the delegate below

            this.addItem(
                i % pageSize,
                getMaterialForObject(o),
                o.toString(),
                getLoreForObject(o),
                ignored -> callback(objects.get(slot))
            );
        }
    }

    public abstract void callback(T t);
    public abstract Material getMaterialForObject(T t);

    public String getLoreForObject(T ignored) {
        return null;
    }
}
