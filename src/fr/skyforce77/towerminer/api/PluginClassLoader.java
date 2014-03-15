package fr.skyforce77.towerminer.api;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

public class PluginClassLoader extends URLClassLoader{

	public void addURL(java.net.URL url) {
		super.addURL(url);
	}

	public PluginClassLoader(URL[] urls, ClassLoader parent,
			URLStreamHandlerFactory factory) {
		super(urls, parent, factory);
	}

	public PluginClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}

	public PluginClassLoader(URL[] urls) {
		super(urls);
	};
}
