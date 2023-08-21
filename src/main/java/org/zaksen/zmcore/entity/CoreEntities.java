package org.zaksen.zmcore.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.zaksen.zmcore.ZMCore;
import org.zaksen.zmcore.block.CoreBlocks;
import org.zaksen.zmcore.entity.entities.DustSolventBlockEntity;
import org.zaksen.zmcore.entity.entities.IslandCoreBlockEntity;
import org.zaksen.zmcore.entity.entities.PurifierBlockEntity;

public class CoreEntities
{
    public static BlockEntityType<PurifierBlockEntity> PURIFIER_BLOCK_ENTITY;
    public static BlockEntityType<IslandCoreBlockEntity> ISLAND_CORE_ENTITY;
    public static BlockEntityType<DustSolventBlockEntity> DUST_SOLVENT_ENTITY;

    public static void registerBlockEntities()
    {
        PURIFIER_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(ZMCore.MOD_ID, "purifier"),
                FabricBlockEntityTypeBuilder.create(PurifierBlockEntity::new,
                        CoreBlocks.PURIFIER).build(null));
        ISLAND_CORE_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(ZMCore.MOD_ID, "island_core"),
                FabricBlockEntityTypeBuilder.create(IslandCoreBlockEntity::new,
                        CoreBlocks.ISLAND_CORE).build(null));
        DUST_SOLVENT_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(ZMCore.MOD_ID, "dust_solvent"),
                FabricBlockEntityTypeBuilder.create(DustSolventBlockEntity::new,
                        CoreBlocks.DUST_SOLVENT).build(null));
    }


    public static void Register()
    {
        try {
            registerBlockEntities();
            ZMCore.LOGGER.debug("Registering entities...");
        } catch (Exception e) {
            ZMCore.LOGGER.error("Error when try register entities: " + e);
        }
    }
}
