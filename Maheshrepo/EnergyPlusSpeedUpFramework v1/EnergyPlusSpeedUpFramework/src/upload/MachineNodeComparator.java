package upload;

import java.util.Comparator;

public class MachineNodeComparator implements Comparator<MachineNode> {

	@Override
	public int compare(MachineNode arg0, MachineNode arg1)
	{
		return arg1.cpu_utilization - arg0.cpu_utilization; 
	}

}
