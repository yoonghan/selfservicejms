package com.jom.jaring.jms.util;

import java.util.Properties;

import com.jaring.jom.jms.impl.PropertyFiles;
import com.jaring.jom.util.impl.PropertyMapperImpl;

class JMSBean implements PropertyMapperImpl{

	private Properties prop;
	private String jmsFactory;
	private String jmsKeyName;
	private String JMS_NAME;

	public JMSBean(String jmsName){
		this.JMS_NAME = jmsName;
	}

	private void checkProperties(Properties prop) throws IllegalAccessException{
		//TODO: Check for key properties in queue if wrong
	}

	public Properties getProp() {
		return prop;
	}

	public void setProp(Properties prop) {
		this.prop = prop;
	}
	

	public String getJmsFactory() {
		return jmsFactory;
	}

	public void setJmsFactory(String jmsFactory) {
		this.jmsFactory = jmsFactory;
	}

	public String getJmsKeyName() {
		return jmsKeyName;
	}

	public void setJmsKeyName(String jmsKeyName) {
		this.jmsKeyName = jmsKeyName;
	}

	@Override
	public void map(Properties property) throws IllegalAccessException {
		setJmsFactory(property.getProperty(JMS_NAME+PropertyFiles.JMS_DIFF_QUEUE_KEY+PropertyFiles.JMS_FACTORY_KEY));
		setJmsKeyName(property.getProperty(JMS_NAME+PropertyFiles.JMS_DIFF_QUEUE_KEY+PropertyFiles.JMS_NAME_KEY));
		
		checkProperties(property);
		setProp(property);
	}

}
