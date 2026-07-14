package com.feliscape.gladius.content.attachment;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.content.entity.projectile.rod.RodProjectile;
import com.feliscape.gladius.content.item.projectile.rod.ProjectileRodItem;
import com.feliscape.gladius.registry.GladiusDataAttachments;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class RodData {
    public static final Supplier<AttachmentType<RodData>> TYPE = GladiusDataAttachments.RODS;
    public static final Codec<RodData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            StuckRod.CODEC.listOf().fieldOf("StuckRods").forGetter(data -> data.stuckRods)
    ).apply(inst, RodData::new));
    private static final StreamCodec<RegistryFriendlyByteBuf, List<StuckRod>> STUCK_ROD_LIST_STREAM_CODEC =
            StuckRod.STREAM_CODEC.apply(ByteBufCodecs.list());

    protected List<StuckRod> stuckRods = new ArrayList<>();
    protected LivingEntity owner;

    public RodData() {
    }

    protected RodData(List<StuckRod> stuckRods) {
        this.stuckRods = new ArrayList<>(stuckRods);
    }

    public void tick(){
        if (owner.level().isClientSide()) return;

        boolean dirty = false;
        for (StuckRod stuckRod : stuckRods){
            stuckRod.tick(this.owner);
        }

        int oldSize = stuckRods.size();
        stuckRods.removeIf(rod -> rod.timeLeft <= 0);
        if (stuckRods.size() != oldSize) dirty = true;

        if (dirty){
            this.owner.syncData(TYPE);
        }
    }

    public boolean addRod(Item item, int timeLeft){
        if (item instanceof ProjectileRodItem rodItem) {
            stuckRods.add(new StuckRod(timeLeft, rodItem));
            this.owner.syncData(TYPE);
            return true;
        }
        else{
            Gladius.LOGGER.warn("Tried to add non-rod item to RodData");
            return false;
        }
    }
    public boolean addRod(ProjectileRodItem item, int timeLeft){
        stuckRods.add(new StuckRod(timeLeft, item));
        return true;
    }

    public ProjectileRodItem getStuckRod(int index){
        return stuckRods.get(index).item;
    }

    public int numberOfRods(){
        return stuckRods.size();
    }

    public static boolean addRod(LivingEntity entity, RodProjectile projectile, int duration){
        var data = entity.getData(TYPE);
        if (data.stuckRods.size() >= getMaxRods(entity)) return false;
        return data.addRod(projectile.getPickupItemStackOrigin().getItem(), duration);
    }

    public static boolean hasMatching(LivingEntity entity, TagKey<Item> tag){
        if (!entity.hasData(TYPE)) return false;
        var data = entity.getData(TYPE);
        for (StuckRod rod : data.stuckRods){
            if (rod.item.builtInRegistryHolder().is(tag)){
                return true;
            }
        }
        return false;
    }

    public static int getNumMatching(LivingEntity entity, TagKey<Item> tag){
        if (!entity.hasData(TYPE)) return 0;
        int i = 0;
        var data = entity.getData(TYPE);
        for (StuckRod rod : data.stuckRods){
            if (rod.item.builtInRegistryHolder().is(tag)){
                i++;
            }
        }
        return i;
    }

    public static int getMaxRods(LivingEntity entity){
        return Mth.ceil(entity.getMaxHealth() / 20.0F);
    }

    public static int getNumRods(LivingEntity entity){
        return entity.hasData(TYPE) ? entity.getData(TYPE).stuckRods.size() : 0;
    }

    public static RodData getInstance(IAttachmentHolder holder){
        if (!(holder instanceof LivingEntity entity)){
            throw new IllegalArgumentException("Trying to attach RodData to non-LivingEntity");
        }
        var data = new RodData();
        data.owner = entity;
        return data;
    }

    protected RodData setHolder(IAttachmentHolder holder){
        if (!(holder instanceof LivingEntity entity)){
            throw new IllegalArgumentException("Trying to attach RodData to non-LivingEntity");
        }
        this.owner = entity;
        return this;
    }

    protected void update(RegistryFriendlyByteBuf buffer){
        this.stuckRods = new ArrayList<>(STUCK_ROD_LIST_STREAM_CODEC.decode(buffer));
    }


    protected static class StuckRod{
        public static final Codec<StuckRod> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                Codec.INT.fieldOf("timeLeft").forGetter(rod -> rod.timeLeft),
                BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(rod -> rod.item)
        ).apply(inst, StuckRod::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, StuckRod> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT,
                rod -> rod.timeLeft,
                ByteBufCodecs.registry(Registries.ITEM),
                rod -> rod.item,
                StuckRod::new
        );

        protected int timeLeft;
        protected ProjectileRodItem item;

        public StuckRod(int timeLeft, Item item) {
            if (!(item instanceof ProjectileRodItem rodItem)) throw new IllegalArgumentException("StuckRod created with non-ProjectileRodItem");
            this.timeLeft = timeLeft;
            this.item = rodItem;
        }
        public StuckRod(int timeLeft, ProjectileRodItem item) {
            this.timeLeft = timeLeft;
            this.item = item;
        }

        public void tick(LivingEntity entity){
            this.item.tick(entity, timeLeft);
            timeLeft--;
        }
    }

    public static class Serializer implements IAttachmentSerializer<Tag, RodData> {
        public RodData read(IAttachmentHolder holder, Tag tag, HolderLookup.Provider provider) {
            DataResult<RodData> parsingResult = CODEC.parse(provider.createSerializationContext(NbtOps.INSTANCE), tag);
            var s = parsingResult.getOrThrow((msg) -> this.buildException("read", msg));
            s.setHolder(holder);
            return s;
        }

        public @Nullable Tag write(RodData attachment, HolderLookup.Provider provider) {
            DataResult<Tag> encodingResult = CODEC.encodeStart(provider.createSerializationContext(NbtOps.INSTANCE), attachment);
            return encodingResult.getOrThrow((msg) -> this.buildException("write", msg));
        }

        private RuntimeException buildException(String operation, String error) {
            return new IllegalStateException("Unable to " + operation + " attachment due to an internal codec error: " + error);
        }
    }
    public static class SyncHandler implements AttachmentSyncHandler<RodData>{
        @Override
        public void write(RegistryFriendlyByteBuf buffer, RodData rodData, boolean initialSync) {
            STUCK_ROD_LIST_STREAM_CODEC.encode(buffer, rodData.stuckRods);
        }

        @Override
        public @Nullable RodData read(IAttachmentHolder iAttachmentHolder, RegistryFriendlyByteBuf buffer, @Nullable RodData rodData) {
            var data = new RodData(STUCK_ROD_LIST_STREAM_CODEC.decode(buffer));
            data.setHolder(iAttachmentHolder);
            return data;
        }

        @Override
        public boolean sendToPlayer(IAttachmentHolder holder, ServerPlayer to) {
            return true;
        }
    }
}
