package github.thegreatk.funmod.init;

import github.thegreatk.funmod.FunMod;
import github.thegreatk.funmod.entity.projectile.GrapplingHook;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityInit {

	private EntityInit() {
	};

	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES,
			FunMod.MODID);

	public static final RegistryObject<EntityType<GrapplingHook>> GRAPPLING_HOOK = ENTITIES.register("grappling_hook",
			() -> EntityType.Builder.<GrapplingHook>of(GrapplingHook::new, MobCategory.MISC).noSave().noSummon()
					.sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(5)
					.build(new ResourceLocation(FunMod.MODID, "grappling_hook").toString()));

	public static void register(IEventBus bus) {
		ENTITIES.register(bus);
	}

}
