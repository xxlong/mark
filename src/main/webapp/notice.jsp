<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Basic DataGrid - jQuery EasyUI Demo</title>
<link rel="stylesheet" type="text/css"
	href="/imagemark/css/statical.css">

<link rel="stylesheet" type="text/css" href="/imagemark/css/easyui.css">
<script type="text/javascript" src="/imagemark/js/jquery.min.js"></script>
<script type="text/javascript" src="/imagemark/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/imagemark/js/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
	var pageSizeDefalut = 20;
	var pageNumberDefalut = 1;
	var urlDefault = "/imagemark/administrator/";
	$(function() {
		create_group_table();
		getData("queryNotification", "Notice");
	});
	function create_group_table() {
		$('#NoticeTable').datagrid({
			toolbar : '#tb',
			width : 820,
			height : 550,
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
	function getData(url, target) {
		var sTime = $('#sTimeFor' + target).val();
		var eTime = $('#eTimeFor' + target).val();
		var type = $('#notificationType').combobox('getValue');
		$.ajax({
			url : urlDefault + url,
			data : {
				startTime : sTime,
				endTime : eTime,
				type : type,
				pageNumber : pageNumberDefalut,
				pageSize : pageSizeDefalut
			},
			dataType : "json",
			success : function(data) {
				var obj=data.data;
				if (obj.total != 0) {
					$('#' + target + 'Table').datagrid('loadData', obj);
				} else {
					$('#' + target + 'Table').datagrid('loadData', {
						total : 0,
						rows : []
					});
				}
			}
		});
	}
	function deleteData(target, url, reloadUrl) {
		var list = new Array();
		var data = $('#' + target + 'Table').datagrid('getChecked');
		if (data = null || data == "") {
			$.messager.alert("提示", "请先选择要操作的数据");
		} else {
			for (var i = 0; i < data.length; i++) {
				list[i] = data[i]._id;
			}
			$.ajax({
				url : urlDefault + url + "?data=" + list,
				/* data :{data:list}, */
				dataType : "json",
				success : function(data) {
					if (data.status != 0) {
						$('#' + target + 'Table').datagrid('loadData', {
							total : 0,
							rows : []
						});
					} else {
						getData(reloadUrl, target);
					}
				}
			});
		}
	}
	function showDetail() {

	}
	function allSelect() {
		$('#NoticeTable').datagrid('selectAll');
		$('#NoticeTable').datagrid('uncheckRow');
		return true;
	}
</script>
</head>
<body>
	<div class="head">
		<span class="welcome"">欢迎您：<label id="userName"><sec:authentication 
		property="principal.username"/>!</label>&nbsp;&nbsp;
			<button class="btn_password">修改密码</button>&nbsp;&nbsp;
			<button class="btn_exit" onclick="logout()">注销登陆</button></span>
	</div>

	<div class="easyui-panel" class="head_option" border="0"
		style="background-color: #e5e6e8; margin-top: 20px; width: 100%">
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
	<div style="height:20px;width:100%;"></div>
	
		
		<div style=" margin:0 auto;width:898px">
			<table id="NoticeTable" style="text-align: center;maring:0 auto;"
				class="easyui-datagrid">
			</table>
			<div id="tb" style=" text-align: center;">
				<div>
					<sec:authorize access="hasRole('ROLE_ADMIN_ALL')">
						起: <input id="sTimeForNotice" class="easyui-datebox"
						style="width: 100px">
						 止: <input id="eTimeForNotice" class="easyui-datebox" 
						style="width: 100px">
					</sec:authorize>
						 类型: <select
						id="notificationType" class="easyui-combobox" panelHeight="auto"
						style="width: 100px">
						<option value="1">通知</option>
						<option value="2">公告</option>
						<option value="3">系统消息</option>
					</select>
					<button class="easyui-linkbutton"
						onclick="getData('queryNotification','Notice')">查询</button>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<sec:authorize access="hasRole('ROLE_ADMIN_ALL')">
						<a href="noticePublic.jsp" class="easyui-linkbutton">发布</a>
						<button class="easyui-linkbutton" onclick="allSelect()">全选</button>
						<button class="easyui-linkbutton"
						onclick="deleteData('Notice','deleteNotification','queryNotification')">
						删除</button>
					</sec:authorize>
					<button class="easyui-linkbutton" onclick="showDetail()">详细内容</button>
				</div>
			</div>
		
</body>
</html>