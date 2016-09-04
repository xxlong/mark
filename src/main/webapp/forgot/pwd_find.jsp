<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<!-- <meta http-equiv="refresh" content="0,url=http://localhost:8080/imagemark/forgot/pwd_find.jsp" >  -->
<title>Insert title here</title>
<link href="styles/main.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="/imagemark/js/jquery.min.js"></script>
	<script type="text/javascript" src="/imagemark/js/md5.js"></script>
</head>
<script type="text/javascript"> 
	$(document).ready(function(){//这个就是jQueryready ，它就像C语言的main 所有操作包含在它里面 
		$("#btn").mousedown(function(){ 
			$("#btn").attr("disabled", true); 
			login(); //点击ID为"btn"的按钮后触发函数 login(); 
		}); 
	}); 
	
	function login(){ //函数 login(); 
		var username = $("#username").val();//取框中的用户名 
		$.ajax({ //一个Ajax过程 
				type: "post", //以post方式与后台沟通 
				url : "http://222.214.218.140:90/imagemark/administrator/pwd_find.do", //地址
				//dataType:'json',//返回的值以 JSON方式 解释 
				data: 'username='+username, 
				contentType: "application/x-www-form-urlencoded; charset=utf-8", 
				success: function(json){//如果调用成功 
				var response = JSON.parse(json); 
				if(response.status==0){
								//alert(json.username+'\n'+json.password); 
								//$('#message').html(response.desc); //把返回值显示在预定义的result定位符位置 
								$('#message').text("用户名或邮箱不存在!");
								$("#btn").attr("disabled", false); 
					}
					else{						
						window.location.href="http://222.214.218.140:90/imagemark/forgot/pwd_view.jsp?mail="+response.desc;
					}
				} 
		}); 
	
} 
</script> 


<body onkeydown="if(event.keyCode==13){return false;}">
<!-- 表单没有指定URL情况下,将被提交到/administrator -->
<%-- <sf:form method="post" modelAttribute="administrator">   --%>
<s:url var="authUrl" value="/administrator/pwd_find.do"></s:url>
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
	
		<th>
		<label for="username">用户名或邮箱：</label></th>  
		<td><%-- <sf:input path="adminName" size="20" maxlength="20"/>
			<small id="adminName_msg">No spaces,please.</small>
			<sf:errors path="adminName" delimiter=", " cssClass="error"/> --%>
			<input id="username" name="username" type="text"/>
		</td>  
		<td><div id="message"  style="border:1px  red;width:200px;height:25px;color: red"></div></td>
	</tr>  
<%-- 	<tr>
		<th><label for="password">密码：</label></th>  
		<td><sf:password path="adminPassword" size="30" showPassword="false"/>
			<small>6 characters or more (be tricky!)</small>
			<sf:errors path="adminPassword" delimiter=", " cssClass="error"></sf:errors>
			<input id="o_password" type="password"/>
			<input id="password" name="j_password" type="password" style="display: none;"/>
			<small><a href="forgot/find.jsp">Forgot?</a></small>
		</td>  
	</tr>   --%>
	<tr>
		<td></td>
		<%--<td><input type=submit value="确认">--%>
	
		<td> <input type="button" id="btn" value="确认">
		</td>
	</tr>  
</table>
</fieldset>
</sf:form>  
<%-- </sf:form>   --%>
<!-- <form action="register.jsp">  
	<input type=submit value=注册>  
</form> -->
<%-- <script type="text/javascript">
 document.getElementById('username').focus();
 $("#btn").click(function(){
/* 	 var pswd =$("#o_password").val();
	 pswd = hex_md5(pswd).toUpperCase();
	 $("#password").val(pswd); */
	 $(".signin").submit();
 });

</script> --%>

</body>
</html>