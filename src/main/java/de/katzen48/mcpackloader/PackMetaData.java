/**
 * @author Katzen48
 * @version 1.0
 */
package de.katzen48.mcpackloader;

import java.util.List;

public interface PackMetaData extends MetaData
{
	String getAuthor();
	String getMinecraftVersion();
	String getForgeVersion();
	List<Mod> getMods();
}
