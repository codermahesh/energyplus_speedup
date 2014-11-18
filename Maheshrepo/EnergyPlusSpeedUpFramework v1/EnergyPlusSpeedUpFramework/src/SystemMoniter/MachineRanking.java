package SystemMoniter;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;


/* This class calculates amount of free network,memory and computation 
 * and Makes grading base on same parameters
 * for this protype only dymanic factors are considered , uppper limits 
 * on this propperty is considerd in calucation of percentage usage 
 * in SIGAR and NetworkData.
 * 
 * @author  : ybar 
 * 
 * @against : This class puts calling thread sleep for while to collect system
 * data only way to gather distribution of packets over half minute.
 * */

public class MachineRanking 
{
	final static int SystemWeightage=40;
	final static int MemoryWeightage=30;
	final static int NetworkWeightage=30;
	
	int rank;
	
	public static int  getRank()
	{
		double networkfree =0;
		double systemfree=0;
		double memoryfree=0;
		try
		{
			Sigar sigar = new Sigar();
			CpuPerc perc = sigar.getCpuPerc();
			Mem mem = sigar.getMem();

			systemfree=perc.getIdle()*100;
			memoryfree =mem.getFreePercent();
			networkfree=100-NetworkData.getPercentageUtilzation();

			double rank=(systemfree * SystemWeightage) +(memoryfree * MemoryWeightage )+(networkfree * NetworkWeightage) ;
			rank =rank/100;
			
			return (int) Math.ceil(rank);
			
		}
		 catch (SigarException e)
			{
				e.printStackTrace();
			}
			 catch (InterruptedException e) 
			{
					e.printStackTrace();
			}
		return 0;
	}
	
 
/*
	public static void main(String[] args)
	{
		double networkfree =0;
		double systemfree=0;
		double memoryfree=0;
		
	
		Sigar sigar = new Sigar();
		try 
		{
			 
			
			CpuPerc perc = sigar.getCpuPerc();
			Mem mem = sigar.getMem();
			
			systemfree=perc.getIdle()*100;
			memoryfree =mem.getFreePercent();
			networkfree=100-NetworkData.getPercentageUtilzation();
			
			System.out.println("system idle: "+systemfree);
			System.out.println("Memory Free " +memoryfree);
			System.out.println("Network Use: " +networkfree);
			 
			double rank=(systemfree * SystemWeightage) +(memoryfree * MemoryWeightage )+(networkfree * NetworkWeightage) ;
			rank =rank/100;
			System.out.println(Math.ceil(rank));
			
		} catch (SigarException e)
		{
			e.printStackTrace();
		}
		 catch (InterruptedException e) 
		{
				e.printStackTrace();
		}
		
	}
	
*/	
}

