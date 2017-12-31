package fr.Florent59.RaceAndClass;

import java.util.Map.Entry;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;


public class JoueurChatEvent implements Listener {
	
	@EventHandler (priority = EventPriority.LOW)
	public void QuandUnJoueurParle(AsyncPlayerChatEvent e){


	Player p = e.getPlayer();
	String Prefixe = e.getMessage().substring(0, 1); // 1er caract�re du message que le joueur tente d'envoyer.
	Random r = new Random();
	float distanceRemove;
	int NumFirstCharMessageSansPrefixe;
		
		if(Main.joueursRaces.get(p.getName()).toUpperCase().equals("OGRE") &&
				!Prefixe.equals("$") &&
				!Prefixe.equals(":") &&
				!Prefixe.equals("*") &&
				!Prefixe.equals(">") &&
				!Prefixe.equals("(")
		){ // Si le joueur est un ogre et seulement s'il n'�crit pas dans les channels ci-dessus, on agit.
	
		
			 if( Prefixe.equals("!")) 
				 
				 {distanceRemove = 100F;
				 NumFirstCharMessageSansPrefixe = 1;} 
			 
			 	else if (Prefixe.equals("+"))
			 		
				 {distanceRemove = 50F;
				 NumFirstCharMessageSansPrefixe = 1;} 
			 
			 		else if (Prefixe.equals("#"))
			 			
			 		{distanceRemove = 3F;
			 		NumFirstCharMessageSansPrefixe = 1;}
								
			 			else { 
			 			distanceRemove = 20F;
			 			NumFirstCharMessageSansPrefixe = 0;
			 			Prefixe = "";
			 			} 
			 
// En fonction du channel (cri, braille, murmure ou local) le distance limite de r�ception du message varie.
// Ainsi que le num�ro du premier caract�re du message sans pr�fixe.
// S'il n'y a pas de pr�fixe !, + ou #, c'est que l'on est dans le channel local.
// Le cas �ch�ant, on r�affecte � Prefixe une cha�ne vide.
				
		
			for (Player onlinePlayer : e.getPlayer().getServer().getOnlinePlayers()) {
			
					if(e.getPlayer().getWorld() != onlinePlayer.getWorld()){
						e.getRecipients().remove(onlinePlayer);
					} else if (e.getPlayer().getLocation().distance(onlinePlayer.getLocation()) > distanceRemove) {
						e.getRecipients().remove(onlinePlayer);
					}			
			}
				 	
			// On vire de la liste des destinataires du message ceux qui sont trop loin ou pas du m�me monde.
				
			for (Player t : e.getRecipients()) {
				if(Main.joueursClasses.get(p.getName()).toUpperCase().equals("MAGE") || 
				Main.joueursRaces.get(p.getName()).toUpperCase().equals("OGRE") ||                          
				t.isOp()){
				t.sendMessage(ChatColor.GRAY  + "[Traduction de " + p.getName() + "] : " +  e.getMessage().substring(NumFirstCharMessageSansPrefixe));
				}
				// On envoie � chaque destinataire la traduction en priv�, s'il est mage, ogre ou OP.
			}
			
			
			String message = e.getMessage().substring(NumFirstCharMessageSansPrefixe); // On enl�ve le pr�fixe du message, s'il y en a un.
			// "!J'ai faim" deviendra "J'ai faim"
			// "! J'ai faim" deviendra " J'ai faim"
			
			
			Pattern pattern = Pattern.compile("[A-Za-z]");  
			// On d�finit la classe des caract�res que l'on recherche (alphanum�rique).
            Matcher matcher = pattern.matcher(message.substring(0,1)); 
            // On definit la chaine dans laquelle on va faire notre recherche,
            // le 1er caract�re du message donc, qui ne comporte donc plus de pr�fixe.
            
            
				if(!matcher.matches()){ 
					// Si ce 1er caract�re n'est pas alphanum�rique... 
					// (exemple: " J'ai faim !", le 1er caract�re est un espace)
					message = message.substring(1); // Alors on le vire du message (" J'ai faim" deviendra "J'ai faim")
				}
				
			String[] parts = message.split(" "); // On coupe le message et on le range dans un tableau
			// La d�coupe se fait selon les espaces.
			// Si le message est "J'ai faim", parts[0] sera �gal � "J'ai" et parts[1] �gal � "faim".
			String listemots = "";
			int nbmots = parts.length;
			String ponctuation = ".";
					
					if( 
					   e.getMessage().substring(e.getMessage().length()-1).equals("!") || 
					   e.getMessage().substring(e.getMessage().length()-1).equals("?")
					){
					nbmots = nbmots -1;
					ponctuation = " " + e.getMessage().substring(e.getMessage().length()-1);
					} 
        // Si le message se terminait par un ! ou un ?, on range ce ! ou ce ? dans la variable ponctuation.
					// On ne compte pas cette ponctuation comme �tant un mot, d'o� le nbmots-1
			
			String fin = " ";
			String motcompare = "";
			
			for(int j = 0; j <= nbmots-1; j++){ // Pour chaque mot du message...
				for(Entry<String, String> entry : Main.motsTrad.entrySet()){ // ... et pour chaque mot de fran�ais r�pertori�...
					String cle = entry.getKey();
				    String valeur = entry.getValue();
					
					// ... on va essayer de voir si le mot du message n'est pas r�pertori�
					
					if (Main.motsTrad.keySet().toArray()[0] == cle){ // Si c'est la premi�re fois que l'on fait une recherche pour tel mot du message,

					motcompare = parts[j];
					// on range le mot dans la variable motcompare car parts[j] ne le conservera pas.
					
						 String finmotcompare = motcompare.substring(motcompare.length()-1);
						 // On range dans finmotcompare le dernier caract�re du mot.
						 // Exemple : si le mot est "ami," le dernier caract�re sera ","
					
						if (finmotcompare.equals(".") || 
						    finmotcompare.equals(",") || 	
							finmotcompare.equals("!") || 
							finmotcompare.equals("?"))
						{
							// si le dernier caract�re est un point, une virgule, un point d'exclamation ou d'interrogation...
							motcompare = motcompare.substring(0, motcompare.length()-1);	
							// ... on l'enl�ve du mot car cela va bloquer la traduction sinon.
							// "ami," deviendra donc "ami"
						}
							
							;}
					
					
					
					if(motcompare.equalsIgnoreCase(cle)) 
						// Si on trouve le mot dans la liste de mots de fran�ais...
					{ parts[j] = valeur;
					// ... alors on prend sa traduction en ogre et on la range dans parts[j]
					break;} // et on arr�te la recherche l�, on passe au mot suivant.
					parts[j] = Main.motsTrad.values().toArray()[r.nextInt(Main.motsTrad.size() -1)].toString();
					// sinon, on met dans parts[j] un mot ogre de notre liste au hasard.
				}
			}
			
			
			
			for(int i = 0; i <= nbmots-1; i++){		
				if(i == nbmots-1){ fin = "";}
				listemots = listemots + parts[i] + fin;	
			// On ajoute tous les mots ogres dans listemots
			}
			
			
			
			e.setMessage(Prefixe +  listemots + ponctuation);
			// On affiche la liste des mots.
			// On ajoute le pr�fixe �ventuel et la ponctuation �ventuelle (par d�faut, un point).
		
		
			
		} 
					
				
		} 

}
