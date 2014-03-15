package fr.skyforce77.towerminer.api.events;

public class PluginMessageEvent extends TMEvent{
	
	private String plugin;
	private String version;
	private int type;
	private Object data;
	
	public PluginMessageEvent(String plugin, String version, int type, Object data) {
		this.plugin = plugin;
		this.version = version;
		this.type = type;
		this.data = data;
	}
	
	public Object getData() {
		return data;
	}
	
	public String getPlugin() {
		return plugin;
	}
	
	public String getVersion() {
		return version;
	}
	
	public int getType() {
		return type;
	}

}
