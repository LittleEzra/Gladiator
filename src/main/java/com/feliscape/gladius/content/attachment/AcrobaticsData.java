package com.feliscape.gladius.content.attachment;

import com.feliscape.gladius.registry.GladiusDataAttachments;
import com.feliscape.gladius.registry.GladiusTags;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.attachment.AttachmentType;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.function.Supplier;

public class AcrobaticsData {
    public static final Supplier<AttachmentType<AcrobaticsData>> TYPE = GladiusDataAttachments.ACROBATICS;

    public static final StreamCodec<ByteBuf, AcrobaticsData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            AcrobaticsData::isClinging,
            ByteBufCodecs.BOOL,
            AcrobaticsData::isSlipping,
            Direction.STREAM_CODEC,
            AcrobaticsData::getDirection,
            ByteBufCodecs.INT,
            AcrobaticsData::getGrip,
            ByteBufCodecs.INT,
            AcrobaticsData::getClingDelay,
            AcrobaticsData::new
    );
    public static final Codec<AcrobaticsData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.INT.fieldOf("direction").forGetter(data -> data.direction.get2DDataValue()),
            Codec.INT.fieldOf("clingDelay").forGetter(AcrobaticsData::getClingDelay),
            Codec.INT.fieldOf("grip").forGetter(AcrobaticsData::getGrip),
            Codec.BOOL.fieldOf("slipping").forGetter(AcrobaticsData::isSlipping)
    ).apply(inst, AcrobaticsData::new));

    private boolean clinging;
    private boolean slipping;
    private Direction direction = Direction.DOWN;
    private int grip;
    int clingDelay;


    public AcrobaticsData(){}

    public AcrobaticsData(int directionData, int grip, int clingDelay, boolean slipping){
        this.direction = directionData == -1 ? Direction.DOWN : Direction.from2DDataValue(directionData);
        this.clinging = this.direction.get2DDataValue() != -1;
        this.slipping = slipping;
        this.grip = grip;
        this.clingDelay = clingDelay;
    }
    public AcrobaticsData(boolean clinging, boolean slipping, Direction direction, int grip, int clingDelay){
        this.clinging = clinging;
        this.slipping = slipping;
        this.direction = direction;
        this.grip = grip;
        this.clingDelay = clingDelay;
    }

    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("direction", clinging ? -1 : direction.get2DDataValue());
        tag.putInt("clingDelay", clingDelay);
        tag.putInt("grip", grip);
        tag.putBoolean("slipping", slipping);
        return tag;
    }

    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        int directionData = tag.getInt("direction");
        this.direction = directionData == -1 ? Direction.DOWN : Direction.from2DDataValue(directionData);
        this.clinging = this.direction.get2DDataValue() != -1;
        this.clingDelay = tag.getInt("clingDelay");
        this.grip = tag.getInt("grip");
        this.slipping = tag.getBoolean("slipping");
    }

    public boolean isClinging() {
        return clinging;
    }
    public boolean isSlipping() {
        return slipping;
    }

    public void tick(Player player){
        if (clingDelay > 0) {
            clingDelay--;
        }
        if (!player.hasInfiniteMaterials()){
            if (clinging && grip > 0) {
                grip--;
            } else if (player.onGround()){
                grip = 10 * 20;
            }
            if (grip <= 0){
                player.resetFallDistance();
            }
        } else{
            grip = 10 * 20;
        }

        if (isClinging()){
            player.resetFallDistance();
        }

        if (!player.level().isClientSide()) {
            syncData(player);
        } else{
            RandomSource random = player.getRandom();
            if (getGrip() <= 0 && isClinging() && random.nextInt(3) == 0){
                float bbWidth = player.getBbWidth();
                double x = this.direction.getStepX() == 0 ? player.getX() + (bbWidth * random.nextDouble()) - (bbWidth * 0.5D):
                        player.getX() + bbWidth * (0.25D + random.nextDouble() * 0.1D) * this.direction.getStepX();
                double y = player.getY(random.nextDouble());
                double z = this.direction.getStepZ() == 0 ? player.getZ() + (bbWidth * random.nextDouble()) - (bbWidth * 0.5D):
                        player.getZ() + bbWidth * (0.25D + random.nextDouble() * 0.1D) * this.direction.getStepZ();

                player.level().addParticle(new DustParticleOptions(new Vector3f(0xa7 / 255.0F, 0xa1 / 255.0F, 0xa0 / 255.0F), 1.0F),
                        x, y, z, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    public void jump(Player player){
        if (!clinging) return;

        if (player.onGround() || player.getAbilities().flying || player.isFallFlying() || !player.getMaxHeightFluidType().isAir()){
            return;
        }

        player.jumpFromGround();
        clingDelay = 10;
        stopClinging(player);

        ServerInputData input = player.getData(ServerInputData.TYPE);
        Vec3 deltaMovement = player.getDeltaMovement();
        boolean neutral = !(getGrip() <= 0) && !input.getForDirection(player.getYRot(), this.direction.getOpposite());
        double x = deltaMovement.x + direction.getStepX() * (neutral ? - 0.05D : -0.175D);
        double y = deltaMovement.y + (neutral ? 0.05D : 0.025D);
        double z = deltaMovement.z + direction.getStepZ() * (neutral ? - 0.05D : -0.175D);

        if (!player.hasInfiniteMaterials())
            grip -= neutral ? 30 : 10;
        player.setDeltaMovement(x, y, z);

        syncData(player, true);

        player.resetFallDistance();
    }

    private void syncData(Player player) {
        player.syncData(TYPE);
    }
    private void syncData(Player player, boolean syncMovement) {
        syncData(player);
        if (player instanceof ServerPlayer serverPlayer) {
            if (syncMovement) serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(player));
        }
    }

    public boolean canCling(Player player){
        if (clingDelay > 0) return false;
        else if (player.onGround()) return false;
        else if (player.getAbilities().flying) return false;
        else if (player.isFallFlying()) return false;
        else if (!player.getMaxHeightFluidType().isAir()) return false;
        else if (!player.isShiftKeyDown()) return false;
        return true;
    }

    public void checkClingingStatus(Player player) {
        if (player.level().isClientSide()) return;

        if (!canCling(player)){
            stopClinging(player);
            syncData(player);
            return;
        }

        AABB boundingBox = player.getBoundingBox();
        Level level = player.level();
        ArrayList<Direction> directions = new ArrayList<>();

        float height = player.getBbHeight();

        for (Direction d : Direction.Plane.HORIZONTAL){
            boolean topCheck    = !level.noBlockCollision(player, boundingBox.contract(0F, height * -0.7F, 0F).contract(0F, height * 0.1F, 0F).move(0.05F * d.getStepX(), -0.1F, 0.05F * d.getStepZ()));
            boolean bottomCheck = !level.noBlockCollision(player, boundingBox.contract(0F, height * -0.1F, 0F).contract(0F, height * 0.7F, 0F).move(0.05F * d.getStepX(), 0.1F, 0.05F * d.getStepZ()));
            if (topCheck && bottomCheck){
                directions.add(d);
            }
        }

        Vec3 lookAngle = player.getLookAngle();
        Direction lookDirection = Direction.fromYRot(player.getYRot());
        if (directions.isEmpty()){
            stopClinging(player);
            syncData(player);
            return;
        }

        if (directions.size() == 1){
            this.direction = directions.get(0);
        } else if (directions.contains(lookDirection)){
            this.direction = lookDirection;
        } else if (directions.contains(lookDirection.getOpposite())){
            this.direction = lookDirection.getOpposite();
        } else{
            double bestFit = 2000F;
            Direction bestDir = directions.getFirst();
            for (Direction dir : directions){
                double d = new Vec3(dir.getStepX() - lookAngle.x, dir.getStepY() - lookAngle.y, dir.getStepZ() - lookAngle.z).length();
                if (d < bestFit){
                    bestFit = d;
                    bestDir = dir;
                }
            }
            this.direction = bestDir;
        }
        //Vec3 deltaMovement = player.getDeltaMovement();
        //player.setDeltaMovement(deltaMovement.x, deltaMovement.y * 0.5D, deltaMovement.z);
        //MovementTech.LOGGER.debug("Direction: {}", direction);
        startClinging(player);

        if (isClinging() && !player.hasInfiniteMaterials() && player.tickCount % 4 == 0){
            BlockState state = level.getBlockState(player.blockPosition().relative(direction));
            if (state.is(GladiusTags.Blocks.SLIPPERY)){
                grip = 0;
                slipping = true;
            } else{
                slipping = false;
            }
        } else {
            slipping = false;
        }

        syncData(player, true);
    }

    private void startClinging(Player player) {
        if (clinging) return;
        clinging = true;
        player.setDeltaMovement(0.0D, 0.0D, 0.0D);
    }
    public void stopClinging(Player player) {
        if (!clinging) return;
        clinging = false;
    }

    public int getGrip() {
        return grip;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getClingDelay() {
        return clingDelay;
    }

    public static boolean canDoAcrobatics(Player player){
        return true;
    }
}
