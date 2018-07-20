package com.company.project.socket;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.util.StringUtils;


public class ServerResponseThread implements Runnable {
	
	private ReceiveThread receiveThread;
    private SendThread sendThread;
    private Socket socket;
    private SocketServerResponseInterface socketServerResponseInterface;
    //发送数据队列  生产消费者模式
    private volatile ConcurrentLinkedQueue<String> dataQueue = new ConcurrentLinkedQueue<>();
    //这个可以去除
   // private static ConcurrentHashMap<String, Socket> onLineClient = new ConcurrentHashMap<>();

    @SuppressWarnings("unused")
	private long lastReceiveTime = System.currentTimeMillis();

    private String userIP;


    public String getUserIP() {
        return userIP;
    }

    public ServerResponseThread(Socket socket, SocketServerResponseInterface socketServerResponseInterface) {
        this.socket = socket;
        this.socketServerResponseInterface = socketServerResponseInterface;
        this.userIP = socket.getInetAddress().getHostAddress();
      
        System.out.println("用户：" + userIP
                + " 加入了聊天室,当前在线人数:" + ConcurrentCache.getCacheSize());
        if(socket.isConnected()){
          this.socketServerResponseInterface.clientOnline(userIP);
        }
    }
	@Override
	public void run() {
	      try {
	            //开启接收线程
	            receiveThread = new ReceiveThread();
	            //字符流
//	            receiveThread.bufferedReader = new BufferedReader(
//	                    new InputStreamReader(socket.getInputStream(), "UTF-8"));
	            //字节流
	            receiveThread.inputStream=socket.getInputStream();
	            receiveThread.start();

	            //开启发送线程
	            sendThread = new SendThread();
	            sendThread.dataOutputStream=new DataOutputStream(socket.getOutputStream());
	           // sendThread.printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
	            sendThread.start();

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	}
	
	  /**
     * 断开socket连接
     */
    public void stop() {
    	  try {
            //  System.out.println("stop");
              if (receiveThread != null) {
                  receiveThread.isCancel = true;
                  receiveThread.interrupt();
                  if (receiveThread.inputStream != null) {
                      SocketUtil.inputStreamShutdown(socket);
                     // System.out.println("before closeBufferedReader");
                      SocketUtil.closeInputStream(receiveThread.inputStream);
                      //System.out.println("after closeBufferedReader");
                      receiveThread.inputStream= null;
                  }
                  receiveThread = null;
                 // System.out.println("stop receiveThread");
              }

              if (sendThread != null) {
                  sendThread.isCancel = true;
                  toNotifyAll(sendThread);
                  sendThread.interrupt();
                  if (sendThread.dataOutputStream != null) {
                      //防止写数据时停止，写完再停
                      synchronized (sendThread.dataOutputStream) {
                          SocketUtil.closeOutputStream(sendThread.dataOutputStream);
                          sendThread.dataOutputStream = null;
                      }
                  }
                  sendThread = null;
                 // System.out.println("stop sendThread");
              }
              
             ConcurrentCache.remove(userIP);  
              System.out.println("用户：" + userIP
                      + " 退出,当前在线人数:" + (ConcurrentCache.getCacheSize()-1));
          } catch (Exception e) {
              e.printStackTrace();
          }
    	
    }
	
	
	  /**
     * 判断本地socket连接状态
     */
    private boolean isConnected() {
        if (socket.isClosed() || !socket.isConnected()) {
        	  ConcurrentCache.remove(userIP);  
              ServerResponseThread.this.stop();
             // System.out.println("socket closed...");
            return false;
        }
        return true;
    }

    
    /**
     * 获取已接连的客户端
     */
    public Socket getConnectdClient(String clientID) {
        return ConcurrentCache.getServerManager().get(clientID).socket;
    }
    
    /**
     * 打印已经连接的客户端
     */
    public static void printAllClient() {
        if (ConcurrentCache.getServerManager() == null) {
            return;
        }
        Iterator<String> inter = ConcurrentCache.getServerManager().keySet().iterator();
        while (inter.hasNext()) {
            System.out.println("client:" + inter.next());
        }
    }
    
    
    /**
     * 数据接收线程
     */
    public class ReceiveThread extends Thread {

        //private BufferedReader bufferedReader;
        private InputStream inputStream;
        private boolean isCancel;

        @Override
        public void run() {
            try {
                while (!isCancel) {
                    if (!isConnected()) {
                        isCancel = true;
                        break;
                    }

                    //十进制字符串
                   // String msg = SocketUtil.readFromStream(bufferedReader);
                    String msg = null;
                    try {
                    	  msg=SocketUtil.readFromByeStream(inputStream);
					} catch (Exception e) {
					   e.printStackTrace();
					   //关闭Socket
					   //关闭IO流
					   //接口回调
					   //客户端下线
                       socketServerResponseInterface.clientOffline(userIP);
                       isCancel=true;                     
					}
                 
                   System.out.println("服务器接收消息"+msg);
                    if (msg != null) {
                        if ("ping".equals(msg)) {
                            //收到心跳包
                            lastReceiveTime = System.currentTimeMillis();
                            //socketServerResponseInterface.clientOnline(userIP);
                        } else {
                            //收到客户端消息
                            //服务器发送消息给客户端
                            //addMessage(msg);
                        	if (!StringUtils.isEmpty(msg)) {
                        		 socketServerResponseInterface.onSocketReceive(msg, userIP);
							}
                           
                            //socketServerResponseInterface.clientOnline(userIP);
                        }
                    } else {
                        //客户端下线
                        socketServerResponseInterface.clientOffline(userIP);
                        ServerResponseThread.this.stop();
                        break;
                    }
                    //System.out.println("ReceiveThread");
                }

                SocketUtil.inputStreamShutdown(socket);
                SocketUtil.closeInputStream(inputStream);
               // System.out.println("ReceiveThread is finish");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    
    /**
     * 数据发送线程,当没有发送数据时让线程等待
     */
    public class SendThread extends Thread {

      //  private PrintWriter printWriter;
        private DataOutputStream dataOutputStream;
        private boolean isCancel;

        @Override
        public void run() {
            try {
                while (!isCancel) {
                    if (!isConnected()) {
                        isCancel = true;
                        break;
                    }

                    String msg = dataQueue.poll();
                    if (msg == null) {
                        toWaitAll(dataQueue);
                    } else if (dataOutputStream != null) {
                        synchronized (dataOutputStream) {
                        	try {
                        		  SocketUtil.write2Stream(msg, dataOutputStream);
							} catch (Exception e) {
								e.printStackTrace();
								isCancel=true;
							}
                          
                        }
                    }
                   // System.out.println("SendThread");
                }

                SocketUtil.outputStreamShutdown(socket);
                //SocketUtil.closePrintWriter(printWriter);//字符流
                SocketUtil.closeOutputStream(dataOutputStream);
               // System.out.println("SendThread is finish");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    
    /**
     * 阻塞线程,millis为0则永久阻塞,知道调用notify()
     */
    public void toWaitAll(Object o) {
        synchronized (o) {
            try {
                o.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * notify()调用后，并不是马上就释放对象锁的，而是在相应的synchronized(){}语句块执行结束，自动释放锁后
     */
    public void toNotifyAll(Object obj) {
        synchronized (obj) {
            obj.notifyAll();
        }
    }
    
    
    /**
     * 发送消息
     */
    public void addMessage(String data) {
        if (!isConnected()) {
            return;
        }

        dataQueue.offer(data);
        //有新增待发送数据，则唤醒发送线程
        toNotifyAll(dataQueue);
    }
}
