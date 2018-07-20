package com.company.project.socket;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentCache {
	private static ConcurrentHashMap<String, ServerResponseThread> 
	 serverManager=new ConcurrentHashMap<>();
	public static void put(String key,  ServerResponseThread value){
		serverManager.put(key, value);
	};
	
	public static void remove(String key){
		serverManager.remove(key);
	}
	
	public static ServerResponseThread getCache(String key){
	  return serverManager.get(key);
	}
	
	public static int getCacheSize(){
		return serverManager.size()+1;
	}

	public static ConcurrentHashMap<String, ServerResponseThread> getServerManager() {
		return serverManager;
	}
	
}
