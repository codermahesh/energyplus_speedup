package Workernode;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ApplicationEnvironment.PlatformInformation;
import ApplicationEnvironment.WorkingSettings;


public class TaskAllocationChecker implements Runnable 
{
	String taskFile;	/*Complete Path*/
	
	public TaskAllocationChecker(String taskFile) 
	{
		this.taskFile=taskFile;
		
	}
	
	@Override
	public void run() 
	{
		try
		{
				/*read newly created task xml file */
				Task assignment=readTaskXml();
				
				if(assignment!=null)
				{
					//System.out.println(assignment.id);
					//System.out.println(assignment.WeatherFile);
					//for(String s : assignment.idfFiles)
					//{
					//	System.out.println(s);
					//}	
	
					launchSimulation(assignment);
				}

		}
		catch(Exception e)
		{
			Logger.getLogger(TaskAllocationChecker.class.getName()).log(Level.SEVERE, null, e);
		}
		
	}
	
	/* Function launches energy plus simulation process with commandline arguments input files
	 * specified in task*/
	private void launchSimulation(Task task)
	{
		String commandLineArguments[]={"","",""};
		Runtime runtime = Runtime.getRuntime();
		
		String taskDirectory= WorkingSettings.dropboxfolder+"\\"+task.id+"\\";
		
		/* format : Energyplus.exe input.idf input.epw */
		/*format  :  0              1              2   */
		commandLineArguments[0]=WorkingSettings.simulationFile;
		commandLineArguments[2]=taskDirectory+task.WeatherFile;
		
		try
		{
			for(String idf : task.idfFiles)
			{
				
				commandLineArguments[1]=taskDirectory+idf;					
				Process process =runtime.exec(commandLineArguments);
			}
		}
		catch(IOException e)
		{
			Logger.getLogger(TaskAllocationChecker.class.getName()).log(Level.SEVERE, null, e); 
		}
	}
	
	/* This function reads taskID.xml and checks whether current machine  has been 
	 * given any allocations or not 
	 * @returns: Task  on successful allocation else null
	 * */
	public Task readTaskXml()
	{
		try
		{
			
			/* XML reading api convention*/
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(taskFile);			
			doc.getDocumentElement().normalize();
			
			NodeList nList = doc.getElementsByTagName("task");
			
			
			/* just to interate the "task" tag element , assumption: there is only one task tag*/
			/* for loop is for future extension*/
			
			
			String weatherFile;
			String id;
			String status;
			Task task=null;	
			String myid= PlatformInformation.getMyMacAddress();
			
			for(int i=0;i<nList.getLength();i++)
			{
				Node nNode = nList.item(i);
				Element eElement = (Element) nNode;
				
				status= eElement.getAttribute("status");
				
				if(status.equals("live"))
				{
				
		
					id=eElement.getAttribute("id");
					weatherFile =eElement.getAttribute("weatherfile");
				 
					/*Explore all node in task root*/
					
					NodeList childNodes = doc.getElementsByTagName("node");
					
					for(int j=0 ;j< childNodes.getLength();j++)
					{				
						Node tnNode = childNodes.item(j);
						Element teElement = (Element) tnNode;
						
						String nodeid=teElement.getAttribute("id");
						
						if(myid.equals(nodeid)) /*Compare current machine id with specified*/
						{	
							/*myid exist, current node has job to execute*/		
							
							if(task==null)
							{
								/*Create New Task*/
								task = new Task();
							}
							
							/*Keep track of all parts of task*/
							
							task.addToTaskList(id,teElement.getAttribute("inputidf"), weatherFile);
							
						}
						
					}
					
				}
				
				
			}
				
			
			return task;
		}
		catch(Exception e)
		{
			Logger.getLogger(TaskAllocationChecker.class.getName()).log(Level.SEVERE, null, e);
		}
		return null;
		
		
	}
	

}
