package com.anyway.imagemark.security;

import java.util.Collection;
import java.util.Iterator;  

import org.apache.log4j.Logger;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class MyAccessDecisionManager implements AccessDecisionManager {
	Logger logger = Logger.getLogger(MyAccessDecisionManager.class);
	public void decide(Authentication authentication, Object arg1,
			Collection<ConfigAttribute> configAttributes) throws AccessDeniedException,
			InsufficientAuthenticationException {
		// TODO Auto-generated method stub
		if(configAttributes == null){  
			return ;  
		}  
		Iterator<ConfigAttribute> ite=configAttributes.iterator();  
		while(ite.hasNext()){  
			ConfigAttribute ca=ite.next();  
			String needRole=((SecurityConfig)ca).getAttribute();  
			for(GrantedAuthority ga:authentication.getAuthorities()){  
				//ga 为用户所被赋予的权限。 needRole 为访问相应的资源应该具有的权限。  
				logger.debug("访问当前资源应该具有的权限="+needRole.trim()+"用户的访问的权限="+ga.getAuthority().trim());  
				if(needRole.trim().equals(ga.getAuthority().trim())){  //ga is user's role.  
					return ;  
				}  
			}  
		}  
		throw new AccessDeniedException("no right");

	}

	public boolean supports(ConfigAttribute arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean supports(Class<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}
