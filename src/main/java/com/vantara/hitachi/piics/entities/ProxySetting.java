package com.vantara.hitachi.piics.entities;

public class ProxySetting {
	public String scheme;
	public String host;
	public String port;
	public String username;
	public String password;

	public ProxySetting() {
		this.scheme = null;
		this.host = null;
		this.port = null;
		this.username = null;
		this.password = null;
	}

	public ProxySetting(String scheme, String host, String port, String username, String password) {
		this.scheme = scheme;
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
	}
}
