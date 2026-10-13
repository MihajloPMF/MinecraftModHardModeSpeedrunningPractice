package com.example.deadlyworld;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public final class DeathPenalty {

	private static final Identifier MODIFIER_ID =
			Identifier.fromNamespaceAndPath(DeadlyWorld.MOD_ID, "death_penalty");

	public static final AttachmentType<Integer> DEATHS = AttachmentRegistry.<Integer>builder()
			.persistent(Codec.INT)
			.copyOnDeath()
			.initializer(() -> 0)
			.buildAndRegister(Identifier.fromNamespaceAndPath(DeadlyWorld.MOD_ID, "deaths"));

	private DeathPenalty() {
	}

	public static void init() {
		ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
			if (!DeadlyConfig.get().deathPenaltyEnabled) {
				return;
			}
			if (!alive) {
				newPlayer.setAttached(DEATHS, newPlayer.getAttachedOrElse(DEATHS, 0) + 1);
			}
			apply(newPlayer);
			newPlayer.setHealth(newPlayer.getMaxHealth());
		});

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			if (DeadlyConfig.get().deathPenaltyEnabled) {
				apply(handler.getPlayer());
			}
		});
	}

	private static void apply(ServerPlayer player) {
		AttributeInstance maxHealth = player.getAttribute(Attributes.MAX_HEALTH);
		if (maxHealth == null) {
			return;
		}
		DeadlyConfig cfg = DeadlyConfig.get();
		maxHealth.removeModifier(MODIFIER_ID);

		int deaths = player.getAttachedOrElse(DEATHS, 0);
		double maxLoss = Math.max(0.0, maxHealth.getBaseValue() - cfg.minMaxHealth);
		double loss = Math.min(deaths * cfg.hpLostPerDeath, maxLoss);
		if (loss > 0) {
			maxHealth.addPermanentModifier(new AttributeModifier(
					MODIFIER_ID, -loss, AttributeModifier.Operation.ADD_VALUE));
		}
		if (player.getHealth() > player.getMaxHealth()) {
			player.setHealth(player.getMaxHealth());
		}
	}
}
