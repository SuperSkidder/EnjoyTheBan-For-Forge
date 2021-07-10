/*
 * Decompiled with CFR 0_132.
 */
package com.enjoytheban.api.value;

import com.enjoytheban.api.value.Value;

public class Option<V>
extends Value<V> {
    public Option(String displayName, String name, V enabled) {
        super(displayName, name);
        this.setValue(enabled);
    }
}

