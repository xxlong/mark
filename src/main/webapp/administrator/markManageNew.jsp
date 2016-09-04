<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

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
		create_check_table();
		create_Url_table();
	});
	function create_Url_table() {
		$('#urlTable')
				.datagrid(
						{
							url : "/imagemark/administrator/getVerifyUrl",
							loadFilter : function(data) {
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
	}
	function queryByCondition(){
		$('#urlTable').datagrid('load',{});
	}
	function create_check_table() {
		$('#CheckTable')
				.datagrid(
						{
							url:"/imagemark/administrator/getComplainedMark",
							queryParams : {
								"startTime" : $('#sTimeForCheck').datebox('getValue'),
								"endTime" : $('#eTimeForCheck').datebox('getValue'),
								'sortType' : 1
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
											var pic = "http://222.214.218.140/thumb/"
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
											return '<a href="#" onclick="subWrite(&quot;'
													+ url + '&quot;)">查看图片</a>';
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
	function queryByCondition(target){
		$('#' + target).datagrid('load',{
			"startTime" : $('#sTimeFor'+target).datebox('getValue'),
			"endTime" : $('#eTimeFor'+target).datebox('getValue'),
			'sortType' : $('#'+target+'SortType').combobox('getValue')
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
					queryByCondition("VerifyTable");
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
	function makeNewWindow() {
		newWindow = window.open("", "_blank");
	}
	//判断新窗口是否关闭，如果关闭重新打开  
	function subWrite(url) {
		// make new window if someone has closed it  
		window.open("/imagemark/administrator/image.jsp?url=" + url, "_blank");
	}
</script>
</head>
<body>
<jsp:include page="../head.jsp"></jsp:include>
	<div style="height: 20px; width: 100%;"></div>
	<div class="easyui-tabs"
		style="width: 900px; height: 600px; margin: 0 auto">
		<div title="审核">
			<table id="urlTable">
			</table>
		</div>
		<div title="标记复审" style="padding: 10px">
			<table id="CheckTable">
			</table>
		</div>
	</div>
	<div id="VerifyUrl">
		<button onclick="queryByCondition()">完成</button>
	</div>
	<div id="markCheck" style="padding: 5px; height: auto">
		<div>
			起: <input id="sTimeForCheck" class="easyui-datebox"
				style="width: 100px"> 止: <input id="eTimeForCheck"
				class="easyui-datebox" style="width: 100px">&nbsp;&nbsp;排序方式:
			<select id="CheckSortType" class="easyui-combobox"
				panelHeight="auto" style="width: 100px">
				<option value="1">标记时间升序</option>
				<option value="2">标记时间倒序</option>
			</select>&nbsp;&nbsp;
			<button class="easyui-linkbutton"
				onclick="queryByCondition('Check')">查询</button>
			&nbsp;&nbsp;&nbsp;&nbsp;
			<button class="easyui-linkbutton" onclick="allSelect('CheckTable')">全选</button>
			&nbsp;&nbsp;&nbsp;&nbsp;
			<button class="easyui-linkbutton"
				onclick="check('VerifyComplain','2')">通过</button>
			<button class="easyui-linkbutton"
				onclick="check('VerifyComplain','6')">删除</button>
		</div>
	</div>
	<jsp:include page="../foot.jsp"></jsp:include>
</body>
</html>