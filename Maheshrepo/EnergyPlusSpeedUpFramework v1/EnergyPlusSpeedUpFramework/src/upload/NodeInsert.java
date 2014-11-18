package upload;

import java.io.File;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import SystemMoniter.MachineRanking;

import ApplicationEnvironment.WorkingSettings;

public class NodeInsert implements Runnable {
	Thread t=null;
	String id="";
	public NodeInsert(String id){

		this.id=id;
		t=new Thread(this);
		t.start();
	}
	
	@Override
	public void run() {
		try{
			
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("nodes");
			doc.appendChild(rootElement);
			Element node = doc.createElement("node");
			rootElement.appendChild(node);
			
			Element idChild = doc.createElement("id");
			idChild.appendChild(doc.createTextNode(id));
			node.appendChild(idChild);
			
			Element cpuChild = doc.createElement("cpu-utilization");
			
			/* GET RANK*/
			String rank = Integer.toString(MachineRanking.getRank());
			
			cpuChild.appendChild(doc.createTextNode(rank));
			node.appendChild(cpuChild);
			Element timeChild = doc.createElement("last-update-time");
			timeChild.appendChild(doc.createTextNode(""+(new Date()).getTime()));
			node.appendChild(timeChild);
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(WorkingSettings.dropboxfolder+"/Nodes/"+id+".xml"));
	 
			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);
	 
			transformer.transform(source, result);
			
			File f=new File(WorkingSettings.dropboxfolder+"/Nodes/"+id+".xml");
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document document = dBuilder.parse(f);
			document.getDocumentElement().normalize();	
			Element element=null ;
			
			/*
			element =document.createElement("node");

			
			Element ide = document.createElement("id");
			Element cpu=document.createElement("cpu-utilization");
			Element update= document.createElement("last-update-time");


			ide.setTextContent(id);
			cpu.setTextContent("50");
			update.setTextContent(value);

			element.appendChild(ide);
			element.appendChild(cpu);
			element.appendChild(update);

			Element live = (Element)document.getElementsByTagName("alivenodes").item(0);
			live.appendChild(element);	

			System.out.println("update " +value );
			*/
			NodeList nodes = document.getElementsByTagName("node");
			while(true){
				
				Node node2 = nodes.item(0);
				if (node2.getNodeType() == Node.ELEMENT_NODE) 
				{
					element = (Element) node2;	

					NodeList nodes1 = element.getElementsByTagName("last-update-time").item(0).getChildNodes();
					Node node1 = (Node) nodes1.item(0);
					node1.setNodeValue(""+(new Date()).getTime());



				}
				Thread.sleep(300000);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
