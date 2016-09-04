package com.anyway.imagemark.controller;

import java.awt.image.ImagingOpException;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.anyway.imagemark.domain.ClickInfo;
import com.anyway.imagemark.domain.Comment;
import com.anyway.imagemark.domain.Complain;
import com.anyway.imagemark.domain.MarkInfo;
import com.anyway.imagemark.domain.Member;
import com.anyway.imagemark.domain.Notification;
import com.anyway.imagemark.image.gridFS;
import com.anyway.imagemark.mail.SendMail;
import com.anyway.imagemark.manage.CommentManage;
import com.anyway.imagemark.service.AdministratorService;
import com.anyway.imagemark.service.MemberService;
import com.anyway.imagemark.service.MerchantService;
import com.anyway.imagemark.utils.CipherUtil;
import com.anyway.imagemark.utils.PageContent;
import com.anyway.imagemark.utils.ResultInfo;
import com.anyway.imagemark.utils.ToolConstants;
import com.anyway.imagemark.webDomain.MemberComment;
import com.anyway.imagemark.webDomain.MemberFoot;
import com.anyway.imagemark.webDomain.MemberInfo;
import com.anyway.imagemark.webDomain.MerchantMark;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@Controller
@RequestMapping(value = "/member")
public class MemberController {
	private static Logger log = Logger.getLogger(MemberController.class);
	@Autowired
	private MemberService memberService;
	@Autowired
	private AdministratorService adminService;
	@Autowired
	private MerchantService merchantServic;
	@Autowired
	private ResultInfo resultInfo;
	// @Autowired
	private PageContent<DBObject> pageContent = new PageContent<DBObject>();
	@Autowired
	private CommentManage commentManage;
	
	//***************************************KG*********************************
	@Autowired
	private SendMail sendMail;
	//***************************************KG*********************************
	
	private List<Integer> urlListWithType;
	private Gson gson = new Gson();

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	@ResponseBody
	public String login(@RequestParam String memberName,
			@RequestParam String memberPassword, @RequestParam int type,
			HttpServletRequest request, HttpServletResponse response) {
		log.info("execute MemberController--login");
		response.setHeader("Access-Control-Allow-Origin", "*");
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
		// String password = CipherUtil.generatePassword(memberPassword);
		int feedback = memberService
				.login(memberName, memberPassword, type, ip);
		if (feedback == ToolConstants.ResultStatus_Success) {
			Member member = memberService.queryMemberByNameOrMail(memberName);
			resultInfo.setData(member);
			resultInfo.setStatus(ToolConstants.ResultStatus_Success);
			resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		} else if (feedback == ToolConstants.ResultStatus_Fail_Password) {
			resultInfo.setStatus(ToolConstants.ResultStatus_Fail_Password);
			resultInfo.setDesc(ToolConstants.PASSWORDFAILED_STRING);
		} else {
			resultInfo.setStatus(ToolConstants.ResultStatus_Fail_NameNotExist);
			resultInfo.setDesc(ToolConstants.REGISTERNOTDONE_STRING);
		}
		String json = gson.toJson(resultInfo);
		return json;
	}

	@RequestMapping(value = "/welcome", method = RequestMethod.GET)
	public String showHelloPage(Model model, Principal principal) {
		log.info("execute MemberController--showHelloPage");
		String name = principal.getName();
		model.addAttribute("username", name);
		model.addAttribute("message", "Spring Security Custom Form example");
		return "member/home";
	}

