package fr.Florent59.RaceAndClass;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ValidAndKits {
	
	public static boolean RaceValide(String race){
		
		boolean raceValide;
		
		switch(race)
		{
			case "HUMAIN":
				raceValide = true;
			break;
			
			case "NAIN":
				raceValide = true;
			break;
			
			case "ELFESYLVAIN":
				raceValide = true;
			break;
			
			case "ELFEHAUT":
				raceValide = true;
			break;
			
			case "ELFENOIR":
				raceValide = true;
			break;
			
			case "BARBARE":
				raceValide = true;
			break;
			
			case "OGRE":
				raceValide = true;
			break;
			
			default:
				raceValide = false;	
		
		}
		return raceValide;
	} // Cette méthode prend un nom de race en paramètre et indique s'il valide ou non (true/false)
	
	public static boolean ClasseValide(String classe){
		
		boolean classeValide;
		
		switch(classe)
		{
			case "RANGER":
				classeValide = true;
			break;
			
			case "GUERRIER":
				classeValide = true;
			break;
			
			case "FORESTIER":
				classeValide = true;
			break;
			
			case "VOLEUR":
				classeValide = true;
			break;
					
			case "MAGE":
				classeValide = true;
			break;
			
			case "PALADIN":
				classeValide = true;
			break;
			
			case "PRÊTRE":
				classeValide = true;
			break;
			
			case "BARDE":
				classeValide = true;
			break;
			
			case "CULTISTE":
				classeValide = true;
			break;
	
			default:
				classeValide = false;	
		
		}
		return classeValide;
	} // Identique que la précédente mais avec les classes.
	
	
	public static void GiveKit(Player player, String classe){
		
		// Distribue un kit au joueur fourni en paramètre, selon la classe fournie en paramètre.
		
		switch(classe)
		{
			case "RANGER":
				ItemStack[] items = {new ItemStack(Material.BREAD, 20), 
									new ItemStack(Material.LEATHER_CHESTPLATE, 1), 
									new ItemStack(Material.WOOD_SWORD, 1)};
				player.getInventory().addItem(items);
				player.sendMessage("Vous inspectez vos maigres fournitures un instant, avant de partir à la recherche de Forgemonts.");
			break;
				
			case "GUERRIER":
				ItemStack[] items2 = {new ItemStack(Material.BREAD, 20), 
						new ItemStack(Material.LEATHER_CHESTPLATE, 1), 
						new ItemStack(Material.WOOD_SWORD, 1)};
				player.getInventory().addItem(items2);
				player.sendMessage("Vous inspectez vos maigres fournitures un instant, avant de partir à la recherche de Forgemonts.");

			break;
			
			case "FORESTIER":
				ItemStack[] items3 = {new ItemStack(Material.BREAD, 20), 
						new ItemStack(Material.LEATHER_CHESTPLATE, 1), 
						new ItemStack(Material.WOOD_SWORD, 1)};
				player.getInventory().addItem(items3);
				player.sendMessage("Vous inspectez vos maigres fournitures un instant, avant de partir à la recherche de Forgemonts.");


			break;
			
			case "VOLEUR":
				ItemStack[] items4 = {new ItemStack(Material.BREAD, 20), 
						new ItemStack(Material.LEATHER_CHESTPLATE, 1), 
						new ItemStack(Material.WOOD_SWORD, 1)};
				player.getInventory().addItem(items4);
				player.sendMessage("Vous inspectez vos maigres fournitures un instant, avant de partir à la recherche de Forgemonts.");


			break;
					
			case "MAGE":
				
				ItemStack[] items5 = {new ItemStack(Material.BREAD, 20), 
						new ItemStack(Material.LEATHER_CHESTPLATE, 1), 
						new ItemStack(Material.WOOD_SWORD, 1),
						new ItemStack(Material.BOOK, 10)
						};
				player.getInventory().addItem(items5);
				player.sendMessage("Vous inspectez vos maigres fournitures un instant, avant de partir à la recherche de Forgemonts. N'oubliez surtout pas vos livres, mage !");


			break;
			
			case "PALADIN":
				
				ItemStack[] items6 = {new ItemStack(Material.BREAD, 20), 
						new ItemStack(Material.LEATHER_CHESTPLATE, 1), 
						new ItemStack(Material.WOOD_SWORD, 1)};
				player.getInventory().addItem(items6);
				player.sendMessage("Vous inspectez vos maigres fournitures un instant, avant de partir à la recherche de Forgemonts.");



			break;
			
			case "PRÊTRE":
				
				ItemStack[] items7 = {new ItemStack(Material.BREAD, 20), 
						new ItemStack(Material.LEATHER_CHESTPLATE, 1), 
						new ItemStack(Material.WOOD_SWORD, 1)};
				player.getInventory().addItem(items7);
				player.sendMessage("Vous inspectez vos maigres fournitures un instant, avant de partir à la recherche de Forgemonts.");



			break;
			
			case "BARDE":
				
				ItemStack[] items8 = {new ItemStack(Material.BREAD, 20), 
						new ItemStack(Material.LEATHER_CHESTPLATE, 1), 
						new ItemStack(Material.WOOD_SWORD, 1)};
				player.getInventory().addItem(items8);
				player.sendMessage("Vous inspectez vos maigres fournitures un instant, avant de partir à la recherche de Forgemonts.");



			break;
			
			case "CULTISTE":
				
				ItemStack[] items9 = {new ItemStack(Material.BREAD, 20), 
						new ItemStack(Material.LEATHER_CHESTPLATE, 1), 
						new ItemStack(Material.WOOD_SWORD, 1)};
				player.getInventory().addItem(items9);
				player.sendMessage("Vous inspectez vos maigres fournitures un instant, avant de partir à la recherche de Forgemonts.");


			break;

		}
		
	}
}
