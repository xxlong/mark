package com.anyway.imagemark.controller;

import java.security.Principal;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import com.anyway.imagemark.domain.Administrator;
import com.anyway.imagemark.domain.Member;
import com.anyway.imagemark.domain.Merchant;
import com.anyway.imagemark.service.AdministratorService;
import com.anyway.imagemark.service.MemberService;
import com.anyway.imagemark.service.MerchantService;
import com.anyway.imagemark.utils.CipherUtil;
import com.anyway.imagemark.utils.ToolConstants;

@Controller
public class LoginController {
	private static Logger log = Logger.getLogger(LoginController.class);
	/*@RequestMapping(value="/welcome", method = RequestMethod.GET)
    public String printWelcome(Model model, Principal principal ) {

        String name = principal.getName();
        model.addAttribute("username", name);
        model.addAttribute("message", "Spring Security Custom Form example");
        return "hello"; 
    }
*/
	@Autowired
	private AdministratorService administratorService;
	@Autowired
	private MerchantService merchantService;
	@Autowired

	private MemberService memberService;
    @RequestMapping(value="/login", method = RequestMethod.GET)
    public String login(Model model) {
        return "login";
    }

    @RequestMapping(value="/403", method = RequestMethod.GET)
    public String errorPage(Model model) { 
        return "403"; 
    }
    @RequestMapping(value="/loginfailed", method = RequestMethod.GET)
    public String loginerror(Model model) { 
        model.addAttribute("error", "true");
        return "login"; 
    }
    
    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logout(Model model) { 
        return "login"; 
    }
    
    @RequestMapping(value="/dispatch", method = RequestMethod.GET)
	public View dispatch(Model model,Principal principal,HttpServletRequest request) {
    	log.info("execute LoginController--dispatch");
    	String username = principal.getName();
    	String path = request.getContextPath() ;
    	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";  
    	String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("http_client_ip");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		// 如果是多级代理，那么取第一个ip为客户ip
		if (ip != null && ip.indexOf(",") != -1) {
			ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();
		}
    	Set<String> roles = AuthorityUtils.authorityListToSet(SecurityContextHolder.getContext()
    			.getAuthentication().getAuthorities());
    	int feedback=100;
    	if (roles.contains("ROLE_ADMIN_ALL")||roles.contains("ROLE_ADMIN_VERIFIER")) {
    		System.out.println("LoginController(97)");
    		Administrator administrator =administratorService.getAdministratorByNameOrMail(username);
    		feedback=administratorService.login(username, administrator.getAdminPassword(), 0, ip);
    		if(feedback==ToolConstants.ResultStatus_Success){
    			log.info("execute LoginController--login with adminName:"+administrator.getAdminName());
    		}
    		return new RedirectView(basePath + "administrator/notificationIndex");
    	}else if(roles.contains("ROLE_MERCHANT")){
    		System.out.println("LoginController(105)");
    		
    		Merchant merchant=merchantService.queryMerchantByNameOrMail(username,ToolConstants.VALID_INT);
    		
    		//******************************************KG*******************************************
    		if(merchant!=null){
        		feedback=merchantService.login(username, merchant.getMerchantPassword(), 0, ip);
    		}
    		//******************************************KG*******************************************

    		if(feedback==ToolConstants.ResultStatus_Success){
    			log.info("execute LoginController--login with merchantName:"+merchant.getMerchantName());
    		}
    		return new RedirectView(basePath + "merchant/notificationIndex");
		}else{
			System.out.println("LoginController(113)");
			Member member=memberService.queryMemberByNameOrMail(username);
			feedback=memberService.login(username, member.getMemberPassword(), 0, ip);
			if(feedback==ToolConstants.ResultStatus_Success){
    			log.info("execute LoginController--login with memberName:"+member.getMemberName());
    		}
			return new RedirectView(basePath + "member/notificationIndex");
		}
	}
}
