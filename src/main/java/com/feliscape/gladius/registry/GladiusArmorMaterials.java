package com.feliscape.gladius.registry;

import com.feliscape.gladius.Gladius;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class GladiusArmorMaterials {
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS =
            DeferredRegister.create(Registries.ARMOR_MATERIAL, Gladius.MOD_ID);

    public static final Holder<ArmorMaterial> ARCHER = register(
            "archer",
            Util.make(new EnumMap<>(ArmorItem.Type.class), p_323384_ -> {
                p_323384_.put(ArmorItem.Type.BOOTS, 1);
                p_323384_.put(ArmorItem.Type.LEGGINGS, 2);
                p_323384_.put(ArmorItem.Type.CHESTPLATE, 3);
                p_323384_.put(ArmorItem.Type.HELMET, 1);
                p_323384_.put(ArmorItem.Type.BODY, 3);
            }),
            15,
            SoundEvents.ARMOR_EQUIP_LEATHER,
            0.0F,
            0.0F,
            () -> Ingredient.of(Items.LEATHER),
            List.of(
                    new ArmorMaterial.Layer(Gladius.location("archer"), "", false)
            )
    );
    public static final Holder<ArmorMaterial> NIGHTWALKER = register(
            "nightwalker",
            Util.make(new EnumMap<>(ArmorItem.Type.class), p_323384_ -> {
                p_323384_.put(ArmorItem.Type.BOOTS, 1);
                p_323384_.put(ArmorItem.Type.LEGGINGS, 2);
                p_323384_.put(ArmorItem.Type.CHESTPLATE, 3);
                p_323384_.put(ArmorItem.Type.HELMET, 1);
                p_323384_.put(ArmorItem.Type.BODY, 3);
            }),
            15,
            SoundEvents.ARMOR_EQUIP_LEATHER,
            0.0F,
            0.0F,
            () -> Ingredient.of(Items.LEATHER),
            List.of(
                    new ArmorMaterial.Layer(Gladius.location("nightwalker"), "", false)
            )
    );
    public static final Holder<ArmorMaterial> FLAMEWALKERS = register(
            "flamewalkers",
            Util.make(new EnumMap<>(ArmorItem.Type.class), p_323384_ -> {
                p_323384_.put(ArmorItem.Type.BOOTS, 2);
                p_323384_.put(ArmorItem.Type.LEGGINGS, 3);
                p_323384_.put(ArmorItem.Type.CHESTPLATE, 4);
                p_323384_.put(ArmorItem.Type.HELMET, 2);
                p_323384_.put(ArmorItem.Type.BODY, 4);
            }),
            15,
            Holder.direct(SoundEvents.BASALT_PLACE),
            0.0F,
            0.0F,
            () -> Ingredient.of(Items.BASALT),
            List.of(
                    new ArmorMaterial.Layer(Gladius.location("flamewalkers"), "", false)
            )
    );

    public static void register(IEventBus eventBus){
        ARMOR_MATERIALS.register(eventBus);
    }

    private static Holder<ArmorMaterial> register(
            String name,
            EnumMap<ArmorItem.Type, Integer> defense,
            int enchantmentValue,
            Holder<SoundEvent> equipSound,
            float toughness,
            float knockbackResistance,
            Supplier<Ingredient> repairIngredient,
            List<ArmorMaterial.Layer> layers
    ) {
        EnumMap<ArmorItem.Type, Integer> enummap = new EnumMap<>(ArmorItem.Type.class);

        for (ArmorItem.Type type : ArmorItem.Type.values()) {
            enummap.put(type, defense.get(type));
        }

        return ARMOR_MATERIALS.register(name, () ->
                new ArmorMaterial(enummap, enchantmentValue, equipSound, repairIngredient, layers, toughness, knockbackResistance));
    }
}
