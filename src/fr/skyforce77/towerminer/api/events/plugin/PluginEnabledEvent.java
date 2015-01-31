package fr.skyforce77.towerminer.api.events.plugin;

import fr.skyforce77.towerminer.api.PluginStatus;

public class PluginEnabledEvent extends PluginEvent{

	PluginStatus status;
	
	public PluginEnabledEvent(String plugin, String version, PluginStatus status) {
		super(plugin, version);
		this.status = status;
	}
	
	public PluginStatus getStatus() {
		return status;
	}

}
