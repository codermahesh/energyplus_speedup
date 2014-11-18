package TaskMaintainer;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

public class OutputNotifier 
{
	
	private String watchDirectory;
	
	public OutputNotifier(String DirectoryToWatch)
	{
		this.watchDirectory=DirectoryToWatch;		
	}
	
	public void startListening()
	{
		int count=0;
		
		Path directory = Paths.get(watchDirectory);
		
		
		try
		{
			WatchService watcher =directory.getFileSystem().newWatchService();
			directory.register(watcher,StandardWatchEventKinds.ENTRY_CREATE);
			WatchKey watckKey = watcher.take();

			while(count<3)
			{
				List<WatchEvent<?>> events = watckKey.pollEvents();

				for (WatchEvent<?> event : events) 
				{
					if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) 
					{
						//System.out.println("Created: " + event.context().toString());
						count++;
						
					}
			 
				}
				
				Thread.sleep(1000);
			}
			
			//System.out.println("Output ready");
			
			/* Lauch Collate script */
			
			initiateCollate(watchDirectory,watchDirectory);

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	public void stopListening()
	{
		
	}
	
	
	public void initiateCollate(String esoFolder,String outputFolder)
	{
		
		try 
		{
			OutputCollateLauncher.launchCollate(esoFolder, outputFolder);
			OutputNotifierThread.removeLog(watchDirectory);			
		} 
		catch (IOException e) 
		{
			
		}
		
	}
	
	/*
	
	public static void main(String [] args)
	{
		OutputNotifier tsk = new OutputNotifier("E:/Ts");
		tsk.startListening();
	}
	
	*/

}
