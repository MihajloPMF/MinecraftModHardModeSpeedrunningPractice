package com.example.deadlyworld;

import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;

public final class TimeScaling {

	public static final Identifier HEALTH_MODIFIER_ID =
			Identifier.fromNamespaceAndPath(DeadlyWorld.MOD_ID, "time_scaling_health");

	private TimeScaling() {
	}

	public static long ticksPerLevel() {
		DeadlyConfig cfg = DeadlyConfig.get();
		double tps = cfg.tickRate > 0 ? cfg.tickRate : 20.0;
		return Math.max(1L, Math.round(cfg.intervalRealMinutes * 60.0 * tps));
	}

	public static long level(long gameTime) {
		return gameTime / ticksPerLevel();
	}

	public static double healthBonus(long gameTime) {
		DeadlyConfig cfg = DeadlyConfig.get();
		return Math.min(level(gameTime) * cfg.healthBonusPerLevel, cfg.maxHealthBonus);
	}

	public static double damageBonus(long gameTime) {
		DeadlyConfig cfg = DeadlyConfig.get();
		return Math.min(level(gameTime) * cfg.damageBonusPerLevel, cfg.maxDamageBonus);
	}

	public static double damageMultiplier(long gameTime) {
		DeadlyConfig cfg = DeadlyConfig.get();
		double timeFactor = cfg.timeScalingEnabled ? 1.0 + damageBonus(gameTime) : 1.0;
		return cfg.monsterDamageMultiplier * timeFactor;
	}

	public static void scaleMonster(Monster mob, ServerLevel level) {
		if (!DeadlyConfig.get().timeScalingEnabled) {
			return;
		}
		AttributeInstance maxHealth = mob.getAttribute(Attributes.MAX_HEALTH);
		if (maxHealth == null || maxHealth.hasModifier(HEALTH_MODIFIER_ID)) {
			return;
		}
		double bonus = healthBonus(level.getGameTime());
		maxHealth.addPermanentModifier(new AttributeModifier(
				HEALTH_MODIFIER_ID, bonus, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
		mob.setHealth(mob.getMaxHealth());
	}
}
