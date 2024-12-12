package org.Richee.Menus;

import org.Richee.Core;
import org.Richee.Models.Course;
import org.Richee.Models.CourseConfig;
import org.Richee.Models.Triggers.AbstractTrigger;
import org.Richee.Translations.Translator;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.logging.Level;

public class TriggerSetup extends AbstractPaginationMenu<AbstractTrigger> {
    private boolean deleteMode = false;
    private final Course course;

    public TriggerSetup(Course course, AbstractTrigger[] objects) {
        super(Translator.id("menu.trigger.setup.title", course.name()), objects, 2);
        this.course = course;
    }

    @Override
    protected void build() {
        super.build();
        var parent = this;

        addItem(
            9 * 2 + 2,
            Material.STONE_BUTTON,
            "menu.trigger.setup.add",
            ignored -> new TriggerSelect() {
                @Override
                public void callback(AbstractTrigger trigger) {
                    player.closeInventory();
                    trigger.setup(player, () -> {
                        parent.objects.add(trigger);
                        parent.reopen();
                    });
                }
            }.open(this.player)
        );

        addItem(
            9 * 2,
            Material.ARROW,
            "menu.trigger.setup.back",
            ignored -> new CourseSetup(
                this.course,
                new CourseConfig(
                    course.config().getSpawn(),
                    course.config().getArea(),
                    new ArrayList<>(this.objects)
                )
            ).open(this.player)
        );

        addItem(
            9 * 2 + 6,
            deleteMode ? Material.BARRIER : Material.GUNPOWDER,
            deleteMode ? "menu.trigger.setup.delete.on" : "menu.trigger.setup.delete.off",
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
        var pos1 = trigger.area.pos1();
        var pos2 = trigger.area.pos2();

        return Translator.id(
            "menu.trigger.setup.lore",
            pos1.getWorld().getName(),
            pos1.x,
            pos1.y,
            pos1.z,
            pos2.x,
            pos2.y,
            pos2.z
        );
    }
}
