package fr.skyforce77.towerminer.api;

import java.util.Map;

public class PluginInfo {

	private Map<?,?> map;
	public PluginInfo(Map<?,?> map) {
		this.map = map;
	}
	
	public String getName() {
		return map.get("name").toString();
	}
	
	public String getVersion() {
		return map.get("version").toString();
	}
	
	public String getMain() {
		return map.get("main").toString();
	}
	
	public String getValue(String value) {
		return map.get(value).toString();
	}
	
	public Map<?, ?> getMap() {
		return map;
	}
	
}
