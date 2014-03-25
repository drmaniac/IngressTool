package de.pieczewski.ingresstool.exceptions;

import java.io.IOException;

public class IngressLoginException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8162359042308156532L;

	public IngressLoginException(Exception e) {
		super(e);
	}

}
