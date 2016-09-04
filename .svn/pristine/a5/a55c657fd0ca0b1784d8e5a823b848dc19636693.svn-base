/*package com.anyway.imagemark.security;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import com.anyway.imagemark.utils.ParseProperties;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;

public class MyInvocationSecurityMetadataSourceService implements
		FilterInvocationSecurityMetadataSource {
	Logger logger = Logger.getLogger(MyInvocationSecurityMetadataSourceService.class);
	private ParseProperties parseProperties=new ParseProperties();
	//private UrlMatcher urlMatcher = new AntUrlPathMatcher();  
	private static Map<String, Collection<ConfigAttribute>> resourceMap = null;  

	public MyInvocationSecurityMetadataSourceService() throws UnknownHostException {  
		loadResourceDefine();  
	}  
	
	private void loadResourceDefine() throws UnknownHostException {  
		        // TODO Auto-generated method stub  

		//原始morpiha查询数据  
		
		String mongip = parseProperties.getRelativePropertyValues("database.properties", "mongo.db.host");  
		String mymongip = mongip.split(":")[0];  
		int port =Integer.parseInt(mongip.split(":")[1]);  

		String dbname = parseProperties.getRelativePropertyValues("database.properties", "mongo.db.databaseName");  
		Mongo mongo = new Mongo(mymongip, port);  
		String userName = parseProperties.getRelativePropertyValues("database.properties", "mongo.db.databaseUserName");  
		char[] password = parseProperties.getRelativePropertyValues("database.properties", "mongo.db.databasePassword").toCharArray();  
		Morphia morphia = new Morphia();  
		Datastore ds = morphia.createDatastore(mongo, dbname,userName,password);  
		//      Datastore ds = morphia.createDatastore(mongo, dbname);  

		List<String> query = new ArrayList<String>(); 
		
		//      morphia查询  
		List<Authorities> authoritiesList= ds.find(Authorities.class).asList();  
		for (Authorities authorities : authoritiesList) {  
			logger.info("权限名字 有  Authority_name："+authorities.getAuthority_name());  
			query.add(authorities.getAuthority_name());  
		}  

		resourceMap = new LinkedHashMap<String, Collection<ConfigAttribute>>();  

		for (String auth : query) {  
			ConfigAttribute ca = new SecurityConfig(auth);  
			//          获取该权限对应的资源的所有链接  
			//          get authority_id from authorities by authority_name  
			List<String> queryResources = new ArrayList<String>();  
			String authority_id=ds.find(Authorities.class, "authority_name =", auth).get().getAuthority_id();  

			//          get resource_id from authorities_resources by authority_id resource_id  
			List<AuthoritiesResources> authoritiesResourcesList=ds.find(AuthoritiesResources.class, "authority_id =", authority_id).asList();  
			for (AuthoritiesResources authoritiesResources : authoritiesResourcesList) {  
				//              get resouce_string from resources by resouce_id  
				String resource_string=ds.find(PageResources.class, "resource_id =", authoritiesResources.getResource_id()).get().getResource_string();  
				logger.info("权限ID 有  Authority_ID："+authority_id+"///对应全新资源名字:"+resource_string);  
				queryResources.add(resource_string);  
			}  

			for (String res : queryResources) {  
				String url = res;  
				if (resourceMap.containsKey(url)) {  
					Collection<ConfigAttribute> value = resourceMap.get(url);  
					value.add(ca);  
					resourceMap.put(url, value);  
				}else {  
					Collection<ConfigAttribute> atts  = new ArrayList<ConfigAttribute>();  
					atts.add(ca);  
					resourceMap.put(url, atts);  
				}  
			}  

		}  
	}  


	public Collection<ConfigAttribute> getAllConfigAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection<ConfigAttribute> getAttributes(Object arg0)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean supports(Class<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}
*/