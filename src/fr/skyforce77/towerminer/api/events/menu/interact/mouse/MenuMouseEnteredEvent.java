package fr.skyforce77.towerminer.api.events.menu.interact.mouse;

import fr.skyforce77.towerminer.api.events.menu.interact.MenuMouseEvent;
import fr.skyforce77.towerminer.menus.Menu;

import java.awt.event.MouseEvent;

public class MenuMouseEnteredEvent extends MenuMouseEvent {

    public MenuMouseEnteredEvent(Menu menu, MouseEvent event) {
        super(menu, event);
    }
}
