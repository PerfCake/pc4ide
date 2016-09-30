package org.perfcake.ide.core.exception;

/**
 * Created by jknetl on 9/28/16.
 */
public class ModelDirectorException extends RuntimeException {

	public ModelDirectorException(String message) {
		super(message);
	}

	public ModelDirectorException(String message, Throwable cause) {
		super(message, cause);
	}

	public ModelDirectorException(Throwable cause) {
		super(cause);
	}

	public ModelDirectorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
