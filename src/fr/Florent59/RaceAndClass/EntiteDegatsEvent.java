package fr.Florent59.RaceAndClass;

import java.util.List;
import java.util.Random;

import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EntiteDegatsEvent implements Listener {
	
	@EventHandler (priority = EventPriority.LOW)
	public void QuandEntitePrendDegatsParJoueur(EntityDamageByEntityEvent e){
		
		if(e.getDamager() instanceof CraftPlayer){
			// On v�rifie que la cause des dommages est un joueur, et on r�cup�re des infos sur le joueur.
			if (Main.joueursRaces.containsKey(e.getDamager().getName()) && Main.joueursClasses.containsKey(e.getDamager().getName())){
				String race = new String (Main.joueursRaces.get(e.getDamager().getName()).toUpperCase());
				String classe = new String (Main.joueursClasses.get(e.getDamager().getName()).toUpperCase());
				Player player = (Player) e.getDamager();
				ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
				String nomItemInMainHand = new String(itemInMainHand.toString().toUpperCase());
				
				switch(race)
				{
					case "HUMAIN":

					break;
					
					case "NAIN":
						if(nomItemInMainHand.contains("AXE"))
							e.setDamage(e.getDamage()*1.2);
						// Si c'est un nain et que les d�g�ts sont inflig�s � la hache, ils sont augment�s de 20%
					break;
					
					case "ELFESYLVAIN":
						e.setDamage(e.getDamage()*0.8); // D�g�ts baiss�s de 20% pour les elfes.
					break;
					
					case "ELFEHAUT":
						e.setDamage(e.getDamage()*0.8);
					break;
					
					case "ELFENOIR":
						e.setDamage(e.getDamage()*0.8);
					break;
					
					case "BARBARE":
						e.setDamage(e.getDamage()*1.2); // D�g�ts augment�s de 20% pour les barbares.
					break;
					
					case "OGRE":
						e.setDamage(e.getDamage()*1.1);
						
						if(randomIntEntreBornes(1, 10) >= 9){
							
							if(player.getFoodLevel() > 14){
								player.setFoodLevel(20);
							} else {
								player.setFoodLevel(player.getFoodLevel()+6);
							}
						// L'ogre, quand il attaque une entit�, a 20% de chances d'augmenter sa barre de faim.
						}
					break;
				
				}
				
				switch(classe)
				{
					case "RANGER":
		
					break;
						
					case "GUERRIER":
						if(nomItemInMainHand.contains("SWORD") || nomItemInMainHand.contains("AXE"))
							e.setDamage(e.getDamage()*1.2); // D�g�ts augment�s de 20% si hache ou �p�e.
		
					break;
					
					case "FORESTIER":
		
					break;
					
					case "VOLEUR":
					
						if(itemInMainHand != null){
						
							if(itemInMainHand.hasItemMeta()){
								
								if(itemInMainHand.getItemMeta().hasLore()){
									
									List<String> loreItemInMainHand = itemInMainHand.getItemMeta().getLore();
									
										if(nomItemInMainHand.contains("SWORD") && loreItemInMainHand.contains("Cette arme a une dr�le de couleur.") && e.getEntity() instanceof LivingEntity && randomIntEntreBornes(1, 10) >= 9){
											// Une �p�e empoisonn�e (voir dans Main) a 20% de chances de donner l'effet poison par coup.
											LivingEntity entity = (LivingEntity) e.getEntity();
											entity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 2000, 1));
										}
								}
							}
						}

					break;
							
					case "MAGE":
						
					break;
					
					case "PALADIN":
						
					break;
					
					case "PR�TRE":
						
					break;
					
					case "BARDE":
		
					break;
					
					case "CULTISTE":
		
					break;
		
				}

			}
			
		} else if(e.getDamager() instanceof Projectile){
			// Si la cause des d�g�ts est un projectile..
			Projectile projectile = (Projectile) e.getDamager();
			String typeProjectile = new String(projectile.getType().toString().toUpperCase());
			
				// On v�rifie que le tireur est un joueur et que le projectile est une fl�che.
				if(projectile.getShooter() instanceof Player && typeProjectile.contains("ARROW")){
					Player shooter = (Player) projectile.getShooter();
					
					if (Main.joueursRaces.containsKey(shooter.getName()) && Main.joueursClasses.containsKey(shooter.getName())){
						String race = new String (Main.joueursRaces.get(shooter.getName()).toUpperCase());
						
						if(race.contains("ELFE")){
							e.setDamage(e.getDamage()*1.2);
						} // Si oui et que c'est un elfe, les d�g�ts sont augment�s de 20%				
					}	
				}
		}	
	}
	
	public static final int randomIntEntreBornes(int borneInf, int borneSup){
		Random r = new Random();
		return (borneInf + r.nextInt(borneSup+1 - borneInf));
	} // Retourne un entier al�atoire compris entre les bornes inf et sup pass�es en param�tres.
}
