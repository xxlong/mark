package com.anyway.imagemark.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class ActivemqProducer {
	
	//Spring的模板，封装了很多功能
	private JmsTemplate jmsTemplate;
	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}
	
	public void sendMessage(final String message) {			
		System.out.println("***********************ActivemqProducer(18)");
		//使用JMSTemplate可以很简单的实现发送消息
		jmsTemplate.send(new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				System.out.println("***********************ActivemqProducer(22) message:"+message);
				return session.createTextMessage(message);
			}
		});
	}

}
