package fr.skyforce77.towerminer.multiplayer;

import java.util.ArrayList;
import java.util.HashMap;

import fr.skyforce77.towerminer.multiplayer.ObjectReceiver.ReceivingThread;
import fr.skyforce77.towerminer.multiplayer.packets.Packet2BigSending;

public class BigSending {
	
	public static HashMap<Integer, ObjectReceiver> receiving = new HashMap<>();
	public static HashMap<Integer, ObjectSender> sending = new HashMap<>();
	
	public static boolean receive(int id, int lenght, int pack, byte[] data) {
		receiving.get(id).data = add(receiving.get(id).data, pack*100, data);
		receiving.get(id).received[pack] = true;
		return pack*100+data.length >= lenght;
	}
	
	public static byte[] add(byte[] base, int start, byte[] data) {
		int i = 0;
		while(i < data.length) {
			base[i+start] = data[i];
			i++;
		}
		return base;
	}

	public static void sendBigObject(final Object object, final ReceivingThread run) {
		new Thread() {
			@Override
			public void run() {
				byte[] map = new Packet2BigSending().serialize(object);
				int lenght = 0;
				int id = 0;
				ArrayList<byte[]> datas = new ArrayList<>();
				
				while(receiving.get(id) != null) {
					id++;
				}
				
				while(lenght < map.length) {
					byte[] data = null;
					if(lenght+100 > map.length) {
						data = new byte[map.length-lenght];
					} else {
						data = new byte[100];
					}

					int i = 0;
					while(i < data.length) {
						data[i] = map[lenght+i];
						i++;
					}
					
					lenght+=data.length;
					datas.add(data);
				}
				
				Packet2BigSending init = new Packet2BigSending();
				init.data = init.serialize(datas.size());
				init.lenght = map.length;
				init.type = 0;
				init.id = id;
				Connect.c.sendTCP(init);
				
				ObjectSender sender = new ObjectSender();
				sender.packets = new Packet2BigSending[datas.size()];
				sender.thread = run;
				sending.put(id, sender);
				
				int i = 0;
				while(i < datas.size()) {
					Packet2BigSending send = new Packet2BigSending();

					send.data = datas.get(i);
					send.pack = i;
					send.type = 1;
					send.id = id;
					send.lenght = map.length;

					Connect.c.sendTCP(send);
					
					sending.get(id).packets[i] = send;
					i++;
					
					try {
						sleep(40l);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				try {
					sleep(10l);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				Packet2BigSending send = new Packet2BigSending();

				send.type = 2;
				send.pack = i;
				send.id = id;

				sending.get(id).testpacket = send;
				Connect.c.sendTCP(send);
			}
		}.run();
	}

}
