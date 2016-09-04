<%--
  Created by IntelliJ IDEA.
  User: dell
  Date: 2015/6/30
  Time: 15:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>商家标记</title>
  <link rel="stylesheet" type="text/css" href="/imagemark/css/style.css">

  <script type="text/javascript" src="/imagemark/js/jquery.min.js"></script>
  <script type="text/javascript" src="/imagemark/js/jquery.masonry.min.js"></script>
  <script type="text/javascript">
    $(function(){
      var $container = $('#container');
      $container.imagesLoaded( function(){
        $container.masonry({
          itemSelector : '.item'
        });
      });
    });
  </script>
</head>
<body>
<div id="container">
  <c:forEach items="${merchantMarkList}" var="merchantMark">
  <div class="item">
    <a href="${merchantMark.link}">
      <img class="flowLayoutMasonry" src="http://222.214.218.140:90/thumb/${merchantMark._id}.png"/>
    </a>
    <p>${merchantMark.componentName}</p>
  </div>
  </c:forEach>
</div>
</body>
</html>
