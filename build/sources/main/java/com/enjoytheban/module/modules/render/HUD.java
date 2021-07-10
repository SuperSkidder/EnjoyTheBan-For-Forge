/*
 * Decompiled with CFR 0_132.
 */
package com.enjoytheban.module.modules.render;

import com.enjoytheban.Client;
import com.enjoytheban.api.AALAPI;
import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.rendering.EventRender2D;
import com.enjoytheban.api.value.Option;
import com.enjoytheban.api.value.Value;
import com.enjoytheban.management.FriendManager;
import com.enjoytheban.management.ModuleManager;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import com.enjoytheban.module.modules.render.UI.TabUI;
import com.enjoytheban.ui.font.CFontRenderer;
import com.enjoytheban.ui.font.FontLoaders;
import com.enjoytheban.utils.Helper;
import com.enjoytheban.utils.math.RotationUtil;
import com.enjoytheban.utils.render.RenderUtil;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class HUD
extends Module {
    public TabUI tabui;
    private Option<Boolean> info = new Option<Boolean>("Information", "information", true);
    private Option<Boolean> rainbow = new Option<Boolean>("Rainbow", "rainbow", false);
    private Option<Boolean> customlogo = new Option<Boolean>("Logo", "logo", false);
    private Option<Boolean> customfont = new Option<Boolean>("Font", "font", false);
    private Option<Boolean> capes = new Option<Boolean>("Capes", "capes", true);
    public static boolean shouldMove;
    public static boolean useFont;
    private String[] directions = new String[]{"S", "SW", "W", "NW", "N", "NE", "E", "SE"};

    public HUD() {
        super("HUD", new String[]{"gui"}, ModuleType.Render);
        this.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)).getRGB());
        this.setEnabled(true);
        this.setRemoved(true);
        this.addValues(this.info, this.rainbow, this.customlogo, this.customfont, this.capes);
    }

    @EventHandler
    private void renderHud(EventRender2D event) {
        CFontRenderer font = FontLoaders.kiona18;
        if (this.customfont.getValue().booleanValue()) {
            useFont = true;
        } else if (!this.customfont.getValue().booleanValue()) {
            useFont = false;
        }
        if (!this.mc.gameSettings.showDebugInfo) {
            String name;
            String direction;
            if (Helper.onServer("enjoytheban")) {
                int ychat;
                boolean cheating = true;
                if (useFont) {
                    ychat = this.mc.ingameGUI.getChatGUI().getChatOpen() ? 14 : 0;
                    font.drawStringWithShadow("Username:\u00a7f " + AALAPI.getUsername(), new ScaledResolution(this.mc).getScaledWidth() - font.getStringWidth("Username: " + AALAPI.getUsername()) - 2, new ScaledResolution(this.mc).getScaledHeight() - 20 - ychat, new Color(102, 172, 255, 1).getRGB());
                    font.drawStringWithShadow("ETB Battle Royale:\u00a7f " + (cheating ? "Cheating" : "Legit"), new ScaledResolution(this.mc).getScaledWidth() - font.getStringWidth("ETB Battle Royale: " + (cheating ? "Cheating" : "Legit")) - 2, new ScaledResolution(this.mc).getScaledHeight() - 10 - ychat, new Color(102, 172, 255, 1).getRGB());
                } else {
                    ychat = this.mc.ingameGUI.getChatGUI().getChatOpen() ? 14 : 0;
                    this.mc.fontRendererObj.drawStringWithShadow("Username:\u00a7f " + AALAPI.getUsername(), new ScaledResolution(this.mc).getScaledWidth() - this.mc.fontRendererObj.getStringWidth("Username: " + AALAPI.getUsername()) - 2, new ScaledResolution(this.mc).getScaledHeight() - 20 - ychat, new Color(102, 172, 255, 1).getRGB());
                    this.mc.fontRendererObj.drawStringWithShadow("ETB Battle Royale:\u00a7f " + (cheating ? "Cheating" : "Legit"), new ScaledResolution(this.mc).getScaledWidth() - this.mc.fontRendererObj.getStringWidth("ETB Battle Royale: " + (cheating ? "Cheating" : "Legit")) - 2, new ScaledResolution(this.mc).getScaledHeight() - 10 - ychat, new Color(102, 172, 255, 1).getRGB());
                }
            }
            if ((boolean)this.customlogo.getValue()) {
                HUD.shouldMove = true;
                GlStateManager.enableAlpha();
                GlStateManager.enableBlend();
                RenderUtil.drawCustomImage(-4, -4, 35, 33, new ResourceLocation("ETB/logo.png"));
                GlStateManager.disableAlpha();
                GlStateManager.disableBlend();
                if (HUD.useFont) {
                    final CFontRenderer cFontRenderer = font;
                    final StringBuilder append = new StringBuilder().append(EnumChatFormatting.GRAY);
                    Client.instance.getClass();
                    final String string = append.append(0.6).toString();
                    final CFontRenderer cFontRenderer2 = font;
                    Client.instance.getClass();
                    final StringBuilder append2 = new StringBuilder(String.valueOf("ETB")).append(" ");
                    Client.instance.getClass();
                    cFontRenderer.drawStringWithShadow(string, cFontRenderer2.getStringWidth(append2.append(0.6).toString()) - 9, HUD.shouldMove ? 15 : 2, new Color(102, 172, 255).getRGB());
                }
                else {
                    final FontRenderer fontRendererObj = this.mc.fontRendererObj;
                    final StringBuilder append3 = new StringBuilder().append(EnumChatFormatting.GRAY);
                    Client.instance.getClass();
                    final String string2 = append3.append(0.6).toString();
                    final FontRenderer fontRendererObj2 = this.mc.fontRendererObj;
                    Client.instance.getClass();
                    final StringBuilder append4 = new StringBuilder(String.valueOf("ETB")).append(" ");
                    Client.instance.getClass();
                    fontRendererObj.drawStringWithShadow(string2, fontRendererObj2.getStringWidth(append4.append(0.6).toString()) - 12, HUD.shouldMove ? 15 : 2, new Color(102, 172, 255).getRGB());
                }
            }
            else if (!(boolean)this.customlogo.getValue()) {
                HUD.shouldMove = false;
                if (HUD.useFont) {
                    final CFontRenderer cFontRenderer3 = font;
                    Client.instance.getClass();
                    final StringBuilder append5 = new StringBuilder(String.valueOf("ETB")).append(" ").append(EnumChatFormatting.GRAY);
                    Client.instance.getClass();
                    cFontRenderer3.drawStringWithShadow(append5.append(0.6).toString(), 2.0, 2.0, new Color(102, 172, 255).getRGB());
                }
                else {
                    final FontRenderer fontRendererObj3 = this.mc.fontRendererObj;
                    Client.instance.getClass();
                    final StringBuilder append6 = new StringBuilder(String.valueOf("ETB")).append(" ").append(EnumChatFormatting.GRAY);
                    Client.instance.getClass();
                    fontRendererObj3.drawStringWithShadow(append6.append(0.6).toString(), 2.0f, 2.0f, new Color(102, 172, 255).getRGB());
                }
            }
            ArrayList<Module> sorted = new ArrayList<Module>();
            Client.instance.getModuleManager();
            for (Module m : ModuleManager.getModules()) {
                if (!m.isEnabled() || m.wasRemoved()) continue;
                sorted.add(m);
            }
            if (useFont) {
                sorted.sort((o1, o2) -> font.getStringWidth(o2.getSuffix().isEmpty() ? o2.getName() : String.format("%s %s", o2.getName(), o2.getSuffix())) - font.getStringWidth(o1.getSuffix().isEmpty() ? o1.getName() : String.format("%s %s", o1.getName(), o1.getSuffix())));
            } else {
                sorted.sort((o1, o2) -> this.mc.fontRendererObj.getStringWidth(o2.getSuffix().isEmpty() ? o2.getName() : String.format("%s %s", o2.getName(), o2.getSuffix())) - this.mc.fontRendererObj.getStringWidth(o1.getSuffix().isEmpty() ? o1.getName() : String.format("%s %s", o1.getName(), o1.getSuffix())));
            }
            int y = 1;
            int rainbowTick = 0;
            if (useFont) {
                for (Module m : sorted) {
                    name = m.getSuffix().isEmpty() ? m.getName() : String.format("%s %s", m.getName(), m.getSuffix());
                    float x = RenderUtil.width() - font.getStringWidth(name);
                    Color rainbow = new Color(Color.HSBtoRGB((float)((double)this.mc.thePlayer.ticksExisted / 50.0 + Math.sin((double)rainbowTick / 50.0 * 1.6)) % 1.0f, 0.5f, 1.0f));
                    font.drawStringWithShadow(name, x - 3.0f, y + 1, this.rainbow.getValue() != false ? rainbow.getRGB() : m.getColor());
                    if (++rainbowTick > 50) {
                        rainbowTick = 0;
                    }
                    y += 9;
                }
            } else {
                for (Module m : sorted) {
                    name = m.getSuffix().isEmpty() ? m.getName() : String.format("%s %s", m.getName(), m.getSuffix());
                    float x = RenderUtil.width() - this.mc.fontRendererObj.getStringWidth(name);
                    Color rainbow = new Color(Color.HSBtoRGB((float)((double)this.mc.thePlayer.ticksExisted / 50.0 + Math.sin((double)rainbowTick / 50.0 * 1.6)) % 1.0f, 0.5f, 1.0f));
                    this.mc.fontRendererObj.drawStringWithShadow(name, x - 2.0f, y, this.rainbow.getValue() != false ? rainbow.getRGB() : m.getColor());
                    if (++rainbowTick > 50) {
                        rainbowTick = 0;
                    }
                    y += 9;
                }
            }
            String text = (Object)((Object)EnumChatFormatting.GRAY) + "X" + (Object)((Object)EnumChatFormatting.WHITE) + ": " + MathHelper.floor_double(this.mc.thePlayer.posX) + " " + (Object)((Object)EnumChatFormatting.GRAY) + "Y" + (Object)((Object)EnumChatFormatting.WHITE) + ": " + MathHelper.floor_double(this.mc.thePlayer.posY) + " " + (Object)((Object)EnumChatFormatting.GRAY) + "Z" + (Object)((Object)EnumChatFormatting.WHITE) + ": " + MathHelper.floor_double(this.mc.thePlayer.posZ);
            if (useFont) {
                int ychat;
                int n = ychat = this.mc.ingameGUI.getChatGUI().getChatOpen() ? 24 : 10;
                if (this.info.getValue().booleanValue()) {
                    font.drawStringWithShadow(text, 4.0, new ScaledResolution(this.mc).getScaledHeight() - ychat, new Color(11, 12, 17).getRGB());
                    font.drawStringWithShadow((Object)((Object)EnumChatFormatting.GRAY) + "FPS: " + (Object)((Object)EnumChatFormatting.WHITE) + Minecraft.getDebugFPS(), 2.0, shouldMove ? 90 : 75, -1);
                    this.drawPotionStatus(new ScaledResolution(this.mc));
                    direction = this.directions[RotationUtil.wrapAngleToDirection(this.mc.thePlayer.rotationYaw, this.directions.length)];
                    Client.instance.getClass();
                    Client.instance.getClass();
                    font.drawStringWithShadow("[" + direction + "]", font.getStringWidth(String.valueOf("ETB") + " " + 0.6 + 10), shouldMove ? 15 : 2, new Color(102, 172, 255).getRGB());
                }
            } else {
                int ychat;
                int n = ychat = this.mc.ingameGUI.getChatGUI().getChatOpen() ? 25 : 10;
                if (this.info.getValue().booleanValue()) {
                    this.mc.fontRendererObj.drawStringWithShadow(text, 4.0f, new ScaledResolution(this.mc).getScaledHeight() - ychat, new Color(11, 12, 17).getRGB());
                    this.mc.fontRendererObj.drawStringWithShadow((Object)((Object)EnumChatFormatting.GRAY) + "FPS: " + (Object)((Object)EnumChatFormatting.WHITE) + Minecraft.getDebugFPS(), 2.0f, shouldMove ? 90 : 75, -1);
                    this.drawPotionStatus(new ScaledResolution(this.mc));
                    direction = this.directions[RotationUtil.wrapAngleToDirection(this.mc.thePlayer.rotationYaw, this.directions.length)];
                    Client.instance.getClass();
                    Client.instance.getClass();
                    this.mc.fontRendererObj.drawStringWithShadow("[" + direction + "]", this.mc.fontRendererObj.getStringWidth(String.valueOf("ETB") + " " + 0.6 + 2), shouldMove ? 15 : 2, new Color(102, 172, 255).getRGB());
                }
            }
        }
    }

    private void drawPotionStatus(ScaledResolution sr) {
        CFontRenderer font = FontLoaders.kiona18;
        int y = 0;
        for (PotionEffect effect : this.mc.thePlayer.getActivePotionEffects()) {
            int ychat;
            Potion potion = Potion.potionTypes[effect.getPotionID()];
            String PType = I18n.format(potion.getName(), new Object[0]);
            switch (effect.getAmplifier()) {
                case 1: {
                    PType = String.valueOf(PType) + " II";
                    break;
                }
                case 2: {
                    PType = String.valueOf(PType) + " III";
                    break;
                }
                case 3: {
                    PType = String.valueOf(PType) + " IV";
                    break;
                }
            }
            if (effect.getDuration() < 600 && effect.getDuration() > 300) {
                PType = String.valueOf(PType) + "\u00a77:\u00a76 " + Potion.getDurationString(effect);
            } else if (effect.getDuration() < 300) {
                PType = String.valueOf(PType) + "\u00a77:\u00a7c " + Potion.getDurationString(effect);
            } else if (effect.getDuration() > 600) {
                PType = String.valueOf(PType) + "\u00a77:\u00a77 " + Potion.getDurationString(effect);
            }
            int n = ychat = this.mc.ingameGUI.getChatGUI().getChatOpen() ? 5 : -10;
            if (useFont) {
                font.drawStringWithShadow(PType, sr.getScaledWidth() - font.getStringWidth(PType) - 2, sr.getScaledHeight() - font.getHeight() + y - 12 - ychat, potion.getLiquidColor());
            } else {
                this.mc.fontRendererObj.drawStringWithShadow(PType, sr.getScaledWidth() - this.mc.fontRendererObj.getStringWidth(PType) - 2, sr.getScaledHeight() - this.mc.fontRendererObj.FONT_HEIGHT + y - 12 - ychat, potion.getLiquidColor());
            }
            y -= 10;
        }
    }

}

