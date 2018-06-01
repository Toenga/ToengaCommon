package fr.toenga.common.tech.sql.thread;

import fr.toenga.common.tech.TechThread;
import fr.toenga.common.tech.sql.SQLService;
import fr.toenga.common.tech.sql.packet.SQLPacket;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
public class SQLPacketThread extends TechThread<SQLPacket> 
{

	private SQLService sqlService;

	public SQLPacketThread(SQLService sqlService, int id)
	{
		super("SQLPacketThread", sqlService.getPacketQueue(), id);
		setSqlService(sqlService);
	}

	@Override
	public void work(SQLPacket sqlPacket) throws Exception
	{
		getSqlService().handle(sqlPacket);
	}

	@Override
	public String getErrorMessage()
	{
		return "[SQLConnector] An error occurred while trying to send packet.";
	}

	@Override
	public boolean isServiceAlive()
	{
		return getSqlService().isAlive();
	}

}
