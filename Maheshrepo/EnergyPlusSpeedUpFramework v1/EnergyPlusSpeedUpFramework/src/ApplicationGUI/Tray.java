package ApplicationGUI;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Tray
{
	String trayImage;

	public Tray(String trayImage)
	{
		this.trayImage = trayImage;

	}

	public	void RunInTray()
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run() 
			{
				createAndShowGUI();
			}
		});
	}
	
	
	void createAndShowGUI() 
	{
		//Check the SystemTray support
		if (!SystemTray.isSupported()) 
		{
			JOptionPane.showMessageDialog(null,"SystemTray is not supported");
			//System.out.println("SystemTray is not supported");
			return;
		}
		
		
		final PopupMenu popup = new PopupMenu();
		final SystemTray tray = SystemTray.getSystemTray();
		
		final TrayIcon trayIcon  = new TrayIcon( createIcon(trayImage,"Energy Plus Simulation Worker Node !")); 
		
		MenuItem jobMenu = new MenuItem("Submit Job");
		MenuItem resultMenu = new MenuItem("Results");
		MenuItem displayMenu = new MenuItem("Settings");
		MenuItem exitItem = new MenuItem("Exit");

		popup.add(jobMenu);
		popup.add(resultMenu);
		popup.add(displayMenu);
		popup.add(exitItem);
		trayIcon.setPopupMenu(popup);

		try 
		{
			tray.add(trayIcon);
		} 
		catch (AWTException e)
		{
			System.out.println("TrayIcon could not be added.");
			return;
		}

		trayIcon.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				JOptionPane.showMessageDialog(null,"This dialog box is run from System Tray");

			}
		}) ;
		
		jobMenu.addActionListener( new ActionListener()
		{

			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JobPanel jobPanel = new JobPanel();
				jobPanel.show();				
			}
		});

		resultMenu.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
		 				
			}
		});
		
		displayMenu.addActionListener( new ActionListener()
		{

			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ApplicationSettingsForm form = new ApplicationSettingsForm(1);
				form.show();
			}
		});

		exitItem.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				// TODO Auto-generated method stub
				tray.remove(trayIcon);
				System.exit(0);
			}
		});


	}

	private Image createIcon(String path,String describe)
	{
		return (new ImageIcon(path, describe)).getImage();

	}
}
