package fr.skyforce77.towerminer.api.events.entity;

import fr.skyforce77.towerminer.api.events.TMEvent;
import fr.skyforce77.towerminer.entity.Entity;

public class EntityEvent extends TMEvent{

	private Entity entity;

	public EntityEvent(Entity entity) {
		super();
		this.entity = entity;
	}
	
	public Entity getEntity() {
		return entity;
	}
	
}
