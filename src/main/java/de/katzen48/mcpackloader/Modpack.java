/**
 * @author Katzen48
 * @version 1.0
 */
package de.katzen48.mcpackloader;

import java.util.List;

public interface Modpack
{	
	PackMetaData getMetaData();
	List<ModpackOverride> getOverrides();
	void extract(String path);
}
