/*
 * Decompiled with CFR 0_132.
 */
package com.enjoytheban.module.modules.world;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.world.EventPreUpdate;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import java.awt.Color;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.BlockPos;

public class AntiVoid
extends Module {
    public AntiVoid() {
        super("AntiVoid", new String[]{"novoid", "antifall"}, ModuleType.World);
        this.setColor(new Color(223, 233, 233).getRGB());
    }

    @EventHandler
    private void onUpdate(EventPreUpdate e) {
        boolean blockUnderneath = false;
        int i = 0;
        while ((double)i < this.mc.thePlayer.posY + 2.0) {
            BlockPos pos = new BlockPos(this.mc.thePlayer.posX, (double)i, this.mc.thePlayer.posZ);
            if (!(this.mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir)) {
                blockUnderneath = true;
            }
            ++i;
        }
        if (blockUnderneath) {
            return;
        }
        if (this.mc.thePlayer.fallDistance < 2.0f) {
            return;
        }
        if (!this.mc.thePlayer.onGround && !this.mc.thePlayer.isCollidedVertically) {
            this.mc.thePlayer.motionY += 0.07;
        }
    }
}

