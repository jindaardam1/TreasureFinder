package es.mariaanasanz.ut7.mods.impl;

import com.ibm.icu.impl.DayPeriodRules;
import com.mojang.datafixers.util.Pair;
import es.mariaanasanz.ut7.mods.base.*;
import net.minecraft.core.BlockPos;
import net.minecraft.server.WorldLoader;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.datafix.fixes.EntitySkeletonSplitFix;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.DataPackConfig;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.WorldData;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.client.event.RenderHighlightEvent;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.MinecraftForge;
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
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.commons.compress.compressors.lz77support.LZ77Compressor;

import java.util.Random;

@Mod(DamMod.MOD_ID)
public class ExampleMod extends DamMod implements ILivingDamageEvent {

    public ExampleMod(){
        super();
    }

    @Override
    public String autor() {
        return "Jagoba Inda Arizaleta";
    }

    @Override
    @SubscribeEvent
    public void onLivingDamage(LivingDamageEvent event) {
        System.out.println("evento LivingDamageEvent invocado "+event.getEntity().getClass()+" provocado por "+event.getSource().getEntity());
    }

    @Override
    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        //
        Level world = event.getEntity().getLevel();
        long momentoDelTia = world.getDayTime();
        Vec3 posicionMuerte = event.getEntity().position();
        BlockPos blockpos = new BlockPos(posicionMuerte.x, posicionMuerte.y, posicionMuerte.z);
        BlockState state = Blocks.CHEST.defaultBlockState();
        LivingEntity mobMuerto = event.getEntity();
        if ((mobMuerto instanceof Skeleton) && !(momentoDelTia < 12000)) {
            world.setBlock(blockpos, state,1);
        }

        System.out.println("*".repeat(50));
        System.out.println("*".repeat(50));
        System.out.println("*".repeat(50));
        System.out.println("*".repeat(50));
        System.out.println(world);
        System.out.println(momentoDelTia);
        System.out.println(posicionMuerte);
        System.out.println(mobMuerto);
        System.out.println("*".repeat(50));
        System.out.println("*".repeat(50));
        System.out.println("*".repeat(50));
        System.out.println("*".repeat(50));
    }



    public Item calcularItem() {
        Item item = null;
        item = Items.IRON_INGOT;
        item = Items.GOLD_INGOT;
        item = Items.BREAD;
        item = Items.DIAMOND;
        item = Items.EMERALD;
        item = Items.REDSTONE;
        item = Items.LAPIS_LAZULI;
        item = Items.IRON_SWORD;
        item = Items.GOLDEN_APPLE;
        item = Items.EXPERIENCE_BOTTLE;
        item = Items.COOKED_BEEF;
        item = Items.PUMPKIN_PIE;
        item = Items.ARROW;
        item = Items.FISHING_ROD;
        item = Items.BOW;
        item = Items.SADDLE;
        item = Items.DRAGON_BREATH;
        item = Items.TOTEM_OF_UNDYING;
        item = Items.NETHER_STAR;
        item = Items.COAL;
        item = Items.IRON_ORE;
        item = Items.GOLD_ORE;
        item = Items.DIAMOND_ORE;
        item = Items.EMERALD_ORE;
        item = Items.REDSTONE_ORE;
        item = Items.LAPIS_BLOCK;
        item = Items.IRON_BLOCK;
        item = Items.GOLD_BLOCK;
        item = Items.DIAMOND_BLOCK;
        item = Items.EMERALD_BLOCK;
        item = Items.BEACON;
        item = Items.ENCHANTED_GOLDEN_APPLE;
        item = Items.SHIELD;
        item = Items.TRIDENT;
        item = Items.TURTLE_HELMET;
        item = Items.HEART_OF_THE_SEA;
        item = Items.NAUTILUS_SHELL;
        item = Items.END_CRYSTAL;
        item = Items.ELYTRA;
        item = Items.DRAGON_EGG;
        item = Items.COMMAND_BLOCK;
        item = Items.CAKE;



        Random random = new Random();
        int roll = random.nextInt(100000) + 1;
        if (roll <= 10000) {
            if (random.nextBoolean()) {
                item = Items.IRON_INGOT;
            } else {
                item = Items.GOLD_INGOT;
            }
        } else if (roll <= 20000) {
            item = Items.BREAD;
        } else if (roll <= 30000) {
            int semillaRoll = random.nextInt(8) + 1;
            item = switch (semillaRoll) {
                case 1 -> Items.WHEAT_SEEDS;
                case 2 -> Items.PUMPKIN_SEEDS;
                case 3 -> Items.BEETROOT_SEEDS;
                case 4 -> Items.MELON_SEEDS;
                case 5 -> Items.COCOA_BEANS;
                case 6 -> Items.CARROT;
                case 7 -> Items.POTATO;
                case 8 -> Items.SWEET_BERRIES;
                default -> item;
            };
        } else {
            item = Items.DIAMOND;
        }
        return item;

    }
}
