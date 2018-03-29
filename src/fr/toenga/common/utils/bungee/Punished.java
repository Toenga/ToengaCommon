package fr.toenga.common.utils.bungee;

import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import fr.toenga.common.utils.i18n.I18n;
import fr.toenga.common.utils.i18n.Locale;
import fr.toenga.common.utils.time.Time;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class Punished
{
	
	private boolean ban,
					mute;
	
	private long	banEnd,
					muteEnd;
	
	private String	banReason,
					muteReason;
	
	private String	banner,
					muter;

	private int		banId;
	private int		muteId;
	
	public Punished()
	{
		ban 	   = false;
		mute 	   = false;
		banEnd     = -1;
		muteEnd    = -1;
		banReason  = null;
		muteReason = null;
		banner     = null;
		muter	   = null;
	}
	
	public Punished(JsonObject jsonObject)
	{
		ban = jsonObject.get("ban").getAsBoolean();
		mute = jsonObject.get("mute").getAsBoolean();
		banEnd = jsonObject.get("banEnd").getAsLong();
		muteEnd = jsonObject.get("muteEnd").getAsLong();
		banReason = jsonObject.get("banReason").getAsString();
		muteReason = jsonObject.get("muteReason").getAsString();
		banner = jsonObject.get("banner").getAsString();
		muter = jsonObject.get("muter").getAsString();
		banId = jsonObject.get("banId").getAsInt();
		muteId = jsonObject.get("muteId").getAsInt();
	}
	
	public DBObject getDBObject()
	{
		BasicDBObject query = new BasicDBObject();
		query.put("ban", ban);
		query.put("mute", mute);
		query.put("banEnd", banEnd);
		query.put("muteEnd", muteEnd);
		query.put("banReason", banReason);
		query.put("muteReason", muteReason);
		query.put("banner", banner);
		query.put("muter", muter);
		query.put("banId", banId);
		query.put("muteId", muteId);
		return query;
	}

	public void checkEnd()
	{
		if (ban && banEnd != -1 && banEnd < System.currentTimeMillis())
		{
			ban 	  = false;
			banEnd 	  = -1;
			banReason = null;
			banner 	  = null;
		}
		
		if (mute && muteEnd != -1 && muteEnd < System.currentTimeMillis())
		{
			mute 	   = false;
			muteEnd    = -1;
			muteReason = null;
			muter 	   = null;
		}
	}

	public String buildBanTime(Locale locale)
	{
		if(banEnd != -1){
			return Time.MILLIS_SECOND.toFrench(banEnd - System.currentTimeMillis(), Time.MINUTE, Time.YEAR);
		} else return I18n.getInstance().get(locale, "punishments.forever")[0];
	}
	
	public String buildMuteTime(Locale locale)
	{
		if(muteEnd != -1){
			return Time.MILLIS_SECOND.toFrench(muteEnd - System.currentTimeMillis(), Time.MINUTE, Time.YEAR);
		} else return I18n.getInstance().get(locale, "punishments.forever")[0];
	}
	
	public String[] buildMuteReason(Locale locale)
	{
		String time = "";
		if (muteEnd != -1)
		{
			time = I18n.getInstance().get(locale, "punishments.for")[0] + Time.MILLIS_SECOND.toFrench(muteEnd - System.currentTimeMillis(), Time.MINUTE, Time.YEAR);
		}
		return I18n.getInstance().get(locale, "punishments.youvebeenmute", muteReason, time);
	}
	
}
