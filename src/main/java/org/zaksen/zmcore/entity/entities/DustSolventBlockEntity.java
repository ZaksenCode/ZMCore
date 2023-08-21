package org.zaksen.zmcore.entity.entities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.zaksen.zmcore.block.blocks.PurifierBlock;
import org.zaksen.zmcore.entity.CoreEntities;
import org.zaksen.zmcore.entity.ImplementedInventory;
import org.zaksen.zmcore.item.enums.ModuleType;
import org.zaksen.zmcore.item.items.ModuleItem;
import org.zaksen.zmcore.recipe.recipes.DustSolventRecipe;
import org.zaksen.zmcore.screen.screens.DustSolventBlockScreenHandler;

import java.util.Optional;

public class DustSolventBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory
{
    private final DefaultedList<ItemStack> Inventory = DefaultedList.ofSize(6, ItemStack.EMPTY);
    private final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private final int defaultMaxProgress = 200;
    private int maxProgress = 200;
    private int fuelTime = 0;
    private int maxFuelTime = 0;

    public DustSolventBlockEntity(BlockPos pos, BlockState state) {
        super(CoreEntities.DUST_SOLVENT_ENTITY, pos, state);
        propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> DustSolventBlockEntity.this.progress;
                    case 1 -> DustSolventBlockEntity.this.maxProgress;
                    case 2 -> DustSolventBlockEntity.this.fuelTime;
                    case 3 -> DustSolventBlockEntity.this.maxFuelTime;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0: DustSolventBlockEntity.this.progress = value; break;
                    case 1: DustSolventBlockEntity.this.maxProgress = value; break;
                    case 2: DustSolventBlockEntity.this.fuelTime = value; break;
                    case 3: DustSolventBlockEntity.this.maxFuelTime = value; break;
                }
            }

            @Override
            public int size() {
                return 4;
            }
        };
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, Inventory);
        nbt.putInt("progress", progress);
        nbt.putInt("fuel_time", fuelTime);
        nbt.putInt("max_fuel_time", maxFuelTime);
        nbt.putInt("max_progress", maxProgress);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, Inventory);
        nbt.getInt("progress");
        nbt.get("fuel_time");
        nbt.get("max_fuel_time");
        nbt.get("max_progress");
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("screen.dust_solvent.name");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new DustSolventBlockScreenHandler(syncId, inv, this, this.propertyDelegate);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.Inventory;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction side) {
        if(side == Direction.UP && slot == 3) return true;
        if((side == Direction.EAST || side == Direction.NORTH || side == Direction.WEST || side == Direction.SOUTH) && slot == 2) return true;
        return false;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction side) {
        return (side == Direction.DOWN && (slot == 5 || slot == 6 || slot == 7 || slot == 8 || slot == 9 || slot == 10 || slot == 11 || slot == 12 || slot == 13));
    }

    private boolean needUseFuel()
    {
        return this.fuelTime > 0;
    }

    private static boolean canUseAsFuel(ItemStack itemStack)
    {
        return AbstractFurnaceBlockEntity.canUseAsFuel(itemStack);
    }

    public static void tick(World world, BlockPos blockPos, BlockState state, DustSolventBlockEntity entity) {
        if (world.isClient()) {return;}
        boolean needUse = entity.needUseFuel();

        if(entity.needUseFuel()) {
            --entity.fuelTime;
        }

        if(hasRecipe(entity)) {
            entity.recountMaxProgress(entity);
            if(entity.needUseFuel()) {
                entity.progress++;
                markDirty(world, blockPos, state);
                if(entity.progress >= entity.maxProgress) {
                    craftItem(entity);
                }
            } else {
                ItemStack fuel = entity.Inventory.get(4);
                if(canUseAsFuel(fuel)) {
                    entity.fuelTime = entity.getFuelTime(fuel, entity);
                    entity.maxFuelTime = entity.fuelTime;
                    fuel.decrement(1);
                } else {
                    entity.resetProgress();
                    markDirty(world, blockPos, state);
                }
            }
        } else {
            entity.resetProgress();
            markDirty(world, blockPos, state);
        }

        if(needUse != entity.needUseFuel()) {
            state = state.with(PurifierBlock.LIT, entity.needUseFuel());
            world.setBlockState(blockPos, state, 3);
        }
    }

    private static void craftItem(DustSolventBlockEntity entity)
    {
        SimpleInventory inventory = new SimpleInventory(entity.size());
        for(int i = 0; i < entity.size(); i++)
        {
            inventory.setStack(i, entity.getStack(i));
        }

        Optional<DustSolventRecipe> recipe = entity.getWorld().getRecipeManager().getFirstMatch(DustSolventRecipe.Type.INSTANCE, inventory, entity.getWorld());

        if(hasRecipe(entity)) {
            entity.removeStack(3, 1);
            entity.setStack(5, new ItemStack(recipe.get().getOutput().getItem(), entity.getStack(5).getCount() + entity.getOutputCount(entity)));
            entity.resetProgress();
        }
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private static boolean hasRecipe(DustSolventBlockEntity entity)
    {
        SimpleInventory inventory = new SimpleInventory(entity.size());
        for(int i = 0; i < entity.size(); i++)
        {
            inventory.setStack(i, entity.getStack(i));
        }

        Optional<DustSolventRecipe> match = entity.getWorld().getRecipeManager().getFirstMatch(DustSolventRecipe.Type.INSTANCE, inventory, entity.getWorld());

        return match.isPresent() && isSameItemInOutputSlot(inventory, match.get().getOutput().getItem()) && canInsertInOutputSlot(inventory);
    }

    private static boolean isSameItemInOutputSlot(SimpleInventory inventory, Item output)
    {
        return (inventory.getStack(5).getItem() == output || inventory.getStack(5).isEmpty());
    }

    private static boolean canInsertInOutputSlot(SimpleInventory inventory)
    {
        return (inventory.getStack(5).getMaxCount() > inventory.getStack(5).getCount());
    }

    // Modules
    private int getModuleCount(ModuleType type, DustSolventBlockEntity entity)
    {
        int count = 0;
        SimpleInventory inventory = new SimpleInventory(entity.size());
        for(int i = 0; i < entity.size(); i++)
        {
            inventory.setStack(i, entity.getStack(i));
        }
        for(int i = 0; i < 3; i++)
        {
            if(inventory.getStack(i).getItem() instanceof ModuleItem)
            {
                ModuleItem moduleItem = (ModuleItem) inventory.getStack(i).getItem();
                if(moduleItem.moduleType == type)
                {
                    count++;
                }
            }
        }
        return count;
    }

    private float getModuleValue(ModuleType type, DustSolventBlockEntity entity)
    {
        float value = 0;
        SimpleInventory inventory = new SimpleInventory(entity.size());
        for(int i = 0; i < entity.size(); i++)
        {
            inventory.setStack(i, entity.getStack(i));
        }
        for(int i = 0; i < 3; i++)
        {
            if(inventory.getStack(i).getItem() instanceof ModuleItem)
            {
                ModuleItem moduleItem = (ModuleItem) inventory.getStack(i).getItem();
                if(moduleItem.moduleType == type)
                {
                    value += moduleItem.getUpgradeValue();
                }
            }
        }
        return value;
    }

    private int getOutputCount(DustSolventBlockEntity entity)
    {
        int module = getModuleCount(ModuleType.PRODUCTIVITY, entity);
        return 1 + module;
    }

    private void recountMaxProgress(DustSolventBlockEntity entity)
    {
        float module = getModuleValue(ModuleType.SPEED, entity);
        if(module > 0) {
            this.maxProgress = (int) (this.defaultMaxProgress / module);
        } else {
            this.maxProgress = this.defaultMaxProgress;
        }
    }

    protected int getFuelTime(ItemStack fuel, DustSolventBlockEntity entity) {
        if (fuel.isEmpty()) {
            return 0;
        } else {
            Item item = fuel.getItem();
            int fuelTime = (Integer)AbstractFurnaceBlockEntity.createFuelTimeMap().getOrDefault(item, 0);
            if(getModuleCount(ModuleType.EFFICIENCY, entity) >= 1)
            {
                return fuelTime * 2;
            }
            return fuelTime;
        }
    }
}