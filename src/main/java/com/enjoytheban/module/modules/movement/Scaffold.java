package com.enjoytheban.module.modules.movement;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.world.EventPostUpdate;
import com.enjoytheban.api.events.world.EventPreUpdate;
import com.enjoytheban.api.value.Option;
import com.enjoytheban.api.value.Value;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import com.enjoytheban.utils.math.RotationUtil;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public class Scaffold extends Module {
   private Option tower = new Option("Tower", "tower", Boolean.valueOf(true));
   private Option silent = new Option("Silent", "Silent", Boolean.valueOf(true));
   private Option aac = new Option("AAC", "AAC", Boolean.valueOf(false));
   private List invalid;
   private BlockCache blockCache;
   private int currentItem;

   public Scaffold() {
      super("Scaffold", new String[]{"magiccarpet", "blockplacer", "airwalk"}, ModuleType.Movement);
      this.invalid = Arrays.asList(new Block[]{Blocks.air, Blocks.water, Blocks.fire, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.chest, Blocks.enchanting_table, Blocks.tnt});
      this.addValues(new Value[]{this.tower, this.silent, this.aac});
      this.currentItem = 0;
      this.setColor((new Color(244, 119, 194)).getRGB());
   }

   public void onEnable() {
      this.currentItem = this.mc.thePlayer.inventory.currentItem;
   }

   public void onDisable() {
      this.mc.thePlayer.inventory.currentItem = this.currentItem;
   }

   @EventHandler
   private void onUpdate(EventPreUpdate event) {
      if(((Boolean)this.aac.getValue()).booleanValue()) {
         this.mc.thePlayer.setSprinting(false);
      }

      if(this.grabBlockSlot() != -1) {
         this.blockCache = this.grab();
         if(this.blockCache != null) {
            float[] rotations = RotationUtil.grabBlockRotations(BlockCache.access$0(this.blockCache));
            event.setYaw(rotations[0]);
            event.setPitch(RotationUtil.getVecRotation(this.grabPosition(BlockCache.access$0(this.blockCache), BlockCache.access$1(this.blockCache)))[1] - 3.0F);
         }
      }
   }

   @EventHandler
   private void onPostUpdate(EventPostUpdate event) {
      if(this.blockCache != null) {
         if(this.mc.gameSettings.keyBindJump.isPressed() && ((Boolean)this.tower.getValue()).booleanValue()) {
            this.mc.thePlayer.setSprinting(false);
            this.mc.thePlayer.motionY = 0.0D;
            this.mc.thePlayer.motionX = 0.0D;
            this.mc.thePlayer.motionZ = 0.0D;
            this.mc.thePlayer.jump();
         }

         int currentSlot = this.mc.thePlayer.inventory.currentItem;
         int slot = this.grabBlockSlot();
         this.mc.thePlayer.inventory.currentItem = slot;
         if(this.placeBlock(BlockCache.access$2(this.blockCache), BlockCache.access$3(this.blockCache))) {
            if(((Boolean)this.silent.getValue()).booleanValue()) {
               this.mc.thePlayer.inventory.currentItem = currentSlot;
               this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(currentSlot));
            }

            this.blockCache = null;
         }

      }
   }

   private boolean placeBlock(BlockPos pos, EnumFacing facing) {
      new Vec3(this.mc.thePlayer.posX, this.mc.thePlayer.posY + (double)this.mc.thePlayer.getEyeHeight(), this.mc.thePlayer.posZ);
      if(this.mc.playerController.onPlayerRightClick(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.getHeldItem(), pos, facing, (new Vec3(BlockCache.access$2(this.blockCache))).addVector(0.5D, 0.5D, 0.5D).add(scale(new Vec3(BlockCache.access$3(this.blockCache).getDirectionVec()),0.5)))) {
         this.mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
         return true;
      } else {
         return false;
      }
   }
    public Vec3 scale(Vec3 v , double p_186678_1_) {
        return new Vec3(v.xCoord * p_186678_1_, v.yCoord * p_186678_1_, v.zCoord * p_186678_1_);
    }

    private Vec3 grabPosition(BlockPos position, EnumFacing facing) {
      Vec3 offset = new Vec3((double)facing.getDirectionVec().getX() / 2.0D, (double)facing.getDirectionVec().getY() / 2.0D, (double)facing.getDirectionVec().getZ() / 2.0D);
      Vec3 point = new Vec3((double)position.getX() + 0.5D, (double)position.getY() + 0.5D, (double)position.getZ() + 0.5D);
      return point.add(offset);
   }

   private BlockCache grab() {
      EnumFacing[] invert = new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN, EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.WEST};
      BlockPos position = (new BlockPos(this.mc.thePlayer.getPositionVector())).offset(EnumFacing.DOWN);
      if(!(this.mc.theWorld.getBlockState(position).getBlock() instanceof BlockAir)) {
         return null;
      } else {
         EnumFacing[] var6;
         int var5 = (var6 = EnumFacing.values()).length;

         for(int offset = 0; offset < var5; ++offset) {
            EnumFacing offsets = var6[offset];
            BlockPos offset1 = position.offset(offsets);
            this.mc.theWorld.getBlockState(offset1);
            if(!(this.mc.theWorld.getBlockState(offset1).getBlock() instanceof BlockAir)) {
               return new BlockCache(this, offset1, invert[offsets.ordinal()], (BlockCache)null);
            }
         }

         BlockPos[] var16 = new BlockPos[]{new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(0, 0, -1), new BlockPos(0, 0, 1)};
         if(this.mc.thePlayer.onGround) {
            BlockPos[] var19 = var16;
            int var18 = var16.length;

            for(var5 = 0; var5 < var18; ++var5) {
               BlockPos var17 = var19[var5];
               BlockPos offsetPos = position.add(var17.getX(), 0, var17.getZ());
               this.mc.theWorld.getBlockState(offsetPos);
               if(this.mc.theWorld.getBlockState(offsetPos).getBlock() instanceof BlockAir) {
                  EnumFacing[] var13;
                  int var12 = (var13 = EnumFacing.values()).length;

                  for(int var11 = 0; var11 < var12; ++var11) {
                     EnumFacing facing2 = var13[var11];
                     BlockPos offset2 = offsetPos.offset(facing2);
                     this.mc.theWorld.getBlockState(offset2);
                     if(!(this.mc.theWorld.getBlockState(offset2).getBlock() instanceof BlockAir)) {
                        return new BlockCache(this, offset2, invert[facing2.ordinal()], (BlockCache)null);
                     }
                  }
               }
            }
         }

         return null;
      }
   }

   private int grabBlockSlot() {
      for(int i = 0; i < 9; ++i) {
         ItemStack itemStack = this.mc.thePlayer.inventory.mainInventory[i];
         if(itemStack != null && itemStack.getItem() instanceof ItemBlock) {
            return i;
         }
      }

      return -1;
   }
}

class BlockCache {
	   private BlockPos position;
	   private EnumFacing facing;
	   final Scaffold this$0;

	   private BlockCache(Scaffold var1, BlockPos position, EnumFacing facing) {
	      this.this$0 = var1;
	      this.position = position;
	      this.facing = facing;
	   }

	   private BlockPos getPosition() {
	      return this.position;
	   }

	   private EnumFacing getFacing() {
	      return this.facing;
	   }

	   static BlockPos access$0(BlockCache var0) {
	      return var0.getPosition();
	   }

	   static EnumFacing access$1(BlockCache var0) {
	      return var0.getFacing();
	   }

	   static BlockPos access$2(BlockCache var0) {
	      return var0.position;
	   }

	   static EnumFacing access$3(BlockCache var0) {
	      return var0.facing;
	   }

	   BlockCache(Scaffold var1, BlockPos var2, EnumFacing var3, BlockCache var4) {
	      this(var1, var2, var3);
	   }
	}

