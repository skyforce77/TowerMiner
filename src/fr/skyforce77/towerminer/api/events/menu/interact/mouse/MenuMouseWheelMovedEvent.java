package fr.skyforce77.towerminer.api.events.menu.interact.mouse;

import fr.skyforce77.towerminer.api.events.menu.interact.MenuMouseEvent;
import fr.skyforce77.towerminer.menus.Menu;

import java.awt.event.MouseEvent;

public class MenuMouseWheelMovedEvent extends MenuMouseEvent {

    public MenuMouseWheelMovedEvent(Menu menu, MouseEvent event) {
        super(menu, event);
    }
}
