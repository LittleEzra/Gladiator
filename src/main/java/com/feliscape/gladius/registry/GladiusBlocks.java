package com.feliscape.gladius.registry;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.block.FrigidIceBlock;
import com.feliscape.gladius.content.block.MistTrapBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class GladiusBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Gladius.MOD_ID);

    public static final DeferredBlock<FrigidIceBlock> FRIGID_ICE = registerBlockWithItem("frigid_ice",
            p -> new FrigidIceBlock(p
                    .mapColor(MapColor.ICE)
                    .strength(2.8F)
                    .friction(0.98F)
                    .sound(SoundType.GLASS)
            ));
    public static final DeferredBlock<MistTrapBlock> MIST_TRAP = registerBlockWithItem("mist_trap",
            p -> new MistTrapBlock(p
                    .mapColor(MapColor.STONE)
                    .strength(3.5F, 6.0F)
                    .requiresCorrectToolForDrops()
                    .friction(0.98F)
                    .sound(SoundType.STONE)
            ));

    private static <T extends Block> DeferredBlock<T> registerBlockWithItem(String name, Function<BlockBehaviour.Properties, ? extends T> block)
    {
        DeferredBlock<T> toReturn = BLOCKS.registerBlock(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }
    private static <T extends Block> DeferredBlock<T> registerBlockWithItem(String name, Function<BlockBehaviour.Properties, ? extends T> block, BlockBehaviour.Properties properties)
    {
        DeferredBlock<T> toReturn = BLOCKS.registerBlock(name, block, properties);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> DeferredItem<Item> registerBlockItem(String name, DeferredBlock<T> block)
    {
        return GladiusItems.ITEMS.registerItem(name, p -> new BlockItem(block.get(), p));
    }

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
