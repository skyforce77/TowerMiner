package fr.skyforce77.towerminer.blocks;

import javax.swing.ImageIcon;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.api.Plugin;
import fr.skyforce77.towerminer.ressources.RessourcesManager;

public class CustomBlock extends Blocks{
	private static final long serialVersionUID = -3553063443480805200L;
	
	private Plugin plugin;
	private String name;
	private int vanilla = 1;

	public CustomBlock(Plugin plugin, String name, String... textures) {
		super();
		this.plugin = plugin;
		this.name = name;
		
		int i = 0;
        while (i < textures.length) {
            this.textures[i] = RessourcesManager.getIconTexture(textures[i]);
            i++;
        }
        
		this.id = Blocks.createCustomBlock(getIdentfier());
		Blocks.customblocks.add(this);
		TowerMiner.printInfo("Registered block "+getIdentfier()+" with id "+id);
	}
	
	public CustomBlock(Plugin plugin, String name, ImageIcon... textures) {
		super();
		this.plugin = plugin;
		this.name = name;
		
		int i = 0;
        while (i < textures.length) {
            this.textures[i] = textures[i];
            i++;
        }
        
		this.id = Blocks.createCustomBlock(getIdentfier());
		Blocks.customblocks.add(this);
	}
	
	public CustomBlock(Plugin plugin, String name, int fallback, ImageIcon... textures) {
		this(plugin, name, textures);
		vanilla = fallback;
	}
	
	public CustomBlock(Plugin plugin, String name, int fallback, String... textures) {
		this(plugin, name, textures);
		vanilla = fallback;
	}
	
	public Plugin getPlugin() {
		return plugin;
	}
	
	public String getName() {
		return name;
	}
	
	public int getFallback() {
		return vanilla;
	}
	
	public String getIdentfier() {
		return plugin.getName()+"."+name;
	}

}
