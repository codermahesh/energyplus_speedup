package upload;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import TaskMaintainer.OutputNotifier;
import TaskMaintainer.OutputNotifierThread;

import ApplicationEnvironment.WorkingSettings;

public class MainUpload 
{
	static Random randomGenerator=null;
	
	public MainUpload()
	{
		randomGenerator=new Random();
	}
	
	
	public  void upload(String idf,String wthr)
	{

		try
		{

			int k=randomGenerator.nextInt(999999);

			File dir = new File(WorkingSettings.dropboxfolder+"/Task"+k);
			dir.mkdir();

			/*Get all available nodes*/
			ArrayList<MachineNode> li=NodeListing.getNode();
			/*Sort them by their rank*/
			Collections.sort(li,new MachineNodeComparator());
						
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			
			Element rootElement = doc.createElement("tasks");
			doc.appendChild(rootElement);
			
			Element tasknode = doc.createElement("task");
			rootElement.appendChild(tasknode);
			
			Attr attr = doc.createAttribute("id");
			attr.setValue("task"+k);
			tasknode.setAttributeNodeNS(attr);
			
			attr = doc.createAttribute("status");
			attr.setValue("live");
			tasknode.setAttributeNodeNS(attr);

			attr = doc.createAttribute("weatherfile");			
			attr.setValue("input.epw");
			tasknode.setAttributeNodeNS(attr);

			//Element node= doc.createElement("node");
			
			
			MachineNode mn=null;
			//Element idChild = doc.createElement("id");
			//idChild.appendChild(doc.createTextNode("task"+k));
			//tasknode.appendChild(idChild);
			
			int len=li.size();
			int count=12;
			Element node;
			
			while(count>0)  // distribute 12 files among machines
			{
					 for(MachineNode mnode : li)
					 {
						 //<node inputidf="1.idf" id="001EEC82B3BE"/>
						 
						 node= doc.createElement("node");
						 attr = doc.createAttribute("id");
						 attr.setValue(mnode.getId());
						 node.setAttributeNodeNS(attr);
						 
						 attr = doc.createAttribute("inputidf");
						 attr.setValue(count+".idf");
						 node.setAttributeNodeNS(attr);
						 
						 tasknode.appendChild(node);
						 count--;
						 if(count<=0)
							 break;
					 }
			}
			

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			
			
			String taskString=WorkingSettings.dropboxfolder+"/Task/";
			
			StreamResult result = new StreamResult(new File(taskString+"Task"+k+".xml"));
			transformer.transform(source, result);


			SplitInformation si=new SplitInformation(idf);
			si.split();

			File f=new File(idf);
			String idfFolder=f.getParent();

			String taskfolder= WorkingSettings.dropboxfolder+"/Task"+k+"/";
			/*Move new idf to task folder*/
			IDFCopier.InputCopier(idfFolder, wthr, taskfolder);


			//check For the Output
			
			Thread outputNotifierThread  =  new Thread(new OutputNotifierThread(taskfolder+"/output",false));
			outputNotifierThread.start();
			

		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
