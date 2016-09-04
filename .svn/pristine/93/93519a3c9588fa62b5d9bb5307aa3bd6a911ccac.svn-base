<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">
function showWindow(flag){
	if(flag){
	var name=$('#userName').html();  
	$("#name").val(name);
	$('#passwordUpdate').window('open');
	}else{
		$('#passwordUpdate').window('close');	
	}	
}
function checkPassword(){
	var name=$('#userName').html();
	var pwd=$('#oldPwd').val();
	var flag;
	$.ajax({
		url :"/imagemark/administrator/checkPassword",
		data : {
			Name : name,
			oldPassword : pwd
		},
		async:false,
		dataType : "json",
		success : function(data) {
			if (data!= 0) {
				$.messager.alert('提示','密码错误');
				flag=false;
			} else {
				flag=true;
			}
		}
	});	
	return flag;
}
function updatePassword(){
	var name=$('#userName').html();
	var oldPwd=$('#oldPwd').val();
	var newPwd=$('#newPwd').val();
	if(oldPwd!=newPwd){
		if(checkPassword()){
		$.ajax({
			url :"/imagemark/administrator/updatePassword",
			data : {
				Name : name,
				oldPassword : oldPwd,
				newPassword:newPwd
			},
			dataType : "json",
			success : function(data) {
				if (data!= 0) {
					$.messager.alert('提示','更新失败');
					showWindow(false);
				} else {
					$.messager.alert('提示','更新成功');
					showWindow(false);
					setTimeout("javascript:location.href='/imagemark/login'", 1000);   
				}
			}
		});
		}else{
			$.messager.alert('提示','密码错误');
		}
	}else{
		$.messager.alert('提示','两次密码不能一样');
	}
	
}

</script>
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="/imagemark/css/head.css">
<link rel="stylesheet" type="text/css" href="/imagemark/css/easyui.css">
</head>
<body>
	<s:url var="memberInfoUrl" value="/member/queryMemberByMemberName"></s:url>
	<s:url var="merchantInfoUrl"
		value="/merchant/queryMerchantByMerchantName"></s:url>
	<div class="head">
		<span class="head_logo"><i><b>I</</b>mageMatch</i></span> <span
			class="welcome">欢迎您回来:<label id="userName"><sec:authentication
					property="principal.username" /></label>!&nbsp;&nbsp;
		</span>
	<!-- 	<a class="easyui-linkbutton"  onclick="showWindow(true)">修改密码</a>          -->
	        
	        <a class="btn_password"  onclick="showWindow(true)">修改密码</a>
		&nbsp;&nbsp; <a class="btn_exit" href="<s:url value='/logout'/>">注销登陆</a>
	</div>
	<div id="menu">
		<sec:authorize access="hasRole('ROLE_ADMIN_ALL')">
			<div style="background-color: #e5e6e8; margin-top: 20px; width: 100%">
				<a href="notice.jsp" class="easyui-linkbutton" style="padding: 5px;"
					data-options="plain:true">信息管理</a> <a href="statistical.jsp"
					class="easyui-linkbutton" style="padding: 5px;"
					data-options="plain:true">统计分析</a> <a href="markManage.jsp"
					class="easyui-linkbutton" style="padding: 5px;"
					data-options="plain:true">标记管理</a> <a href="userManage.jsp"
					class="easyui-linkbutton" style="padding: 5px;"
					data-options="plain:true">用户管理</a> <a href="history.jsp"
					class="easyui-linkbutton" style="padding: 5px;"
					data-options="plain:true">历史管理</a>
			</div>
		</sec:authorize>
		<sec:authorize access="hasRole('ROLE_ADMIN_VERIFIER')">
			<div
				style="background-color: #e5e6e8; margin-top: 20px; width: 100%; border: 0px;">
				<a href="markManage.jsp" class="easyui-linkbutton"
					style="padding: 5px;" data-options="plain:true">标记管理</a><a
					href="userManage.jsp" class="easyui-linkbutton"
					style="padding: 5px;" data-options="plain:true">用户管理</a>
			</div>
		</sec:authorize>
		<sec:authorize access="hasRole('ROLE_MERCHANT')">
			<div
				style="background-color: #e5e6e8; margin-top: 20px; width: 100%; border: 0px;">
				<a href="notice.jsp" class="easyui-linkbutton"
					style="padding: 5px; text-decoration: none;"
					data-options="plain:true">首页</a> <a href="${merchantInfoUrl }"
					class="easyui-linkbutton"
					style="padding: 5px; text-decoration: none;"
					data-options="plain:true">我的信息</a> <a href="merchantMark.jsp"
					class="easyui-linkbutton"
					style="padding: 5px; text-decoration: none;"
					data-options="plain:true">我的标记</a>
			</div>
		</sec:authorize>
		<sec:authorize access="hasRole('ROLE_MEMBER')">
			<div
				style="background-color: #e5e6e8; margin-top: 20px; width: 100%; border: 0px;">
				<a href="notice.jsp" class="easyui-linkbutton" style="padding: 5px;"
					data-options="plain:true">首页</a> <a href="${memberInfoUrl }"
					class="easyui-linkbutton" style="padding: 5px;"
					data-options="plain:true">我的信息</a> <a href="memberClick.jsp"
					class="easyui-linkbutton" style="padding: 5px;"
					data-options="plain:true">我的足迹</a> <a href="memberComment.jsp"
					class="easyui-linkbutton" style="padding: 5px;"
					data-options="plain:true">我的评论</a>
			</div>
		</sec:authorize>
	</div>
	<div id="passwordUpdate" class="easyui-window" title="修改密码" style="width:250px;height:330px;border:0;padding-top: 10px;background-color: #EBEBEB;" data-options="modal:true,closed:true,resizable:false" >
		<form>
			<table style="padding-top: 12px;margin:0 auto;width: 100%;">
				<tr valign="middle">
					<td style="padding-top: 10px;padding-left: 10px;">用户名: <input style="width: 120px;" id ="name" type="text" name="Name" disabled>
					</td>
				</tr>
				<tr>
					<td style="padding-top: 10px;padding-left: 10px;">旧密码: <input style="width: 120px;" id ="oldPwd" type="password" name="oldPassword" onblur="checkPassword()">
					</td>
				</tr>
				<tr>
					<td style="padding-top: 10px;padding-left: 10px;">新密码: <input style="width: 120px;" id ="newPwd" type="password" name="newPassword">
					</td>
				</tr>
			<!--  	<tr style="padding-right: 20px;">
				    <td style="padding-top: 12px;"><input type="button" style="float: right;" value="取消" onclick="showWindow(false)"></td>
					<td style="padding-top: 12px;"><input type="button" style="float: right;" value="提交" onclick="updatePassword()"></td>
				</tr>
				-->
				
			</table>
			    <button onclick="showWindow(false)" style="margin-left: 89px;margin-top: 22px;">取消</button>
				<button onclick="updatePassword()">提交</button>
		</form>
	</div>
</body>
</html>