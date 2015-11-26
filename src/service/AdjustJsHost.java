package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;

public class AdjustJsHost {

	public void adjust(File jsFile) throws IOException{
		FileInputStream inputStream = new FileInputStream(jsFile);
		byte[] b = new byte[(int) jsFile.length()];
		inputStream.read(b);
		inputStream.close();
		String fileContent = new String(b,0,b.length,"utf-8").replace("${serverIp}", InetAddress.getLocalHost().getHostAddress());
		FileOutputStream outputStream = new FileOutputStream(jsFile);
		outputStream.write(fileContent.getBytes("utf-8"));
		outputStream.close();
	}
}
