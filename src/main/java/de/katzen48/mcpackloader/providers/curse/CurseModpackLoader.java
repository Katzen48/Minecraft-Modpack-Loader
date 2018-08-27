/**
 * @author Katzen48
 * @version 1.0
 */
package de.katzen48.mcpackloader.providers.curse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.JsonObject;

import de.katzen48.mcpackloader.Modpack;
import de.katzen48.mcpackloader.ModpackLoader;
import de.katzen48.mcpackloader.util.HttpUtils;

public class CurseModpackLoader implements ModpackLoader
{	
	@Override
	public Modpack load(String name, String version)
	{
		String packUrl = Curse.CURSE_MODPACK_URL + getCurseName(name);
		
		int versionNumber = getVersionNumber(packUrl + "/files", version);
		
		if(versionNumber == -1)
			return null;
		
		return load(getProjectId(packUrl), versionNumber);
	}
	
	@Override
	public Modpack load(int projectId, int versionId)
	{
		JsonObject modpackMeta = HttpUtils.getJsonFromUrl(Curse.UNOFFICIAL_API_URL + "direct/addon/" + projectId + "/file/" + versionId);
		String fileUrl = modpackMeta.get("downloadUrl").getAsString();
		String fileName = modpackMeta.get("fileNameOnDisk").getAsString();
		
		String tmpFileUrl = HttpUtils.downloadFile(fileUrl, System.getProperty("java.io.tmpdir"), fileName);
		
		return new CurseParser(new File(tmpFileUrl)).parse();
	}
	
	private int getProjectId(String url)
	{
		return Integer.parseInt(HttpUtils.getElementsFromUrl(url, ".info-data").first().text());
	}
	
	private int getVersionNumber(String url, String version)
	{
		for(Element element : getVersionElements(url))
		{
			if(element.attr("data-name").equalsIgnoreCase(version))
				return Integer.parseInt(element.attr("href").split("/")[4]);
		}
		
		return -1;
	}
	
	private List<Element> getVersionElements(String url)
	{
		List<Element> elements = new ArrayList<>();
		Elements fileNameContainers = HttpUtils.getElementsFromUrl(url, ".project-file-name-container");
		
		for(Element element : fileNameContainers)
		{
			elements.add(element.selectFirst(".overflow-tip"));
		}
		
		return elements;
	}
	
	private String getCurseName(String name)
	{
		return name.toLowerCase().replaceAll(" ", "-");
	}

	@Override
	public List<String> getVersions(String name)
	{
		List<String> versions = new ArrayList<>();
		
		for(Element element : getVersionElements(Curse.CURSE_MODPACK_URL + getCurseName(name) + "/files"))
		{
			versions.add(element.attr("data-name"));
		}
		
		return versions;
	}

	@Override
	public String getNewestVersion(String name)
	{
		List<String> versions = getVersions(name);
		
		return versions.isEmpty() ? null : versions.get(0);
	}
}
