package servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tool.Download;

/**
 * Servlet implementation class VideoTrans
 */
public class VideoTrans extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public VideoTrans() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String path = request.getParameter("path");
		System.out.println(request.getParameterMap().size());
		Enumeration<String> heads = request.getHeaderNames();
		while(heads.hasMoreElements()){
			String key = heads.nextElement();
			System.out.println(key + ":" + request.getHeader(key));
		}
		Iterator<String> iterator = request.getParameterMap().keySet().iterator();
		while(iterator.hasNext()){
			String key = iterator.next();
			System.out.println(key + ":" + request.getParameter(key));
		}
		System.out.println(path);
		if(null == path){
			path = "E:/FFOutput/Survival Shooter Tutorial - 1 of 10 .mp4";
		}
		File file;
		file = new File(path);
		Thread thread = new Thread(new PrintAllInfo(request));
		thread.start();
		Download.share().download(new FileInputStream(file),
				response.getOutputStream());
		thread.interrupt();
	}
	class PrintAllInfo implements Runnable{
		HttpServletRequest req;
		public PrintAllInfo(HttpServletRequest req) {
			this.req = req;
		}
		@Override
		public void run() {
			while(true){
				try {
					Enumeration<String> heads = req.getHeaderNames();
					while(heads.hasMoreElements()){
						String key = heads.nextElement();
						System.out.println(key + ":" + req.getHeader(key));
					}
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	class Preq implements Runnable{
		public Preq(InputStream in) {
			this.in = in;
		}
		InputStream in;
		@Override
		public void run() {
			int count;
			byte[] b = new byte[1024];
			try {
				count = in.read(b);
				while (count > 0) {
					count = in.read(b);
					System.out.println("req:" + new String(b, 0, count));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}
