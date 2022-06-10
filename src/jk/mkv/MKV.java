package jk.mkv;

import java.util.LinkedList;
import java.util.List;

public class MKV {
	
	public static void main(String[] args)
	{
		try
		{
			List<Job> jobs = new LinkedList<Job>();
			boolean recheck = false;
			switch(args.length)
			{
				case 0:
				{
					jobs = Converter.autodetect();
					recheck = true;
					break;
				}
				case 1:
				{
					if(isHelp(args[0]))
					{
						usage();
						return;
					}
					else
					{
						jobs = Converter.validate(args);
						break;
					}
				}
				default:
				{
					jobs = Converter.validate(args);
					break;
				}
			}
			
			Converter.getInstance().enqueue(jobs);
			Converter.getInstance().run(recheck);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private static boolean isHelp(String arg)
	{
		return arg.equals("-h") || arg.equals("-help") || arg.equals("--help");
	}
	
	private static void usage()
	{
		System.out.println("[MKV] USAGE: java MKV");
		System.out.println("             java MKV -h");
		System.out.println("             java MKV -help");
		System.out.println("             java MKV --help");
		System.out.println("             java MKV <input-files>");
		System.out.println();
		System.out.println("   -h              displays this help");
		System.out.println("   -help           displays this help");
		System.out.println("   --help          displays this help");
		System.out.println("   <input-files>   a list of avi files to convert. If omited, an automatic job queue will be created with any avi/srt file combination in the current folder.");
		System.out.println();
	}
}
