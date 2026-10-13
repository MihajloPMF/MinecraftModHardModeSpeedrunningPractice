package com.example.deadlyworld.mixin;

import com.example.deadlyworld.TimeScaling;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

	@ModifyVariable(method = "hurtServer", at = @At("HEAD"), argsOnly = true)
	private float deadlyWorld$scaleMonsterDamage(float amount, ServerLevel level, DamageSource source) {
		if (!((Object) this instanceof Player)) {
			return amount;
		}
		if (!(source.getEntity() instanceof Monster)) {
			return amount;
		}
		return (float) (amount * TimeScaling.damageMultiplier(level.getGameTime()));
	}
}
