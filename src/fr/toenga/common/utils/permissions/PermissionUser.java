package fr.toenga.common.utils.permissions;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import fr.toenga.common.utils.general.GsonUtils;
import fr.toenga.common.utils.permissions.Permission.PermissionResult;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PermissionUser
{

	@SuppressWarnings("serial")
	Type groupType = new TypeToken<Map<String, Map<String, Long>>>() {}.getType();
	@SuppressWarnings("serial")
	Type permissionType = new TypeToken<List<Permission>>() {}.getType();

	private Map<String, Map<String, Long>>	groups;
	private List<Permission>				permissions;

	public PermissionUser(Map<String, Map<String, Long>> groups, List<Permission> permissions)
	{
		this.groups = groups;
		this.permissions = permissions;
	}
	
	public PermissionUser(JsonObject jsonObject)
	{
		groups = GsonUtils.getPrettyGson().fromJson(jsonObject.get("groups"), groupType);
		permissions = GsonUtils.getPrettyGson().fromJson(jsonObject.get("permissions"), permissionType);
	}
	
	public boolean hasPermission(String place, String permission)
	{
		Map<String, Long> g = groups.get(place);
		if (g == null)
		{
			return false;
		}
		long time = System.currentTimeMillis();
		PermissionResult permissionResult = null;
		for (Entry<String, Long> entry : g.entrySet())
		{
			if (entry.getValue() <= time)
			{
				continue;
			}
			Permissible permissible = PermissionsManager.getManager().getGroup(entry.getKey());
			if (permissible == null)
			{
				continue;
			}
			
			permissionResult = permissible.testPermission(new Permission(permission));
			if (permissionResult.equals(PermissionResult.YES))
			{
				return true;
			}
		}
		return false;
	}
	
	public DBObject getDBObject()
	{
		BasicDBObject dbObject = new BasicDBObject();
		dbObject.put("groups", groups);
		dbObject.put("permissions", groups);
		return dbObject;
	}

}
