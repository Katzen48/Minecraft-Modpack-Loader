/**
 * @author Katzen48
 * @version 1.0
 */
package de.katzen48.mcpackloader.providers.curse;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import de.katzen48.mcpackloader.Mod;
import de.katzen48.mcpackloader.PackMetaData;

public class CursePackMetaData implements PackMetaData
{
	private String name;
	private String version;
	private String author;
	private String minecraftVersion;
	private String forgeVersion;
	private String url = null;
	private String overridesDirectory;
	private List<Mod> mods;
	
	protected CursePackMetaData(String json)
	{
		parse(json);
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
	public String getAuthor()
	{
		return author;
	}

	@Override
	public String getMinecraftVersion()
	{
		return minecraftVersion;
	}

	@Override
	public String getForgeVersion()
	{
		return forgeVersion;
	}

	@Override
	public String getUrl()
	{
		return url;
	}
	
	public String getOverridesDirectory()
	{
		return overridesDirectory;
	}

	@Override
	public List<Mod> getMods()
	{
		return mods;
	}
	
	
	private void parse(String json)
	{
		JsonObject jsonObject = new JsonObject();
		
		JsonParser parser = new JsonParser();
		JsonElement jsonElement = parser.parse(json);
		jsonObject = jsonElement.getAsJsonObject();
		
		//PARSING
		name = jsonObject.get("name").getAsString();
		version = jsonObject.get("version").getAsString();
		author = jsonObject.get("author").getAsString();
		minecraftVersion = jsonObject.get("minecraft").getAsJsonObject().get("version").getAsString();
		forgeVersion = jsonObject.get("minecraft").getAsJsonObject().get("modLoaders").getAsJsonArray().get(0).getAsJsonObject().get("id").getAsString();
		overridesDirectory = jsonObject.get("overrides").getAsString();
		
		//PARSING OF MODS
		mods = new ArrayList<>();
		for(JsonElement mod : jsonObject.get("files").getAsJsonArray())
		{
			JsonObject modObject = mod.getAsJsonObject();
			mods.add(new CurseMod(modObject.get("projectID").getAsInt(), modObject.get("fileID").getAsInt()));
		}
	}
}
