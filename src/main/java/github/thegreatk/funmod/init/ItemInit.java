package github.thegreatk.funmod.init;

import github.thegreatk.funmod.FunMod;
import github.thegreatk.funmod.items.TNTArrowItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ItemInit {

	private ItemInit() {
	};

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FunMod.MODID);

	public static final RegistryObject<Item> TNT_ARROW = ITEMS.register("tnt_arrow",
			() -> new TNTArrowItem(new Item.Properties().tab(FunMod.FUN_MOD_TAB), 6.0F));

	public static void register(IEventBus bus) {
		ITEMS.register(bus);
	}

}
