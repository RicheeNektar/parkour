package org.Richee.Menus;

import org.Richee.Core;
import org.Richee.Events.PlayerJoinCourseEvent;
import org.Richee.IO.Courses;
import org.Richee.Models.Area;
import org.Richee.Models.Course;
import org.Richee.Models.CourseConfig;
import org.Richee.Models.Interactions.LocationInteraction;
import org.Richee.Models.TestCourse;
import org.Richee.Prefix;
import org.Richee.Translations.Translator;
import org.bukkit.Material;

import java.io.IOException;
import java.util.Arrays;

public class CourseSetup extends AbstractMenu {
    private Course course;
    private CourseConfig config;

    private final static int SLOT_SPAWN_POINT = 0;
    private final static int SLOT_BOUNDARIES = 1;
    private final static int SLOT_SAVE = 3;
    private final static int SLOT_TEST = 4;
    private final static int SLOT_IS_READY = 5;
    private final static int SLOT_TRIGGERS = 8;

    public CourseSetup(Course course) {
        this(course, course.config().clone());
    }

    public CourseSetup(Course course, CourseConfig config) {
        super(Translator.id("menu.course.setup.title", course.name()), 18);
        this.course = course;
        this.config = config;
    }

    @Override
    public void build() {
        super.build();

        var errors = config.validate();
        var isReady = errors.length == 0;

        this.addItem(
            SLOT_IS_READY,
            isReady ? Material.GLOWSTONE_DUST : Material.REDSTONE,
            "menu.course.setup." + (isReady ? "is_ready" : "is_not_ready"),
            errors
        );

        var spawn = config.getSpawn();
        var spawnInvalid = spawn == null || spawn.getWorld() == null;
        this.addItem(
            SLOT_SPAWN_POINT,
            Material.RED_BED,
            "menu.course.setup.spawn.edit",
            !spawnInvalid
                ? Translator.id("menu.course.setup.spawn.lore", spawn.getWorld().getName(), spawn.x, spawn.y, spawn.z)
                : null,
            ignored -> {
                player.closeInventory();

                new LocationInteraction(player, Translator.id("prompt.location", "Spawnpoint"), location -> {
                    config = new CourseConfig(
                        location,
                        config.getArea(),
                        Arrays.asList(config.getTriggers())
                    );
                    open(player);
                });
            }
        );

        var area = config.getArea();
        var areaInvalid = area == null || area.pos1().getWorld() == null;
        this.addItem(
            SLOT_BOUNDARIES,
            Material.BARRIER,
            "menu.course.setup.area.edit",
            !areaInvalid
                ? Translator.id("menu.course.setup.area.lore", area.pos1().getWorld().getName(), area.pos1().x, area.pos1().y, area.pos1().z, area.pos2().x, area.pos2().y, area.pos2().z)
                : null,
            ignored -> {
                player.closeInventory();

                new LocationInteraction(
                    player,
                    Translator.id("prompt.location", "Position 1"),
                    pos1 -> new LocationInteraction(
                        player,
                        Translator.id("prompt.location", "Position 2"),
                        pos2 -> {
                            if (!pos1.getWorld().getUID().equals(pos2.getWorld().getUID())) {
                                player.sendMessage(Translator.id("prompt.location.world_mismatch"));
                            } else {
                                config = new CourseConfig(
                                    config.getSpawn(),
                                    new Area(pos1, pos2),
                                    Arrays.asList(config.getTriggers())
                                );
                            }

                            open(player);
                        }
                    )
                );
            }
        );

        this.addItem(
            SLOT_SAVE,
            this.course.config().equals(this.config) ? Material.BOOK : Material.WRITABLE_BOOK,
            "menu.course.setup.save",
            ignored -> {
                this.course = new Course(course.name(), config);
                try {
                    Courses.save(course);
                } catch (IOException e) {
                    player.sendMessage(Translator.id(Prefix.ERROR, "generic.error"));
                    Core.logException(e);
                }
                open(player); // Refresh
            }
        );

        this.addItem(
            SLOT_TEST,
            Material.REDSTONE_TORCH,
            "menu.course.setup.test",
            ignored -> {
                var course = new TestCourse(this.course.name(), this.config);
                try {
                    Courses.save(course);
                } catch (IOException e) {
                    Core.logException(e);
                    // Not too important
                }
                player.closeInventory();
                Core.publishEvent(new PlayerJoinCourseEvent(player, course));
            }
        );

        // @todo: Block actions
    }

    @ItemAction(slot = SLOT_TRIGGERS, material = Material.COMMAND_BLOCK, label = "menu.course.setup.triggers.edit")
    public void editTriggers() {
        new TriggerSetup(this.course, this.config.getTriggers()).open(player);
    }
}
