package fr.skyforce77.towerminer.api.commands;

import fr.skyforce77.towerminer.api.commands.Argument.ArgumentType;

public class CommandEcho extends Command{

	@Override
	public void onInitialized(String label) {
		setArguments(new Argument("text", ArgumentType.String, false));
	}
	
	@Override
	public void onTyped(CommandSender sender, String[] args) {
		String status = "";
		for(String s : args)
			status = status+" "+s;
		sender.sendMessage(status);
	}
	
	@Override
	public boolean isCorrect(String[] args) {
		if(args.length >= 1 && !args[0].equals("")) {
			return true;
		}
		return false;
	}
}
