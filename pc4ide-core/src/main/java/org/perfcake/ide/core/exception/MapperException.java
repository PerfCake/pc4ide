/**
 *
 */
package org.perfcake.ide.core.exception;

/**
 *
 * Represent exception in mapping process.
 *
 * @author jknetl
 *
 */
public class MapperException extends RuntimeException {

	private static final long serialVersionUID = 7673761145209276868L;

	public MapperException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MapperException(String message, Throwable cause) {
		super(message, cause);
	}

	public MapperException(String message) {
		super(message);
	}

	public MapperException(Throwable cause) {
		super(cause);
	}
}
