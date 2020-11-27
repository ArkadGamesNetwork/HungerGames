package fr.mrcubee.survivalgames.listeners.block;

import fr.mrcubee.survivalgames.SurvivalGames;
import org.bukkit.plugin.PluginManager;

public class RegisterListeners {
	
	public static void register(SurvivalGames survivalGames) {
		PluginManager pluginManager;

		if (survivalGames == null)
			return;
		pluginManager = survivalGames.getServer().getPluginManager();
		pluginManager.registerEvents(new BlockPhysics(survivalGames), survivalGames);

	}
}