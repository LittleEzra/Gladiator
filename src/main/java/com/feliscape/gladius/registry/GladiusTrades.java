package com.feliscape.gladius.registry;

import com.feliscape.gladius.Gladius;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

@EventBusSubscriber(modid = Gladius.MOD_ID)
public class GladiusTrades {
    @SubscribeEvent
    public static void addTrades(VillagerTradesEvent event){
        var trades = event.getTrades();

        if (event.getType() == VillagerProfession.WEAPONSMITH){
            trades.get(2).add(((trader, random) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 5 + random.nextInt(3)),
                    new ItemStack(GladiusItems.OIL_BOTTLE.get()),
                    8, 5, 0.05f
            )));
        }
    }
}
