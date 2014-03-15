package fr.skyforce77.towerminer.ressources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class FileContainer implements Serializable
{

	private static final long serialVersionUID = -9016818554406097333L;

	private byte[] filedata;
	private String filename;

	public FileContainer(File f, String filename) throws IOException
	{
		this.filename = filename;
		FileInputStream fis = new FileInputStream(f);
		filedata = new byte[fis.available()];
		fis.read(filedata);
		fis.close();
	}

	public String getFileName()
	{
		return filename;
	}

	public File recreate(File folder) throws IOException
	{
		folder.mkdirs();
		File file = new File(folder, getFileName());
		file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(filedata, 0, filedata.length);
		fos.flush();
		fos.close();
		return file;
	}

}
