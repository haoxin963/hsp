
package net.hsp.common;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Log   { 
	public static  Logger logger = LogManager.getLogger(Log.class);
	
 
	public static  void debug(Object message, Throwable t) { 
		logger.debug(message, t);
	}
 
	public static void debug(Object message) {
		
		logger.debug(message);
	}
 
	public static void error(Object message, Throwable t) {
		
		logger.error(message, t);
	}
 
	public static void error(Object message) {
		
		logger.error(message);
	}
 
	public static void info(Object message, Throwable t) {
		
		logger.info(message, t);
	}
 
	public static void info(Object message) {
		 
		logger.info(message);
	}

	
	
}
