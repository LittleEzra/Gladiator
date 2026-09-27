package com.feliscape.gladius.content.event;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.client.hud.GripStrengthLayer;
import com.feliscape.gladius.content.attachment.ServerInputData;
import com.feliscape.gladius.content.attachment.AcrobaticsData;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = Gladius.MOD_ID)
public class AcrobaticsHandler {
    @SubscribeEvent
    public static void beforePlayerTick(PlayerTickEvent.Pre event){

        Player player = event.getEntity();
        if (player.level().isClientSide()) return;
        if (!AcrobaticsData.canDoAcrobatics(player)) return;

        var data = player.getData(AcrobaticsData.TYPE);

        if (data.isClinging() && data.getGrip() <= 0) {
            Vec3 deltaMovement = player.getDeltaMovement();
            double amount = data.isSlipping() ? -0.2D : -0.1D;
            if (deltaMovement.y < amount) {
                player.setDeltaMovement(deltaMovement.x, amount, deltaMovement.z);
            }
        }
    }

    public static boolean canMantle(Player player){
        if (!AcrobaticsData.canDoAcrobatics(player)) return false;
        AABB boundingBox = player.getBoundingBox();
        Level level = player.level();

        float height = player.getBbHeight();
        ServerInputData input = player.getData(ServerInputData.TYPE);

        for (Direction d : Direction.Plane.HORIZONTAL){
            boolean topCheck    = !level.noBlockCollision(player, boundingBox.contract(0F, height * -0.7F, 0F).contract(0F, height * 0.1F, 0F).move(0.15F * d.getStepX(), 0.2F, 0.15F * d.getStepZ()));
            boolean centerCheck = !level.noBlockCollision(player, boundingBox.contract(0F, height * -0.4F, 0F).contract(0F, height * 0.4F, 0F).move(0.15F * d.getStepX(),  0-0F, 0.15F * d.getStepZ()));
            boolean bottomCheck = !level.noBlockCollision(player, boundingBox.contract(0F, height * -0.1F, 0F).contract(0F, height * 0.7F, 0F).move(0.15F * d.getStepX(),  0.1F, 0.15F * d.getStepZ()));
            if (!topCheck && centerCheck && bottomCheck && input.getForDirection(player.getYRot(), d)) return true;
        }
        return false;
    }

    @SubscribeEvent
    public static void playerTick(PlayerTickEvent.Post event){
        Player player = event.getEntity();
        if (player.isShiftKeyDown() && canMantle(player)){
            player.getData(AcrobaticsData.TYPE).stopClinging(player);
            Vec3 deltaMovement = player.getDeltaMovement();
            player.setDeltaMovement(deltaMovement.x, Math.max(deltaMovement.y, 0.5D), deltaMovement.z);
            if (player instanceof ServerPlayer serverPlayer){
                serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(player));
            }
            return;
        }

        if (!AcrobaticsData.canDoAcrobatics(player)) {
            if (event.getEntity().level().isClientSide())
                GripStrengthLayer.disable();
            return;
        }

        if (event.getEntity().level().isClientSide())
            GripStrengthLayer.enable();

        var data = player.getData(AcrobaticsData.TYPE);
        data.tick(player);

        if (player.tickCount % 2 == 0){
            data.checkClingingStatus(player);
        }
    }
}
