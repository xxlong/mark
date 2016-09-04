<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<link rel="stylesheet" type="text/css" href="/imagemark/css/easyui.css">
<link rel="stylesheet" type="text/css" href="/imagemark/css/head.css">

<script type="text/javascript" src="/imagemark/js/jquery.min.js"></script>
<script type="text/javascript" src="/imagemark/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/imagemark/js/easyui-lang-zh_CN.js"></script>
</head>
<body>
	<jsp:include page="../head.jsp"></jsp:include>

	<div id="myinformation" align="center" style="font-weight: normal;">
		<p id="text_information" style="font-size: 30px;font-weight: normal;height:50px;">
			让世界<span Style="font-family: monospace;color:#8DEEEE;font-weight: bolder;font-size:48px;padding-left: 6px;padding-bottom: 20px; ">发</span><span Style="font-family: monospace;color:#FA8072;font-weight: bolder;font-size:28px;padding-right: 6px;">现</span>你
		</p>
		<table style="font-size: 20px;margin-top: 40px;">
			<tr height="30px">
				<td >昵称</td>
				<td ><label id="memberNickName" style="margin-left: 60px;">${memberInfo.memberName}</label></td>
			</tr>
			<tr height="30px">
				<td>邮箱</td>
				<td><label id="memberMail" style="margin-left: 60px;">${memberInfo.memberMail}</label></td>
			</tr>
			<tr height="30px">
				<td>注册时间</td>
				<td><label id="memberRegisterTimes" style="margin-left: 60px;">${memberInfo.registerTime}</label></td>
			</tr>
			<tr height="30px">
				<td>当前积分</td>
				<td><label id="memberScore" style="margin-left: 60px;">${memberInfo.score}</label></td>
			</tr>
			<tr height="30px">
				<td>当前等级</td>
				<td><label id="memberLevel" style="margin-left: 60px;">${memberInfo.level}</label></td>
			</tr>
			<tr height="30px">
				<td>状态</td>
				<td><label id="memberStatus" style="margin-left: 60px;">${memberInfo.status}</label></td>
			</tr>
		</table>
	</div>
	<jsp:include page="../foot.jsp"></jsp:include>
</body>
</html>