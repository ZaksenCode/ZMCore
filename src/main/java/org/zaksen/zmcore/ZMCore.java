package org.zaksen.zmcore;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zaksen.zmcore.block.CoreBlocks;
import org.zaksen.zmcore.entity.CoreEntities;
import org.zaksen.zmcore.item.CoreItems;
import org.zaksen.zmcore.recipe.CoreRecipes;
import org.zaksen.zmcore.screen.CoreScreenHandlers;

public class ZMCore implements ModInitializer
{
    public static final String MOD_ID = "zmcore";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize()
    {
        CoreItems.Register();
        CoreBlocks.Register();
        CoreEntities.Register();
        CoreScreenHandlers.Register();
        CoreRecipes.Register();
    }
}
