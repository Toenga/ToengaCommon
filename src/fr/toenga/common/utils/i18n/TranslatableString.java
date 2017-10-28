package fr.toenga.common.utils.i18n;

import lombok.Data;

/**
 * Représente une chaîne de caractère traductible. Utiliser à plusieurs endroit
 * dans l'API pour simplifier.
 * 
 * @author LeLanN
 */
@Data
public class TranslatableString
{
	
	private String 		key;
	private Object[]	objects;

	/**
	 * Crée une nouvelle chaîne traduisible
	 * 
	 * @param key
	 *            La key
	 * @param objects
	 *            Les arguments
	 */
	public TranslatableString(String key, Object... objects)
	{
		this.key = key;
		this.objects = objects;
	}
	
	/**
	 * Récupère le message sur plusieurs lignes
	 * 
	 * @param locale
	 *            La langue
	 * @return Le message
	 */
	public String[] get(Locale locale) {
		return I18n.getInstance().get(locale, key, objects);
	}

	/**
	 * Récupère la première ligne du message
	 * 
	 * @param locale
	 *            La langue
	 * @return La ligne
	 */
	public String getAsLine(Locale locale)
	{
		return I18n.getInstance().get(locale, key, objects)[0];
	}

}
