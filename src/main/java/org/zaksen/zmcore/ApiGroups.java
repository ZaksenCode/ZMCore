package org.zaksen.zmcore;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.zaksen.zmcore.ZMCore;

public class ApiGroups
{
    public static final ItemGroup MAIN_GROUP = FabricItemGroupBuilder.build(new Identifier(ZMCore.MOD_ID, "main"), Items.BARRIER::getDefaultStack);
}
