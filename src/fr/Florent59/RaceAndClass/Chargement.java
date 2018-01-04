package fr.Florent59.RaceAndClass;

import java.util.HashMap;
import java.util.List;

import org.bukkit.plugin.Plugin;

public class Chargement {
	
	private final Plugin plugin;
	   
	    public Chargement(Plugin plugin)
	    {
	        this.plugin = plugin;
	    }
	
	public void ChargerDates(String nomListconfig, HashMap<String, Long> hashmap){
		List<String> listconfig = plugin.getConfig().getStringList(nomListconfig);
		
		if(listconfig.size() >=1){
			for(String ligne : listconfig){
				String[] words = ligne.split(":");
				hashmap.put(words[0], Long.parseLong(words[1]));
			} // Permet de charger les dates depuis la config vers une hashMap.
		}
	}
	
	public void ChargerString(String nomListconfig, HashMap<String, String> hashmap){
		List<String> listconfig = plugin.getConfig().getStringList(nomListconfig);
		
		if(listconfig.size() >=1){
			for(String ligne : listconfig){
				String[] words = ligne.split(":");
				hashmap.put(words[0], words[1]);
			} // Permet de charger une info STRING depuis la config vers une hashMap.
		}
	}
	
	public void Charger2String(String nomListconfig, HashMap<String, String> hashmap, HashMap<String, String> hashmap2){
		List<String> listconfig = plugin.getConfig().getStringList(nomListconfig);
		
		if(listconfig.size() >=1){
			for(String ligne : listconfig){
				String[] words = ligne.split(":");
				hashmap.put(words[0], words[1]);
				hashmap2.put(words[0], words[2]);
			} // Permet de charger deux infos STRING depuis la config vers deux hashMap.
		}
	}
	
	public void SaveDatesOnConfig(String nomListconfig, HashMap<String, Long> hashmap){
		List<String> listconfig = plugin.getConfig().getStringList(nomListconfig);
		
		if(!listconfig.isEmpty())
			listconfig.clear();
		
			for(String p : hashmap.keySet()){
				listconfig.add(p + ":" + hashmap.get(p));	
			}
			
			plugin.getConfig().set(nomListconfig, listconfig);
			plugin.saveConfig();
		
	}
	
	public void SavePlayersOnConfig(String nomListconfig, HashMap<String, String> hashmap, HashMap<String, String> hashmap2){
		List<String> listconfig = plugin.getConfig().getStringList(nomListconfig);
		
		if(!listconfig.isEmpty())
			listconfig.clear();
		
		try{
			
			for(String p : hashmap.keySet()){
				listconfig.add(p + ":" + hashmap.get(p) + ":" + hashmap2.get(p));
			}
	
			plugin.getConfig().set(nomListconfig, listconfig);
			plugin.saveConfig();
			
		} catch (NullPointerException e) {
			return;
		}
		
	}

}
