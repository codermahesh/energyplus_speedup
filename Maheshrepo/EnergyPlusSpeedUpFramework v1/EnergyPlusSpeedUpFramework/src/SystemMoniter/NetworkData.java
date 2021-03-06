package SystemMoniter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hyperic.sigar.NetFlags;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

public class NetworkData {

	static Map<String, Long> rxCurrentMap = new HashMap<String, Long>();
	static Map<String, List<Long>> rxChangeMap = new HashMap<String, List<Long>>();
	static Map<String, Long> txCurrentMap = new HashMap<String, Long>();
	static Map<String, List<Long>> txChangeMap = new HashMap<String, List<Long>>();
	private static Sigar sigar;

	private final static int SAMPLE_LIMIT=10;
	
	/**
	 * @throws InterruptedException
	 * @throws SigarException
	 * 
	 */
	public NetworkData(Sigar s) throws SigarException, InterruptedException {
		sigar = s;
		getMetric();
		//System.out.println(networkInfo());
		Thread.sleep(1000);     
	}

	public static double getPercentageUtilzation() throws SigarException,
	InterruptedException 
	{
		new NetworkData(new Sigar());
		return NetworkData.startMetricTest();
		
	}

	public static String networkInfo() throws SigarException {
		//String info = sigar.getNetInfo().toString();
		String info = "\n"+ sigar.getNetInterfaceConfig().toString();
		return info;
	}

	public static String getDefaultGateway() throws SigarException {
		return sigar.getNetInfo().getDefaultGateway();
	}

	public static double startMetricTest() throws SigarException, InterruptedException 
	{
		int count=0;
		long totalrx=0,totaltx=0;
		while (count<SAMPLE_LIMIT) 
		{
			Long[] m = getMetric();
			totalrx += m[0];
			totaltx += m[1];
			//System.out.print("totalrx(download): ");
			//System.out.println("\t" + Sigar.formatSize(totalrx));
			//System.out.print("totaltx(upload): ");
			//System.out.println("\t" + Sigar.formatSize(totaltx));
			 
			Thread.sleep(1000);
			count++;
		}
		double throughtput =getThroughput(totalrx,totaltx);	
		//System.out.println();
		/* Avoiding float calculations*/
		/* Calculates nearly same result divide by 1>>>>> thing*/
		try
		{
			return throughtput;
		}
		catch(Exception e)
		{
			return 0;
		}
	}

 
	private static double getThroughput(final long totalrx,final long totaltx)
	{
		try 
		{
			NetInterfaceStat stat =sigar.getNetInterfaceStat(sigar.getNetInterfaceConfig().getName());
			//System.out.println("" +(totalrx+totaltx) + "Sppeding" +stat.getSpeed());
			double throughput =((totalrx+totaltx)/ stat.getSpeed()) *100;
			
			if((totalrx+totaltx) > 0  && throughput == 0.0)
			{
				return 1.0;
			}
			else	
				return throughput;
		} catch (SigarException e) 
		{
			e.printStackTrace();
			return 0;
		}
		  
	}
	public static Long[] getMetric() throws SigarException 

	{
		for (String ni : sigar.getNetInterfaceList()) {
			// System.out.println(ni);
			NetInterfaceStat netStat = sigar.getNetInterfaceStat(ni);
			NetInterfaceConfig ifConfig = sigar.getNetInterfaceConfig(ni);
			String hwaddr = null;
			if (!NetFlags.NULL_HWADDR.equals(ifConfig.getHwaddr())) {
				hwaddr = ifConfig.getHwaddr();
			}
			if (hwaddr != null) 
			{
				long rxCurrenttmp = netStat.getRxBytes();
				saveChange(rxCurrentMap, rxChangeMap, hwaddr, rxCurrenttmp, ni);
				long txCurrenttmp = netStat.getTxBytes();
				saveChange(txCurrentMap, txChangeMap, hwaddr, txCurrenttmp, ni);
			}
		}
		long totalrxDown = getMetricData(rxChangeMap);
		long totaltxUp = getMetricData(txChangeMap);
		for (List<Long> l : rxChangeMap.values())
			l.clear();
		for (List<Long> l : txChangeMap.values())
			l.clear();
		return new Long[] { totalrxDown, totaltxUp };
	}

	private static long getMetricData(Map<String, List<Long>> rxChangeMap) {
		long total = 0;
		for (Entry<String, List<Long>> entry : rxChangeMap.entrySet()) {
			int average = 0;
			for (Long l : entry.getValue()) {
				average += l;
			}
			total += average / entry.getValue().size();
		}
		return total;
	}

	private static void saveChange(Map<String, Long> currentMap,
			Map<String, List<Long>> changeMap, String hwaddr, long current,
			String ni) {
		Long oldCurrent = currentMap.get(ni);
		if (oldCurrent != null) {
			List<Long> list = changeMap.get(hwaddr);
			if (list == null) {
				list = new LinkedList<Long>();
				changeMap.put(hwaddr, list);
			}
			list.add((current - oldCurrent));
		}
		currentMap.put(ni, current);
	}
}