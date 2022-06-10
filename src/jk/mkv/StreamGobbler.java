package jk.mkv;

import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamGobbler extends Thread
{
    private InputStream is;
    private boolean active;
    
    public StreamGobbler(InputStream is)
    {
        this.is = is;
        this.active = true;
    }
    
    public void run()
    {	
        try
        {
            InputStreamReader isr = new InputStreamReader(is);
            
            while(isActive())
            {
            	while(isr.ready())
            	{
            		System.out.print((char) isr.read());
                	//Thread.sleep(1);
            	}
            	Thread.sleep(100);
            }
            
            isr.close();
        }
        catch (Exception e)
        {
        	e.printStackTrace();  
        }
    }
    
    private boolean isActive()
    {
    	return active;
    }
    
    public void finish()
    {
    	this.active = false;
    }
}