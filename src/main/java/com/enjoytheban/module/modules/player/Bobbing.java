/*
 * Decompiled with CFR 0_132.
 */
package com.enjoytheban.module.modules.player;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.world.EventPreUpdate;
import com.enjoytheban.api.value.Numbers;
import com.enjoytheban.api.value.Value;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class Bobbing
extends Module {
    private Numbers<Double> boob = new Numbers<Double>("Amount", "Amount", 1.0, 0.1, 5.0, 0.5);

    public Bobbing() {
        super("Bobbing+", new String[]{"bobbing+"}, ModuleType.Player);
        this.addValues(this.boob);
    }

    @EventHandler
    public void onUpdate(EventPreUpdate event) {
        this.setColor(new Color(20, 200, 100).getRGB());
        if (this.mc.thePlayer.onGround) {
            this.mc.thePlayer.cameraYaw = (float)(0.09090908616781235 * this.boob.getValue());
        }
    }
}

