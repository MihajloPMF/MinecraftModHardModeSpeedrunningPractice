package com.example.deadlyworld;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.world.entity.monster.Monster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeadlyWorld implements ModInitializer {

	public static final String MOD_ID = "deadly_world";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		DeadlyConfig.load();
		DeathPenalty.init();

		ServerEntityEvents.ENTITY_LOAD.register((entity, level) -> {
			if (entity instanceof Monster monster) {
				TimeScaling.scaleMonster(monster, level);
			}
		});

		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			float rate = DeadlyConfig.get().tickRate;
			if (rate > 0) {
				server.tickRateManager().setTickRate(rate);
				LOGGER.info("Tick rate postavljen na {}", rate);
			}
		});

		LOGGER.info("Deadly World ucitan");
	}
}
