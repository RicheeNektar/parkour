package org.Richee.Menus;

import org.Richee.Models.Triggers.AbstractTrigger;
import org.Richee.Models.Triggers.CheckpointTrigger;
import org.Richee.Models.Triggers.DeathTrigger;
import org.Richee.Models.Triggers.WinTrigger;
import org.Richee.Translations.Translator;
import org.bukkit.Material;

public class TriggerSelect extends AbstractPaginationMenu<AbstractTrigger> {
    public TriggerSelect() {
        super(
            Translator.id("menu.trigger.select.title"),
            new AbstractTrigger[] {
                new WinTrigger(),
                new DeathTrigger(),
                new CheckpointTrigger(),
            }
        );
    }

    public void callback(AbstractTrigger trigger) {}

    @Override
    public Material getMaterialForObject(AbstractTrigger trigger) {
        return trigger.getIcon();
    }
}
