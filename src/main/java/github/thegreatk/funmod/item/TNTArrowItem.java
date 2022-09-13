package github.thegreatk.funmod.item;

import github.thegreatk.funmod.world.entity.projectile.TNTArrow;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TNTArrowItem extends ArrowItem {
	public TNTArrowItem(Properties properties) {
		super(properties);
	}

	@Override
	public AbstractArrow createArrow(Level level, ItemStack stack, LivingEntity shooter) {
		return new TNTArrow(level, shooter, this);
	}
}