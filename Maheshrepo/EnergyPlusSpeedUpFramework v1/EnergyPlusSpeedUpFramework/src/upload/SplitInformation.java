/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package upload;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.JOptionPane;

/**
 *
 * @author ybar
 */
public class SplitInformation
{
    private String filepath;
    private String folderpath;

    public SplitInformation(String filepath)
    {
        this.filepath=filepath;
        File f=new File(filepath);
        
        this.folderpath=f.getParent();
        f=null;
    }

    private void createPartialInputFiles(int bm,int bd, int em, int ed,int index)
    {
        BufferedReader breader;
        BufferedWriter bwriter;
        String sb= "  RunPeriod,";
        try
        {
            breader = new BufferedReader( new InputStreamReader( new  FileInputStream(filepath)));
            bwriter = new BufferedWriter(new FileWriter(folderpath+"/"+index+".idf"));
            
            String line=null;
            boolean flag=true;
            while(flag)
            {
                line=breader.readLine();
                if(line==null)
                {
                    break;
                }
                bwriter.write(line+"\n");
                //System.out.print(line+"\n");

                if(line.equals(sb))
                {
                    //System.out.println("******************************************");
                    flag=false;
                }
            }

            /*Modifications to Partial Idf*/
            bwriter.write("    ,                        !- Name"+"\r\n");
            if(bm<10)
            {
                bwriter.write("    "+bm+",                       !- Begin Month"+"\r\n");
            }
            else
            {
                bwriter.write("    "+bm+",                      !- Begin Month"+"\r\n");
            }
            if(bd<10)
            {
                bwriter.write("    "+bd+",                       !- Begin Day of Month"+"\r\n");
            }
            else
            {
                bwriter.write("    "+bd+",                      !- Begin Day of Month"+"\r\n");
            }
            if(em<10)
            {
                bwriter.write("    "+em+",                       !- End Month"+"\r\n");
            }
            else
            {
                bwriter.write("    "+em+",                      !- End Month"+"\r\n");
            }
            if(ed<10)
            {
                bwriter.write("    "+ed+",                       !- End Day of Month"+"\r\n");
            }
            else
            {
                bwriter.write("    "+ed+",                      !- End Day of Month"+"\r\n");
            }

            
            /*Copy Rest*/
            for(int i=0;i<9;i++)
            line=breader.readLine();
            
            while(line!=null)
            {
            	bwriter.write(line+"\n");
            	line=breader.readLine();
            }
            
            breader.close();
            bwriter.close();
        }
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, e," File Not Found !",JOptionPane.ERROR_MESSAGE);
        }

    }
    public void split()
    {
        int beginMonth = 0, beginDay = 0, endMonth = 0, endDay = 0;
        int rank;

        	/*
		Generating partial idf files for all computers
		rank is only used as index, does not qualify computers rank
	*/
	for(rank=0;rank<12;rank++)
	{


		switch(rank)
		{
			case 0:	beginDay 	= 1;
				beginMonth 	= 1;
				endDay 		= 4;
				endMonth 	= 2;
				break;

			case 1:	beginDay 	= 22;
				beginMonth 	= 1;
				endDay 		= 4;
				endMonth 	= 3;
				break;

			case 2:	beginDay 	= 19;
				beginMonth 	= 2;
				endDay 		= 1;
				endMonth 	= 4;
				break;

			case 3:	beginDay 	= 19;
				beginMonth 	= 3;
				endDay 		= 6;
				endMonth 	= 5;
				break;

			case 4:	beginDay 	= 23;
				beginMonth 	= 4;
				endDay 		= 3;
				endMonth 	= 6;
				break;

			case 5:	beginDay 	= 21;
				beginMonth 	= 5;
				endDay 		= 1;
				endMonth 	= 7;
				break;

			case 6:	beginDay 	= 18;
				beginMonth 	= 6;
				endDay 		= 5;
				endMonth 	= 8;
				break;

			case 7:	beginDay 	= 23;
				beginMonth 	= 7;
				endDay 		= 2;
				endMonth 	= 9;
				break;

			case 8:	beginDay 	= 20;
				beginMonth 	= 8;
				endDay 		= 30;
				endMonth 	= 9;
				break;

			case 9: beginDay 	= 24;
				beginMonth 	= 9;
				endDay 		= 4;
				endMonth 	= 11;
				break;

			case 10: beginDay 	= 22;
				beginMonth 	= 10;
				endDay 		= 2;
				endMonth 	= 12;
				break;

			case 11: beginDay 	= 19;
				beginMonth 	= 11;
				endDay 		= 31;
				endMonth 	= 12;
				break;
		}

            /* Making Changes to file*/
                createPartialInputFiles(beginMonth, beginDay, endMonth, endDay,rank+1);
        }
    }
    
    	
     
}
