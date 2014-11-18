package upload;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class IDFCopier {
	
	public static void OutputCopier()
	{
		
	}
	
	public static void InputCopier(String IDFSourceFolder,String weatherFile,String destinationFolder)
	{
		File destination = new File(destinationFolder);
		File source;
		
		
		/*Copy all 12 files from input folder to dropbox task_id folder*/

		for(int i=1;i<13;i++)
		{
					source= new File(IDFSourceFolder+"/"+i+".idf");
					
					try
					{
						FileUtils.copyFileToDirectory(source, destination);		
						
						/*delete original idf files*/
						source.delete();
					}
					catch(IOException e)
					{
						e.printStackTrace();
					}
		}
		
		
		/*Cpoy weather file*/
		try
		{
			source = new File(weatherFile);
			
			/*Create copy of weather file and rename it to input.epw*/
			String newFilePath = source.getAbsolutePath().replace(source.getName(), "") + "input.epw";
			File newFile = new File(newFilePath);
			FileUtils.moveFile(source,newFile);
			
			FileUtils.copyFileToDirectory(newFile, destination);		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		File output = new File(destinationFolder+"/output");
		output.mkdir();
		
	}
	
	
	
	public static void main(String[] args)
	{
		InputCopier("E:/","E:/in.epw","C:/Users/ybar/Dropbox/EP/Task56344");
	}

}
