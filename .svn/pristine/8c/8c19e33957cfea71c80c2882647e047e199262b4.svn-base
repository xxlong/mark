<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="/imagemark/css/easyui.css">
<link rel="stylesheet" type="text/css" href="/imagemark/css/head.css">

<script type="text/javascript" src="/imagemark/js/jquery.min.js"></script>
<script type="text/javascript" src="/imagemark/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/imagemark/js/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
var urlDefault="http://192.168.1.127:8899/imagemark/member/";
var defaultPage=1;
$(function() {
	createCommentTable();
});
function createCommentTable(){
	$('#memberCommentTable')
	.datagrid(
			{
				url:"/imagemark/member/getMemberStatisticalComments?"+"memberName="+$('#userName').text(),
				queryParams : {
					/* 'memberName' : $('#userName').text(), */
					'sortField':0,
					'filter':0,
					'sortType':$('#commentSortType').combobox('getValue')
				},
				loadFilter: function(data){
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
				toolbar : '#memberComment',
				width : 820,
				height : 550,
				rownumbers : true,
				autoRowHeight : true,
				pagination : true,
				pageSize : 10,
				columns : [ [
				             {
									field : 'ch',
									checkbox: 'true'
								},
								{
									field : '_id',
									title:'_id',
									hidden: 'true'
								},
						{
							field : 'picName',
							title : '评论对象',
							width : 60,
							formatter : function(value, row, index) {
								var pic = "http://192.168.1.127/thumb/"
										+ row.picName + ".png";
								return '<a href="'+row.componentLink+'"><img src="'+pic+'" width="40" height="40"/></a>';
							}
						}, {
							field : 'commentTime',
							title : '评论时间',
							width : 100
						}, {
							field : 'comment',
							title : '评分',
							width : 100
						}, 
						] ]

			});
}
function queryByCondition(){
	$('#memberCommentTable').datagrid('load',{
		'memberName' : $('#userName').text(),
		'sortField':0,
		'filter':0,
		'sortType':$('#commentSortType').combobox('getValue')
	});
}
</script>
</head>
<body>
<jsp:include page="../head.jsp"></jsp:include>
<div style="height: 20px; width: 100%;"></div>
<div style="margin: 0 auto; width: 898px">
	<table id="memberCommentTable">
	</table>
</div>
<div id="memberComment" style="padding: 5px; height: auto">
	<div class="textfont">
		排序方式: <select id="commentSortType" class="easyui-combobox"
			panelHeight="auto" style="width: 100px">
			<option value="1">评论时间升序</option>
			<option value="2">评论时间倒序</option>
		</select>&nbsp;&nbsp;
		<button class="easyui-linkbutton" onclick="queryByCondition()">查询</button>
	</div>
</div>
<jsp:include page="../foot.jsp"></jsp:include>
</body>
</html>