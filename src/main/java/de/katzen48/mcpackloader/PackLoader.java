/**
 * @author Katzen48
 * @version 1.0
 */
package de.katzen48.mcpackloader;

import java.util.List;

import de.katzen48.mcpackloader.providers.curse.CurseModpackLoader;

public final class PackLoader
{
	public static final Modpack loadModpack(Provider provider, String name, String version)
	{
		return provider.loader.load(name, version);
	}
	
	public static final String getNewestModpackVersion(Provider provider, String name)
	{
		return provider.loader.getNewestVersion(name);
	}
	
	public static final List<String> getModpackVersions(Provider provider, String name)
	{
		return provider.loader.getVersions(name);
	}
	
	
	public static enum Provider
	{		
		CURSE(new CurseModpackLoader());
		
		private ModpackLoader loader;

		
		private Provider(ModpackLoader loader)
		{
			this.loader = loader;
		}
	}
}
