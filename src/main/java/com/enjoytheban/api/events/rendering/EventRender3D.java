/*
 * Decompiled with CFR 0_132.
 */
package com.enjoytheban.api.events.rendering;

import com.enjoytheban.api.Event;

public class EventRender3D
extends Event {
    private float ticks;

    public EventRender3D() {

    }

    public EventRender3D(float ticks) {
        this.ticks = ticks;
    }

    public float getPartialTicks() {
        return this.ticks;
    }
}