	@RequestMapping(value = "/queryMarkInfoByUrl", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String queryMarkInfoByUrl(@RequestParam String memberName,
			@RequestParam String type, @RequestParam String url,
			@RequestParam int componentType, @RequestParam int pageNumber,
			@RequestParam int sortType, HttpServletResponse response) {
		log.info("execute MemberController--queryMarkInfoByUrl");
		response.setHeader("Access-Control-Allow-Origin", "*");
		DBObject query = new BasicDBObject();
		query.put("status", ToolConstants.VALID_INT);
		log.info("the param values__" + "memberName: " + memberName + "url: "
				+ url + "componentType: " + componentType + "pageNumber: "
				+ pageNumber + "sortType: " + sortType);
		String shortUrl;
		if (url.contains("?")) {
			shortUrl = url.substring(0, url.indexOf("?"));
		} else {
			shortUrl = url;
		}
		query.put("Url", shortUrl);
		if (componentType != 0) {
			query.put("componentType", componentType);
		}
		DBObject sortOrder = new BasicDBObject();
		switch (sortType) {
		case 1:
			sortOrder.put("componentPrice", 1);
			break;
		case 2:
			sortOrder.put("componentPrice", -1);
			break;
		case 3:
			sortOrder.put("componentTrust", -1);
			break;
		case 4:
			sortOrder.put("markDate", -1);
			break;
		default:
			sortOrder = null;
			System.out.println("have access to sortType!");
			break;
		}
		if (type.equals("1"))
			pageContent = memberService.SearchMarkByUrlAndCommented(memberName,
					query, sortOrder, pageNumber, 3);
		else
			pageContent = memberService.queryMarkInfoByUrl(query, sortOrder,
					pageNumber, 3);
		log.info(pageContent.getList().get(0).toString());
		if (pageContent == null) {
			resultInfo.setStatus(ToolConstants.ResultStatus_NoData);
			resultInfo.setDesc(ToolConstants.ResultDesc_NoData);
		} else {
			System.out.println("retrieved data!");
			resultInfo.setData(pageContent);
			resultInfo.setStatus(ToolConstants.ResultStatus_Success);
			resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		}
		String json = gson.toJson(resultInfo);
		return json;
	}

	@RequestMapping(value = "/queryMarkInfoByNode", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String queryMarkInfoByNode(
			@RequestParam(value = "memberName", required = true, defaultValue = "") String memberName,
			@RequestParam String node, @RequestParam int page,
			@RequestParam int sortType, HttpServletResponse response) {
		log.info("execute MemberController--queryMarkInfoByNode");
		response.setHeader("Access-Control-Allow-Origin", "*");
		DBObject query = new BasicDBObject();
		query.put("node_id", node);
		query.put("status", ToolConstants.VALID_INT);
		// Pattern pattern= Pattern.compile(shortUrl, Pattern.CASE_INSENSITIVE);
		DBObject sortOrder = new BasicDBObject();
		switch (sortType) {
		case 1:
			sortOrder.put("componentPrice", 1);
			break;
		case 2:
			sortOrder.put("componentPrice", -1);
			break;
		case 3:
			sortOrder.put("componentTrust", -1);
			break;
		case 4:
			sortOrder.put("markDate", -1);
			break;
		case 5:
			sortOrder.put("praise", -1);
		default:
			sortOrder = null;
			System.out.println("have access to sortType!");
			break;
		}
		pageContent = memberService.SearchMarkByUrlAndCommented(memberName,
				query, sortOrder, page,
				ToolConstants.ForeGround_Default_PageSize);
		log.info(pageContent.getList().get(0).toString());
		if (pageContent == null) {
			resultInfo.setStatus(ToolConstants.ResultStatus_NoData);
			resultInfo.setDesc(ToolConstants.ResultDesc_NoData);
		} else {
			System.out.println("retrieved data!");
			resultInfo.setData(pageContent);
			resultInfo.setStatus(ToolConstants.ResultStatus_Success);
			resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		}
		String json = gson.toJson(resultInfo);
		return json;
	}

	@RequestMapping(value = "/queryMarked", method = RequestMethod.GET)
	@ResponseBody
	public String queryMarked(@RequestParam String memberName,
			@RequestParam List<String> images, HttpServletResponse response) {
		log.info("execute MemberController--queryMarked");
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("have access to queryMarked");
		if (memberName.equals("anonymity")) {
			urlListWithType = memberService.queryMarked(images);
			System.out.println("memberName=anonymity");
		}
		resultInfo.setData(urlListWithType);
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	@RequestMapping(value = "/addMember", method = RequestMethod.POST)
	@ResponseBody
	public String addMemberFromForm(
			@Valid Member member,
			BindingResult bindingResult,
			@RequestParam int width,
			@RequestParam int height,
			@RequestParam int previewWidth,
			@RequestParam int previewHeight,
			@RequestParam int cropWidth,
			@RequestParam int cropHeight,
			@RequestParam int cropX,
			@RequestParam int cropY,
			@RequestParam(value = "image", required = false) MultipartFile image,
			HttpServletResponse response) {
		log.info("execute MemberController--addMember");
		response.setHeader("Access-Control-Allow-Origin", "*");
		if (bindingResult.hasErrors()) {
			resultInfo.setStatus(ToolConstants.ResultStatus_Fail_DataIllegal);
			resultInfo.setDesc(ToolConstants.ResultDesc_Fail_DataIllegal);
			String json = gson.toJson(resultInfo);
			return json;
		}
		//encode the password
		/*if (member.getMemberPassword() != null
				&& !member.getMemberPassword().isEmpty()) {
			String password = member.getMemberPassword();
			member.setMemberPassword(CipherUtil.generatePassword(password));
		}*/
		String idString = (new ObjectId()).toString();
		member.set_id(idString);
		if (image != null && !image.isEmpty()) {
			log.info("picture store: " + idString);
			int cx = (int) (cropX * ((float) (width) / (float) (previewWidth)));
			int cy = (int) (cropY * ((float) (height) / (float) (previewHeight)));
			int cw = (int) (cropWidth * ((float) (width) / (float) (previewWidth)));
			int ch = (int) (cropHeight * ((float) (height) / (float) (previewHeight)));
			try {
				log.info("access to if image!=null");
				validateImage(image);
				System.out.println("accessing to saveIMage!");
				saveImage(member.get_id() + ".png", image, cx, cy, cw, ch);
				System.out.println("saveIMage successfully!");
				member.setAvalPic(avalPicPrefix + idString + "." + "png");
			} catch (ImagingOpException e) {
				// TODO: handle exception
				member.setAvalPic(defaultPic);
				e.printStackTrace();
			}
		} else {
			// image null
			member.setAvalPic(defaultPic);
		}
		//***************************************************KG*******************************************
		String randomCode = RandomStringUtils.randomAlphanumeric(30);
		String active_url = adminService.generateActiveLink("http://222.214.218.140:90/imagemark/administrator/register_active.do", member.getMemberName(),randomCode);
		int feedback = memberService.addMember(member,active_url,sendMail,randomCode);
		if (feedback == ToolConstants.ResultStatus_Success) {	
			//sendMail.sendModelMail(member.getMemberMail(), "imagemark 邮件激活", "register.vm",active_url, member.getMemberName());
			//System.out.println("*******************MemberController(318)*****************");
			//***************************************************KG*******************************************				
			resultInfo.setStatus(ToolConstants.ResultStatus_Success);
			resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		} else if (feedback == ToolConstants.ResultStatus_Fail_MailUsed) {
			resultInfo.setStatus(ToolConstants.ResultStatus_Fail_MailUsed);
			resultInfo.setDesc(ToolConstants.MAILUSED_STRING);
		} else if (feedback == ToolConstants.ResultStatus_Fail_NameUsed) {
			resultInfo.setStatus(ToolConstants.ResultStatus_Fail_NameUsed);
			resultInfo.setDesc(ToolConstants.NAMEUSED_STRING);
		} 
		//*******************************************KG********************************************************
		else if (feedback == ToolConstants.ResultStatus_Fail_MailNOTEXIST) {
			resultInfo.setStatus(ToolConstants.ResultStatus_Fail_MailNOTEXIST);
			resultInfo.setDesc(ToolConstants.MAILNOTEXIST_STRING);
		} 
		//*******************************************KG********************************************************
		else {
			resultInfo.setStatus(ToolConstants.ResultStatus_Fail_MailAndNameUsed);
			resultInfo.setDesc(ToolConstants.MAILUSED_STRING+ ToolConstants.NAMEUSED_STRING);
		}
		String json = gson.toJson(resultInfo);
		return json;
	}

	@RequestMapping(value = "/updateMember", method = RequestMethod.POST)
	@ResponseBody
	public String updateMember(@Valid Member member,
			BindingResult bindingResult, HttpServletResponse response) {
		log.info("execute MemberController--updateMember");
		response.setHeader("Access-Control-Allow-Origin", "*");
		if (bindingResult.hasErrors()) {
			resultInfo.setStatus(ToolConstants.ResultStatus_Fail_DataIllegal);
			resultInfo.setDesc(ToolConstants.ResultDesc_Fail_DataIllegal);
			String json = gson.toJson(resultInfo);
			return json;
		}
		DBObject condition = new BasicDBObject();
		condition.put("memberName", member.getMemberName());
		memberService.updateMember(condition, member);
		pageContent = null;
		resultInfo.setData(pageContent);
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	/*
	 * @RequestMapping(value = "/saveComment", method = RequestMethod.GET)
	 * 
	 * @ResponseBody public String saveComment(@RequestParam String memberName,
	 * 
	 * @RequestParam String mark_id, @RequestParam int comment,
	 * HttpServletResponse response) {
	 * log.info("execute MemberController--saveComment, memberName:" +
	 * memberName); response.setHeader("Access-Control-Allow-Origin", "*");
	 * Comment commentinfo = new Comment();
	 * commentinfo.setMem_id(memberService.getIdByMemberName(memberName));
	 * commentinfo.setMark_id(mark_id); commentinfo.setComment(comment);
	 * commentinfo.setCriticTime(System.currentTimeMillis()); int feedback =
	 * memberService.saveComment(commentinfo); if (feedback ==
	 * ToolConstants.ResultStatus_Success) { MarkInfo markInfo =
	 * memberService.queryMarkInfoByMarkId(commentinfo .getMark_id());
	 * resultInfo.setData(markInfo.getComponentTrust());
	 * resultInfo.setStatus(ToolConstants.ResultStatus_Success);
	 * resultInfo.setDesc(ToolConstants.SUCCES_STRING); } else {
	 * resultInfo.setStatus(ToolConstants.ResultStatus_Fail_ExistedError);
	 * resultInfo.setDesc(ToolConstants.ResultDesc_Fail_ExistedError); } String
	 * json = gson.toJson(resultInfo); return json; }
	 */
	// 点赞
	@RequestMapping(value = "/savePraise", method = RequestMethod.GET)
	@ResponseBody
	public String savePraise(@RequestParam String memberName,
			@RequestParam String mark_id, HttpServletResponse response) {
		log.info("execute MemberController--saveComment, memberName:"
				+ memberName);
		response.setHeader("Access-Control-Allow-Origin", "*");
		Comment commentinfo = new Comment();
		commentinfo.setMem_id(memberService.getIdByMemberName(memberName));
		commentinfo.setMark_id(mark_id);
		commentinfo.setPhraise(true);
		int feedback = memberService.saveComment(commentinfo);
		if (feedback == ToolConstants.ResultStatus_Success) {
			MarkInfo markInfo = memberService.queryMarkInfoByMarkId(commentinfo
					.getMark_id());
			resultInfo.setData(markInfo.getPhraise());
			resultInfo.setStatus(ToolConstants.ResultStatus_Success);
			resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		} else {
			resultInfo.setStatus(ToolConstants.ResultStatus_Fail_ExistedError);
			resultInfo.setDesc(ToolConstants.ResultDesc_Fail_ExistedError);
		}
		String json = gson.toJson(resultInfo);
		return json;
	}

	// 评论
	@RequestMapping(value = "/saveTextComment", method = RequestMethod.POST)
	@ResponseBody
	public String saveTextComment(@RequestParam String memberName,
			@RequestParam String mark_id, @RequestParam String textComment,
			HttpServletResponse response) {
		log.info("execute MemberController--saveComment, memberName:"
				+ memberName);
		log.info("textComment " + textComment);
		response.setHeader("Access-Control-Allow-Origin", "*");
		Comment commentinfo = new Comment();
		commentinfo.setMem_id(memberService.getIdByMemberName(memberName));
		commentinfo.setMark_id(mark_id);
		commentinfo.setTextComment(textComment);
		commentinfo.setTime(System.currentTimeMillis());
		int feedback = memberService.saveComment(commentinfo);
		if (feedback == ToolConstants.ResultStatus_Success) {
			PageContent<DBObject> textContent = adminService.getTextComment(
					mark_id, 1, 3);
			resultInfo.setData(textContent);
			resultInfo.setStatus(ToolConstants.ResultStatus_Success);
			resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		} else {
			resultInfo.setStatus(ToolConstants.ResultStatus_Fail_ExistedError);
			resultInfo.setDesc(ToolConstants.ResultDesc_Fail_ExistedError);
		}
		String json = gson.toJson(resultInfo);
		return json;
	}

	@RequestMapping(value = "/saveComplain", method = RequestMethod.GET)
	@ResponseBody
	public String saveComplain(@RequestParam String memberName,
			@RequestParam String mem_id, @RequestParam String mark_id,
			@RequestParam int complainType, HttpServletResponse response) {
		log.info("execute MemberController--saveComplian: memberName:"
				+ memberName);
		response.setHeader("Access-Control-Allow-Origin", "*");
		Complain complain = new Complain();
		complain.setComplainer(memberName);
		complain.setComplainReason(complainType);
		complain.setMem_id(mem_id);
		complain.setMark_id(mark_id);
		int feedback = memberService.saveComplain(complain);
		if (feedback == ToolConstants.ResultStatus_Success) {
			resultInfo.setStatus(ToolConstants.ResultStatus_Success);
			resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		} else {
			resultInfo.setStatus(ToolConstants.ResultStatus_Fail_ExistedError);
			resultInfo.setDesc(ToolConstants.ResultDesc_Fail_ExistedError);
		}
		String json = gson.toJson(resultInfo);
		return json;
	}

	@RequestMapping(value = "/deleteComment", method = RequestMethod.GET)
	@ResponseBody
	public String deleteComment(@RequestParam String mem_id,
			@RequestParam String mark_id, HttpServletResponse response) {
		log.info("execute MemberController--deleteComment");
		response.setHeader("Access-Control-Allow-Origin", "*");
		DBObject condition = new BasicDBObject();
		condition.put("mem_id", mem_id);
		condition.put("mark_id", mark_id);
		memberService.deleteComment(condition);
		pageContent = null;
		resultInfo.setData(pageContent);
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	@RequestMapping(value = "/updateComment", method = RequestMethod.GET)
	@ResponseBody
	public String updateComment(@RequestParam String mem_id,
			@RequestParam String mark_id, @RequestParam int score,
			HttpServletResponse response) {
		log.info("execute MemberController--updateComment");
		response.setHeader("Access-Control-Allow-Origin", "*");
		Comment comment = new Comment();
		comment.setMem_id(mem_id);
		comment.setMark_id(mark_id);
		DBObject condition = new BasicDBObject();
		condition.put("mem_id", mem_id);
		condition.put("mark_id", mark_id);
		memberService.updateComment(condition, comment);
		pageContent = null;
		resultInfo.setData(pageContent);
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	@RequestMapping(value = "/saveClickInfo", method = RequestMethod.GET)
	@ResponseBody
	public String saveClickInfo(@RequestParam String mem_id,
			@RequestParam String domain, @RequestParam String mark_id,@RequestParam String mer_id,
			HttpServletRequest request, HttpServletResponse response) {
		log.info("execute MemberController--saveClickInfo");
		response.setHeader("Access-Control-Allow-Origin", "*");
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
		ClickInfo clickInfo = new ClickInfo();
		clickInfo.setMem_id(mem_id);
		clickInfo.setDomain(domain);
		clickInfo.setMark_id(mark_id);
		clickInfo.setMer_id(mer_id);
		clickInfo.setDate(System.currentTimeMillis());
		memberService.saveClickInfo(clickInfo);
		pageContent = null;
		resultInfo.setData(pageContent);
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	/**
	 * @author Kario 拉取自身相关的信息
	 * @param mem_id
	 *            会员的id
	 */
	@RequestMapping(value = "/queryNotification", method = RequestMethod.POST)
	@ResponseBody
	public String queryNotification(
			@RequestParam String memberName,
			@RequestParam(value = "type", required = true, defaultValue = "1") int type,
			@RequestParam(value = "sortType", required = true, defaultValue = "1") int sortType,
			@RequestParam(value = "page", required = true, defaultValue = "1") int page,
			@RequestParam(value = "rows", required = true, defaultValue = "10") int rows,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		PageContent<Notification> pageContent = memberService
				.queryNotificationsByType(memberName, type, sortType, page,
						rows);
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

	@RequestMapping(value = "/notificationIndex", method = RequestMethod.GET)
	public String notificationIndex(Model model, Principal principal,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		PageContent<Notification> notificationPage = memberService
				.queryNotificationsByType(principal.getName(),
						ToolConstants.Notification_Type_Notice,
						ToolConstants.TIMEASC, ToolConstants.PageNumber_first,
						ToolConstants.BackGround_Default_PageSize);
		model.addAttribute("notificationPage", notificationPage);
		return "member/notice";
	}

	// 统计用户自己做出的评论信息--用户id
	@RequestMapping(value = "/getStatisticalCommentsByMemberId", method = RequestMethod.GET)
	@ResponseBody
	public String getStatisticalCommentsByMemberId(
			@RequestParam String memberName, @RequestParam int sortType,
			@RequestParam int pageNumber, HttpServletResponse response) {
		log.info("execute MemberController--getStatisticalCommentsByMemberId");
		response.setHeader("Access-Control-Allow-Origin", "*");
		String mem_id = null;
		if (memberName != null) {
			mem_id = memberService.getIdByMemberName(memberName);
		}
		PageContent<MemberComment> pageContent = new PageContent<MemberComment>();
		pageContent.setPageSize(ToolConstants.BackGround_Default_PageSize);
		pageContent.setPageNumber(pageNumber);
		pageContent.setList(commentManage.getComment(mem_id));
		resultInfo.setData(pageContent);
		resultInfo.setStatus(ToolConstants.ResultStatus_Success);
		resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		String json = gson.toJson(resultInfo);
		return json;
	}

	// 会员--我的信息
	@RequestMapping(value = "/queryMemberInfoByMemberName", method = RequestMethod.GET)
	@ResponseBody
	public String queryMemberInfoByMemberName(@RequestParam String memberName,
			HttpServletResponse response) {
		log.info("execute MemberController--queryMemberInfoByMemberName");
		response.setHeader("Access-Control-Allow-Origin", "*");
		if (memberName.isEmpty() || memberName == null) {
			log.info("execute getMemberBehaviours memberName : null or empty!");
			resultInfo
					.setStatus(ToolConstants.ResultStatus_Fail_ParameterError);
			resultInfo.setDesc(ToolConstants.ResultDesc_Fail_ParameterError);
		} else {
			log.info("execute getMemberBehaviours memberName : " + memberName);
			resultInfo.setData(memberService
					.queryMemberInfoByMemberName(memberName));
			resultInfo.setStatus(ToolConstants.ResultStatus_Success);
			resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		}
		String json = gson.toJson(resultInfo);
		return json;
	}

	// 会员--我的足迹
	@RequestMapping(value = "/getMemberStatisticalClicks", method = RequestMethod.POST)
	@ResponseBody
	public String getMemberStatisticalClicks(
			@RequestParam String memberName,
			@RequestParam int sortField,
			@RequestParam int sortType,
			@RequestParam(value = "page", required = true, defaultValue = "1") int page,
			@RequestParam(value = "rows", required = true, defaultValue = "10") int rows,
			HttpServletResponse response) {
		log.info("execute MemberController--getMemberStatisticalClicks");
		response.setHeader("Access-Control-Allow-Origin", "*");
		PageContent<MemberFoot> pageContent = new PageContent<MemberFoot>();
		pageContent = memberService.getMemberStatisticalClicks(memberName,
				sortField, sortType, page, rows);
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

	// 会员--我的评价
	@RequestMapping(value = "/getMemberStatisticalComments", method = RequestMethod.POST)
	@ResponseBody
	public String getMemberStatisticalComments(
			@RequestParam String memberName,
			@RequestParam(value = "filter", required = true, defaultValue = "0") int filter,
			@RequestParam(value = "sortType", required = true, defaultValue = "1") int sortType,
			@RequestParam(value = "page", required = true, defaultValue = "1") int page,
			@RequestParam(value = "rows", required = true, defaultValue = "10") int rows,
			HttpServletResponse response) {
		log.info("execute MemberController--getMemberStatisticalComments");
		response.setHeader("Access-Control-Allow-Origin", "*");
		PageContent<MemberComment> pageContent = new PageContent<MemberComment>();
		pageContent = memberService.getMemberStatisticalComments(memberName,
				filter, sortType, page, rows);
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

	// 会员--我的资料
	@RequestMapping(value = "/queryMemberByMemberName", method = RequestMethod.GET)
	public String queryMemberByMemberName(Model model, Principal principal,
			HttpServletResponse response) {
		log.info("execute MemberController--queryMemberByMemberName");
		response.setHeader("Access-Control-Allow-Origin", "*");
		String memberName = principal.getName();
		if (memberName.isEmpty() || memberName == null) {
			log.info("execute queryMemberByMemberName memberName : null or empty!");
			/*
			 * resultInfo
			 * .setStatus(ToolConstants.ResultStatus_Fail_ParameterError);
			 * resultInfo.setDesc(ToolConstants.ResultDesc_Fail_ParameterError);
			 */
		} else {
			log.info("execute queryMemberByMemberName memberName : "
					+ memberName);
			MemberInfo memberInfo = memberService
					.queryMemberInfoByMemberName(memberName);
			/*
			 * resultInfo.setData(mem);
			 * resultInfo.setStatus(ToolConstants.ResultStatus_Success);
			 * resultInfo.setDesc(ToolConstants.SUCCES_STRING);
			 */
			model.addAttribute("memberInfo", memberInfo);
		}
		return "member/memberInfo";
	}

	@RequestMapping(value = "/getAllNodes", method = RequestMethod.POST)
	@ResponseBody
	public String getAllNodes(@RequestParam String urlList,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("execute MerchantController--getAllNodes");
		String[] urlArr = urlList.split(";");
		List<List<DBObject>> nodeList = merchantServic.getAllNode(urlArr);
		log.info(gson.toJson(urlList));
		log.info(gson.toJson(nodeList));
		return gson.toJson(nodeList);
	}

	@RequestMapping(value = "/getRecommend", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String getRecommend(HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("execute memberService--getRecommend");
		return gson.toJson(memberService.getRecommend());
	}

	@RequestMapping(value = "/getUserPrase", method = RequestMethod.POST)
	@ResponseBody
	public String getUserPrase(HttpServletResponse response,
			@RequestParam String userName, @RequestParam int page,
			@RequestParam int pageSize, @RequestParam int flag) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// flag=1 praise flag=2 collected
		log.info("execute memberService--getRecommend");
		return gson.toJson(memberService.getUserPrased(userName, page,
				pageSize, flag));
	}

	private void validateImage(MultipartFile image) {
		if (!image.getContentType().equals("image/jpeg")) {
			throw new ImagingOpException("Only JPG images accepted");
		}
		System.out.println("validate successfully!");
	}

	private void saveImage(String filename, MultipartFile image, int x, int y,
			int width, int height) throws ImagingOpException {
		log.info("save image process...");
		try {
			log.info("new gridFs");
			gridFS fs = new gridFS();
			log.info("start save");
			fs.saveImage(image, filename, x, y, width, height);
			System.out.println("picture upload");
		} catch (Exception e) {
			throw new ImagingOpException("Unable to save image");
		}
	}

	@RequestMapping(value = "/getMarkInfosByMerchant", method = RequestMethod.POST)
	@ResponseBody
	public String getMarkInfosByMerchant(@RequestParam String mer_id,
			@RequestParam int page, @RequestParam int pagesize,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("execute memberService--getMarkInfosByMerchant");
		PageContent<MerchantMark> pageContent = merchantServic
				.getMarkInfoByMerchant(mer_id, page, pagesize);
		resultInfo.setData(pageContent);
		return gson.toJson(resultInfo);
	}

	@RequestMapping(value = "/getMerchantInfo", method = RequestMethod.POST)
	@ResponseBody
	public String getMerchantInfo(@RequestParam String mer_id,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		log.info("execute memberService--getMerchantInfo");
		DBObject object = merchantServic.getMerchantInfo(mer_id);
		if (object != null) {
			resultInfo.setData(object);
			resultInfo.setStatus(ToolConstants.ResultStatus_Success);
			resultInfo.setDesc(ToolConstants.SUCCES_STRING);
		} else {
			resultInfo.setStatus(ToolConstants.ResultStatus_NoData);
			resultInfo.setDesc(ToolConstants.ResultDesc_NoData);
		}
		return gson.toJson(resultInfo);
	}

	private static String avalPicPrefix = "http://45.79.69.182/pics/";
	private static String defaultPic = "http://45.79.69.182/imagemark/images/avatar_default.jpg";
}
