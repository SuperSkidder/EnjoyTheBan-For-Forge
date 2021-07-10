/*
 * Decompiled with CFR 0_132.
 */
package com.enjoytheban.module.modules.render.UI;

import com.enjoytheban.Client;
import com.enjoytheban.api.EventBus;
import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.misc.EventKey;
import com.enjoytheban.api.events.rendering.EventRender2D;
import com.enjoytheban.api.value.Mode;
import com.enjoytheban.api.value.Numbers;
import com.enjoytheban.api.value.Option;
import com.enjoytheban.api.value.Value;
import com.enjoytheban.management.Manager;
import com.enjoytheban.management.ModuleManager;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import com.enjoytheban.module.modules.render.HUD;
import com.enjoytheban.ui.font.CFontRenderer;
import com.enjoytheban.ui.font.FontLoaders;
import com.enjoytheban.utils.Helper;
import com.enjoytheban.utils.math.MathUtil;
import com.enjoytheban.utils.render.RenderUtil;
import java.awt.Color;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.settings.GameSettings;

public class TabUI
implements Manager {
    private Section section = Section.TYPES;
    private ModuleType selectedType = ModuleType.values()[0];
    private Module selectedModule = null;
    private Value selectedValue = null;
    private int currentType = 0;
    private int currentModule = 0;
    private int currentValue = 0;
    private int height = 12;
    private int maxType;
    private int maxModule;
    private int maxValue;
    private static /* synthetic */ int[] $SWITCH_TABLE$com$enjoytheban$module$modules$render$UI$TabUI$Section;

    @Override
    public void init() {
        ModuleType[] arrmoduleType = ModuleType.values();
        int n = arrmoduleType.length;
        int n2 = 0;
        while (n2 < n) {
            ModuleType mt = arrmoduleType[n2];
            if (this.maxType <= Helper.mc.fontRendererObj.getStringWidth(mt.name().toUpperCase()) + 4) {
                this.maxType = Helper.mc.fontRendererObj.getStringWidth(mt.name().toUpperCase()) + 4;
            }
            ++n2;
        }
        Client.instance.getModuleManager();
        for (Module m : ModuleManager.getModules()) {
            if (this.maxModule > Helper.mc.fontRendererObj.getStringWidth(m.getName().toUpperCase()) + 4) continue;
            this.maxModule = Helper.mc.fontRendererObj.getStringWidth(m.getName().toUpperCase()) + 4;
        }
        Client.instance.getModuleManager();
        for (Module m : ModuleManager.getModules()) {
            if (m.getValues().isEmpty()) continue;
            for (Value val : m.getValues()) {
                if (this.maxValue > Helper.mc.fontRendererObj.getStringWidth(val.getDisplayName().toUpperCase()) + 4) continue;
                this.maxValue = Helper.mc.fontRendererObj.getStringWidth(val.getDisplayName().toUpperCase()) + 4;
            }
        }
        this.maxModule += 12;
        this.maxValue += 24;
        boolean highestWidth = false;
        this.maxType = this.maxType < this.maxModule ? this.maxModule : this.maxType;
        this.maxModule += this.maxType;
        this.maxValue += this.maxModule;
        EventBus.getInstance().register(this);
    }

    private void resetValuesLength() {
        this.maxValue = 0;
        for (Value val : this.selectedModule.getValues()) {
            int off;
            int n = off = val instanceof Option ? 6 : Helper.mc.fontRendererObj.getStringWidth(String.format(" \u00a77%s", val.getValue().toString())) + 6;
            if (this.maxValue > Helper.mc.fontRendererObj.getStringWidth(val.getDisplayName().toUpperCase()) + off) continue;
            this.maxValue = Helper.mc.fontRendererObj.getStringWidth(val.getDisplayName().toUpperCase()) + off;
        }
        this.maxValue += this.maxModule;
    }

    @EventHandler
    private void renderTabGUI(EventRender2D e) {
        block34 : {
            block33 : {
                CFontRenderer font = FontLoaders.kiona18;
                if (!HUD.useFont) break block33;
                if (Helper.mc.gameSettings.showDebugInfo || !Client.instance.getModuleManager().getModuleByClass(HUD.class).isEnabled()) break block34;
                int categoryY = HUD.shouldMove ? 26 : this.height;
                int moduleY = categoryY;
                int valueY = categoryY;
                RenderUtil.drawBorderedRect(2.0f, categoryY, this.maxType - 25, categoryY + 12 * ModuleType.values().length, 2.0f, new Color(0, 0, 0, 130).getRGB(), new Color(0, 0, 0, 180).getRGB());
                ModuleType[] moduleArray = ModuleType.values();
                int mA = moduleArray.length;
                int mA2 = 0;
                while (mA2 < mA) {
                    ModuleType mt = moduleArray[mA2];
                    if (this.selectedType == mt) {
                        RenderUtil.drawRect(2.5f, categoryY + 0.5f, this.maxType - 25.5f, (categoryY + Helper.mc.fontRendererObj.FONT_HEIGHT) + 2.5f, new Color(102, 172, 255).getRGB());
                        moduleY = categoryY;
                    }
                    if (this.selectedType == mt) {
                        font.drawStringWithShadow(mt.name(), 7.0, categoryY + 3, -1);
                    } else {
                        font.drawStringWithShadow(mt.name(), 5.0, categoryY + 3, new Color(180, 180, 180).getRGB());
                    }
                    categoryY += 12;
                    ++mA2;
                }
                if (this.section == Section.MODULES || this.section == Section.VALUES) {
                    RenderUtil.drawBorderedRect(this.maxType - 20, moduleY, this.maxModule - 38, moduleY + 12 * Client.instance.getModuleManager().getModulesInType(this.selectedType).size(), 2.0f, new Color(0, 0, 0, 130).getRGB(), new Color(0, 0, 0, 180).getRGB());
                    for (Module m : Client.instance.getModuleManager().getModulesInType(this.selectedType)) {
                        if (this.selectedModule == m) {
                            RenderUtil.drawRect(this.maxType - 19.5f, moduleY + 0.5f, this.maxModule - 38.5f, (moduleY + Helper.mc.fontRendererObj.FONT_HEIGHT) + 2.5f, new Color(102, 172, 255).getRGB());
                            valueY = moduleY;
                        }
                        if (this.selectedModule == m) {
                            font.drawStringWithShadow(m.getName(), this.maxType - 15, moduleY + 3, m.isEnabled() ? -1 : 11184810);
                        } else {
                            font.drawStringWithShadow(m.getName(), this.maxType - 17, moduleY + 3, m.isEnabled() ? -1 : 11184810);
                        }
                        if (!m.getValues().isEmpty()) {
                            RenderUtil.drawRect(this.maxModule - 38, moduleY + 0.5f, this.maxModule - 39, (moduleY + Helper.mc.fontRendererObj.FONT_HEIGHT) + 2.5f, new Color(153, 200, 255).getRGB());
                            if (this.section == Section.VALUES && this.selectedModule == m) {
                                RenderUtil.drawBorderedRect(this.maxModule - 32, valueY, this.maxValue - 25, valueY + 12 * this.selectedModule.getValues().size(), 2.0f, new Color(10, 10, 10, 180).getRGB(), new Color(10, 10, 10, 180).getRGB());
                                for (Value val : this.selectedModule.getValues()) {
                                    RenderUtil.drawRect(this.maxModule - 31.5f, valueY + 0.5f, this.maxValue - 25.5f, (valueY + Helper.mc.fontRendererObj.FONT_HEIGHT) + 2.5f, this.selectedValue == val ? new Color(102, 172, 255).getRGB() : 0);
                                    if (val instanceof Option) {
                                        font.drawStringWithShadow(val.getDisplayName(), this.selectedValue == val ? this.maxModule - 27 : this.maxModule - 29, valueY + 3, (Boolean)val.getValue() != false ? new Color(153, 200, 255).getRGB() : 11184810);
                                    } else {
                                        String toRender = String.format("%s: \u00a77%s", val.getDisplayName(), val.getValue().toString());
                                        if (this.selectedValue == val) {
                                            font.drawStringWithShadow(toRender, this.maxModule - 27, valueY + 3, -1);
                                        } else {
                                            font.drawStringWithShadow(toRender, this.maxModule - 29, valueY + 3, -1);
                                        }
                                    }
                                    valueY += 12;
                                }
                            }
                        }
                        moduleY += 12;
                    }
                }
                break block34;
            }
            if (!Helper.mc.gameSettings.showDebugInfo && Client.instance.getModuleManager().getModuleByClass(HUD.class).isEnabled()) {
                int categoryY = HUD.shouldMove ? 26 : this.height;
                int moduleY = categoryY;
                int valueY = categoryY;
                RenderUtil.drawBorderedRect(2.0f, categoryY, this.maxType - 25, categoryY + 12 * ModuleType.values().length, 2.0f, new Color(0, 0, 0, 130).getRGB(), new Color(0, 0, 0, 180).getRGB());
                ModuleType[] moduleArray = ModuleType.values();
                int mA = moduleArray.length;
                int mA2 = 0;
                while (mA2 < mA) {
                    ModuleType mt = moduleArray[mA2];
                    if (this.selectedType == mt) {
                        RenderUtil.drawRect(2.5f, categoryY + 0.5f, this.maxType - 25.5f, (categoryY + Helper.mc.fontRendererObj.FONT_HEIGHT) + 2.5f, new Color(102, 172, 255).getRGB());
                        moduleY = categoryY;
                    }
                    if (this.selectedType == mt) {
                        Helper.mc.fontRendererObj.drawStringWithShadow(mt.name(), 7.0f, categoryY + 2, -1);
                    } else {
                        Helper.mc.fontRendererObj.drawStringWithShadow(mt.name(), 5.0f, categoryY + 2, new Color(180, 180, 180).getRGB());
                    }
                    categoryY += 12;
                    ++mA2;
                }
                if (this.section == Section.MODULES || this.section == Section.VALUES) {
                    RenderUtil.drawBorderedRect(this.maxType - 20, moduleY, this.maxModule - 38, moduleY + 12 * Client.instance.getModuleManager().getModulesInType(this.selectedType).size(), 2.0f, new Color(0, 0, 0, 130).getRGB(), new Color(0, 0, 0, 180).getRGB());
                    for (Module m : Client.instance.getModuleManager().getModulesInType(this.selectedType)) {
                        if (this.selectedModule == m) {
                            RenderUtil.drawRect(this.maxType - 19.5f, moduleY + 0.5f, this.maxModule - 38.5f, (moduleY + Helper.mc.fontRendererObj.FONT_HEIGHT) + 2.5f, new Color(102, 172, 255).getRGB());
                            valueY = moduleY;
                        }
                        if (this.selectedModule == m) {
                            Helper.mc.fontRendererObj.drawStringWithShadow(m.getName(), this.maxType - 15, moduleY + 2, m.isEnabled() ? -1 : 11184810);
                        } else {
                            Helper.mc.fontRendererObj.drawStringWithShadow(m.getName(), this.maxType - 17, moduleY + 2, m.isEnabled() ? -1 : 11184810);
                        }
                        if (!m.getValues().isEmpty()) {
                            RenderUtil.drawRect(this.maxModule - 38, moduleY + 0.5f, this.maxModule - 39, (moduleY + Helper.mc.fontRendererObj.FONT_HEIGHT) + 2.5f, new Color(153, 200, 255).getRGB());
                            if (this.section == Section.VALUES && this.selectedModule == m) {
                                RenderUtil.drawBorderedRect(this.maxModule - 32, valueY, this.maxValue - 25, valueY + 12 * this.selectedModule.getValues().size(), 2.0f, new Color(10, 10, 10, 180).getRGB(), new Color(10, 10, 10, 180).getRGB());
                                for (Value val : this.selectedModule.getValues()) {
                                    RenderUtil.drawRect(this.maxModule - 31.5f, valueY + 0.5f, this.maxValue - 25.5f, (valueY + Helper.mc.fontRendererObj.FONT_HEIGHT) + 2.5f, this.selectedValue == val ? new Color(102, 172, 255).getRGB() : 0);
                                    if (val instanceof Option) {
                                        Helper.mc.fontRendererObj.drawStringWithShadow(val.getDisplayName(), this.selectedValue == val ? this.maxModule - 27 : this.maxModule - 29, valueY + 2, (Boolean)val.getValue() != false ? new Color(153, 200, 255).getRGB() : 11184810);
                                    } else {
                                        String toRender = String.format("%s: \u00a77%s", val.getDisplayName(), val.getValue().toString());
                                        if (this.selectedValue == val) {
                                            Helper.mc.fontRendererObj.drawStringWithShadow(toRender, this.maxModule - 27, valueY + 2, -1);
                                        } else {
                                            Helper.mc.fontRendererObj.drawStringWithShadow(toRender, this.maxModule - 29, valueY + 2, -1);
                                        }
                                    }
                                    valueY += 12;
                                }
                            }
                        }
                        moduleY += 12;
                    }
                }
            }
        }
    }

    @EventHandler
    private void onKey(EventKey e) {
        if (!Helper.mc.gameSettings.showDebugInfo) {
            block0 : switch (e.getKey()) {
                case 208: {
                    switch (TabUI.$SWITCH_TABLE$com$enjoytheban$module$modules$render$UI$TabUI$Section()[this.section.ordinal()]) {
                        case 1: {
                            ++this.currentType;
                            if (this.currentType > ModuleType.values().length - 1) {
                                this.currentType = 0;
                            }
                            this.selectedType = ModuleType.values()[this.currentType];
                            break block0;
                        }
                        case 2: {
                            ++this.currentModule;
                            if (this.currentModule > Client.instance.getModuleManager().getModulesInType(this.selectedType).size() - 1) {
                                this.currentModule = 0;
                            }
                            this.selectedModule = Client.instance.getModuleManager().getModulesInType(this.selectedType).get(this.currentModule);
                            break block0;
                        }
                        case 3: {
                            ++this.currentValue;
                            if (this.currentValue > this.selectedModule.getValues().size() - 1) {
                                this.currentValue = 0;
                            }
                            this.selectedValue = this.selectedModule.getValues().get(this.currentValue);
                        }
                    }
                    break;
                }
                case 200: {
                    switch (TabUI.$SWITCH_TABLE$com$enjoytheban$module$modules$render$UI$TabUI$Section()[this.section.ordinal()]) {
                        case 1: {
                            --this.currentType;
                            if (this.currentType < 0) {
                                this.currentType = ModuleType.values().length - 1;
                            }
                            this.selectedType = ModuleType.values()[this.currentType];
                            break block0;
                        }
                        case 2: {
                            --this.currentModule;
                            if (this.currentModule < 0) {
                                this.currentModule = Client.instance.getModuleManager().getModulesInType(this.selectedType).size() - 1;
                            }
                            this.selectedModule = Client.instance.getModuleManager().getModulesInType(this.selectedType).get(this.currentModule);
                            break block0;
                        }
                        case 3: {
                            --this.currentValue;
                            if (this.currentValue < 0) {
                                this.currentValue = this.selectedModule.getValues().size() - 1;
                            }
                            this.selectedValue = this.selectedModule.getValues().get(this.currentValue);
                        }
                    }
                    break;
                }
                case 205: {
                    switch (TabUI.$SWITCH_TABLE$com$enjoytheban$module$modules$render$UI$TabUI$Section()[this.section.ordinal()]) {
                        case 1: {
                            this.currentModule = 0;
                            this.selectedModule = Client.instance.getModuleManager().getModulesInType(this.selectedType).get(this.currentModule);
                            this.section = Section.MODULES;
                            break block0;
                        }
                        case 2: {
                            if (this.selectedModule.getValues().isEmpty()) break block0;
                            this.resetValuesLength();
                            this.currentValue = 0;
                            this.selectedValue = this.selectedModule.getValues().get(this.currentValue);
                            this.section = Section.VALUES;
                            break block0;
                        }
                        case 3: {
                            if (this.selectedValue instanceof Option) {
                                this.selectedValue.setValue((Boolean)this.selectedValue.getValue() == false);
                            } else if (this.selectedValue instanceof Numbers) {
                                Numbers value = (Numbers)this.selectedValue;
                                double inc = (double) value.getValue();
                                inc += (value.getIncrement()).doubleValue();
                                if ((inc = MathUtil.toDecimalLength(inc, 1)) > (double)value.getMaximum()) {
                                    inc = (double) ((Numbers)this.selectedValue).getMinimum();
                                }
                                this.selectedValue.setValue(inc);
                            } else if (this.selectedValue instanceof Mode) {
                                Mode theme = (Mode)this.selectedValue;
                                Enum current = (Enum)theme.getValue();
                                int next = current.ordinal() + 1 >= theme.getModes().length ? 0 : current.ordinal() + 1;
                                this.selectedValue.setValue(theme.getModes()[next]);
                            }
                            this.resetValuesLength();
                        }
                    }
                    break;
                }
                case 28: {
                    switch (TabUI.$SWITCH_TABLE$com$enjoytheban$module$modules$render$UI$TabUI$Section()[this.section.ordinal()]) {
                        case 1: {
                            break block0;
                        }
                        case 2: {
                            this.selectedModule.setEnabled(!this.selectedModule.isEnabled());
                            break block0;
                        }
                        case 3: {
                            this.section = Section.MODULES;
                        }
                    }
                    break;
                }
                case 203: {
                    switch (TabUI.$SWITCH_TABLE$com$enjoytheban$module$modules$render$UI$TabUI$Section()[this.section.ordinal()]) {
                        case 1: {
                            break block0;
                        }
                        case 2: {
                            this.section = Section.TYPES;
                            this.currentModule = 0;
                            break block0;
                        }
                        case 3: {
                            if (Helper.onServer("enjoytheban")) break block0;
                            if (this.selectedValue instanceof Option) {
                                this.selectedValue.setValue((Boolean)this.selectedValue.getValue() == false);
                            } else if (this.selectedValue instanceof Numbers) {
                                Numbers value = (Numbers)this.selectedValue;
                                double inc = (double) value.getValue();
                                inc -= (value.getIncrement()).doubleValue();
                                if ((inc = MathUtil.toDecimalLength(inc, 1)) < (double)value.getMinimum()) {
                                    inc = (double) ((Numbers)this.selectedValue).getMaximum();
                                }
                                this.selectedValue.setValue(inc);
                            } else if (this.selectedValue instanceof Mode) {
                                Mode theme = (Mode)this.selectedValue;
                                Enum current = (Enum)theme.getValue();
                                int next = current.ordinal() - 1 < 0 ? theme.getModes().length - 1 : current.ordinal() - 1;
                                this.selectedValue.setValue(theme.getModes()[next]);
                            }
                            this.maxValue = 0;
                            for (Value val : this.selectedModule.getValues()) {
                                int off;
                                int n = off = val instanceof Option ? 6 : Minecraft.getMinecraft().fontRendererObj.getStringWidth(String.format(" \u00a77%s", val.getValue().toString())) + 6;
                                if (this.maxValue > Minecraft.getMinecraft().fontRendererObj.getStringWidth(val.getDisplayName().toUpperCase()) + off) continue;
                                this.maxValue = Minecraft.getMinecraft().fontRendererObj.getStringWidth(val.getDisplayName().toUpperCase()) + off;
                            }
                            this.maxValue += this.maxModule;
                        }
                    }
                }
            }
        }
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$enjoytheban$module$modules$render$UI$TabUI$Section() {
        int[] arrn;
        int[] arrn2 = $SWITCH_TABLE$com$enjoytheban$module$modules$render$UI$TabUI$Section;
        if (arrn2 != null) {
            return arrn2;
        }
        arrn = new int[Section.values().length];
        try {
            arrn[Section.MODULES.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Section.TYPES.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[Section.VALUES.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        $SWITCH_TABLE$com$enjoytheban$module$modules$render$UI$TabUI$Section = arrn;
        return $SWITCH_TABLE$com$enjoytheban$module$modules$render$UI$TabUI$Section;
    }

    public static enum Section {
        TYPES,
        MODULES,
        VALUES;
    }

}

