package com.feliscape.gladius.registry;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.item.*;
import com.feliscape.gladius.content.item.component.AspectComponent;
import com.feliscape.gladius.content.item.projectile.FirebrandItem;
import com.feliscape.gladius.content.item.projectile.IceBombItem;
import com.feliscape.gladius.content.item.projectile.OilBottleItem;
import com.feliscape.gladius.content.item.projectile.arrow.ExplosiveArrowItem;
import com.feliscape.gladius.content.item.projectile.arrow.PrismarineArrowItem;
import com.feliscape.gladius.content.item.projectile.arrow.WingedArrowItem;
import com.feliscape.gladius.data.registry.GladiusAspects;
import net.minecraft.util.Unit;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.DispenserBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GladiusItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Gladius.MOD_ID);

    public static final DeferredItem<Item> FROSTMANCER_SPAWN_EGG = ITEMS.registerItem("frostmancer_spawn_egg",
            p -> new DeferredSpawnEggItem(GladiusEntityTypes.FROSTMANCER, 0x959b9b, 0x273355, p));

    public static final DeferredItem<Item> BLAZING_HEART = ITEMS.registerSimpleItem("blazing_heart");
    public static final DeferredItem<FrigidSeedItem> FRIGID_SEED = ITEMS.registerItem("frigid_seed",
            p -> new FrigidSeedItem(p.rarity(Rarity.RARE)));
    public static final DeferredItem<Item> FRIGID_SHARD = ITEMS.registerSimpleItem("frigid_shard");

    public static final DeferredItem<HearthStoneItem> HEARTH_STONE = ITEMS.registerItem("hearth_stone",
            p -> new HearthStoneItem(p.stacksTo(16)));

    public static final DeferredItem<ExplosiveArrowItem> EXPLOSIVE_ARROW = ITEMS.registerItem("explosive_arrow",
            p -> new ExplosiveArrowItem(p.component(GladiusComponents.POWER, 3)));
    public static final DeferredItem<PrismarineArrowItem> PRISMARINE_ARROW = ITEMS.registerItem("prismarine_arrow",
            PrismarineArrowItem::new);
    public static final DeferredItem<WingedArrowItem> WINGED_ARROW = ITEMS.registerItem("winged_arrow",
            WingedArrowItem::new);

    public static final DeferredItem<OilBottleItem> OIL_BOTTLE = ITEMS.registerItem("oil_bottle",
            p -> new OilBottleItem(p.stacksTo(16)));
    public static final DeferredItem<FirebrandItem> FIREBRAND = ITEMS.registerItem("firebrand",
            p -> new FirebrandItem(p.stacksTo(16)));
    public static final DeferredItem<IceBombItem> ICE_BOMB = ITEMS.registerItem("ice_bomb",
            p -> new IceBombItem(p.stacksTo(16)));

    public static final DeferredItem<FlashPowderItem> FLASH_POWDER = ITEMS.registerItem("flash_powder",
            p -> new FlashPowderItem(p));

    public static final DeferredItem<CrystalButterflyItem> CRYSTAL_BUTTERFLY = ITEMS.registerItem("crystal_butterfly",
            p -> new CrystalButterflyItem(p));

    public static final DeferredItem<GildedDaggerItem> GILDED_DAGGER = ITEMS.registerItem("gilded_dagger",
            p -> new GildedDaggerItem(p.attributes(SwordItem.createAttributes(Tiers.IRON, 1, -1.9F))
                    .component(GladiusComponents.BLOOD, 0)));
    public static final DeferredItem<ClaymoreItem> CLAYMORE = ITEMS.registerItem("claymore",
            p -> new ClaymoreItem(p.rarity(Rarity.EPIC)
                    .attributes(SwordItem.createAttributes(GladiusTiers.CLAYMORE, 6, -3.0F))
                    .component(GladiusComponents.TWO_HANDED, Unit.INSTANCE))
    );
    public static final DeferredItem<FlambergeItem> FLAMBERGE = ITEMS.registerItem("flamberge",
            p -> new FlambergeItem(p.rarity(Rarity.RARE)
                    .attributes(SwordItem.createAttributes(GladiusTiers.FLAMBERGE, 7, -2.8F))
                    .component(GladiusComponents.ASPECT, AspectComponent.of(GladiusAspects.FIRE, true))
                    .component(GladiusComponents.TWO_HANDED, Unit.INSTANCE)
            ));
    public static final DeferredItem<WandItem> GOLDEN_WAND = ITEMS.registerItem("golden_wand",
            p -> new WandItem(p.stacksTo(1)
                    .component(GladiusComponents.ASPECT, AspectComponent.of(GladiusAspects.MAGIC, false))
                    .component(GladiusComponents.MAGIC_CHARGES, 10)
                    .component(GladiusComponents.MAX_MAGIC_CHARGES, 10)
            ));
    public static final DeferredItem<ArmorItem> FLAMEWALKERS = ITEMS.registerItem("flamewalkers",
            p -> new ArmorItem(GladiusArmorMaterials.FLAMEWALKERS, ArmorItem.Type.BOOTS, p));
    public static final DeferredItem<PowerGauntletsItem> POWER_GAUNTLETS = ITEMS.registerItem("power_gauntlets",
            p -> new PowerGauntletsItem(p.attributes(PowerGauntletsItem.createAttributes())));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }

    public static void registerDispenseBehaviors() {
        DispenserBlock.registerProjectileBehavior(EXPLOSIVE_ARROW);
        DispenserBlock.registerProjectileBehavior(PRISMARINE_ARROW);
        DispenserBlock.registerProjectileBehavior(WINGED_ARROW);
        DispenserBlock.registerProjectileBehavior(OIL_BOTTLE);
        DispenserBlock.registerProjectileBehavior(FIREBRAND);
        DispenserBlock.registerProjectileBehavior(ICE_BOMB);
    }
}
