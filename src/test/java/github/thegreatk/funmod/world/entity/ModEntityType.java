package github.thegreatk.funmod.world.entity;

import github.thegreatk.funmod.FunMod;
import github.thegreatk.funmod.world.entity.projectile.TNTArrow;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntityType {

	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES,
			FunMod.MODID);

	public static final RegistryObject<EntityType<TNTArrow>> TNT_ARROW = ENTITIES.register("tnt_arrow",
			() -> EntityType.Builder.<TNTArrow>of(TNTArrow::new, MobCategory.MISC).sized(0.5F, 0.5F)
					.clientTrackingRange(4).updateInterval(20)
					.build(new ResourceLocation(FunMod.MODID, "tnt_arrow").toString()));

	public static void register(IEventBus bus) {
		ENTITIES.register(bus);
	}

}
