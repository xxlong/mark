<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="/imagemark/js/jquery.min.js"></script>
<link rel="stylesheet" type="text/css" href="/imagemark/css/head.css">
<link rel="stylesheet" type="text/css" href="/imagemark/css/easyui.css">
<script type="text/javascript" src="/imagemark/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/imagemark/js/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
function GetCookie(name) {  
    var arg = name + "=";  
    var alen = arg.length;  
    var clen = document.cookie.length;  
    var i = 0;  
    while (i < clen) {  
        var j = i + alen;  
        if (document.cookie.substring(i, j) == arg) return getCookieVal(j);  
        i = document.cookie.indexOf(" ", i) + 1;  
        if (i == 0) break;  
    }  
    return null;  
}
function getCookieVal(offset) {  
    var endstr = document.cookie.indexOf(";", offset);  
    if (endstr == -1) endstr = document.cookie.length;  
    return unescape(document.cookie.substring(offset, endstr));  
}  
$(function() {
	var title=GetCookie("title");
	var content=GetCookie("content");
	$('#title').html(title);
	$('#content').html(content);
});
</script>
<title>Insert title here</title>
</head>
<body>
 <jsp:include page="../head.jsp"></jsp:include>
	<div id="container">
		<!-- Header -->
		<!-- END Header -->
		<div class="clear"></div>
		<!-- Content -->
		<div >
			<h2 class="top-title"><label id="title"></label></h2>
			<!-- Clear floatting -->
			<div style="float: right;">
				<a href="javascript:history.go(-1);">返回</a>
			</div>
			<hr />
			<!-- Divider -->
			<div id=content>
			</div>

		</div>
	</div>
</body>
</html>