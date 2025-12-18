package com.feliscape.gladius.content.mixin;

import com.feliscape.gladius.content.entity.ArrowInterface;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractArrow.class)
public class AbstractArrowMixin implements ArrowInterface {
    @Shadow protected boolean inGround;

    @Override
    public boolean isInGround() {
        return inGround;
    }
}
