package me.tinywifi.trollwalk.mixin;

import me.tinywifi.trollwalk.TrollWalk;
import me.tinywifi.trollwalk.util.MovementFix;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Inject(at = @At("HEAD"), method = "send(Lnet/minecraft/network/Packet;Lnet/minecraft/network/PacketCallbacks;)V", cancellable = true)
    private void send(Packet<?> unknownPacket, PacketCallbacks unknownCallback, CallbackInfo info) {
        if (TrollWalk.enabled) {
            if (unknownPacket instanceof PlayerMoveC2SPacket packet) {
                if (!MovementFix.isBad(packet)) return;
                info.cancel();
                PlayerMoveC2SPacket fixedPacket = MovementFix.fix(packet);
                if (MovementFix.isBad(fixedPacket)) return;
                Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).sendPacket(fixedPacket);
            }
            if (unknownPacket instanceof VehicleMoveC2SPacket packet) {
                if (!MovementFix.isBad(packet)) return;
                info.cancel();
                VehicleMoveC2SPacket fixedPacket = MovementFix.fix(packet);
                if (MovementFix.isBad(fixedPacket)) return;
                Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).sendPacket(fixedPacket);
            }
        }
    }
}