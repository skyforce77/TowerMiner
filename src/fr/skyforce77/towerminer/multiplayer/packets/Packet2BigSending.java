package fr.skyforce77.towerminer.multiplayer.packets;

import java.util.ArrayList;

import com.esotericsoftware.kryonet.Connection;

import fr.skyforce77.towerminer.multiplayer.BigSending;
import fr.skyforce77.towerminer.multiplayer.Connect;
import fr.skyforce77.towerminer.multiplayer.ObjectReceiver;

public class Packet2BigSending extends Packet{

	public byte type = 0;
	public int id = 0;
	public int lenght = 0;
	public int pack = 0;
	public byte[] data;

	@Override
	public void onClientReceived(Connection c) {
		if(type == 0) {
			ObjectReceiver rec = new ObjectReceiver();
			rec.id = id;
			rec.sent = (int)deserialize(data);
			rec.data = new byte[lenght];
			rec.received = new boolean[rec.sent];
			BigSending.receiving.put(id, rec);
		} else if(type == 2) {
			ArrayList<Byte> fails = new ArrayList<Byte>();
			boolean received = true;
			
			int i = 0;
			for(boolean b : BigSending.receiving.get(id).received) {
				i++;
				if(!b) {
					received = false;
					fails.add((byte)i);
				}
			}
			
			Packet2BigSending pb = new Packet2BigSending();
			pb.type = 2;
			pb.id = id;
			if(received) {
				pb.pack = 1;
			} else {
				pb.data = new byte[fails.size()];
				i = 0;
				for(Byte b : fails) {
					pb.data[i] = b;
					i++;
				}
			}
			Connect.c.sendTCP(pb);
		} else {
			BigSending.receive(id, lenght, pack, data);
		}
	}
	
	@Override
	public void onServerReceived(Connection c) {
		if(type == 2) {
			if(pack == 1) {
				BigSending.sending.get(id).thread.run(id);
			} else {
				for(byte b : data) {
					Connect.c.sendTCP(BigSending.sending.get(id).packets[(int)b]);
				}
				Connect.c.sendTCP(BigSending.sending.get(id).testpacket);
			}
		}
	}

}
