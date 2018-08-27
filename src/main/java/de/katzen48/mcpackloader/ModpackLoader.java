/**
 * @author Katzen48
 * @version 1.0
 */
package de.katzen48.mcpackloader;

import java.util.List;

public interface ModpackLoader
{
	Modpack load(String name, String version);
	Modpack load(int projectId, int versionId);
	List<String> getVersions(String name);
	String getNewestVersion(String name);
}
