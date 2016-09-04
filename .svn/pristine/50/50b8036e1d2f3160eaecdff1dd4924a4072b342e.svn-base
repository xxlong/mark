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
	var pageSizeDefalut = 20;
	var sortTypeDefalut = 1;
	var pageNumberDefalut = 1;
	var newWindow;
	var urlDefault = "/imagemark/administrator/";
	$(function() {
		create_verify_table();
		create_check_table();
		/* create_Url_table(); */
		/* getData("Verify", "getNotVerifyMark");
		getData("Check", "getComplainedMark"); */
	});
	/* function create_Url_table() {
		$('#urlTable')
				.datagrid(
						{
							url : "/imagemark/administrator/getVerifyUrl",
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
							toolbar : '#VerifyUrl',
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
										field : 'url',
										title : '标记图片',
										width : 100,
										formatter : function(value, row, index) {
											var url = row.url;
											return '<a href="#" onclick="subWrite(&quot;'
													+ url + '&quot;)">查看图片</a>';
										}
									}, {
										field : 'count',
										title : '位置数',
										width : 100
									} ] ]

						});
	} */
	function queryByCondition() {
		$('#urlTable').datagrid('load', {});
	}
	function create_verify_table() {
		$('#VerifyTable')
				.datagrid(
						{
							url : "/imagemark/administrator/getNotVerifyMark",
							queryParams : {
								"startTime" : $('#sTimeForVerify').datebox(
										'getValue'),
								"endTime" : $('#eTimeForVerify').datebox(
										'getValue'),
								'sortType' : 1
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
							toolbar : '#markVerify',
							width : 820,
							height : 550,
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
										formatter : function(value, row, index) {
											var pic = "http://222.214.218.140:90/thumb/"
													+ row._id + ".png";
											return '<a href="'+row.link+'"><img src="'+pic+'" width="40" height="40"/></a>';
										}
									},
									{
										field : 'url',
										title : '标记图片',
										width : 100,
										formatter : function(value, row, index) {
											var url = row.url;
											var node = row.node_id;
											return '<a href="#" onclick="subWrite(&quot;'
													+ url
													+ '&quot;,&quot;'
													+ node
													+ '&quot;)">查看图片</a>';
										}
									}, {
										field : 'merchantName',
										title : '商家名称',
										width : 100
									}, {
										field : 'componentName',
										title : '商品名称',
										width : 100
									}, {
										field : 'componentPrice',
										title : '商品价格',
										width : 100
									}, {
										field : 'componentType',
										title : '商品类型',
										width : 100
									}, {
										field : 'markDate',
										title : '标记时间',
										width : 100
									}, ] ]

						});
	}
	function create_check_table() {
		$('#CheckTable')
				.datagrid(
						{
							url : "/imagemark/administrator/getComplainedMark",
							queryParams : {
								"startTime" : $('#sTimeForCheck').datebox(
										'getValue'),
								"endTime" : $('#eTimeForCheck').datebox(
										'getValue'),
								'sortType' : 1
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
							toolbar : '#markCheck',
							width : 820,
							height : 550,
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
										formatter : function(value, row, index) {
											var pic = "http://222.214.218.140:90/thumb/"
													+ row._id + ".png";
											return '<a href="'+row.link+'"><img src="'+pic+'" width="40" height="40"/></a>';
										}
									},
									{
										field : 'url',
										title : '标记图片',
										width : 100,
										formatter : function(value, row, index) {
											var url = row.url;
											var node = row.node_id;
											return '<a href="#" onclick="subWrite(&quot;'
													+ url
													+ '&quot;,&quot;'
													+ node
													+ '&quot;)">查看图片</a>';
										}
									}, {
										field : 'merchantName',
										title : '商家名称',
										width : 100
									}, {
										field : 'componentName',
										title : '商品名称',
										width : 100
									}, {
										field : 'componentPrice',
										title : '商品价格',
										width : 100
									}, {
										field : 'componentType',
										title : '商品类型',
										width : 100
									}, {
										field : 'complainReason',
										title : '投诉原因',
										width : 100
									}, {
										field : 'complainTime',
										title : '投诉时间',
										width : 100
									}, {
										field : 'complainId',
										title : '',
										hidden : true,
										width : 100
									}, ] ]

						});
	}
	function queryByCondition(target) {
		$('#' + target + 'Table').datagrid('load', {
			"startTime" : $('#sTimeFor' + target).datebox('getValue'),
			"endTime" : $('#eTimeFor' + target).datebox('getValue'),
			'sortType' : $('#' + target + 'SortType').combobox('getValue')
		});
	}
	function search(target, url) {
		var sTime = $('#sTimeFor' + target).datebox('getValue');
		var eTime = $('#eTimeFor' + target).datebox('getValue');
		var Type = $('#' + target + 'SortType').combobox('getValue');
		$.ajax({
			url : urlDefault + url,
			data : {
				startTime : sTime,
				endTime : eTime,
				sortType : Type,
				pageNumber : pageNumberDefalut
			},
			dataType : "json",
			success : function(data) {
				if (data.status != 0) {
					$('#' + target + 'Table').datagrid('loadData', {
						total : 0,
						rows : []
					});
				} else {
					var obj = data.data;
					if (obj.total != 0) {
						$('#' + target + 'Table').datagrid('loadData', obj);
					} else {
						$('#' + target + 'Table').datagrid('loadData', {
							total : 0,
							rows : []
						});
					}
				}
			}
		});
	}
	function verify(url, status) {
		var list = new Array();
		var data = $('#VerifyTable').datagrid('getChecked');
		for (var i = 0; i < data.length; i++) {
			list[i] = data[i]._id;
		}
		$.ajax({
			url : urlDefault + url + "?data=" + list + "&status=" + status,
			/* data :{data:list}, */
			dataType : "json",
			success : function(data) {
				if (data.status != 0) {
					$('#VerifyTable').datagrid('loadData', {
						total : 0,
						rows : []
					});
				} else {
					queryByCondition("Verify");
				}
			}
		});

	}
	function check(url, status) {
		var list = new Array();
		var data = $('#CheckTable').datagrid('getChecked');
		for (var i = 0; i < data.length; i++) {
			list[i] = data[i].complainId;
		}
		$.ajax({
			url : urlDefault + url + "?data=" + list + "&status=" + status,
			/* data :{data:list}, */
			dataType : "json",
			success : function(data) {
				if (data.status != 0) {
					$('#CheckTable').datagrid('loadData', {
						total : 0,
						rows : []
					});
				} else {
					queryByCondition("Check");
				}
			}
		});

	}
	function allSelect(target) {
		$('#' + target).datagrid('selectAll');
		$('#' + target).datagrid('uncheckRow');
		return true;
	}
	function makeWindow() {
		alert('test');
	}
	//判断新窗口是否关闭，如果关闭重新打开  
	function subWrite(url, node) {
		var width = document.body.clientWidth - 500;
		var height = document.body.clientHeight - 300;
		var pic = "http://222.214.218.140:9090/image.php?url=" + url;
		newWindow = window.open("", "_blank", 'height=400,width=380,top='
				+ height + ',left=' + width
				+ ', toolbar=yes, menubar=yes,location=yes, status=yes');
		newWindow.document
				.write('<script type="text/javascript" src="/imagemark/js/jquery.min.js"><'+'/script><script type="text/javascript" src="/imagemark/js/mark_icon.js"><'+'/script>')
		newWindow.document
				.write('<img id="img" style="width:300" src=\''
						+ pic+'\' /><p id="nodeId" style="display:none">'
						+ node + '</p>')
		newWindow.document.close();

	}
