package org.shivas.server.config;

public interface Config {
	
	int loginPort();

	int gameId();
	String gameAddress();
	int gamePort();
	
	String clientVersion();
	
}