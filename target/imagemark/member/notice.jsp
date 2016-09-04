<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>member notice</title>
<link rel="stylesheet" type="text/css" href="/imagemark/css/easyui.css">
<link rel="stylesheet" type="text/css" href="/imagemark/css/head.css">

<script type="text/javascript" src="/imagemark/js/jquery.min.js"></script>
<script type="text/javascript" src="/imagemark/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/imagemark/js/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
	$(function() {
		create_notice_table();
	});
	function create_notice_table() {
		$('#memberNoticeTable').datagrid({
			url : "/imagemark/member/queryNotification",
			queryParams : {
				"memberName" : $('#userName').text(),
				"type" : $('#notificationType').combobox('getValue'),
				"sortType" : 2
			},
			loadFilter : function(data) {
				if (data.status != 0) {
					return {
						total : 0,
						rows : []
					};
				} else {
					var obj = data.data;
					if (obj.total != 0) {
						return obj;
					} else {
						return {
							total : 0,
							rows : []
						};
					}
				}
			},
			toolbar : '#memberNotice',
			width : 820,
			height : 550,
			singleSelect : true,
			rownumbers : true,
			autoRowHeight : true,
			pagination : true,
			pageSize : 10,
			columns : [ [ {
				field : 'ch',
				checkbox : 'true'
			}, {
				field : '_id',
				title : '通知ID',
				hidden : 'true',
				width : 100
			}, {
				field : 'type',
				title : '类别',
				width : 100
			}, {
				field : 'notifier',
				title : '通知对象',
				width : 100
			}, {
				field : 'sendTime',
				title : '发布时间',
				width : 100
			}, {
				field : 'title',
				title : '标题',
				width : 100
			}, ] ]

		});
	}
	function queryByCondition() {
		$('#memberNoticeTable').datagrid('load', {
			"memberName" : $('#userName').text(),
			"type" : $('#notificationType').combobox('getValue'),
			"sortType" : 2
		});
	}
	function showDetail() {

	}
	function allSelect() {
		$('#memberNoticeTable').datagrid('selectAll');
		$('#memberNoticeTable').datagrid('uncheckRow');
		return true;
	}
</script>
</head>
<body>
	<jsp:include page="../head.jsp"></jsp:include>
	<div style="height: 20px; width: 100%;"></div>
	<div style="margin: 0 auto; width: 898px">
		<table id="memberNoticeTable">
		</table>
	</div>
	<div id="memberNotice" style="padding: 5px; height: auto">
		<div class="textfont">
			类型: <select id="notificationType" class="easyui-combobox"
				style="width: 100px">
				<option value="1">通知</option>
				<option value="2">公告</option>
				<option value="3">系统消息</option>
			</select>
			<button class="easyui-linkbutton" onclick="queryByCondition()">查询</button>
			<button class="easyui-linkbutton" onclick="showDetail()">详细内容</button>
		</div>
	</div>
	<jsp:include page="../foot.jsp"></jsp:include>
</body>
</html>