package net.hsp.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.LoggingEvent;

public class UserActionLogJDBCAppender extends org.apache.log4j.AppenderSkeleton implements org.apache.log4j.Appender {

	protected int bufferSize = 50;

	protected ArrayList buffer;

	protected ArrayList removes;

	private boolean locationInfo = false;
	
	//private UserLogService service;

	public UserActionLogJDBCAppender() {
		super();
		buffer = new ArrayList(bufferSize);
		removes = new ArrayList(bufferSize);
	}

	public void setBufferSize(int newBufferSize) {
		bufferSize = newBufferSize;
		buffer.ensureCapacity(bufferSize);
		removes.ensureCapacity(bufferSize);
	}

	public int getBufferSize() {
		return bufferSize;
	}

	public void flushBuffer() {
		removes.ensureCapacity(buffer.size());
		//if(service ==null){
			//service = (UserLogService) SpringCtx.getBean("userLogServiceImpl");
	//	}
		List list = new ArrayList();
		for (Iterator i = buffer.iterator(); i.hasNext();) {
			LoggingEvent logEvent = (LoggingEvent) i.next();
			try {
				String sql = logEvent.getMessage() + ""; 
				if(StringUtils.isNotBlank(sql)){
					String[] strs = sql.split("\\|"); 
					/**
					UserActionLog log = new UserActionLog();
					log.setName(strs[0]);
					log.setMs(strs[1]);
					log.setIp(strs[2]);
					log.setOperateTime(new Date(logEvent.getTimeStamp()));
					log.setAction(strs[3]);
					list.add(log);*/
				}
				
			} catch (Exception e) {
				errorHandler.error("Failed to excute sql", e, ErrorCode.FLUSH_FAILURE);
			} finally {
				removes.add(logEvent);
			}
		}
		if (!list.isEmpty()) {
			try {
				//service.batchSave(list);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		buffer.removeAll(removes);
		removes.clear();
	}

	@Override
	public void append(LoggingEvent event) {
		event.getNDC();
		event.getThreadName(); 
		event.getMDCCopy();
		if (locationInfo) {
			event.getLocationInformation();
		}
		event.getRenderedMessage();
		event.getThrowableStrRep();
		buffer.add(event);

		if (buffer.size() >= bufferSize)
			flushBuffer();
	}

	@Override
	public void close() {
		flushBuffer();
		this.closed = true;
	}

	@Override
	public boolean requiresLayout() {
		return false;
	}

}