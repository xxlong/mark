<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
	var urlDefault = "/imagemark/administrator/";
	$(function() {
		create_member_table();
		create_merchantManage_table();
		create_merchantVerify_table("VerifyMerchantTable", "forVerify");
	});
	function create_member_table() {
		$('#MemberManageTable').datagrid({
			url:"/imagemark/administrator/getStatisticAnalysisOnMembers",
			queryParams : {
				"startTime" : $('#sTimeForMemberManage').datebox('getValue'),
				"endTime" : $('#eTimeForMemberManage').datebox('getValue'),
				"sortField" : $('#MemberManageSortField').combobox('getValue'),
				"sortType" : $('#MemberManageSortType').combobox('getValue')
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
			toolbar : '#forMember',
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
				title : '会员ID',
				hidden : 'true',
			}, {
				field : 'memberName',
				align : 'center',
				title : '会员名称',
				width : 100
			}, {
				field : 'registerTime',
				align : 'center',
				title : '注册时间',
				width : 100
			}, {
				field : 'lastLoginDate',
				align : 'center',
				title : '上次登陆时间',
				width : 100
			}, {
				field : 'score',
				align : 'center',
				title : '会员积分',
				width : 100
			}, {
				field : 'level',
				title : '会员等级',
				align : 'center',
				width : 100
			}, {
				field : 'clickTimes',
				title : '点击次数',
				align : 'center',
				width : 100
			}, {
				field : 'commentTimes',
				align : 'center',
				title : '评论次数',
				width : 100
			}, 
			] ]

		});
	}
	function create_merchantVerify_table(target, toolbar) {
		$('#' + target).datagrid({
			url:"/imagemark/administrator/queryUserManagementOnNotVerifiedMerchants",
			queryParams : {
				"sortType" : $('#MerchantSortType').combobox('getValue')
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
			toolbar : '#' + toolbar,
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
				title : '商家ID',
				hidden : 'true',
			}, {
				field : 'merchantName',
				title : '商家名称',
				align : 'center',
				width : 100
			}, {
				field : 'time',
				title : '注册时间',
				align : 'center',
				width : 100
			}, {
				field : 'merchantTrust',
				title : '商家信用',
				align : 'center',
				width : 100
			}, {
				field : 'merchantID',
				title : '身份证号',
				align : 'center',
				width : 100
			}, {
				field : 'storeName',
				title : '店铺名称',
				align : 'center',
				width : 100
			}, {
				field : 'storeAddress',
				title : '店铺地址',
				align : 'center',
				width : 100
			}, {
				field : 'storeType',
				title : '店铺类型',
				align : 'center',
				width : 100
			}, {
				field : 'phoneNumber',
				title : '手机号',
				align : 'center',
				width : 100
			}, ] ]

		});
	}
	function create_merchantManage_table() {
		$('#MerchantManageTable').datagrid({
			url:"/imagemark/administrator/getStatisticAnalysisOnMerchants",
			queryParams : {
				"startTime" : $('#sTimeForMerchantManage').datebox('getValue'),
				"endTime" : $('#eTimeForMerchantManage').datebox('getValue'),
				"sortField" : $('#MerchantManageSortField').combobox('getValue'),
				"sortType" : $('#MerchantManageSortType').combobox('getValue')
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
			toolbar : '#forMerchant',
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
				title : '商家ID',
				hidden : 'true',
			}, {
				field : 'merchantName',
				title : '商家名称',
				align : 'center',
				width : 100
			}, {
				field : 'registeTime',
				title : '注册时间',
				align : 'center',
				width : 100
			}, {
				field : 'trust',
				title : '商家信用',
				align : 'center',
				width : 100
			}, {
				field : 'clickTimes',
				title : '点击次数',
				align : 'center',
				width : 100
			}, {
				field : 'commentTimes',
				title : '评论次数',
				align : 'center',
				width : 100
			}, {
				field : 'num',
				title : '标记数',
				align : 'center',
				width : 100
			}, ] ]

		});
	}
	function queryByCondition(target){
		$('#' + target+'Table').datagrid('load',{
			"startTime" : $('#sTimeFor'+target).datebox('getValue'),
			"endTime" : $('#eTimeFor'+target).datebox('getValue'),
			"sortField" : $('#'+target+'SortField').combobox('getValue'),
			"sortType" : $('#'+target+'SortType').combobox('getValue')
		});
	}
	function queryByConditionAnother(){
		$('#VerifyMerchantTable').datagrid('load',{
			"sortType" :  $('#MerchantSortType').combobox('getValue')
		});
	}
	function allSelect(target) {
		$('#' + target).datagrid('selectAll');
		$('#' + target).datagrid('uncheckRow');
		return true;
	}
	function VerifyMerchant(url) {
		var list = new Array();
		var data = $('#VerifyMerchantTable').datagrid('getChecked');
		for (var i = 0; i < data.length; i++) {
			list[i] = data[i].merchantName;
		}
		alert(list);
		$
				.ajax({
					url : urlDefault + url + "?data=" + list,
					/* data :{data:list}, */
					dataType : "json",
					success : function(data) {
						if (data.status != 0) {
							$('#VerifyMerchantTable').datagrid('loadData', {
								total : 0,
								rows : []
							});
						} else {
							queryByConditionAnother();
						}
					}
				});
	}
	function DeleteData(target, url, reloadUrl, Field) {
		var list = new Array();
		var data = $('#' + target + 'Table').datagrid('getChecked');
		if (data = null || data == "") {
			$.messager.alert("提示", "请先选择要操作的数据");
		} else {
			for (var i = 0; i < data.length; i++) {
				if (Field == "memberName") {
					list[i] = data[i].memberName;
				} else if (Field == "merchantName") {
					list[i] = data[i].merchantName;
				} else {
					alert("error");
				}
			}
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
					getMerOrMemData(target, reloadUrl);
				}
			}
		});
	}
	function openConditions(target){
		$('#'+target).window('open'); 
	}
	function closeConditions(target){
		$('#'+target).window('close'); 
	}
