/*
 * Decompiled with CFR 0_132.
 */
package com.enjoytheban.module.modules.combat;

import com.enjoytheban.Client;
import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.world.EventPacketSend;
import com.enjoytheban.api.events.world.EventPostUpdate;
import com.enjoytheban.api.events.world.EventPreUpdate;
import com.enjoytheban.api.value.Mode;
import com.enjoytheban.api.value.Numbers;
import com.enjoytheban.api.value.Option;
import com.enjoytheban.api.value.Value;
import com.enjoytheban.management.FriendManager;
import com.enjoytheban.management.ModuleManager;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import com.enjoytheban.module.modules.combat.AntiBot;
import com.enjoytheban.module.modules.combat.AutoHeal;
import com.enjoytheban.module.modules.combat.Criticals;
import com.enjoytheban.module.modules.movement.Speed;
import com.enjoytheban.module.modules.player.Teams;
import com.enjoytheban.utils.Helper;
import com.enjoytheban.utils.TimerUtil;
import com.enjoytheban.utils.math.MathUtil;
import com.enjoytheban.utils.math.RotationUtil;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class Killaura
extends Module {
    private TimerUtil timer = new TimerUtil();
    private Entity target;
    private List targets = new ArrayList(0);
    private int index;
    private int xd;
    private int tpdelay;
    private Numbers<Double> aps = new Numbers<Double>("APS", "APS", 10.0, 1.0, 20.0, 0.5);
    private Numbers<Double> reach = new Numbers<Double>("Reach", "reach", 4.5, 1.0, 6.0, 0.1);
    private Option<Boolean> blocking = new Option<Boolean>("Autoblock", "autoblock", true);
    private Option<Boolean> players = new Option<Boolean>("Players", "players", true);
    private Option<Boolean> animals = new Option<Boolean>("Animals", "animals", true);
    private Option<Boolean> mobs = new Option<Boolean>("Mobs", "mobs", false);
    private Option<Boolean> invis = new Option<Boolean>("Invisibles", "invisibles", false);
    private Option<Boolean> god = new Option<Boolean>("Velt God Mode", "Velt God Mode", false);
    private Mode<Enum> mode = new Mode("Mode", "mode", (Enum[])AuraMode.values(), (Enum)AuraMode.Switch);
    private boolean isBlocking;
    private Comparator<Entity> angleComparator = Comparator.comparingDouble(e2 -> RotationUtil.getRotations(e2)[0]);

    public Killaura() {
        super("KillAura", new String[]{"ka", "aura", "killa"}, ModuleType.Combat);
        this.setColor(new Color(226, 54, 30).getRGB());
        this.addValues(this.aps, this.reach, this.blocking, this.players, this.animals, this.mobs, this.invis, this.god, this.mode);
    }

    @Override
    public void onDisable() {
        this.targets.clear();
        if (this.blocking.getValue().booleanValue() && this.canBlock() && this.mc.thePlayer.isBlocking()) {
            this.stopAutoBlockHypixel();
        }
    }

    @Override
    public void onEnable() {
        this.target = null;
        this.index = 0;
        this.xd = 0;
    }

    private boolean canBlock() {
        if (this.mc.thePlayer.getCurrentEquippedItem() != null && this.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
            return true;
        }
        return false;
    }

    private void startAutoBlockHypixel() {
        if (Helper.onServer("hypixel")) {
            KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindUseItem.getKeyCode(), true);
            if (this.mc.playerController.sendUseItem(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.inventory.getCurrentItem())) {
                this.mc.getItemRenderer().resetEquippedProgress2();
            }
        }
    }

    private void stopAutoBlockHypixel() {
        if (Helper.onServer("hypixel")) {
            KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindUseItem.getKeyCode(), false);
            this.mc.playerController.onStoppedUsingItem(this.mc.thePlayer);
        }
    }

    private void startAutoBlock() {
        if (!Helper.onServer("hypixel")) {
            this.mc.playerController.sendUseItem(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.getCurrentEquippedItem());
        }
    }

    private void stopAutoBlock() {
        if (!Helper.onServer("hypixel")) {
            this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
    }

    private boolean shouldAttack() {
        return this.timer.hasReached(1000.0 / (this.aps.getValue() + MathUtil.randomDouble(0.0, 5.0)));
    }

    @EventHandler
    private void onUpdate(EventPreUpdate event) {
        this.setSuffix(this.mode.getValue());
        this.targets = this.loadTargets();
        this.targets.sort(this.angleComparator);
        if (this.target != null && this.target instanceof EntityPlayer || this.target instanceof EntityMob || this.target instanceof EntityAnimal) {
            this.target = null;
        }
        if (this.mc.thePlayer.ticksExisted % 50 == 0 && this.targets.size() > 1) {
            ++this.index;
        }
        if (!this.targets.isEmpty()) {
            if (this.index >= this.targets.size()) {
                this.index = 0;
            }
            this.target = (Entity)this.targets.get(this.index);
            if (!(Helper.onServer("invaded") || Helper.onServer("minemen") || Helper.onServer("faithful"))) {
                event.setYaw(RotationUtil.faceTarget(this.target, 1000.0f, 1000.0f, false)[0]);
                if (!AutoHeal.currentlyPotting) {
                    event.setPitch(RotationUtil.faceTarget(this.target, 1000.0f, 1000.0f, false)[1]);
                }
            }
            if (this.blocking.getValue().booleanValue() && this.canBlock() && this.isBlocking) {
                this.stopAutoBlock();
                if (this.god.getValue().booleanValue()) {
                    event.y += 13.8;
                }
            } else if (this.god.getValue().booleanValue()) {
                event.y += 4.5;
                event.setOnground(true);
            }
        } else if (this.blocking.getValue().booleanValue() && this.canBlock() && this.mc.thePlayer.isBlocking()) {
            this.stopAutoBlockHypixel();
        }
    }

    private void swap(int slot, int hotbarNum) {
        this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, this.mc.thePlayer);
    }

    @EventHandler
    private void onUpdatePost(EventPostUpdate e) {
        if (Helper.onServer("enjoytheban")) {
            this.reach.setValue(4.0);
            this.aps.setValue(12.0);
            this.god.setValue(false);
        }
        if (!Helper.onServer("faithful")) {
            Speed speed;
            if (this.god.getValue().booleanValue() && (speed = (Speed)Client.instance.getModuleManager().getModuleByClass(Speed.class)).isEnabled()) {
                speed.setEnabled(false);
            }
            if (this.target != null) {
                double angle = Math.toRadians(this.target.rotationYaw - 90.0f + 360.0f) % 360.0;
                if (this.shouldAttack()) {
                    Criticals crits = (Criticals)Client.instance.getModuleManager().getModuleByClass(Criticals.class);
                    if (crits.isEnabled()) {
                        crits.hypixelCrit();
                    }
                    if (crits.isEnabled()) {
                        crits.packetCrit();
                    }
                    if (this.mode.getValue() == AuraMode.Switch) {
                        this.attack();
                    } else {
                        this.swap(9, this.mc.thePlayer.inventory.currentItem);
                        this.attack();
                        this.attack();
                        this.attack();
                        crits.offsetCrit();
                        this.swap(9, this.mc.thePlayer.inventory.currentItem);
                        this.attack();
                        this.attack();
                        crits.offsetCrit();
                    }
                    if (!this.mc.thePlayer.isBlocking() && this.canBlock() && this.blocking.getValue().booleanValue()) {
                        this.startAutoBlockHypixel();
                    }
                    this.timer.reset();
                }
                if (this.canBlock() && this.blocking.getValue().booleanValue() && !this.mc.thePlayer.isBlocking()) {
                    this.startAutoBlock();
                }
            }
        }
    }

    private List<Entity> loadTargets() {
        return this.mc.theWorld.loadedEntityList.stream().filter(e -> (double)this.mc.thePlayer.getDistanceToEntity((Entity)e) <= this.reach.getValue() && this.qualifies((Entity)e)).collect(Collectors.toList());
    }

    private boolean qualifies(Entity e) {
        if (e == this.mc.thePlayer) {
            return false;
        }
        AntiBot ab = (AntiBot)Client.instance.getModuleManager().getModuleByClass(AntiBot.class);
        if (ab.isServerBot(e)) {
            return false;
        }
        if (!e.isEntityAlive()) {
            return false;
        }
        if (FriendManager.isFriend(e.getName())) {
            return false;
        }
        if (e instanceof EntityPlayer && this.players.getValue().booleanValue() && !Teams.isOnSameTeam(e)) {
            return true;
        }
        if (e instanceof EntityMob && this.mobs.getValue().booleanValue()) {
            return true;
        }
        if (e instanceof EntityAnimal && this.animals.getValue().booleanValue()) {
            return true;
        }
        if (e.isInvisible() && !this.invis.getValue().booleanValue()) {
            return true;
        }
        return false;
    }

    private void attack() {
        if (this.blocking.getValue().booleanValue() && this.canBlock() && this.mc.thePlayer.isBlocking() && this.qualifies(this.target)) {
            this.stopAutoBlockHypixel();
        }
        if (Helper.onServer("invaded")) {
            this.mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
        } else {
            this.mc.thePlayer.swingItem();
        }
        this.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(this.target, C02PacketUseEntity.Action.ATTACK));
    }

    @EventHandler
    private void blockinglistener(EventPacketSend packet) {
        C07PacketPlayerDigging packetPlayerDigging;
        C08PacketPlayerBlockPlacement blockPlacement;
        if (packet.getPacket() instanceof C07PacketPlayerDigging && (packetPlayerDigging = (C07PacketPlayerDigging)packet.getPacket()).getStatus().equals((Object)C07PacketPlayerDigging.Action.RELEASE_USE_ITEM)) {
            this.isBlocking = false;
        }
        if (packet.getPacket() instanceof C08PacketPlayerBlockPlacement && (blockPlacement = (C08PacketPlayerBlockPlacement)packet.getPacket()).getStack() != null && blockPlacement.getStack().getItem() instanceof ItemSword && blockPlacement.getPosition().equals(new BlockPos(-1, -1, -1))) {
            this.isBlocking = true;
        }
    }

    public void mmcAttack(EntityLivingBase entity) {
        this.mmcAttack(entity, false);
    }

    public void mmcAttack(EntityLivingBase entity, boolean crit) {
        this.mc.thePlayer.swingItem();
        float sharpLevel = EnchantmentHelper.getModifierForCreature(this.mc.thePlayer.getHeldItem(), entity.getCreatureAttribute());
        boolean vanillaCrit = this.mc.thePlayer.fallDistance > 0.0f && !this.mc.thePlayer.onGround && !this.mc.thePlayer.isOnLadder() && !this.mc.thePlayer.isInWater() && !this.mc.thePlayer.isPotionActive(Potion.blindness) && this.mc.thePlayer.ridingEntity == null;
        this.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity((Entity)entity, C02PacketUseEntity.Action.ATTACK));
        if (crit || vanillaCrit) {
            this.mc.thePlayer.onCriticalHit(entity);
        }
        if (sharpLevel > 0.0f) {
            this.mc.thePlayer.onEnchantmentCritical(entity);
        }
    }

    static enum AuraMode {
        Switch,
        Tick;
    }

}

