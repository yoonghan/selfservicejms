package com.self.service.jms.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import com.google.common.base.Optional;
import com.self.service.jms.impl.PropertyFiles;

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
		JMSReader reader;
		JMSQueue queue = null;
		try {
			reader = new JMSReader();
			Properties prop = reader.getProp();
			queue = new JMSQueue(prop, 
					prop.getProperty(queueName+PropertyFiles.JMS_DIFF_QUEUE_KEY+PropertyFiles.JMS_FACTORY_KEY),
					prop.getProperty(queueName+PropertyFiles.JMS_DIFF_QUEUE_KEY+PropertyFiles.JMS_NAME_KEY),
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
