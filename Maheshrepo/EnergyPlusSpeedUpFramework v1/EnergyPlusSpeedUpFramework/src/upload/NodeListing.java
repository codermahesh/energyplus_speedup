package upload;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ApplicationEnvironment.WorkingSettings;



public class NodeListing 
{
	private static String getValue(String tag, Element element) 
	{
		NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
		Node node = (Node) nodes.item(0);
		return node.getNodeValue();
	}
	public static ArrayList<MachineNode> getNode(){
		ArrayList<MachineNode> li=new ArrayList<MachineNode>();
		try{
			File folder= new File(WorkingSettings.dropboxfolder+"/Nodes/");
			File xmlFiles[]=folder.listFiles();
			int noOfFiles=xmlFiles.length;
			File xmlFile=null;
			for(int i=0;i<noOfFiles;i++){
				xmlFile=xmlFiles[i];
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();
				doc.getDocumentElement().getNodeName();
				NodeList nList = doc.getElementsByTagName("node");
				Element eElement = (Element)nList.item(0);
				String timestamp=getValue("last-update-time", eElement);
				long updatedTime=Long.parseLong(timestamp);
				long time=(new Date()).getTime();
				if(time-updatedTime < 300000)
				li.add (new MachineNode( getValue("id", eElement),
						getValue("cpu-utilization", eElement),
						getValue("last-update-time", eElement)));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return li;
	}
}
