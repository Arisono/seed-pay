package com.company.project.socket;
import java.io.*;
import java.net.*;
import java.util.Enumeration;

import org.springframework.util.StringUtils;

import com.company.project.utils.BytesUtils;



public class SocketUtil {
	
	 public static int PORT =10099;
	 
	 
	 /**
	  * 按字节读取网络IO
	 * @param inputStream
	 * @return
	 * @throws IOException 
	 */
	public static String readFromByeStream(InputStream inputStream) throws IOException{
		DataInputStream dataInputStream = new DataInputStream(inputStream);
		byte [] lengh=new byte [3];		   
		lengh[0]=dataInputStream.readByte();
		lengh[1]=dataInputStream.readByte();
	    lengh[2]=dataInputStream.readByte();
        System.out.println("长度字节："+BytesUtils.bytesToInt(lengh, 1, 2));
        byte[] data = new byte[BytesUtils.bytesToInt(lengh, 1, 2)+1];
		dataInputStream.readFully(data);
        String hex=BytesUtils.bytesToHex(lengh)+BytesUtils.bytesToHex(data);
        return hex;
	 }

	    /**
	     * 读数据
	     *
	     * @param bufferedReader
	     */
	    public static String readFromStream(BufferedReader bufferedReader) {
	        try {
	            String s;
	            if ((s = bufferedReader.readLine()) != null) {
	                return s;
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }

	    /**
	     * 写数据
	     *
	     * @param data
	     * @param printWriter
	     */
	    public static void write2Stream(String data, PrintWriter printWriter) {
	        if (data == null) {
	            return;
	        }
	        if (printWriter != null) {
	            printWriter.println(data);
	        }
	    }
	    
	    
	    /**
	     * 写数据- 字节
	     * @param data
	     * @param dataStream
	     * @throws IOException 
	     */
	    public static void write2Stream(String data,DataOutputStream dataOutputStream) throws IOException{
	    	if(StringUtils.isEmpty(data)){
	    		return;
	    	}
	   
	    	 if(dataOutputStream != null){
	               
	                 dataOutputStream.write(BytesUtils.hexStringToBytes(data));
	                 dataOutputStream.flush();
	             
	         }
	    }
	    
	    /**
	     * 写数据- 字节
	     * @param data
	     * @param dataStream
	     */
	    public static void write2Stream(byte [] data,DataOutputStream dataOutputStream){
	    	if(data==null){
	    		return;
	    	}
	    	if (data.length==0) {
				return;
			}
	    	 if(dataOutputStream != null){
	             try {    
	                 dataOutputStream.write(data);
	                 dataOutputStream.flush();
	             } catch (IOException e) {
	                 e.printStackTrace();
	             }
	         }
	    }


	    /**
	     * 关闭输入流
	     *
	     * @param socket
	     */
	    public static void inputStreamShutdown(Socket socket) {
	        try {
	            if (!socket.isClosed() && !socket.isInputShutdown()) {
	                socket.shutdownInput();
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    /**
	     * 关闭BufferedReader
	     *
	     * @param br
	     */
	    public static void closeBufferedReader(BufferedReader br) {
	        try {
	            if (br != null) {
	                br.close();
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    
	    /**
	     * 关闭BufferedReader
	     *
	     * @param br
	     */
	    public static void closeInputStream(InputStream br) {
	        try {
	            if (br != null) {
	                br.close();
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    /**
	     * 关闭输出流
	     *
	     * @param socket
	     */
	    public static void outputStreamShutdown(Socket socket) {
	        try {
	            if (!socket.isClosed() && !socket.isOutputShutdown()) {
	                socket.shutdownOutput();
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    /**
	     * 关闭PrintWriter
	     *
	     * @param pw
	     */
	    public static void closePrintWriter(PrintWriter pw) {
	        if (pw != null) {
	            pw.close();
	        }
	    }
	    
	    /**
	     * 关闭PrintWriter
	     *
	     * @param pw
	     */
	    public static void closeOutputStream(DataOutputStream pw) {
	        if (pw != null) {
	            try {
					pw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	        }
	    }

	    /**
	     * 获取本机IP地址
	     */
	    @SuppressWarnings("rawtypes")
		public static String getIP() {
	        String hostIP = null;
	        try {
	            Enumeration nis = NetworkInterface.getNetworkInterfaces();
	            InetAddress ia = null;
	            while (nis.hasMoreElements()) {
	                NetworkInterface ni = (NetworkInterface) nis.nextElement();
	                Enumeration<InetAddress> ias = ni.getInetAddresses();
	                while (ias.hasMoreElements()) {
	                    ia = ias.nextElement();
	                    if (ia instanceof Inet6Address) {
	                        continue;// skip ipv6
	                    }
	                    String ip = ia.getHostAddress();
	                    if (!"127.0.0.1".equals(ip)) {
	                        hostIP = ia.getHostAddress();
	                        break;
	                    }
	                }
	            }
	        } catch (SocketException e) {
	            e.printStackTrace();
	        }
	        return hostIP;

	    }
	    
	    
	    public static String getByteToString(byte [] data){
	    	StringBuffer str=new StringBuffer();
	        for(int i=0;i<data.length;i++){
	        	if(data[i]<0){
	        		int tem =data[i]+256;
	        		str.append(tem+" ");
	        	}else{
	        		str.append(data[i]+" ");
	        	}
	        }
	        
	        System.out.println("十进制："+str.toString());
	        
	        return str.toString();
	    }
	    
	    /**
	     * 将16进制字符串转换为字节数组
	     * 
	     * @param hex
	     * @return
	     */
	    public static byte[] hexStringToBytes(String hex) {
	        if (hex == null || "".equals(hex)) {
	            return null;
	        }
	        int len = hex.length() / 2;
	        byte[] result = new byte[len];
	        char[] chArr = hex.toCharArray();
	        for (int i = 0; i < len; i++) {
	            int pos = i * 2;
	            result[i] = (byte) (toByte(chArr[pos]) << 4 | toByte(chArr[pos + 1]));
	        }
	        return result;
	    }
	    
	    /**
	     * 将16进制字符转换为字节
	     * 
	     * @param c
	     * @return
	     */
	    public static byte toByte(char c) {
	        byte b = (byte) "0123456789ABCDEF".indexOf(c);
	        return b;
	    }
	    
		// 将字节数组转换为16进制字符串
		public static String BinaryToHexString(byte[] bytes) {
			String hexStr = "0123456789ABCDEF";
			String result = "";
			String hex = "";
			for (byte b : bytes) {
				hex = String.valueOf(hexStr.charAt((b & 0xF0) >> 4));
				hex += String.valueOf(hexStr.charAt(b & 0x0F));
				result += hex + "";
			}
			return result;
		}
	    
}
