package fr.Florent59.RaceAndClass;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodEvent implements Listener {
	
	@EventHandler (priority = EventPriority.LOW)
	public void ChangeFood(FoodLevelChangeEvent e){
		
		if(e.getEntity() instanceof Player){
			
			Player player = (Player) e.getEntity();
			int oldFoodLevel = player.getFoodLevel();
	        int newFoodLevel = e.getFoodLevel();
	        // Si le FoodLevel d'une entité change, on vérifie si c'est un joueur, puis on récupère l'ancien et le nouveau niveau.
			
			if (Main.joueursRaces.containsKey(player.getName()) && Main.joueursClasses.containsKey(player.getName()) && (oldFoodLevel > newFoodLevel)){
				
				// S'il est listé dans les races/classes et que c'est une BAISSE de FoodLevel, on récupère la race et la classe.
				String race = new String (Main.joueursRaces.get(player.getName()).toUpperCase());
				String classe = new String (Main.joueursClasses.get(player.getName()).toUpperCase());
				
				if(race.equals("OGRE")){
					e.setFoodLevel(e.getFoodLevel()-1); // Si c'est un ogre, la baisse est deux fois plus importante.
				} else if(race.equals("BARBARE") || classe.equals("GUERRIER")){		
					e.setFoodLevel(e.getFoodLevel()-EntiteDegatsEvent.randomIntEntreBornes(0, 1));
				} // Si c'est un barbare ou un guerrier, la baisse est 1,5 fois plus importante (on enlève 1 de plus une fois sur 2)
				
				
			}	
		}
	}

}
