package ApplicationGUI;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import upload.NodeInsert;

import ApplicationEnvironment.Platform;
import ApplicationEnvironment.PlatformInformation;
import ApplicationEnvironment.WorkingSettings;

public class ApplicationSettingsForm extends JFrame 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel textDropboxFolder;
	private JLabel textSimulationFile;
	private JCheckBox chckbxStartupLaunch;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ApplicationSettingsForm frame = new ApplicationSettingsForm();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	} 

	/**
	 * Create the frame.
	 */
	public ApplicationSettingsForm(int read)
	{
		initForm();
		WorkingSettings.LoadAppProperties();
		textDropboxFolder.setText(WorkingSettings.dropboxfolder);
		textSimulationFile.setText(WorkingSettings.simulationFile);
		if(WorkingSettings.startupLauch.equals("yes"))
		{
			chckbxStartupLaunch.setSelected(true);
		}
		else
		{
			chckbxStartupLaunch.setSelected(false);
		}	
	}
	
	public ApplicationSettingsForm()
	{
		setResizable(false);
		initForm();
	}
	
	public void initForm() 
	{
		setIconImage(Toolkit.getDefaultToolkit().getImage(ApplicationSettingsForm.class.getResource("/com/sun/java/swing/plaf/windows/icons/JavaCup32.png")));
		setTitle("EnergyPlus SpeedUp Settings");
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblChooseSimulationFile = new JLabel("Choose Simulation File");
		lblChooseSimulationFile.setBounds(35, 44, 255, 14);
		contentPane.add(lblChooseSimulationFile);
		
		textSimulationFile = new JLabel();
		textSimulationFile.setBounds(35, 63, 373, 14);
		textSimulationFile.setHorizontalAlignment(SwingConstants.LEFT);
		textSimulationFile.setText("Choose");
		contentPane.add(textSimulationFile);
		
		JButton btnBrowseSimulationFile = new JButton("Browse");
		btnBrowseSimulationFile.setBounds(35, 82, 93, 23);
		
		btnBrowseSimulationFile.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
			
		        JFileChooser filechoice  = new JFileChooser();
		        FileNameExtensionFilter filter=null;
		        Platform platform = PlatformInformation.getPlatformName();
		        switch(platform)
		        {
		            case WINDOWS:
		                 filter = new FileNameExtensionFilter("Executable", "exe");
		               break; 
		            case LINUX:
		                filter = new FileNameExtensionFilter("Executable", "o");
		                break;
		            case MAC:
		                filter = new FileNameExtensionFilter("Executable", "o");
		                break;
					case UNDEFINED :
						break;
					default :
						break;
		                
		        }
		        
		       
		        filechoice.removeChoosableFileFilter(filechoice.getFileFilter());
		        filechoice.setMultiSelectionEnabled(false);
		        filechoice.setAcceptAllFileFilterUsed(false);
		        filechoice.addChoosableFileFilter(filter);
		        filechoice.setFileFilter(filter);
		        
		        int ret = filechoice.showOpenDialog(null);
		        
		        if(ret == JFileChooser.APPROVE_OPTION)
		        {
		         	
		          textSimulationFile.setText(filechoice.getSelectedFile().getAbsolutePath());
		         
		        }
		        else
		        {
		              if(textSimulationFile.getText().equals("Choose"))
		              {
		                  JOptionPane.showMessageDialog(null,"You Have To select Simulation File!");
		              }
		        	
		        }
		        
		        
			}
			
		});
		contentPane.add(btnBrowseSimulationFile);
		
		JLabel lblChooseDropboxFolder = new JLabel("Choose Dropbox Folder");
		lblChooseDropboxFolder.setBounds(35, 110, 224, 14);
		contentPane.add(lblChooseDropboxFolder);
		
		textDropboxFolder = new JLabel();
		textDropboxFolder.setBounds(35, 129, 373, 14);
		textDropboxFolder.setText("Choose");
		contentPane.add(textDropboxFolder);
		
		JButton btnDropboxFolder = new JButton("Browse");
		btnDropboxFolder.setBounds(35, 148, 93, 23);
		
		btnDropboxFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
		        JFileChooser chooser = new JFileChooser();
		        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		        chooser.setAcceptAllFileFilterUsed(false);
		        int ret= chooser.showOpenDialog(null);
		        
		        if(ret == JFileChooser.APPROVE_OPTION)
		        {
		        	textDropboxFolder.setText(chooser.getSelectedFile().getAbsolutePath());
		        }
		        else
		        {
		        	if(textSimulationFile.getText().equals("Choose"))
		              {
		        			JOptionPane.showMessageDialog(null,"You have to Select Dropbox Folder");
		              }		
		        }
				
			}
		});
		contentPane.add(btnDropboxFolder);
		
		chckbxStartupLaunch = new JCheckBox("StartUp Lauch");
		chckbxStartupLaunch.setBounds(35, 176, 126, 23);
		contentPane.add(chckbxStartupLaunch);
		
		JButton btnSaveSettings = new JButton("Save Settings");
		btnSaveSettings.setBounds(270, 228, 138, 23);
		btnSaveSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(textSimulationFile.getText().equals("Choose") || textDropboxFolder.getText().equals("Choose"))
				{					
						JOptionPane.showMessageDialog(null,"No Empty Fileds are allowed!");
						return;
				}
				
				WorkingSettings.simulationFile = textSimulationFile.getText();
				WorkingSettings.dropboxfolder= textDropboxFolder.getText();
				if(chckbxStartupLaunch.isSelected())
				{
					WorkingSettings.startupLauch="yes" ;
		
				}
				else
				{
					WorkingSettings.startupLauch="no" ;
				}
				
				WorkingSettings.StoreNewProperties();
				
				disposeForm();
			
			}
		});
		NodeInsert ni=new NodeInsert("Abhinay");
		contentPane.add(btnSaveSettings);
	}

	
	/*Terminates the form working */
	void disposeForm()
	{
		this.dispose();
	}
}
