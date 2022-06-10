package jk.mkv;

import java.io.File;

public class Job {
	
	private static final String TEMP_SUFFIX = ".temp.";
	
	private static final String TEMP_JS = ".temp.js";
	private static final String JS = ".js";
	private static final String MKV = ".mkv";
	private static final String SRT = ".srt";
	
	private static final String STAT = MKV + ".stat";
	private static final String MBTREE = STAT + ".mbtree";
	
	private File avi;
	private File srt;
	
	private String workdir;	
	private String title;
	private String mkv;
	
	private String tempSuffix;
	
	public Job(File avi, File srt)
	{
		this.avi = avi;
		this.srt = srt;
		this.workdir = inferWorkdir();
		this.title = inferTitle();
		this.mkv = inferMKV();
		this.tempSuffix = TEMP_SUFFIX + title;
	}
	
	private String inferWorkdir()
	{
		return avi.getAbsolutePath().substring(0, avi.getAbsolutePath().lastIndexOf(File.separatorChar) + 1);
	}
	
	private String inferTitle()
	{
		return avi.getName().substring(0, avi.getName().lastIndexOf("."));
	}
	
	private String inferMKV()
	{
		return title + MKV;
	}
	
	public boolean execute()
	{
		try
		{
			print(1);
			convertSRT();			//creates TEMP_SRT
			assertFile(tempSuffix + SRT);
			print(2);
			createProject();		//creates TEMP_SCRIPT
			assertFile(TEMP_JS);
			print(3);
			loadConfigurations();	//creates FINAL_SCRIPT
			assertFile(tempSuffix + JS);
			rm(TEMP_JS);
			print(4);
			encodeAVI();			//creates TEMP_MKV
			assertFile(tempSuffix + MKV);
			print(5);
			mkvmerge();				//creates MKV
			assertFile(mkv);
			print(6);
			rm(tempSuffix + MKV);
			
			return true;
		}
		catch(Exception e)
		{			
			e.printStackTrace();
			return false;
		}
		finally //does not delete TEMP_MKV as it may be used without calling mkv_converter
		{
			rm(TEMP_JS);
			rm(tempSuffix + SRT);
			rm(tempSuffix + JS);
			rm(tempSuffix + STAT);
			rm(tempSuffix + MBTREE);	
		}
	}
	
	private void convertSRT() throws Exception
	{
		SRTConverter.convertToDefault(srt.getAbsolutePath(), workdir + tempSuffix + SRT);
	}
	
	private void createProject() throws Exception
	{
		CLIUtil.createProject(avi.getAbsolutePath(), workdir + TEMP_JS);
	}
	
	private void loadConfigurations() throws Exception
	{
		ConfigurationLoader.load(workdir + TEMP_JS, workdir + tempSuffix + JS);
	}
	
	private void encodeAVI() throws Exception
	{
		CLIUtil.encodeAVI(workdir + tempSuffix + JS, workdir + tempSuffix + MKV);
	}
	
	private void mkvmerge() throws Exception
	{
		CLIUtil.mkvmerge(title, workdir + mkv, workdir + tempSuffix + MKV, workdir + tempSuffix + SRT);
	}
	
	private void rm(String filename)
	{
		try
		{
			File rm = new File(workdir + filename);
			if(rm.exists())
				rm.delete();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void assertFile(String filename) throws Exception
	{
		File rm = new File(workdir + filename);
		if(!rm.exists())
			throw new MKVException("Required file " + filename + " was not created.");
	}
	
	private void print(int step)
	{
		System.out.println("==================================================");
		System.out.print("[MKV] Step " + step + " - ");
		switch(step)
		{
			case 1:
			{
				System.out.println("Convert SRT to UTF-8\n");
				return;
			}
			case 2:
			{
				System.out.println("Create project script\n");
				return;
			}
			case 3:
			{
				System.out.println("Load configuration script\n");
				return;
			}
			case 4:
			{
				System.out.println("Encode video\n");
				return;
			}
			case 5:
			{
				System.out.println("Merge subtitles\n");
				return;
			}
			case 6:
			{
				System.out.println("Delete temporary files\n");
				return;
			}
		}
	}
	
	public String getName()
	{
		return "Job: " + avi.getName();
	}
	
	public String getDetails()
	{
		return "[WORKDIR] " + workdir +
			 "\n[VID] " + avi.getName() +
		     "\n[SUB] " + srt.getName() +
		     "\n[MKV] " + mkv;
	}
	
	@Override
	public boolean equals(Object other)
	{
		if(other != null && other instanceof Job)
			return ((Job) other).avi.equals(this.avi);
		
		return false;
	}
}
