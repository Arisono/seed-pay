package com.company.project.socket;

public interface SocketServerResponseInterface {
	
	/**
	 * 客户端上线回调
	 *
	 * @param clientIp
	 */
	void clientOnline(String clientIp);

	/**
	 * 接收客户端消息
	 * @param socketResult
	 * @param code
	 */
	public void onSocketReceive(String socketResult, String clientIp);
	
	/**
	 * 客户端断线回调
	 */
	void clientOffline(String clientIp);

}
