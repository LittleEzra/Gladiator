package com.feliscape.gladius.util;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.item.NightwalkerArmorItem;
import com.feliscape.gladius.registry.GladiusItems;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Pose;

import javax.annotation.Nullable;

public class MixinUtil {
    private static final ResourceLocation NIGHTWALKER_SKIN = Gladius.location("textures/entity/player/nightwalker.png");

    @Nullable
    public static ResourceLocation getPlayerSkin(AbstractClientPlayer player){
        if (NightwalkerArmorItem.isInStealth(player)){
            return NIGHTWALKER_SKIN;
        }
        return null;
    }
}
