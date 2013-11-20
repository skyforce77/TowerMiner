package fr.skyforce77.towerminer.multiplayer.packets;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import com.esotericsoftware.kryonet.Connection;

import fr.skyforce77.towerminer.multiplayer.Connect;

public class Packet {

	public void onClientReceived(Connection c) {}

	public void onServerReceived(Connection c) {}
	
	public void onReceived(Connection c) {}

	public byte[] serialize(Object o) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try {
			out = new ObjectOutputStream(bos);   
			out.writeObject(o);
			return bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public Object deserialize(byte[] bytes) {
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		ObjectInput in = null;
		try {
			in = new ObjectInputStream(bis);
			return in.readObject(); 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bis.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public void sendTCP() {
		if(Connect.c != null) {
			Connect.c.sendTCP(this);
		}
	}
	
	public void sendUDP() {
		if(Connect.c != null) {
			Connect.c.sendUDP(this);
		}
	}

}
