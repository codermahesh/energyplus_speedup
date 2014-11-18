package upload;

import java.util.Date;

/**/
public class MachineNode
{
	String id;
	int cpu_utilization;
	Date timestamp;
	int priority=0;
	
	String timeStamp="";

	public MachineNode(String id , String cpu_utilization, String time)
	{
		try{
			this.id =id;
			this.cpu_utilization =	Integer.parseInt(cpu_utilization);
			timeStamp =time;
			/*DateFormat dateformat = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy",Locale.ENGLISH);
			timestamp =dateformat.parse(time);*/
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public int getCpu_utilization()
	{
		return cpu_utilization;
	}

	public void setCpu_utilization(int cpu_utilization)
	{
		this.cpu_utilization = cpu_utilization;
	}

	public Date getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(Date timestamp)
	{
		this.timestamp = timestamp;
	}

	@Override
	public String toString()
	{
		return id+cpu_utilization+timestamp;
	}
	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
}


