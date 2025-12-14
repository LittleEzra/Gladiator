package com.feliscape.gladius.content.item;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.attachment.PowerGauntletData;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class PowerGauntletsItem extends GauntletsItem {
    public PowerGauntletsItem(Properties properties) {
        super(properties, 28);
    }

    public static ItemAttributeModifiers createAttributes() {
        return createAttributes(3.0D, 1.0D, -0.75D);
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return !player.isCreative();
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity, InteractionHand hand) {
        if (stack.is(this)){
            if (entity instanceof Player player){
                if (player.getCooldowns().isOnCooldown(this)) return false;
                player.getCooldowns().addCooldown(this, 8);
            }

            PowerGauntletData data = entity.getData(PowerGauntletData.type());

            var viewVector = entity.getViewVector(1.0F);
            boolean slamAttack = entity.isShiftKeyDown() || viewVector.y < -0.65D;
            data.launch(12, viewVector);

            double x = viewVector.x * 0.5D;
            double y = viewVector.y * 0.4D + 0.2D;
            double z = viewVector.z * 0.5D;
            if (slamAttack){
                x *= 0.1D;
                y -= 2.2D;
                z *= 0.1D;
                entity.setDeltaMovement(0.0D, entity.getDeltaMovement().y, 0.0D);
            }

            entity.push(x, y, z);
            if (entity instanceof ServerPlayer serverPlayer){
                serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(serverPlayer));
            }
            entity.syncData(PowerGauntletData.type());
            return true;
        }
        return false;
    }
}
