package jk.mkv;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Converter {
	
	private static Converter instance;

	public synchronized static Converter getInstance()
	{
		if(instance == null)
			instance = new Converter();
		
		return instance;
	}
	
	public static List<Job> autodetect()
	{
		return validate(new File(".").list());
	}
	
	public static List<Job> validate(String[] args)
	{
		if(args == null)
			return new LinkedList<Job>();
		
		List<Job> jobs = new LinkedList<Job>();
		
		for(int i=0; i<args.length; i++)
		{
			if(isValid(args[i]))
			{
				jobs.add(new Job(getAVI(args[i]), getSRT(args[i])));
			}
		}
			
		return jobs;
	}
	
	/**********************************************/
	
	private Queue<Job> queue;
	private Job current;
	private boolean windows;
	
	private Converter()
	{
		queue = new LinkedList<Job>();
		current = null;
		windows = System.getProperty("os.name").toUpperCase().contains("WIN");
	}
	
	public boolean isWindows()
	{
		return windows;
	}
	
	public synchronized void enqueue(List<Job> newJobs)
	{
		for(int i=0; i<newJobs.size(); i++)
		{
			if(!queue.contains(newJobs.get(i)))
				queue.add(newJobs.get(i));
		}
	}
	
	public void run(boolean recheck)
	{
		printJobs(this.queue);
		while((current = next()) != null)
		{			
			System.out.println("==================================================");
			System.out.println("[MKV] Start - " + current.getName());
			
			if(!current.execute())
				System.out.println("[MKV] " + current.getName() + " failed to execute! View stack trace for details.");
			
			System.out.println("[MKV] End - " + current.getName());
			System.out.println("==================================================");
			
			if(recheck)
			{
				enqueue(autodetect());
				printJobs(this.queue);
			}
		}
	}
	
	private synchronized Job next()
	{
		if(queue.isEmpty())
			return null;
		else
			return queue.remove();
	}

	
	/**********************************************/
	//aux
	
	private static void printJobs(Queue<Job> jobs)
	{
		System.out.println("==================================================");
		System.out.println("[MKV] Job list:\n");
		
		Iterator<Job> it = jobs.iterator();
		int count = 1;
		while(it.hasNext())
			printJob(it.next(), count++);
		
		System.out.println("==================================================");
	}
	
	private static void printJob(Job job, int number)
	{
		System.out.println("Job " + number + ":\n" + job.getDetails() + "\n");
	}
	
	private static boolean isValid(String filename)
	{
		if(getAVI(filename) == null || getSRT(filename) == null || getMKV(filename) != null)
			return false;
		else
			return true;
	}
	
	private static File getAVI(String avi)
	{
		if(avi.toUpperCase().endsWith(".AVI") || avi.toUpperCase().endsWith(".MP4"))
			return getFile(avi);
		else
			return null;
	}
	
	private static File getSRT(String avi)
	{
		return getFile(avi.substring(0, avi.lastIndexOf(".")) + ".srt");
	}
	
	private static File getMKV(String avi)
	{
		return getFile(avi.substring(0, avi.lastIndexOf(".")) + ".mkv");
	}
	
	private static File getFile(String filename)
	{
		File file = new File(filename);
		if(file.exists())
			return file;
		else
			return null;
	}
}
