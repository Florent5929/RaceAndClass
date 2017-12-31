package fr.Florent59.RaceAndClass;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin {
	
	public static HashMap<String, String> joueursRaces = new HashMap<String, String>();
	public static HashMap<String, String> joueursClasses = new HashMap<String, String>();
	public static HashMap<String, String> motsTrad = new HashMap<String, String>();
	public List<String> joueursConfig;
	public List<String> motsTradConfig;
	public String[] words;
	public HashMap<String, Long> joueursDateSoigner = new HashMap<String, Long>();
	public List<String> datesConfigSoigner;
	
	@Override
	public void onEnable(){ 
		
		if (!this.getDataFolder().exists()) { 
			 this.saveDefaultConfig();
			 this.getConfig().options().copyDefaults(true);
		} // S'il n'y a pas de dossier et de fichier de configuration, on crée ceux par défaut. 
		
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new JoueurMoveEvent(), this);
		pm.registerEvents(new JoueurChatEvent(), this);
		pm.registerEvents(new JoueurJoinEvent(), this);
		pm.registerEvents(new EntiteDegatsEvent(), this);
		pm.registerEvents(new FoodEvent(), this);
		
		joueursConfig = getConfig().getStringList("joueurs");
		motsTradConfig = getConfig().getStringList("motsTrad");
		datesConfigSoigner = getConfig().getStringList("soigner");
		
		if(joueursConfig.size() >=1){
			for(String ligne : joueursConfig){
				words = ligne.split(":");
				joueursRaces.put(words[0], words[1]);
				joueursClasses.put(words[0], words[2]);
			} // Déplacement des infos de la config dans les HashMap Races/Classes.
		}
		
		if(motsTradConfig.size() >=1){
			for(String ligne : motsTradConfig){
				words = ligne.split(":");
				motsTrad.put(words[0], words[1]);
			} // Déplacement des infos de la config dans la HashMap motsTrad.
		}
		
		if(datesConfigSoigner.size() >=1){
			for(String ligne : datesConfigSoigner){
				words = ligne.split(":");
				joueursDateSoigner.put(words[0], Long.parseLong(words[1]));
			} // Stockage dans joueursDateSoigner des Elfes Sylvains et de la date de leur dernière commande de soin
		}
		
	}
	
	public void onDisable(){
		joueursConfig = getConfig().getStringList("joueurs");
		
		if(!joueursConfig.isEmpty())
			joueursConfig.clear();
		
		try{
			
			for(String p : joueursRaces.keySet()){
				joueursConfig.add(p + ":" + joueursRaces.get(p) + ":" + joueursClasses.get(p));
			}
	
			getConfig().set("joueurs", joueursConfig);
			saveConfig();
			
		} catch (NullPointerException e) {
			return;
		}
		
		// Fin de la sauvegarde des joueurs. Début de la sauvegarde des dates de la commande SOIGNER.
		
		datesConfigSoigner = getConfig().getStringList("dates");
		
		if(!datesConfigSoigner.isEmpty())
			datesConfigSoigner.clear();
		
			for(String p : joueursDateSoigner.keySet()){
				datesConfigSoigner.add(p + ":" + joueursDateSoigner.get(p));	
			}
			
		getConfig().set("dates", datesConfigSoigner);
		saveConfig();

	}
	
	
	
	public boolean AreNear(Player player, Player cible){
			// Retourne vrai si les joueurs sont à 5 blocs ou moins l'un de l'autre, faux sinon.
			if(player.getWorld().equals(cible.getWorld())){
				
				if(player.getLocation().distance(cible.getLocation()) <= 5f){
					return true;
				}	
			} 
		
		return false;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player player = (Player) sender;
		label = label.toUpperCase();
		
		if(sender instanceof Player ){
			
			switch(label)
			{
				case "GETRACE":
					
					if(args.length != 1){
						player.sendMessage("Usage : /getRace <nomCompteMinecraft>");
					} else if (!joueursRaces.containsKey(args[0])){
						player.sendMessage("La joueur " + args[0] + " n'a pas de race répertoriée.");
					} else {
						player.sendMessage("La race du joueur " + args[0] + " est " + joueursRaces.get(args[0]));
					}
					
					break;
					
				case "SETRACE":
					
					if(args.length != 2){
						player.sendMessage("Usage : /setRace <nomCompteMinecraft> <nomRace>");
					} else if(!ValidAndKits.RaceValide(args[1].toUpperCase())){
						player.sendMessage("La race " + args[1] + " n'est pas valide. Les races valides sont Humain, Nain, ElfeSylvain, ElfeHaut, ElfeNoir, Barbare ou Ogre.");
					} else {
						joueursRaces.put(args[0], args[1]);
						player.sendMessage(ChatColor.DARK_GREEN + "La race " + args[1] + " a été attribuée à " + args[0] + ". S'il est connecté, il doit se déconnecter puis se reconnecter pour bénéficier de tous les avantages.");
					}
					
					break;
					
				case "GETCLASS":
					
					if(args.length != 1){
						player.sendMessage("Usage : /getClass <nomCompteMinecraft>");
					} else if (!joueursClasses.containsKey(args[0])){
						player.sendMessage("La joueur " + args[0] + " n'a pas de classe répertoriée.");
					} else {
						player.sendMessage("La classe du joueur " + args[0] + " est " + joueursClasses.get(args[0]));
					}
					
					break;
					
				case "SETCLASS":
					
					if(args.length != 2){
						player.sendMessage("Usage : /setClass <nomCompteMinecraft> <nomClass>");
					} else if(!ValidAndKits.ClasseValide(args[1].toUpperCase())){
						player.sendMessage("La classe " + args[1] + " n'est pas valide. Les classes valides sont Ranger, Guerrier, Forestier, Voleur, Mage, Paladin, Prêtre, Barde et Cultiste.");
					} else {
						joueursClasses.put(args[0], args[1]);
						player.sendMessage(ChatColor.DARK_GREEN + "La classe " + args[1] + " a été attribuée à " + args[0] + ".  S'il est connecté, il doit se déconnecter puis se reconnecter pour bénéficier de tous les avantages.");
					}
					
					break;
					
				case "SETRACECLASS":
					
					if(args.length != 2){
						player.sendMessage("Usage : /setRaceClass <nomRace> <nomClass>");
						
					} else if (joueursRaces.containsKey(player.getName())){
						player.sendMessage("Vous avez déjà une race qui est " + joueursRaces.get(player.getName()) + ". Il est impossible de la modifier sans un opérateur.");
						
					} else if (joueursClasses.containsKey(player.getName())){
						player.sendMessage("Vous avez déjà une classe qui est " + joueursClasses.get(player.getName()) + ". Il est impossible de la modifier sans un opérateur.");
						
					} else if(!ValidAndKits.RaceValide(args[0].toUpperCase())){
						player.sendMessage("La race " + args[0] + " n'est pas valide. Les races valides sont Humain, Nain, ElfeSylvain, ElfeHaut, ElfeNoir, Barbare ou Ogre.");
						
					} else if(!ValidAndKits.ClasseValide(args[1].toUpperCase())){
						player.sendMessage("La classe " + args[1] + " n'est pas valide. Les classes valides sont Ranger, Guerrier, Forestier, Voleur, Mage, Paladin, Prêtre, Barde et Cultiste.");
					}
					 else {
						joueursRaces.put(player.getName(), args[0]);
						joueursClasses.put(player.getName(), args[1]);
						JoueurJoinEvent.GiveBonus(player, Main.joueursRaces.get(player.getName()).toUpperCase(), Main.joueursClasses.get(player.getName()).toUpperCase());
						player.sendMessage(ChatColor.DARK_GREEN + "La race " + args[0] + " et la classe " + args[1] + " vous ont été attribuées.");
						ValidAndKits.GiveKit(player, args[1].toUpperCase());
					}
					
					break;
				
				case "AIGUISER":
					// Permet à un voleur d'empoisonner sa lame, tenue dans la main principale.
					if (Main.joueursRaces.containsKey(player.getName()) && Main.joueursClasses.containsKey(player.getName())){
						String classe = new String (Main.joueursClasses.get(player.getName()).toUpperCase());
						ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
						ItemMeta im = player.getInventory().getItemInMainHand().getItemMeta();
						String nomItemInMainHand = new String(itemInMainHand.toString().toUpperCase());
						List<String> loreItemInMainHand = itemInMainHand.getItemMeta().getLore();
						
							if(classe.equals("VOLEUR") && nomItemInMainHand.contains("SWORD")){
								
								if(loreItemInMainHand == null){
									// S'il n'y a pas de lore, on crée un itemMeta, on rajoute un lore dessus et on attribue cet itemMeta à l'item.
									ArrayList<String> liste = new ArrayList<String>();
									liste.add("Cette arme a une drôle de couleur.");
									im.setLore(liste);
									itemInMainHand.setItemMeta(im);
									
									player.sendMessage(ChatColor.DARK_GREEN + "Vous avez enduit votre arme de poison avec succès. Vous êtes impatient de l'essayer...");
									player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_DIAMOND, 10f, 10f);
									
									// S'il y a déjà un lore, on ajoute juste la phrase au lore existant.
								} else if(!loreItemInMainHand.contains("Cette arme a une drôle de couleur.")){
									loreItemInMainHand.add("\nCette arme a une drôle de couleur.");
									player.sendMessage(ChatColor.DARK_GREEN + "Vous avez enduit votre arme de poison avec succès. Vous êtes impatient de l'essayer...");
									player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_DIAMOND, 10f, 10f);
									
								} else {
									player.sendMessage(ChatColor.RED + "Vous avez déjà empoisonné cette arme.");
								}
								
							} else {
								player.sendMessage(ChatColor.RED + "La commande a échoué. Vous devez être un voleur, tenir une épée dans votre main principale et ne pas l'avoir déjà empoisonnée.");
							}			
					}
					
					break;
				
				case "SOIGNER":
					// Permet à un elfe sylvain de soigner un joueur proche de lui. Utilisable toutes les 20 minutes.
					
					Collection <? extends Player>  playersOnline = player.getServer().getOnlinePlayers();
					Date currentDate = new Date(); // Stockage dans currentDate de la date du jour
					Long currentDateLong = currentDate.getTime(); // Stockage dans currentDateLong du nombre de millisecondes écoulées entre auj et le 1er janvier 1970 
					Long lastCommandDateLong = joueursDateSoigner.get(player.getName()); // Stockage dans lastCommandDateLong de la date de la dernière commande, exprimée en nb de millisecondes écoulées depuis le 1er janvier 1970
					int coeffDelai = 1;
					boolean delaiRespecte;
					
					if(player.isOp()) // Les opérateurs peuvent retaper la commande immédiatement sans aucun délai.
						coeffDelai = 0;
					
					if(lastCommandDateLong == null){
						delaiRespecte = true;
					} else if((currentDateLong - lastCommandDateLong) > 1200*1000*coeffDelai){ // délai de 1200 secondes * 1000 (millisecondes) soit 20 minutes.
						delaiRespecte = true;
					} else {
						delaiRespecte = false;
					}
						
						if(args.length != 1){
							player.sendMessage("Usage : /soigner <nomCompteMinecraft>");
						} else if (!playersOnline.contains(Bukkit.getPlayer(args[0]))){
							player.sendMessage("La joueur " + args[0] + " n'est pas répertorié dans les joueurs connectés.");
						} else if(player.getName().equals(args[0])){
							player.sendMessage("Vous ne pouvez pas vous auto-soigner.");
						} else if (!AreNear(player, Bukkit.getPlayer(args[0]))){
							player.sendMessage("La joueur " + args[0] + " n'est pas assez proche de vous (5 blocs maximum)");
						} else if(!Main.joueursRaces.containsKey(player.getName()) || !Main.joueursClasses.containsKey(player.getName())){
							player.sendMessage("Impossible d'utiliser cette commande sans avoir renseigné sa race et sa classe.");
						} else if(!joueursRaces.get(args[0]).toUpperCase().equals("ELFESYLVAIN")){
							player.sendMessage("Seuls les elfes sylvains peuvent utiliser cette commande.");
						} else if(!delaiRespecte){
							player.sendMessage("Votre dernière utilisation de cette commande est trop récente.");
						} else {
							// Si tout est bon, alors on agit.
							
							Player cible = Bukkit.getPlayer(args[0]);
							Attributable playerAttributable = (Attributable) cible;
							AttributeInstance maxHealthAttributeInstance = playerAttributable.getAttribute(Attribute.GENERIC_MAX_HEALTH);
							int gain = (EntiteDegatsEvent.randomIntEntreBornes(1, 6))*2;
							
								// On donne à la cible entre 1 et 6 coeurs (tirage au sort). Si ça dépasse le max de santé autorisé, on met au max.
								if((cible.getHealth() + gain) > maxHealthAttributeInstance.getBaseValue()){
									cible.setHealth(maxHealthAttributeInstance.getBaseValue());
								} else {
									cible.setHealth(cible.getHealth() + gain);
								}
							player.sendMessage("Vous avez donné les premiers soins avec succès.");
							cible.sendMessage("Vous avez reçu les premiers soins par un elfe sylvain, vous vous sentez un peu mieux. [+" + gain/2 + " coeur(s)]");
							joueursDateSoigner.put(player.getName(), currentDateLong); // Sauvegarde de la date de la commande.
						}
						
					break;
					
				default:
					return false;
			}
			
		}			
	
	return false;	
	}
}
