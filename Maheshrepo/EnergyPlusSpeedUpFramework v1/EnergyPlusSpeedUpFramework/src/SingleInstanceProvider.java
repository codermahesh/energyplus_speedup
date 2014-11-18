import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;


public class SingleInstanceProvider {
	
	
	private static RandomAccessFile randomAccessFile;

	public static boolean lockApplication()
	{
		try
		{
			randomAccessFile = new RandomAccessFile("Runner.class","rw");
			FileChannel channel = randomAccessFile.getChannel();
			
			FileLock lock = channel.tryLock();
			
			if(lock == null)
			{
				return false;
			}
			else
				return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

}
