package fr.toenga.common.utils.i18n;

import java.util.Locale;

public class LocaleUtils
{
	
	public static final Locale defaultLocale = Locale.FRANCE;

	public static Locale getLocaleByFullName(String name)
	{
		for (Locale locale : Locale.getAvailableLocales())
		{
			String n = locale.getLanguage() + "-" + locale.getCountry();
			if (name.equalsIgnoreCase(n))
			{
				return locale;
			}
		}
		return null;
	}
	
	public static Locale getLocaleByName(String name)
	{
		for (Locale locale : Locale.getAvailableLocales())
		{
			if (name.equalsIgnoreCase(locale.getLanguage()))
			{
				return locale;
			}
		}
		return null;
	}
	
}