</script>
</head>
<body>

	 <jsp:include page="../head.jsp"></jsp:include>
	<div style="height:20px;width:100%;"></div>
	<div class="easyui-tabs"
		style="width: 900px; height: 600px; margin: 0 auto">
		<div title="会员管理" style="padding: 10px">
			<table id="MemberManageTable">
			</table>
		</div>
		<div title="商家管理" style="padding: 10px">
			<table id="MerchantManageTable">
			</table>
		</div>
		<div title="商家审核" style="padding: 10px">
			<table id="VerifyMerchantTable">
			</table>
		</div>
	</div>
	<div id="forMember" style="padding: 5px; height: auto">
		<div style="font-weight: normal;">
			起: <input id="sTimeForMemberManage" class="easyui-datebox"
				style="width: 100px"> 止: <input id="eTimeForMemberManage"
				class="easyui-datebox" style="width: 100px">&nbsp;&nbsp;排序方式:
			<select id="MemberManageSortType" class="easyui-combobox"
				panelHeight="auto" style="width: 100px">
				<option value="1">升序</option>
				<option value="2">倒序</option>
			</select>&nbsp;&nbsp;排序条件： <select id="MemberManageSortField"
				class="easyui-combobox" panelHeight="auto" style="width: 100px">
				<option value="1">会员积分</option>
				<option value="2">点击数</option>
				<option value="3">评论数</option>
				<option value="4">注册时间</option>
			</select>
			<button class="easyui-linkbutton"
				onclick="queryByCondition('MemberManage')">查询</button>
			<button class="easyui-linkbutton"
				onclick="allSelect('MemberManageTable')">全选</button>
			<button class="easyui-linkbutton"
				onclick="DeleteData('MemberManage','DeleteMember','getStatisticAnalysisOnMembers','memberName')">删除</button>
			<button class="easyui-linkbutton" onclick="openConditions('conditionForMember')">更多查询</button>
		</div>
	</div>
	<div id="forMerchant" style="padding: 5px; height: auto">
		<div style="font-weight: normal;">
			起: <input id="sTimeForMerchantManage" class="easyui-datebox"
				style="width: 100px"> 止: <input id="eTimeForMerchantManage"
				class="easyui-datebox" style="width: 100px">&nbsp;&nbsp;排序方式:
			<select id="MerchantManageSortType" class="easyui-combobox"
				panelHeight="auto" style="width: 100px">
				<option value="1">升序</option>
				<option value="2">倒序</option>
			</select>&nbsp;&nbsp;排序条件： <select id="MerchantManageSortField"
				class="easyui-combobox" panelHeight="auto" style="width: 100px">
				<option value="5">注册时间</option>
				<option value="1">商家积分</option>
				<option value="2">标记数量</option>
				<option value="3">点击数</option>
				<option value="4">评论数</option>
			</select>
			<button class="easyui-linkbutton"
				onclick="queryByCondition('MerchantManage')">查询</button>
			<button class="easyui-linkbutton"
				onclick="allSelect('MerchantManageTable')">全选</button>
			<button class="easyui-linkbutton"
				onclick="DeleteData('MerchantManage','DeleteMerchant','getStatisticAnalysisOnMerchants','merchantName')">删除</button>
			<button class="easyui-linkbutton" onclick="openConditions('conditionForMerchant')">更多查询</button>
		</div>
	</div>
	<div id="forVerify" style="padding: 5px; height: auto">
		<div style="font-weight: normal;">
			起: <input id="sTimeForMerchant" class="easyui-datebox"
				style="width: 100px"> 止: <input id="eTimeForMerchant"
				class="easyui-datebox" style="width: 100px">&nbsp;&nbsp;商家名称：<input
				id="textMerchantName2" class="easyui-textbox">
			&nbsp;&nbsp;排序方式: <select id="MerchantSortType"
				class="easyui-combobox" panelHeight="auto" style="width: 100px">
				<option value="1">注册时间升序</option>
				<option value="2">注册时间倒序</option>
			</select>&nbsp;&nbsp;
			<button class="easyui-linkbutton"
				onclick="queryByConditionAnother()">查询</button>
			<button class="easyui-linkbutton"
				onclick="allSelect('VerifyMerchantTable')">全选</button>
			<button class="easyui-linkbutton"
				onclick="VerifyMerchant('VerifyMerchant')">通过</button>
		</div>
	</div>
	<div id="conditionForMember" class="easyui-window" title="查询会员"  closed="true"
		style="width: 600px; height: 400px"
		data-options="iconCls:'icon-save',modal:true">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',split:true" style="height: 100px"></div>
			<div data-options="region:'center'">The Content.</div>
		</div>
	</div>
	<div id="conditionForMerchant" class="easyui-window" title="查询商家" closed="true"
		style="width: 600px; height: 400px"
		data-options="iconCls:'icon-save',modal:true">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',split:true" style="height: 100px"></div>
			<div data-options="region:'center'">The Content.</div>
		</div>
	</div>
	<jsp:include page="../foot.jsp"></jsp:include>
</body>
</html>