package com.anyway.imagemark.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.Principal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.anyway.imagemark.domain.Administrator;
import com.anyway.imagemark.domain.Comment;
import com.anyway.imagemark.domain.MarkInfo;
import com.anyway.imagemark.domain.Member;
import com.anyway.imagemark.domain.Merchant;
import com.anyway.imagemark.domain.Notification;
import com.anyway.imagemark.mail.SendMail;
import com.anyway.imagemark.service.AdministratorService;
import com.anyway.imagemark.service.MemberService;
import com.anyway.imagemark.service.MerchantService;
import com.anyway.imagemark.utils.CipherUtil;
import com.anyway.imagemark.utils.DateFormat;
import com.anyway.imagemark.utils.PageContent;
import com.anyway.imagemark.utils.ResultInfo;
import com.anyway.imagemark.utils.ToolConstants;
import com.anyway.imagemark.utils.VerifyUtil;
import com.anyway.imagemark.webDomain.MemberComment;
import com.anyway.imagemark.webDomain.MemberInfo;
import com.anyway.imagemark.webDomain.MerchantInfo;
import com.anyway.imagemark.webDomain.MerchantMark;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@Controller
@RequestMapping(value = "/administrator")
public class AdministratorController {
	private static Logger log = Logger.getLogger(AdministratorController.class);
	@Autowired
	private AdministratorService administratorService;
	@Autowired
	private MerchantService merchantService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private ResultInfo resultInfo;
	
	@Autowired
	private SendMail sendMail;
	private Gson gson = new Gson();

	// 后台管理系统--展示登录表单

	@RequestMapping(value="/showPage", method = RequestMethod.GET)
	public String showLoginPage(Model model) {
		log.info("execute AdministratorController--showLoginPage");
		model.addAttribute(new Administrator());
		return "login";
	}

	@RequestMapping(value = "/welcome", method = RequestMethod.GET)
	public String showHelloPage(Model model, Principal principal) {
		log.info("execute AdministratorController--showHelloPage");
		String name = principal.getName();
		model.addAttribute("username", name);
		model.addAttribute("message", "Spring Security Custom Form example");
		return "administrator/view";
	}

	// 后台管理系统--登录处理
	@RequestMapping(method = RequestMethod.GET)
	public String login(@Valid Administrator administrator,
			BindingResult bindingResult, HttpServletRequest request) {
		log.info("execute AdministratorController--login with adminName:"
				+ administrator.getAdminName());
		if (bindingResult.hasErrors()) {
			return "login";
		}
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
		String password = CipherUtil.generatePassword(administrator
				.getAdminPassword());
		int feedback;
		feedback = administratorService.login(administrator.getAdminName(),
				password, 0, ip);
		if (feedback == ToolConstants.LOGIN_SUCCESS) {
			log.info("login successfull!");
			Administrator administratorFromDB = administratorService
					.getAdministratorByNameOrMail(administrator.getAdminName());
			log.info("has got "
					+ " ");
			if (administratorFromDB.getRole() == 2) {
				return "redirect:/merchant/" + administrator.getAdminName();
			} else if (administratorFromDB.getRole() == 3) {
				return "redirect:/member/" + administrator.getAdminName();
			} else {
				return "redirect:/administrator/"
						+ administrator.getAdminName();
			}
		} else {
			return "login";
		}
	}
	//商家、用户进入管理界面
	@RequestMapping(value="infoManage",method=RequestMethod.GET)
	public String dispathInfo(@Valid Administrator administrator, HttpServletRequest request){
		Administrator administratorFromDB = administratorService
				.getAdministratorByNameOrMail(administrator.getAdminName());
			if (administratorFromDB.getRole() == 2) {
				return "redirect:/merchant/" + administrator.getAdminName();
			} else if (administratorFromDB.getRole() == 3) {
				return "redirect:/member/" + administrator.getAdminName();
			} else {
				return "redirect:/administrator/"
						+ administrator.getAdminName();
			}
		}
	
	// 增加管理员
	@RequestMapping(value = "/addAdministrator", method = RequestMethod.POST)
	@ResponseBody
	public String addAdministrator(@Valid Administrator administrator,
			BindingResult bindingResult, HttpServletResponse response) {
		log.info("execute AdministratorController--addAdministrator");
		response.setHeader("Access-Control-Allow-Origin", "*");
		if (bindingResult.hasErrors()) {
			resultInfo.setStatus(ToolConstants.ResultStatus_Fail_DataIllegal);
			resultInfo.setDesc(ToolConstants.ResultDesc_Fail_DataIllegal);
			String json = gson.toJson(resultInfo);
			return json;
		}
		if (administrator.getAdminPassword() != null
				&& !administrator.getAdminPassword().isEmpty()) {
			String password = administrator.getAdminPassword();
			administrator.setAdminPassword(CipherUtil
					.generatePassword(password));
		}
		int feedback = administratorService.addAdministrator(administrator);
		if (feedback == ToolConstants.ResultStatus_Success) {
			resultInfo.setData(null);
			resultInfo.setStatus(ToolConstants.ResultStatus_Success);
			resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		} else if (feedback == ToolConstants.ResultStatus_Fail_MailUsed) {
			resultInfo.setStatus(ToolConstants.ResultStatus_Fail_MailUsed);
			resultInfo.setDesc(ToolConstants.MAILUSED_STRING);
		} else if (feedback == ToolConstants.ResultStatus_Fail_NameUsed) {
			resultInfo.setStatus(ToolConstants.ResultStatus_Fail_NameUsed);
			resultInfo.setDesc(ToolConstants.NAMEUSED_STRING);
		} else {
			resultInfo
					.setStatus(ToolConstants.ResultStatus_Fail_MailAndNameUsed);
			resultInfo.setDesc(ToolConstants.MAILUSED_STRING
					+ ToolConstants.NAMEUSED_STRING);
		}
		String json = gson.toJson(resultInfo);
		return json;
	}

