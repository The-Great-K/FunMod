package github.thegreatk.funmod;

import github.thegreatk.funmod.init.ItemInit;
import github.thegreatk.funmod.world.entity.ModEntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(FunMod.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class FunMod {

	public static final String MODID = "funmod";
	public static final String MODNAME = "Fun Mod";
	public static String VERSION = "1.0";

	public FunMod() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		ItemInit.register(bus);
		ModEntityType.register(bus);

		MinecraftForge.EVENT_BUS.register(this);
	}

	public static final CreativeModeTab FUN_MOD_TAB = new CreativeModeTab(MODID) {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(Blocks.DEEPSLATE_DIAMOND_ORE);
		}
	};

}
