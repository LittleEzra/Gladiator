package com.feliscape.gladius.networking.payload;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

public class GladiusLevelEvents {
    public static final int STAB_EFFECTS = 1000;
    public static final int CLAYMORE_BLOCK = 1001;
    public static final int OIL_BOTTLE_SPLASH = 1002;
    public static final int OIL_BOTTLE_SPLASH_WATER = 1003;
    public static final int FIREBRAND_LIGHT = 1004;
    public static final int HEARTHSTONE_USE = 1005;
    public static final int FRIGID_ICE_SPREAD = 3001;

    public static void send(Level level, int id, BlockPos location){
        if (level.isClientSide) return;
        PacketDistributor.sendToAllPlayers(new GladiusLevelEventPayload(id, location.getX(), location.getY(), location.getZ()));
    }
}
