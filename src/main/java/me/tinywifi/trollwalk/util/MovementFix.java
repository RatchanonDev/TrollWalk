package me.tinywifi.trollwalk.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MovementFix {
    public static double fix(double d) {
        return new BigDecimal(d).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public static PlayerMoveC2SPacket fix(PlayerMoveC2SPacket packet) {
        if (packet instanceof PlayerMoveC2SPacket.Full)
            return new PlayerMoveC2SPacket.Full(
                    fix(packet.getX(0)),
                    packet.getY(0),
                    fix(packet.getZ(0)),
                    packet.getYaw(0),
                    packet.getPitch(0),
                    packet.isOnGround());

        if (packet instanceof PlayerMoveC2SPacket.PositionAndOnGround)
            return new PlayerMoveC2SPacket.PositionAndOnGround(
                    fix(packet.getX(0)),
                    packet.getY(0),
                    fix(packet.getZ(0)),
                    packet.isOnGround());

        return packet;
    }

    public static VehicleMoveC2SPacket fix(VehicleMoveC2SPacket packet) {
        MinecraftClient instance = MinecraftClient.getInstance();
        Entity entity = instance.player.getVehicle();
        entity.setPos(fix(entity.getX()), entity.getY(), fix(entity.getZ()));
        return new VehicleMoveC2SPacket(entity);
    }

    public static Boolean isBad(double d) {
        return ((long)(d * 1000) % 10) != 0;
    }

    public static Boolean isBad(PlayerMoveC2SPacket packet) {
        return isBad(packet.getX(0)) || isBad(packet.getZ(0));
    }

    public static Boolean isBad(VehicleMoveC2SPacket packet) {
        MinecraftClient instance = MinecraftClient.getInstance();
        Entity entity = instance.player.getVehicle();
        return isBad(entity.getX()) || isBad(entity.getZ());
    }
}