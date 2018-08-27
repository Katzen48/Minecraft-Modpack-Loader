/**
 * @author Katzen48
 * @version 1.0
 */
package de.katzen48.mcpackloader;

public interface ModpackOverride
{
	public String getName();
	public String getPath();
	public void extract(String path);
}
