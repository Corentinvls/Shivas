package org.shivas.server.config;

public class DefaultConfig implements Config {

	public int loginPort() {
		return 5555;
	}

	public int gameId() {
		return 1; // JIVA
	}

	public String gameAddress() {
		return "127.0.0.1";
	}

	public int gamePort() {
		return 5556;
	}

	public String clientVersion() {
		return "1.29.1";
	}

}