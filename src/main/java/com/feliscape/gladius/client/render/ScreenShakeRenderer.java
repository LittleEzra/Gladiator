package com.feliscape.gladius.client.render;

import com.feliscape.gladius.Gladius;
import com.feliscape.gladius.GladiusClient;
import com.feliscape.gladius.GladiusClientConfig;
import com.feliscape.gladius.util.RandomUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.MaceItem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = Gladius.MOD_ID, value = Dist.CLIENT)
public class ScreenShakeRenderer {
    private List<Shake> screenShakes = new ArrayList<>();
    private RandomSource random;

    public ScreenShakeRenderer() {
        random = RandomSource.create();
    }

    public void shake(Vector2f amplitude, int duration){
        if (GladiusClientConfig.CONFIG.screenShake.getAsDouble() <= 0.0D) return;
        screenShakes.add(new Shake(amplitude, duration));
    }
    public void shake(float amplitudeX, float amplitudeY, int duration){
        shake(new Vector2f(amplitudeX, amplitudeY), duration);
    }
    public void shake(float amplitude, int duration){
        shake(amplitude, amplitude, duration);
    }

    public void updateScreenShakes(){
        screenShakes.removeIf(Shake::update);
    }

    public Vector2f getTotalAmplitude(float partialTick){
        Vector2f amplitude = new Vector2f(0.0F);
        for (Shake s : screenShakes){
            float f = (s.getTimeLeft(partialTick) / (float) s.duration);
            f = f * f;
            amplitude.x += s.amplitude.x * f;
            amplitude.y += s.amplitude.y * f;
        }
        return amplitude;
    }

    private void resetShaking(){
        screenShakes.clear();
    }

    @SubscribeEvent
    public static void clientTick(ClientTickEvent.Post event){
        GladiusClient.screenShake.updateScreenShakes();
    }
    @SubscribeEvent
    public static void applyShake(ViewportEvent.ComputeCameraAngles event){
        double configStrength = GladiusClientConfig.CONFIG.screenShake.getAsDouble();
        if (configStrength <= 0.0D) return;

        ScreenShakeRenderer renderer = GladiusClient.screenShake;
        Vector2f amplitude = renderer.getTotalAmplitude((float) event.getPartialTick());
        event.setYaw(event.getYaw() + (float) Math.cos(renderer.random.nextGaussian() * 6.0D) * amplitude.x * (float) configStrength);
        event.setPitch(event.getPitch() + (float) Math.sin(renderer.random.nextGaussian() * 6.0D) * amplitude.y * (float) configStrength);
    }

    protected static class Shake{
        final Vector2f amplitude;
        final int duration;
        int timeLeft;

        public Shake(Vector2f amplitude, int duration) {
            this.amplitude = amplitude;
            this.duration = duration;
            this.timeLeft = duration;
        }
        public Shake(float amplitude, int duration) {
            this.amplitude = new Vector2f(amplitude);
            this.duration = duration;
            this.timeLeft = duration;
        }

        public float getTimeLeft(float partialTick){
            return Math.max(timeLeft - partialTick, 0.0F);
        }

        public boolean update(){
            return --timeLeft <= 0;
        }
    }
}
