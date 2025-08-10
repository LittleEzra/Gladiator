package com.feliscape.gladius.registry;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.item.*;
import com.feliscape.gladius.content.item.projectile.FirebrandItem;
import com.feliscape.gladius.content.item.projectile.OilBottleItem;
import com.feliscape.gladius.content.item.projectile.arrow.ExplosiveArrowItem;
import com.feliscape.gladius.content.item.projectile.arrow.PrismarineArrowItem;
import com.feliscape.gladius.content.item.projectile.arrow.WingedArrowItem;
import com.feliscape.gladius.content.item.projectile.slingshot.CoatedSteelShotItem;
import com.feliscape.gladius.content.item.projectile.slingshot.GoldShotItem;
import com.feliscape.gladius.content.item.projectile.slingshot.SteelShotItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.DispenserBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GladiusItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Gladius.MOD_ID);

    public static final DeferredItem<ExplosiveArrowItem> EXPLOSIVE_ARROW = ITEMS.registerItem("explosive_arrow",
            p -> new ExplosiveArrowItem(p.component(GladiusComponents.POWER, 3)));
    public static final DeferredItem<PrismarineArrowItem> PRISMARINE_ARROW = ITEMS.registerItem("prismarine_arrow",
            PrismarineArrowItem::new);
    public static final DeferredItem<WingedArrowItem> WINGED_ARROW = ITEMS.registerItem("winged_arrow",
            WingedArrowItem::new);

    public static final DeferredItem<SlingshotItem> SLINGSHOT = ITEMS.registerItem("slingshot",
            p -> new SlingshotItem(p));

    public static final DeferredItem<SteelShotItem> STEEL_SHOT = ITEMS.registerItem("steel_shot",
            p -> new SteelShotItem(p));
    public static final DeferredItem<GoldShotItem> GOLD_SHOT = ITEMS.registerItem("gold_shot",
            p -> new GoldShotItem(p));
    public static final DeferredItem<CoatedSteelShotItem> COATED_STEEL_SHOT = ITEMS.registerItem("coated_steel_shot",
            p -> new CoatedSteelShotItem(p));

    public static final DeferredItem<OilBottleItem> OIL_BOTTLE = ITEMS.registerItem("oil_bottle",
            p -> new OilBottleItem(p.stacksTo(16)));
    public static final DeferredItem<FirebrandItem> FIREBRAND = ITEMS.registerItem("firebrand",
            p -> new FirebrandItem(p));

    public static final DeferredItem<FlashPowderItem> FLASH_POWDER = ITEMS.registerItem("flash_powder",
            p -> new FlashPowderItem(p));

    public static final DeferredItem<CrystalButterflyItem> CRYSTAL_BUTTERFLY = ITEMS.registerItem("crystal_butterfly",
            p -> new CrystalButterflyItem(p));

    public static final DeferredItem<GildedDaggerItem> GILDED_DAGGER = ITEMS.registerItem("gilded_dagger",
            p -> new GildedDaggerItem(p.attributes(SwordItem.createAttributes(Tiers.IRON, 0, -1.9F))));
    public static final DeferredItem<ClaymoreItem> CLAYMORE = ITEMS.registerItem("claymore",
            p -> new ClaymoreItem(p.attributes(SwordItem.createAttributes(GladiusTiers.CLAYMORE, 5, -3.0F))));


    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }

    public static void registerDispenseBehaviors() {
        DispenserBlock.registerProjectileBehavior(EXPLOSIVE_ARROW);
        DispenserBlock.registerProjectileBehavior(PRISMARINE_ARROW);
        DispenserBlock.registerProjectileBehavior(WINGED_ARROW);
        DispenserBlock.registerProjectileBehavior(OIL_BOTTLE);
        DispenserBlock.registerProjectileBehavior(FIREBRAND);
    }
}
