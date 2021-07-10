/*
 * Decompiled with CFR 0_132.
 */
package com.enjoytheban.api.events.misc;

import com.enjoytheban.api.Event;

public class EventChat
extends Event {
    private String message;

    public EventChat(String message) {
        this.message = message;
        this.setType((byte)0);
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

