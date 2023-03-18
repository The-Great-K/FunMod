package github.thegreatk.funmod.entity.projectile;

import java.util.Collections;
import java.util.Random;

import javax.annotation.Nullable;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class GrapplingHook extends FishingHook {
	private static final Logger LOGGER = LogUtils.getLogger();
	private final Random syncronizedRandom = new Random();
	private static final EntityDataAccessor<Integer> DATA_HOOKED_ENTITY = SynchedEntityData
			.defineId(GrapplingHook.class, EntityDataSerializers.INT);
	private int life;
	private int damage = 2;
	@Nullable
	private Entity hookedIn;
	private GrapplingHook.GrapplingHookState currentState = GrapplingHook.GrapplingHookState.FLYING;

	private GrapplingHook(EntityType<? extends GrapplingHook> type, Level level, int enchantment$1, int enchantment$2) {
		super(type, level);
	}

	public GrapplingHook(EntityType<? extends GrapplingHook> type, Level level) {
		this(type, level, 0, 0);
	}

	protected void defineSynchedData() {
		this.getEntityData().define(DATA_HOOKED_ENTITY, 0);
	}

	public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
		if (DATA_HOOKED_ENTITY.equals(accessor)) {
			int i = this.getEntityData().get(DATA_HOOKED_ENTITY);
			this.hookedIn = i > 0 ? this.level.getEntity(i - 1) : null;
		}

		super.onSyncedDataUpdated(accessor);
	}

	public boolean shouldRenderAtSqrDistance(double distance) {
		double d0 = 64.0D;
		return distance < 4096.0D;
	}

	public void tick() {
		this.syncronizedRandom.setSeed(this.getUUID().getLeastSignificantBits() ^ this.level.getGameTime());
		super.tick();
		Player player = this.getPlayerOwner();
		if (player == null) {
			this.discard();
		} else if (this.level.isClientSide || !this.shouldStopFishing(player)) {
			if (this.onGround) {
				++this.life;
				if (this.life >= 4800) {
					this.discard();
					return;
				}
			} else {
				this.life = 0;
			}
			BlockPos blockpos = this.blockPosition();

			if (this.currentState == GrapplingHook.GrapplingHookState.FLYING) {
				if (this.hookedIn != null) {
					this.setDeltaMovement(Vec3.ZERO);
					this.currentState = GrapplingHook.GrapplingHookState.HOOKED_IN_ENTITY;
					return;
				}

				this.checkCollision();
			} else {
				if (this.currentState == GrapplingHook.GrapplingHookState.HOOKED_IN_ENTITY) {
					if (this.hookedIn != null) {
						if (!this.hookedIn.isRemoved() && this.hookedIn.level.dimension() == this.level.dimension()) {
							this.setPos(this.hookedIn.getX(), this.hookedIn.getY(0.8D), this.hookedIn.getZ());
						} else {
							this.setHookedEntity((Entity) null);
							this.currentState = GrapplingHook.GrapplingHookState.FLYING;
						}
					}

					return;
				}

			}

			this.move(MoverType.SELF, this.getDeltaMovement());
			this.updateRotation();
			if (this.currentState == GrapplingHook.GrapplingHookState.FLYING
					&& (this.onGround || this.horizontalCollision)) {
				this.setDeltaMovement(Vec3.ZERO);
			}

			double d1 = 0.92D;
			this.setDeltaMovement(this.getDeltaMovement().scale(0.92D));
			this.reapplyPosition();
		}
	}

	private boolean shouldStopFishing(Player player) {
		ItemStack itemstack = player.getMainHandItem();
		ItemStack itemstack1 = player.getOffhandItem();
		boolean flag = itemstack.is(Items.FISHING_ROD);
		boolean flag1 = itemstack1.is(Items.FISHING_ROD);
		if (!player.isRemoved() && player.isAlive() && (flag || flag1) && !(this.distanceToSqr(player) > 1024.0D)) {
			return false;
		} else {
			this.discard();
			return true;
		}
	}

	private void checkCollision() {
		HitResult hitresult = ProjectileUtil.getHitResult(this, this::canHitEntity);
		if (hitresult.getType() == HitResult.Type.MISS
				|| !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult))
			this.onHit(hitresult);
	}

	protected boolean canHitEntity(Entity entity) {
		return super.canHitEntity(entity) || entity.isAlive() && entity instanceof ItemEntity;
	}

	protected void onHitEntity(EntityHitResult result) {
		super.onHitEntity(result);
		if (!this.level.isClientSide) {
			this.setHookedEntity(result.getEntity());
		}

	}

	protected void onHitBlock(BlockHitResult result) {
		super.onHitBlock(result);
		this.setDeltaMovement(this.getDeltaMovement().normalize().scale(result.distanceTo(this)));
	}

	private void setHookedEntity(@Nullable Entity entity) {
		this.hookedIn = entity;
		this.getEntityData().set(DATA_HOOKED_ENTITY, entity == null ? 0 : entity.getId() + 1);
	}

	public void addAdditionalSaveData(CompoundTag tag) {
	}

	public void readAdditionalSaveData(CompoundTag tag) {
	}

	public int retrieve(ItemStack item) {
		Player player = this.getPlayerOwner();
		if (!this.level.isClientSide && player != null && !this.shouldStopFishing(player)) {
			int i = 0;
			if (this.hookedIn != null) {
				this.damageEntity(this.hookedIn);
				CriteriaTriggers.FISHING_ROD_HOOKED.trigger((ServerPlayer) player, item, this, Collections.emptyList());
				this.level.broadcastEntityEvent(this, (byte) 31);
				i = this.hookedIn instanceof ItemEntity ? 3 : 5;
			}

			if (this.onGround) {
				i = 2;
			}

			this.discard();
		}
		return damage;
	}

	public void handleEntityEvent(byte i) {
		if (i == 31 && this.level.isClientSide && this.hookedIn instanceof Player
				&& ((Player) this.hookedIn).isLocalPlayer()) {
			this.damageEntity(this.hookedIn);
		}

		super.handleEntityEvent(i);
	}

	protected void damageEntity(Entity entity) {
		Entity player = this.getOwner();
		if (player != null) {
			Vec3 vec3 = (new Vec3(player.getX() - this.getX(), player.getY() - this.getY(),
					player.getZ() - this.getZ())).scale(0.1D);
			entity.setDeltaMovement(entity.getDeltaMovement().add(vec3));
		}
	}

	protected Entity.MovementEmission getMovementEmission() {
		return Entity.MovementEmission.NONE;
	}

	public void remove(Entity.RemovalReason reason) {
		this.updateOwnerInfo((GrapplingHook) null);
		super.remove(reason);
	}

	public void onClientRemoval() {
		this.updateOwnerInfo((GrapplingHook) null);
	}

	public void setOwner(@Nullable Entity entity) {
		super.setOwner(entity);
		this.updateOwnerInfo(this);
	}

	private void updateOwnerInfo(@Nullable GrapplingHook hook) {
		Player player = this.getPlayerOwner();
		if (player != null) {
			player.fishing = hook;
		}

	}

	@Nullable
	public Player getPlayerOwner() {
		Entity entity = this.getOwner();
		return entity instanceof Player ? (Player) entity : null;
	}

	@Nullable
	public Entity getHookedIn() {
		return this.hookedIn;
	}

	public boolean canChangeDimensions() {
		return false;
	}

	public Packet<?> getAddEntityPacket() {
		Entity entity = this.getOwner();
		return new ClientboundAddEntityPacket(this, entity == null ? this.getId() : entity.getId());
	}

	public void recreateFromPacket(ClientboundAddEntityPacket packet) {
		super.recreateFromPacket(packet);
		if (this.getPlayerOwner() == null) {
			int i = packet.getData();
			LOGGER.error("Failed to recreate fishing hook on client. {} (id: {}) is not a valid owner.",
					this.level.getEntity(i), i);
			this.kill();
		}

	}

	static enum GrapplingHookState {
		FLYING, HOOKED_IN_ENTITY, BOBBING;
	}

	static enum OpenWaterType {
		ABOVE_WATER, INSIDE_WATER, INVALID;
	}
}
