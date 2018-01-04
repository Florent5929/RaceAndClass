package fr.Florent59.RaceAndClass;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;

public class Skill {
	
	public static boolean AreNear(Player player, Player cible, Float distance){
		
		if(player.getWorld().equals(cible.getWorld())){
			
			if(player.getLocation().distance(cible.getLocation()) <= distance){
				return true;
			}	
		} 
	
	return false;
	} // Retourne vrai si la distance entre les deux joueurs est inf�rieure ou �gale � la distance renseign�e.

	
	public static boolean DelaiRespecte(Long lastCommandTimestamp, int nbSecondes){
		
		Date currentDate = new Date();
		Long currentDateLong = currentDate.getTime(); 

		if(lastCommandTimestamp == null){
			return true;
		} else if((currentDateLong - lastCommandTimestamp) > nbSecondes*1000){
			return true;
		} else {
			return false;
		}
	} // Retourne vrai si le nombre de secondes entre aujourd'hui et la date fournie est sup�rieur � nbSecondes, ou si la date fournie est null.
	
	
	public void Activer(Player player){
		
		if (Main.joueursRaces.containsKey(player.getName()) && Main.joueursClasses.containsKey(player.getName())){
			player.removePotionEffect(PotionEffectType.NIGHT_VISION);
			player.removePotionEffect(PotionEffectType.JUMP);
			JoueurJoinEvent.GiveBonus(player, Main.joueursRaces.get(player.getName()).toUpperCase(), Main.joueursClasses.get(player.getName()).toUpperCase());
			player.sendMessage(ChatColor.DARK_GREEN + "Les bonus/malus auxquels votre race et votre classe vous donnent droit vous ont �t� r�-attribu�s.");
		}
		
	}
	
	public void Soigner(Player player, String nomCible){
		
		Collection <? extends Player>  playersOnline = player.getServer().getOnlinePlayers();
		Long lastCommandDateLong = Main.joueursDateSoigner.get(player.getName()); // Stockage dans lastCommandDateLong de la date de la derni�re commande, exprim�e en nb de millisecondes �coul�es depuis le 1er janvier 1970

			
			if (!playersOnline.contains(Bukkit.getPlayer(nomCible))){
				player.sendMessage("La joueur " + nomCible + " n'est pas r�pertori� dans les joueurs connect�s.");
			} else if(player.getName().equals(nomCible)){
				player.sendMessage("Vous ne pouvez pas vous auto-soigner.");
			} else if (!AreNear(player, Bukkit.getPlayer(nomCible), 5f)){
				player.sendMessage("La joueur " + nomCible + " n'est pas assez proche de vous (5 blocs maximum)");
			} else if(!Main.joueursRaces.containsKey(player.getName()) || !Main.joueursClasses.containsKey(player.getName())){
				player.sendMessage("Impossible d'utiliser cette commande sans avoir renseign� sa race et sa classe.");
			} else if(!Main.joueursRaces.get(nomCible).toUpperCase().equals("ELFESYLVAIN")){
				player.sendMessage("Seuls les elfes sylvains peuvent utiliser cette commande.");
			} else if(!DelaiRespecte(lastCommandDateLong, 1200) && !player.isOp()){
				player.sendMessage("Votre derni�re utilisation de cette commande est trop r�cente.");
			} else {
				// Si tout est bon, alors on agit.
				
				Player cible = Bukkit.getPlayer(nomCible);
				Attributable playerAttributable = (Attributable) cible;
				AttributeInstance maxHealthAttributeInstance = playerAttributable.getAttribute(Attribute.GENERIC_MAX_HEALTH);
				int gain = (EntiteDegatsEvent.randomIntEntreBornes(1, 6))*2;
				
					// On donne � la cible entre 1 et 6 coeurs (tirage au sort). Si �a d�passe le max de sant� autoris�, on met au max.
					if((cible.getHealth() + gain) > maxHealthAttributeInstance.getBaseValue()){
						cible.setHealth(maxHealthAttributeInstance.getBaseValue());
					} else {
						cible.setHealth(cible.getHealth() + gain);
					}
				player.sendMessage("Vous avez donn� les premiers soins avec succ�s.");
				cible.sendMessage("Vous avez re�u les premiers soins par un elfe sylvain, vous vous sentez un peu mieux. [+" + gain/2 + " coeur(s)]");
				Date currentDate = new Date(); // Stockage dans currentDate de la date du jour
				Long currentDateLong = currentDate.getTime(); // Stockage dans currentDateLong du nombre de millisecondes �coul�es entre auj et le 1er janvier 1970 
				Main.joueursDateSoigner.put(player.getName(), currentDateLong); // Sauvegarde de la date de la commande.
			}
	}
	
