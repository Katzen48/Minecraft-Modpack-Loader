/**
 * @author Katzen48
 * @version 1.0
 */
package de.katzen48.mcpackloader.providers.curse;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import de.katzen48.mcpackloader.Mod;
import de.katzen48.mcpackloader.Modpack;
import de.katzen48.mcpackloader.ModpackOverride;
import de.katzen48.mcpackloader.PackMetaData;
import de.katzen48.mcpackloader.util.HttpUtils;

public class CurseModpack implements Modpack
{
	public static final String MANIFEST_PATH = "manifest.json";
	
	private ZipFile zip;
	private CursePackMetaData metaData;
	private List<ModpackOverride> overrides;
	
	public CurseModpack(ZipFile zip)
	{
		this.zip = zip;
		parse();
	}
	
	@Override
	public PackMetaData getMetaData()
	{
		return metaData;
	}
	
	private void parse()
	{
		ZipEntry entry = zip.getEntry(MANIFEST_PATH);
		
		String metaData = "";
		
		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(zip.getInputStream(entry), Charset.forName("UTF-8")));
			String line = null;
			
			while((line = reader.readLine()) != null)
			{
				metaData += line;
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		this.metaData = new CursePackMetaData(metaData);
		this.overrides = new ArrayList<>();
		
		parseOverrides();
	}
	
	private void parseOverrides()
	{
		ZipEntry entry = zip.getEntry(metaData.getOverridesDirectory());
		
		if(entry == null)
			return;
		
		for(ZipEntry file : collectFiles(entry))
		{
			String[] path = file.getName().split("/");
			
			try
			{
				overrides.add(new CurseModpackOverride(path[path.length-1]
						, file.getName(), zip.getInputStream(file)));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private List<ZipEntry> collectFiles(ZipEntry startEntry)
	{
		List<ZipEntry> entries = new ArrayList<>();
		
		try
		{
			if(zip.getInputStream(startEntry) != null)
			{
				entries.add(startEntry);
				
				return entries;
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		Enumeration<? extends ZipEntry> zipEnum = zip.entries();
		
		while(zipEnum.hasMoreElements())
		{
			ZipEntry nextEntry = zipEnum.nextElement();
			
			if(!nextEntry.isDirectory() && nextEntry.getName().startsWith(startEntry.getName()))
				entries.add(nextEntry);
		}
		
		return entries;
	}

	@Override
	public List<ModpackOverride> getOverrides()
	{
		return overrides;
	}

	@Override
	public void extract(String path)
	{
		if(!path.endsWith("/"))
			path += "/";
		
		File modsFolder = new File(path + "mods");
		
		if(!modsFolder.exists())
			modsFolder.mkdirs();
		
		//DOWNLOAD MODS
		for(Mod mod : metaData.getMods())
		{
			System.out.println("Loading Mod '" + mod.getMetaData().getName() + "'");
			HttpUtils.downloadFile(mod.getMetaData().getUrl(), modsFolder.getAbsolutePath(), mod.getMetaData().getNameOnDisk());
		}	
		
		//EXTRACT OVERRIDES
		for(ModpackOverride override : overrides)
			override.extract(modsFolder.getParent());
	}
}
