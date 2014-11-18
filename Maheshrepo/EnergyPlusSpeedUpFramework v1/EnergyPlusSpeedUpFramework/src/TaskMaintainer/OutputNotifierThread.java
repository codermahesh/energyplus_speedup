package TaskMaintainer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class OutputNotifierThread implements Runnable 
{

	String watchDirectory;
	public OutputNotifierThread(String watchDirectory, boolean logged)
	{
		this.watchDirectory=watchDirectory;
		if(!logged)
		{
			logTask();
		}
		
	}

	void logTask()
	{
		/*Upon Every firsst run create submiited job entry to tasklog file*/
		try 
		{
			File taskLog = new File("TaskLog.log");

			if(!taskLog.exists())
			{

				taskLog.createNewFile();

			}
			FileWriter fileWritter = new FileWriter(taskLog.getName(),true);
	        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
	        
	        bufferWritter.write(watchDirectory+"\n");
	        bufferWritter.close();

		} 
		catch (IOException e) 
		{

		} 
		
	}
	
	
	@Override
	public void run() 
	{	
		/*initiate watch daemon*/
		try
		{
			OutputNotifier on =  new OutputNotifier(watchDirectory);
			on.startListening();
		}
		catch(Exception e)
		{
			
		}
	}

	static void removeLog(String logDirectory)
	{
		try
		{
			File taskLog = new File("TaskLog.log");
			 
			
			
			if(!taskLog.exists())
			{
				return;
			}
			
			
			File temp = File.createTempFile("tempfile", ".tmp");
			 BufferedWriter bwriter = new BufferedWriter(new FileWriter(temp));
			
			 
			FileInputStream fstream =  new FileInputStream(taskLog);
			BufferedReader breader = new BufferedReader(new InputStreamReader(new DataInputStream(fstream)));
			String strLine;
			
			while((strLine = breader.readLine()) != null)
			{
				if(!strLine.equals(logDirectory))
				{
					bwriter.write(strLine+"\n");
				}
			}
			
			taskLog.delete();
			temp.renameTo(taskLog.getAbsoluteFile());
			
			breader.close();
			bwriter.close();
			
		}
		catch(Exception e)
		{
			
		}
	}


	public static void initTaskLookUp()
	{
		try
		{
			File taskLog = new File("TaskLog.log");
			
			if(!taskLog.exists())
			{
				return;
			}

			FileInputStream fstream =  new FileInputStream(taskLog);
			BufferedReader breader = new BufferedReader(new InputStreamReader(new DataInputStream(fstream)));
			String strLine;
			
			while((strLine = breader.readLine()) != null)
			{
				Thread outputNotifierThread = new Thread(new OutputNotifierThread(strLine, true));
				outputNotifierThread.start();
				
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
