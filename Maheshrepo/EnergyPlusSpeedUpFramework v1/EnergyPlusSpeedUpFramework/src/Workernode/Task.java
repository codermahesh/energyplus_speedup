package Workernode;

import java.util.ArrayList;

public class Task 
{
	
	String id=null;	/*identification*/
	String WeatherFile;
	ArrayList<String> idfFiles;
	
	
	public void addToTaskList(String id,String idfFile,String weatherFile)
	{	
			/*if no task id then initialise array*/
		
		if(this.id == null)
		{
			
			this.id= id;
			this.WeatherFile=weatherFile;
			idfFiles = new ArrayList<String>();
			
			idfFiles.add(idfFile);
			
		}
		else
		{
			idfFiles.add(idfFile);	
		}	
		
	}
	
}
