package fr.skyforce77.towerminer.api;

public class PluginStatus {
	
	public static PluginStatus OK = new PluginStatus(0);
	public static PluginStatus WARNING = new PluginStatus(1);
	public static PluginStatus ERROR = new PluginStatus(2);

	private int code;
	
	public PluginStatus(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
}
