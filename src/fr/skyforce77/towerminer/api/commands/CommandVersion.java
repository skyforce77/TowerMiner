package fr.skyforce77.towerminer.api.commands;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.api.Utils;
import fr.skyforce77.towerminer.protocol.Protocol;

public class CommandVersion extends Command {

	@Override
	public void onTyped(String[] args) {
		Utils.write("Protocol: "+Protocol.version);
		Utils.write("TowerMiner: "+TowerMiner.version);
	}
	
	@Override
	public boolean isCorrect(String[] args) {
		if(args.length == 1 && args[0].equals("")) {
			return true;
		}
		return false;
	}

}
