package github.thegreatk.funmod.init;

import github.thegreatk.funmod.FunMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ItemInit {

	private ItemInit() {
	};

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FunMod.MODID);

	public static void register(IEventBus bus) {
		ITEMS.register(bus);
	}

}
