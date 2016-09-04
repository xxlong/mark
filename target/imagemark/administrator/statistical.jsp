<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>统计管理</title>
<link rel="stylesheet" type="text/css" href="/imagemark/css/head.css">
<link rel="stylesheet" type="text/css" href="/imagemark/css/easyui.css">
<script type="text/javascript" src="/imagemark/js/jquery.min.js"></script>
<script type="text/javascript" src="/imagemark/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/imagemark/js/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="/imagemark/js/highcharts.js"></script>
<script type="text/javascript" src="/imagemark/js/exporting.js"></script>
<script type="text/javascript">
	var pageSizeDefalut = 10;
	var urlDefault = "http://192.168.1.127:8899/imagemark/administrator/";
	$(function() {
		create_mark_table();
		create_merchant_table();
		create_member_table();
		create_comment_table();
		create_Click_table();
		totalMember("Member", "getNumForMember");
		statisPerMonth("increseMemberPerMonth", "", "", "memberChart", true);
		$('#statisticalTab').tabs(
				{
					border : false,
					selected : 0,
					onSelect : function(title) {
						if (title == "会员统计") {
							totalMember("Member", "getNumForMember");
							statisPerMonth("increseMemberPerMonth", "", "",
									"memberChart", true);
						} else if (title == "商家统计") {
							totalMember("Merchant", "getNumForMerchant");
							statisPerMonth("increseMerchantPerMonth", "", "",
									"merchantChart", true);
						} else if (title == "标记统计") {
							totalMember("Mark", "getNumForMark");
							statisPerMonth("increseMarkPerMonth", "", "",
									"markChart", true);
							statisPerMonth("increseMarkByTypePerMonth", "", "",
									"markTypeChart", false);
						} else if (title == "评论统计") {
							totalMember("Comment", "getNumForComment");
							statisPerMonth("increseCommentPerMonth", "", "",
									"commentChart", true);
						} else {
							totalMember("Click", "getNumForClick");
							statisPerMonth("increseClickPerMonth", "", "",
									"clickChart", true);
						}
					}
				});
	});
	function create_mark_table() {
		$('#MarkTable')
				.datagrid(
						{
							toolbar : '#forMark',
							width : 820,
							height : 550,
							singleSelect : true,
							autoRowHeight : true,
							pagination : true,
							pageSize : 10,
							url : "/imagemark/administrator/getStatisticAnalysisOnMarks",
							queryParams : {
								"startTime" : $('#sTimeForMark').datebox(
										'getValue'),
								"endTime" : $('#eTimeForMark').datebox(
										'getValue'),
								"sortField" : $('#MarkSortField').combobox(
										'getValue'),
								"sortType" : $('#MarkSortType').combobox(
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
							columns : [ [
									{
										field : '_id',
										title : '商家记录',
										width : 60,
										align : 'center',
										formatter : function(value, row, index) {
											var pic = "http://192.168.1.127/thumb/"
													+ row._id + ".png";
											return '<a href="'+row.link+'"><img src="'+pic+'" width="40" height="40"/></a>';
										}
									}, {
										field : 'markDate',
										title : '标记时间',
										align : 'center',
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
									}, /* {
										field : 'unstatisfyComment',
										title : '差评数',
										align : 'center',
										width : 100
									}, {
										field : 'statisfyComment',
										title : '好评数',
										align : 'center',
										width : 100
									},  */{
										field : 'merchantName',
										title : '商家名',
										align : 'center',
										width : 100
									}, ] ]

						});
	}
	function create_merchant_table() {
		$('#MerchantTable').datagrid({
			toolbar : '#forMerchant',
			width : 820,
			height : 550,
			singleSelect : true,
			rownumbers : true,
			autoRowHeight : true,
			pagination : true,
			pageSize : 10,
			url : "/imagemark/administrator/getStatisticAnalysisOnMerchants",
			queryParams : {
				"startTime" : $('#sTimeForMerchant').datebox('getValue'),
				"endTime" : $('#eTimeForMerchant').datebox('getValue'),
				"sortField" : $('#MerchantSortField').combobox('getValue'),
				"sortType" : $('#MerchantSortType').combobox('getValue')
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
			columns : [ [ {
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
			}, */ ] ]

		});
	}
	function create_member_table() {
		$('#MemberTable').datagrid({
			toolbar : '#forMember',
			width : 820,
			height : 550,
			singleSelect : true,
			rownumbers : true,
			autoRowHeight : true,
			pagination : true,
			pageSize : 10,
			url : "/imagemark/administrator/getStatisticAnalysisOnMembers",
			queryParams : {
				"startTime" : $('#sTimeForMember').datebox('getValue'),
				"endTime" : $('#eTimeForMember').datebox('getValue'),
				"sortField" : $('#MemberSortField').combobox('getValue'),
				"sortType" : $('#MemberSortType').combobox('getValue')
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
			columns : [ [ {
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
			}/* , {
				field : 'statisfyComment',
				align : 'center',
				title : '好评数',
				width : 100
			}, {
				field : 'unstatisfyComment',
				align : 'center',
				title : '差评数',
				width : 100
			},  */] ]

		});
	}
	function create_comment_table() {
		$('#CommentTable')
				.datagrid(
						{
							toolbar : '#forComment',
							width : 820,
							height : 550,
							singleSelect : true,
							autoRowHeight : true,
							pagination : true,
							pageSize : 10,
							url : "/imagemark/administrator/getStatisticAnalysisOnComments",
							queryParams : {
								"startTime" : $('#sTimeForComment').datebox(
										'getValue'),
								"endTime" : $('#eTimeForComment').datebox(
										'getValue'),
								"sortField" : $('#CommentSortField').combobox(
										'getValue'),
								"sortType" : $('#CommentSortType').combobox(
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
							columns : [ [
									{
										field : '_id',
										title : '评论ID',
										hidden : 'true'
									},
									{
										field : 'picName',
										title : '评论对象',
										align : 'center',
										formatter : function(value, row, index) {
											var pic = "http://192.168.1.127/thumb/"
													+ row.picName + ".png";
											return '<a href="'+row.componentLink+'"><img src="'+pic+'" width="40" height="40"/></a>';
										},
										width : 100
									}, {
										field : 'commentTime',
										title : '评论时间',
										align : 'center',
										width : 100
									}, {
										field : 'comment',
										title : '评分',
										align : 'center',
										width : 100
									}, {
										field : 'memberName',
										title : '评论会员',
										align : 'center',
										width : 100
									}, ] ]

						});
	}
	function create_Click_table() {
		$('#ClickTable').datagrid({
			toolbar : '#forClick',
			width : 820,
			height : 550,
			singleSelect : true,
			autoRowHeight : true,
			pagination : true,
			pageSize : 10,
			url : "/imagemark/administrator/getStatisticAnalysisOnClick",
			queryParams : {
				"startTime" : $('#sTimeForClick').datebox('getValue'),
				"endTime" : $('#eTimeForClick').datebox('getValue'),
				"sortField" : $('#ClickSortField').combobox('getValue'),
				"sortType" : $('#ClickSortType').combobox('getValue')
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
			columns : [ [ {
				field : 'domain',
				title : '域名',
				align : 'center',
				width : 100
			}, {
				field : 'count',
				title : '点击数量',
				align : 'center',
				width : 100
			}, ] ]

		});
	}

	function createChart(target, data) {
		$('#' + target)
				.highcharts(
						{
							chart : {
								type : 'column'
							},
							title : {
								text : '每月增量'
							},
							xAxis : {
								categories : [ 'Jan', 'Feb', 'Mar', 'Apr',
										'May', 'Jun', 'Jul', 'Aug', 'Sep',
										'Oct', 'Nov', 'Dec' ]
							},
							yAxis : {
								min : 0,
								title : {
									text : '数量'
								}
							},
							tooltip : {
								headerFormat : '<span style="font-size:10px">{point.key}</span><table>',
								pointFormat : '<tr><td style="color:{series.color};padding:0">{series.name}: </td>'
										+ '<td style="padding:0"><b>{point.y:.1f}</b></td></tr>',
								footerFormat : '</table>',
								shared : true,
								useHTML : true
							},
							plotOptions : {
								column : {
									pointPadding : 0.2,
									borderWidth : 0
								}
							},
							series : [ {
								name : '月份',
								data : data
							} ]
						});
	}
	function createTypeChart(target, data) {
		$('#' + target)
				.highcharts(
						{
							chart : {
								type : 'column'
							},
							title : {
								text : '每月增量'
							},
							xAxis : {
								categories : [ 'Jan', 'Feb', 'Mar', 'Apr',
										'May', 'Jun', 'Jul', 'Aug', 'Sep',
										'Oct', 'Nov', 'Dec' ]
							},
							yAxis : {
								min : 0,
								title : {
									text : '数量'
								}
							},
							tooltip : {
								headerFormat : '<span style="font-size:10px">{point.key}</span><table>',
								pointFormat : '<tr><td style="color:{series.color};padding:0">{series.name}: </td>'
										+ '<td style="padding:0"><b>{point.y:.1f}</b></td></tr>',
								footerFormat : '</table>',
								shared : true,
								useHTML : true
							},
							plotOptions : {
								column : {
									pointPadding : 0.2,
									borderWidth : 0
								}
							},
							series : data
						});
	}
	/*调用函数 */
	/*target   */
	function getDataForMember(target, url, sortField) {
		var sTime = $('#sTimeFor' + target).val();
		var eTime = $('#eTimeFor' + target).val();
		if (sTime == undefined)
			sTime = "";
		if (eTime == undefined)
			eTime = "";
		$.ajax({
			url : urlDefault + url,
			data : {
				starttime : sTime,
				endtime : eTime,
				sortField : sortField,
				sortType : 1,
				pageNumber : 1,
				pageSize : pageSizeDefalut
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
					$('#' + target + 'Table').datagrid('loadData', obj);
				}
			}
		});
	}
	function totalMember(target, url) {
		$.ajax({
			url : urlDefault + url,
			dataType : "json",
			success : function(data) {
				$('#total' + target).html(data[0].total);
				$('#newAdd' + target).html(data[1].month);
			}
		});
	}
	/* function searchMember(target, url) {
		var sTime = $('#sTimeFor' + target).datebox('getValue');
		var eTime = $('#eTimeFor' + target).datebox('getValue');
		var Type = $('#' + target + 'SortType').combobox('getValue');
		var Field = $('#' + target + 'SortField').combobox('getValue');
		$.ajax({
			url : urlDefault + url,
			type:"post",
			data : {
				starttime : sTime,
				endtime : eTime,
				sortField : Field,
				sortType : Type,
				pageNumber : 1,
				pageSize : pageSizeDefalut
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
					$('#' + target + 'Table').datagrid('loadData', obj);
				}
			}
		});
	} */
	function queryByCondition(target) {
		$('#' + target + 'Table').datagrid('load', {
			"starttime" : $('#sTimeFor' + target).datebox('getValue'),
			"endtime" : $('#eTimeFor' + target).datebox('getValue'),
			"sortField" :$('#' + target + 'SortField').combobox('getValue'),
			"sortType": $('#' + target + 'SortType').combobox('getValue')
		});
	}
	function statisPerMonth(url, year, month, chartTarget, flag) {
		var date = new Date();
		var myYear, myMonth;
		var myData;
		var list = new Array();
		var size;
		if (year == "" && month == "") {
			myYear = date.getFullYear();
			myMonth = date.getMonth() + 1;
		}
		$.ajax({
			url : urlDefault + url,
			data : {
				year : myYear,
				month : myMonth
			},
			dataType : "json",
			success : function(data) {
				if (data.status == 0) {
					myData = data.data.rows;
					size = data.data.total;
					if (flag) {
						for (var i = 0; i < parseInt(size); i++) {
							list[i] = myData[i].increaseMent;
						}
						createChart(chartTarget, list);
					} else {
						createTypeChart(chartTarget, myData);
					}
				}
			}
		});
	}
</script>
</head>
<body>

	<jsp:include page="../head.jsp"></jsp:include>

	<div style="height: 20px; width: 100%;"></div>
	<div class="easyui-tabs" id="statisticalTab"
		style="width: 900px; height: 600px; margin-top: 13px; margin: 0 auto">
		<!-- 会员统计模块 -->
		<div title="会员统计" style="padding: 10px">
			<div class="easyui-accordion" style="width: 850px; height: 500px;">
				<div title="每月增量" style="padding: 10px;">
					<div id="memberChart"></div>
				</div>
				<div title="会员统计">
					<div>
						<label>系统会员总量:</label><label id="totalMember"></label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<label>当月增量：</label><label
							id="newAddMember"></label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</div>
					<hr>
					<table id="MemberTable">
					</table>
				</div>
			</div>
		</div>
		<!-- 会员统计模块 -->
		<!-- 商家统计模块 -->
		<div title="商家统计" style="padding: 10px">
			<div class="easyui-accordion" style="width: 850px; height: 500px;">
				<div title="每月增量">
					<div id="merchantChart"></div>
				</div>
				<div title="商家统计">
					<label>系统商家总量:</label><label id="totalMerchant"></label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<label>当月增量：</label><label
						id="newAddMerchant"></label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<!-- <button class="easyui-linkbutton" onclick="statisPerMonth()">每月增量</button> -->
					<hr>
					<table id="MerchantTable">
					</table>
				</div>
			</div>
		</div>
		<!-- 商家统计模块 -->
		<!-- 标记统计模块 -->
		<div title="标记统计" style="padding: 10px">
			<div class="easyui-accordion" style="width: 850px; height: 500px;">
				<div title="每月增量">
					<div id="markChart"></div>
				</div>
				<div title="分类增量">
					<div id="markTypeChart"></div>
				</div>
				<div title="标记统计">
					<label>系统标记总量:</label><label id="totalMark"></label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<label>当月增量：</label><label
						id="newAddMark"></label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<!-- <button class="easyui-linkbutton" onclick="statisPerMonth()">类别统计</button> -->
					<hr>
					<table id="MarkTable">
					</table>
				</div>

			</div>
		</div>
		<!-- 标记统计模块 -->
		<!-- 评论统计模块 -->
		<div title="评论统计" style="padding: 10px">
			<div class="easyui-accordion" style="width: 850px; height: 500px;">
				<div title="每月增量">
					<div id="commentChart"></div>
				</div>
				<div title="评论统计">
					<label>系统评论总量:</label><label id="totalComment"></label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<label>当月增量：</label><label
						id="newAddComment"></label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<hr>
					<table id="CommentTable">
					</table>
				</div>
			</div>
		</div>
		<div title="点击统计" style="padding: 10px">
			<div class="easyui-accordion" style="width: 850px; height: 500px;">
				<div title="每月增量">
					<div id="clickChart"></div>
				</div>
				<div title="域名统计">
					<label>点击总量:</label><label id="totalClick"></label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<label>当月增量：</label><label
						id="newAddClick"></label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<hr>
					<table id="ClickTable">
					</table>
				</div>
			</div>
		</div>
		<!-- 评论统计模块 -->
	</div>
	<!--会员统计任务栏  -->
	<div id="forMember" style="padding: 5px; height: auto">
		<div>
			起: <input id="sTimeForMember" class="easyui-datebox"
				style="width: 100px"> 止: <input id="eTimeForMember"
				class="easyui-datebox" style="width: 100px">&nbsp;&nbsp;排序方式:
			<select id="MemberSortType" class="easyui-combobox"
				panelHeight="auto" style="width: 100px">
				<option value="1">升序</option>
				<option value="2">倒序</option>
			</select>&nbsp;&nbsp;排序条件： <select id="MemberSortField"
				class="easyui-combobox" panelHeight="auto" style="width: 100px">
				<option value="1">会员积分</option>
				<option value="2">点击数</option>
				<option value="3">评论数</option>
				<option value="4">注册时间</option>
			</select>
			<!-- searchMember('Member','getStatisticAnalysisOnMembers') -->
			<button class="easyui-linkbutton"
				onclick="queryByCondition('Member')">查询</button>
		</div>
	</div>
	<!--会员统计任务栏  -->
	<!--商家统计任务栏  -->
	<div id="forMerchant" style="padding: 5px; height: auto">
		<div>
			起: <input id="sTimeForMerchant" class="easyui-datebox"
				style="width: 100px"> 止: <input id="eTimeForMerchant"
				class="easyui-datebox" style="width: 100px">&nbsp;&nbsp;排序方式:
			<select id="MerchantSortType" class="easyui-combobox"
				panelHeight="auto" style="width: 100px">
				<option value="1">升序</option>
				<option value="2">倒序</option>
			</select>&nbsp;&nbsp;排序条件： <select id="MerchantSortField"
				class="easyui-combobox" panelHeight="auto" style="width: 100px">
				<option value="1">商家积分</option>
				<option value="2">标记数量</option>
				<option value="3">点击数</option>
				<option value="4">评论数</option>
				<option value="5">注册时间</option>
			</select>
			<!-- "searchMember('Merchant','getStatisticAnalysisOnMerchants')" -->
			<button class="easyui-linkbutton"
				onclick="queryByCondition('Merchant')">查询</button>
		</div>
	</div>
	<!--商家统计任务栏  -->
	<!--标记统计任务栏  -->
	<div id="forMark" style="padding: 5px; height: auto">
		<div>
			起: <input id="sTimeForMark" class="easyui-datebox"
				style="width: 100px"> 止: <input id="eTimeForMark"
				class="easyui-datebox" style="width: 100px">&nbsp;&nbsp;排序方式:
			<select id="MarkSortType" class="easyui-combobox" panelHeight="auto"
				style="width: 100px">
				<option value="1">升序</option>
				<option value="2">倒序</option>
			</select>&nbsp;&nbsp;排序条件： <select id="MarkSortField" class="easyui-combobox"
				panelHeight="auto" style="width: 100px">
				<option value="1">标记时间</option>
				<option value="2">标记信用</option>
				<option value="3">点击数</option>
				<option value="4">评论数</option>
			</select>
			<!-- "searchMember('Mark','getStatisticAnalysisOnMarks')" -->
			<button class="easyui-linkbutton"
				onclick="queryByCondition('Mark')">查询</button>
		</div>
	</div>
	<!--标记统计任务栏  -->
	<!--评论统计任务栏  -->
	<div id="forComment" style="padding: 5px; height: auto">
		<div>
			起: <input id="sTimeForComment" class="easyui-datebox"
				style="width: 100px"> 止: <input id="eTimeForComment"
				class="easyui-datebox" style="width: 100px"> 排序方式: <select
				id="CommentSortType" class="easyui-combobox" panelHeight="auto"
				style="width: 100px">
				<option value="1">升序</option>
				<option value="2">倒序</option>
			</select>排序条件<select id="CommentSortField" class="easyui-combobox"
				panelHeight="auto" style="width: 100px">
				<option value="1">评论时间</option>
			</select>
			<button class="easyui-linkbutton" onclick="">查询</button>
		</div>
	</div>
	<div id="forClick" style="padding: 5px; height: auto">
		<div>
			起: <input id="sTimeForClick" class="easyui-datebox"
				style="width: 100px"> 止: <input id="eTimeForClick"
				class="easyui-datebox" style="width: 100px"> 排序方式: <select
				id="ClickSortType" class="easyui-combobox" panelHeight="auto"
				style="width: 100px">
				<option value="1">升序</option>
				<option value="2">倒序</option>
			</select>排序条件<select id="ClickSortField" class="easyui-combobox"
				panelHeight="auto" style="width: 100px">
				<option value="1">点击数量</option>
			</select>
			<button class="easyui-linkbutton" onclick="">查询</button>
		</div>
	</div>
	<!--评论统计任务栏  -->
	<jsp:include page="../foot.jsp"></jsp:include>
</body>
</html>