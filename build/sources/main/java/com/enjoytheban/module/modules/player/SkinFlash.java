/*
 * Decompiled with CFR 0_132.
 */
package com.enjoytheban.module.modules.player;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.world.EventTick;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import java.awt.Color;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EnumPlayerModelParts;

public class SkinFlash
extends Module {
    public SkinFlash() {
        super("SkinFlash", new String[]{"derpskin"}, ModuleType.Player);
        this.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)).getRGB());
    }

    @Override
    public void onDisable() {
        EnumPlayerModelParts[] parts;
        if (this.mc.thePlayer != null && (parts = EnumPlayerModelParts.values()) != null) {
            EnumPlayerModelParts[] arrayOfEnumPlayerModelParts1 = parts;
            int j = arrayOfEnumPlayerModelParts1.length;
            int i = 0;
            while (i < j) {
                EnumPlayerModelParts part = arrayOfEnumPlayerModelParts1[i];
                this.mc.gameSettings.setModelPartEnabled(part, true);
                ++i;
            }
        }
    }

    @EventHandler
    private void onTick(EventTick e) {
        EnumPlayerModelParts[] parts;
        if (this.mc.thePlayer != null && (parts = EnumPlayerModelParts.values()) != null) {
            EnumPlayerModelParts[] arrayOfEnumPlayerModelParts1 = parts;
            int j = arrayOfEnumPlayerModelParts1.length;
            int i = 0;
            while (i < j) {
                EnumPlayerModelParts part = arrayOfEnumPlayerModelParts1[i];
                boolean newState = this.isEnabled() ? random.nextBoolean() : true;
                this.mc.gameSettings.setModelPartEnabled(part, newState);
                ++i;
            }
        }
    }
}

