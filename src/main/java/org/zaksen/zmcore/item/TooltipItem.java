package org.zaksen.zmcore.item;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TooltipItem extends Item
{
    protected final String translateKey;
    protected final int lines;
    public TooltipItem(Settings settings, String translateKey, int lines) {
        super(settings);
        this.translateKey = translateKey;
        this.lines = lines;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if(translateKey != "" && lines > 0) {
            if (Screen.hasShiftDown()) {
                for (int i = 0; i < this.lines; i++) {
                    tooltip.add(Text.translatable(String.format("%s_%s", this.translateKey, i + 1)).formatted(Formatting.YELLOW));
                }
            } else {
                tooltip.add(Text.translatable("item.zmcore.more_info_tip").formatted(Formatting.GRAY));
            }
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}
