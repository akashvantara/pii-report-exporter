package com.vantara.hitachi.piics.entities;

import java.util.HashMap;
import java.util.Map;

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

	public Map<String, String> toMap() {
		Map<String, String> r = new HashMap<>();

		r.put("scheme", this.scheme);
		r.put("host", this.host);
		r.put("port", this.port);
		r.put("username", this.username);
		r.put("password", this.password);

		return r;
	}
}
