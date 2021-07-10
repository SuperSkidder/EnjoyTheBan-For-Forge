/*
 * Decompiled with CFR 0_132.
 */
package com.enjoytheban.module.modules.player;

import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import java.awt.Color;
import java.util.Random;

public class Dab
extends Module {
    public Dab() {
        super("Dab", new String[]{"dab"}, ModuleType.Player);
        this.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)).getRGB());
        this.setRemoved(false);
    }
}

