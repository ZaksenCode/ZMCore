package org.zaksen.zmcore.screen.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.zaksen.zmcore.ZMCore;

public class PurifierBlockScreen extends HandledScreen<PurifierBlockScreenHandler>
{
    private static final Identifier TEXTURE = new Identifier(ZMCore.MOD_ID, "textures/gui/purifier_gui.png");

    public PurifierBlockScreen(PurifierBlockScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f,1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
        renderProgress(matrices, x, y);
    }

    private void renderProgress(MatrixStack matrices, int x, int y)
    {
        if(handler.isCrafting())
        {
            drawTexture(matrices, x + 80, y + 34, 176, 14, handler.getScaledProgress(), 18);
        }
        if(handler.isUsingFuel())
        {
            int scaledFuelTime = handler.getScaledFuel();
            drawTexture(matrices, x + 56, (y + 36 + 12) - scaledFuelTime, 176, 12 - scaledFuelTime, 14, scaledFuelTime + 1);
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }
}
