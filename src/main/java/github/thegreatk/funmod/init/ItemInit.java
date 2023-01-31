package github.thegreatk.funmod.init;

import github.thegreatk.funmod.FunMod;
import github.thegreatk.funmod.items.GrapplingHookItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ItemInit {

	private ItemInit() {
	};

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FunMod.MODID);

	public static final RegistryObject<Item> GRAPPLING_HOOK = ITEMS.register("grappling_hook",
			() -> new GrapplingHookItem(new Item.Properties().tab(FunMod.FUN_MOD_TAB)));

	public static void register(IEventBus bus) {
		ITEMS.register(bus);
	}

}
