package fr.skyforce77.towerminer.api.events.chat;

import java.util.ArrayList;

import fr.skyforce77.towerminer.api.events.TMEvent;
import fr.skyforce77.towerminer.protocol.chat.ChatModel;
import fr.skyforce77.towerminer.protocol.chat.MessageModel;
import fr.skyforce77.towerminer.render.RenderRunnable;

public class ChatMouseHoverRenderEvent extends TMEvent{
	
	private ChatModel model;
    private ArrayList<RenderRunnable> renders = new ArrayList<>();
    private ArrayList<RenderRunnable> replacerenders = new ArrayList<>();

	public ChatMouseHoverRenderEvent(ChatModel model) {
		this.model = model;
	}
	
	public MessageModel getMouseModel() {
		return model.getMouseModel();
	}
	
	public ChatModel getChatModel() {
		return model;
	}

    public ArrayList<RenderRunnable> getRenders() {
        return renders;
    }
    
    public ArrayList<RenderRunnable> getReplaceRenders() {
        return replacerenders;
    }

    public void addRender(RenderRunnable render) {
    	if(render.isReplaceRender()) {
    		replacerenders.add(render);
    	} else {
    		renders.add(render);
    	}
    }
	
}
