package org.zaksen.zmcore.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.zaksen.zmcore.ZMCore;
import org.zaksen.zmcore.ApiGroups;
import org.zaksen.zmcore.item.enums.ModuleType;
import org.zaksen.zmcore.item.items.DustPowderItem;
import org.zaksen.zmcore.item.items.ModuleItem;

public class CoreItems
{
    public static final Item DUST_POWDER = registerItem("dust_powder", new DustPowderItem(new FabricItemSettings().group(ApiGroups.MAIN_GROUP)));
    public static final Item IRON_DUST_POWDER = registerItem("iron_dust_powder", new DustPowderItem(new FabricItemSettings().group(ApiGroups.MAIN_GROUP)));
    public static final Item GOLD_DUST_POWDER = registerItem("gold_dust_powder", new DustPowderItem(new FabricItemSettings().group(ApiGroups.MAIN_GROUP)));
    public static final Item REDSTONE_DUST_POWDER = registerItem("redstone_dust_powder", new DustPowderItem(new FabricItemSettings().group(ApiGroups.MAIN_GROUP)));
    public static final Item LAPIS_DUST_POWDER = registerItem("lapis_dust_powder", new DustPowderItem(new FabricItemSettings().group(ApiGroups.MAIN_GROUP)));
    public static final Item DIAMOND_DUST_POWDER = registerItem("diamond_dust_powder", new DustPowderItem(new FabricItemSettings().group(ApiGroups.MAIN_GROUP)));
    public static final Item EMERALD_DUST_POWDER = registerItem("emerald_dust_powder", new DustPowderItem(new FabricItemSettings().group(ApiGroups.MAIN_GROUP)));
    public static final Item BLANK_MODULE = registerItem("blank_module", new ModuleItem(new FabricItemSettings().group(ApiGroups.MAIN_GROUP), ModuleType.BLANK, "item.zmcore.blank_module_tip", 0));
    public static final Item SPEED_MODULE = registerItem("speed_module", new ModuleItem(new FabricItemSettings().group(ApiGroups.MAIN_GROUP), ModuleType.SPEED, "item.zmcore.speed_module_tip", 1.2f));
    public static final Item EFFICIENCY_MODULE = registerItem("efficiency_module", new ModuleItem(new FabricItemSettings().group(ApiGroups.MAIN_GROUP), ModuleType.EFFICIENCY, "item.zmcore.efficiency_module_tip", 1.5f));
    public static final Item PRODUCTIVITY_MODULE = registerItem("productivity_module", new ModuleItem(new FabricItemSettings().group(ApiGroups.MAIN_GROUP), ModuleType.PRODUCTIVITY, "item.zmcore.productivity_module_tip", 2f));

    private static Item registerItem(String name, Item item)
    {
        return Registry.register(Registry.ITEM, new Identifier(ZMCore.MOD_ID, name), item);
    }

    public static void Register()
    {
        try {
            ZMCore.LOGGER.debug("Registering items...");
        } catch (Exception e) {
            ZMCore.LOGGER.error("Error when try register items: " + e);
        }
    }
}