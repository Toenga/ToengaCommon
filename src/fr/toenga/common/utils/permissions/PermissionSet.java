package fr.toenga.common.utils.permissions;

import java.util.List;
import java.util.Map;

import com.google.gson.JsonElement;

import fr.toenga.common.utils.permissions.Permission.PermissionResult;
import lombok.AllArgsConstructor;

/**
 * Represent a set of permission, power and value. A set is linked to a place list. Places can be minigames, faction, ...
 * @author LeLanN
 */
@AllArgsConstructor
public class PermissionSet
{
	private List<String> places;
	private List<Permission> permissions;
	private Map<String, Integer> powers;
	private Map<String, JsonElement> values;

	public int getPower(String label)
	{
		return powers.containsKey(label) ? (int) powers.get(label) : 0;
	}

	public JsonElement getValue(String label)
	{
		return values.get(label);
	}
	
	public PermissionResult hasPermission(Permission permission)
	{
		PermissionResult result = null;

		for (Permission perm : this.permissions)
		{
			result = perm.compare(permission);

			if (result != PermissionResult.UNKNOWN)
			{
				return result;
			}
		}
		
		return PermissionResult.UNKNOWN;
	}
	
	public boolean isCompatible(String place)
	{
		return places.contains(place) || places.contains("*");
	}
	
	public boolean isCompatible()
	{
		return isCompatible( PermissionsManager.getManager().getCurrentPlace() );
	}
}
