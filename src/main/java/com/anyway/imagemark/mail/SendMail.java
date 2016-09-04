package com.anyway.imagemark.mail;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.velocity.VelocityEngineUtils;

public class SendMail {  

	  private JavaMailSender mailSender;
	    public void setMailSender(JavaMailSender mailSender) {
			this.mailSender = mailSender;
		}
	    
	    private VelocityEngine velocityEngine;
	    public void setVelocityEngine(VelocityEngine velocityEngine) {
			this.velocityEngine = velocityEngine;
		}

    //文本邮件
    public void sendTextMail() {  
    	//<span style="color: #ff0000;">注意SimpleMailMessage只能用来发送text格式的邮件</span>  
    	SimpleMailMessage mail = new SimpleMailMessage(); 
    	try {  
    		mail.setTo("1044489906@qq.com");//接受者  
    		mail.setFrom("basketball402@163.com");//发送者,这里还可以另起Email别名，不用和xml里的username一致  
    		mail.setSubject("spring mail test!");//主题  
    		mail.setText("springMail的简单发送测试");//邮件内容  
    		mailSender.send(mail);  
    		System.out.println("*******************send Success!************************");
    	} catch (Exception e) {  
    		e.printStackTrace();  
    	}  
    }  
    


	//html邮件
    public void sendHtmlMail(String sendTo,String name) {  
    	 // JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();  
    	  //MimeMessage mailMessage = senderImpl.createMimeMessage();   	 
    	MimeMessage mailMessage = mailSender.createMimeMessage(); 
    	  try {  
    		  //设置utf-8或GBK编码，否则邮件会有乱码  
        	  MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage,true,"utf-8");  
        	  messageHelper.setTo(sendTo);//接受者     
        	  messageHelper.setFrom("basketball402@163.com");//发送者  
        	  messageHelper.setSubject("imagemark register");//主题  
        	  //邮件内容，注意加参数true，表示启用html格式  
        	  messageHelper.setText("<html><head></head><body>"+"您好!"+name+"恭喜您注册成功了</body></html>",true);  
/*        	  senderImpl.setUsername("username") ; // 根据自己的情况,设置username
        	    senderImpl.setPassword("password") ; // 根据自己的情况, 设置password
        	    Properties prop = new Properties() ;
        	    prop.put("mail.smtp.auth", "true") ; // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确
        	    prop.put("mail.smtp.timeout", "25000") ; 
        	    senderImpl.setJavaMailProperties(prop); */        	  
        	  mailSender.send(mailMessage);  
        		System.out.println("*******************send Success!************************");
    	  } catch (Exception e) {  
    	   e.printStackTrace();  
    	  }  
}
    
    //html邮件带附件
    public void sendHtmlAttachMail() {  
/*    	  JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();  
    	  MimeMessage mailMessage = senderImpl.createMimeMessage();  */
    	MimeMessage mailMessage = mailSender.createMimeMessage(); 
    	  try {  
        	//设置utf-8或GBK编码，否则邮件会有乱码  
        	MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage,true,"utf-8");  
    	   messageHelper.setTo("1044489906@qq.com");//接受者     
    	   messageHelper.setFrom("basketball402@163.com");//发送者  
    	   messageHelper.setSubject("html带附件test");//主题  
    	   //邮件内容，注意加参数true  
    	   //messageHelper.setText("<html><head></head><body><h1>恭喜您注册成功了!</h1></body></html>",true);  
    	   File myPicture = new File("/home/xxlong/桌面/xiaomao2.jpg");    
           
           //如果是html代码，boolean 的参数用该为true    
    	  /* messageHelper.setText("<html><head><meta http-equiv="+"Content-Type"+" content="+"text/html; charset=gb2312"+
           "></head><body><h1>恭喜您 注册成功了!去登陆<a href=''http://localhost:8080/imagemark/login.jsp>http://localhost:8080/imagemark/login.jsp"+ "</a>查看<br><img src='cid:myPicture'></body></html>", true);    */
    	   messageHelper.setText("<html><head><meta http-equiv="+"Content-Type"+" content="+"text/html; charset=gb2312"+
    	           "></head><body><h1>恭喜您 注册成功了!</body></html>", true); 
         
           //添加图片至HTML 代码中    
    	   //messageHelper.addInline("myPicture", myPicture);  
    	   
    	   //附件内容 
    	  // messageHelper.addInline("a", new File("/home/xxlong/桌面/xiaomao2.jpg"));  
    	   //messageHelper.addInline("b", new File("/home/xxlong/ccccc/makefile"));   
    	   File file=new File("/home/xxlong/py/python.rar");    
    	   // 这里的方法调用和插入图片是不同的，使用MimeUtility.encodeWord()来解决附件名称的中文问题  
    	   messageHelper.addAttachment(MimeUtility.encodeWord(file.getName()), file);   
    	   mailSender.send(mailMessage);  
    	   System.out.println("*******************send Success!************************");
    	  } catch (Exception e) {  
    	   e.printStackTrace();  
    	  } 
    }
    
    
    //带模板的邮件
    public boolean sendModelMail(String sendTo,String subject,String vm,String url,String name){ 	
    	 System.out.println("SendMail(114)"+sendTo);
   	 // JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();  
  	  //MimeMessage mailMessage = senderImpl.createMimeMessage();   	 
  	MimeMessage mailMessage = mailSender.createMimeMessage(); 
  	System.out.println("sendMail(118)");
  	  try {  
  		  //设置utf-8或GBK编码，否则邮件会有乱码  
      	  MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage,true,"utf-8");  
      	  messageHelper.setTo(sendTo);//接受者    
      	  messageHelper.setFrom("basketball402@163.com");//发送者  
      	  messageHelper.setSubject(subject);//主题  
      	// 声明Map对象，并填入用来填充模板文件的键值对  
      	Map<String, String> model = new HashMap<String, String>();  
      	model.put("username", name);  
      	model.put("url", url);  
      	// Spring提供的VelocityEngineUtils将模板进行数据填充，并转换成普通的String对象  
      	String emailText = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "velocity/"+vm,"utf-8", model);  
      	  //邮件内容，注意加参数true，表示启用html格式  
      	  messageHelper.setText(emailText, true);    	  
      	  mailSender.send(mailMessage);  
      	  System.out.println("*******************send mode mail Success!************************");
      	  return true;
  	  } catch (Exception e) {  
  		  System.out.println("************ com.anyway.imagemark.mail.SendMail136***************");
  	      e.printStackTrace();  
  	      return false;
  	  }
    	
    }
}