<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Basic DataGrid - jQuery EasyUI Demo</title>
<link rel="stylesheet" type="text/css" href="/imagemark/css/easyui.css">
<link rel="stylesheet" type="text/css" href="/imagemark/css/head.css">
<script type="text/javascript" src="/imagemark/js/jquery.min.js"></script>
<script type="text/javascript" src="/imagemark/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/imagemark/js/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
	var urlDefault = "/imagemark/administrator/";
	$(function() {
		create_mark_table();
		create_member_table();
		create_merchant_table();
	});
	function create_mark_table() {
		$('#DeleteMark')
				.datagrid(
						{
							url:"/imagemark/administrator/queryDeletedMarks",
							queryParams : {
								"startTime" : $('#sTimeForDeleteMark').datebox('getValue'),
								"endTime" : $('#eTimeForDeleteMark').datebox('getValue'),
								"Name" : $('#textDeleteMark').val(),
								"sortType" : $('#MarkSortType').combobox('getValue')
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
							toolbar : '#MarkDeleted',
							width : 820,
							height : 550,
							rownumbers : true,
							autoRowHeight : true,
							pagination : true,
							pageSize : 10,
							columns : [ [
									{
										field : 'ch',
										checkbox : 'true'
									},
									{
										field : '_id',
										title : '商家记录',
										width : 60,
										align : 'center',
										formatter : function(value, row, index) {

											var pic = "http://222.214.218.140:90/thumb/"
													+ row._id + ".png";
											return '<a href="'+row.componentLinkAddress+'"><img src="'+pic+'" width="40" height="40"/></a>';
										}
									}, {
										field : 'merchantName',
										title : '商家名称',
										align : 'center',
										width : 100
									}, {
										field : 'componentName',
										title : '商品名称',
										align : 'center',
										width : 100
									}, {
										field : 'componentPrice',
										title : '商品价格',
										align : 'center',
										width : 100
									}, {
										field : 'componentType',
										title : '商品类型',
										align : 'center',
										width : 100
									}, {
										field : 'deleteDate',
										title : '删除时间',
										align : 'center',
										width : 100
									}, ] ]

						});
	}
	function create_merchant_table() {
		$('#DeleteMerchant').datagrid({
			url:"/imagemark/administrator/queryDeletedMerchants",
			queryParams : {
				"startTime" : $('#sTimeForDeleteMerchant').datebox('getValue'),
				"endTime" : $('#eTimeForDeleteMerchant').datebox('getValue'),
				"Name" : $('#textDeleteMerchant').val(),
				"sortType" :  $('#MerchantSortType').combobox('getValue')
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
			toolbar : '#MerchantDeleted',
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
				hidden : 'true'
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
				field : 'deleteDate',
				title : '删除时间',
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
			}, /* {
				field : 'goodCommentsNum',
				title : '好评数',
				align : 'center',
				width : 100
			}, {
				field : 'badCommentsNum',
				title : '差评数',
				align : 'center',
				width : 100
			},  */] ]

		});
	}
	function create_member_table() {
		$('#DeleteMember').datagrid({
			url:"/imagemark/administrator/queryDeletedMembers",
			queryParams : {
				"startTime" : $('#sTimeForDeleteMember').datebox('getValue'),
				"endTime" : $('#eTimeForDeleteMember').datebox('getValue'),
				"Name" : $('#textDeleteMember').val(),
				"sortType" :  $('#MemberSortType').combobox('getValue')
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
			toolbar : '#MemberDeleted',
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
				hidden : 'true'
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
				field : 'deleteDate',
				title : '删除时间',
				align : 'center',
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
			}, /* {
				field : 'statisfyComment',
				align : 'center',
				title : '好评数',
				width : 100
			}, {
				field : 'unstatisfyComment',
				align : 'center',
				title : '差评数',
				width : 100
			}, */ ] ]

		});
	}

	function queryByCondition(target){
		$('#Delete' + target).datagrid('load',{
			"startTime" : $('#sTimeForDelete'+target).datebox('getValue'),
			"endTime" : $('#eTimeForDelete'+target).datebox('getValue'),
			"Name" : $('#textDelete' + target).val(),
			"sortType": $('#'+target+'SortType').combobox('getValue')
		});
	}
	function allSelect(target) {
		$('#' + target).datagrid('selectAll');
		$('#' + target).datagrid('uncheckRow');
		return true;
	}
	function restoreData(target, url, Field) {
		var list = new Array();
		var data = $('#' + target).datagrid('getChecked');
		for (var i = 0; i < data.length; i++) {
			if (Field == "memberName") {
				list[i] = data[i].memberName;
			} else if (Field == "merchantName") {
				list[i] = data[i].merchantName;
			} else {
				list[i] = data[i]._id;
			}
		}
		$.ajax({
			url : urlDefault + url + "?data=" + list,
			/* data :{data:list}, */
			dataType : "json",
			success : function(data) {
				if (data.status != 0) {
					$('#' + target).datagrid('loadData', {
						total : 0,
						rows : []
					});
				} else {
					queryByCondition(target);
				}
			}
		});
	}
