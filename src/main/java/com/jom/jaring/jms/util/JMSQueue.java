package com.jom.jaring.jms.util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.jaring.jom.jms.impl.PropertyFiles;

public class JMSQueue implements AutoCloseable{
	
	private Connection conn = null;
	private Queue queue = null;
	
	private int readTimeout = 1000;
	private int maxRead = 100;
	
	public JMSQueue(Properties jmsProperties, String jmsFactory, String jmsName, String queueName){
		setInitialContext(jmsProperties, jmsFactory, jmsName, queueName);
	}

	private void setInitialContext(Properties jmsProperties, String jmsFactory, String jmsName, String queueName) {
		InitialContext initCtx;
		try {
			if(jmsProperties != null){
				initCtx = new InitialContext(jmsProperties);
			}else
				initCtx = new InitialContext();
			
			QueueConnectionFactory connFactory = (QueueConnectionFactory)initCtx.lookup(jmsFactory);
			this.conn = connFactory.createConnection();
			this.queue = (Queue)initCtx.lookup(jmsName);
			
			String readTimeoutStr = jmsProperties.getProperty(queueName+PropertyFiles.JMS_DIFF_QUEUE_KEY+PropertyFiles.JMS_READ_TIMEOUT, ""+readTimeout);
			try{
				this.readTimeout = Integer.parseInt(readTimeoutStr, 10);
			}catch(Exception e){
				e.printStackTrace();
			}
			String readMaxStr = jmsProperties.getProperty(queueName+PropertyFiles.JMS_DIFF_QUEUE_KEY+PropertyFiles.JMS_READ_MAX, ""+maxRead);
			try{
				this.maxRead = Integer.parseInt(readMaxStr, 10);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			start();
		} catch (JMSException|NamingException e) {
			e.printStackTrace();
		}
		
	}
	
	public boolean sendMessage(String message){
		
		boolean messageSent = false;
		
		if(conn != null && queue != null){
			
			Session session = null;
			MessageProducer messageProducer = null;
			try {
				session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
				messageProducer = session.createProducer(queue);
				TextMessage txtMessage = session.createTextMessage();
				txtMessage.setText(message);
				messageProducer.send(txtMessage);
				messageSent = true;
			} catch (JMSException e) {
				e.printStackTrace();
			} finally {
				if(messageProducer != null)
					try {
						messageProducer.close();
					} catch (JMSException e) {
						e.printStackTrace();
					}
				if(session != null)
					try{
						session.close();
					} catch (JMSException e){
						e.printStackTrace();
					}
			}
		}
		
		return messageSent;
	}
	
	public String readMessage() throws JMSException{
		
		String messageRead = "";
		
		if(conn != null && queue != null){
			
			Session session = null;
			MessageConsumer messageConsumer = null;
			try{
				session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
				messageConsumer = session.createConsumer(queue);
				TextMessage message = (TextMessage)messageConsumer.receive();
				messageRead = message.getText();
			}finally{
				if(messageConsumer != null)
					messageConsumer.close();
				if(session != null)
					session.close();
			}
		}
		
		return messageRead;
	}
	
	public List<String> readAllMessages(int maxCount, boolean clearAll) throws JMSException{
		
		maxCount = maxCount<maxRead?maxCount:maxRead;
		
		List<String> messageRead = new ArrayList<String>(maxCount);
		int counter = 0;
		
		if(conn != null && queue != null){
			Session session = null;
			QueueBrowser messageBrowser = null;
			try{
				session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
				messageBrowser = session.createBrowser(queue);
				@SuppressWarnings("unchecked")
				Enumeration<Message> messages = messageBrowser.getEnumeration();
				
				while(messages.hasMoreElements()
						&& (counter < maxCount || clearAll)){
					
					messages.nextElement();
					
					String msg = readMessage();
					if(msg != null){
						messageRead.add(msg);
					}
					
					counter++;
				}
			}finally{
				if(messageBrowser != null)
					messageBrowser.close();
				if(session != null)
					session.close();
			}
		}
		
		return messageRead;
	}
	
	public int getMessageCount() throws JMSException{
		
		int count = 0;
		
		if(conn != null && queue != null){
			Session session = null;
			QueueBrowser messageBrowser = null;
			try{
				session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
				messageBrowser = session.createBrowser(queue);
				@SuppressWarnings("unchecked")
				Enumeration<Message> messages = messageBrowser.getEnumeration(); 
				while(messages.hasMoreElements()){
					messages.nextElement();
					count++;
				}
			}finally{
				if(messageBrowser != null)
					messageBrowser.close();
				if(session != null)
					session.close();
			}
		}
		
		return count;
	}
	
	public void start() throws JMSException{
		if(conn!=null )
			conn.start();
	}
	
	public void stop() throws JMSException{
		if(conn!=null)
			conn.stop();
	}
	
	public void close(){
		if(conn != null)
			try {
				conn.close();
				conn = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
}
