/*
 * Decompiled with CFR 0_132.
 */
package com.enjoytheban.module.modules.combat;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.world.EventPreUpdate;
import com.enjoytheban.api.value.Option;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.FoodStats;

public class Regen
extends Module {
    private Option<Boolean> guardian = new Option<Boolean>("Guardian", "guardian", true);

    public Regen() {
        super("Regen", new String[]{"fastgen"}, ModuleType.Combat);
        this.setColor(new Color(208, 30, 142).getRGB());
    }

    @EventHandler
    private void onUpdate(EventPreUpdate event) {
        if (this.mc.thePlayer.onGround && (double)this.mc.thePlayer.getHealth() < 16.0 && this.mc.thePlayer.getFoodStats().getFoodLevel() > 17 && this.mc.thePlayer.isCollidedVertically) {
            int i = 0;
            while (i < 60) {
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 1.0E-9, this.mc.thePlayer.posZ, this.mc.thePlayer.rotationYaw, this.mc.thePlayer.rotationPitch, true));
                this.mc.thePlayer.motionX = 0.0;
                this.mc.thePlayer.motionZ = 0.0;
                ++i;
            }
            if (this.guardian.getValue().booleanValue() && this.mc.thePlayer.ticksExisted % 3 == 0) {
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 999.0, this.mc.thePlayer.posZ, true));
            }
            this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
    }
}

