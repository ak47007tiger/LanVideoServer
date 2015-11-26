package service;

import java.util.HashMap;
import java.util.Map;

import listener.GlobalConfig;

public class ContentTypeProvider {
	Map<String, String> typeKv = new HashMap<String, String>();
	private static ContentTypeProvider provider;
	@SuppressWarnings("unchecked")
	public static ContentTypeProvider getProvider() {
		if(null == provider){
			provider = new ContentTypeProvider();
			provider.typeKv = (Map<String, String>) GlobalConfig.getShare().getConfig(GlobalConfig.key_content_type_map);
		}
		return provider;
	}
	public String get(String suffix){
		return typeKv.get(suffix);
	}
}
