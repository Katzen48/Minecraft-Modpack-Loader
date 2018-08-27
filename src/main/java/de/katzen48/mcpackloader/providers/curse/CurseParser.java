/**
 * @author Katzen48
 * @version 1.0
 */
package de.katzen48.mcpackloader.providers.curse;

import java.io.File;
import java.util.zip.ZipFile;

import de.katzen48.mcpackloader.Modpack;

public class CurseParser
{
	private File packFile;
	
	public CurseParser(File packFile)
	{
		this.packFile = packFile;
	}
	
	public Modpack parse()
	{
		try
		{
			return new CurseModpack(new ZipFile(packFile));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
