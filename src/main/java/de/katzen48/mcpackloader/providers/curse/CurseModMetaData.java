/**
 * @author Katzen48
 * @version 1.0
 */
package de.katzen48.mcpackloader.providers.curse;

import com.google.gson.JsonObject;

import de.katzen48.mcpackloader.ModMetaData;
import de.katzen48.mcpackloader.util.HttpUtils;

public class CurseModMetaData implements ModMetaData
{
	private String name;
	private String version;
	private String url;
	private String nameOnDisk;
	
	public CurseModMetaData(int projectId, int fileId)
	{
		parse(projectId, fileId);
	}
	
	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public String getVersion()
	{
		return version;
	}

	@Override
	public String getUrl()
	{
		return url;
	}
	
	@Override
	public String getNameOnDisk()
	{
		return nameOnDisk;
	}
	
	private void parse(int projectId, int fileId)
	{
		this.name = parseAddonMeta(projectId).get("name").getAsString();
		
		JsonObject fileMeta = parseAddonFileMeta(projectId, fileId);
		
		this.version = fileMeta.get("fileName").getAsString();
		this.url = HttpUtils.encodeUrl(fileMeta.get("downloadUrl").getAsString());
		this.nameOnDisk = fileMeta.get("fileNameOnDisk").getAsString();
	}
	
	private JsonObject parseAddonMeta(int projectId)
	{
		return HttpUtils.getJsonFromUrl(Curse.UNOFFICIAL_API_URL + "direct/addon/" + projectId);
	}
	
	private JsonObject parseAddonFileMeta(int projectId, int fileId)
	{
		return HttpUtils.getJsonFromUrl(Curse.UNOFFICIAL_API_URL + "direct/addon/" + projectId + "/file/" + fileId);
	}
}
