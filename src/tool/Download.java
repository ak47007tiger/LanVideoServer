package tool;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Download {
	public static Download download;
	static{
		download = new Download();
	}
	public static Download share(){
		return download;
	}
	ThreadPoolExecutor pool;
	private Download() {
		pool = new ThreadPoolExecutor(2, 4, 3, TimeUnit.SECONDS,
				new ArrayBlockingQueue<Runnable>(3),
				new ThreadPoolExecutor.DiscardOldestPolicy());
	}
	public void download(InputStream in, OutputStream out) throws IOException{
		new DownloadTask(in, out).run();
	}
	class DownloadTask implements Runnable{
		InputStream in;
		OutputStream out;
		public DownloadTask(InputStream in, OutputStream out) {
			this.in = in;
			this.out = out;
		}

		@Override
		public void run() {
			byte[] b = new byte[1024 * 1024];
			int count;
			try {
				while (-1 != (count = in.read(b))) {
					out.write(b, 0, count);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					close(in);
					close(out);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	public void close(Reader reader) throws IOException {
		if (null != reader)
			reader.close();
	}

	public void close(InputStream in) throws IOException {
		if (null != in)
			in.close();
	}

	public void close(OutputStream out) throws IOException {
		if (null != out)
			out.close();
	}
}
