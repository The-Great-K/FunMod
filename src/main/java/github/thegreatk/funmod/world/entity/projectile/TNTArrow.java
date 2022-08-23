package github.thegreatk.funmod.world.entity.projectile;

import github.thegreatk.funmod.init.ItemInit;
import github.thegreatk.funmod.world.entity.ModEntityType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TNTArrow extends AbstractArrow {
	private final Item referenceItem;

	public TNTArrow(EntityType<? extends AbstractArrow> type, Level level) {
		super(type, level);
		this.referenceItem = ItemInit.TNT_ARROW.get();
	}

	public TNTArrow(LivingEntity attacker, Level level, Item referenceItem) {
		super(ModEntityType.TNT_ARROW.get(), attacker, level);
		this.referenceItem = referenceItem;
	}

	@Override
	public ItemStack getPickupItem() {
		return new ItemStack(this.referenceItem);
	}

}
