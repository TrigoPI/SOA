package com.vulnerable.Gateway;

import org.springframework.http.ResponseEntity;

public class HttpErrorException extends Exception {
	public final int status;
	
	public HttpErrorException(int status) {
		super("SHEH");
		this.status = status;
	}
}