	// 管理员--信息管理--发布通知、公告
	@PreAuthorize("hasRole('ROLE_ADMIN_ALL')")
	@RequestMapping(value = "/addNotification", method = RequestMethod.POST)
	@ResponseBody
	public String addNotification(@Valid Notification notification,
			BindingResult bindingResult) {
		log.info("execute AdministratorController--addNotification");
		if (bindingResult.hasErrors()) {
			resultInfo.setStatus(ToolConstants.ResultStatus_Fail_DataIllegal);
			resultInfo.setDesc(ToolConstants.ResultDesc_Fail_DataIllegal);
			String json = gson.toJson(resultInfo);
			return json;
		}
		administratorService.addNotification(notification);
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	/**
	 * @author Kario 获取通知和公告
	 *
	 */
	@RequestMapping(value = "/queryNotification", method = RequestMethod.POST)
	@ResponseBody
	public String queryNotification(
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam(value = "type", required = true, defaultValue = "1") int type,
			@RequestParam(value = "page", required = true, defaultValue = "1") int page,
			@RequestParam(value = "rows", required = true, defaultValue = "10") int rows,
			HttpServletResponse response) {
		log.info("execute AdministratorController--queryNotification");
		response.setHeader("Access-Control-Allow-Origin", "*");
		PageContent<Notification> pageContent = administratorService
				.queryNotificationsByType(startTime, endTime, type, page, rows);
		resultInfo.setData(pageContent);
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	@RequestMapping(value = "/notificationIndex", method = RequestMethod.GET)
	public String notificationIndex(Model model, Principal principal,
			HttpServletResponse response) {
		log.info("execute AdministratorController--notificationIndex");
		response.setHeader("Access-Control-Allow-Origin", "*");
		DateFormat format = new DateFormat();
		PageContent<Notification> notificationPage = administratorService
				.queryNotificationsByType(format.generateTime(
						ToolConstants.DATEFORMAT_STRING,
						format.generateTime(30)), format.generateTime(
						ToolConstants.DATEFORMAT_STRING,
						System.currentTimeMillis()), 1, 1,
						ToolConstants.BackGround_Default_PageSize);
		model.addAttribute("notificationPage", notificationPage);
		return "administrator/notice";
	}

	// 管理员--信息管理--通知、公告管理
	@RequestMapping(value = "/deleteNotification", method = RequestMethod.GET)
	@ResponseBody
	public String deleteNotification(@RequestParam List<String> data,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("execute AdministratorController--deleteNotification");
		for (int i = 0; i < data.size(); i++) {
			administratorService.deleteNotification(data.get(i));
		}
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	// 管理员--信息管理--修改通知、公告
	@RequestMapping(value = "/updateNotification", method = RequestMethod.POST)
	@ResponseBody
	public String updateNotification(@Valid Notification notification,
			BindingResult bindingResult) {
		log.info("execute AdministratorController--updateNotification");
		if (bindingResult.hasErrors()) {
			resultInfo.setStatus(ToolConstants.ResultStatus_Fail_DataIllegal);
			resultInfo.setDesc(ToolConstants.ResultDesc_Fail_DataIllegal);
			String json = gson.toJson(resultInfo);
			return json;
		}
		DBObject condition = new BasicDBObject();
		condition.put("sendID", "xx");
		administratorService.updateNotification(condition, notification);
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	// 管理员--删除商家
	@RequestMapping(value = "/deleteMerchant", method = RequestMethod.GET)
	@ResponseBody
	public String deleteMerchant(@RequestParam String merchantName) {
		log.info("execute AdministratorController--deleteMerchant");
		administratorService.deleteMerchant(merchantName);
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	// 管理员--删除标记
	@RequestMapping(value = "/deleteMarkInfo", method = RequestMethod.GET)
	@ResponseBody
	public String deleteMarkInfo(MarkInfo markInfo) {
		log.info("execute AdministratorController--deleteMarkInfo");
		administratorService.deleteMarkInfo(markInfo.get_id());
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	// 管理员--修改标记
	@RequestMapping(value = "/updateMarkInfo", method = RequestMethod.POST)
	@ResponseBody
	public String updateMarkInfo(@Valid MarkInfo markInfo,
			BindingResult bindingResult) {
		log.info("execute AdministratorController--updateMarkInfo");
		if (bindingResult.hasErrors()) {
			resultInfo.setStatus(ToolConstants.ResultStatus_Fail_DataIllegal);
			resultInfo.setDesc(ToolConstants.ResultDesc_Fail_DataIllegal);
			String json = gson.toJson(resultInfo);
			return json;
		}
		DBObject condition = new BasicDBObject();
		condition.put("Url", "url");
		condition.put("componentLinkAddress", "componentLinkAddress");
		administratorService.updateMarkInfo(condition, markInfo);
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	// 管理员--删除评价
	@RequestMapping(value = "/deleteComment", method = RequestMethod.GET)
	@ResponseBody
	public String deleteComment(Comment comment) {
		log.info("execute AdministratorController--deleteComment");
		administratorService.deleteComment(comment.get_id());
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	/*// 管理员--修改评价
	@RequestMapping(value = "/updateComment", method = RequestMethod.GET)
	@ResponseBody
	public String updateComment(Comment newComment) {
		log.info("execute AdministratorController--updateComment");
		Comment comment = new Comment();
		comment.setMem_id(newComment.getMem_id());
		comment.setMark_id(newComment.getMark_id());
		comment.setComment(newComment.getComment());
		comment.setCriticTime(System.currentTimeMillis());
		DBObject condition = new BasicDBObject();
		condition.put("_id", newComment.get_id());
		administratorService.updateComment(condition, comment);
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}*/

	/**
	 * @author Kario 统计会员的总量及当期会员的增量
	 * @return List<DBobject>
	 */
	@RequestMapping(value = "/getNumForMember", method = RequestMethod.GET)
	@ResponseBody
	public String getNumForMember(HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("execute adminstrator method:getNumForMember ");
		return gson.toJson(administratorService.getNumForMember());
	}

	// 管理员--统计分析--会员统计--获得某段时间内注册的统计的用户信息
	@RequestMapping(value = "/getStatisticAnalysisOnMembers", method = RequestMethod.POST)
	@ResponseBody
	public String getStatisticAnalysisOnMembers(
			@RequestParam(value = "starttime", required = false) String starttime,
			@RequestParam(value = "endtime", required = false) String endtime,
			@RequestParam(value = "sortField", required = true, defaultValue = "1") int sortField,
			@RequestParam(value = "sortType", required = true, defaultValue = "1") int sortType,
			@RequestParam(value = "page", required = true, defaultValue = "1") int page,
			@RequestParam(value = "rows", required = true, defaultValue = "10") int rows,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("execute AdministratorController--getStatisticAnalysisOnMembers");
		PageContent<MemberInfo> pageContent = new PageContent<MemberInfo>();
		// sortField为---注册时间统计（sortType为时间排序），1---积分统计（sortType为次数排序），2---点击数统计，（sortType为次数排序）3---评论数统计（sortType为次数排序）；
		if (sortField == 1) {
			pageContent = administratorService.getStatisticalByScore(starttime,
					endtime, sortType, page, rows);
		} else if (sortField == 2) {
			pageContent = administratorService.getMembersBehavioursByClickSpan(
					starttime, endtime, sortType, page, rows);
		} else if (sortField == 3) {
			pageContent = administratorService
					.getMembersBehavioursByCommentSpan(starttime, endtime,
							sortType, page, rows);
		} else if (sortField == 4) {
			pageContent = administratorService
					.getMembersBehavioursByRegisterSpan(starttime, endtime,
							sortType, page, rows);
		} else {
			log.info("other");
		}
		resultInfo.setData(pageContent);
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	/**
	 * @author Kario 统计每月增量
	 * @param year
	 *            年份
	 * @param month
	 *            月份
	 * @return List<DBobject>
	 */
	@RequestMapping(value = "/increseMemberPerMonth", method = RequestMethod.GET)
	@ResponseBody
	public String increseMemberPerMonth(@RequestParam int year,
			@RequestParam int month, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("execute increseMemberPerMonth");
		PageContent<DBObject> pageContent = administratorService
				.getMemberIncreseMentPerMonth(year, month);
		resultInfo.setData(pageContent);
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	/**
	 * @author Kario 统计商家的总量及当期会员的增量
	 * @return List<DBobject>
	 */
	@RequestMapping(value = "/getNumForMerchant", method = RequestMethod.GET)
	@ResponseBody
	public String getNumForMerchant(HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("execute adminstrator method:getNumForMerchant ");
		return gson.toJson(administratorService.getNumForMerchant());
	}

	// 管理员--统计分析--商家统计--获得某段时间内注册的统计的商家信息
	@RequestMapping(value = "/getStatisticAnalysisOnMerchants", method = RequestMethod.POST)
	@ResponseBody
	public String getStatisticAnalysisOnMerchants(
			@RequestParam(value = "starttime", required = false) String starttime,
			@RequestParam(value = "endtime", required = false) String endtime,
			@RequestParam(value = "sortField", required = true, defaultValue = "1") int sortField,
			@RequestParam(value = "sortType", required = true, defaultValue = "1") int sortType,
			@RequestParam(value = "page", required = true, defaultValue = "1") int page,
			@RequestParam(value = "rows", required = true, defaultValue = "10") int rows,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("execute AdministratorController--getStatisticAnalysisOnMerchants");
		PageContent<MerchantInfo> pageContent = new PageContent<MerchantInfo>();
		/* sortField :1为商家积分，2为标记数量，3为被点击数，4为被评论数，5为注册时间 */
		if (sortField == 1) {
			pageContent = administratorService
					.getMerchantsBehavioursByMarkAggregate(starttime, endtime,
							"trust", sortType, page, rows);
		} else if (sortField == 2) {
			pageContent = administratorService
					.getMerchantsBehavioursByMarkAggregate(starttime, endtime,
							"count", sortType, page, rows);
		} else if (sortField == 3) {
			pageContent = administratorService
					.getMerchantsBehavioursByMarkAggregate(starttime, endtime,
							"totalClick", sortType, page, rows);
		} else if (sortField == 4) {
			pageContent = administratorService
					.getMerchantsBehavioursByMarkAggregate(starttime, endtime,
							"totalComment", sortType, page, rows);
		} else if (sortField == 5) {
			pageContent = administratorService
					.getMerchantsBehavioursByRegisterSpan(starttime, endtime,
							sortType, page, rows);
		} else {
			log.info("other ");
		}
		resultInfo.setData(pageContent);
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	/**
	 * @author Kario 统计每月增量
	 * @param year
	 *            年份
	 * @param month
	 *            月份
	 * @return List<DBobject>
	 */
	@RequestMapping(value = "/increseMerchantPerMonth", method = RequestMethod.GET)
	@ResponseBody
	public String increseMerchantPerMonth(@RequestParam int year,
			@RequestParam int month, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("execute increseMemberPerMonth");
		PageContent<DBObject> pageContent = administratorService
				.getMerchantIncreseMentPerMonth(year, month);
		resultInfo.setData(pageContent);
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	/**
	 * @author Kario 统计标记的总量及当期会员的增量
	 * @return List<DBobject>
	 */
	@RequestMapping(value = "/getNumForMark", method = RequestMethod.GET)
	@ResponseBody
	public String getNumForMark(HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("execute adminstrator method:getNumForMark ");
		return gson.toJson(administratorService.getNumForMark());
	}

	// 管理员--统计分析--标记统计
	@RequestMapping(value = "/getStatisticAnalysisOnMarks", method = RequestMethod.POST)
	@ResponseBody
	public String getStatisticAnalysisOnMarks(
			@RequestParam(value = "starttime", required = false) String starttime,
			@RequestParam(value = "endtime", required = false) String endtime,
			@RequestParam(value = "sortField", required = true, defaultValue = "1") int sortField,
			@RequestParam(value = "sortType", required = true, defaultValue = "1") int sortType,
			@RequestParam(value = "page", required = true, defaultValue = "1") int page,
			@RequestParam(value = "rows", required = true, defaultValue = "10") int rows,
			HttpServletResponse response) {
		/* sortField :1为标记时间，2为标记信用，3为点击数，4评论数 */
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("execute AdministratorController--getStatisticAnalysisOnMarks");
		PageContent<MerchantMark> pageContent = new PageContent<MerchantMark>();
		if (sortField == 1) {
			pageContent = administratorService.getStatisticalMarkBySort(
					starttime, endtime, "markDate", sortType, page, rows);
		} else if (sortField == 2) {
			pageContent = administratorService.getStatisticalMarkBySort(
					starttime, endtime, "componentTrust", sortType, page, rows);
		} else if (sortField == 3) {
			pageContent = administratorService.getStatisticalMarkBySort(
					starttime, endtime, "directCount", sortType, page, rows);
		} else if (sortField == 4) {
			pageContent = administratorService.getStatisticalMarkBySort(
					starttime, endtime, "praise", sortType, page, rows);
		} else {
			pageContent = administratorService.getStatisticalMarkBySort(
					starttime, endtime, "markDate", sortType, page, rows);
		}
		if (pageContent != null) {
			resultInfo.setData(pageContent);
			resultInfo.setStatus(ToolConstants.ResultStatus_Success);
			resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		} else {
			resultInfo.setStatus(ToolConstants.ResultStatus_NoData);
			resultInfo.setDesc(ToolConstants.ResultDesc_NoData);
		}

		String json = gson.toJson(resultInfo);
		return json;
	}

	/**
	 * @author Kario 统计每月增量
	 * @param year
	 *            年份
	 * @param month
	 *            月份
	 * @return List<DBobject>
	 */
	@RequestMapping(value = "/increseMarkPerMonth", method = RequestMethod.GET)
	@ResponseBody
	public String increseMarkPerMonth(@RequestParam int year,
			@RequestParam int month, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("execute increseMemberPerMonth");
		PageContent<DBObject> pageContent = administratorService
				.getMarkIncreseMentPerMonth(year, month);
		resultInfo.setData(pageContent);
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	@RequestMapping(value = "/increseMarkByTypePerMonth", method = RequestMethod.GET)
	@ResponseBody
	public String increseMarkByTypePerMonth(@RequestParam int year,
			@RequestParam int month, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("execute increseMemberPerMonth");
		PageContent<DBObject> pageContent = administratorService
				.getMarkIncreseByTypeMentPerMonth(year, month);
		resultInfo.setData(pageContent);
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	/**
	 * @author Kario 统计评论的总量及当期评论的增量
	 * @return List<DBobject>
	 */
	@RequestMapping(value = "/getNumForComment", method = RequestMethod.GET)
	@ResponseBody
	public String getNumForComment(HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("execute adminstrator method:getNumForMerchant ");
		return gson.toJson(administratorService.getNumForComment());
	}

	// 管理员--统计分析--评价统计
	@RequestMapping(value = "/getStatisticAnalysisOnComments", method = RequestMethod.POST)
	@ResponseBody
	public String getStatisticAnalysisOnComments(
			@RequestParam(value = "starttime", required = false) String starttime,
			@RequestParam(value = "endtime", required = false) String endtime,
			@RequestParam(value = "sortField", required = true, defaultValue = "1") int sortField,
			@RequestParam(value = "sortType", required = true, defaultValue = "1") int sortType,
			@RequestParam(value = "page", required = true, defaultValue = "1") int page,
			@RequestParam(value = "rows", required = true, defaultValue = "10") int rows,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("execute AdministratorController--getStatisticAnalysisOnComments");
		PageContent<MemberComment> pageContent = administratorService
				.getStatisticalCommentByNewAdd(starttime, endtime, sortType,
						page, rows);
		resultInfo.setData(pageContent);
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	/**
	 * @author Kario 统计每月增量
	 * @param year
	 *            年份
	 * @param month
	 *            月份
	 * @return List<DBobject>
	 */
	@RequestMapping(value = "/increseCommentPerMonth", method = RequestMethod.GET)
	@ResponseBody
	public String increseCommentPerMonth(@RequestParam int year,
			@RequestParam int month, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("execute increseMemberPerMonth");
		PageContent<DBObject> pageContent = administratorService
				.getCommentIncreseMentPerMonth(year, month);
		resultInfo.setData(pageContent);
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	/**
	 * @author Kario 统计点击的总量及当期点击的增量
	 * @return List<DBobject>
	 */
	@RequestMapping(value = "/getNumForClick", method = RequestMethod.GET)
	@ResponseBody
	public String getNumForClick(HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("execute adminstrator method:getNumForMerchant ");
		return gson.toJson(administratorService.getNumForClick());
	}

	@RequestMapping(value = "/increseClickPerMonth", method = RequestMethod.GET)
	@ResponseBody
	public String increseClickPerMonth(@RequestParam int year,
			@RequestParam int month, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("execute increseMemberPerMonth");
		PageContent<DBObject> pageContent = administratorService
				.getClickIncreseMentPerMonth(year, month);
		resultInfo.setData(pageContent);
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	@RequestMapping(value = "/getStatisticAnalysisOnClick", method = RequestMethod.POST)
	@ResponseBody
	public String getStatisticAnalysisOnClick(
			@RequestParam(value = "starttime", required = false) String starttime,
			@RequestParam(value = "endtime", required = false) String endtime,
			@RequestParam(value = "sortField", required = true, defaultValue = "1") int sortField,
			@RequestParam(value = "sortType", required = true, defaultValue = "1") int sortType,
			@RequestParam(value = "page", required = true, defaultValue = "1") int page,
			@RequestParam(value = "rows", required = true, defaultValue = "10") int rows,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("execute AdministratorController--getStatisticAnalysisOnComments");
		PageContent<DBObject> pageContent = administratorService
				.getStatisticalClick(starttime, endtime, sortType, page, rows);
		resultInfo.setData(pageContent);
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	/**
	 * @author Kario 积分统计和信用统计移除
	 *
	 */
	// 管理员--统计分析--积分统计
	@RequestMapping(value = "/getStatisticAnalysisOnMemberScore", method = RequestMethod.GET)
	@ResponseBody
	public String getStatisticAnalysisOnMemberScore(@RequestParam int sortType,
			@RequestParam int pageNumber, @RequestParam int pageSize,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("execute AdministratorController--getStatisticAnalysisOnMemberScore");
		PageContent<MemberInfo> pageContent = new PageContent<MemberInfo>();
		DBObject query = new BasicDBObject();
		query.put("status", ToolConstants.VALID_INT);
		DBObject sortOrder = new BasicDBObject();
		if (sortType == 1) {
			sortOrder.put("memberScore", 1);
		} else if (sortType == 2) {
			sortOrder.put("memberScore", -1);
		} else {
			sortOrder = null;
		}
		pageContent = administratorService.getAllMembersBehaviours(query,
				sortOrder, pageNumber, pageSize);
		if (pageContent != null) {
			resultInfo.setData(pageContent);
			resultInfo.setStatus(ToolConstants.ResultStatus_Success);
			resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		} else {
			resultInfo.setStatus(ToolConstants.ResultStatus_NoData);
			resultInfo.setDesc(ToolConstants.ResultDesc_NoData);
		}

		String json = gson.toJson(resultInfo);
		return json;
	}

	// 管理员--统计分析--信用统计
	@RequestMapping(value = "/getStatisticAnalysisOnMerchantTrust", method = RequestMethod.GET)
	@ResponseBody
	public String getStatisticAnalysisOnMerchantTrust(
			@RequestParam int sortType, @RequestParam int pageNumber,
			@RequestParam int pageSize) {
		log.info("execute AdministratorController--getStatisticAnalysisOnMerchantTrust");
		DBObject query = new BasicDBObject();
		query.put("status", ToolConstants.VALID_INT);
		DBObject sortOrder = new BasicDBObject();
		if (sortType == 1) {
			sortOrder.put("merchantTrust", 1);
		} else if (sortType == 2) {
			sortOrder.put("merchantTrust", -1);
		} else {
			sortOrder = null;
		}
		PageContent<MerchantInfo> pageContent = administratorService
				.getAllMerchantsBehaviours(query, sortOrder, pageNumber,
						pageSize);
		resultInfo.setData(pageContent);
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	// 获取某一张图的标记位置信息
	@RequestMapping(value = "/getNodes", method = RequestMethod.GET)
	@ResponseBody
	public String getNodes(@RequestParam String url,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("execute AdministratorController--getNodes");
		PageContent<DBObject> pageContent = merchantService.queryNode(url, 0);
		resultInfo.setData(pageContent);
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	// 设置某一张图的标记位置信息的状态
	@RequestMapping(value = "/changeNodeStatus", method = RequestMethod.GET)
	@ResponseBody
	public String changeNodeStatus(@RequestParam String node,
			@RequestParam int status, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("execute AdministratorController--changeNodeStatus");
		int feedBack = administratorService.changeNodeStatus(node, status);
		resultInfo.setStatus(feedBack);
		if (feedBack == ToolConstants.ResultStatus_Success) {
			resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		} else if (feedBack == ToolConstants.ResultStatus_Fail_InsertError) {
			resultInfo.setDesc(ToolConstants.ResultDesc_Fail_InsertError);
		} else if (feedBack == ToolConstants.ResultStatus_NoData) {
			resultInfo.setDesc(ToolConstants.ResultDesc_NoData);
		} else {

		}
		String json = gson.toJson(resultInfo);
		return json;
	}

	// 获取点相关的未审核标记
	@RequestMapping(value = "/getMarkInfoByNode", method = RequestMethod.POST)
	@ResponseBody
	public String getMarkInfoByNode(
			@RequestParam String node,
			@RequestParam(value = "sortType", required = true, defaultValue = "4") int sortType,
			@RequestParam(value = "page", required = true, defaultValue = "1") int page,
			@RequestParam(value = "rows", required = true, defaultValue = "10") int rows,
			HttpServletResponse response) {
		log.info("execute MerchantController--getMarkInfoByNode  node=" + node);
		response.setHeader("Access-Control-Allow-Origin", "*");
		PageContent<DBObject> pageContent = new PageContent<DBObject>();
		pageContent = merchantService.queryMarkByNode(node, sortType, page,
				ToolConstants.VERIFY_INT, rows);
		if (pageContent == null) {
			log.info("getStatisticalMarksByMerchantName  no data!");
			resultInfo.setStatus(ToolConstants.ResultStatus_NoData);
			resultInfo.setDesc(ToolConstants.ResultDesc_NoData);
		} else {
			resultInfo.setData(pageContent);
			resultInfo.setStatus(ToolConstants.ResultStatus_Success);
			resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		}
		String json = gson.toJson(resultInfo);
		return json;
	}

	// 获取点相关的未审核标记
	@RequestMapping(value = "/getMarkInfoByNodeId", method = RequestMethod.POST)
	@ResponseBody
	public String getMarkInfoByNodeId(@RequestParam String node,
			@RequestParam int sortType, @RequestParam int page,
			@RequestParam int rows, HttpServletResponse response) {
		log.info("execute AdministratorController--getMarkInfoByNodeId  node_Id="
				+ node);
		response.setHeader("Access-Control-Allow-Origin", "*");
		PageContent<DBObject> pageContent = new PageContent<DBObject>();
		pageContent = merchantService.queryMarkByNode(node, sortType, page,
				ToolConstants.VERIFY_INT, rows);
		if (pageContent == null) {
			log.info("getStatisticalMarksByMerchantName  no data!");
			resultInfo.setStatus(ToolConstants.ResultStatus_NoData);
			resultInfo.setDesc(ToolConstants.ResultDesc_NoData);
		} else {
			resultInfo.setData(pageContent);
			resultInfo.setStatus(ToolConstants.ResultStatus_Success);
			resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		}
		String json = gson.toJson(resultInfo);
		return json;
	}

	/**
	 * @author Kario 标记管理只包含审核标记和重审标 查找未审核的标记标记，改变其状态
	 *
	 */
	@RequestMapping(value = "/getNotVerifyMark", method = RequestMethod.POST)
	@ResponseBody
	public String getNotVerifyMark(@RequestParam String startTime,
			@RequestParam String endTime, @RequestParam int sortType,
			@RequestParam int page, @RequestParam int rows,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("execute AdministratorController--getNotVerifyMark");
		PageContent<MerchantMark> pageContent = administratorService
				.getMarksManagementNotVerifiedMarks(startTime, endTime,
						sortType, page, rows);
		if (pageContent != null) {
			resultInfo.setData(pageContent);
			resultInfo.setStatus(ToolConstants.ResultStatus_Success);
			resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		} else {
			resultInfo.setStatus(ToolConstants.ResultStatus_NoData);
			resultInfo.setDesc(ToolConstants.ResultDesc_NoData);
		}
		String json = gson.toJson(resultInfo);
		return json;
	}

	/**
	 * @author Kario 审核标记，改变其状态
	 *
	 */
	@RequestMapping(value = "/VerifyMark", method = RequestMethod.GET)
	@ResponseBody
	public String VerifyMark(@RequestParam List<String> data,
			@RequestParam int status, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("execute AdministratorController--VerifyMark");
		for (int i = 0; i < data.size(); i++) {
			administratorService.validateMarkInfo(data.get(i), status);
		}
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	// 获取有待审核商品的图片
	@RequestMapping(value = "/getVerifyUrl", method = RequestMethod.POST)
	@ResponseBody
	public String getVerifyUrl(
			@RequestParam(value = "rows", required = true, defaultValue = "10") int rows,
			@RequestParam(value = "page", required = true, defaultValue = "1") int page,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("execute AdministratorController--getVerifyUrl");
		PageContent<DBObject> pageContent = administratorService.getVerifyUrl(
				page, rows);
		resultInfo.setData(pageContent);
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	// 获取投诉标记
	@RequestMapping(value = "/getComplainedMark", method = RequestMethod.POST)
	@ResponseBody
	public String getComplainedMark(
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam(value = "sortType", required = true, defaultValue = "1") int sortType,
			@RequestParam(value = "page", required = true, defaultValue = "1") int page,
			@RequestParam(value = "rows", required = true, defaultValue = "10") int rows,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("execute AdministratorController--getComplainedMark");
		PageContent<MerchantMark> pageContent = administratorService
				.getComplainedMark(startTime, endTime, sortType, page, rows);
		if (pageContent != null) {
			resultInfo.setData(pageContent);
			resultInfo.setStatus(ToolConstants.ResultStatus_Success);
			resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		} else {
			resultInfo.setStatus(ToolConstants.ResultStatus_NoData);
			resultInfo.setDesc(ToolConstants.ResultDesc_NoData);
		}
		String json = gson.toJson(resultInfo);
		return json;
	}

	// 审核投诉标记
	@RequestMapping(value = "/VerifyComplain", method = RequestMethod.GET)
	@ResponseBody
	public String VerifyComplain(@RequestParam List<String> data,
			@RequestParam int status, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("execute AdministratorController--verifyComplain");
		for (int i = 0; i < data.size(); i++) {
			administratorService.setComplainStatus(data.get(i), status);
		}
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	// 管理员--标记管理--新增标记--按时间段、行业、时间范围+点击数、时间范围+评论数
	@RequestMapping(value = "/getMarksManagementOnNewAddedMarks", method = RequestMethod.GET)
	@ResponseBody
	public String getMarksManagementOnNewAddedMarks(
			@RequestParam String starttime, @RequestParam String endtime,
			@RequestParam int sortType, @RequestParam int pageNumber,
			@RequestParam int pageSize, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("execute AdministratorController--getMarksManagementOnNewAddedMarks");
		PageContent<MerchantMark> pageContent = new PageContent<MerchantMark>();
		pageContent = administratorService.getStatisticalMarksByNewAdd(
				starttime, endtime, sortType, pageNumber, pageSize);
		resultInfo.setData(pageContent);
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	// 管理员--标记管理--分类标记
	@RequestMapping(value = "/getMarksManagementOnClassifiedMarks", method = RequestMethod.GET)
	@ResponseBody
	public String getMarksManagementOnClassifiedMarks(
			@RequestParam String starttime, @RequestParam String endtime,
			@RequestParam int componentType, @RequestParam int sortType,
			@RequestParam int pageNumber, @RequestParam int pageSize) {
		log.info("execute AdministratorController--getMarksManagementOnClassifiedMarks");
		PageContent<MerchantMark> pageContent = new PageContent<MerchantMark>();
		pageContent = administratorService.getStatisticalMarksByComponentType(
				starttime, endtime, false, componentType, sortType, pageNumber,
				pageSize);
		resultInfo.setData(pageContent);
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	@RequestMapping(value = "/getMarksManagementVerifyMark", method = RequestMethod.GET)
	@ResponseBody
	public String getMarksManagementVerifyMark(@RequestParam String starttime,
			@RequestParam String endtime, @RequestParam int sortType,
			@RequestParam int page, @RequestParam int rows,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("execute AdministratorController--getMarksManagementOnClassifiedMarks");
		PageContent<MerchantMark> pageContent = new PageContent<MerchantMark>();
		pageContent = administratorService.getMarksManagementNotVerifiedMarks(
				starttime, endtime, sortType, page, rows);
		resultInfo.setData(pageContent);
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	// 管理员--标记管理--关注标记
	@RequestMapping(value = "/getMarksManagementOnConcernedMarks", method = RequestMethod.GET)
	@ResponseBody
	public String getMarksManagementOnConcernedMarks(
			@RequestParam String starttime, @RequestParam String endtime,
			@RequestParam int filter, @RequestParam int sortType,
			@RequestParam int pageNumber, @RequestParam int pageSize) {
		log.info("execute AdministratorController--getMarksManagementOnConcernedMarks");
		PageContent<MerchantMark> pageContent = new PageContent<MerchantMark>();
		pageContent = administratorService.getStatisticalMarksByFilter(
				starttime, endtime, "directCount", filter, sortType,
				pageNumber, pageSize);
		resultInfo.setData(pageContent);
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	// 管理员--标记管理--评论标记
	@RequestMapping(value = "/getMarksManagementOnCommentedMarks", method = RequestMethod.GET)
	@ResponseBody
	public String getMarksManagementOnCommentedMarks(
			@RequestParam String starttime, @RequestParam String endtime,
			@RequestParam int filter, @RequestParam int sortType,
			@RequestParam int pageNumber, @RequestParam int pageSize) {
		log.info("execute AdministratorController--getMarksManagementOnCommentedMarks");
		PageContent<MerchantMark> pageContent = new PageContent<MerchantMark>();
		pageContent = administratorService.getStatisticalMarksByFilter(
				starttime, endtime, "commentNum", filter, sortType, pageNumber,
				pageSize);
		resultInfo.setData(pageContent);
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	// 管理员--评论管理--新增评论
	@RequestMapping(value = "/getCommentsManagementOnNewAddedComments", method = RequestMethod.GET)
	@ResponseBody
	public String getCommentsManagementOnNewAddedComments(
			@RequestParam String starttime, @RequestParam String endtime,
			@RequestParam int sortType, @RequestParam int pageNumber,
			@RequestParam int pageSize) {
		log.info("execute AdministratorController--getCommentsManagementOnNewAddedComments");
		PageContent<MemberComment> pageContent = new PageContent<MemberComment>();
		pageContent = administratorService.getStatisticalCommentByNewAdd(
				starttime, endtime, sortType, pageNumber, pageSize);
		resultInfo.setData(pageContent);
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	// 管理员--评论管理--分类评论--分层显示--第一层为标记
	@RequestMapping(value = "/getCommentsManagementOnClassifiedComments", method = RequestMethod.GET)
	@ResponseBody
	public String getCommentsManagementOnClassifiedComments(
			@RequestParam String starttime, @RequestParam String endtime,
			@RequestParam int componentType, @RequestParam int sortType,
			@RequestParam int pageNumber, @RequestParam int pageSize) {
		log.info("execute AdministratorController--getCommentsManagementOnClassifiedComments");
		PageContent<MerchantMark> pageContent = new PageContent<MerchantMark>();
		pageContent = administratorService.getStatisticalMarksByComponentType(
				starttime, endtime, true, componentType, sortType, pageNumber,
				pageSize);
		resultInfo.setData(pageContent);
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	// 管理员--评论管理--会员评论
	@RequestMapping(value = "/getCommentsManagementOnMembersComments", method = RequestMethod.GET)
	@ResponseBody
	public String getCommentsManagementOnMembersComments(
			@RequestParam String starttime, @RequestParam String endtime,
			@RequestParam int sortType, @RequestParam int pageNumber,
			@RequestParam int pageSize) {
		log.info("execute AdministratorController--getCommentsManagementOnMembersComments");
		PageContent<MemberInfo> pageContent = new PageContent<MemberInfo>();
		pageContent = administratorService.getMembersBehavioursByCommentSpan(
				starttime, endtime, sortType, pageNumber, pageSize);
		resultInfo.setData(pageContent);
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	// 管理员--评论管理--商家评论---分层显示
	@RequestMapping(value = "/getCommentsManagementOnMerchantsComments", method = RequestMethod.GET)
	@ResponseBody
	public String getCommentsManagementOnMerchantsComments(
			@RequestParam String starttime, @RequestParam String endtime,
			@RequestParam int sortType, @RequestParam int pageNumber,
			@RequestParam int pageSize) {
		log.info("execute AdministratorController--getCommentsManagementOnMerchantsComments");
		PageContent<MerchantMark> pageContent = new PageContent<MerchantMark>();
		pageContent = administratorService.getStatisticalMarksByMerchant(
				starttime, endtime, sortType, pageNumber, pageSize);
		resultInfo.setData(pageContent);
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	// 管理员--用户管理--新增会员
	@RequestMapping(value = "/queryUserManagementOnNewAddedMembers", method = RequestMethod.GET)
	@ResponseBody
	public String queryUserManagementOnNewAddedMembers(
			@RequestParam String starttime, @RequestParam String endtime,
			@RequestParam int filter, @RequestParam int sortType,
			@RequestParam int pageNumber, @RequestParam int pageSize,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("execute AdministratorController--queryUserManagementOnNewAddedMembers");
		PageContent<Member> pageContent = new PageContent<Member>();
		pageContent = administratorService
				.queryUserManagementOnNewAddedMembers(starttime, endtime,
						filter, sortType, pageNumber, pageSize);
		resultInfo.setData(pageContent);
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	// 管理员--用户管理--审核商户
	@RequestMapping(value = "/queryUserManagementOnNotVerifiedMerchants", method = RequestMethod.POST)
	@ResponseBody
	public String queryUserManagementOnNotVerifiedMerchants(
			@RequestParam(value = "sortType", required = true, defaultValue = "1") int sortType,
			@RequestParam(value = "page", required = true, defaultValue = "1") int page,
			@RequestParam(value = "rows", required = true, defaultValue = "10") int rows,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("execute AdministratorController--queryUserManagementOnNotVerifiedMerchants");
		PageContent<DBObject> pageContent = new PageContent<DBObject>();
		pageContent = administratorService
				.queryUserManagementOnNotVerifiedMerchants(sortType, page, rows);
		if (pageContent != null) {
			resultInfo.setData(pageContent);
			resultInfo.setStatus(ToolConstants.ResultStatus_Success);
			resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		} else {
			resultInfo.setStatus(ToolConstants.ResultStatus_NoData);
			resultInfo.setDesc(ToolConstants.ResultDesc_NoData);
		}
		String json = gson.toJson(resultInfo);
		return json;
	}

	/**
	 * @author Kario 审核商家，
	 *
	 */
	@RequestMapping(value = "/VerifyMerchant", method = RequestMethod.GET)
	@ResponseBody
	public String VerifyMerchant(@RequestParam List<String> data,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("execute AdministratorController--VerifyMerchant");
		for (int i = 0; i < data.size(); i++) {
			String merchantName = data.get(i);
			if (VerifyUtil.polledMerchantNameMap.containsKey(merchantName)) {
				administratorService.validateMerchant(data.get(i));
				VerifyUtil.polledMerchantNameMap.remove(merchantName);
			}
		}
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	/**
	 * @author Kario 删除商家，
	 * @param data
	 *            为商家名
	 */
	@RequestMapping(value = "/DeleteMerchant", method = RequestMethod.GET)
	@ResponseBody
	public String DeleteMerchant(@RequestParam List<String> data,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("execute AdministratorController--DeleteMerchant");
		for (int i = 0; i < data.size(); i++) {
			administratorService.deleteMerchant(data.get(i));
		}
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	/**
	 * @author Kario 恢复商家，
	 * @param data
	 *            为商家名
	 */
	@RequestMapping(value = "/RestoreMerchant", method = RequestMethod.GET)
	@ResponseBody
	public String RestoreMerchant(@RequestParam List<String> data,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("execute AdministratorController--RestoreMerchant");
		for (int i = 0; i < data.size(); i++) {
			administratorService.restoreMerchant(data.get(i));
		}
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	// 管理员--用户管理--商户查询
	@RequestMapping(value = "/queryUserManagementOnMerchants", method = RequestMethod.GET)
	@ResponseBody
	public String queryUserManagementOnMerchants(@RequestParam int filter,
			@RequestParam int sortType, @RequestParam int pageNumber,
			@RequestParam int pageSize, HttpServletResponse response) {
		log.info("execute AdministratorController--queryUserManagementOnMerchants");
		response.setHeader("Access-Control-Allow-Origin", "*");
		PageContent<Merchant> pageContent = new PageContent<Merchant>();
		pageContent = administratorService.queryUserManagementOnMerchants(
				filter, sortType, pageNumber, pageSize);
		resultInfo.setData(pageContent);
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	// 管理员--用户管理--会员查询
	@RequestMapping(value = "/queryUserManagementOnMembers", method = RequestMethod.GET)
	@ResponseBody
	public String queryUserManagementOnMembers(@RequestParam int filter,
			@RequestParam int sortType, @RequestParam int pageNumber,
			@RequestParam int pageSize, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("execute AdministratorController--queryUserManagementOnMembers");
		PageContent<Member> pageContent = new PageContent<Member>();
		pageContent = administratorService
				.queryUserManagementOnNewAddedMembers("", "", filter, sortType,
						pageNumber, pageSize);
		resultInfo.setData(pageContent);
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	/**
	 * @author Kario 删除会员，
	 * @param data
	 *            为会员名
	 */
	@RequestMapping(value = "/DeleteMember", method = RequestMethod.GET)
	@ResponseBody
	public String DeleteMember(@RequestParam List<String> data,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("execute AdministratorController--DeleteMember");
		for (int i = 0; i < data.size(); i++) {
			administratorService.deleteMember(data.get(i));
		}
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	/**
	 * @author Kario 恢复会员，
	 * @param data
	 *            为会员名
	 */
	@RequestMapping(value = "/RestoreMember", method = RequestMethod.GET)
	@ResponseBody
	public String RestoreMember(@RequestParam List<String> data,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("execute AdministratorController--RestoreMember");
		for (int i = 0; i < data.size(); i++) {
			administratorService.restoreMember(data.get(i));
		}
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	// 管理员--历史管理--已删标记
	@RequestMapping(value = "/queryDeletedMarks", method = RequestMethod.POST)
	@ResponseBody
	public String queryDeletedMarks(
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam(value = "Name", required = false) String Name,
			@RequestParam(value = "sortType", required = true, defaultValue = "1") int sortType,
			@RequestParam(value = "page", required = true, defaultValue = "1") int page,
			@RequestParam(value = "rows", required = true, defaultValue = "10") int rows,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("execute AdministratorController--queryDeletedMarks");
		PageContent<MerchantMark> pageContent = new PageContent<MerchantMark>();
		pageContent = administratorService.queryDeletedMarks(startTime,
				endTime, Name, sortType, page, rows);
		resultInfo.setData(pageContent);
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	@RequestMapping(value = "/RestoreMark", method = RequestMethod.GET)
	@ResponseBody
	public String RestoreMark(@RequestParam List<String> data,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("execute AdministratorController--RestoreMark");
		for (int i = 0; i < data.size(); i++) {
			administratorService.restoreMarkInfo(data.get(i));
		}
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	// 管理员--历史管理--已删评价
	@RequestMapping(value = "/queryDeletedComments", method = RequestMethod.GET)
	@ResponseBody
	public String queryDeletedComments(@RequestParam int sortType,
			@RequestParam int pageNumber, @RequestParam int pageSize,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("execute AdministratorController--queryDeletedComments");
		PageContent<MemberComment> pageContent = new PageContent<MemberComment>();
		pageContent = administratorService.queryDeletedComments(sortType,
				pageNumber, pageSize);
		resultInfo.setData(pageContent);
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	// 管理员--历史管理--已删会员
	@RequestMapping(value = "/queryDeletedMembers", method = RequestMethod.POST)
	@ResponseBody
	public String queryDeletedMembers(
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam(value = "Name", required = false) String Name,
			@RequestParam(value = "sortType", required = true, defaultValue = "1") int sortType,
			@RequestParam(value = "page", required = true, defaultValue = "1") int page,
			@RequestParam(value = "rows", required = true, defaultValue = "10") int rows,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("execute AdministratorController--queryDeletedMembers");
		PageContent<MemberInfo> pageContent = new PageContent<MemberInfo>();
		pageContent = administratorService.queryDeletedMembers(startTime,
				endTime, Name, sortType, page, rows);
		resultInfo.setData(pageContent);
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	// 管理员--历史管理--已删商家
	@RequestMapping(value = "/queryDeletedMerchants", method = RequestMethod.POST)
	@ResponseBody
	public String queryDeletedMerchants(
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam(value = "Name", required = false) String Name,
			@RequestParam(value = "sortType", required = true, defaultValue = "1") int sortType,
			@RequestParam(value = "page", required = true, defaultValue = "1") int page,
			@RequestParam(value = "rows", required = true, defaultValue = "10") int rows,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("execute AdministratorController--queryDeletedMerchants");
		PageContent<MerchantInfo> pageContent = new PageContent<MerchantInfo>();
		pageContent = administratorService.queryDeletedMerchants(startTime,
				endTime, Name, sortType, page, rows);
		resultInfo.setData(pageContent);
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	@RequestMapping(value = "/checkPassword", method = RequestMethod.GET)
	@ResponseBody
	public int checkPassword(@RequestParam String Name,
			@RequestParam String oldPassword) {
		return administratorService.checkPassword(Name,
				CipherUtil.generatePassword(oldPassword));
	}

	@RequestMapping(value = "/updatePassword", method = RequestMethod.GET)
	@ResponseBody
	public int updatePassword(@RequestParam String Name,
			@RequestParam String oldPassword, @RequestParam String newPassword) {
		return administratorService.updatePassword(Name,
				CipherUtil.generatePassword(oldPassword),
				CipherUtil.generatePassword(newPassword));
	}

	@RequestMapping(value = "/getTextComment", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String getTextComment(@RequestParam String mark_id,
			@RequestParam int page, @RequestParam int pageSize,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		PageContent<DBObject> pageContent = administratorService
				.getTextComment(mark_id, page, pageSize);
		if (pageContent != null) {
			resultInfo.setStatus(ToolConstants.ResultStatus_Success);
			resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		} else {
			resultInfo.setStatus(ToolConstants.ResultStatus_NoData);
			resultInfo.setDesc(ToolConstants.ResultDesc_NoData);
		}
		resultInfo.setData(pageContent);
		return gson.toJson(resultInfo);
	}

	//*************************************KG********************************
	
	//String active_url = administratorService.generateActiveLink("http://localhost:8080/imagemark/register_active.do", merchant.getMerchantName(), merchant.getRandomCode());
	//sendMail.sendModelMail(merchant.getMerchantMail(), "imagemark 邮件激活", "register.vm",active_url, merchant.getMerchantName());
	//System.out.println("*******************MerchantController(232)*****************");
	
	//注册,邮箱激活
	@RequestMapping(value = "/register_active.do")
	public String  register_active(@RequestParam String username,@RequestParam String checkCode,Model model) {
		System.out.println("Administrator(1481)"+username);
		//model.addAttribute("username", username);
		System.out.println("********AdministratorController1483********checkCode="+checkCode);
		if(administratorService.isMailValid(username, checkCode)){
			Merchant merchant = merchantService.findMerchantByName(username);
			if(merchant!=null){
				System.out.println("*************AdministratorController1486 ***********");
				administratorService.activeAdministrator(username,ToolConstants.VERIFY_INT);
				merchantService.activeMerchant(username);
				VerifyUtil.verifiedMerchantNameList.addFirst(merchant.getMerchantName());
				log.info("Add to verifiedMerchantNameList with merchantId: "+ merchant.getMerchantName()+"successfully!");
				return "/register/merchant_success";
			}else{
				System.out.println("*************AdministratorController1493 ***********");
				administratorService.activeAdministrator(username,ToolConstants.VALID_INT);
				memberService.activeMember(username);
				return "/register/member_success";
			}
			
		}
			return "/register/fail";
	}
	
	//发送找回密码邮件
	@RequestMapping(value = "/pwd_find.do")
	@ResponseBody
	public String pwd_find(@RequestParam String username) {
		Administrator administrator =administratorService.getAdministratorByNameOrMail(username);
		if(administrator==null){
			resultInfo.setStatus(0);
			return gson.toJson(resultInfo);			 
		}else{
			resultInfo.setDesc(administrator.getAdminMail());
			resultInfo.setStatus(1);
			String active_url = administratorService.generateActiveLink("http://222.214.218.140:90/imagemark/administrator/mail_varify.do", administrator.getAdminName(), administrator.getRandomCode());
			sendMail.sendModelMail(administrator.getAdminMail(), "imagemark 找回密码", "pwd_find.vm",active_url, administrator.getAdminName());
			return gson.toJson(resultInfo);
		}
	}
	
	//找回密码-判断邮件链接是否有效
	@RequestMapping(value = "/mail_varify.do")
	public String  mail_varify(@RequestParam String username,@RequestParam String checkCode,Model model) {
		System.out.println("Administrator(1519)"+username);
		model.addAttribute("username", username);
		if(administratorService.isMailValid(username, checkCode)){
			return "/forgot/pwd_reset";
		}
			return "/forgot/pwd_reset_fail";
	}
	
	//修改密码
	@RequestMapping(value = "/pwd_reset.do")
	public String pwd_reset(@RequestParam String username,@RequestParam String password) {
		Merchant merchant = merchantService.findMerchantByName(username);
		//Member member = memberService.findMemberByName(username);
		
		if(merchant!=null){
			if(administratorService.changepasswd(username,password)&&merchantService.changepasswd(username, password)){
				return "/forgot/pwd_find_success";
			}else{
				return "/forgot/pwd_find_fail";
			}
		}else{
			if(administratorService.changepasswd(username,password)&&memberService.changepasswd(username, password)){
				return "/forgot/pwd_find_success";
			}else{
				return "/forgot/pwd_find_fail";
			}
		}
	}
	
	
	//插件单击忘记密码,找回密码
	//修改密码
	@RequestMapping(value = "/forgot_pwd.do")
	public String forgot_pwd() {
			return "/forgot/pwd_find";
	}
	//*************************************KG********************************
	
}
