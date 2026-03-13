package com.feliscape.gladius.content.entity.enemy.piglin;

import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.PiglinModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.piglin.PiglinArmPose;

import java.util.HashMap;

public interface ExtendedPiglinArmPose {
    HashMap<PiglinArmPose, ExtendedPiglinArmPose> PROXIES = new HashMap<>();

    ExtendedPiglinArmPose NONE = new None();
    ExtendedPiglinArmPose TOOT_HORN = new TootHorn();

    static ExtendedPiglinArmPose proxy(PiglinArmPose vanilla){
        return PROXIES.computeIfAbsent(vanilla, VanillaProxy::new);
    }

    void applyPose(Mob mob, PiglinModel<?> model, float ageInTicks);

    record None() implements ExtendedPiglinArmPose{

        @Override
        public void applyPose(Mob mob, PiglinModel<?> model, float ageInTicks) {

        }
    }

    record TootHorn() implements ExtendedPiglinArmPose{

        @Override
        public void applyPose(Mob mob, PiglinModel<?> model, float ageInTicks) {
            model.leftArm.xRot = Mth.clamp(model.head.xRot, -1.2F, 1.2F) - 1.4835298F;
            model.leftArm.yRot = model.head.yRot + (float) (Math.PI / 6);
        }
    }

    record VanillaProxy(PiglinArmPose pose) implements ExtendedPiglinArmPose{
        @Override
        public void applyPose(Mob mob, PiglinModel<?> model, float ageInTicks) {
            if (this.pose == PiglinArmPose.DANCING) {
                float f3 = ageInTicks / 60.0F;
                model.rightEar.zRot = ((float)Math.PI / 6F) + ((float)Math.PI / 180F) * Mth.sin(f3 * 30.0F) * 10.0F;
                model.leftEar.zRot = (-(float)Math.PI / 6F) - ((float)Math.PI / 180F) * Mth.cos(f3 * 30.0F) * 10.0F;
                model.head.x = Mth.sin(f3 * 10.0F);
                model.head.y = Mth.sin(f3 * 40.0F) + 0.4F;
                model.rightArm.zRot = ((float)Math.PI / 180F) * (70.0F + Mth.cos(f3 * 40.0F) * 10.0F);
                model.leftArm.zRot = model.rightArm.zRot * -1.0F;
                model.rightArm.y = Mth.sin(f3 * 40.0F) * 0.5F + 1.5F;
                model.leftArm.y = Mth.sin(f3 * 40.0F) * 0.5F + 1.5F;
                model.body.y = Mth.sin(f3 * 40.0F) * 0.35F;
            } else if (this.pose == PiglinArmPose.ATTACKING_WITH_MELEE_WEAPON && model.attackTime == 0.0F) {
                this.holdWeaponHigh(mob, model);
            } else if (this.pose == PiglinArmPose.CROSSBOW_HOLD) {
                AnimationUtils.animateCrossbowHold(model.rightArm, model.leftArm, model.head, !mob.isLeftHanded());
            } else if (this.pose == PiglinArmPose.CROSSBOW_CHARGE) {
                AnimationUtils.animateCrossbowCharge(model.rightArm, model.leftArm, mob, !mob.isLeftHanded());
            } else if (this.pose == PiglinArmPose.ADMIRING_ITEM) {
                model.head.xRot = 0.5F;
                model.head.yRot = 0.0F;
                if (mob.isLeftHanded()) {
                    model.rightArm.yRot = -0.5F;
                    model.rightArm.xRot = -0.9F;
                } else {
                    model.leftArm.yRot = 0.5F;
                    model.leftArm.xRot = -0.9F;
                }
            }
        }

        private void holdWeaponHigh(Mob mob, PiglinModel<?> model) {
            if (mob.isLeftHanded()) {
                model.leftArm.xRot = -1.8F;
            } else {
                model.rightArm.xRot = -1.8F;
            }
        }
    }
}
