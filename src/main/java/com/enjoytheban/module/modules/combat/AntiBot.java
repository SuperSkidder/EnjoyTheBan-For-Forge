/*
 * Decompiled with CFR 0_132.
 */
package com.enjoytheban.module.modules.combat;

import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import com.enjoytheban.utils.Helper;
import java.awt.Color;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IChatComponent;

public class AntiBot
extends Module {
    public AntiBot() {
        super("AntiBot", new String[]{"nobot", "botkiller"}, ModuleType.Combat);
        this.setColor(new Color(217, 149, 251).getRGB());
    }

    public boolean isServerBot(Entity entity) {
        if (this.isEnabled()) {
            if (Helper.onServer("hypixel")) {
                if (entity.getDisplayName().getFormattedText().startsWith("\u00a7") && !entity.isInvisible() && !entity.getDisplayName().getFormattedText().toLowerCase().contains("npc")) {
                    return false;
                }
                return true;
            }
            if (Helper.onServer("mineplex")) {
                for (Object object : this.mc.theWorld.playerEntities) {
                    EntityPlayer entityPlayer = (EntityPlayer)object;
                    if (entityPlayer == null || entityPlayer == this.mc.thePlayer || !entityPlayer.getName().startsWith("Body #") && entityPlayer.getMaxHealth() != 20.0f) continue;
                    return true;
                }
            }
        }
        return false;
    }
}

