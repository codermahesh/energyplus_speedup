package ApplicationEnvironment;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class WorkingSettings 
{

	public static String dropboxfolder=null;
	public static String simulationFile=null;
	public static String startupLauch=null;
	
	static Properties properties;
	
	
	 
	public static boolean LoadAppProperties()
	{
	     properties = new Properties();
		 try
         {
             properties.load(new FileInputStream("config.properties"));

             dropboxfolder=properties.getProperty("dropboxfolder");
             simulationFile=properties.getProperty("simulationFile");
             startupLauch=properties.getProperty("startupLauch" );

             return true;            // success
         }
         catch(IOException e)
         {
        	 return false;			// fail
         }
		 
	}
	
	public static boolean StoreNewProperties()
	{
		 properties = new Properties();
		 try
		 {
			 properties.setProperty("dropboxfolder", dropboxfolder);
			 properties.setProperty("simulationFile", simulationFile);
			 properties.setProperty("startupLauch", startupLauch);
			 
			 properties.store(new FileOutputStream("config.properties"), null);
			 return true;
		 }
		 catch(IOException e)
		 {
			 return false;
		 }
	}
	
}