</script>
</head>
<body>

	 <jsp:include page="../head.jsp"></jsp:include>
	<div style="height: 20px; width: 100%;"></div>
	<div class="easyui-tabs"
		style="width: 900px; height: 600px; margin: 0 auto">
		<div title="已删标记" style="padding: 10px">
			<table id="DeleteMark">
			</table>
		</div>
		<div title="已删会员" style="padding: 10px">
			<table id="DeleteMember">
			</table>
		</div>
		<div title="已删商家" style="padding: 10px">
			<table id="DeleteMerchant">
			</table>
		</div>
	</div>
	<div id="MemberDeleted" style="padding: 5px; height: auto">
		<div style="font-weight: normal;">
			起: <input id="sTimeForDeleteMember" class="easyui-datebox"
				style="width: 100px"> 止: <input id="eTimeForDeleteMember"
				class="easyui-datebox" style="width: 100px">&nbsp;&nbsp;会员名称：<input
				id="textDeleteMember" class="easyui-textbox">&nbsp;&nbsp;排序方式:
			<select id="MemberSortType" class="easyui-combobox"
				panelHeight="auto" style="width: 100px">
				<option value="1">删除时间升序</option>
				<option value="2">删除时间倒序</option>
			</select>&nbsp;&nbsp;
			<button class="easyui-linkbutton" style="margin-left:-10px;"
				onclick="queryByCondition('Member')">查询</button>
			&nbsp;
			<button class="easyui-linkbutton" style="margin-left:-10px;"
				onclick="allSelect('DeleteMember')">全选</button>
			&nbsp;
			<button class="easyui-linkbutton" style="margin-left:-10px;"
				onclick="restoreData('DeleteMember','RestoreMember','memberName')">恢复</button>
		</div>
	</div>
	<!--会员统计任务栏  -->
	<!--商家统计任务栏  -->
	<div id="MerchantDeleted" style="padding: 5px; height: auto">
		<div style="font-weight: normal;">
			起: <input id="sTimeForDeleteMerchant" class="easyui-datebox"
				style="width: 100px"> 止: <input id="eTimeForDeleteMerchant"
				class="easyui-datebox" style="width: 100px">&nbsp;&nbsp;商家名称：<input
				id="textDeleteMerchant" class="easyui-textbox">&nbsp;&nbsp;排序方式:
			<select id="MerchantSortType" class="easyui-combobox"
				panelHeight="auto" style="width: 100px">
				<option value="1">升序</option>
				<option value="2">倒序</option>
			</select>&nbsp;&nbsp;
			<button class="easyui-linkbutton" style="margin-left:-10px;"
				onclick="queryByCondition('Merchant')">查询</button>
			&nbsp;
			<button class="easyui-linkbutton" style="margin-left:-10px;"
				onclick="allSelect('DeleteMerchant')">全选</button>
			&nbsp;
			<button class="easyui-linkbutton" style="margin-left:-10px;"
				onclick="restoreData('DeleteMerchant','RestoreMerchant','merchantName')">恢复</button>
		</div>
	</div>
	<!--商家统计任务栏  -->
	<!--标记统计任务栏  -->
	<div id="MarkDeleted" style="padding: 5px; height: auto">
		<div style="font-weight: normal;">
			起: <input id="sTimeForDeleteMark" class="easyui-datebox"
				style="width: 100px"> 止: <input id="eTimeForDeleteMark"
				class="easyui-datebox" style="width: 100px">&nbsp;&nbsp;商家名称：<input
				id="textDeleteMark" class="easyui-textbox">&nbsp;&nbsp;排序方式:
			<select id="MarkSortType" class="easyui-combobox" panelHeight="auto"
				style="width: 100px">
				<option value="1">删除时间升序</option>
				<option value="2">删除时间倒序</option>
			</select>&nbsp;&nbsp;
			<button class="easyui-linkbutton"  style="margin-left:-10px;"
				onclick="queryByCondition('Mark')">查询</button>
			&nbsp;
			<button style="margin-left:-10px;" class="easyui-linkbutton" onclick="allSelect('DeleteMark')">全选</button>
			&nbsp;
			<button class="easyui-linkbutton" style="margin-left:-10px;"
				onclick="restoreData('DeleteMark','RestoreMark','mark')">恢复</button>
		</div>
	</div>
	<!--标记统计任务栏  -->
	<jsp:include page="../foot.jsp"></jsp:include>
</body>
</html>