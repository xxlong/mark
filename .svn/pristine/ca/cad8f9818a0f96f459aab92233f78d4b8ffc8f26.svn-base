
var urlDefault = "/imagemark/";

var verify_logo = urlDefault + "images/mark_logo.gif";
var marked_logo = urlDefault + "images/has_sign.png";


//程序入口
$(function(){
	var img_src = $("#img").attr("src");
	if(img_src!=undefined){
		var sub_pos = img_src.indexOf("=");
		var sub_end = img_src.indexOf("&");
		img_src = img_src.substring(sub_pos+1,sub_end);
		$.ajax({
			type: "GET",
			url: urlDefault + "administrator/getNodes",
			cache: false,
			data: {url:img_src},
			success: function(data){
				var response = JSON.parse(data);
				console.log(response);
				if(response.status == 0 ){	
					for(var i=0;i<response.data.total;i++){
						create_img_mark(response.data.rows[i]);
					}
				}
			}
		});
	}
});

//添加标记
function create_img_mark(mark_data){
	var mark_id = mark_data.nodeId;
	var mark_state = mark_data.status;
	var mark_type = mark_data.checkMark;
	var posX = parseInt($("#img").width()*mark_data.xPosition)+$("#img").offset().left;
	var posY = parseInt($("#img").height()*mark_data.yPosition)+$("#img").offset().top;
	//添加标记
	if(mark_id==$("#nodeId").html())
		var logo_src = verify_logo;
	else if(mark_id!=$("#nodeId").html())
		var logo_src = marked_logo;
	$('<img id="' + mark_id + '" class="mark_logo" mark="no" src="' + logo_src + '"/>').appendTo($('body'));
	$('#' + mark_id).css({
		"position": "absolute",
		"top": posY + "px",
		"left": posX + "px"
	});
	//resize后修订位置
	$("#img").resize(function(){
		var posX = parseInt($("#img").width()*mark_data.xPosition)+$("#img").offset().left;
		var posY = parseInt($("#img").height()*mark_data.yPosition)+$("#img").offset().top;
		$('#' + mark_id).css({
			"top": posY + "px",
			"left": posX + "px"
		});
	});
}






//重写jquery的resize事件
(function($,h,c){var a=$([]),e=$.resize=$.extend($.resize,{}),i,k="setTimeout",j="resize",d=j+"-special-event",b="delay",f="throttleWindow";e[b]=250;e[f]=true;$.event.special[j]={setup:function(){if(!e[f]&&this[k]){return false}var l=$(this);a=a.add(l);$.data(this,d,{w:l.width(),h:l.height()});if(a.length===1){g()}},teardown:function(){if(!e[f]&&this[k]){return false}var l=$(this);a=a.not(l);l.removeData(d);if(!a.length){clearTimeout(i)}},add:function(l){if(!e[f]&&this[k]){return false}var n;function m(s,o,p){var q=$(this),r=$.data(this,d);r.w=o!==c?o:q.width();r.h=p!==c?p:q.height();n.apply(this,arguments)}if($.isFunction(l)){n=l;return m}else{n=l.handler;l.handler=m}}};function g(){i=h[k](function(){a.each(function(){var n=$(this),m=n.width(),l=n.height(),o=$.data(this,d);if(m!==o.w||l!==o.h){n.trigger(j,[o.w=m,o.h=l])}});g()},e[b])}})(jQuery,this);


