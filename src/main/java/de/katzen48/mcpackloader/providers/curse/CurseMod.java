/**
 * @author Katzen48
 * @version 1.0
 */
package de.katzen48.mcpackloader.providers.curse;

import de.katzen48.mcpackloader.Mod;
import de.katzen48.mcpackloader.ModMetaData;

public class CurseMod implements Mod
{
	private CurseModMetaData metaData;
	
	public CurseMod(int projectId, int fileId)
	{
		metaData = new CurseModMetaData(projectId, fileId);
	}
	
	@Override
	public ModMetaData getMetaData()
	{
		return metaData;
	}
}
