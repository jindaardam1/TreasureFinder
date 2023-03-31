package es.mariaanasanz.ut7.mods.impl;

import es.mariaanasanz.ut7.mods.base.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.Random;

//Crea un mod que cada vez que el jugador asesine a un esqueleto de día CON UNA ESPADA, genere un cofre en la posición del esqueleto
// con una cantidad de items aleatoria en base al nivel del jugador. El cofre podrá contener hasta 30 items aleatorios (a elección de
// vosotros entre que objetos aleatorios) y para cada uno de esos 30 items aleatorios, la probabilidad de obtenerlo será de X, dónde X es el nivel del jugador.
// Además, en base al tipo de arma, esa probabilidad aumentará en 1 en función del tipo. Si es de madera, aumentará en 1, si es de piedra
// en 2, si es de hierro en 3, si es de oro en 5 y si es de diamante en 10.

@Mod(DamMod.MOD_ID)
public class ExampleMod extends DamMod implements ILivingDamageEvent {

    public final int NIVEL_MAXIMO_RELEVANTE = 27;

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
        if (event.getSource().getEntity() != null) {
            Random random = new Random();
            Level world = event.getEntity().getLevel(); // El mundo en el que se está jugando
            Player jugador = (Player) event.getSource().getEntity(); // El jugador que está causando el evento de muerte de mob
            ItemStack jugadorMataCon = jugador.getMainHandItem(); // El objeto con el que el jugador ha matado al mob
            int nivelJugador = jugador.experienceLevel; // El nivel del jugador
            if (nivelJugador > NIVEL_MAXIMO_RELEVANTE) {
                nivelJugador = NIVEL_MAXIMO_RELEVANTE;
            }
            long momentoDelTia = world.getDayTime(); // Momento del día en el que ocurre el event
            Vec3 posicionMuerte = event.getEntity().position(); // Posición de muerte del mob
            BlockPos blockpos = new BlockPos(posicionMuerte.x, posicionMuerte.y, posicionMuerte.z); // Conversión de la posición de muerte del mob a BlockPos
            BlockState state = calcularTipoDeCofre(random.nextInt(20) + 1, random.nextInt(12) + 1);
            LivingEntity mobMuerto = event.getEntity();
            if ((mobMuerto instanceof Skeleton) && (momentoDelTia < 12000) &&
                    ((jugadorMataCon.getItem() instanceof SwordItem) || (jugadorMataCon.getItem() instanceof AxeItem))) {
                world.setBlock(blockpos, state, 512);
                assert Minecraft.getInstance().level != null;
                Minecraft.getInstance().level.setBlock(blockpos, state, 512);
                world.setBlock(blockpos.above(), Blocks.DIRT.defaultBlockState(), 512);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                world.removeBlock(blockpos.above(), true);
                BlockEntity chest = world.getBlockEntity(blockpos);
                for (int i = 0; i < nivelJugador; i++) { // Bucle que llama al método
                    crearEntity(chest, jugadorMataCon, i);
                }
                System.out.println("Cofre creado!");
            }
            assert Minecraft.getInstance().level != null;
            Minecraft.getInstance().level.setBlock(blockpos.above(), Blocks.DIRT.defaultBlockState(), 512);
            world.setBlock(blockpos.above(), Blocks.DIRT.defaultBlockState(), 512);
            world.removeBlock(blockpos.above(), true);
        }
    }

    public BlockState calcularTipoDeCofre(int rollCofre, int rollShulker) {
        return switch (rollCofre) {
            case 1 -> Blocks.ENDER_CHEST.defaultBlockState();
            case 2, 3, 4 -> calcularTipoShulker(rollShulker);
            case 5, 6, 7 -> Blocks.BARREL.defaultBlockState();
            default -> Blocks.CHEST.defaultBlockState();
        };
    }

    public BlockState calcularTipoShulker(int rollShulker) {
        return switch (rollShulker) {
            case 1 -> Blocks.SHULKER_BOX.defaultBlockState();
            case 2 -> Blocks.WHITE_SHULKER_BOX.defaultBlockState();
            case 3 -> Blocks.LIGHT_GRAY_SHULKER_BOX.defaultBlockState();
            case 4 -> Blocks.GRAY_SHULKER_BOX.defaultBlockState();
            case 5 -> Blocks.BLACK_SHULKER_BOX.defaultBlockState();
            case 6 -> Blocks.BROWN_SHULKER_BOX.defaultBlockState();
            case 7 -> Blocks.RED_SHULKER_BOX.defaultBlockState();
            case 8 -> Blocks.ORANGE_SHULKER_BOX.defaultBlockState();
            case 9 -> Blocks.YELLOW_SHULKER_BOX.defaultBlockState();
            case 10 -> Blocks.LIME_SHULKER_BOX.defaultBlockState();
            case 11 -> Blocks.GREEN_SHULKER_BOX.defaultBlockState();
            case 12 -> Blocks.CYAN_SHULKER_BOX.defaultBlockState();
            case 13 -> Blocks.LIGHT_BLUE_SHULKER_BOX.defaultBlockState();
            case 14 -> Blocks.BLUE_SHULKER_BOX.defaultBlockState();
            case 15 -> Blocks.PURPLE_SHULKER_BOX.defaultBlockState();
            case 16 -> Blocks.MAGENTA_SHULKER_BOX.defaultBlockState();
            default -> Blocks.PINK_SHULKER_BOX.defaultBlockState();
        };
    }

    public void crearEntity(BlockEntity entity, ItemStack jugadorMataCon, int iteracion) {
        Random random = new Random();
        if (entity instanceof ChestBlockEntity chest) {
            ItemStack item = new ItemStack(calcularItem());
            item.setCount(random.nextInt(calcularCalidadArma(jugadorMataCon.getItem())) + 1);
            chest.setItem(iteracion, item);
        } else if (entity instanceof ShulkerBoxBlockEntity chest) {
            ItemStack item = new ItemStack(calcularItem());
            item.setCount(random.nextInt(calcularCalidadArma(jugadorMataCon.getItem())) + 1);
            chest.setItem(iteracion, item);
        } else if (entity instanceof BarrelBlockEntity chest) {
            ItemStack item = new ItemStack(calcularItem());
            item.setCount(random.nextInt(calcularCalidadArma(jugadorMataCon.getItem())) + 1);
            chest.setItem(iteracion, item);
        }   // No hace falta crearlo para el EnderChest porque no se le pueden settear items.
            // Para el EnderChest solo aparece el EnderChest ya que dentro tendrá lo que tú hayas metido antes.
    }

    public int calcularCalidadArma(Item arma) {
        if (arma.equals(Items.WOODEN_AXE) || arma.equals(Items.WOODEN_SWORD)) {
            return 1;
        } else if (arma.equals(Items.STONE_AXE) || arma.equals(Items.STONE_SWORD)) {
            return 2;
        } else if (arma.equals(Items.IRON_AXE) || arma.equals(Items.IRON_SWORD)) {
            return 3;
        } else if (arma.equals(Items.GOLDEN_AXE) || arma.equals(Items.GOLDEN_SWORD)) {
            return 4;
        } else if (arma.equals(Items.DIAMOND_AXE) || arma.equals(Items.DIAMOND_SWORD)) {
            return 5;
        } else if (arma.equals(Items.NETHERITE_AXE) || arma.equals(Items.NETHERITE_SWORD)) {
            return 6;
        }
        return -1;
    }

    public Item calcularItem() {
        // Arays con los objetos que pueden tocar y sus rarezas
        Item[] arrayExtremadamenteRaros = {Items.ELYTRA, Items.TRIDENT};

        Item[] arrayMuyRaros = {Items.DIAMOND_BLOCK, Items.EMERALD_BLOCK, Items.TOTEM_OF_UNDYING, Items.NETHER_STAR, Items.DIAMOND_HORSE_ARMOR, Items.DRAGON_EGG,
                Items.END_CRYSTAL, Items.ENCHANTED_GOLDEN_APPLE, Items.HEART_OF_THE_SEA, Items.BLUE_SHULKER_BOX};

        Item[] arrayRaros = {Items.DIAMOND, Items.EMERALD, Items.SADDLE, Items.DRAGON_BREATH, Items.IRON_HORSE_ARMOR, Items.GOLDEN_HORSE_ARMOR, Items.EMERALD,
                Items.IRON_BLOCK, Items.LAPIS_BLOCK, Items.GOLD_BLOCK, Items.TURTLE_HELMET, Items.GLOWSTONE, Items.ENDER_PEARL, Items.OBSIDIAN, Items.GHAST_TEAR,
                Items.BLAZE_ROD, Items.MAGMA_CREAM, Items.NETHER_WART, Items.PRISMARINE_SHARD, Items.PRISMARINE_CRYSTALS, Items.SEA_LANTERN, Items.SHULKER_SHELL,
                Items.IRON_NUGGET, Items.GOLD_NUGGET, Items.IRON_SWORD, Items.IRON_AXE, Items.IRON_SHOVEL, Items.DIAMOND_SWORD, Items.DIAMOND_AXE,
                Items.DIAMOND_SHOVEL, Items.DIAMOND_PICKAXE, Items.EMERALD_BLOCK, Items.GOLD_BLOCK, Items.IRON_BLOCK, Items.REDSTONE_BLOCK, Items.SLIME_BLOCK,
                Items.HONEY_BLOCK, Items.BREWING_STAND, Items.CAULDRON, Items.HOPPER, Items.PISTON, Items.DISPENSER, Items.DROPPER, Items.TRIPWIRE_HOOK, Items.OAK_BOAT,
                Items.CROSSBOW, Items.SHIELD, Items.CAMPFIRE, Items.LEAD, Items.TNT, Items.TNT_MINECART};

        Item[] arrayNormales = {Items.REDSTONE, Items.LAPIS_LAZULI, Items.IRON_PICKAXE, Items.GOLDEN_APPLE, Items.EXPERIENCE_BOTTLE, Items.FISHING_ROD,
                Items.BOW, Items.IRON_INGOT, Items.GOLD_INGOT, Items.BEACON, Items.SHIELD, Items.NAUTILUS_SHELL};

        Item[] arrayComunes = {Items.COOKED_BEEF, Items.PUMPKIN_PIE, Items.ARROW, Items.COAL, Items.WHEAT_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS,
                Items.MELON_SEEDS, Items.COCOA_BEANS, Items.CARROT, Items.POTATO, Items.SWEET_BERRIES, Items.CAKE, Items.STONE, Items.DIRT, Items.COBBLESTONE,
                Items.WARPED_FENCE, Items.OAK_LOG, Items.BIRCH_LOG, Items.SPRUCE_LOG, Items.JUNGLE_LOG, Items.ACACIA_LOG, Items.DARK_OAK_LOG, Items.SAND,
                Items.GRAVEL, Items.COAL, Items.IRON_INGOT, Items.GOLD_INGOT, Items.STICK, Items.STRING, Items.FEATHER, Items.LEATHER, Items.BEEF, Items.PORKCHOP,
                Items.CHICKEN, Items.EGG, Items.SUGAR, Items.WHEAT, Items.PAPER, Items.BOOK, Items.GLASS, Items.BRICK, Items.CLAY_BALL, Items.BONE, Items.GUNPOWDER};

        Random random = new Random();
        int roll = random.nextInt(1000000) + 1;
        if (roll == 1) {
            return arrayExtremadamenteRaros[random.nextInt(arrayExtremadamenteRaros.length)];
        } else if (roll <= 11){
            return arrayMuyRaros[random.nextInt(arrayMuyRaros.length)];
        } else if (roll <= 1111) {
            return arrayRaros[random.nextInt(arrayRaros.length)];
        } else if (roll <= 111111) {
            return arrayNormales[random.nextInt(arrayNormales.length)];
        } else {
            return arrayComunes[random.nextInt(arrayComunes.length)];
        }
    }
}
