package github.thegreatk.funmod.client.renderer.entity;

import github.thegreatk.funmod.FunMod;
import github.thegreatk.funmod.world.entity.projectile.TNTArrow;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TNTArrowRenderer extends ArrowRenderer<TNTArrow> {

	public TNTArrowRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(TNTArrow entity) {
		Item referenceItem = entity.getPickupItem().getItem();
		return new ResourceLocation(FunMod.MODID,
				"textures/entity/projectiles/" + referenceItem.getRegistryName().getPath() + ".png");
	}

}
