package fr.mrcubee.survivalgames.listeners.entity;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import fr.mrcubee.survivalgames.GameStats;
import fr.mrcubee.survivalgames.SurvivalGames;

public class EntityDamage implements Listener {
	
	private SurvivalGames survivalGames;
	
	public EntityDamage(SurvivalGames survivalGames) {
		this.survivalGames = survivalGames;
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void entityDamageEvent(EntityDamageEvent event) {
		GameStats gameStats = survivalGames.getGame().getGameStats();

		if (gameStats != GameStats.DURING) {
			event.setCancelled(true);
			if (event.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION)  {
				if (event.getEntityType() == EntityType.PLAYER)
					event.getEntity().teleport(event.getEntity().getLocation().add(0, 3.5, 0));
				else
					event.getEntity().remove();
			}
		}
	}
}
