package fr.Florent59.RaceAndClass;

import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class JoueurJoinEvent implements Listener {
	
	@EventHandler (priority = EventPriority.LOW)
	public void onPlayerConnect(PlayerJoinEvent e){
		// On donne les bonus de classes/races à la connexion
		Player player = e.getPlayer();
		
		if (Main.joueursRaces.containsKey(player.getName()) && Main.joueursClasses.containsKey(player.getName())){
			player.removePotionEffect(PotionEffectType.NIGHT_VISION);
			GiveBonus(player, Main.joueursRaces.get(player.getName()).toUpperCase(), Main.joueursClasses.get(player.getName()).toUpperCase());
		}
		
	}
	
	@EventHandler (priority = EventPriority.LOW)
	public void onPlayerRespawn(PlayerRespawnEvent e){
		// Et également, au respawn après la mort
		Player player = e.getPlayer();
		
		if (Main.joueursRaces.containsKey(player.getName()) && Main.joueursClasses.containsKey(player.getName())){
			GiveBonus(player, Main.joueursRaces.get(player.getName()).toUpperCase(), Main.joueursClasses.get(player.getName()).toUpperCase());
		}
		
	}
	
	
	
	public static void GiveBonus(Player player, String race, String classe){
		
		Attributable playerAttributable = (Attributable) player;
		AttributeInstance maxHealthAttributeInstance = playerAttributable.getAttribute(Attribute.GENERIC_MAX_HEALTH);
		AttributeInstance knockbackResistanceAttributeInstance = playerAttributable.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
		AttributeInstance attackSpeedAttributeInstance = playerAttributable.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
		
			switch(race) // On récupère les attributes du joueur et selon sa race, on les modifie.
			{
				case "HUMAIN":
					maxHealthAttributeInstance.setBaseValue(20.0); // valeur par défaut minecraft vanilla
					player.setWalkSpeed(0.2f); // valeur par défaut minecraft vanilla
					knockbackResistanceAttributeInstance.setBaseValue(0.0); // valeur par défaut minecraft vanilla
					attackSpeedAttributeInstance.setBaseValue(4.0); // valeur par défaut minecraft vanilla
				break;
				
				case "NAIN":
					
					if(maxHealthAttributeInstance.getBaseValue() != 24){
						maxHealthAttributeInstance.setBaseValue(24.0);
						player.setHealth(24.0);
					} // On met la santé maximum du nain à 24 (il aura donc une barre de vie de 12 coeurs).
					
					player.setWalkSpeed(0.2f*0.8f); // vitesse à 80%
					knockbackResistanceAttributeInstance.setBaseValue(0.0);
					attackSpeedAttributeInstance.setBaseValue(4.0);
					
				break;
				
				case "ELFESYLVAIN":
					maxHealthAttributeInstance.setBaseValue(16.0);
					player.setWalkSpeed(0.2f*1.2f); // vitesse à 120%
					knockbackResistanceAttributeInstance.setBaseValue(0.0);
					attackSpeedAttributeInstance.setBaseValue(4.0);
					player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1, false, false));
					player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 0, false, false));
					// On donne les effets jump amélioré et vision nocturne de façon permanente.
				break;
				
				case "ELFEHAUT":
					maxHealthAttributeInstance.setBaseValue(16.0);
					player.setWalkSpeed(0.2f*1.2f); // vitesse à 120%
					knockbackResistanceAttributeInstance.setBaseValue(0.0);
					attackSpeedAttributeInstance.setBaseValue(4.0);
					player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1, false, false));
					player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 0, false, false));
					
				break;
				
				case "ELFENOIR":
					maxHealthAttributeInstance.setBaseValue(16.0);
					player.setWalkSpeed(0.2f*1.2f); // vitesse à 120%
					knockbackResistanceAttributeInstance.setBaseValue(0.0);
					attackSpeedAttributeInstance.setBaseValue(4.0);
					player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1, false, false));
					player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 0, false, false));
					
				break;
				
				case "BARBARE":
					maxHealthAttributeInstance.setBaseValue(20.0);
					player.setWalkSpeed(0.2f);
					knockbackResistanceAttributeInstance.setBaseValue(0.3); // 30% de chances de résister au recul (explosions, ennemis...)
					attackSpeedAttributeInstance.setBaseValue(2.0); // Met 2 fois moins de temps à se "recharger" avant de pouvoir réattaquer.
				break;
				
				case "OGRE":
					
					if(maxHealthAttributeInstance.getBaseValue() != 28){
						maxHealthAttributeInstance.setBaseValue(28.0);
						player.setHealth(28.0);
					}
					
					player.setWalkSpeed(0.2f*0.75f); // vitesse à 75%
					knockbackResistanceAttributeInstance.setBaseValue(0.3);
					attackSpeedAttributeInstance.setBaseValue(2.0);
					
				break;
			
			}
			
			switch(classe)
			{
				case "RANGER":
	
				break;
					
				case "GUERRIER":
					knockbackResistanceAttributeInstance.setBaseValue(0.3);
					attackSpeedAttributeInstance.setBaseValue(2.0);
				break;
				
				case "FORESTIER":
	
				break;
				
				case "VOLEUR":
					player.setWalkSpeed(player.getWalkSpeed()*1.2f); // vitesse de la race augmentée de 20%
					attackSpeedAttributeInstance.setBaseValue(3.0);
				break;
						
				case "MAGE":
					maxHealthAttributeInstance.setBaseValue(14.0); // Barre de vie de 7 coeurs
				break;
				
				case "PALADIN":
					
				break;
				
				case "PRÊTRE":
					maxHealthAttributeInstance.setBaseValue(14.0); // Barre de vie de 7 coeurs
				break;
				
				case "BARDE":
	
				break;
				
				case "CULTISTE":
	
				break;
	
			}
	}
}
