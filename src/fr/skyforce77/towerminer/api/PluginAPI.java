package fr.skyforce77.towerminer.api;

public class PluginAPI {

	private static int version = 1;
	private static int revision = 0;
	
	public static int getVersion() {
		return version;
	}
	
	public static int getRevision() {
		return revision;
	}
	
	public static String getVersionName() {
		return version+"."+revision;
	}
	
}