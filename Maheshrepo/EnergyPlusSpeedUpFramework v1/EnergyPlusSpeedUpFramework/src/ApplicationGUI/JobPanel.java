package ApplicationGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import upload.MainUpload;

@SuppressWarnings("serial")
public class JobPanel extends JFrame {

	private JPanel contentPane;
	
	JLabel lblIDFinput;
	
	JLabel lblChooseWeatherFile;
	/**
	 * Launch the application.
	 */
	

	/**
	 * Create the frame.
	 */
	public JobPanel() {
		setResizable(false);
		
		setTitle("New Job Panel");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblChooseIdfInput = new JLabel("Choose IDF input File");
		lblChooseIdfInput.setBounds(22, 20, 385, 14);
		contentPane.add(lblChooseIdfInput);
		
		lblIDFinput = new JLabel("");
		lblIDFinput.setBounds(22, 47, 364, 14);
		contentPane.add(lblIDFinput);
		
		JButton btnBrowseIDF = new JButton("Browse");
		btnBrowseIDF.setBounds(23, 72, 92, 23);
		btnBrowseIDF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try
				{
					JFileChooser filechooser =  new JFileChooser();
					FileTypeFilter filefilter =new FileTypeFilter("idf","Intermediate data file");
					filechooser.setFileFilter(filefilter);
			        filechooser.setMultiSelectionEnabled(false);
			        filechooser.setAcceptAllFileFilterUsed(false);
			        int result = filechooser.showDialog(new JPanel(),"Select your File");
			        
			        if (result == JFileChooser.APPROVE_OPTION)
		            {
			        	lblIDFinput.setText(filechooser.getSelectedFile().getAbsolutePath());
		            }
					
				}
				catch(Exception e1)
				{
					JOptionPane.showMessageDialog(null, "Error Occured While locating idfs","Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		contentPane.add(btnBrowseIDF);
		
		lblChooseWeatherFile = new JLabel("Choose Weather File");
		lblChooseWeatherFile.setBounds(22, 116, 182, 14);
		contentPane.add(lblChooseWeatherFile);
		
		final JLabel lblWeatherFileInput = new JLabel("");
		lblWeatherFileInput.setBounds(22, 135, 385, 14);
		contentPane.add(lblWeatherFileInput);
		
		JButton btnBrowseWeatherFile = new JButton("Browse");
		btnBrowseWeatherFile.setBounds(22, 160, 93, 23);
		btnBrowseWeatherFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try
				{
					JFileChooser filechooser =  new JFileChooser();
					 FileTypeFilter filefilter =new FileTypeFilter("epw","Weather File"); 
					filechooser.setFileFilter(filefilter);
			        filechooser.setMultiSelectionEnabled(false);
			        filechooser.setAcceptAllFileFilterUsed(false);
			        int result = filechooser.showDialog(new JPanel(),"Select your File");
			        
			        if (result == JFileChooser.APPROVE_OPTION)
		            {
			        	lblWeatherFileInput.setText(filechooser.getSelectedFile().getAbsolutePath());
		            }
					
				}
				catch(Exception e1)
				{
					JOptionPane.showMessageDialog(null, "Error Occured While locating weather files","Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		contentPane.add(btnBrowseWeatherFile);
		
		JButton btnSubmitJob = new JButton("Submit");
		btnSubmitJob.setBounds(118, 218, 86, 23);
		btnSubmitJob.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String idfFIle=lblIDFinput.getText();
				String weatherFile=lblWeatherFileInput.getText();
				if(idfFIle==null || idfFIle.length()==0){
					JOptionPane.showMessageDialog(null, "No Idf file is Selected","Error",JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(weatherFile==null || weatherFile.length()==0){
					JOptionPane.showMessageDialog(null, "No weather File is selected","Error",JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				
				
				
				
				MainUpload mi=new MainUpload();
				mi.upload(idfFIle,weatherFile);
				
			}
		});
		contentPane.add(btnSubmitJob);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(230, 218, 92, 23);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				disposeForm();
			}
		});
		contentPane.add(btnCancel);
	}

	
	void disposeForm()
	{
		this.dispose();
	}
}
