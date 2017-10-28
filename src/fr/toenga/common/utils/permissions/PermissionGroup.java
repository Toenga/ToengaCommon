package fr.toenga.common.utils.permissions;

import java.util.Map;

import com.google.gson.JsonObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class PermissionGroup
{

	private String 						name;
	private GroupType					type;
	private int	  						power;
	private String[]					permissions;
	private Map<String, JsonObject> 	customPermissions;
	private String[]					supergroups;
	
}