	public void Aiguiser(Player player){
		
		if (Main.joueursRaces.containsKey(player.getName()) && Main.joueursClasses.containsKey(player.getName())){
			String classe = new String (Main.joueursClasses.get(player.getName()).toUpperCase());
			
			ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
			String nomItemInMainHand = new String(itemInMainHand.toString().toUpperCase());
			ItemStack itemInOffHand = player.getInventory().getItemInOffHand();
	
				if(classe.equals("VOLEUR") && itemInMainHand != null){
					
					if(nomItemInMainHand.contains("SWORD") && EstPotionAiguiser(itemInOffHand)){
						EmpoisonneArme(player);
						player.getInventory().setItemInOffHand(null);
					} else {
						player.sendMessage(ChatColor.RED + "La commande a �chou�. Vous devez �tre un voleur, tenir une �p�e dans votre main principale, un poison sp�cial dans votre main secondaire, et ne pas avoir d�j� empoisonn�e votre arme.");
					}
					
				} else {
					player.sendMessage(ChatColor.RED + "La commande a �chou�. Vous devez �tre un voleur, tenir une �p�e dans votre main principale, un poison sp�cial dans votre main secondaire, et ne pas avoir d�j� empoisonn�e votre arme.");
				}			
		} 
	}
	
	public void EmpoisonneArme(Player player){
		
		ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
		ItemMeta im = itemInMainHand.getItemMeta();
		List<String> loreItemInMainHand = itemInMainHand.getItemMeta().getLore();
		
		if(loreItemInMainHand == null){
			// S'il n'y a pas de lore, on cr�e un itemMeta, on rajoute un lore dessus et on attribue cet itemMeta � l'item.
			ArrayList<String> liste = new ArrayList<String>();
			liste.add("Cette arme a une dr�le de couleur.");
			im.setLore(liste);
			itemInMainHand.setItemMeta(im);
				
			player.sendMessage(ChatColor.DARK_GREEN + "Vous avez enduit votre arme de poison avec succ�s. Vous �tes impatient de l'essayer...");
			player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_DIAMOND, 10f, 10f);
				
			// S'il y a d�j� un lore, on ajoute juste la phrase au lore existant.
		} else if(!loreItemInMainHand.contains("Cette arme a une dr�le de couleur.")){
			loreItemInMainHand.add("\nCette arme a une dr�le de couleur.");
			player.sendMessage(ChatColor.DARK_GREEN + "Vous avez enduit votre arme de poison avec succ�s. Vous �tes impatient de l'essayer...");
			player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_DIAMOND, 10f, 10f);
				
		} else {
			player.sendMessage(ChatColor.RED + "Vous avez d�j� empoisonn� cette arme.");
		}
		
	}
	
	public boolean EstPotionAiguiser(ItemStack item){
		
		if(item != null){
			if(item.getType().equals(Material.POTION) && item.hasItemMeta()){		
				PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
				
					if(potionMeta.hasCustomEffect(PotionEffectType.POISON) && item.getItemMeta().hasLore()){
						
						if(item.getItemMeta().getLore().contains("#poison")){
							return true;
						}
						
					}	
			}
		}
		return false;
	}
	
	public Skill(){
		
	}

	public void Musique(Player player, String nomMusique) {
		if (Main.joueursRaces.containsKey(player.getName()) && Main.joueursClasses.containsKey(player.getName())){
			String classe = new String (Main.joueursClasses.get(player.getName()).toUpperCase());
			nomMusique = nomMusique.toUpperCase();
			PotionEffectType potionEffectType = null;
			String message = new String("");
			Long lastCommandDateLong = Main.joueursDateMusique.get(player.getName()); // Stockage dans lastCommandDateLong de la date de la derni�re commande, exprim�e en nb de millisecondes �coul�es depuis le 1er janvier 1970
			
				if(classe.equals("BARDE")){
					
					switch(nomMusique){
						
						case "GUERRE":
							potionEffectType = PotionEffectType.INCREASE_DAMAGE;
							message = "La musique du barde vous donne la rage de vaincre.";
							break;
						
						case "PAILLARDE":
							potionEffectType = PotionEffectType.ABSORPTION;
							message = "La musique du barde vous donne du beaume au coeur.";
							break;
							
						default:
							break;
					
					}
				
					if(DelaiRespecte(lastCommandDateLong, 1200) || player.isOp()){
						if(potionEffectType != null){
							for(Player onlinePlayer : player.getServer().getOnlinePlayers()){
								if(AreNear(player, onlinePlayer, 20f)){
									onlinePlayer.addPotionEffect(potionEffectType.createEffect(2000, 0));
									onlinePlayer.sendMessage(message);
								}
							}
							Date currentDate = new Date(); // Stockage dans currentDate de la date du jour
							Long currentDateLong = currentDate.getTime(); // Stockage dans currentDateLong du nombre de millisecondes �coul�es entre auj et le 1er janvier 1970 
							Main.joueursDateMusique.put(player.getName(), currentDateLong); // Sauvegarde de la date de la commande.
						} else {
							player.sendMessage(ChatColor.RED + "La musique " + nomMusique.toLowerCase() + " est inconnue.");
						}
					} else {
						player.sendMessage(ChatColor.RED + "Votre derni�re utilisation de cette commande est trop r�cente.");
					}
					
				} else {
					player.sendMessage(ChatColor.RED + "La commande a �chou�. Vous devez �tre un barde.");
				}			
		} 
	}

}
