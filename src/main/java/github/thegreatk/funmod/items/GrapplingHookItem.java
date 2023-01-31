package github.thegreatk.funmod.items;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class GrapplingHookItem extends Item {

	public GrapplingHookItem(Properties p) {
		super(p);
	}

	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand use) {

		ItemStack itemstack = player.getItemInHand(use);
		if (player.fishing != null) {
			if (!level.isClientSide) {
				int i = player.fishing.retrieve(itemstack);
				itemstack.hurtAndBreak(i, player, (p_41288_) -> {
					p_41288_.broadcastBreakEvent(use);
				});
			}

			level.playSound((Player) null, player.getX(), player.getY(), player.getZ(),
					SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.NEUTRAL, 1.0F,
					0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
			level.gameEvent(player, GameEvent.FISHING_ROD_REEL_IN, player);
		} else {
			level.playSound((Player) null, player.getX(), player.getY(), player.getZ(),
					SoundEvents.FISHING_BOBBER_THROW, SoundSource.NEUTRAL, 0.5F,
					0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
			if (!level.isClientSide) {
				int k = EnchantmentHelper.getFishingSpeedBonus(itemstack);
				int j = EnchantmentHelper.getFishingLuckBonus(itemstack);
				level.addFreshEntity(new FishingHook(player, level, j, k));
			}

			player.awardStat(Stats.ITEM_USED.get(this));
			level.gameEvent(player, GameEvent.FISHING_ROD_CAST, player);
		}

		return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
	}

}
