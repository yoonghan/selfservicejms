package com.self.service.jms.util;

import java.io.IOException;
import java.util.Properties;

import com.self.service.jms.impl.PropertyFiles;
import com.self.service.util.common.PropertyLoaderUtil;

class JMSReader {

	private final String CLASS_NAME = JMSReader.class.getName();
	private Properties prop;
	
	public JMSReader() throws IllegalAccessException, ClassNotFoundException, IOException{
		initProperty();
	}
	
	private void initProperty() throws IllegalAccessException, ClassNotFoundException, IOException{
		Properties prop = new PropertyLoaderUtil().loadProperty(null, CLASS_NAME, PropertyFiles.JMSReader);
		setProp(prop);
		checkProperties(prop);
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
}
