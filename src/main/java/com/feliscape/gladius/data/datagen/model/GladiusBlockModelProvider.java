package com.feliscape.gladius.data.datagen.model;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.block.MistTrapBlock;
import com.feliscape.gladius.registry.GladiusBlocks;
import net.minecraft.core.FrontAndTop;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

public class GladiusBlockModelProvider extends BlockStateProvider {
    public GladiusBlockModelProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Gladius.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(GladiusBlocks.FRIGID_ICE);
        mistTrap(GladiusBlocks.MIST_TRAP.get());
        flameTrap(GladiusBlocks.FLAME_TRAP.get());
    }

    private void flameTrap(Block block){
        var magmaBlock = ResourceLocation.withDefaultNamespace("block/magma");
        var unpoweredModel = cubeSeparateFaces(block, "", magmaBlock);
        var breathingModel = cubeSeparateFaces(block, "_on", magmaBlock);

        getVariantBuilder(block)
                .forAllStates(state -> {
                    var powered = state.getValue(MistTrapBlock.POWERED);
                    var orientation = state.getValue(MistTrapBlock.ORIENTATION);

                    var model = unpoweredModel;
                    if (powered){
                        model = breathingModel;
                    }

                    return applyRotation(orientation, ConfiguredModel.builder().modelFile(model)).build();
                })
        ;
        simpleBlockItem(block, unpoweredModel);
    }
    private void mistTrap(Block block){
        var frigidIce = blockTexture(GladiusBlocks.FRIGID_ICE.get());
        var unpoweredModel = cubeSeparateFaces(block, "", frigidIce);
        var poweredModel = cubeSeparateFaces(block, "_awake", "_on", frigidIce);
        var breathingModel = cubeSeparateFaces(block, "_on", frigidIce);

        getVariantBuilder(block)
                .forAllStates(state -> {
                    var powered = state.getValue(MistTrapBlock.POWERED);
                    var breathing = state.getValue(MistTrapBlock.BREATHING);
                    var orientation = state.getValue(MistTrapBlock.ORIENTATION);

                    var model = unpoweredModel;
                    if (breathing){
                        model = breathingModel;
                    } else if (powered){
                        model = poweredModel;
                    }

                    return applyRotation(orientation, ConfiguredModel.builder().modelFile(model)).build();
                })
        ;
        simpleBlockItem(block, unpoweredModel);
    }

    private ConfiguredModel.Builder<?> applyRotation(FrontAndTop frontAndTop, ConfiguredModel.Builder<?> builder) {
        return switch (frontAndTop) {
            case DOWN_NORTH -> builder.rotationX(90);
            case DOWN_SOUTH -> builder.rotationX(90).rotationY(180);
            case DOWN_WEST -> builder.rotationX(90).rotationY(270);
            case DOWN_EAST -> builder.rotationX(90).rotationY(90);
            case UP_NORTH -> builder.rotationX(270).rotationY(180);
            case UP_SOUTH -> builder.rotationX(270);
            case UP_WEST -> builder.rotationX(270).rotationY(90);
            case UP_EAST -> builder.rotationX(270).rotationY(270);
            case NORTH_UP -> builder;
            case SOUTH_UP -> builder.rotationY(180);
            case WEST_UP -> builder.rotationY(270);
            case EAST_UP -> builder.rotationY(90);
            default ->
                    throw new UnsupportedOperationException("Rotation " + frontAndTop + " can't be expressed with existing x and y values");
        };
    }

    private BlockModelBuilder cubeSeparateFaces(Block block, String suffix, ResourceLocation particle){
        return models().cube(
                this.name(block) + suffix,
                this.blockTexture(block).withSuffix("_bottom" + suffix),
                this.blockTexture(block).withSuffix("_top" + suffix),
                this.blockTexture(block).withSuffix("_north" + suffix),
                this.blockTexture(block).withSuffix("_south" + suffix),
                this.blockTexture(block).withSuffix("_east" + suffix),
                this.blockTexture(block).withSuffix("_west" + suffix)
        ).texture("particle", particle);
    }
    private BlockModelBuilder cubeSeparateFaces(Block block, String suffix, String sideSuffix, ResourceLocation particle){
        return models().cube(
                this.name(block) + suffix,
                this.blockTexture(block).withSuffix("_bottom" + sideSuffix),
                this.blockTexture(block).withSuffix("_top" + sideSuffix),
                this.blockTexture(block).withSuffix("_north" + suffix),
                this.blockTexture(block).withSuffix("_south" + sideSuffix),
                this.blockTexture(block).withSuffix("_east" + sideSuffix),
                this.blockTexture(block).withSuffix("_west" + sideSuffix)
        ).texture("particle", particle);
    }
    private BlockModelBuilder cubeSeparateFaces(Block block, String suffix){
        return models().cube(
                this.name(block) + suffix,
                this.blockTexture(block).withSuffix("_bottom" + suffix),
                this.blockTexture(block).withSuffix("_top" + suffix),
                this.blockTexture(block).withSuffix("_north" + suffix),
                this.blockTexture(block).withSuffix("_south" + suffix),
                this.blockTexture(block).withSuffix("_east" + suffix),
                this.blockTexture(block).withSuffix("_west" + suffix)
        ).texture("particle", this.blockTexture(block).withSuffix("_north" + suffix));
    }

    private void blockWithItem(Supplier<? extends Block> block){
        simpleBlockWithItem(block.get(), cubeAll(block.get()));
    }
    private void blockWithItem(Supplier<? extends Block> block, String renderType){
        simpleBlockWithItem(block.get(), models().cubeAll(this.name(block.get()), this.blockTexture(block.get())).renderType(renderType));
    }

    private String name(Block block) {
        return this.getLocation(block).getPath();
    }

    private ResourceLocation getLocation(Supplier<? extends Block> block){
        return BuiltInRegistries.BLOCK.getKey(block.get());
    }
    private ResourceLocation getLocation(Block block){
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    private ResourceLocation extend(ResourceLocation location, String suffix) {
        String namespace = location.getNamespace();
        String path = location.getPath();
        return ResourceLocation.fromNamespaceAndPath(namespace, path + suffix);
    }
}
