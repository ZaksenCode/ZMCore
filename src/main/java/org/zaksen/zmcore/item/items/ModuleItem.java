package org.zaksen.zmcore.item.items;

import org.zaksen.zmcore.item.TooltipItem;
import org.zaksen.zmcore.item.enums.ModuleType;

public class ModuleItem extends TooltipItem
{
    public final ModuleType moduleType;
    protected final float upgradeValue;

    public ModuleItem(Settings settings, ModuleType moduleType, String translateKey, float upgradeValue) {
        super(settings.maxCount(1), translateKey, 1);
        this.moduleType = moduleType;
        this.upgradeValue = upgradeValue;
    }

    public float getUpgradeValue()
    {
        return this.upgradeValue;
    }
}