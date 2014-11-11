package fr.skyforce77.towerminer.api;

import java.io.File;

import fr.skyforce77.towerminer.ressources.RessourcesManager;

public class Plugin {

	private PluginInfo info;
	
	public PluginStatus onEnable() {
		return PluginStatus.OK;
	}
	
	public boolean isPluginNeededByClient() {
        return false;
	}
	
	public PluginInfo getInfos() {
        return info;
	}
	
	public void setPluginInfos(PluginInfo info) {
        this.info = info;
	}
	
	public String getName() {
		return info.getName();
	}
	
	public String getVersion() {
		return info.getVersion();
	}
	
	public File getDirectory() {
		File f = new File(RessourcesManager.getDirectory(), "/plugins/"+getName());
		if(!f.exists())
			f.mkdirs();
		return f;
	}
	
}
