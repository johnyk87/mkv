package jk.mkv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class ConfigurationLoader {
	
	private static final String CONFIG_SCRIPT = "/configurations.js";
	private static final String CONFIG_SCRIPT_WIN = "/configurations_win.js";
	private static final String BREAK_LINE = "//** Video Codec conf **";
	
	public static void load(String videoScript, String finalScript) throws Exception
	{
		BufferedWriter bw = null;
		try
		{
			bw = new BufferedWriter(new FileWriter(finalScript));
			
			loadVideoScript(videoScript, bw);
			loadConfigScript(bw);
		}
		finally
		{
			if(bw != null)
				bw.close();
		}
	}
	
	private static void loadVideoScript(String videoScript, BufferedWriter bw) throws Exception
	{
		BufferedReader br = new BufferedReader(new FileReader(videoScript));
		
		String line = null;
		while(((line = br.readLine()) != null) && !line.contains(BREAK_LINE))
		{
			bw.write(line);
			bw.newLine();
		}
		
		br.close();
	}
	
	private static void loadConfigScript(BufferedWriter bw) throws Exception
	{
		String script = CONFIG_SCRIPT;
		if(Converter.getInstance().isWindows())
			script = CONFIG_SCRIPT_WIN;
		
		BufferedReader br = new BufferedReader(new InputStreamReader(ConfigurationLoader.class.getResourceAsStream(script)));
		
		String line = null;
		while((line = br.readLine()) != null)
		{
			bw.write(line);
			bw.newLine();
		}
		
		br.close();
	}
}
