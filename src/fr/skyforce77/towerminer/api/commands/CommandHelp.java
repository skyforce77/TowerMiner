package fr.skyforce77.towerminer.api.commands;

import java.util.ArrayList;

import fr.skyforce77.towerminer.api.Utils;
import fr.skyforce77.towerminer.api.commands.Argument.ArgumentType;
import fr.skyforce77.towerminer.protocol.chat.ChatMessage;

public class CommandHelp extends Command {

	int pagesnumber = 0;
	ArrayList<ArrayList<String>> pages = new ArrayList<>();
	
	@Override
	public void onTyped(String[] args) {
		update();
		if(args.length == 1 && args[0].equals("")) {
			sendPage(0);
		} else if(args.length == 1 && !args[0].equals("") && isNumber(args[0])) {
			sendPage(Integer.parseInt(args[0])-1);
		}
	}
	
	public void sendPage(int number) {
		if(number > pagesnumber) {
			Utils.write("Page #"+(number+1)+" do not exists");
			return;
		}
		Utils.write("<Commands> Page: "+(number+1)+"/"+(pagesnumber+1));
		for(String command : pages.get(number)) {
			Command c = CommandManager.getCommand(command);
			if(c.getUse() == null) {
				Utils.write("- "+command);
			} else {
				ChatMessage message = new ChatMessage("- ");
				message.add(c.getUse());
				Utils.write(message);
			}
		}
	}
	
	public void update() {
		int i = 1;
		pagesnumber = 0;
		pages = new ArrayList<>();
		ArrayList<String> commands = new ArrayList<>();
		for(String command : CommandManager.getCommands()) {
			if(i < 8) {
				commands.add(command);
				i++;
			} else {
				pages.add(commands);
				commands = new ArrayList<>();
				commands.add(command);
				i = 2;
				pagesnumber++;
			}
		}
		if(i > 1) {
			pages.add(commands);
		}
	}
	
	@Override
	public void onInitialized(String label) {
		setArguments(new Argument("page", ArgumentType.Integer, true));
	}
	
	@Override
	public boolean isCorrect(String[] args) {
		if(args.length == 1 && args[0].equals("")) {
			return true;
		} else if(args.length == 1 && !args[0].equals("") && isNumber(args[0])) {
			return true;
		}
		return false;
	}

}
