package fr.Florent59.RaceAndClass;

import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	public static HashMap<String, String> joueursRaces = new HashMap<String, String>();
	public static HashMap<String, String> joueursClasses = new HashMap<String, String>();
	public static HashMap<String, String> motsTrad = new HashMap<String, String>();
	public static HashMap<String, Long> joueursDateSoigner = new HashMap<String, Long>();
	public static HashMap<String, Long> joueursDateMusique = new HashMap<String, Long>();
	
	@Override
	public void onEnable(){ 
		
		if (!this.getDataFolder().exists()) { 
			 this.saveDefaultConfig();
			 this.getConfig().options().copyDefaults(true);
		} // S'il n'y a pas de dossier et de fichier de configuration, on crée ceux par défaut. 
		
		Chargement chargement = new Chargement(this);
		chargement.Charger2String("joueurs", joueursRaces, joueursClasses);
		chargement.ChargerString("motsTrad", motsTrad);
		chargement.ChargerDates("soigner", joueursDateSoigner);
		chargement.ChargerDates("musique", joueursDateMusique);
		// Chargement des informations de la config pour les ranger dans des HashMaps.
		
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new JoueurMoveEvent(), this);
		pm.registerEvents(new JoueurChatEvent(), this);
		pm.registerEvents(new JoueurJoinEvent(), this);
		pm.registerEvents(new EntiteDegatsEvent(), this);
		pm.registerEvents(new FoodEvent(), this);
		pm.registerEvents(new TrapEvent(), this);
	}
	
	public void onDisable(){
		
		Chargement chargement = new Chargement(this);
		chargement.SavePlayersOnConfig("joueurs", joueursRaces, joueursClasses);
		chargement.SaveDatesOnConfig("soigner", joueursDateSoigner);
		chargement.SaveDatesOnConfig("musique", joueursDateMusique);

	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player player = (Player) sender;
		label = label.toUpperCase();
		int nbArgs = args.length;
		
		if(sender instanceof Player ){
			
			switch(label)
			{
				case "SKILL":
					
					Skill skill = new Skill();
					
						switch(nbArgs)
						{					
							case 1:
								args[0] = args[0].toUpperCase();
								
									switch(args[0]){
									
										case "ACTIVER":	
											skill.Activer(player);
											break;
										
										case "AIGUISER":
											skill.Aiguiser(player);
											break;	
										
										default:
											player.sendMessage("/skill " + args[0].toLowerCase() + " n'est pas une commande valide. Vérifiez qu'il ne manque pas une information, comme un nom de compte minecraft par exemple.");
											break;
									}
							
								break;
							
							case 2:
								args[0] = args[0].toUpperCase();
								
									switch(args[0]){
									
										case "SOIGNER":
											skill.Soigner(player, args[1]);		
											break;
											
										case "MUSIQUE":
											skill.Musique(player, args[1]);
											break;
										
										default:
											player.sendMessage("/skill " + args[0].toLowerCase() + " " + args[1] + " n'est pas une commande valide. Vérifiez qu'il ne manque pas une information, comme un nom de compte minecraft par exemple.");
											break;
									}
								
								break;
								
							default:
								player.sendMessage("Usage : /skill <nomCompétence>");
								break;
						}					
					
					break;
				
				case "GETRACE":
					
					if(nbArgs != 1){
						player.sendMessage("Usage : /getRace <nomCompteMinecraft>");
					} else if (!joueursRaces.containsKey(args[0])){
						player.sendMessage("La joueur " + args[0] + " n'a pas de race répertoriée.");
					} else {
						player.sendMessage("La race du joueur " + args[0] + " est " + joueursRaces.get(args[0]));
					}
					
					break;
					
				case "SETRACE":
					
					if(nbArgs != 2){
						player.sendMessage("Usage : /setRace <nomCompteMinecraft> <nomRace>");
					} else if(!ValidAndKits.RaceValide(args[1].toUpperCase())){
						player.sendMessage("La race " + args[1] + " n'est pas valide. Les races valides sont Humain, Nain, ElfeSylvain, ElfeHaut, ElfeNoir, Barbare ou Ogre.");
					} else {
						joueursRaces.put(args[0], args[1]);
						player.sendMessage(ChatColor.DARK_GREEN + "La race " + args[1] + " a été attribuée à " + args[0] + ". S'il est connecté, il doit se déconnecter puis se reconnecter pour bénéficier de tous les avantages.");
					}
					
					break;
					
				case "GETCLASS":
					
					if(nbArgs != 1){
						player.sendMessage("Usage : /getClass <nomCompteMinecraft>");
					} else if (!joueursClasses.containsKey(args[0])){
						player.sendMessage("La joueur " + args[0] + " n'a pas de classe répertoriée.");
					} else {
						player.sendMessage("La classe du joueur " + args[0] + " est " + joueursClasses.get(args[0]));
					}
					
					break;
					
				case "SETCLASS":
					
					if(nbArgs != 2){
						player.sendMessage("Usage : /setClass <nomCompteMinecraft> <nomClass>");
					} else if(!ValidAndKits.ClasseValide(args[1].toUpperCase())){
						player.sendMessage("La classe " + args[1] + " n'est pas valide. Les classes valides sont Ranger, Guerrier, Forestier, Voleur, Mage, Paladin, Prêtre, Barde et Cultiste.");
					} else {
						joueursClasses.put(args[0], args[1]);
						player.sendMessage(ChatColor.DARK_GREEN + "La classe " + args[1] + " a été attribuée à " + args[0] + ".  S'il est connecté, il doit se déconnecter puis se reconnecter pour bénéficier de tous les avantages.");
					}
					
					break;
					
				case "SETRACECLASS":
					
					if(nbArgs != 2){
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
					
				default:
					return false;
			}		
		}			
	return false;	
	}
}
