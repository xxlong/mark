<%@ page language="java" contentType="text/html; charset=gb2312"
    pageEncoding="gb2312"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>Insert title here</title>
<link href="styles/main.css" rel="stylesheet" type="text/css" />
</head>
<body>
<!-- 表单没有指定URL情况下,将被提交到/administrator -->
<%-- <sf:form method="post" modelAttribute="administrator">   --%>
<s:url var="authUrl" value="/administrator"></s:url>
<c:if test="${not empty param.error}">
	<font color="red">Login error.<br />  
    </font>  
    Reason:${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
</c:if>
<form method="post" class="signin" action="${authUrl }">
<fieldset>
<table cellspacing="0">  
	<tr>
		<th><label for="username_or_email">用户名或邮箱：</label></th>  
		<td><%-- <sf:input path="adminName" size="20" maxlength="20"/>
			<small id="adminName_msg">No spaces,please.</small>
			<sf:errors path="adminName" delimiter=", " cssClass="error"/> --%>
			<input id="username_or_email" name="j_username" type="text"/>
		</td>  
	</tr>  
	<tr>
		<th><label for="password">密码：</label></th>  
		<td><%-- <sf:password path="adminPassword" size="30" showPassword="false"/>
			<small>6 characters or more (be tricky!)</small>
			<sf:errors path="adminPassword" delimiter=", " cssClass="error"></sf:errors> --%>
			<input id="password" name="j_password" type="password"/>
			<small><a href="#">Forgot?</a></small>
		</td>  
	</tr>  
	<tr>
		<td></td>
		<td><input type=submit value="确认">
		</td>
	</tr>  
</table>
</fieldset>
</form>  
<%-- </sf:form>   --%>
<!-- <form action="register.jsp">  
	<input type=submit value=注册>  
</form> -->
<script type="text/javascript">
 document.getElementById('username_or_email').focus();
</script>

</body>
</html>