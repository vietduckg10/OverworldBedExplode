package com.ducvn.overworldbedexplode.events;

import com.ducvn.overworldbedexplode.OverworldBedExplodeMod;
import com.ducvn.overworldbedexplode.config.OverworldBedExplodeConfig;
import net.minecraft.block.BlockState;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.ExplosionContext;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = OverworldBedExplodeMod.MODID)
public class OverworldBedExplodeEvent {

    @SubscribeEvent
    public static void OverworldBedExplode(PlayerInteractEvent.RightClickBlock event) {
        World world = event.getWorld();
        BlockState state = event.getWorld().getBlockState(event.getPos());
        String blocktag = state.getBlock().getTags().toString();
        if (!world.isClientSide){
            Boolean allDimension = OverworldBedExplodeConfig.aplly_on_all_dimensions.get();
            Boolean canExplode = true;
            if (!allDimension){
                if (world.dimension() != World.OVERWORLD){
                    canExplode = false;
                }
            }
            if (blocktag.equals("[minecraft:beds]") && canExplode){
                BlockPos pos = event.getPos();
                Double explosionChance = OverworldBedExplodeConfig.explosion_chance.get();
                Random roll = new Random();
                List<? extends String> mobList = OverworldBedExplodeConfig.mob_list.get();
                int mobNumber = OverworldBedExplodeConfig.mob_number.get();
                if (roll.nextDouble() <= explosionChance) {
                    Double teleportChance = OverworldBedExplodeConfig.teleport_random_player_chance.get();
                    List<PlayerEntity> playerList = new ArrayList<>(world.players());
                    if (teleportChance > 0 && playerList.size() > 1){
                        if (roll.nextDouble() <= teleportChance){
                            playerList.remove(event.getPlayer());
                            PlayerEntity unluckyPlayer = playerList.get(roll.nextInt(playerList.size()));
                            while (unluckyPlayer.level != event.getPlayer().level && playerList.size() > 0){
                                unluckyPlayer = playerList.get(roll.nextInt(playerList.size()));
                            }
                            if (unluckyPlayer.isAlive() && !unluckyPlayer.isCreative() && !unluckyPlayer.isSpectator()){
                                unluckyPlayer.setPos(pos.getX(), pos.getY(), pos.getZ());
                                ((ServerWorld) world).updateChunkPos(unluckyPlayer);
                            }
                        }
                    }
                    world.removeBlock(pos, false);
                    world.explode((Entity) null, DamageSource.badRespawnPointExplosion(), (ExplosionContext) null,
                            (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D,
                            (double) pos.getZ() + 0.5D, 5.0F, true, Explosion.Mode.DESTROY);
                    for (String mobName : mobList){
                        CompoundNBT nbt = new CompoundNBT();
                        nbt.putString("id", mobName);
                        roll = new Random();
                        for (int spawnNumber = roll.nextInt(mobNumber); spawnNumber < mobNumber; spawnNumber++){
                            EntityType.by(nbt).get().spawn((ServerWorld) world,null, null, pos, SpawnReason.EVENT, false, false);
                        }
                    }
                }
            }
        }
    }
}
