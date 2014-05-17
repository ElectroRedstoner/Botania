/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Apr 13, 2014, 7:21:32 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.tool;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibItemNames;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemManasteelSword extends ItemSword {

	static final int MANA_PER_DAMAGE = 51;

	IIcon elucidatorIcon;

	public ItemManasteelSword() {
		this(BotaniaAPI.manasteelToolMaterial, LibItemNames.MANASTEEL_SWORD);
	}

	public ItemManasteelSword(ToolMaterial mat, String name) {
		super(mat);
		setCreativeTab(BotaniaCreativeTab.INSTANCE);
		setUnlocalizedName(name);
	}

	@Override
	public Item setUnlocalizedName(String par1Str) {
		GameRegistry.registerItem(this, par1Str);
		return super.setUnlocalizedName(par1Str);
	}

	@Override
	public String getUnlocalizedNameInefficiently(ItemStack par1ItemStack) {
		return super.getUnlocalizedNameInefficiently(par1ItemStack).replaceAll("item.", "item." + LibResources.PREFIX_MOD);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		itemIcon = IconHelper.forItem(par1IconRegister, this);
		elucidatorIcon = IconHelper.forName(par1IconRegister, "elucidator");
	}

	@Override
	public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase) {
		ManasteelToolCommons.damageItem(par1ItemStack, 1, par3EntityLivingBase, MANA_PER_DAMAGE);
		return true;
	}

	@Override
	public IIcon getIconIndex(ItemStack par1ItemStack) {
		return par1ItemStack.getDisplayName().equals("The Elucidator") ? elucidatorIcon : super.getIconIndex(par1ItemStack);
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		return getIconIndex(stack);
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x, int y, int z, EntityLivingBase entity) {
		if (block.getBlockHardness(world, x, y, z) != 0F)
			ManasteelToolCommons.damageItem(stack, 1, entity, MANA_PER_DAMAGE);

		return true;
	}

	@Override
	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
		return par2ItemStack.getItem() == ModItems.manaResource && par2ItemStack.getItemDamage() == 0 ? true : super.getIsRepairable(par1ItemStack, par2ItemStack);
	}

}
