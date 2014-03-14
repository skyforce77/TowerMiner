package fr.skyforce77.towerminer.ressources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class FileContainer implements Serializable{

	private static final long serialVersionUID = -9016818554406097333L;

	private ArrayList<byte[]> read = new ArrayList<byte[]>();
	private ArrayList<Integer> readcount = new ArrayList<Integer>();
	private String filename;

	public FileContainer(File f, String filename) throws IOException {
		this.filename = filename;
		FileInputStream fis = new FileInputStream(f);
		int count;
		byte data[] = new byte[1024];
		while ((count = fis.read(data)) != -1) {
			read.add(data);
			readcount.add(count);
			System.out.println(data.hashCode()+":"+count);
		}
		fis.close();
	}

	public String getFileName() {
		return filename;
	}

	public File recreate(File folder) throws IOException {
		folder.mkdirs();
		File file = new File(folder, getFileName());
		//if(!file.exists()) {
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			int i = 0;
			while(i < read.size()) {
				fos.write(read.get(i), 0, readcount.get(i));
				System.out.println(read.get(i).hashCode()+":"+readcount.get(i));
				i++;
			}
			fos.flush();
			fos.close();
		//}
		return file;
	}

}