</script>
</head>
<body>
	<jsp:include page="../head.jsp"></jsp:include>
	<div style="height: 20px; width: 100%;"></div>
	<div class="easyui-tabs"
		style="width: 900px; height: 600px; margin: 0 auto">
		<!-- <div title="审核">
			<table id="urlTable">
			</table>
		</div> -->
		<div title="标记审核" style="padding: 10px">
			<table id="VerifyTable">
			</table>
		</div>
		<div title="标记复审" style="padding: 10px">
			<table id="CheckTable">
			</table>
		</div>
	</div>
	<!-- <div id="VerifyUrl">
		<button onclick="queryByCondition()">完成</button>
	</div> -->
	<div id="markVerify" style="padding: 5px; height: auto">
		<div style="font-weight: normal;">
			起: <input id="sTimeForVerify" class="easyui-datebox"
				style="width: 100px"> 止: <input id="eTimeForVerify"
				class="easyui-datebox" style="width: 100px">&nbsp;&nbsp;排序方式:
			<select id="VerifySortType" class="easyui-combobox"
				panelHeight="auto" style="width: 100px">
				<option value="1">标记时间升序</option>
				<option value="2">标记时间倒序</option>
			</select>&nbsp;&nbsp;
			<button style="margin-left: -3px;" class="easyui-linkbutton"
				onclick="queryByCondition('Verify')">查询</button>
			&nbsp;&nbsp;&nbsp;&nbsp;
			<button style="margin-left: -3px;" class="easyui-linkbutton"
				onclick="allSelect('VerifyTable')">全选</button>
			&nbsp;&nbsp;&nbsp;&nbsp;
			<button style="margin-left: -3px;" class="easyui-linkbutton"
				onclick="verify('VerifyMark','2')">通过</button>
			<button style="margin-left: -3px;" class="easyui-linkbutton"
				onclick="verify('VerifyMark','6')">删除</button>

		</div>
	</div>
	<div id="markCheck" style="padding: 5px; height: auto">
		<div style="font-weight: normal;">
			起: <input id="sTimeForCheck" class="easyui-datebox"
				style="width: 100px"> 止: <input id="eTimeForCheck"
				class="easyui-datebox" style="width: 100px">&nbsp;&nbsp;排序方式:
			<select id="CheckSortType" class="easyui-combobox" panelHeight="auto"
				style="width: 100px">
				<option value="1">标记时间升序</option>
				<option value="2">标记时间倒序</option>
			</select>&nbsp;&nbsp;
			<button class="easyui-linkbutton" onclick="queryByCondition('Check')">查询</button>
			&nbsp;&nbsp;&nbsp;&nbsp;
			<button style="margin-left: -3px;" class="easyui-linkbutton"
				onclick="allSelect('CheckTable')">全选</button>
			&nbsp;&nbsp;&nbsp;&nbsp;
			<button style="margin-left: -3px;" class="easyui-linkbutton"
				onclick="check('VerifyComplain','2')">通过</button>
			<button style="margin-left: -3px;" class="easyui-linkbutton"
				onclick="check('VerifyComplain','6')">删除</button>
		</div>
	</div>
	<div id="urlWindow"></div>
	<jsp:include page="../foot.jsp"></jsp:include>
</body>
</html>