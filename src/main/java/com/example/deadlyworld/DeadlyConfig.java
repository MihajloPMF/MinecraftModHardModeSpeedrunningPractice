package com.example.deadlyworld;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public final class DeadlyConfig {

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static DeadlyConfig instance = new DeadlyConfig();

	public float tickRate = 40.0f;

	public double monsterDamageMultiplier = 1.2;

	public boolean timeScalingEnabled = true;
	public double intervalRealMinutes = 15.0;
	public double healthBonusPerLevel = 0.03;
	public double damageBonusPerLevel = 0.03;
	public double maxHealthBonus = 3.0;
	public double maxDamageBonus = 3.0;

	public boolean deathPenaltyEnabled = true;
	public double hpLostPerDeath = 1.0;
	public double minMaxHealth = 6.0;

	public static DeadlyConfig get() {
		return instance;
	}

	public static void load() {
		Path file = FabricLoader.getInstance().getConfigDir().resolve(DeadlyWorld.MOD_ID + ".json");
		try {
			Files.createDirectories(file.getParent());
		} catch (IOException e) {
			DeadlyWorld.LOGGER.error("Ne mogu da napravim config direktorijum {}", file.getParent(), e);
			return;
		}
		if (Files.exists(file)) {
			try (Reader reader = Files.newBufferedReader(file)) {
				DeadlyConfig loaded = GSON.fromJson(reader, DeadlyConfig.class);
				if (loaded != null) {
					instance = loaded;
				}
			} catch (Exception e) {
				DeadlyWorld.LOGGER.error("Ne mogu da procitam {}, koristim podrazumevane vrednosti", file, e);
			}
		}
		try (Writer writer = Files.newBufferedWriter(file)) {
			GSON.toJson(instance, writer);
		} catch (IOException e) {
			DeadlyWorld.LOGGER.error("Ne mogu da upisem {}", file, e);
		}
	}
}
