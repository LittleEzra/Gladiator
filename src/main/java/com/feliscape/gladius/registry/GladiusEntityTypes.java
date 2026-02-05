package com.feliscape.gladius.registry;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.entity.enemy.blackstonegolem.BlackstoneGolem;
import com.feliscape.gladius.content.entity.CrystalButterfly;
import com.feliscape.gladius.content.entity.FlashPowderCloud;
import com.feliscape.gladius.content.entity.Frostmancer;
import com.feliscape.gladius.content.entity.misc.*;
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
    public static final Supplier<EntityType<MistCloud>> MIST_CLOUD = ENTITY_TYPES.registerEntityType("mist_cloud",
            MistCloud::new, MobCategory.MISC, b -> b
                    .sized(3.0F, 1.5F)
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE)
    );

    public static final Supplier<EntityType<MagicOrb>> MAGIC_ORB = ENTITY_TYPES.registerEntityType("magic_orb",
            MagicOrb::new, MobCategory.MISC, b -> b
                    .sized(0.25F, 0.25F)
                    .eyeHeight(0.125F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
    );
    public static final Supplier<EntityType<IceCharge>> ICE_CHARGE = ENTITY_TYPES.registerEntityType("ice_charge",
            IceCharge::new, MobCategory.MISC, b -> b
                    .sized(0.25F, 0.25F)
                    .eyeHeight(0.125F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
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

    public static final Supplier<EntityType<MagmaPool>> MAGMA_POOL = ENTITY_TYPES.registerEntityType("magma_pool",
            MagmaPool::new, MobCategory.MISC, b -> b
                    .sized(4.0F, 4.0F)
                    .clientTrackingRange(4)
                    .updateInterval(5)
    );
    public static final Supplier<EntityType<FireWake>> FIRE_WAKE = ENTITY_TYPES.registerEntityType("fire_wake",
            FireWake::new, MobCategory.MISC, b -> b
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(5)
    );

    public static final Supplier<EntityType<IceBomb>> ICE_BOMB = ENTITY_TYPES.registerEntityType("ice_bomb",
            IceBomb::new, MobCategory.MISC, b -> b
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
    );
    public static final Supplier<EntityType<IceSpikeSpawner>> ICE_SPIKE_SPAWNER = ENTITY_TYPES.registerEntityType("ice_spike_spawner",
            IceSpikeSpawner::new, MobCategory.MISC, b -> b
                    .sized(3.0F, 2.0F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
    );
    public static final Supplier<EntityType<IceSpike>> ICE_SPIKE = ENTITY_TYPES.registerEntityType("ice_spike",
            IceSpike::new, MobCategory.MISC, b -> b
                    .sized(0.65F, 2.0F)
                    .clientTrackingRange(4)
                    .updateInterval(5)
    );
    public static final Supplier<EntityType<IceBlockProjectile>> ICE_BLOCK = ENTITY_TYPES.registerEntityType("ice_block",
            IceBlockProjectile::new, MobCategory.MISC, b -> b
                    .sized(1.5F, 0.65F)
                    .clientTrackingRange(4)
                    .updateInterval(1)
    );

    public static final Supplier<EntityType<Frostmancer>> FROSTMANCER = ENTITY_TYPES.registerEntityType("frostmancer",
            Frostmancer::new, MobCategory.MONSTER, b -> b
                    .sized(0.6F, 1.95F).passengerAttachments(2.0F).ridingOffset(-0.6F)
                    .clientTrackingRange(8)
                    .updateInterval(2)
    );
    public static final Supplier<EntityType<BlackstoneGolem>> BLACKSTONE_GOLEM = ENTITY_TYPES.registerEntityType("blackstone_golem",
            BlackstoneGolem::new, MobCategory.MONSTER, b -> b
                    .sized(1.4F, 3.75F)
                    .fireImmune()
                    .clientTrackingRange(10)
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
