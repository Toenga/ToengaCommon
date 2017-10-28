package fr.toenga.common.utils.i18n;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.toenga.common.utils.general.FileUtils;
import lombok.Getter;

public class I18n
{

	@Getter
	public static Gson									gson	= new GsonBuilder().setPrettyPrinting().create();
	private static Map<Locale, Map<String, String[]>> 	strings = new HashMap<>();
	private static Map<Locale, File>					lFolder = new HashMap<>();
	private static Random								random	= new Random();

	public static void loadI18n(File i18nFolder)
	{
		for (File langFolder : i18nFolder.listFiles())
		{
			try
			{
				String fileName = langFolder.getName();
				int pos = fileName.lastIndexOf(".");
				if (pos > 0)
				{
					fileName = fileName.substring(0, pos);
				}
				Locale locale = LocaleUtils.getLocaleByFullName(fileName);
				if (locale == null)
				{
					System.out.println("Unknown locale: " + fileName + ".");
					continue;
				}
				System.out.println(locale + " : " + langFolder.getName());
				lFolder.put(locale, langFolder);
				loadFolder(locale, langFolder, "");
				System.out.println("Loaded locale: " + fileName + "!");
			}
			catch (Exception exception)
			{
				exception.printStackTrace();
			}

		}
	}

	public static void loadFolder(Locale locale, File folder, String path) throws Exception
	{
		for (File file : folder.listFiles())
		{
			if (file.isDirectory())
			{
				System.out.println(file.getName());
				loadFolder(locale, file, path + file.getName() + ".");
			}
			if (file.getName().endsWith(".json"))
			{
				String fileName = file.getName();
				int pos = fileName.lastIndexOf(".");
				if (pos > 0) {
					fileName = fileName.substring(0, pos);
				}
				System.out.println("A : " + fileName);
				String fileContent = FileUtils.readFile(file);
				String[] data = gson.fromJson(fileContent, String[].class);
				String fullPath = path + fileName;
				System.out.println("B : " + fullPath);
				if (!strings.containsKey(locale))
				{
					strings.put(locale, new HashMap<>());
				}
				System.out.println(fileName);
				Map<String, String[]> maps = !strings.containsKey(locale) ? new HashMap<>() : strings.get(locale);
				maps.put(fullPath, data);
				strings.put(locale, maps);
			}
		}
	}

	private static String generateMessage(Locale locale, String key, Object... objects)
	{
		String[] defaults = new String[] { key };
		File folder = lFolder.get(locale);
		if (folder == null)
		{
			System.out.println("NOT OK");
			return key;
		}
		String path = folder.getAbsolutePath() + "/";
		String[] t = key.split("\\.");
		String fN = "";
		if (t != null && t.length > 0)
		{
			String[] w = Arrays.copyOf(t, t.length - 1);
			for (String p : w)
			{
				path += p + "/";
			}
			fN = t[t.length - 1] + ".json";
		}
		else
		{
			fN = key + ".json";
		}
		File pathFile = new File(path);
		if (!pathFile.exists())
		{
			pathFile.mkdirs();
		}
		File file = new File(pathFile, fN);
		try
		{
			file.createNewFile();
		}
		catch (IOException exception)
		{
			exception.printStackTrace();
		}
		String json = gson.toJson(defaults);
		FileUtils.writeFile(file, json);
		return key;
	}

	public static String getMessage(Locale locale, String key, Object... objects)
	{
		if (!strings.containsKey(locale))
		{
			generateMessage(locale, key, objects);
			return key;
		}
		Map<String, String[]> maps = strings.get(locale);
		if (!maps.containsKey(key))
		{
			generateMessage(locale, key, objects);
			return key;
		}
		String[] stringList = maps.get(key);
		String message = stringList[random.nextInt(stringList.length)];
		if (objects != null && objects.length > 0)
		{
			for (int i = objects.length - 1; i >= 0; i--)
			{
				message = message.replace("%" + i, objects[i].toString());
			}
		}
		return message;
	}

}