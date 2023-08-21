package org.zaksen.zmcore.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.zaksen.zmcore.ZMCore;
import org.zaksen.zmcore.block.blocks.DustOreBlock;
import org.zaksen.zmcore.ApiGroups;
import org.zaksen.zmcore.block.blocks.DustSolventBlock;
import org.zaksen.zmcore.block.blocks.IslandCoreBlock;
import org.zaksen.zmcore.block.blocks.PurifierBlock;
import org.zaksen.zmcore.item.TooltipBlockItem;

public class CoreBlocks
{
    public static final Block DUST = registerBlock("dust", new DustOreBlock(FabricBlockSettings.of(Material.SOIL).strength(0.5f).hardness(0.3f).sounds(BlockSoundGroup.SAND)));
    public static final Item DUST_ITEM = registerBlockItem("dust", DUST, ApiGroups.MAIN_GROUP);
    public static final Block IRON_DUST = registerBlock("iron_dust", new DustOreBlock(FabricBlockSettings.of(Material.SOIL).strength(1f).hardness(0.5f).sounds(BlockSoundGroup.SAND)));
    public static final Item IRON_DUST_ITEM = registerBlockItem("iron_dust", IRON_DUST, ApiGroups.MAIN_GROUP);
    public static final Block GOLD_DUST = registerBlock("gold_dust", new DustOreBlock(FabricBlockSettings.of(Material.SOIL).strength(1f).hardness(0.5f).sounds(BlockSoundGroup.SAND)));
    public static final Item GOLD_DUST_ITEM = registerBlockItem("gold_dust", GOLD_DUST, ApiGroups.MAIN_GROUP);
    public static final Block REDSTONE_DUST = registerBlock("redstone_dust", new DustOreBlock(FabricBlockSettings.of(Material.SOIL).strength(1f).hardness(0.5f).sounds(BlockSoundGroup.SAND)));
    public static final Item REDSTONE_DUST_ITEM = registerBlockItem("redstone_dust", REDSTONE_DUST, ApiGroups.MAIN_GROUP);
    public static final Block LAPIS_DUST = registerBlock("lapis_dust", new DustOreBlock(FabricBlockSettings.of(Material.SOIL).strength(1f).hardness(0.5f).sounds(BlockSoundGroup.SAND)));
    public static final Item LAPIS_DUST_ITEM = registerBlockItem("lapis_dust", LAPIS_DUST, ApiGroups.MAIN_GROUP);
    public static final Block DIAMOND_DUST = registerBlock("diamond_dust", new DustOreBlock(FabricBlockSettings.of(Material.SOIL).strength(1f).hardness(0.5f).sounds(BlockSoundGroup.SAND)));
    public static final Item DIAMOND_DUST_ITEM = registerBlockItem("diamond_dust", DIAMOND_DUST, ApiGroups.MAIN_GROUP);
    public static final Block EMERALD_DUST = registerBlock("emerald_dust", new DustOreBlock(FabricBlockSettings.of(Material.SOIL).strength(1f).hardness(0.5f).sounds(BlockSoundGroup.SAND)));
    public static final Item EMERALD_DUST_ITEM = registerBlockItem("emerald_dust", EMERALD_DUST, ApiGroups.MAIN_GROUP);
    public static final Block ISLAND_CORE = registerBlock("island_core", new IslandCoreBlock(FabricBlockSettings.of(Material.METAL).strength(10f).hardness(10f).sounds(BlockSoundGroup.STONE)));
    public static final Item ISLAND_CORE_ITEM = registerBlockItem("island_core", ISLAND_CORE, ApiGroups.MAIN_GROUP);
    public static final Block MACHINE_CASE = registerBlock("machine_case", new Block(FabricBlockSettings.of(Material.METAL).strength(2f).hardness(2f).sounds(BlockSoundGroup.STONE)));
    public static final Item MACHINE_CASE_ITEM = registerBlockItem("machine_case", MACHINE_CASE, ApiGroups.MAIN_GROUP);
    public static final Block PURIFIER = registerBlock("purifier", new PurifierBlock(FabricBlockSettings.of(Material.METAL).strength(2f).hardness(2f).sounds(BlockSoundGroup.STONE)));
    public static final Item PURIFIER_ITEM = registerBlockItem("purifier", PURIFIER, ApiGroups.MAIN_GROUP, "item.zmcore.base_tip", 4);
    public static final Block DUST_SOLVENT = registerBlock("dust_solvent", new DustSolventBlock(FabricBlockSettings.of(Material.METAL).strength(2f).hardness(2f).sounds(BlockSoundGroup.STONE)));
    public static final Item DUST_SOLVENT_ITEM = registerBlockItem("dust_solvent", DUST_SOLVENT, ApiGroups.MAIN_GROUP, "item.zmcore.base_tip", 4);

    private static Block registerBlock(String name, Block block)
    {
        return Registry.register(Registry.BLOCK, new Identifier(ZMCore.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block, ItemGroup tab)
    {
        return Registry.register(Registry.ITEM, new Identifier(ZMCore.MOD_ID, name), new TooltipBlockItem(block, new FabricItemSettings().group(tab), "", 0));
    }

    private static Item registerBlockItem(String name, Block block, ItemGroup tab, String translateKey, int lines)
    {
        return Registry.register(Registry.ITEM, new Identifier(ZMCore.MOD_ID, name), new TooltipBlockItem(block, new FabricItemSettings().group(tab), translateKey, lines));
    }

    public static void Register()
    {
        try {
            ZMCore.LOGGER.debug("Registering blocks...");
        } catch (Exception e) {
            ZMCore.LOGGER.error("Error when try register blocks: " + e);
        }
    }
}