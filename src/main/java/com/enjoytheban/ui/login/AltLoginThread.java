/*
 * Decompiled with CFR 0_132.
 */
package com.enjoytheban.ui.login;

import com.enjoytheban.Client;
import com.enjoytheban.management.FileManager;
import com.enjoytheban.ui.login.Alt;
import com.enjoytheban.ui.login.AltManager;
import com.enjoytheban.utils.mixin.MinecraftToMixin;
import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import java.net.Proxy;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class AltLoginThread
extends Thread {
    private final Minecraft mc = Minecraft.getMinecraft();
    private final String password;
    private String status;
    private final String username;

    public AltLoginThread(String username, String password) {
        super("Alt Login Thread");
        this.username = username;
        this.password = password;
        this.status = "\u00a7eWaiting...";
    }

    private final Session createSession(String username, String password) {
        YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
        auth.setUsername(username);
        auth.setPassword(password);
        try {
            auth.logIn();
            return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
        }
        catch (AuthenticationException authenticationException) {
            return null;
        }
    }

    public String getStatus() {
        return this.status;
    }

    @Override
    public void run() {
        if (this.password.equals("")) {
            MinecraftToMixin.mixMC.setSession(new Session(this.username, "", "", "mojang"));
            this.status = "\u00a7aLogged in. (" + this.username + " - offline name)";
            return;
        }
        this.status = "\u00a7eLogging in...";
        Session auth = this.createSession(this.username, this.password);
        if (auth == null) {
            this.status = "\u00a7cLogin failed!";
        } else {
            Client.instance.getAltManager().setLastAlt(new Alt(this.username, this.password));
            FileManager.saveLastAlt();
            this.status = "\u00a7aLogged in. (" + auth.getUsername() + ")";
            MinecraftToMixin.mixMC.setSession(auth);
        }
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

