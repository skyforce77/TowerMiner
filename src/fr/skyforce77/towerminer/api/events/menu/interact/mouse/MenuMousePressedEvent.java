package fr.skyforce77.towerminer.api.events.menu.interact.mouse;

import fr.skyforce77.towerminer.api.events.menu.interact.MenuMouseEvent;
import fr.skyforce77.towerminer.menus.Menu;

import java.awt.event.MouseEvent;

public class MenuMousePressedEvent extends MenuMouseEvent {

    public MenuMousePressedEvent(Menu menu, MouseEvent event) {
        super(menu, event);
    }
}
