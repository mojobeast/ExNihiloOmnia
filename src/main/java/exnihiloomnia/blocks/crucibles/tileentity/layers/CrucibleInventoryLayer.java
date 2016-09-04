package exnihiloomnia.blocks.crucibles.tileentity.layers;

import exnihiloomnia.registries.crucible.CrucibleRegistry;
import exnihiloomnia.registries.crucible.CrucibleRegistryEntry;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.Constants;
import exnihiloomnia.blocks.ENOBlocks;
import exnihiloomnia.blocks.crucibles.tileentity.TileEntityCrucible;

public class CrucibleInventoryLayer extends CrucibleFluidLayer implements ISidedInventory{
	protected ItemStack lastItemAdded = null;
	private final static int[] TOP_SLOTS = new int[] {0};

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return null;
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return null;
	}

    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        return null;
    }

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		if (isItemValidForSlot(index, stack))
		{
			lastItemAdded = stack;
			
			TileEntityCrucible crucible = (TileEntityCrucible)this;
			crucible.addSolid(CrucibleRegistry.getItem(Block.getBlockFromItem(stack.getItem()), stack.getMetadata()).solidVolume);
			crucible.sync();
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return false;
	}

	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if (index == 0 && stack != null && CrucibleRegistry.containsItem(Block.getBlockFromItem(stack.getItem()), stack.getMetadata()))
		{
            CrucibleRegistryEntry block = CrucibleRegistry.getItem(Block.getBlockFromItem(stack.getItem()), stack.getMetadata());

            TileEntityCrucible crucible = (TileEntityCrucible)this;
			return lastItemAdded == null ? fluid == null ? crucible.hasSpaceFor(block.solidVolume) : fluid.getFluid() == block.fluid && crucible.hasSpaceFor(block.solidVolume) : lastItemAdded.getItem().equals(stack.getItem()) && crucible.hasSpaceFor(block.solidVolume);
		}
		
		return false;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {}

    @Override
    public String getName()
    {
        return ENOBlocks.CRUCIBLE.getUnlocalizedName();
    }

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public ITextComponent getDisplayName() {
		return null;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
        return TOP_SLOTS;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack stack, EnumFacing direction) {
		return direction == EnumFacing.UP && isItemValidForSlot(index, stack);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);

		NBTTagList items = compound.getTagList("items", Constants.NBT.TAG_COMPOUND);
		if (items.tagCount() > 0)
		{
			NBTTagCompound item = items.getCompoundTagAt(0);
			lastItemAdded = ItemStack.loadItemStackFromNBT(item);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);

		NBTTagList items = new NBTTagList();
		if (lastItemAdded != null)
		{
			NBTTagCompound item = new NBTTagCompound();
			lastItemAdded.writeToNBT(item);
			items.appendTag(item);
		}
		compound.setTag("items", items);
		return compound;
	}
}