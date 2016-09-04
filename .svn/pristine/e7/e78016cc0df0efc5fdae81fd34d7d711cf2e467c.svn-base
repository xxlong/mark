<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Basic DataGrid - jQuery EasyUI Demo</title>
<link rel="stylesheet" type="text/css" href="/imagemark/css/head.css">
<link rel="stylesheet" type="text/css" href="/imagemark/css/easyui.css">
<script type="text/javascript" src="/imagemark/js/jquery.min.js"></script>
<script type="text/javascript" src="/imagemark/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/imagemark/js/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
	$(function() {
		create_group_table();
	});
	function create_group_table() {
		$('#NoticeTable').datagrid({
			url:"/imagemark/administrator/queryNotification",
			queryParams : {
				"startTime" :"",
				"endTime" : "",
				"type" :"1"
			},
			loadFilter: function(data){
				if (data.status!=0){
					return {total:0,rows:[]};
				} else {
					var obj = data.data;
					if (obj.total != 0) {
						return obj;
					} else {
						return {total:0,rows:[]};
					}
				}
			},
			toolbar : '#tb',
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
	function queryByCondition(){
		$('#NoticeTable').datagrid('load',{
			"startTime" : $('#sTimeForNotice').datebox('getValue'),
			"endTime" : $('#eTimeForNotice').datebox('getValue'),
			"type" : $('#notificationType').combobox('getValue')
		});
	}
	function deleteData(target, url) {
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
						queryByCondition();
					}
				}
			});
		}
	}
	function showDetail() {
		var data = $('#NoticeTable').datagrid('getChecked'); 
		if(data==""){
			$.messager.alert('提示','请选择要查看的数据');
		}else{
			var title=data[0].title;
			var content=data[0].content;
			SetCookie("title",title);
			SetCookie("content",content);
			window.location.href="/imagemark/administrator/noticeDetail.jsp";
		}
	}
	function SetCookie(name, value) {  
	    var Days = 30;  
	    var exp = new Date();  
	    exp.setTime(exp.getTime() + 60 * 2000);//过期时间 2分钟  
	    document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();  
	}  
	function allSelect() {
		$('#NoticeTable').datagrid('selectAll');
		$('#NoticeTable').datagrid('uncheckRow');
		return true;
	}
</script>
</head>
<body>
	<s:url var="queryNotificationUrl" value="/administrator/queryNotification"></s:url>
	<jsp:include page="../head.jsp"></jsp:include>
	<div style="height: 20px; width: 100%;"></div>
	<div style="margin: 0 auto; width: 898px">
		<table id="NoticeTable" style="text-align: center; maring: 0 auto;">
		</table>
		<div id="tb" style="text-align: center;">
			<div style="font-weight: normal;">
				起: <input id="sTimeForNotice" class="easyui-datebox"
					style="width: 100px"> 止: <input id="eTimeForNotice"
					class="easyui-datebox" style="width: 100px"> 类型: <select
					id="notificationType" class="easyui-combobox" panelHeight="auto"
					style="width: 100px">
					<option value="1">通知</option>
					<option value="2">公告</option>
					<option value="3">系统消息</option>
				</select>
				<button style="margin-left: 10px;" class="easyui-linkbutton"
					onclick="queryByCondition()">查询</button>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="noticePublic.jsp" style="margin-left:-97px;" class="easyui-linkbutton" style="font-weight: normal;">发布</a>
				<button style="margin-left: 10px;" class="easyui-linkbutton" onclick="allSelect()">全选</button>
				<button style="margin-left: 10px;" class="easyui-linkbutton"
					onclick="deleteData('Notice','deleteNotification')">删除</button>
				<button style="margin-left: 10px;" class="easyui-linkbutton" onclick="showDetail()">详细内容</button>
			</div>
		</div>
	</div>
	<jsp:include page="../foot.jsp"></jsp:include>
</body>
</html>