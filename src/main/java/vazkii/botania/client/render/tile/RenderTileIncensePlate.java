/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [May 15, 2015, 4:27:27 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelIncensePlate;
import vazkii.botania.common.block.tile.TileIncensePlate;

public class RenderTileIncensePlate extends TileEntitySpecialRenderer {

	private static final float[] ROTATIONS = new float[] {
		180F, 0F, 90F, 270F
	};

	ResourceLocation texture = new ResourceLocation(LibResources.MODEL_INCENSE_PLATE);
	ModelIncensePlate model = new ModelIncensePlate();

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float ticks, int digProgress) {
		TileIncensePlate plate = (TileIncensePlate) tileentity;

		EnumFacing facing = plate.getWorld() != null ? ((EnumFacing) plate.getWorld().getBlockState(plate.getPos()).getValue(BotaniaStateProps.CARDINALS)) : EnumFacing.SOUTH;

		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.translate(d0, d1, d2);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		GlStateManager.translate(0.5F, 1.5F, 0.5F);
		GlStateManager.scale(1F, -1F, -1F);
		GlStateManager.rotate(ROTATIONS[Math.max(Math.min(ROTATIONS.length, facing.getIndex() - 2), 0)], 0F, 1F, 0F);
		model.render();
		GlStateManager.scale(1F, -1F, -1F);

		ItemStack stack = plate.getStackInSlot(0);
		if(stack != null) {
			GlStateManager.pushMatrix();
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
			float s = 0.4F;
			GlStateManager.translate(0.1F, -1.46F, 0F);
			GlStateManager.scale(s, s, s);
			GlStateManager.rotate(180F, 0F, 1F, 0F);
			Minecraft.getMinecraft().getRenderItem().renderItemModel(stack); // todo 1.8
//			int renderPass = 0;
//			do {
//				IIcon icon = stack.getItem().getIcon(stack, renderPass);
//				if(icon != null) {
//					Color color = new Color(stack.getItem().getColorFromItemStack(stack, renderPass));
//					GlStateManager.color((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue());
//					float f = icon.getMinU();
//					float f1 = icon.getMaxU();
//					float f2 = icon.getMinV();
//					float f3 = icon.getMaxV();
//					ItemRenderer.renderItemIn2D(Tessellator.getInstance(), f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1F / 32F);
//					GlStateManager.color(1F, 1F, 1F);
//				}
//				renderPass++;
//			} while(renderPass < stack.getItem().getRenderPasses(stack.getItemDamage()));
			GlStateManager.popMatrix();
		}
		GlStateManager.color(1F, 1F, 1F);
		GlStateManager.enableRescaleNormal();
		GlStateManager.popMatrix();
	}

}
