package fr.skyforce77.towerminer.api.commands;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.protocol.Protocol;

public class CommandVersion extends Command {

	@Override
	public void onTyped(CommandSender sender, String[] args) {
		sender.sendMessage("Protocol: "+Protocol.version);
		sender.sendMessage("TowerMiner: "+TowerMiner.version);
	}
	
	@Override
	public boolean isCorrect(String[] args) {
		if(args.length == 1 && args[0].equals("")) {
			return true;
		}
		return false;
	}

}
