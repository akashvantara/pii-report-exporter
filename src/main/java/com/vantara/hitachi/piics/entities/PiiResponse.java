package com.vantara.hitachi.piics.entities;

public class PiiResponse {
	public static final String RESPONSE_OK = "OK";
	public static final String RESPONSE_NOTOK = "NOT_OK";

	private String status;

	public PiiResponse(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
