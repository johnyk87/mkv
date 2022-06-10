package jk.mkv;

public class CLIUtil {

	private static final String CREATE_PROJECT = "avidemux2_cli --nogui --force-alt-h264 --force-unpack --rebuild-index --load %AVI% --save-workbench %SCRIPT%";
	private static final String CREATE_PROJECT_WIN = "avidemux2_cli --nogui --force-alt-h264 --force-unpack --rebuild-index --load \"%AVI%\" --save-workbench \"%SCRIPT%\"";
	private static final String ENCODE_AVI = "avidemux2_cli --nogui --force-alt-h264 --run %SCRIPT% --save %MKV%";
	private static final String ENCODE_AVI_WIN = "avidemux2_cli --nogui --force-alt-h264 --run \"%SCRIPT%\" --save \"%MKV%\"";
	private static final String MKVMERGE = "mkvmerge --title %TITLE% -o %MKV% %VIDEO% %SRT%";
	private static final String MKVMERGE_WIN = "mkvmerge --title \"%TITLE%\" -o \"%MKV%\" \"%VIDEO%\" \"%SRT%\"";
	
	public static void createProject(String avi, String script) throws Exception
	{
		String command = CREATE_PROJECT;
		if (Converter.getInstance().isWindows())
			command = CREATE_PROJECT_WIN;
		
		command = command.replace("%AVI%", avi);
		command = command.replace("%SCRIPT%", script);
		
		execute(command);
	}
	
	public static void encodeAVI(String script, String mkv) throws Exception
	{
		String command = ENCODE_AVI;
		if (Converter.getInstance().isWindows())
			command = ENCODE_AVI_WIN;
		
		command = command.replace("%SCRIPT%", script);
		command = command.replace("%MKV%", mkv);
		
		execute(command);
	}
	
	public static void mkvmerge(String title, String mkv, String video, String srt) throws Exception
	{
		String command = MKVMERGE;
		if (Converter.getInstance().isWindows())
			command = MKVMERGE_WIN;
		
		command = command.replace("%TITLE%", title);
		command = command.replace("%MKV%", mkv);
		command = command.replace("%VIDEO%", video);
		command = command.replace("%SRT%", srt);
		
		execute(command);
	}
	
	private static void execute(String command) throws Exception
	{
		System.out.println("[CLI] Command: " + command);
		
		CommandExecutor exec = new CommandExecutor(command);
		exec.start();
		
		StreamGobbler output = new StreamGobbler(exec.getOutputStream());
		output.start();
		
		StreamGobbler error = new StreamGobbler(exec.getErrorStream());
		error.start();
		
		exec.join();
		output.finish();
		output.join();
		error.finish();
		error.join();
		
		System.out.println("[CLI] Exit value: " + exec.exitStatus());
	}
}
