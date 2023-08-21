package org.zaksen.zmcore.recipe;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.zaksen.zmcore.ZMCore;
import org.zaksen.zmcore.recipe.recipes.DustSolventRecipe;
import org.zaksen.zmcore.recipe.recipes.PurifierRecipe;

public class CoreRecipes
{
    private static void registerRecipes()
    {
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(ZMCore.MOD_ID, PurifierRecipe.Serializer.ID),
                PurifierRecipe.Serializer.INSTANCE);
        Registry.register(Registry.RECIPE_TYPE, new Identifier(ZMCore.MOD_ID, PurifierRecipe.Type.ID),
                PurifierRecipe.Type.INSTANCE);
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(ZMCore.MOD_ID, DustSolventRecipe.Serializer.ID),
                DustSolventRecipe.Serializer.INSTANCE);
        Registry.register(Registry.RECIPE_TYPE, new Identifier(ZMCore.MOD_ID, DustSolventRecipe.Type.ID),
                DustSolventRecipe.Type.INSTANCE);
    }

    public static void  Register()
    {
        try {
            registerRecipes();
            ZMCore.LOGGER.debug("Registering recipes...");
        } catch (Exception e) {
            ZMCore.LOGGER.error("Error when try register recipes: " + e);
        }
    }
}
