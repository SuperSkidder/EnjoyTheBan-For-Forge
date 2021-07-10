/*
 * Decompiled with CFR 0_132.
 */
package com.enjoytheban;

import com.enjoytheban.api.value.Value;
import com.enjoytheban.management.CommandManager;
import com.enjoytheban.management.FileManager;
import com.enjoytheban.management.FriendManager;
import com.enjoytheban.management.ModuleManager;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.modules.render.UI.TabUI;
import com.enjoytheban.ui.login.AltManager;
import java.io.PrintStream;
import java.time.OffsetDateTime;
import java.util.List;
import net.minecraft.util.ResourceLocation;

public class Client {
    public final String name = "ETB";
    public final double version = 0.6;
    public static boolean publicMode = false;
    public static Client instance = new Client();
    private ModuleManager modulemanager;
    private CommandManager commandmanager;
    private AltManager altmanager;
    private FriendManager friendmanager;
    private TabUI tabui;
    public static ResourceLocation CLIENT_CAPE = new ResourceLocation("ETB/cape.png");

    public void initiate() {
        this.commandmanager = new CommandManager();
        this.commandmanager.init();
        this.friendmanager = new FriendManager();
        this.friendmanager.init();
        this.modulemanager = new ModuleManager();
        this.modulemanager.init();
        this.tabui = new TabUI();
        this.tabui.init();
        this.altmanager = new AltManager();
        AltManager.init();
        AltManager.setupAlts();
        FileManager.init();
    }

    public ModuleManager getModuleManager() {
        return this.modulemanager;
    }

    public CommandManager getCommandManager() {
        return this.commandmanager;
    }

    public AltManager getAltManager() {
        return this.altmanager;
    }

    public void shutDown() {
        String values = "";
        instance.getModuleManager();
        for (Module m : ModuleManager.getModules()) {
            for (Value v : m.getValues()) {
                values = String.valueOf(values) + String.format("%s:%s:%s%s", m.getName(), v.getName(), v.getValue(), System.lineSeparator());
            }
        }
        FileManager.save("Values.txt", values, false);
        String enabled = "";
        instance.getModuleManager();
        for (Module m : ModuleManager.getModules()) {
            if (!m.isEnabled()) continue;
            enabled = String.valueOf(enabled) + String.format("%s%s", m.getName(), System.lineSeparator());
        }
        FileManager.save("Enabled.txt", enabled, false);
    }
}

