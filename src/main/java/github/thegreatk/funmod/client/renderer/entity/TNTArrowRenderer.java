package github.thegreatk.funmod.client.renderer.entity;

import github.thegreatk.funmod.FunMod;
import github.thegreatk.funmod.world.entity.projectile.TNTArrow;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TNTArrowRenderer extends ArrowRenderer<TNTArrow> {

	public TNTArrowRenderer(Context context) {
		super(context);
	}

	public ResourceLocation getTextureLocation(TNTArrow arrow) {
		Item referenceItem = arrow.getPickupItem().getItem();
		return new ResourceLocation(FunMod.MODID,
				"textures/entity/projectiles/" + referenceItem.getRegistryName().getPath() + ".png");
	}
}