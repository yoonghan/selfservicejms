package com.jaring.jom.jms.impl;

public interface PropertyFiles {
	public static final String JMSReader="/selfservicejms.properties";
	
	//JMS connection property
	public static final String JMS_FACTORY_KEY=".jms.factory";
	public static final String JMS_NAME_KEY=".jms.name";
	public static final String JMS_READ_TIMEOUT=".jms.read.timeout";//milliseconds
	public static final String JMS_READ_MAX=".jms.read.max";
	public static final String JMS_PING_DELAY_TIME=".jms.ping.time";//milliseconds
	
	//JMS difference
	public static final String JMS_DIFF_QUEUE_KEY=".queue";
	
}
