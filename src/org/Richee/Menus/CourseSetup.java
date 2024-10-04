package org.Richee.Menus;

import org.Richee.CourseIO;
import org.Richee.Exceptions.Validation.WorldMismatchLocationException;
import org.Richee.Interactions.LocationInteraction;
import org.Richee.Models.Course;
import org.Richee.Models.CourseConfig;
import org.Richee.Translations.Translator;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class CourseSetup extends AbstractMenu {
    private final Course course;
    private final CourseConfig config;

    private final static int SLOT_SPAWN_POINT = 0;
    private final static int SLOT_BOUNDARIES = 1;
    private final static int SLOT_SAVE = 3;
    private final static int SLOT_IS_READY = 5;
    private final static int SLOT_TRIGGERS = 8;

    public CourseSetup(Course course) throws CloneNotSupportedException {
        super(Translator.id("menu.course_setup.title", course.toString()), 18);
        this.course = course;
        this.config = course.getConfig();
    }

    @Override
    public void build() {
        super.build();

        this.addItem(
            SLOT_IS_READY,
            course.isReady() ? Material.REDSTONE : Material.GLOWSTONE_DUST,
            "menu.course_setup." + (course.isReady() ? "is_ready" : "is_not_ready")
        );

        this.addItem(
            SLOT_SPAWN_POINT,
            config.getSpawn() == null ? Material.ENDER_PEARL : Material.ENDER_EYE,
            "menu.course_setup.edit_spawn",
            player -> {
                player.closeInventory();

                new LocationInteraction(player, Translator.id("prompt.location", "Spawnpoint"), spawn -> {
                    config.setSpawn(spawn);
                    open(player);

                    return null;
                });
                return null;
            },
            new String[] {
                config.getSpawn().world,
                STR."\{config.getSpawn().x}, \\{config.getSpawn().y}, \\{config.getSpawn().z}",
            }
        );


        this.addItem(
            SLOT_BOUNDARIES,
            config.getSpawn() == null ? Material.BARRIER : Material.ENDER_EYE,
            "menu.course_setup.edit_area",
            player -> {
                player.closeInventory();

                new LocationInteraction(player, Translator.id("prompt.location", "Position 1"), pos1 -> {
                    new LocationInteraction(player, Translator.id("prompt.location", "Position 2"), pos2 -> {
                        try {
                            config.setArea(pos1, pos2);
                        } catch (WorldMismatchLocationException e) {
                            player.sendMessage(Translator.id("prompt.location.world_mismatch"));
                        }

                        open(player);
                        return null;
                    });
                    return null;
                });
                return null;
            },
            new String[] {
                config.getSpawn().world,
                STR."\{config.getSpawn().x}, \\{config.getSpawn().y}, \\{config.getSpawn().z}",
            }
        );

        // @todo: Block actions
    }

    @ItemAction(slot = SLOT_SAVE, material = Material.WRITABLE_BOOK, label = "menu.course_setup.save")
    public void save() throws Exception {
        course.setConfig(config);
        CourseIO.save(course);
    }

    @ItemAction(slot = SLOT_TRIGGERS, material = Material.COMMAND_BLOCK, label = "menu.course_setup.edit_triggers")
    public void editTriggers(Player player) {
        player.sendMessage("Not implemented yet");
    }
}
