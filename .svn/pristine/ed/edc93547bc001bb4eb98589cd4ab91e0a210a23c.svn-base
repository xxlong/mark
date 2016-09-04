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
	$(function() {
		create_merchantMark_table();
	});
	function create_merchantMark_table() {
		$('#merchantMarkTable')
				.datagrid(
						{
							url : "/imagemark/merchant/getStatisticalMarksByMerchantName?"
									+ "merchantName=" + $('#userName').text(),
							queryParams : {
								/* 'merchantName' : $('#userName').text(), */
								'sortType' : $('#markSortType').combobox(
										'getValue')
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
							toolbar : '#merchantMark',
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
										formatter : function(value, row, index) {
											var pic = "http://192.168.1.127/thumb/"
													+ row._id + ".png";
											return '<a href="'+row.link+'"><img src="'+pic+'" width="40" height="40"/></a>';
										}
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
									}, {
										field : 'trust',
										title : '标记信用',
										align : 'center',
										width : 100
									}, {
										field : 'clickTimes',
										title : '点击数',
										align : 'center',
										width : 100
									}, {
										field : 'commentTimes',
										title : '评论数',
										align : 'center',
										width : 100
									}, {
										field : 'unstatisfyComment',
										title : '差评数',
										align : 'center',
										width : 100
									}, {
										field : 'statisfyComment',
										title : '好评数',
										align : 'center',
										width : 100
									}, ] ]

						});
	}
	function queryByCondition() {
		$('#merchantMarkTable').datagrid('load', {
			'merchantName' : $('#userName').text(),
			'sortType' : $('#markSortType').combobox('getValue')
		});
	}
	function allSelect(target) {
		$('#' + target).datagrid('selectAll');
		$('#' + target).datagrid('uncheckRow');
		return true;
	}
	function deleteMark(url) {
		var list = new Array();
		var data = $('#merchantMarkTable').datagrid('getChecked');
		for (var i = 0; i < data.length; i++) {
			list[i] = data[i]._id;
		}
		$.ajax({
			url : urlDefault + url + "?data=" + list,
			/* data :{data:list}, */
			dataType : "json",
			success : function(data) {
				if (data.status != 0) {
					$('##merchantMarkTable').datagrid('loadData', {
						total : 0,
						rows : []
					});
				} else {
					queryByCondition();
				}
			}
		});
	}
</script>
</head>
<body style="font-weight: normal;">
	<jsp:include page="../head.jsp"></jsp:include>
	<div style="height: 20px; width: 100%;"></div>
	<div style="margin: 0 auto; width: 898px">
		<table id="merchantMarkTable">
		</table>
	</div>

	<div id="merchantMark" style="padding: 5px; height: auto">
		<div style="font-weight: normal;">
			排序方式: <select id="markSortType" class="easyui-combobox"
				panelHeight="auto" style="width: 100px">
				<option value="1">标记时间升序</option>
				<option value="2">标记时间倒序</option>
			</select>&nbsp;&nbsp;
			<button class="easyui-linkbutton" onclick="queryByCondition()">查询</button>
			<button class="easyui-linkbutton"
				onclick="allSelect('merchantMarkTable')">全选</button>
			<button class="easyui-linkbutton"
				onclick="deleteMark('deleteMarkInfo')">删除</button>
		</div>
	</div>
	<jsp:include page="../foot.jsp"></jsp:include>
</body>
</html>