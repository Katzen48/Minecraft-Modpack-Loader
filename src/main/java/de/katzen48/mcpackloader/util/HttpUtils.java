/**
 * @author Katzen48
 * @version 1.0
 */
package de.katzen48.mcpackloader.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class HttpUtils
{
	private static CookieManager cookieManager = new CookieManager();
	private static final String AGENT = "Minecraft-Modpack-Loader";
	
	public static String downloadFile(String url, String directory, String fileName)
	{			
		if(!directory.endsWith("/") && !directory.endsWith(File.pathSeparator))
			directory += "/";
		
		try
		{
			String absoluteFilePath = directory + fileName;
			
			URLConnection con = new URL(encodeUrl(url)).openConnection();
			con.setRequestProperty("User-Agent", AGENT);
			ReadableByteChannel channel = Channels.newChannel(con.getInputStream());
			FileOutputStream output = new FileOutputStream(new File(absoluteFilePath));
			
			System.out.println("Starting Download of '" + url + "' into '" + absoluteFilePath + "'");
			
			output.getChannel().transferFrom(channel, 0, Long.MAX_VALUE);
			output.close();
			channel.close();
			
			System.out.println("Finished Download");
			
			return directory + fileName;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			
			return null;
		}
	}
	
	public static String getLocationFromRedirect(String url, boolean encodeUrl)
	{
		if(encodeUrl)
			url = encodeUrl(url);
		
		System.out.println("Resolving Location from Redirect. (URL: '" + url + "')");
		
		HttpURLConnection.setFollowRedirects(false);
		
		try
		{
			HttpURLConnection https = (HttpURLConnection) new URL(url).openConnection();
			https.setRequestProperty("User-Agent", AGENT);
			
			if(cookieManager.getCookieStore().getCookies().size() > 0)
			{
				https.setRequestProperty("Cookie", cookieManager.getCookieStore().getCookies().stream()
						.map(HttpCookie::getValue).collect(Collectors.joining(";")));
			}
			
			https.connect();
			
			if(https.getHeaderFields().get("Set-Cookie") != null)
			{
				for(String cookie : https.getHeaderFields().get("Set-Cookie"))
				{
					try
					{
						cookieManager.getCookieStore().add(https.getURL().toURI(), HttpCookie.parse(cookie).get(0));
					}
					catch (URISyntaxException e)
					{
						e.printStackTrace();
					}
				}
			}
			
			String location = https.getHeaderField("Location");
			
			if(encodeUrl)
				location = encodeUrl(url);
			
			System.out.println("Resolved Location: '" + location + "'");
			
			return location;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String encodeUrl(String url)
	{
		url = url.replaceAll(" ", "%20");
		
		try
		{
			String[] baseUrlParts = url.split("://");
			StringBuilder builder = new StringBuilder();
			
			int i = 0;
			for(String locationPart : baseUrlParts[1].split("/"))
			{
				builder.append((i == 0 ? "" : "/") + URLEncoder.encode(locationPart, "UTF-8"));
				i++;
			}
			
			return baseUrlParts[0] + "://" + builder.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			
			return url;
		}
	}
	
	public static Elements getElementsFromUrl(String url, String elementClass)
	{
		try
		{
			Document doc = Jsoup.connect(url).get();
			
			return doc.select(elementClass);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			
			return null;
		}
	}
	
	public static JsonObject getJsonFromUrl(String url)
	{
		try
		{
			URL website = new URL(url);
			
			InputStream stream = website.openConnection().getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			StringBuilder builder = new StringBuilder();
			
			String line;
			while((line = reader.readLine()) != null)
				builder.append(line);
			
			stream.close();
			
			JsonParser parser = new JsonParser();
			
			return parser.parse(builder.toString()).getAsJsonObject();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			
			return new JsonObject();
		}
	}
}
