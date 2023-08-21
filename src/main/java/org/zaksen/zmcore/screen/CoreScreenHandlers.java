package org.zaksen.zmcore.screen;

import net.minecraft.screen.ScreenHandlerType;
import org.zaksen.zmcore.ZMCore;
import org.zaksen.zmcore.screen.screens.DustSolventBlockScreenHandler;
import org.zaksen.zmcore.screen.screens.PurifierBlockScreenHandler;

public class CoreScreenHandlers
{
    public static ScreenHandlerType<PurifierBlockScreenHandler> PURIFIER_SCREEN_HANDLER;
    public static ScreenHandlerType<DustSolventBlockScreenHandler> DUST_SOLVENT_SCREEN_HANDLER;

    private static void registerScreenHandlers()
    {
        PURIFIER_SCREEN_HANDLER = new ScreenHandlerType<>(PurifierBlockScreenHandler::new);
        DUST_SOLVENT_SCREEN_HANDLER = new ScreenHandlerType<>(DustSolventBlockScreenHandler::new);
    }

    public static void Register()
    {
        try {
            registerScreenHandlers();
            ZMCore.LOGGER.debug("Registering screen handlers...");
        } catch (Exception e) {
            ZMCore.LOGGER.error("Error when try register screen handlers: " + e);
        }
    }
}
