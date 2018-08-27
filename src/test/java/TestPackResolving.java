import java.io.File;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import de.katzen48.mcpackloader.Mod;
import de.katzen48.mcpackloader.Modpack;
import de.katzen48.mcpackloader.ModpackOverride;
import de.katzen48.mcpackloader.PackLoader;
import de.katzen48.mcpackloader.PackLoader.Provider;
import de.katzen48.mcpackloader.providers.curse.CurseParser;

/**
 * @author Katzen48
 * @version 1.0
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestPackResolving
{	
	@Test
	public void testMcTimeResolving()
	{
		Assert.assertNotNull("Modpack can not be null.", PackLoader.loadModpack(Provider.CURSE, "minecraft time", "Time 0.92 Beta"));
	}
	
	@Test
	public void testMcTimeNewestVersion()
	{
		Assert.assertNotNull("Modpack can not have zero Versions.", PackLoader.getNewestModpackVersion(Provider.CURSE, "minecraft time"));
	}
	
	@Test
	public void testMcTimeVersioning()
	{
		Assert.assertFalse("Modpack can not have zero Versions.", PackLoader.getModpackVersions(Provider.CURSE, "minecraft time").isEmpty());
	}
	
	@Test
	public void testPackDotZip()
	{
		println("--------------[ START ]--------------");
		println("");
		
		File modpackFile = findFile(System.getProperty("java.io.tmpdir"), "^.*Time.*\\.zip");
		
		println("--------------[ LOGS ]--------------");
		println("");
		
		Modpack pack = new CurseParser(modpackFile).parse();
		Assert.assertNotNull("Pack '" + pack.getMetaData().getName() + " 'is in Folder " + modpackFile.getAbsolutePath(), pack);
		
		println("");
		println("--------------[ INFO ]--------------");
		println("");
		
		println("Name: " + pack.getMetaData().getName());
		println("Author: " + pack.getMetaData().getAuthor());
		println("Version: " + pack.getMetaData().getVersion());
		println("MC Version: " + pack.getMetaData().getMinecraftVersion());
		println("Forge Version: " + pack.getMetaData().getForgeVersion());
		
		println("");
		println("--------------[ MODS ]--------------");
		println("");
		
		for(Mod mod : pack.getMetaData().getMods())
		{
			println("");
			println("[MOD]");
			println("Name: " + mod.getMetaData().getName());
			println("Version: " + mod.getMetaData().getVersion());
			println("URL: " + mod.getMetaData().getUrl());
		}
		
		println("");
		println("------------[ OVERRIDES ]------------");
		println("");
		
		for(ModpackOverride override : pack.getOverrides())
		{
			println("");
			println("[OVERRIDE]");
			println("FileName: " + override.getName());
			println("Path: " + override.getPath());
		}
		
		println("");
		println("------------[ EXTRACTING ]------------");
		println("");
		
		pack.extract(System.getProperty("java.io.tmpdir") + "/Time");
		
		println("");
		println("--------------[ END ]--------------");
		
		
	}
	
	private void println(String string)
	{
		System.out.println(string);
		System.out.flush();
	}
	
	private File findFile(String directory, String regex)
	{
		File folder = new File(directory);
		
		for(File file : folder.listFiles())
			if(file.getName().matches(regex))
				return file;
		
		return null;
	}
}
