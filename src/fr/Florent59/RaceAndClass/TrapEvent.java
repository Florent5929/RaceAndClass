package fr.Florent59.RaceAndClass;

import java.util.ArrayList;
import java.util.Date;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import net.md_5.bungee.api.ChatColor;

public class TrapEvent implements Listener {

	@EventHandler (priority = EventPriority.LOW)
	public void Interact(PlayerInteractEvent e){
		
		Block bloc = e.getClickedBlock();
		Player player = e.getPlayer();
		ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
		
		if(bloc != null){
		
			String nomBloc = new String(bloc.getType().toString());
		
			if ((nomBloc.toUpperCase().contains("DOOR") || nomBloc.toUpperCase().contains("CHEST")) && player.getGameMode().toString().toUpperCase().contains("SURVIVAL")){
				
				Location locationBloc = bloc.getLocation();
				Location locationBlocEnfoui = new Location(player.getWorld(), locationBloc.getX(), locationBloc.getY()-2, locationBloc.getZ());
				Block blocEnfoui = locationBlocEnfoui.getBlock();
				
					 if(blocEnfoui.getState() instanceof Chest){
						Chest chestState = (Chest) blocEnfoui.getState();
						
							if(!TrapIsActive(chestState.getInventory()) && itemInMainHand.toString().toUpperCase().contains("TRIPWIRE_HOOK") && Main.joueursClasses.get(player.getName()).toUpperCase().equals("VOLEUR")){
								player.sendMessage("Ce piège semble avoir déjà été désactivé.");
								e.setCancelled(true);
							} else if(TrapIsActive(chestState.getInventory()) && itemInMainHand.toString().toUpperCase().contains("TRIPWIRE_HOOK") && Main.joueursClasses.get(player.getName()).toUpperCase().equals("VOLEUR")){
								
								int tirage = EntiteDegatsEvent.randomIntEntreBornes(1, 20);
								player.sendMessage(ChatColor.DARK_PURPLE + "Vous avez tiré un " + tirage + " sur votre dé à 20 faces.");
								ItemStack montre = new ItemStack(Material.WATCH);
								ItemMeta meta = montre.getItemMeta();
								Date date = new Date();
								String displayName = new String("" + date.getTime());
								ArrayList<String> liste = new ArrayList<String>();
								liste.add("Cette montre HRP informe le plugin que le piège");
								liste.add("a été désactivé le " + date + ",");
								liste.add("pour une durée de 2h. Si quelqu'un désactive à");
								liste.add("nouveau le piège après cette date, elle sera");
								liste.add("automatiquement supprimée pour être remplacée");
								liste.add("par une nouvelle montre.");
								meta.setLore(liste);
								meta.setDisplayName(displayName);
								montre.setItemMeta(meta);
								e.setCancelled(true);
								
									if(tirage == 1){
										player.sendMessage("Oh non, au lieu de désactiver le piège, vous venez de l'activer ! En plus, votre crochet s'est cassé.");
										CheckChestState(player, chestState, locationBloc);
										
										if(itemInMainHand.getAmount() > 1)
											itemInMainHand.setAmount(itemInMainHand.getAmount()-1);
										else
											player.getInventory().setItemInMainHand(null);
										
									} else if(tirage >=2 && tirage <=5){
										player.sendMessage("Oh non, au lieu de désactiver le piège, vous venez de l'activer !");
										CheckChestState(player, chestState, locationBloc);
									} else if(tirage >=6 && tirage <=9){
										player.sendMessage("Vous n'avez pas réussi à désactiver le piège, mais au moins, il ne s'est pas déclenché.");
									} else if(tirage >=10 && tirage <=14){
										player.sendMessage("Vous avez réussi à désactiver le piège, mais votre crochet s'est cassé.");
										
										if(itemInMainHand.getAmount() > 1)
											itemInMainHand.setAmount(itemInMainHand.getAmount()-1);
										else
											player.getInventory().setItemInMainHand(null);
										
										chestState.getInventory().addItem(montre);
									} else if(tirage >=15 && tirage <= 19){
										player.sendMessage("Vous avez réussi à désactiver le piège avec brio et votre crochet est intact.");
										chestState.getInventory().addItem(montre);
									} else {
										player.sendMessage("Vous avez réussi à désactiver le piège avec brio, votre crochet est intact et en plus, vous venez d'en trouver un deuxième ! Un autre voleur est visiblement passé par là et a dû l'oublier.");
										chestState.getInventory().addItem(montre);
										itemInMainHand.setAmount(itemInMainHand.getAmount()+1);
									}
							} else if(TrapIsActive(chestState.getInventory())){
								CheckChestState(player, chestState, locationBloc);
							}
							
					 } 
			}	
		}	
	}
	
	public void CheckSkull(Player player, ItemStack item, Location locationBloc){

		if(item.getType().toString().toUpperCase().contains("SKULL")){

			String skullType = new String("" + item.getDurability());
			
			if(EntiteAFaireSpawner(skullType) != null)
				SpawnEntites(player.getWorld(), locationBloc, EntiteAFaireSpawner(skullType));	
		}
	}
	
