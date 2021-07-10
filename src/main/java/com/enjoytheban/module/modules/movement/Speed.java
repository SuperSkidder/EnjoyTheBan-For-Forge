/*
 * Decompiled with CFR 0_132.
 */
package com.enjoytheban.module.modules.movement;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.world.EventMove;
import com.enjoytheban.api.events.world.EventPreUpdate;
import com.enjoytheban.api.value.Mode;
import com.enjoytheban.api.value.Value;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import com.enjoytheban.utils.Helper;
import com.enjoytheban.utils.PlayerUtils;
import com.enjoytheban.utils.TimerUtil;
import com.enjoytheban.utils.math.MathUtil;

import java.awt.Color;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;

public class Speed
        extends Module {
    private Mode<Enum> mode = new Mode("Mode", "mode", (Enum[]) SpeedMode.values(), (Enum) SpeedMode.HypixelHop);
    private int stage;
    private double movementSpeed;
    private double distance;
    private TimerUtil timer = new TimerUtil();

    public Speed() {
        super("Speed", new String[]{"zoom"}, ModuleType.Movement);
        this.setColor(new Color(99, 248, 91).getRGB());
        this.addValues(this.mode);
    }

    @Override
    public void onDisable() {
        if (this.mode.getValue() == SpeedMode.Area51) {
            this.mc.thePlayer.motionX = 0.0;
            this.mc.thePlayer.motionZ = 0.0;
        }
    }

    private boolean canZoom() {
        if (PlayerUtils.isMoving() && this.mc.thePlayer.onGround) {
            return true;
        }
        return false;
    }

    @EventHandler
    private void onUpdate(EventPreUpdate e) {
        if (Helper.onServer("enjoytheban")) {
            this.mode.setValue(SpeedMode.Bhop);
        }
        this.setSuffix(this.mode.getValue());
        if (this.mode.getValue() == SpeedMode.Sloth && this.canZoom()) {
            if (PlayerUtils.isMoving()) {
                boolean under = this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.getEntityBoundingBox().offset(this.mc.thePlayer.motionX, 1.6, this.mc.thePlayer.motionZ)).isEmpty();
                if (this.mc.thePlayer.ticksExisted % 2 != 0 && under) {
                    e.y += 0.42;
                }
                this.mc.thePlayer.motionY = -10.0;
                if (this.mc.thePlayer.onGround) {
                    PlayerUtils.setSpeed(PlayerUtils.getSpeed() * (this.mc.thePlayer.ticksExisted % 2 == 0 ? 4.0f : 0.28f));
                }
            }
        } else if (this.mode.getValue() == SpeedMode.Onground && this.canZoom()) {
            switch (this.stage) {
                case 1: {
                    e.setY(e.getY() + 0.4);
                    e.setOnground(false);
                    ++this.stage;
                    break;
                }
                case 2: {
                    e.setY(e.getY() + 0.4);
                    e.setOnground(false);
                    ++this.stage;
                    break;
                }
                default: {
                    this.stage = 1;
                    break;
                }
            }
        } else if (this.mode.getValue() != SpeedMode.Janitor || e.getType() != 0 || !this.canZoom()) {
            if (this.mode.getValue() == SpeedMode.AGC) {
                double speed = 0.2;
                double x = Math.cos(Math.toRadians(this.mc.thePlayer.rotationYaw + 90.0f));
                double z = Math.sin(Math.toRadians(this.mc.thePlayer.rotationYaw + 90.0f));
                double n = (double) this.mc.thePlayer.movementInput.moveForward * speed * x;
                double xOff = n + (double) this.mc.thePlayer.movementInput.moveStrafe * speed * z;
                double n2 = (double) this.mc.thePlayer.movementInput.moveForward * speed * z;
                double zOff = n2 - (double) (this.mc.thePlayer.movementInput.moveStrafe * 0.5f) * x;
                this.mc.thePlayer.setAIMoveSpeed(this.mc.thePlayer.getAIMoveSpeed());
                if (this.mc.thePlayer.onGround) {
                    if (PlayerUtils.isMoving()) {
                        this.mc.thePlayer.motionY = 0.2;
                    }
                } else if (this.mc.thePlayer.motionY <= -0.10000000149011612) {
                    double cock = 10.0;
                    this.mc.thePlayer.setPosition(this.mc.thePlayer.posX + xOff * cock, this.mc.thePlayer.posY, this.mc.thePlayer.posZ + zOff * cock);
                    this.mc.thePlayer.motionY -= 0.0010000000474974513;
                }
            } else if (this.mode.getValue() != SpeedMode.OldGuardian && this.mode.getValue() != SpeedMode.GuardianYport) {
                double xDist = this.mc.thePlayer.posX - this.mc.thePlayer.prevPosX;
                double zDist = this.mc.thePlayer.posZ - this.mc.thePlayer.prevPosZ;
                this.distance = Math.sqrt(xDist * xDist + zDist * zDist);
            } else if (this.mode.getValue() == SpeedMode.GuardianYport) {
//                this.mc.timer.timerSpeed = PlayerUtils.isMoving() ? 1.6f : 1.0f;
                if (PlayerUtils.isMoving() && this.mc.thePlayer.onGround) {
                    this.mc.thePlayer.motionY = 0.12;
                    this.mc.thePlayer.motionY -= 0.04;
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 1.0E-9, this.mc.thePlayer.posZ, this.mc.thePlayer.onGround));
                    PlayerUtils.setSpeed(0.7);
                } else {
                    PlayerUtils.setSpeed(Math.sqrt(this.mc.thePlayer.motionX * this.mc.thePlayer.motionX + this.mc.thePlayer.motionZ * this.mc.thePlayer.motionZ));
                }
            } else if (PlayerUtils.isMoving() && this.mc.thePlayer.onGround) {
                this.mc.thePlayer.motionY = 0.4;
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 1.0E-9, this.mc.thePlayer.posZ, this.mc.thePlayer.onGround));
                PlayerUtils.setSpeed(1.75);
            } else {
                PlayerUtils.setSpeed(Math.sqrt(this.mc.thePlayer.motionX * this.mc.thePlayer.motionX + this.mc.thePlayer.motionZ * this.mc.thePlayer.motionZ));
            }
        }
    }

    @EventHandler
    private void onMove(EventMove e) {
        if (this.mode.getValue() == SpeedMode.HypixelHop) {
            if (this.canZoom() && this.stage == 1) {
                this.movementSpeed = 1.56 * MathUtil.getBaseMovementSpeed() - 0.01;
            } else if (this.canZoom() && this.stage == 2) {
                this.mc.thePlayer.motionY = 0.3999;
                e.setY(0.3999);
                this.movementSpeed *= 1.58;
            } else if (this.stage == 3) {
                double difference = 0.66 * (this.distance - MathUtil.getBaseMovementSpeed());
                this.movementSpeed = this.distance - difference;
            } else {
                List collidingList = this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.getCollisionBoundingBox().offset(0.0, this.mc.thePlayer.motionY, 0.0));
                if (collidingList.size() > 0 || this.mc.thePlayer.isCollidedVertically && this.stage > 0) {
                    this.stage = PlayerUtils.isMoving() ? 1 : 0;
                }
                this.movementSpeed = this.distance - this.distance / 159.0;
            }
            this.movementSpeed = Math.max(this.movementSpeed, MathUtil.getBaseMovementSpeed());
            PlayerUtils.setSpeed(this.movementSpeed);
            if (PlayerUtils.isMoving()) {
                ++this.stage;
            }
        } else if (this.mode.getValue() == SpeedMode.Area51) {
            if (PlayerUtils.isMoving()) {
                if (Minecraft.getMinecraft().thePlayer.motionY <= 0.0) {
                    Minecraft.getMinecraft().thePlayer.motionY *= 1.5;
                }
                this.mc.thePlayer.onGround = true;
                PlayerUtils.setSpeed(4.0);
            } else {
                this.mc.thePlayer.motionX = 0.0;
                this.mc.thePlayer.motionZ = 0.0;
            }
        } else if (this.mode.getValue() == SpeedMode.Onground && this.canZoom()) {
            switch (this.stage) {
                case 1: {
                    this.movementSpeed = 1.89 * MathUtil.getBaseMovementSpeed() - 0.01;
                    this.distance += 1.0;
                    if (this.distance == 1.0) {
                        e.setY(e.getY() + 8.0E-6);
                        break;
                    }
                    if (this.distance != 2.0) break;
                    e.setY(e.getY() - 8.0E-6);
                    this.distance = 0.0;
                    break;
                }
                case 2: {
                    this.movementSpeed = 1.2 * MathUtil.getBaseMovementSpeed() - 0.01;
                    break;
                }
                default: {
                    this.movementSpeed = (float) MathUtil.getBaseMovementSpeed();
                }
            }
            this.movementSpeed = Math.max(this.movementSpeed, MathUtil.getBaseMovementSpeed());
            PlayerUtils.setSpeed(this.movementSpeed);
            ++this.stage;
        } else if (this.mode.getValue() == SpeedMode.Janitor && this.canZoom()) {
            PlayerUtils.setSpeed(this.mc.thePlayer.ticksExisted % 2 != 0 ? 0 : 2);
        } else if (this.mode.getValue() == SpeedMode.Mineplex) {
            if (this.canZoom() && this.stage == 1) {
                this.movementSpeed = 0.58;
            } else if (this.canZoom() && this.stage == 2) {
                this.mc.thePlayer.motionY = 0.3;
                e.setY(0.3);
                this.movementSpeed = 0.64;
            } else if (this.stage == 3) {
                double difference = 0.66 * (this.distance - MathUtil.getBaseMovementSpeed());
                this.movementSpeed = this.distance - difference;
            } else {
                List collidingList = this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.getCollisionBoundingBox().offset(0.0, this.mc.thePlayer.motionY, 0.0));
                if (collidingList.size() > 0 || this.mc.thePlayer.isCollidedVertically && this.stage > 0) {
                    this.stage = PlayerUtils.isMoving() ? 1 : 0;
                }
                this.movementSpeed = this.distance - this.distance / 159.0;
            }
            this.movementSpeed = Math.max(this.movementSpeed, MathUtil.getBaseMovementSpeed());
           PlayerUtils.setSpeed( this.movementSpeed);
            if (PlayerUtils.isMoving()) {
                ++this.stage;
            }
        } else if (this.mode.getValue() == SpeedMode.Bhop) {
            if (this.canZoom() && this.stage == 1) {
                this.movementSpeed = 2.55 * MathUtil.getBaseMovementSpeed() - 0.01;
            } else if (this.canZoom() && this.stage == 2) {
                this.mc.thePlayer.motionY = 0.3999;
                e.setY(0.3999);
                this.movementSpeed *= 2.1;
            } else if (this.stage == 3) {
                double difference = 0.66 * (this.distance - MathUtil.getBaseMovementSpeed());
                this.movementSpeed = this.distance - difference;
            } else {
                List collidingList = this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.getCollisionBoundingBox().offset(0.0, this.mc.thePlayer.motionY, 0.0));
                if (collidingList.size() > 0 || this.mc.thePlayer.isCollidedVertically && this.stage > 0) {
                    this.stage = PlayerUtils.isMoving() ? 1 : 0;
                }
                this.movementSpeed = this.distance - this.distance / 159.0;
            }
            this.movementSpeed = Math.max(this.movementSpeed, MathUtil.getBaseMovementSpeed());
            PlayerUtils.setSpeed(this.movementSpeed);
            if (PlayerUtils.isMoving()) {
                ++this.stage;
            }
        } else if (this.mode.getValue() == SpeedMode.Guardian) {
            if (PlayerUtils.isMoving()) {
                if (this.mc.thePlayer.onGround) {
                    int i = 0;
                    while (i < 20) {
                        this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 1.0E-9, this.mc.thePlayer.posZ, this.mc.thePlayer.onGround));
                        ++i;
                    }
                    PlayerUtils.setSpeed(1.399999976158142);
                    this.mc.thePlayer.motionY = 0.4;
                    EventMove.y = 0.4;
                } else {
                    PlayerUtils.setSpeed((float) Math.sqrt(this.mc.thePlayer.motionX * this.mc.thePlayer.motionX + this.mc.thePlayer.motionZ * this.mc.thePlayer.motionZ));
                }
            } else {
                this.mc.thePlayer.motionX = 0.0;
                this.mc.thePlayer.motionZ = 0.0;
            }
        }
    }

    static enum SpeedMode {
        Bhop,
        HypixelHop,
        Onground,
        OldGuardian,
        Guardian,
        GuardianYport,
        Mineplex,
        AGC,
        Janitor,
        Sloth,
        Area51;
    }

}

