<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<link href="styles/main.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="/imagemark/js/jquery.min.js"></script>
	<script type="text/javascript" src="/imagemark/js/md5.js"></script>
</head>
<body>
<!-- 表单没有指定URL情况下,将被提交到/administrator -->
<%-- <sf:form method="post" modelAttribute="administrator">   --%>
<s:url var="authUrl" value="../administrator/pwd_reset.do"></s:url>
<%-- <c:if test="${not empty param.error}">
	<font color="red">Login error.<br />  
    </font>  
    Reason:${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
</c:if>
<c:if test="${not empty param.out}">
    <div>You've logged out successfully.</div>
</c:if>
<c:if test="${not empty param.time}">
	<div>You've been logged out due to inactivity.</div>
</c:if> --%>
<sf:form method="post" class="signin" action="${authUrl }">
<fieldset>
<table cellspacing="0">  
	<tr>
		<th><label for="password">重置密码：</label></th>  
		<td><%-- <sf:input path="adminName" size="20" maxlength="20"/>
			<small id="adminName_msg">No spaces,please.</small>
			<sf:errors path="adminName" delimiter=", " cssClass="error"/> --%>
			<input id="password" name="password" type="password"/>
		</td>  
		<td><div id="message"  style="border:1px  red;width:200px;height:25px;color: red"></div></td>
	</tr>  
	<tr>
		<th><label for="re_password">确认密码：</label></th>  
		<td><%-- <sf:password path="adminPassword" size="30" showPassword="false"/>
			<small>6 characters or more (be tricky!)</small>
			<sf:errors path="adminPassword" delimiter=", " cssClass="error"></sf:errors> --%>
			<input id="re_password" type="password"/>		
		</td>  
		<td><div id="re_message"  style="border:1px  red;width:200px;height:25px;color: red"></div></td>
		
	</tr>  
	<tr>
		<td></td>
		<%--<td><input type=submit value="确认">--%>
		<td><input type="button" id="btn" value="提交"><input type="hidden"  value="${ requestScope.username}"  name="username"/>
		</td>
	</tr>  
</table>
</fieldset>
</sf:form>  
<%-- </sf:form>   --%>
<!-- <form action="register.jsp">  
	<input type=submit value=注册>  
</form> -->
<script type="text/javascript">
 document.getElementById('password').focus();
 $("#btn").click(function(){
 	 if(!check_pswd($("#password").val())){
 	  $("#re_message").text("");
 	 	$("#message").text("密码为6~20位数字字母");
 	 }else if(!check_pswd($("#re_password").val())){
 		 $("#message").text("");
 	 	$("#re_message").text("密码为6~20位数字字母");
 	 }else if($("#password").val()!=$("#re_password").val()){
 		 $("#message").text("");
 		 $("#re_message").text("您两次输入的密码不一致");
 	 }else{
 	 	 var pswd =$("#password").val();
		 pswd = hex_md5(pswd).toUpperCase();
	 	$("#password").val(pswd);
		 $(".signin").submit();
 	 }
 });

//对输入密码的检验
function check_pswd(pswd){
	var pswd_reg = /^[0-9A-Za-z]{6,20}$/;
	if(pswd_reg.test(pswd)){
		return true;
	}
	return false;
}

</script>

</body>
</html>