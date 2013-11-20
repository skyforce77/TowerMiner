package fr.skyforce77.towerminer.multiplayer;

import fr.skyforce77.towerminer.multiplayer.ObjectReceiver.ReceivingThread;
import fr.skyforce77.towerminer.multiplayer.packets.Packet2BigSending;

public class ObjectSender {

	public Packet2BigSending[] packets;
	public Packet2BigSending testpacket;
	public ReceivingThread thread;

}
