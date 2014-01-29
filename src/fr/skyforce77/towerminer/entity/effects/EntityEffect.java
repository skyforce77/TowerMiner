package fr.skyforce77.towerminer.entity.effects;

import fr.skyforce77.towerminer.entity.Mob;

import java.io.Serializable;

public class EntityEffect implements Serializable {

    private static final long serialVersionUID = 8857258954792583487L;

    int type = 0;
    int ticks = -1;
    int data = 0;

    public EntityEffect(EntityEffectType type, int duration) {
        this.type = type.getID();
        ticks = duration;
    }

    public EntityEffect(EntityEffectType type, int duration, int data) {
        this.type = type.getID();
        ticks = duration;
        this.data = data;
    }

    public int getTicks() {
        return ticks;
    }

    public int getData() {
        return data;
    }

    public EntityEffectType getType() {
        return EntityEffectType.byId(type);
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

    public void setData(int data) {
        this.data = data;
    }

    public void onTick(Mob m) {
        getType().onTick(m, this);
    }

}
