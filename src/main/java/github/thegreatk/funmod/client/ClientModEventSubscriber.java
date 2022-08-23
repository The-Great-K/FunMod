package github.thegreatk.funmod.client;

import github.thegreatk.funmod.FunMod;
import github.thegreatk.funmod.client.renderer.entity.TNTArrowRenderer;
import github.thegreatk.funmod.world.entity.ModEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FunMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEventSubscriber {

	@SubscribeEvent
	public static void onClientSetup(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(ModEntityType.TNT_ARROW.get(), TNTArrowRenderer::new);
	}

}
