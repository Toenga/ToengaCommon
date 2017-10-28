package fr.toenga.common.utils.general;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class FileUtils
{

	public static final String EOL = System.getProperty("line.separator");

	public static String readFile(File file) throws Exception
	{
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

		String str;
		StringBuilder stringBuilder = new StringBuilder();
		while ((str = in.readLine()) != null) {
			stringBuilder.append(str);
			stringBuilder.append(EOL);
		}

		in.close();
		return stringBuilder.toString();
	}

	public static void writeFile(File file, String json)
	{
		try
		{
			PrintWriter writer = new PrintWriter(file.getAbsolutePath(), "UTF-8");
			writer.println(json);
			writer.close();
		}
		catch (Exception error)
		{
			error.printStackTrace();
		}
	}


}
