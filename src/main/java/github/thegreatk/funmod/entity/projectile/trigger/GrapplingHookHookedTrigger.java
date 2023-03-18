package github.thegreatk.funmod.entity.projectile.trigger;

import java.util.Collection;

import com.google.gson.JsonObject;

import github.thegreatk.funmod.entity.projectile.GrapplingHook;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class GrapplingHookHookedTrigger extends SimpleCriterionTrigger<GrapplingHookHookedTrigger.TriggerInstance> {
	static final ResourceLocation ID = new ResourceLocation("grappling_hook_hooked");

	public ResourceLocation getId() {
		return ID;
	}

	public GrapplingHookHookedTrigger.TriggerInstance createInstance(JsonObject obj,
			EntityPredicate.Composite composite, DeserializationContext context) {
		ItemPredicate itempredicate = ItemPredicate.fromJson(obj.get("rod"));
		EntityPredicate.Composite entitypredicate$composite = EntityPredicate.Composite.fromJson(obj, "entity",
				context);
		ItemPredicate itempredicate1 = ItemPredicate.fromJson(obj.get("item"));
		return new GrapplingHookHookedTrigger.TriggerInstance(composite, itempredicate, entitypredicate$composite,
				itempredicate1);
	}

	public void trigger(ServerPlayer player, GrapplingHook rod, Collection<ItemStack> collection) {
		LootContext lootcontext = EntityPredicate.createContext(player,
				(Entity) (rod.getHookedIn() != null ? rod.getHookedIn() : rod));
		this.trigger(player, (obj) -> {
			return obj.matches(lootcontext, collection);
		});
	}

	public static class TriggerInstance extends AbstractCriterionTriggerInstance {
		private final ItemPredicate rod;
		private final EntityPredicate.Composite entity;
		private final ItemPredicate item;

		public TriggerInstance(EntityPredicate.Composite player, ItemPredicate rod, EntityPredicate.Composite entity,
				ItemPredicate item) {
			super(GrapplingHookHookedTrigger.ID, player);
			this.rod = rod;
			this.entity = entity;
			this.item = item;
		}

		public boolean matches(LootContext lootContext, Collection<ItemStack> loot) {
			if (!this.entity.matches(lootContext)) {
				return false;
			} else {
				if (this.item != ItemPredicate.ANY) {
					boolean flag = false;
					Entity entity = lootContext.getParamOrNull(LootContextParams.THIS_ENTITY);
					if (entity instanceof ItemEntity) {
						ItemEntity itementity = (ItemEntity) entity;
						if (this.item.matches(itementity.getItem())) {
							flag = true;
						}
					}

					for (ItemStack itemstack : loot) {
						if (this.item.matches(itemstack)) {
							flag = true;
							break;
						}
					}

					if (!flag) {
						return false;
					}
				}

				return true;
			}
		}

		public JsonObject serializeToJson(SerializationContext context) {
			JsonObject jsonobject = super.serializeToJson(context);
			jsonobject.add("rod", this.rod.serializeToJson());
			jsonobject.add("entity", this.entity.toJson(context));
			jsonobject.add("item", this.item.serializeToJson());
			return jsonobject;
		}
	}
}