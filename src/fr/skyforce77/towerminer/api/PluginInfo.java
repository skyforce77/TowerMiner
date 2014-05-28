package fr.skyforce77.towerminer.api;

import java.util.Map;

public class PluginInfo {

	private String name, version;
	private Map<?,?> map;
	public PluginInfo(Map<?,?> map) {
		this.version = map.get("version").toString();
		this.name = map.get("name").toString();
		this.map = map;
	}
	
	public String getName() {
		return name;
	}
	
	public String getVersion() {
		return version;
	}
	
	public Map<?, ?> getMap() {
		return map;
	}
	
}
