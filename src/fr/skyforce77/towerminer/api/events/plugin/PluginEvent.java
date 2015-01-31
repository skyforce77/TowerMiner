package fr.skyforce77.towerminer.api.events.plugin;

import fr.skyforce77.towerminer.api.events.TMEvent;

public class PluginEvent extends TMEvent{
	
	private String plugin;
	private String version;
	
	public PluginEvent(String plugin, String version) {
		this.plugin = plugin;
		this.version = version;
	}
	
	public String getPlugin() {
		return plugin;
	}
	
	public String getVersion() {
		return version;
	}

}
