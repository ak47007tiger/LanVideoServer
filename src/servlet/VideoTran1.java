package servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.ContentTypeProvider;

/**
 * Servlet implementation class VideoTran1
 */
public class VideoTran1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VideoTran1() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = request.getParameter("path");
		if(null == path){
			path = "E:/FFOutput/Survival Shooter Tutorial - 1 of 10 .mp4";
		}
		File file = new File(path);
		String suffix = file.getName().substring(file.getName().lastIndexOf('.') + 1);
		String rang = request.getHeader("range");
		if(null == rang){
			response.getOutputStream().write("no rang".getBytes());
			return;
		}
		String start = rang.split("=")[1].split("-")[0];
		
		FileInputStream fin = new FileInputStream(file);
		fin.skip(Long.parseLong(start));
		
		byte[] b = new byte[1024 * 1014 * 2];
		int count = fin.read(b);
		fin.close();
		
		long end = Long.parseLong(start) + count;
		long total = file.length();
		response.setStatus(206);
		response.addHeader("Content-Length", total + "");
		response.addHeader("Content-Type", ContentTypeProvider.getProvider().get(suffix));
		response.addHeader("Content-Range", "bytes " + start + "-" + end + "/" + total);
		
		response.getOutputStream().write(b, 0, count);
	}
	
	public static void main(String[] args) {
		String[] rangs = "bytes=0-".split("=")[1].split("-");
		System.out.println(rangs);
	}

}
