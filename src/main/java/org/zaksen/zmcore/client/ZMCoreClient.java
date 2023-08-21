package org.zaksen.zmcore.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import org.zaksen.zmcore.screen.CoreScreenHandlers;
import org.zaksen.zmcore.screen.screens.DustSolventBlockScreen;
import org.zaksen.zmcore.screen.screens.PurifierBlockScreen;

public class ZMCoreClient implements ClientModInitializer
{

    @Override
    public void onInitializeClient()
    {
        HandledScreens.register(CoreScreenHandlers.PURIFIER_SCREEN_HANDLER, PurifierBlockScreen::new);
        HandledScreens.register(CoreScreenHandlers.DUST_SOLVENT_SCREEN_HANDLER, DustSolventBlockScreen::new);
    }
}
