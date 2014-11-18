/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Workernode;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author ybar
 */


/*  This class creates thread that monitors  change "CREATE" in file system at Task folder
 *  
 *  if change is detected then it launches new thread to see if there is job associated with
 *  current node 
 * 
 */

public class TaskMonitor implements Runnable
{
	String moniterDirectory;
	
	public TaskMonitor(String energyplusFolder) 
	{
		this.moniterDirectory = energyplusFolder+"\\Task";
	}
	
	@Override
	
	public void run() 
	{
		/* install watch Service on TaskDirectory*/
		Path directory = Paths.get(moniterDirectory);
		try
		{
			WatchService watcher =directory.getFileSystem().newWatchService();
			directory.register(watcher,StandardWatchEventKinds.ENTRY_CREATE);
			WatchKey watckKey = watcher.take();
			
			
			while(true)
			{
				
				
				List<WatchEvent<?>> events = watckKey.pollEvents();
				for (WatchEvent<?> event : events) 
				{
					/* Look for new File update in Task folder*/
					if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) 
					{						
						String taskFilePath=moniterDirectory+"\\"+event.context().toString();
						System.out.println(taskFilePath);
						
						Thread taskAllocationCheckerThread= new 
								
								Thread (new TaskAllocationChecker(taskFilePath),"Allocation Checker");
						
						taskAllocationCheckerThread.run();
						
					}
					
				}	
				Thread.sleep(1000);
			}
			
		}
		catch(Exception e)	
		{
			 /*Create Log file*/
			 Logger.getLogger(TaskMonitor.class.getName()).log(Level.SEVERE, null, e);
		}
		
	}
 
    
}
