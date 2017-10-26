package i18n;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.toenga.common.utils.general.FileUtils;

public class I18n
{

	private static Gson									gson	= new GsonBuilder().setPrettyPrinting().create();
	private static Map<Locale, Map<String, String[]>> 	strings = new HashMap<>();
	private static Random								random	= new Random();

	public static void loadI18n(File i18nFolder)
	{
		for (File langFolder : i18nFolder.listFiles())
		{
			try {
				String fileName = langFolder.getName();
				int pos = fileName.lastIndexOf(".");
				if (pos > 0) {
				    fileName = fileName.substring(0, pos);
				}
				Locale locale = getLocale(fileName);
				if (locale == null)
				{
					System.out.println("Unknown locale: " + fileName + ".");
					continue;
				}
				loadFolder(locale, langFolder, "");
				System.out.println("Loaded locale: " + fileName + "!");
			} catch (IOException exception) {
				exception.printStackTrace();
			}
			
		}
	}
	
	private static Locale getLocale(String name)
	{
		for (Locale locale : Locale.getAvailableLocales())
		{
			String n = locale.getLanguage() + "-" + locale.getCountry();
			if (name.equalsIgnoreCase(n))
			{
				return locale;
			}
		}
		return null;
	}
	
	public static void loadFolder(Locale locale, File folder, String path) throws IOException
	{
		for (File file : folder.listFiles())
		{
			if (file.isDirectory())
			{
				loadFolder(locale, file, path + file.getName() + ".");
			}
			if (file.getName().endsWith(".json"))
			{
				String fileName = file.getName();
				int pos = fileName.lastIndexOf(".");
				if (pos > 0) {
				    fileName = fileName.substring(0, pos);
				}
				String fileContent = FileUtils.readFile(file);
				String[] data = gson.fromJson(fileContent, String[].class);
				String fullPath = path + fileName;
				if (!strings.containsKey(locale))
				{
					strings.put(locale, new HashMap<>());
				}
				Map<String, String[]> maps = new HashMap<>();
				maps.put(fullPath, data);
				strings.put(locale, maps);
			}
		}
	}
	
	public static String getMessage(Locale locale, String key)
	{
		if (!strings.containsKey(locale))
		{
			return key;
		}
		Map<String, String[]> maps = strings.get(locale);
		if (!maps.containsKey(key))
		{
			return key;
		}
		String[] stringList = maps.get(key);
		return stringList[random.nextInt(stringList.length)];
	}

}