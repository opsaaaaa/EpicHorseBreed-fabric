package epic.horse.breed.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;


@Mixin(HorseBaseEntity.class)
public abstract class HorseBreedMixin extends AnimalEntity {

	protected HorseBreedMixin(EntityType<? extends HorseBaseEntity> entityType, World world) {
		super(entityType, world);
	}

	// Override the setChildAttributes method in the HorseBaseEntity 
	public void setChildAttributes(PassiveEntity mate, HorseBaseEntity child) {

		// Average the parents stats.
		double health = (this.getAttributeBaseValue(EntityAttributes.GENERIC_MAX_HEALTH)
				+ mate.getAttributeBaseValue(EntityAttributes.GENERIC_MAX_HEALTH)) * 0.5D;

		double jump = (this.getAttributeBaseValue(EntityAttributes.HORSE_JUMP_STRENGTH)
				+ mate.getAttributeBaseValue(EntityAttributes.HORSE_JUMP_STRENGTH)) * 0.5D;

		double speed = (this.getAttributeBaseValue(EntityAttributes.GENERIC_MOVEMENT_SPEED)
				+ mate.getAttributeBaseValue(EntityAttributes.GENERIC_MOVEMENT_SPEED)) * 0.5D;


		// Clamp the horse stats to the maximum and minimum values found in vanilla
		// And add some linear variation to the stats
		// ( also secretly increase maximum horse health by 10, shh ;) 
		health = this.clampStat(15D, 				 42D, 			health + this.getChildHealthAdjustment());
		jump =   this.clampStat(0.4000000059604645D, 1.000000006D,  jump + this.getChildJumpStrengthAdjustment());
		speed =  this.clampStat(0.112499997D,		 0.337499997D, 	speed + this.getChildMovementSpeedAdjustment());

		child.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(health);
		child.getAttributeInstance(EntityAttributes.HORSE_JUMP_STRENGTH).setBaseValue(jump);
		child.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(speed);
	}

	public double getChildHealthAdjustment() {
		// Adjust health by a maximum of 1/2 Hearts
		return 3D - (double)this.random.nextInt(6);
	}

	public double getChildJumpStrengthAdjustment() {
		// Adjust jump hight by roughly maximum of 1/2 blocks 
		return 0.25 * (0.5D - this.random.nextDouble());
	}

	public double getChildMovementSpeedAdjustment() {
		// Adjust movement speed by a maximum of 10%
		return 0.225D * 0.20D * (0.5D - this.random.nextDouble());
	}

	private double clampStat(double min, double max, double val) {
		return Math.max(min, Math.min(max, val));
	}

}
