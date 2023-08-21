package org.zaksen.zmcore.entity.entities;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.zaksen.zmcore.block.CoreBlocks;
import org.zaksen.zmcore.entity.CoreEntities;

public class IslandCoreBlockEntity extends BlockEntity
{
    private final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 200;

    public IslandCoreBlockEntity(BlockPos pos, BlockState state)
    {
        super(CoreEntities.ISLAND_CORE_ENTITY, pos, state);
        propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> progress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0: progress = value; break;
                }
            }

            @Override
            public int size() {
                return 2;
            }
        };
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        nbt.getInt("progress");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("progress", progress);
    }

    public static void tick(World world, BlockPos blockPos, BlockState state, IslandCoreBlockEntity entity)
    {
        if(world.isClient()) { return; }
        if(entity.hasSpawn(world, blockPos)) {
            entity.progress++;
            markDirty(world, blockPos, state);
            if(entity.progress >= entity.maxProgress) {
                entity.spawnDust(world, blockPos);
            }
        } else {
            entity.resetProgress();
            markDirty(world, blockPos, state);
        }
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private void spawnDust(World world, BlockPos blockPos) {
        if(hasSpawn(world, blockPos))
        {
            resetProgress();
            BlockPos freePosition = getFreePosition(world, blockPos);
            world.setBlockState(freePosition, getRandomDust());
        }
    }

    private BlockState getRandomDust()
    {
        int random = Random.create().nextBetween(0, 20);
        return switch (random) {
            case 5, 6, 7, 8, 9 -> CoreBlocks.IRON_DUST.getDefaultState();
            case 10, 11, 12, 13 -> CoreBlocks.GOLD_DUST.getDefaultState();
            case 14, 15 -> CoreBlocks.REDSTONE_DUST.getDefaultState();
            case 16, 17 -> CoreBlocks.LAPIS_DUST.getDefaultState();
            case 18 -> CoreBlocks.DIAMOND_DUST.getDefaultState();
            case 19 -> CoreBlocks.EMERALD_DUST.getDefaultState();
            default -> CoreBlocks.DUST.getDefaultState();
        };
    }

    private BlockPos getFreePosition(World world, BlockPos blockPos) {
        if(hasBlockOn(world, blockPos.up())) return blockPos.up();
        else if (hasBlockOn(world, blockPos.down())) return blockPos.down();
        else if (hasBlockOn(world, blockPos.west())) return blockPos.west();
        else if (hasBlockOn(world, blockPos.east())) return blockPos.east();
        else if (hasBlockOn(world, blockPos.south())) return blockPos.south();
        else return blockPos.north();
    }

    private boolean hasBlockOn(World world, BlockPos pos)
    {
        return world.getBlockState( pos ).getBlock() == Blocks.AIR;
    }

    private boolean hasSpawn(World world, BlockPos blockPos) {
        return hasBlockOn(world, blockPos.up()) || hasBlockOn(world, blockPos.down()) ||
                hasBlockOn(world, blockPos.east()) || hasBlockOn(world, blockPos.west()) ||
                hasBlockOn(world, blockPos.south()) || hasBlockOn(world, blockPos.north());
    }
}
