package service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class EchoServer {

	public static void main(String[] args) throws Exception {
		String addr = InetAddress.getLocalHost().getHostAddress();
		System.out.println(addr);
		new EchoServer().Start();
	}
	void doWord(ServerSocket serverSocket){
		while(true){
			Socket client = null;
			try {
				client = serverSocket.accept();
				client.setSoTimeout(200);
				
				byte[] b = new byte[64];
				int count = client.getInputStream().read(b);
				String msg = new String(b,0,count,"utf-8");
				
				if(msg.equals("mars-123123-server?")){
					client.getOutputStream().write("you got video server".getBytes("utf-8"));
					System.out.println(client.getInetAddress().getHostAddress());
					System.out.println(client.getRemoteSocketAddress());
					System.out.println(client.getLocalAddress());
				}
				client.close();
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally{
				try {
					if(null != client){
						client.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public void Start(){
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(10080, 16, InetAddress.getLocalHost());
			System.out.println("server start");
			doWord(serverSocket);
		} catch (UnknownHostException e2) {
			e2.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		try {
			if(null != serverSocket){
				serverSocket.close();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
