package fr.toenga.common.utils.i18n;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Représente un mot traductible. Utiliser à plusieurs endroit dans l'API pour
 * simplifier, ou pour intégrer un mot traduit en<br>
 * paramètre d'un message (ex : block ou entité).
 * 
 * @author LeLanN
 */
@Data
@AllArgsConstructor
public class TranslatableWord
{
	
	private String 			key;
	private boolean 		plural;
	private WordDeterminant determinant;

	/**
	 * Récupère le mot traduit dans une langue
	 * 
	 * @param locale
	 *            La langue
	 * @return Le mot
	 */
	public String getWord(Locale locale)
	{
		return I18n.getInstance().getWord(locale, key, plural, determinant);
	}
	
}
