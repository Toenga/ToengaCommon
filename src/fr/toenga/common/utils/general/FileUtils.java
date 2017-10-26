package fr.toenga.common.utils.general;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileUtils {
	
	public static final String EOL = System.getProperty("line.separator");
	
	public static String readFile(File file) throws IOException {
		BufferedReader br = null;
		FileReader fr = null;
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String nextLine = "";
			StringBuilder sb = new StringBuilder();
			while ((nextLine = br.readLine()) != null) {
				sb.append(nextLine);
				sb.append(EOL);
			}
			return sb.toString();
		}
		finally {
			if (br != null) br.close();
			if (fr != null) fr.close();
		}
	}

	
}
