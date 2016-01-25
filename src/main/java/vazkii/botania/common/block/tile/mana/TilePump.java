/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 18, 2015, 3:16:57 PM (GMT)]
 */
package vazkii.botania.common.block.tile.mana;

import com.google.common.collect.ImmutableMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.animation.Animation;
import net.minecraftforge.client.model.animation.IAnimationProvider;
import net.minecraftforge.client.model.animation.ITimeValue;
import net.minecraftforge.client.model.animation.TimeValues;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.common.block.tile.TileMod;

public class TilePump extends TileMod implements IAnimationProvider {

	private static final String TAG_ACTIVE = "active";

	public float innerRingPos;
	public boolean active = false;
	public boolean hasCart = false;
	public boolean hasCartOnTop = false;
	public float moving = 0F;

	public int comparator;
	public boolean hasRedstone = false;
	int lastComparator = 0;

	private final TimeValues.VariableValue move;
	private final IAnimationStateMachine asm;

	public TilePump() {
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			move = new TimeValues.VariableValue(0);
			asm = Animation.INSTANCE.load(new ResourceLocation("botania", "asms/block/pump.json"), ImmutableMap.of());
		} else {
			move = null;
			asm = null;
		}
	}

	@Override
	public void updateEntity() {
		hasRedstone = false;
		for(EnumFacing dir : EnumFacing.VALUES) {
			int redstoneSide = worldObj.getRedstonePower(pos.offset(dir), dir);
			if(redstoneSide > 0) {
				hasRedstone = true;
				break;
			}
		}

		float max = 8F;
		float min = 0F;

		float incr = max / 10F;

		if(innerRingPos < max && active && moving >= 0F) {
			innerRingPos += incr;
			moving = incr;
			if(innerRingPos >= max) {
				innerRingPos = Math.min(max, innerRingPos);
				moving = 0F;
				for(int x = 0; x < 2; x++)
					worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, getPos().getX() + Math.random(), getPos().getY() + Math.random(), getPos().getZ() + Math.random(), 0, 0, 0);
			}
		} else if(innerRingPos > min) {
			innerRingPos -= incr * 2;
			moving = -incr * 2;
			if(innerRingPos <= min) {
				innerRingPos = Math.max(min, innerRingPos);
				moving = 0F;
			}
		}
		move.setValue(innerRingPos);


		if(!hasCartOnTop)
			comparator = 0;
		if(!hasCart && active) {

			setActive(false);
		}
		if(active && hasRedstone)
			setActive(false);

		hasCart = false;
		hasCartOnTop = false;

		if(comparator != lastComparator)
			worldObj.updateComparatorOutputLevel(pos, worldObj.getBlockState(pos).getBlock());
		lastComparator = comparator;
	}

	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		cmp.setBoolean(TAG_ACTIVE, active);
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		active = cmp.getBoolean(TAG_ACTIVE);
	}

	public void setActive(boolean active) {
		if(!worldObj.isRemote) {
			boolean diff = this.active != active;
			this.active = active;
			if(diff)
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(worldObj, pos);
		}
	}

	@Override
	public IAnimationStateMachine asm() {
		return asm;
	}
}
