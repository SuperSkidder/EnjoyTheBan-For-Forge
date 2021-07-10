/*
 * Decompiled with CFR 0_132.
 */
package com.enjoytheban.module.modules.player;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.misc.EventChat;
import com.enjoytheban.management.FriendManager;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import java.awt.Color;
import java.util.Random;
import java.util.function.BiConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class AutoAccept
extends Module {
    public AutoAccept() {
        super("AutoAccept", new String[]{"TPAccept, autotp"}, ModuleType.Player);
        this.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)).getRGB());
    }

    @EventHandler
    private void onPacket(EventChat e) {
        if (e.getType() == 0) {
            String message = e.getMessage();
            if (this.gotTpaRequest(message)) {
                this.handleRequest(message, "/tpaccept", "Accepted teleport");
            }
            if (this.gotPartyRequest(message)) {
                this.handleRequest(message, "/party accept", "Accepted party invite");
            }
            if (this.gotFactionRequest(message)) {
                this.handleRequest(message, "/f join", "Accepted faction invitation");
            }
        }
    }

    private void handleRequest(String message, String messageToSend, String notificationMessage) {
        FriendManager.getFriends().forEach((friends, alias) -> {
            if (FriendManager.isFriend(FriendManager.getAlias(friends)) && message.contains(FriendManager.getAlias(friends))) {
                this.mc.thePlayer.sendChatMessage(String.valueOf(messageToSend) + " " + FriendManager.getAlias(friends));
            }
        });
    }

    private boolean gotTpaRequest(String message) {
        if (!((message = message.toLowerCase()).contains("has requested to teleport") || message.contains("to teleport to you") || message.contains("has requested that you teleport to them"))) {
            return false;
        }
        return true;
    }

    private boolean gotFactionRequest(String message) {
        if ((message = message.toLowerCase()).contains("has invited you to join") && !message.contains("party")) {
            return true;
        }
        return false;
    }

    private boolean gotPartyRequest(String message) {
        message = message.toLowerCase();
        return message.contains("has invited you to join their party");
    }
}

