<%@ page language="java" contentType="text/html; charset=gb2312"
    pageEncoding="gb2312"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style type="text/css">
.register_border{
border:1px solid #B7B7B7;
width:500px;
height:400px;
margin: 0 auto;
}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>Insert title here</title>

</head>
<body >

<!-- <div style="height:120px;background-color: #e5e6e8;">
    <div style="font-size: 40px;font-family:arial;padding-top: 30px;padding-left: 30px;"><i>商家注册</i></i></div>
</div>
<div style="height:100%;width:100%">
     <div class="register_border" style="margin-top:60px">
          <div style="text-align: center;font-size: 50px;padding-top: 20px;">Please Register</div>
          <div style="margin-top:30px;margin-left:38px;">
               <span style="font-size:30px;">用户名</span>
               <input type="text" size="30" style="width:250px;height:26px;margin-left: 5px;"></input>
          </div>
          <div id="border_password" style="margin-top:25px;margin-left:38px">
		     <span style="font-size:30px;">密码</span>
			 <input type="text" size="30" style="margin-left:34px;width:250px;height:26px"></input>
		 </div>
		 <div id="border_password" style="margin-top:25px;margin-left:38px">
		     <span style="font-size:30px;">邮箱</span>
			 <input type="text" size="30" style="margin-left:34px;width:250px;height:26px"></input>
		 </div>
		 <div id="border_password" style="margin-top:25px;margin-left:38px">
		     <span style="font-size:30px;">角色</span>
			 <input type="text" size="30" style="margin-left:34px;width:250px;height:26px"></input>
		 </div>
         <button style="margin-left:342px;margin-top:20px;width:50px;height:28px">确定</button>
     </div>
     
</div> -->



 <center>  
  <h1>注册新商家</h1>  
  <form action="http://localhost:8080/imagemark/administrator/addAdministrator" method="post">  
  <table>   
    <tr valign="middle">  
      <td>管理员用户名:：  
        <input type="text" name="adminName">  
      </td>  
    </tr>
    <tr>  
      <td>管理员密码:  
        <input type="password" name="adminPassword">  
      </td>  
    </tr>
    <tr>  
      <td>邮箱:  
        <input type="text" name="adminMail">  
      </td>  
    </tr>
    <tr>  
      <td>角色:  
        <input type="text" name="role">  
      </td>  
    </tr>
    <tr>  
      <td>  
        <input type="submit" value="提交">  
      </td>  
    </tr>  
  </table>  
  </form>  
</center> 

</body>
</html>