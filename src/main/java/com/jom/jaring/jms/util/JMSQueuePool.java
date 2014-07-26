package com.jom.jaring.jms.util;

import java.io.IOException;
import java.util.HashMap;

import com.google.common.base.Optional;
import com.jaring.jom.jms.impl.PropertyFiles;
import com.jaring.jom.util.common.PropertyLoaderUtil;

public class JMSQueuePool {

	//private final String CLASS_NAME = "com.self.service.jms.util.JMSQueuePool";
	private final int POOL_SIZE = 1;
	private HashMap<String,JMSQueue> queuePool = new HashMap<String,JMSQueue>(POOL_SIZE);
	
	private final static class Singleton{
		private final static JMSQueuePool instance = new JMSQueuePool();
	}
	
	private JMSQueuePool(){
	}
	
	public static JMSQueuePool getInstance(){
		return Singleton.instance;
	}
	
	public final Optional<JMSQueue> getQueuePool(String queueName){
		JMSQueue queue = queuePool.get(queueName);
		if(queue == null){
			queue = initQueue(queueName);
		}
		return Optional.fromNullable(queue);
	}
	
	private final synchronized JMSQueue initQueue(String queueName){
		
		JMSQueue queue = null;
		try {
			JMSBean jmsBean = new JMSBean(queueName);
			new PropertyLoaderUtil().loadProperty(PropertyFiles.JMSReader, jmsBean);

			queue = new JMSQueue(jmsBean.getProp(), 
					jmsBean.getJmsFactory(),
					jmsBean.getJmsKeyName(),
					queueName);
			if(queue != null){
				queuePool.put(queueName , queue);
			}
		} catch (IllegalAccessException | ClassNotFoundException | IOException | NoClassDefFoundError e) {
			e.printStackTrace();
		}
		return queue;
	}
	
}
