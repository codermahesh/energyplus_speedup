import javax.swing.JOptionPane;

import Workernode.TaskMonitor;

import upload.NodeInsert;
import ApplicationEnvironment.PlatformInformation;
import ApplicationEnvironment.WorkingSettings;
import ApplicationGUI.ApplicationSettingsForm;
import ApplicationGUI.Tray;


public class Runner {

	/**
	 * @param args
	 */
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args)
	{
			/*Initialising Application*/
			
			/*Check for single instance*/
			if(!SingleInstanceProvider.lockApplication())
			{
				JOptionPane.showMessageDialog(null,"Application is Already Running !");
				System.exit(0);
			}
			
			/*Read Settings*/
			if(!WorkingSettings.LoadAppProperties())
			{
					ApplicationSettingsForm form =  new ApplicationSettingsForm();
					form.show();
			}
			
			
			
			/*Keep Application live in Tray*/
			Tray tray = new Tray("Resources/iiit.gif");
			
			 
			/*Initialise thread for monitoring and launchinf new jobs*/
			Thread TaskMonitorThread= new Thread( new TaskMonitor(WorkingSettings.dropboxfolder),"TaskMonitorThread");
			TaskMonitorThread.start();
			
			/*check lgos for old jobs*/
			
			
			try
			{
				NodeInsert ni=new NodeInsert(PlatformInformation.getMyMacAddress());
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			tray.RunInTray();

			
	}

}
