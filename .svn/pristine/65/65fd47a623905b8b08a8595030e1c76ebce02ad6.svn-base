package com.anyway.imagemark.security;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.anyway.imagemark.daoimpl.AdministratorDao;
import com.anyway.imagemark.domain.Administrator;


@Service
public class MongoUserDetailsService implements UserDetailsService {
	@Autowired
	private AdministratorDao administratorDao;
	private static final Logger logger =
	Logger.getLogger(MongoUserDetailsService.class);
	//private User userdetails;
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		logger.info("execute MongoUserDetailsService--loadUserByUsername");
		boolean enabled = true;
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;
		Administrator administrator = getUserDetail(username);
		logger.info("Authorized administrator:"+administrator.getAdminMail());
		return new User(administrator.getAdminName(),administrator.getAdminPassword()
				,enabled,accountNonExpired,credentialsNonExpired,
				accountNonLocked,getAuthorities(administrator.getRole()));
	}
	
	public List<GrantedAuthority> getAuthorities(Integer
			role) {
		List<GrantedAuthority> authList =
				new ArrayList<GrantedAuthority>();
		if (role.intValue() == 10) {
			authList.add(new SimpleGrantedAuthority
					("ROLE_ADMIN_ALL"));
		} else if (role.intValue() == 11) {
			authList.add(new SimpleGrantedAuthority
					("ROLE_ADMIN_VERIFIER"));
		}else if (role.intValue() == 2) {
			authList.add(new SimpleGrantedAuthority
					("ROLE_MERCHANT"));
		}else{
			authList.add(new SimpleGrantedAuthority
					("ROLE_MEMBER"));
		}
		return authList;
	}
	
	public Administrator getUserDetail(String username) {
		logger.info("execute MongoUserDetailsService--getUserDetail,username:"+username);
		Administrator administrator =administratorDao.getAdministratorByNameOrMail(username);
		return administrator;
	}

}
