package com.autodeposit;

import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class DepositTickHandler {
    private static int tickCounter = 0;

    public static void register() {
        net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents.END_SERVER_TICK.register(server -> {
            tickCounter++;
            if (tickCounter < 20) return;
            tickCounter = 0;

            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                ServerWorld world = player.getServerWorld();
                BlockPos playerPos = player.getBlockPos();

                for (int dx = -1; dx <= 1; dx++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        BlockPos chestPos = playerPos.add(dx, 0, dz);
                        if (world.getBlockState(chestPos).getBlock() instanceof ChestBlock) {
                            BlockEntity entity = world.getBlockEntity(chestPos);
                            if (entity instanceof ChestBlockEntity chest) {
                                DepositHelper.tryDeposit(player.getInventory(), chest);
                            }
                        }
                    }
                }
            }
        });
    }
                              }
