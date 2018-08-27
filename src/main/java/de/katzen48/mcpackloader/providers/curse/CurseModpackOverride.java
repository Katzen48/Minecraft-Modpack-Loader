/**
 * @author Katzen48
 * @version 1.0
 */
package de.katzen48.mcpackloader.providers.curse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import de.katzen48.mcpackloader.ModpackOverride;

public class CurseModpackOverride implements ModpackOverride
{
	private String name;
	private String path;
	private InputStream inputStream;
	
	public CurseModpackOverride(String name, String path, InputStream inputStream)
	{
		this.name = name;
		this.path = path;
		this.inputStream = inputStream;
	}

	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public String getPath()
	{
		return path;
	}

	@Override
	public void extract(String path)
	{
		String extractPath = path.endsWith("/") ? path + this.path : path + "/" + this.path;
		File file = new File(extractPath);
		
		if(!file.exists())
			createFile(file);
		
		try
		{
			transfer(inputStream, new FileOutputStream(file));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	private void transfer(InputStream inputStream, OutputStream outputStream)
	{
		try
		{
			byte[] buf = new byte[4096];
			int len;

			while ((len = inputStream.read(buf)) > 0)
				outputStream.write(buf, 0, len);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void createFile(File file)
	{
		if(!file.getParentFile().exists())
		{
			file.getParentFile().mkdirs();
		}
			
		try
		{
			file.createNewFile();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
