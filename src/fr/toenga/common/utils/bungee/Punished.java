package fr.toenga.common.utils.bungee;

import com.google.gson.JsonObject;

import fr.toenga.common.utils.data.ExplicitObject;
import fr.toenga.common.utils.general.GsonUtils;
import fr.toenga.common.utils.i18n.I18n;
import fr.toenga.common.utils.i18n.Locale;
import fr.toenga.common.utils.time.Time;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class Punished extends ExplicitObject
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
		super(null);
		ban 	   = false;
		mute 	   = false;
		banEnd     = -1;
		muteEnd    = -1;
		banReason  = null;
		muteReason = null;
		banner     = null;
		muter	   = null;
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
	
	public static Punished fromJson(JsonObject punished)
	{
		return GsonUtils.getGson().fromJson(punished, Punished.class);
	}
	
}
