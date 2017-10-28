package fr.toenga.common.utils.permissions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.BSONObject;

import i18n.I18n;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class PermissionUser
{

	private Map<String, List<String>>	groups;
	private String[]					permissions;

	public boolean hasPermission(String key)
	{
		System.out.println(permissions.length);
		List<String> perms = new ArrayList<>();
		if (permissions != null)
		{
			perms.addAll(Arrays.asList(permissions));
		}
		List<String> gS = groups.get("default");
		for (PermissionGroup pGroup : PermissionManager.getGroups().get("default"))
		{
			if (gS.contains(pGroup.getName()))
			{	
				if (pGroup.getPermissions() != null)
				{
					perms.addAll(Arrays.asList(pGroup.getPermissions()));
				}
			}
		}
		if (perms.contains("-" + key))
		{
			return false;
		}
		if (perms.contains("*"))
		{
			return true;
		}
		return perms.contains(key);
	}

	public static PermissionUser toObject(BSONObject dbObject) {
		if (dbObject == null || !dbObject.containsField("permissions"))
		{
			Map<String, List<String>> g = new HashMap<>();
			g.put("default", Arrays.asList("default"));
			return new PermissionUser(g, new String[] {});
		}
		return I18n.getGson().fromJson(dbObject.get("permissions").toString(), PermissionUser.class);
	}

}
