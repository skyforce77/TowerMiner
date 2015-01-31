package fr.skyforce77.towerminer.api.commands;

import fr.skyforce77.towerminer.protocol.chat.ChatModel;
import fr.skyforce77.towerminer.protocol.chat.MessageModel;


public class Argument {

	private String name;
	private boolean optional;
	private String[] values;
	private ArgumentType type = ArgumentType.String;
	
	public Argument(String name, ArgumentType type) {
		this.name = name;
		this.optional = false;
		this.type = type;
		this.values = type.getValues();
	}
	
	public Argument(String name, ArgumentType type, String... values) {
		this.name = name;
		this.type = type;
		this.values = values;
	}
	
	public Argument(String name, ArgumentType type, boolean optional) {
		this.name = name;
		this.optional = optional;
		this.type = type;
		this.values = type.getValues();
	}
	
	public Argument(String name, ArgumentType type, boolean optional, String... values) {
		this.name = name;
		this.optional = optional;
		this.type = type;
		this.values = values;
	}
	
	public String getName() {
		return name;
	}
	
	public String[] getValues() {
		return values;
	}
	
	public boolean isOptional() {
		return optional;
	}
	
	@Deprecated
	public ChatModel getRender() {
		return toChatModel();
	}
	
	public ChatModel toChatModel() {
		String s = type.getDisplay();
		ChatModel model;
		if(optional) {
			model = new ChatModel("["+name+"]");
		} else {
			model = new ChatModel(name);
		}
		model.setMouseModel(new MessageModel(s));
		return model;
	}
	
	public ArgumentType getType() {
		return type;
	}
	
	public static class ArgumentType {
		
		public static ArgumentType String = new ArgumentType("text");
		public static ArgumentType Url = new ArgumentType("url");
		public static ArgumentType Integer = new ArgumentType("number");
		public static ArgumentType Boolean = new ArgumentType("true/false", "true", "false");
		
		private String display;
		private String[] values = new String[0];
		
		public ArgumentType(String display) {
			this.display = display;
		}
		
		public ArgumentType(String display, String... values) {
			this.display = display;
			this.values = values;
		}
		
		public String getDisplay() {
			return display;
		}
		
		public String[] getValues() {
			return values;
		}
	}
}
