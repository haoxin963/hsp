package net.hsp.web.util;

/**
 * Web层公用的Exception.
 * 
 * 继承自RuntimeException, 从由Spring管理事务的函数中抛出时会触发事务回滚.
 * 
 * @author JSmart
 */
public class WebException extends Exception {
	private static final long serialVersionUID = 3583566093089790852L;

	public WebException() {
		super();
	}

	public WebException(String message) {
		super(message);
	}

	public WebException(Throwable cause) {
		super(cause);
	}

	public WebException(String message, Throwable cause) {
		super(message, cause);
	}
}
