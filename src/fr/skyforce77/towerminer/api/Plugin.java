package fr.skyforce77.towerminer.api;

public class Plugin {

	private PluginInfo info;
	
	public void onEnable() {}
	
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
	
}
