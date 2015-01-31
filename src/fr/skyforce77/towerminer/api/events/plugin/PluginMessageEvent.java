package fr.skyforce77.towerminer.api.events.plugin;

public class PluginMessageEvent extends PluginEvent{
	
	private int type;
	private Object data;
	
	public PluginMessageEvent(String plugin, String version, int type, Object data) {
		super(plugin, version);
		this.type = type;
		this.data = data;
	}
	
	public Object getData() {
		return data;
	}
	
	public int getType() {
		return type;
	}

}
