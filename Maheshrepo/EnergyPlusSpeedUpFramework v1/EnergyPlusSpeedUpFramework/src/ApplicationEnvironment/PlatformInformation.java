package ApplicationEnvironment;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class PlatformInformation 
{

	public static Platform getPlatformName()
	{
		String OS = System.getProperty("os.name").toLowerCase();
		if(OS.indexOf("win") >= 0)
		{
			return Platform.WINDOWS;
		}
		else if(OS.indexOf("mac") >= 0)
		{
			return Platform.MAC;
		}
		else if(OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 )
		{
			return Platform.LINUX;
		}
		else
		{
			return Platform.UNDEFINED;
		}   

	}

	public static String getMyMacAddress()
	{
		InetAddress ip;
		try {

			ip = InetAddress.getLocalHost();
			
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			byte[] mac = network.getHardwareAddress();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) 
			{
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "" : ""));		
			}
			return sb.toString();

		} catch (UnknownHostException e) 
		{
			e.printStackTrace();
		} catch (SocketException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	public static void main(String[] arg)
	{
		System.out.println(getMyMacAddress());
	}
	*/
}


