package fr.skyforce77.towerminer.api.commands;

public 	enum SenderType {
	
	CHAT(0),
	OTHER_PLAYER(1),
	CONSOLE(2);
	
	int id;
	
	private SenderType(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
}