	public void CheckWeb(Player player, ItemStack item, Location locationBloc){
		if(item.getType().toString().toUpperCase().contains("WEB"))
			SpawnEntites(player.getWorld(), locationBloc, EntiteAFaireSpawner("WEB"));
	}
	
	public void CheckTorch(Player player, ItemStack item){
		if(item.getType().toString().toUpperCase().contains("TORCH"))
			player.setFireTicks(300);
	}
	
	public void CheckTNT(Player player, ItemStack item, Location locationBloc){
		if(item.getType().toString().toUpperCase().contains("TNT"))
			player.getWorld().createExplosion(locationBloc, 5f);
	}
	
	public boolean TrapIsActive(Inventory inventaireCoffre){
		
		int i = 0;
		
		for(ItemStack item :  inventaireCoffre){
			
			if(item != null){

				if(item.getType().toString().toUpperCase().contains("WATCH") && item.hasItemMeta()){
					
					if(item.getItemMeta().hasDisplayName()){
						String lore = new String (item.getItemMeta().getDisplayName());
						try{
							Long lastDate = Long.parseLong(lore);
							
								if(!Skill.DelaiRespecte(lastDate, 3600*2)){
									return false;
								} else {
									inventaireCoffre.setItem(i, null);
									return true;
								}
							
						} catch (Exception exception){		
						}
					}
				}
			}
			i++;
		}
		return true;
	}
	
	public void CheckChestState(Player player, Chest chestState, Location locationBloc){
		
		if(TrapIsActive(chestState.getInventory())){
			
			for(ItemStack item :  chestState.getInventory()){
				
				if(item != null){
	
					CheckBook(player, item, locationBloc);
					CheckPotion(player, item);
					CheckTNT(player, item, locationBloc);
					CheckWeb(player, item, locationBloc);
					CheckTorch(player, item);
					CheckSkull(player, item, locationBloc);
				}
			}
		}
		
		
	}
	
	public void CheckPotion(Player player, ItemStack item){
		
		if(item.getItemMeta() instanceof PotionMeta){
			PotionMeta potion = (PotionMeta) item.getItemMeta();
			PotionType potiontype = potion.getBasePotionData().getType();
				
				if(potiontype.toString().toUpperCase().equals("UNCRAFTABLE")){
					// potion obtenue par /give
					if(potion.getLore() != null){
						String lore = new String(potion.getLore().toString().toUpperCase());
						
							try{			
								PotionEffect potioneffect = PotionEffectType.getByName(lore.substring(1, lore.length()-1)).createEffect(2000, 0);
								player.addPotionEffect(potioneffect);
								
							} catch(NullPointerException exception){
								player.sendMessage("Erreur : L'effet " + lore.substring(1, lore.length()-1) + " n'a pas été trouvé.");
							}
					}
					
				} else {
					PotionEffect potioneffect = potiontype.getEffectType().createEffect(2000, 0);
					player.addPotionEffect(potioneffect);
				}
		}
	}
	
	public void CheckBook(Player player, ItemStack item, Location locationBloc){
		
		if(item.getItemMeta() instanceof BookMeta){
			BookMeta book = (BookMeta) item.getItemMeta();
			
		if(!book.getPage(1).toString().equals(""))
			player.sendMessage(book.getPage(1).toString());
			
			for (Player onlinePlayer : player.getServer().getOnlinePlayers()) {
					
				if(book.getPageCount() >=2 && Skill.AreNear(player, onlinePlayer, 10f))	
					onlinePlayer.sendMessage(book.getPage(2).toString());			
			}		
		}
	}
	
	public static void SpawnEntites(World world, Location location, EntityType entityType){
		Location[] locationEntites = new Location[4];
		locationEntites[0] = new Location(world, location.getX()+3, location.getY(), location.getZ());
		locationEntites[1] = new Location(world, location.getX()-3, location.getY(), location.getZ());
		locationEntites[2] = new Location(world, location.getX(), location.getY(), location.getZ()+3);
		locationEntites[3] = new Location(world, location.getX(), location.getY(), location.getZ()-3);
		
		for(int i=0; i<=3; i++){
			world.spawnEntity(locationEntites[i], entityType);
		}
	}
	
	public static EntityType EntiteAFaireSpawner(String nomItem){
		EntityType entityType = null;
		
		switch(nomItem)
		{
			case "0":
				entityType = EntityType.SKELETON;
			break;
			
			case "1":
				entityType = EntityType.WITHER_SKELETON;
			break;
			
			case "2":
				entityType = EntityType.ZOMBIE;
			break;
			
			case "4":
				entityType = EntityType.CREEPER;
			break;
			
			case "WEB":
				entityType = EntityType.SPIDER;
			break;
			
		}
	
		return entityType;
	}
}
