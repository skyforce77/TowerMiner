package fr.skyforce77.towerminer.maps;

import java.io.Serializable;

public class MapLine implements Serializable{

	private static final long serialVersionUID = 5762060744804813081L;
	
	int[] id;
	int[] data;
	
	public MapLine() {}
	
	public MapLine(int length) {
		this(createArray(length));
	}
	
	public MapLine(int[] id) {
		this.id = id;
		this.data = createArray(id.length);
	}
	
	public MapLine(int[] id, int[] data) {
		this.id = id;
		this.data = data;
	}
	
	public int getId(int x) {
		if(id.length <= x) {
			return 0;
		}
		return id[x];
	}
	
	public void setId(int x, int id) {
		if(this.id.length <= x) {
			extend(x+1);
		}
		this.id[x] = id;
	}
	
	public void setData(int x, int data) {
		if(id.length <= x) {
			extend(x+1);
		}
		this.data[x] = data;
	}
	
	public int getData(int x) {
		if(id.length <= x) {
			return 0;
		}
		return data[x];
	}
	
	public int getLenght() {
		return id.length;
	}
	
	public void extend(int newx) {
		int[] ids = new int[newx];
		int[] datas = new int[newx];
		int c = 0;
		for(int d : id) {
			if(c >= newx) {
				return;
			}
			ids[c] = d;
			datas[c] = getData(c);
			c++;
		}
		while(c < newx) {
			ids[c] = 0;
			datas[c] = 0;
			c++;
		}
		id = ids;
		data = datas;
	}
	
	private static int[] createArray(int length) {
		int[] id = new int[length];
		int i = 0;
		while(i < length) {
			id[i] = 0;
			i++;
		}
		return id;
	}

}
