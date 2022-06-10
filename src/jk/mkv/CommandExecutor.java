package jk.mkv;

import java.io.InputStream;

public class CommandExecutor extends Thread
{
	private String command;
	private Process proc;
    
    public CommandExecutor(String command)
    {
    	this.command = command;
    }
    
    public void run()
    {
    	try
		{
    		proc = Runtime.getRuntime().exec(command);
    		while(!finished())
    			Thread.sleep(5000);
		}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    
    public InputStream getOutputStream()
    {
    	try
    	{
    		while(proc == null)
    			Thread.sleep(100);
        	
        	return proc.getInputStream();
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		return null;
    	}
    }
    
    public InputStream getErrorStream()
    {
    	try
    	{
    		while(proc == null)
    			Thread.sleep(100);
        	
        	return proc.getErrorStream();
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		return null;
    	}
    }
    
    public int exitStatus()
    {
    	if(!finished())
    		return -1;
    	
    	return proc.exitValue();
    }
    
    private boolean finished()
    {
    	try
    	{
    		proc.exitValue();
    		return true;
    	}
    	catch(IllegalThreadStateException e)
    	{
    		return false;
    	}
    }
}
