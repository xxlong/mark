<%@ page language="java" contentType="text/html; charset=gb2312"
    pageEncoding="gb2312"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>Insert title here</title>
</head>
<body>
<center>  
  <h1>注册新商家</h1>  
  <form action="http://localhost:8080/imagemark/merchant/addMerchant" method="post">  
  <table>  
    <tr>  
      <td>用户名:  
        <input type="text" name="merchantName">  
      </td>  
    </tr>  
    <tr valign="middle">  
      <td>密码：  
        <input type="password" name="merchantPassword">  
      </td>  
    </tr>
    <tr>  
      <td>邮箱:  
        <input type="text" name="merchantMail">  
      </td>  
    </tr>
    <tr>  
      <td>电话:  
        <input type="text" name="phoneNumber">  
      </td>  
    </tr>
    <tr>  
      <td>地址:  
        <input type="text" name="merchantAddress">  
      </td>  
    </tr>
    <tr>  
      <td>身份证:  
        <input type="text" name="merchantID">  
      </td>  
    </tr>
    <tr>  
      <td>店名:  
        <input type="text" name="storeName">  
      </td>  
    </tr>
    <tr>  
      <td>店名商品类型:  
        <input type="text" name="storeType">  
      </td>  
    </tr>
    <tr>  
      <td>店网址:  
        <input type="text" name="storeAddress">  
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