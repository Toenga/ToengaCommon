package i18n;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.toenga.common.utils.general.FileUtils;

public class I18n
{

	private static Gson						gson	= new GsonBuilder().setPrettyPrinting().create();
	private static Map<String, String[]> 	strings = new HashMap<>();
	private static Random					random	= new Random();

	public static void load(File folder) throws Exception
	{
		for (File file : folder.listFiles())
		{
			if (file.isDirectory())
			{
				load(file);
			}
			if (file.getName().endsWith(".json"))
			{
				String name = file.getName().replace(".json", "");
				String fileContent = FileUtils.readFile(file);
				String[] data = gson.fromJson(fileContent, String[].class);
				strings.put(name, data);
			}
		}
	}
	
	public static String getMessage(String key)
	{
		if (!strings.containsKey(key))
		{
			return key;
		}
		return strings.get(key)[random.nextInt(strings.get(key).length)];
	}

}
