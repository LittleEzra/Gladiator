package com.feliscape.gladius.registry;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.entity.CrystalButterfly;
import com.feliscape.gladius.content.entity.FlashPowderCloud;
import com.feliscape.gladius.content.entity.projectile.*;
import com.feliscape.gladius.registry.foundation.DeferredEntityTypeRegister;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;

import java.util.function.Supplier;

public class GladiusEntityTypes {
    public static final DeferredEntityTypeRegister ENTITY_TYPES =
            DeferredEntityTypeRegister.create(Gladius.MOD_ID);

    public static final Supplier<EntityType<FlashPowderCloud>> FLASH_POWDER_CLOUD = ENTITY_TYPES.registerEntityType("flash_powder_cloud",
            FlashPowderCloud::new, MobCategory.MISC, b -> b
                    .sized(6.0F, 4.5F)
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE)
    );

    public static final Supplier<EntityType<ExplosiveArrow>> EXPLOSIVE_ARROW = ENTITY_TYPES.registerEntityType("explosive_arrow",
            ExplosiveArrow::new, MobCategory.MISC, b -> b
                    .sized(0.5F, 0.5F)
                    .eyeHeight(0.13F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
    );
    public static final Supplier<EntityType<PrismarineArrow>> PRISMARINE_ARROW = ENTITY_TYPES.registerEntityType("prismarine_arrow",
            PrismarineArrow::new, MobCategory.MISC, b -> b
                    .sized(0.5F, 0.5F)
                    .eyeHeight(0.13F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
    );
    public static final Supplier<EntityType<WingedArrow>> WINGED_ARROW = ENTITY_TYPES.registerEntityType("winged_arrow",
            WingedArrow::new, MobCategory.MISC, b -> b
                    .sized(0.5F, 0.5F)
                    .eyeHeight(0.13F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
    );
    public static final Supplier<EntityType<ThrownOilBottle>> OIL_BOTTLE = ENTITY_TYPES.registerEntityType("oil_bottle",
            ThrownOilBottle::new, MobCategory.MISC, b -> b
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
    );
    public static final Supplier<EntityType<Firebrand>> FIREBRAND = ENTITY_TYPES.registerEntityType("firebrand",
            Firebrand::new, MobCategory.MISC, b -> b
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
    );

    public static final Supplier<EntityType<CrystalButterfly>> CRYSTAL_BUTTERFLY = ENTITY_TYPES.registerEntityType("crystal_butterfly",
            CrystalButterfly::new, MobCategory.MISC, b -> b
                    .sized(0.35F, 0.35F)
                    .clientTrackingRange(8)
                    .updateInterval(2)
    );

    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}
