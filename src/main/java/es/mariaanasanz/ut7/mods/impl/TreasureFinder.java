package es.mariaanasanz.ut7.mods.impl;

import es.mariaanasanz.ut7.mods.base.*;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@Mod(DamMod.MOD_ID)
public class TreasureFinder extends DamMod implements IBlockBreakEvent, IServerStartEvent,
        IItemPickupEvent, ILivingDamageEvent, IUseItemEvent, IFishedEvent,
        IInteractEvent, IMovementEvent {

    public TreasureFinder() {
        super();
    }


    @Override
    public String autor() {
        return "Jagoba Inda Arizaleta";
    }

    @Override
    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {

    }

    @Override
    @SubscribeEvent
    public void onServerStart(ServerStartingEvent event) {

    }

    @Override
    @SubscribeEvent
    public void onItemPickup(EntityItemPickupEvent event) {

    }

    @Override
    @SubscribeEvent
    public void onLivingDamage(LivingDamageEvent event) {

    }

    @Override
    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        System.out.println(event.getEntity().toString());
        System.out.println(event.getSource().toString());
        System.out.println(Objects.requireNonNull(event.getPhase()).toString());
        System.out.println(event.getListenerList().toString());
        System.out.println();
        System.out.println("evento LivingDamageEvent invocado "+event.getEntity().getClass()+" provocado por "+event.getSource().getEntity());

    }

    @Override
    @SubscribeEvent
    public void onUseItem(LivingEntityUseItemEvent event) {

    }


    @Override
    @SubscribeEvent
    public void onPlayerFish(ItemFishedEvent event) {

    }

    @Override
    @SubscribeEvent
    public void onPlayerTouch(PlayerInteractEvent.RightClickBlock event) {

    }

    @Override
    @SubscribeEvent
    public void onPlayerWalk(MovementInputUpdateEvent event) {

    }
}

