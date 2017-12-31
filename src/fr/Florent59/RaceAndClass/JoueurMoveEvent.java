package fr.Florent59.RaceAndClass;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import net.md_5.bungee.api.ChatColor;

public class JoueurMoveEvent implements Listener {

	@EventHandler (priority = EventPriority.LOW)
	public void onPlayerMove(PlayerMoveEvent e){
		Player player = e.getPlayer();
		
		if (!Main.joueursRaces.containsKey(player.getName()) && !Main.joueursClasses.containsKey(player.getName())){
			
			Location location = player.getLocation();
		    player.teleport(location);
			// Si un joueur n'est pas répertorié dans les listes et tente de bouger, on l'empêche.
		    player.sendMessage("---------------------------------------");
			player.sendMessage(ChatColor.RED + 
					"Votre race et votre classe ne sont pas connues ! "
					+ "Merci de choisir une race et une classe avec la commande /setRaceClass <nomRace> <nomClass>"
					+ " (exemple : /setRaceClass Humain Mage) "
					+ "Les races valides sont Humain, Nain, ElfeSylvain, ElfeHaut, ElfeNoir, Barbare ou Ogre. "
					+ "Les classes valides sont Ranger, Guerrier, Forestier, Voleur, Mage, Paladin, Prêtre, Barde et Cultiste. "
					+ "Attention, il est impossible de modifier sans un opérateur, soyez sûr de votre choix.");
			player.sendMessage("---------------------------------------");
		}
		
	}
}
