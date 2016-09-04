package com.anyway.imagemark.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException; 
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.StringUtils;

import com.anyway.imagemark.domain.Administrator;

public class LoginSuccessHandler implements AuthenticationSuccessHandler,
		InitializingBean {
	private static Logger log = Logger.getLogger(LoginSuccessHandler.class);
	private String defaultTargetUrl;  

	private boolean forwardToDestination = false;  

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		if(StringUtils.isEmpty(defaultTargetUrl))  
            throw new Exception("You must configure defaultTargetUrl");
	}
	
	//@Transactional(readOnly=false,propagation= Propagation.REQUIRED,rollbackFor={Exception.class})
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
					throws IOException,ServletException {
		// TODO Auto-generated method stub
		this.saveLoginInfo(request, authentication);  
        if(this.forwardToDestination){  
        	log.info("Login success,Forwarding to "+this.defaultTargetUrl);  
              
            request.getRequestDispatcher(this.defaultTargetUrl).forward(request, response);  
        }else{  
        	log.info("Login success,Redirecting to "+this.defaultTargetUrl);  
              
            this.redirectStrategy.sendRedirect(request, response, this.defaultTargetUrl);  
        }  
	}
	
	//@Transactional(readOnly=false,propagation= Propagation.REQUIRED,rollbackFor={Exception.class})  
    public void saveLoginInfo(HttpServletRequest request,Authentication authentication){  
    	Administrator user = (Administrator)authentication.getPrincipal();  
        try {  
            String ip = this.getIpAddress(request);  
           // Date date = new Date();  
            //user.setLastLogin(date);  
            //user.setLoginIp(ip);  
            //this.sysUsersRepository.saveAndFlush(user);  
        } catch (DataAccessException e) {  
            /*if(log.isWarnEnabled()){  
            	log.info("无法更新用户登录信息至数据库");  
            } */ 
        }  
    }  
      
    public String getIpAddress(HttpServletRequest request){    
        String ip = request.getHeader("x-forwarded-for");    
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {    
            ip = request.getHeader("Proxy-Client-IP");    
        }    
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {    
            ip = request.getHeader("WL-Proxy-Client-IP");    
        }    
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {    
            ip = request.getHeader("HTTP_CLIENT_IP");    
        }    
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {    
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");    
        }    
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {    
            ip = request.getRemoteAddr();    
        }    
        return ip;    
    }  
  
    public void setDefaultTargetUrl(String defaultTargetUrl) {  
        this.defaultTargetUrl = defaultTargetUrl;  
    }  
  
    public void setForwardToDestination(boolean forwardToDestination) {  
        this.forwardToDestination = forwardToDestination;  
    }  

}
