package org.zaksen.zmcore.entity.entities;

import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.zaksen.zmcore.block.blocks.PurifierBlock;
import org.zaksen.zmcore.entity.CoreEntities;
import org.zaksen.zmcore.entity.ImplementedInventory;
import org.zaksen.zmcore.item.CoreItems;
import org.zaksen.zmcore.recipe.recipes.PurifierRecipe;
import org.zaksen.zmcore.screen.screens.PurifierBlockScreenHandler;

import java.util.Optional;

public class PurifierBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory
{
    private final DefaultedList<ItemStack> Inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);
    private final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 100;
    private int fuelTime = 0;
    private int maxFuelTime = 0;

    public PurifierBlockEntity(BlockPos pos, BlockState state) {
        super(CoreEntities.PURIFIER_BLOCK_ENTITY, pos, state);
        propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> PurifierBlockEntity.this.progress;
                    case 1 -> PurifierBlockEntity.this.maxProgress;
                    case 2 -> PurifierBlockEntity.this.fuelTime;
                    case 3 -> PurifierBlockEntity.this.maxFuelTime;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0: PurifierBlockEntity.this.progress = value; break;
                    case 1: PurifierBlockEntity.this.maxProgress = value; break;
                    case 2: PurifierBlockEntity.this.fuelTime = value; break;
                    case 3: PurifierBlockEntity.this.maxFuelTime = value; break;
                }
            }

            @Override
            public int size() {
                return 4;
            }
        };
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("screen.purifier.name");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new PurifierBlockScreenHandler(syncId, inv, this, this.propertyDelegate);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.Inventory;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, Inventory);
        nbt.putInt("progress", progress);
        nbt.putInt("fuel_time", fuelTime);
        nbt.putInt("max_fuel_time", maxFuelTime);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, Inventory);
        nbt.getInt("progress");
        nbt.get("fuel_time");
        nbt.get("max_fuel_time");
    }

    public static void tick(World world, BlockPos blockPos, BlockState state, PurifierBlockEntity entity)
    {
        if(world.isClient()) { return; }
        boolean needUse = entity.needUseFuel();

        if(entity.needUseFuel()) {
            --entity.fuelTime;
        }

        if(hasRecipe(entity)) {
            if(entity.needUseFuel()) {
                entity.progress++;
                markDirty(world, blockPos, state);
                if(entity.progress >= entity.maxProgress) {
                    craftItem(entity);
                }
            } else {
                ItemStack fuel = entity.Inventory.get(0);
                if(canUseAsFuel(fuel)) {
                    entity.fuelTime = entity.getFuelTime(fuel);
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

    private boolean needUseFuel()
    {
        return this.fuelTime > 0;
    }

    private static boolean canUseAsFuel(ItemStack itemStack)
    {
        return AbstractFurnaceBlockEntity.canUseAsFuel(itemStack);
    }

    protected int getFuelTime(ItemStack fuel) {
        if (fuel.isEmpty()) {
            return 0;
        } else {
            Item item = fuel.getItem();
            return (Integer)AbstractFurnaceBlockEntity.createFuelTimeMap().getOrDefault(item, 0);
        }
    }
    private void resetProgress()
    {
        this.progress = 0;
    }

    private static void craftItem(PurifierBlockEntity entity)
    {
        SimpleInventory inventory = new SimpleInventory(entity.size());
        for(int i = 0; i < entity.size(); i++)
        {
            inventory.setStack(i, entity.getStack(i));
        }

        Optional<PurifierRecipe> recipe = entity.getWorld().getRecipeManager().getFirstMatch(PurifierRecipe.Type.INSTANCE, inventory, entity.getWorld());


        if(hasRecipe(entity)) {
            entity.removeStack(1, 1);
            entity.setStack(2, new ItemStack(recipe.get().getOutput().getItem(), entity.getStack(2).getCount() + 1));
            entity.setStack(3, new ItemStack(CoreItems.DUST_POWDER, entity.getStack(3).getCount() + 1));
            entity.resetProgress();
        }
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction side) {
        if(side == Direction.UP && slot == 0) return true;
        if((side == Direction.EAST || side == Direction.NORTH || side == Direction.WEST || side == Direction.SOUTH) && slot == 1) return true;
        return false;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction side) {
        return (side == Direction.DOWN && (slot == 2 || slot == 3));
    }

    private static boolean hasRecipe(PurifierBlockEntity entity)
    {
        SimpleInventory inventory = new SimpleInventory(entity.size());
        for(int i = 0; i < entity.size(); i++)
        {
            inventory.setStack(i, entity.getStack(i));
        }

        Optional<PurifierRecipe> match = entity.getWorld().getRecipeManager().getFirstMatch(PurifierRecipe.Type.INSTANCE, inventory, entity.getWorld());

        return match.isPresent() && isSameItemInOutputSlot(inventory, match.get().getOutput().getItem(), CoreItems.DUST_POWDER) && canInsertInOutputSlot(inventory);
    }

    private static boolean isSameItemInOutputSlot(SimpleInventory inventory, Item outputRes, Item outputDust)
    {
        return (inventory.getStack(2).getItem() == outputRes || inventory.getStack(2).isEmpty()) &&
                (inventory.getStack(3).getItem() == outputDust || inventory.getStack(3).isEmpty());
    }

    private static boolean canInsertInOutputSlot(SimpleInventory inventory)
    {
        return (inventory.getStack(2).getMaxCount() > inventory.getStack(2).getCount()) &&
                (inventory.getStack(3).getMaxCount() > inventory.getStack(3).getCount());
    }
}
