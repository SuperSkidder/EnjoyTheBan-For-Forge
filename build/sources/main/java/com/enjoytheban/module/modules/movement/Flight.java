/*
 * Decompiled with CFR 0_132.
 */
package com.enjoytheban.module.modules.movement;

import com.enjoytheban.Client;
import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.world.EventMove;
import com.enjoytheban.api.events.world.EventPostUpdate;
import com.enjoytheban.api.events.world.EventPreUpdate;
import com.enjoytheban.api.value.Mode;
import com.enjoytheban.api.value.Value;
import com.enjoytheban.management.ModuleManager;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import com.enjoytheban.module.modules.movement.Speed;
import com.enjoytheban.utils.PlayerUtils;
import com.enjoytheban.utils.TimerUtil;
import com.enjoytheban.utils.math.MathUtil;
import java.awt.Color;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;

public class Flight
extends Module {
    public Mode mode = new Mode("Mode", "mode", (Enum[])FlightMode.values(), (Enum)FlightMode.Guardian);
    private TimerUtil timer = new TimerUtil();
    private double movementSpeed;
    private int hypixelCounter;
    private int hypixelCounter2;
    int counter, level;
    double moveSpeed, lastDist;   
    boolean b2;
    public Flight() {
        super("Flight", new String[]{"fly", "angel"}, ModuleType.Movement);
        this.setColor(new Color(158, 114, 243).getRGB());
        this.addValues(this.mode);
    }

	public void damagePlayer(int damage) {
		if (damage < 1)
			damage = 1;
		if (damage > MathHelper.floor_double(mc.thePlayer.getMaxHealth()))
			damage = MathHelper.floor_double(mc.thePlayer.getMaxHealth());

		double offset = 0.0625;
		if (mc.thePlayer != null && mc.getNetHandler() != null && mc.thePlayer.onGround) {
			for (int i = 0; i <= ((3 + damage) / offset); i++) { // TODO: teach rederpz (and myself) how math works
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
						mc.thePlayer.posY + offset, mc.thePlayer.posZ, false));
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
						mc.thePlayer.posY, mc.thePlayer.posZ, (i == ((3 + damage) / offset))));
			}
		}
	}
    
    @Override
    public void onEnable() {
        if (this.mode.getValue() == FlightMode.Hypixel || this.mode.getValue() == FlightMode.HypixelZoom) {
        	if(this.mode.getValue() == FlightMode.HypixelZoom) damagePlayer(1);
            this.hypixelCounter = 0;
            this.hypixelCounter2 = 1000;
            this.mc.thePlayer.motionY = 0.42f;
        }
		level = 1;
		moveSpeed = 0.1D;
		b2 = true;
		lastDist = 0.0D;
    }

    @Override
    public void onDisable() {
        if (this.mode.getValue() == FlightMode.Area51) {
            this.mc.thePlayer.motionX = 0.0;
            this.mc.thePlayer.motionZ = 0.0;
        }
        this.hypixelCounter = 0;
        this.hypixelCounter2 = 100;
		level = 1;
		moveSpeed = 0.1D;
		b2 = false;
		lastDist = 0.0D;
    }

    @EventHandler
    private void onUpdate(EventPreUpdate e) {
        this.setSuffix(this.mode.getValue());
        
        if (this.mode.getValue() == FlightMode.Guardian) {
            if (!this.mc.thePlayer.onGround && this.mc.thePlayer.ticksExisted % 2 == 0) {
                this.mc.thePlayer.motionY = 0.04;
            }
            if (this.mc.gameSettings.keyBindJump.isPressed()) {
                this.mc.thePlayer.motionY += 1.0;
            }
            if (this.mc.gameSettings.keyBindSneak.isPressed()) {
                this.mc.thePlayer.motionY -= 1.0;
            }
        } else if (this.mode.getValue() == FlightMode.Vanilla) {
            this.mc.thePlayer.motionY = this.mc.thePlayer.movementInput.jump ? 1.0 : (this.mc.thePlayer.movementInput.sneak ? -1.0 : 0.0);
//            if (this.mc.thePlayer.moving()) {
//                PlayerUtils.setSpeed(1.0);
//            } else {
//                this.mc.thePlayer.setSpeed(0.0);
//            }
        } else if (this.mode.getValue() == FlightMode.Area51) {
            this.mc.thePlayer.motionY = this.mc.thePlayer.movementInput.jump ? 1.0 : (this.mc.thePlayer.movementInput.sneak ? -1.0 : 0.0);
        } else if (this.mode.getValue() == FlightMode.Hypixel || this.mode.getValue() == FlightMode.HypixelZoom) {
   			++counter;
   			if (Minecraft.getMinecraft().thePlayer.moveForward == 0
   					&& Minecraft.getMinecraft().thePlayer.moveStrafing == 0) {
   				Minecraft.getMinecraft().thePlayer.setPosition(
   						Minecraft.getMinecraft().thePlayer.posX + 1.0D,
   						Minecraft.getMinecraft().thePlayer.posY + 1.0D,
   						Minecraft.getMinecraft().thePlayer.posZ + 1.0D);
   				Minecraft.getMinecraft().thePlayer.setPosition(Minecraft.getMinecraft().thePlayer.prevPosX,
   						Minecraft.getMinecraft().thePlayer.prevPosY,
   						Minecraft.getMinecraft().thePlayer.prevPosZ);
   				Minecraft.getMinecraft().thePlayer.motionX = 0.0D;
   				Minecraft.getMinecraft().thePlayer.motionZ = 0.0D;
   			}
   			Minecraft.getMinecraft().thePlayer.motionY = 0.0D;
   			if (Minecraft.getMinecraft().gameSettings.keyBindJump.isPressed())
   				Minecraft.getMinecraft().thePlayer.motionY += 0.5f;
   			if (Minecraft.getMinecraft().gameSettings.keyBindSneak.isPressed())
   				Minecraft.getMinecraft().thePlayer.motionY -= 0.5f;
   			if (counter != 1 && counter == 2) {
   				Minecraft.getMinecraft().thePlayer.setPosition(Minecraft.getMinecraft().thePlayer.posX,
   						Minecraft.getMinecraft().thePlayer.posY + 1.0E-10D,
   						Minecraft.getMinecraft().thePlayer.posZ);
   				counter = 0;
   			}
        } else if (this.mode.getValue() == FlightMode.OldGuardianLongJumpFly && PlayerUtils.isMoving() && !Client.instance.getModuleManager().getModuleByClass(Speed.class).isEnabled()) {
            if (this.mc.thePlayer.isAirBorne) {
                if (this.mc.thePlayer.ticksExisted % 12 == 0 && this.mc.theWorld.getBlockState(new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ)).getBlock() instanceof BlockAir) {
//                    this.mc.thePlayer.setSpeed(6.5);
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 1.0E-9, this.mc.thePlayer.posZ, this.mc.thePlayer.onGround));
                    this.mc.thePlayer.motionY = 0.455;
                } else {
//                    this.mc.thePlayer.setSpeed((float)Math.sqrt(this.mc.thePlayer.motionX * this.mc.thePlayer.motionX + this.mc.thePlayer.motionZ * this.mc.thePlayer.motionZ));
                }
            } else {
                this.mc.thePlayer.motionX = 0.0;
                this.mc.thePlayer.motionZ = 0.0;
            }
            if (this.mc.thePlayer.movementInput.jump) {
                this.mc.thePlayer.motionY = 0.85;
            } else if (this.mc.thePlayer.movementInput.sneak) {
                this.mc.thePlayer.motionY = -0.85;
            }
        }
    }

    @EventHandler
    public void onPost(EventPostUpdate e) {
    	if (this.mode.getValue() == FlightMode.Hypixel || this.mode.getValue() == FlightMode.HypixelZoom) {
 			double xDist = Minecraft.getMinecraft().thePlayer.posX
 					- Minecraft.getMinecraft().thePlayer.prevPosX;
 			double zDist = Minecraft.getMinecraft().thePlayer.posZ
 					- Minecraft.getMinecraft().thePlayer.prevPosZ;
 			lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
    	}
    }
    
    @EventHandler
    private void onMove(EventMove e) {
		if (this.mode.getValue() == FlightMode.Hypixel || this.mode.getValue() == FlightMode.HypixelZoom) { 
			float forward = mc.thePlayer.movementInput.moveForward;
			float strafe = mc.thePlayer.movementInput.moveStrafe;
			float yaw = mc.thePlayer.rotationYaw;
			double mx = Math.cos(Math.toRadians((double) (yaw + 90.0F)));
			double mz = Math.sin(Math.toRadians((double) (yaw + 90.0F)));
			
			if (forward == 0.0F && strafe == 0.0F) {
				e.x = 0.0D;
				e.z = 0.0D;
			} else if (forward != 0.0F) {
				if (strafe >= 1.0F) {
					yaw += (float) (forward > 0.0F ? -45 : 45);
					strafe = 0.0F;
				} else if (strafe <= -1.0F) {
					yaw += (float) (forward > 0.0F ? 45 : -45);
					strafe = 0.0F;
				}

				if (forward > 0.0F) {
					forward = 1.0F;
				} else if (forward < 0.0F) {
					forward = -1.0F;
				}
			}
			if (b2) {
				if (level != 1 || Minecraft.getMinecraft().thePlayer.moveForward == 0.0F
						&& Minecraft.getMinecraft().thePlayer.moveStrafing == 0.0F) {
					if (level == 2) {
						level = 3;
						moveSpeed *= 2.1499999D;
					} else if (level == 3) {
						level = 4;
						double difference = (mc.thePlayer.ticksExisted % 2 == 0 ? 0.0103D : 0.0123D)
								* (lastDist - MathUtil.getBaseMovementSpeed());
						moveSpeed = lastDist - difference;
					} else {
						if (Minecraft.getMinecraft().theWorld
								.getCollidingBoundingBoxes(Minecraft.getMinecraft().thePlayer,
										Minecraft.getMinecraft().thePlayer.getCollisionBoundingBox().offset(0.0D,
												Minecraft.getMinecraft().thePlayer.motionY, 0.0D))
								.size() > 0 || Minecraft.getMinecraft().thePlayer.isCollidedVertically) {
							level = 1;
						}
						moveSpeed = lastDist - lastDist / 159.0D;
					}
				} else {
					level = 2;
					int amplifier = Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)
							? Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed)
									.getAmplifier() + 1
							: 0;
					double boost = Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed) ? 1.56
							: 2.034;
					moveSpeed = boost * MathUtil.getBaseMovementSpeed();
				}
				moveSpeed = this.mode.getValue() == FlightMode.HypixelZoom ? Math.max(moveSpeed, MathUtil.getBaseMovementSpeed()) : MathUtil.getBaseMovementSpeed();
				
				e.x = (double) forward * moveSpeed * mx + (double) strafe * moveSpeed * mz;
				e.z = (double) forward * moveSpeed * mz - (double) strafe * moveSpeed * mx;
				if (forward == 0.0F && strafe == 0.0F) {
					e.x = 0.0D;
					e.z = 0.0D;
				}
			}
		}
    }

    double getBaseMoveSpeed() {
        double baseSpeed = 0.275;
        if (this.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = this.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }

    public static enum FlightMode {
        Vanilla,
        Guardian,
        Hypixel,
        HypixelZoom,
        Area51,
        OldGuardianLongJumpFly,
        GuardianLongJumpFly,
        AGC;
    }

}

