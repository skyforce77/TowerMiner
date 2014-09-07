package fr.skyforce77.towerminer.api.events.menu.interact.window;

import fr.skyforce77.towerminer.api.events.menu.interact.MenuWindowEvent;
import fr.skyforce77.towerminer.menus.Menu;

import java.awt.event.WindowEvent;

public class MenuWindowIconifiedEvent extends MenuWindowEvent {

    public MenuWindowIconifiedEvent(Menu menu, WindowEvent event) {
        super(menu, event);
    }
}
