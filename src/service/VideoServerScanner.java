package service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

public class VideoServerScanner {
	public static void main(String[] args) {
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();
			short prefixLen = NetworkInterface.getByInetAddress(inetAddress)
					.getInterfaceAddresses().get(0).getNetworkPrefixLength();
			
			InetAddress minAddr = InetAddress
					.getByAddress(getMinAddrByteCanUse(inetAddress, prefixLen));
			System.out.println(minAddr.getHostAddress());

			InetAddress maxAddr = InetAddress.getByAddress(getMaxAddrByteCanUse(inetAddress, prefixLen));
			System.out.println(maxAddr.getHostAddress());
			
			new VideoServerScanner().scan();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} 
	}

	static void testBinaryApi() {
		System.out.println(Integer.MAX_VALUE);
		System.out.println(Integer.toBinaryString(Integer.MAX_VALUE).length());
		System.out.println(Integer.toBinaryString(Integer.MAX_VALUE));

		System.out.println(Integer.MAX_VALUE + Integer.MAX_VALUE);
		System.out.println(Integer.toBinaryString(
				Integer.MAX_VALUE + Integer.MAX_VALUE).length());
		int v = Integer.MAX_VALUE + Integer.MAX_VALUE;
		System.out.println(Integer.toBinaryString(v));
	}

	public static void useSample() {
		List<String> ips;
		try {
			ips = new VideoServerScanner().scan();
			for (String ip : ips) {
				System.out.println(ip);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public static InetAddress[] getAddressByPlatform() throws UnknownHostException, SocketException{
		String osName = System.getProperty("os.name");
		if(osName.matches(".*windows.*") || osName.matches(".*Windows.*")){
			return new InetAddress[]{InetAddress.getLocalHost()};
		}else if(osName.matches(".*Linux.*")){
			Enumeration<InetAddress> t = NetworkInterface.getNetworkInterfaces().nextElement().getInetAddresses();
			return new InetAddress[]{t.nextElement(),t.nextElement()};
		}
		return null;
	}
	
	public List<String> scan() throws UnknownHostException, SocketException {
		List<String> ips = new ArrayList<String>();
		InetAddress localHost = InetAddress.getLocalHost();
		NetworkInterface networkInterface = NetworkInterface
				.getByInetAddress(localHost);
		InterfaceAddress interfaceAddress = networkInterface
				.getInterfaceAddresses().get(0);
		short prefixLen = interfaceAddress.getNetworkPrefixLength();
		int minIp = toIntIp(getMinAddrByteCanUse(localHost, prefixLen));
		int maxIp = toIntIp(getMaxAddrByteCanUse(localHost, prefixLen));
		int curIp = minIp;
		while(curIp <= maxIp){
			Socket socket = new Socket();
			InetAddress inetAddress = InetAddress.getByAddress(toByteIp(curIp));
			System.out.println("cur host: " + inetAddress.getHostAddress());
			try {
				socket.connect(new InetSocketAddress(inetAddress, 10080), 50);
				
				socket.setSoTimeout(200);
				
				socket.getOutputStream().write("mars-123123-server?".getBytes("utf-8"));
				
				byte[] b = new byte[64];
				int count = socket.getInputStream().read(b);
				String msg = new String(b,0,count,"utf-8");
				
				if(count > 0 && "you got video server".equals(msg)){
					ips.add(inetAddress.getHostAddress());
				}else{
					System.out.println(inetAddress.getHostAddress() + " is not video server");
				}
			} catch (IOException e) {
				System.out.println(inetAddress.getHostAddress() + " can not be connect");
			} finally{
				try {
					if(null != socket){
						socket.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			curIp++;
		}
		System.out.println(ips.size());
		return ips;
	}

	public static byte[] getMinAddrByteCanUse(InetAddress oneAddr,
			short prefixLen) {
		byte[] addrByte = oneAddr.getAddress();
		int ipInt = toIntIp(addrByte);
		char[] ipChar = new char[32];
		Arrays.fill(ipChar, '0');
		int i = 0;
		while (i < prefixLen) {
			ipChar[i] = '1';
			i++;
		}
		int min = (int) Long.parseLong(new String(ipChar), 2) & ipInt;
		return toByteIp(min + 2);
	}

	public static byte[] getMaxAddrByteCanUse(InetAddress oneAddr,
			short prefixLen) {
		byte[] addrByte = oneAddr.getAddress();
		int ipInt = toIntIp(addrByte);
		char[] ipChar = new char[32];
		Arrays.fill(ipChar, '1');
		int i = 0;
		while (i < prefixLen) {
			ipChar[i] = '0';
			i++;
		}
		int max = (int) Long.parseLong(new String(ipChar), 2) | ipInt;
		return toByteIp(max - 1);
	}

	public static byte[] toByteIp(int ip) {
		return new byte[] { (byte) (ip >> 24), (byte) (ip >> 16),
				(byte) (ip >> 8), (byte) ip };
	}

	public static int toIntIp(byte[] addrByte) {
		return addrByte[0] << 24 & 0xff000000 | addrByte[1] << 16 & 0x00ff0000
				| addrByte[2] << 8 & 0x0000ff00 | addrByte[3] & 0x000000ff;
	}

	static void testTrans() {
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();
			System.out.println(inetAddress.getHostAddress());
			int intAddr = toIntIp(inetAddress.getAddress());
			System.out.println(Integer.toBinaryString(intAddr));
			byte[] byteAddr = toByteIp(intAddr);
			System.out.println(InetAddress.getByAddress(byteAddr)
					.getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

	}
}
