<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript"
	src='<c:url value="/kindeditor-4.1.10/kindeditor.js"></c:url>'></script>
<script type="text/javascript">
KindEditor.ready(function(K) {
		window.editor = K.create('#editor');
	});
	function back(){
		window.location.href="javascript:history.go(-1);"
	}
</script>
<title></title>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<script type="text/javascript" src="/imagemark/js/jquery.min.js"></script>
<title>图片标记管理系统</title>
<link rel="stylesheet" type="text/css" href="/imagemark/css/easyui.css">
<script type="text/javascript" src="/imagemark/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/imagemark/js/easyui-lang-zh_CN.js"></script>
</head>

<body>
	<s:url var="publicNotificationUrl"
		value="/administrator/addNotification"></s:url>
	<!-- Container -->
	<div id="container">
	<jsp:include page="../head.jsp"></jsp:include>
		
		
		                                                                                                                                                                                       
	<%-- 	<s:url var="memberInfoUrl" value="/member/queryMemberByMemberName"></s:url>
	<s:url var="merchantInfoUrl" value="/merchant/queryMerchantByMerchantName"></s:url>
	<div class="head">
		<span class="head_logo"><i><b>I</</b>mageMatch</i></span> <span
			class="welcome">欢迎您回来:<label id="userName"><sec:authentication
					property="principal.username" /></label>!&nbsp;&nbsp;
		</span>
		<a class="btn_password" href="#">修改密码</a>
		&nbsp;&nbsp;
		<a class="btn_exit" href="<s:url value='/logout'/>">注销登陆</a>
	</div>
	<div style="background-color: #e5e6e8; margin-top: 20px; width: 100%;height: 34px;">
	<a href="notice.jsp" class="easyui-linkbutton" style="padding: 5px;"
					data-options="plain:true"><span class="btn_style">信息管理</span></a>
					<a href="statistical.jsp"
					class="easyui-linkbutton" style="padding: 5px;"
					data-options="plain:true"><span class="btn_style">统计分析</span></a>
					<a href="markManage.jsp"
					class="easyui-linkbutton" style="padding: 5px;"
					data-options="plain:true"><span class="btn_style">标记管理</span></a>
					<a href="userManage.jsp"
					class="easyui-linkbutton" style="padding: 5px;"
					data-options="plain:true"><span class="btn_style">用户管理</span></a>
					<a href="history.jsp"
					class="easyui-linkbutton" style="padding: 5px;"
					data-options="plain:true"><span class="btn_style">历史管理</span></a>
	</div>	 --%>
		
		
		
		
		
		
		
		
		
		
		
	</div>	
		<div class="clear"></div>
		<!-- Content -->
		<div id="content" style="font-weight: normal;margin-top: 8px;">
			<span class="top-title">信息发布</span>
			<input type="button" value="返回" onclick="back()" style="float: right;margin-right: 5px;"/>
	
			
	   <!-- 		<form method="post" action="${publicNotificationUrl }">

				<div class="clear"></div>
				<!-- Clear floatting -->
       <!--        <div>
					<a href="javascript:history.go(-1);"
						class="submit button small ui-corner-all">返回</a>
				</div>                          	  -->
				<div>
					<div style="margin-top: 10px;">
						<span class="bold" style="">新闻标题:</span>
						<input type="text" name="title"
							style="width: 200px"><br />
					<div>
					    <span class="bold">信息类别:</span>
						<select name="type">
							<option value="1">通知</option>
							<option value="2">公告</option>
						</select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
						<span class="bold">接收对象:</span>
						<input type="checkbox" name="notifier"
							checked="checked" value="merchant" />商家 &nbsp;&nbsp;&nbsp;
						<input type="checkbox" name="notifier" value="member" />会员
						</div>
						<div style="width: 700px; margin: 0 auto;margin-top: 12px;">
							<textarea id="editor" name="content"
								style="width: 700px; height: 300px;">
						    </textarea>
						</div>
						<input type="submit" value="发送" class="submit button large ui-corner-all" 
						style="margin-top: 30px;margin-left: 69%;"> <input
							type="reset" value="重置" class="submit button large ui-corner-all" style="margin-left: 10px;">
					</div>
				</div>

			</form>

			<!-- Divider -->


		</div>
		<!-- END Content -->
</body>
</html>

