package fr.skyforce77.towerminer.api.events.menu.interact.window;

import fr.skyforce77.towerminer.api.events.menu.interact.MenuWindowEvent;
import fr.skyforce77.towerminer.menus.Menu;

import java.awt.event.WindowEvent;

public class MenuWindowClosingEvent extends MenuWindowEvent {

    public MenuWindowClosingEvent(Menu menu, WindowEvent event) {
        super(menu, event);
    }
}
