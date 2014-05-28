package fr.skyforce77.towerminer.api;

public enum EventPriority {

	FIRST(4),
	SECOND(3),
	THIRD(2),
	FOURTH(1),
	FIFTH(0);
	
	private int priority;
	
	private EventPriority(int priority) {
		this.priority = priority;
	}
	
	public int getPriority() {
		return priority;
	}
}
