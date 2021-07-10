/*
 * Decompiled with CFR 0_132.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package com.enjoytheban.module.modules.render;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.rendering.EventRender3D;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import com.enjoytheban.utils.math.Vec4f;
import com.enjoytheban.utils.render.GLUProjection;
import java.awt.Color;
import java.util.List;
import javax.vecmath.Vector3d;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.GL11;

public class ChestESP
extends Module {
    public ChestESP() {
        super("ChestESP", new String[]{"chesthack"}, ModuleType.Render);
        this.setColor(new Color(90, 209, 165).getRGB());
    }
    public static void glColor(final int hex) {
        final float alpha = (hex >> 24 & 0xFF) / 255.0f;
        final float red = (hex >> 16 & 0xFF) / 255.0f;
        final float green = (hex >> 8 & 0xFF) / 255.0f;
        final float blue = (hex & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
    }
    public static void drawBox(final AxisAlignedBB box) {
        if (box == null) {
            return;
        }
        GL11.glBegin(7);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glEnd();
    }

    public static void drawCompleteBox(final AxisAlignedBB axisalignedbb, final float width, final int insideColor, final int borderColor) {
        GL11.glLineWidth(width);
        GL11.glEnable(2848);
        GL11.glEnable(2881);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        glColor(insideColor);
        drawBox(axisalignedbb);
        glColor(borderColor);
        GL11.glDisable(2848);
        GL11.glDisable(2881);
    }


    @EventHandler
    public void onRender(EventRender3D eventRender) {
        ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        for (Object o : this.mc.theWorld.loadedTileEntityList) {
            Block block;
            TileEntity tileEntity = (TileEntity)o;
            if (tileEntity == null || !ChestESP.isStorage(tileEntity)) continue;
            double posX = tileEntity.getPos().getX();
            double posY = tileEntity.getPos().getY();
            double posZ = tileEntity.getPos().getZ();
            AxisAlignedBB axisAlignedBB = null;
            if (tileEntity instanceof TileEntityChest) {
                block = this.mc.theWorld.getBlockState(new BlockPos(posX, posY, posZ)).getBlock();
                Block x1 = this.mc.theWorld.getBlockState(new BlockPos(posX + 1.0, posY, posZ)).getBlock();
                Block x2 = this.mc.theWorld.getBlockState(new BlockPos(posX - 1.0, posY, posZ)).getBlock();
                Block z1 = this.mc.theWorld.getBlockState(new BlockPos(posX, posY, posZ + 1.0)).getBlock();
                Block z2 = this.mc.theWorld.getBlockState(new BlockPos(posX, posY, posZ - 1.0)).getBlock();
                if (block != Blocks.trapped_chest) {
                    if (x1 == Blocks.chest) {
                        this.mc.getRenderManager();
                        this.mc.getRenderManager();
                        this.mc.getRenderManager();
                        this.mc.getRenderManager();
                        this.mc.getRenderManager();
                        this.mc.getRenderManager();
                        axisAlignedBB = new AxisAlignedBB(posX + 0.05000000074505806 - mc.getRenderManager().viewerPosX, posY - mc.getRenderManager().viewerPosY, posZ + 0.05000000074505806 - mc.getRenderManager().viewerPosZ, posX + 1.9500000476837158 - mc.getRenderManager().viewerPosX, posY + 0.8999999761581421 - mc.getRenderManager().viewerPosY, posZ + 0.949999988079071 - mc.getRenderManager().viewerPosZ);
                    } else if (z2 == Blocks.chest) {
                        this.mc.getRenderManager();
                        this.mc.getRenderManager();
                        this.mc.getRenderManager();
                        this.mc.getRenderManager();
                        this.mc.getRenderManager();
                        this.mc.getRenderManager();
                        axisAlignedBB = new AxisAlignedBB(posX + 0.05000000074505806 - mc.getRenderManager().viewerPosX, posY - mc.getRenderManager().viewerPosY, posZ + 0.05000000074505806 - mc.getRenderManager().viewerPosZ - 1.0, posX + 0.949999988079071 - mc.getRenderManager().viewerPosX, posY + 0.8999999761581421 - mc.getRenderManager().viewerPosY, posZ + 0.949999988079071 - mc.getRenderManager().viewerPosZ);
                    } else if (x1 != Blocks.chest && x2 != Blocks.chest && z1 != Blocks.chest && z2 != Blocks.chest) {
                        this.mc.getRenderManager();
                        this.mc.getRenderManager();
                        this.mc.getRenderManager();
                        this.mc.getRenderManager();
                        this.mc.getRenderManager();
                        this.mc.getRenderManager();
                        axisAlignedBB = new AxisAlignedBB(posX + 0.05000000074505806 - mc.getRenderManager().viewerPosX, posY - mc.getRenderManager().viewerPosY, posZ + 0.05000000074505806 - mc.getRenderManager().viewerPosZ, posX + 0.949999988079071 - mc.getRenderManager().viewerPosX, posY + 0.8999999761581421 - mc.getRenderManager().viewerPosY, posZ + 0.949999988079071 - mc.getRenderManager().viewerPosZ);
                    }
                } else if (x1 == Blocks.trapped_chest) {
                    this.mc.getRenderManager();
                    this.mc.getRenderManager();
                    this.mc.getRenderManager();
                    this.mc.getRenderManager();
                    this.mc.getRenderManager();
                    this.mc.getRenderManager();
                    axisAlignedBB = new AxisAlignedBB(posX + 0.05000000074505806 - mc.getRenderManager().viewerPosX, posY - mc.getRenderManager().viewerPosY, posZ + 0.05000000074505806 - mc.getRenderManager().viewerPosZ, posX + 1.9500000476837158 - mc.getRenderManager().viewerPosX, posY + 0.8999999761581421 - mc.getRenderManager().viewerPosY, posZ + 0.949999988079071 - mc.getRenderManager().viewerPosZ);
                } else if (z2 == Blocks.trapped_chest) {
                    this.mc.getRenderManager();
                    this.mc.getRenderManager();
                    this.mc.getRenderManager();
                    this.mc.getRenderManager();
                    this.mc.getRenderManager();
                    this.mc.getRenderManager();
                    axisAlignedBB = new AxisAlignedBB(posX + 0.05000000074505806 - mc.getRenderManager().viewerPosX, posY - mc.getRenderManager().viewerPosY, posZ + 0.05000000074505806 - mc.getRenderManager().viewerPosZ - 1.0, posX + 0.949999988079071 - mc.getRenderManager().viewerPosX, posY + 0.8999999761581421 - mc.getRenderManager().viewerPosY, posZ + 0.949999988079071 - mc.getRenderManager().viewerPosZ);
                } else if (x1 != Blocks.trapped_chest && x2 != Blocks.trapped_chest && z1 != Blocks.trapped_chest && z2 != Blocks.trapped_chest) {
                    this.mc.getRenderManager();
                    this.mc.getRenderManager();
                    this.mc.getRenderManager();
                    this.mc.getRenderManager();
                    this.mc.getRenderManager();
                    this.mc.getRenderManager();
                    axisAlignedBB = new AxisAlignedBB(posX + 0.05000000074505806 - mc.getRenderManager().viewerPosX, posY - mc.getRenderManager().viewerPosY, posZ + 0.05000000074505806 - mc.getRenderManager().viewerPosZ, posX + 0.949999988079071 - mc.getRenderManager().viewerPosX, posY + 0.8999999761581421 - mc.getRenderManager().viewerPosY, posZ + 0.949999988079071 - mc.getRenderManager().viewerPosZ);
                }
            } else {
                this.mc.getRenderManager();
                this.mc.getRenderManager();
                this.mc.getRenderManager();
                this.mc.getRenderManager();
                this.mc.getRenderManager();
                this.mc.getRenderManager();
                axisAlignedBB = new AxisAlignedBB(posX - mc.getRenderManager().viewerPosX, posY - mc.getRenderManager().viewerPosY, posZ - mc.getRenderManager().viewerPosZ, posX + 1.0 - mc.getRenderManager().viewerPosX, posY + 1.0 - mc.getRenderManager().viewerPosY, posZ + 1.0 - mc.getRenderManager().viewerPosZ);
            }
            if (axisAlignedBB == null) continue;
            float[] colors = this.getColorForTileEntity(tileEntity);
            GlStateManager.disableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.disableTexture2D();
            GlStateManager.disableDepth();
            GL11.glEnable((int)2848);
            drawCompleteBox(axisAlignedBB, 1.0f, ChestESP.toRGBAHex(colors[0] / 255.0f, colors[1] / 255.0f, colors[2] / 255.0f, 0.1254902f), ChestESP.toRGBAHex(colors[0] / 255.0f, colors[1] / 255.0f, colors[2] / 255.0f, 1.0f));
            GL11.glDisable((int)2848);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.enableBlend();
            GlStateManager.enableAlpha();
            AxisAlignedBB bb = null;
            if (tileEntity instanceof TileEntityChest) {
                block = this.mc.theWorld.getBlockState(new BlockPos(posX, posY, posZ)).getBlock();
                Block posX1 = this.mc.theWorld.getBlockState(new BlockPos(posX + 1.0, posY, posZ)).getBlock();
                Block posX2 = this.mc.theWorld.getBlockState(new BlockPos(posX - 1.0, posY, posZ)).getBlock();
                Block posZ1 = this.mc.theWorld.getBlockState(new BlockPos(posX, posY, posZ + 1.0)).getBlock();
                Block posZ2 = this.mc.theWorld.getBlockState(new BlockPos(posX, posY, posZ - 1.0)).getBlock();
                if (block != Blocks.trapped_chest) {
                    if (posX1 == Blocks.chest) {
                        bb = new AxisAlignedBB(posX + 0.05000000074505806, posY, posZ + 0.05000000074505806, posX + 1.9500000476837158, posY + 0.8999999761581421, posZ + 0.949999988079071);
                    } else if (posZ2 == Blocks.chest) {
                        bb = new AxisAlignedBB(posX + 0.05000000074505806, posY, posZ + 0.05000000074505806 - 1.0, posX + 0.949999988079071, posY + 0.8999999761581421, posZ + 0.949999988079071);
                    } else if (posX1 != Blocks.chest && posX2 != Blocks.chest && posZ1 != Blocks.chest && posZ2 != Blocks.chest) {
                        bb = new AxisAlignedBB(posX + 0.05000000074505806, posY, posZ + 0.05000000074505806, posX + 0.949999988079071, posY + 0.8999999761581421, posZ + 0.949999988079071);
                    }
                } else if (posX1 == Blocks.trapped_chest) {
                    bb = new AxisAlignedBB(posX + 0.05000000074505806, posY, posZ + 0.05000000074505806, posX + 1.9500000476837158, posY + 0.8999999761581421, posZ + 0.949999988079071);
                } else if (posZ2 == Blocks.trapped_chest) {
                    bb = new AxisAlignedBB(posX + 0.05000000074505806, posY, posZ + 0.05000000074505806 - 1.0, posX + 0.949999988079071, posY + 0.8999999761581421, posZ + 0.949999988079071);
                } else if (posX1 != Blocks.trapped_chest && posX2 != Blocks.trapped_chest && posZ1 != Blocks.trapped_chest && posZ2 != Blocks.trapped_chest) {
                    bb = new AxisAlignedBB(posX + 0.05000000074505806, posY, posZ + 0.05000000074505806, posX + 0.949999988079071, posY + 0.8999999761581421, posZ + 0.949999988079071);
                }
            } else {
                bb = new AxisAlignedBB(posX, posY, posZ, posX + 1.0, posY + 1.0, posZ + 1.0);
            }
            if (bb == null) break;
            Vector3d[] corners = new Vector3d[]{new Vector3d(bb.minX, bb.minY, bb.minZ), new Vector3d(bb.maxX, bb.maxY, bb.maxZ), new Vector3d(bb.minX, bb.maxY, bb.maxZ), new Vector3d(bb.minX, bb.minY, bb.maxZ), new Vector3d(bb.maxX, bb.minY, bb.maxZ), new Vector3d(bb.maxX, bb.minY, bb.minZ), new Vector3d(bb.maxX, bb.maxY, bb.minZ), new Vector3d(bb.minX, bb.maxY, bb.minZ)};
            GLUProjection.Projection result = null;
            Vec4f transformed = new Vec4f((float)scaledResolution.getScaledWidth() * 2.0f, (float)scaledResolution.getScaledHeight() * 2.0f, -1.0f, -1.0f);
            Vector3d[] arrvector3d = corners;
            int n = arrvector3d.length;
            int n2 = 0;
            while (n2 < n) {
                Vector3d vec = arrvector3d[n2];
                result = GLUProjection.getInstance().project(vec.x - this.mc.getRenderManager().viewerPosX, vec.y - this.mc.getRenderManager().viewerPosY, vec.z - this.mc.getRenderManager().viewerPosZ, GLUProjection.ClampMode.NONE, true);
                transformed.setX((float)Math.min((double)transformed.getX(), result.getX()));
                transformed.setY((float)Math.min((double)transformed.getY(), result.getY()));
                transformed.setW((float)Math.max((double)transformed.getW(), result.getX()));
                transformed.setH((float)Math.max((double)transformed.getH(), result.getY()));
                ++n2;
            }
            if (result == null) break;
        }
    }

    public static boolean isStorage(TileEntity entity) {
        if (!(entity instanceof TileEntityChest || entity instanceof TileEntityBrewingStand || entity instanceof TileEntityDropper || entity instanceof TileEntityDispenser || entity instanceof TileEntityFurnace || entity instanceof TileEntityHopper || entity instanceof TileEntityEnderChest)) {
            return false;
        }
        return true;
    }

    public static int toRGBAHex(float r, float g, float b, float a) {
        return ((int)(a * 255.0f) & 255) << 24 | ((int)(r * 255.0f) & 255) << 16 | ((int)(g * 255.0f) & 255) << 8 | (int)(b * 255.0f) & 255;
    }

    private float[] getColorForTileEntity(TileEntity entity) {
        if (entity instanceof TileEntityChest) {
            TileEntityChest chest = (TileEntityChest)entity;
            if (chest.getChestType() == 0) {
                return new float[]{180.0f, 160.0f, 0.0f, 255.0f};
            }
            if (chest.getChestType() == 1) {
                return new float[]{160.0f, 10.0f, 10.0f, 255.0f};
            }
        }
        if (entity instanceof TileEntityEnderChest) {
            return new float[]{0.0f, 160.0f, 100.0f, 255.0f};
        }
        if (entity instanceof TileEntityFurnace) {
            return new float[]{120.0f, 120.0f, 120.0f, 255.0f};
        }
        return new float[]{255.0f, 255.0f, 255.0f, 255.0f};
    }
}

