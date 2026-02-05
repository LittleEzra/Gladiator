package com.feliscape.gladius.registry.entity;

import com.feliscape.gladius.Gladius;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.schedule.Activity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class GladiusActivities {
    public static final DeferredRegister<Activity> ACTIVITIES =
            DeferredRegister.create(Registries.ACTIVITY, Gladius.MOD_ID);

    public static final Supplier<Activity> CHARGING = ACTIVITIES.register(
            "charging", location -> new Activity(location.getPath())
    );

    public static void register(IEventBus eventBus){
        ACTIVITIES.register(eventBus);
    }
}
