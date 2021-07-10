/*
 * Decompiled with CFR 0_132.
 */
package com.enjoytheban.module.modules.render;

import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;

public class Xray
extends Module {
    public List<Integer> blocks = new ArrayList<Integer>();

    public Xray() {
        super("Xray", new String[]{"xrai", "oreesp"}, ModuleType.Render);
        this.setColor(Color.GREEN.getRGB());
        this.blocks.add(16);
        this.blocks.add(56);
        this.blocks.add(14);
        this.blocks.add(15);
        this.blocks.add(129);
        this.blocks.add(73);
    }

    @Override
    public void onEnable() {
        this.mc.renderGlobal.loadRenderers();
    }

    @Override
    public void onDisable() {
        this.mc.renderGlobal.loadRenderers();
    }

    public List<Integer> getBlocks() {
        return this.blocks;
    }
}

