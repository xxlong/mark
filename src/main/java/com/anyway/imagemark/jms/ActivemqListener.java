package com.anyway.imagemark.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.anyway.imagemark.mail.SendMail;

public class ActivemqListener implements MessageListener {
    //private static final Logger logger = LoggerFactory.getLogger(ActivemqListener.class);
/*	@Autowired
	private SendMail sendMail;*/
	public void onMessage(Message message) {
		/*System.out.println("********************ActivemqListener(15) message:"+message);
//延时		
   try {
			Thread.sleep(50000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        TextMessage textMessage = (TextMessage) message;   
        System.out.println("********************ActivemqListener(18) testMessage:"+textMessage);
       try {
    	   System.out.println("*******************ActivemqListener(20)Message received: " + textMessage.getText());
    	   String text[] = textMessage.getText().split(";");
    	   System.out.println("******************ActivemqListener(32)**************"+text[0]+"     "+text[1]);
    	   sendMail.sendHtmlMail(text[0],text[1]);
    	   System.out.println("******************ActivemqListener(34)  sendHtmlMail()**************");
	} catch (JMSException e) {
		e.printStackTrace();
	}*/
	}
}