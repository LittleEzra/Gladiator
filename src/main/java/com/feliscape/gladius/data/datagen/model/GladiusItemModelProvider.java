package com.feliscape.gladius.data.datagen.model;


import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.client.GladiusItemProperties;
import com.feliscape.gladius.registry.GladiusItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Objects;
import java.util.function.Supplier;

public class GladiusItemModelProvider extends ItemModelProvider {

    public GladiusItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Gladius.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(GladiusItems.BLAZING_HEART.get());

        basicItem(GladiusItems.EXPLOSIVE_ARROW.get());
        basicItem(GladiusItems.PRISMARINE_ARROW.get());
        basicItem(GladiusItems.WINGED_ARROW.get());

        basicItem(GladiusItems.OIL_BOTTLE.get());
        basicItem(GladiusItems.CRYSTAL_BUTTERFLY.get());
        rodItem(GladiusItems.FIREBRAND.get());

        basicItem(GladiusItems.FLASH_POWDER.get());

        gildedDaggerItem(GladiusItems.GILDED_DAGGER.get());
    }

    public ItemModelBuilder potionBundleItem(Item item) {
        ResourceLocation location = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item));
        return this.getBuilder(location.toString()).parent(new ModelFile.UncheckedModelFile("gladius:item/potion_bundle_template"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(location.getNamespace(), "item/" + location.getPath()))
                .texture("layer1", ResourceLocation.fromNamespaceAndPath(location.getNamespace(), "item/" + location.getPath() + "_layer1"))
                .texture("layer2", ResourceLocation.fromNamespaceAndPath(location.getNamespace(), "item/" + location.getPath() + "_layer2"))
                .texture("layer3", ResourceLocation.fromNamespaceAndPath(location.getNamespace(), "item/" + location.getPath() + "_layer3"))
                ;
    }

    public ItemModelBuilder boomerangItem(Item item) {
        ResourceLocation location = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item));

        return this.getBuilder(location.toString()).parent(new ModelFile.UncheckedModelFile("gladius:item/template_boomerang"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(location.getNamespace(), "item/" + location.getPath()));
    }
    public ItemModelBuilder gildedDaggerItem(Item item) {
        ResourceLocation bloodyLocation = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item)).withSuffix("_bloody");
        handheldUnrotatedItem(bloodyLocation);

        var bloody = new ModelFile.UncheckedModelFile(bloodyLocation.withPrefix("item/"));

        return handheldUnrotatedItem(item)
                .override()
                .predicate(GladiusItemProperties.BLOOD, 0.2F)
                .model(bloody)
                .end();
    }

    private ItemModelBuilder simpleDoubleLayered(Supplier<? extends Item> item){
        return withExistingParent(getLocation(item.get()).getPath(),
                ResourceLocation.withDefaultNamespace("item/generated"))
                .texture("layer0", Gladius.location("item/" + getLocation(item.get()).getPath()))
                .texture("layer1", Gladius.location("item/" + getLocation(item.get()).getPath() + "_overlay"))
                ;
    }

    public ItemModelBuilder rodItem(Item item) {
        return rodItem(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item)));
    }

    public ItemModelBuilder rodItem(ResourceLocation item) {
        return getBuilder(item.toString())
                .parent(new ModelFile.UncheckedModelFile("item/handheld_rod"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(item.getNamespace(), "item/" + item.getPath()));
    }
    public ItemModelBuilder handheldUnrotatedItem(Item item) {
        return handheldUnrotatedItem(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item)));
    }

    public ItemModelBuilder handheldUnrotatedItem(ResourceLocation item) {
        return getBuilder(item.toString())
                .parent(new ModelFile.UncheckedModelFile("gladius:item/handheld_unrotated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(item.getNamespace(), "item/" + item.getPath()));
    }

    public ItemModelBuilder slingshotItem(Item item) {
        return slingshotItem(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item)));
    }

    public ItemModelBuilder slingshotItem(ResourceLocation item) {
        return getBuilder(item.toString())
                .parent(new ModelFile.UncheckedModelFile(Gladius.stringLocation("item/template_slingshot")))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(item.getNamespace(), "item/" + item.getPath()));
    }

    private ResourceLocation getLocation(Supplier<? extends Item> item){
        return BuiltInRegistries.ITEM.getKey(item.get());
    }
    private ResourceLocation getLocation(Item item){
        return BuiltInRegistries.ITEM.getKey(item);
    }
}
