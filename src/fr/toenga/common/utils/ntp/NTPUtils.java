package fr.toenga.common.utils.ntp;

import java.net.InetAddress;
import java.util.TimerTask;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import fr.toenga.common.utils.logs.Log;
import fr.toenga.common.utils.logs.LogType;
import fr.toenga.common.utils.threading.TimerUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = false)
@Data
public class NTPUtils extends TimerTask 
{

	@Getter private static NTPUtils instance 		= new NTPUtils("fr.pool.ntp.org");
	private static long 			timeDiff	 	= -1;

	private String		ntpServer;

	public NTPUtils(String ntpServer) 
	{
		this.setNtpServer(ntpServer);
		TimerUtils.getTimer().schedule(this, 0, 30_000L);
	}

	@Override
	public void run() 
	{
		try 
		{
			long start = System.currentTimeMillis();
			NTPUDPClient timeClient = new NTPUDPClient();
			InetAddress inetAddress = InetAddress.getByName(getNtpServer());
			TimeInfo timeInfo = timeClient.getTime(inetAddress);
			long returnTime = timeInfo.getMessage().getTransmitTimeStamp().getTime();
			long end = System.currentTimeMillis();
			long diff = end - start;
			returnTime -= diff;
			timeDiff = end - returnTime;
		}catch(Exception error)
		{
			Log.log(LogType.ERROR, "Error occurred while trying to fetch NTP time.");
			error.printStackTrace();
		}
	}

	public static long getTime() 
	{
		return System.currentTimeMillis() - timeDiff;
	}

}
