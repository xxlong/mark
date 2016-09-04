package com.anyway.imagemark.security;

import java.util.ArrayList;  
import java.util.Collection;  
import java.util.HashMap;
import java.util.HashSet;  
import java.util.LinkedHashSet;  
import java.util.List;  
import java.util.Map;  
import java.util.Set;  

import javax.annotation.Resource;  
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils; 
import org.apache.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;  


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;  
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;  

import com.anyway.imagemark.daoimpl.MemberDao;
import com.anyway.imagemark.domain.Member;

public class MemberDetailService implements UserDetailsService {
	Logger logger = Logger.getLogger(MemberDetailService.class);  
	
	private UserCache userCache;  
	private DataSource dataSource;

	@Autowired
	private MemberDao memberDao;
	public UserDetails loadUserByUsername(String memberName)
			throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Member member=memberDao.getMemberByNameOrMail(memberName);
		if (member == null){
			return null; 
		}
		Collection<GrantedAuthority> auths=new ArrayList<GrantedAuthority>();  
		Set<GrantedAuthority> authSet = new HashSet<GrantedAuthority>();  
		//authSet.add(new GrantedAuthorityImpl(res.getName()));  

		//取得用户的权限  
		//List<String> auth=pubUsersService.findAuthByUserName(username);
		List<String> auth=new ArrayList<String>();
		auth.add("ROLE_ADMIN");
		auth.add("ROLE_MEMBER");
		auth.add("ROLE_MERCHANT");
		//获取用户权限描述  
		//String authority_desc = pubUsersService.findAuthDescByUserName(username);
		String authority_desc="Description";
		for(int i =0;i<auth.size();i++)  
		{  
			logger.debug("登录用户： "+memberName+" 用户权限包括 "+auth.get(i));  
			authSet.add(new GrantedAuthorityImpl(auth.get(i)));  
		}  
		auths = authSet;  
		String password=null;  
		String userid = null;  

		//取得用户的密码  
		/*UserSoc usesoc = new UserSoc();  
		usesoc=pubUsersService.findUserSocByname(memberName);  
		userid=usesoc.getUser_id();  
		password=usesoc.getUser_password();  

		//String user_location = pubUsersService.findUserRolesByUserId(userid);  
		Roles roles = new Roles();  
		roles = pubUsersService.findRolesByUserId(userid);  
		//      String rolesId = roles.getRole_id();  
		//      可以返回province device 的ID 也可以返回中文  
		String provinceIdRole=roles.getProvinceid();  
		String devicetypeIdRole=roles.getDevicetype();  */

		/*//查询省中文名,role  中省可能有是","分割的  
		String[] provinceIdArray = StringUtils.split(provinceIdRole, ",");  
		Collection<String> piaCol = new LinkedHashSet<String>();  
		for (String provinceId : provinceIdArray) {  
			piaCol.add(pubUsersService.findResourceById(provinceId,"province"));  
		}  
		String provincename=StringUtils.join(piaCol, ",");  

		//设备中文名查询  
		String[] devicetypeIdArray = StringUtils.split(devicetypeIdRole, ",");  
		Collection<String> diaCol = new LinkedHashSet<String>();  
		for (String devicetypeId : devicetypeIdArray) {  
			diaCol.add(pubUsersService.findResourceById(devicetypeId,"device_type"));  
		}  
		String devicetypeName=StringUtils.join(diaCol, ",");  

		//查询资源列表  
		String resourceIdArray= pubUsersService.findResourcesIdByUserName(roles.getRole_id());  */

		//<span style="color: rgb(0, 153, 0);">//此处可以把用户登录后相关数据写到SESSION中，用于其他功能使用</span>  
		Map<String, Object> sessionmap = new HashMap<String, Object>();  
		sessionmap.put("usernamesession", memberName);  
		sessionmap.put("userid", userid);  
		//sessionmap.put("provinceid", provinceIdRole);  
		//sessionmap.put("devicetypeid", devicetypeIdRole);  
		//资源列表  
		//sessionmap.put("resourceArray",resourceIdArray );  
		//操作日志会用到  
		//sessionmap.put("provincename", provincename);  
		//sessionmap.put("devicetypeName", devicetypeName);  
		sessionmap.put("authority_desc", authority_desc);  
		//sessionmap.put("user_location", user_location);  
		//sessionmap.put("role_id", roles.getRole_id());  
		//logger.debug("登录用户名为:"+memberName+"//登录用户ID:"+userid+"//登录省份ID:"+provinceIdRole+"//登录设备ID："+devicetypeIdRole+"//登录省份名称："+provincename+"//登录设备名称："+devicetypeName+"//权限："+authority_desc);  

		boolean enables = true;  
		boolean accountNonExpired = true;  
		boolean credentialsNonExpired = true;  
		boolean accountNonLocked = true;  
		//封装成spring security User类  
		User userdetails = new User(memberName,password,enables,accountNonExpired,credentialsNonExpired,accountNonLocked,auths);  
		//logger.debug("当前登录用户名：密码="+memberName+":"+password);  

		//      用户登录时操作一次即可，登出后销毁  
		/*ActionContext ctx = ActionContext.getContext();  
		ctx.getSession().putAll(sessionmap);  */  

		return userdetails;


	}
	
	public UserCache getUserCache() {  
		return this.userCache;  
	}  
	public void setUserCache(UserCache userCache) {  
		this.userCache = userCache;  
	}  
	public DataSource getDataSource() {  
		return dataSource;  
	}  
	public void setDataSource(DataSource dataSource) {  
		this.dataSource = dataSource;  
	}  


}
