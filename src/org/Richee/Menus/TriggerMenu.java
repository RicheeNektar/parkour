package org.Richee.Menus;

import org.Richee.Core;
import org.Richee.Models.Area;
import org.Richee.Models.Course;
import org.Richee.Models.CourseConfig;
import org.Richee.Models.Interactions.AreaInteraction;
import org.Richee.Models.Triggers.AbstractTrigger;
import org.Richee.Translations.Translator;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.logging.Level;

public class TriggerMenu extends AbstractPaginationMenu<AbstractTrigger> {
    private boolean deleteMode = false;
    private final Course course;
    private CourseConfig config;

    public TriggerMenu(Course course, AbstractTrigger[] objects) {
        this(course, objects, 2);
    }

    public TriggerMenu(Course course, AbstractTrigger[] objects, int rows) {
        super(Translator.id("menu.trigger.title", course.name()), objects, rows);
        this.course = course;
        this.config = course.config();
    }

    @Override
    protected void build() {
        super.build();
        var parent = this;

        addItem(
            9 * 2 + 2,
            Material.STONE_BUTTON,
            "menu.trigger.add",
            ignored -> new TriggerSelect() {
                @Override
                public void callback(AbstractTrigger trigger) {
                player.closeInventory();
                new AreaInteraction(player, area -> {
                    try {
                        parent.objects.add(
                            trigger.getClass().getConstructor(Area.class).newInstance(area)
                        );
                        parent.reopen();
                    } catch (Exception e) {
                        throw new RuntimeException(e); // Should never happen
                    }
                });
                }
            }.open(this.player)
        );

        addItem(
            9 * 2,
            Material.ARROW,
            "menu.trigger.back",
            ignored -> new CourseSetup(
                this.course,
                new CourseConfig(
                    config.getSpawn(),
                    config.getArea(),
                    new ArrayList<>(this.objects)
                )
            ).open(this.player)
        );

        addItem(
            9 * 2 + 6,
            deleteMode ? Material.BARRIER : Material.GUNPOWDER,
            deleteMode ? "menu.trigger.delete.on" : "menu.trigger.delete.off",
            ignored -> {
                deleteMode = !deleteMode;
                reopen();
            }
        );
    }

    @Override
    public void callback(AbstractTrigger trigger) {
        if (deleteMode) {
            objects.remove(trigger);
            reopen();
        } else {
            Core.log(Level.FINE, trigger.getName());
        }
    }

    @Override
    public Material getMaterialForObject(AbstractTrigger trigger) {
        return trigger.getIcon();
    }

    @Override
    public String getLoreForObject(AbstractTrigger trigger) {
        return trigger.getLore();
    }
}
