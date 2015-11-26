package listener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import service.AdjustJsHost;
import service.EchoServer;

/**
 * Application Lifecycle Listener implementation class ServerStart
 *
 */
public class GlobalConfig implements ServletContextListener {
	public static String key_absdirs = "absdirs";
	public static String key_videotypes = "videotypes";
	public static String key_content_type_map = "key_content_type_map";
	Map<String, Object> configs = new HashMap<String, Object>();
	Thread echoThread;

	/**
	 * Default constructor.
	 */
	public GlobalConfig() {
		serverStart = this;
	}

	private static GlobalConfig serverStart;

	public static GlobalConfig getShare() {
		return serverStart;
	}

	public Object getConfig(String key) {
		return configs.get(key);
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent arg0) {

		File configDir = new File(arg0.getServletContext().getRealPath(""),
				"WEB-INF/configdir");
		initAbsDirs(configDir);
		initVideoType(configDir);
		initContentTypes(configDir);
		echoThread = new Thread(new Runnable() {
			@Override
			public void run() {
				new EchoServer().Start();
			}
		});
		echoThread.start();
		File jsFile = new File(arg0.getServletContext().getRealPath(""),"mobile/videolist.js"); 
		try {
			new AdjustJsHost().adjust(jsFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initVideoType(File configDir) {
		File videotype = new File(configDir, "videotype");
		Properties properties = new Properties();
		Set<String> videotypes = new HashSet<String>();
		try {
			properties.load(new FileInputStream(videotype));
			Enumeration<Object> enumeration = properties.keys();
			while (enumeration.hasMoreElements()) {
				Object key = enumeration.nextElement();
				String val = (String) properties.get(key);
				videotypes.add(val);
				videotypes.add(val.toUpperCase());
			}
			configs.put(key_videotypes, videotypes);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
		echoThread.interrupt();
	}
	void initContentTypes(File configDir){
		HashMap<String, String> typesMap = new HashMap<String, String>();
		File videotype = new File(configDir, "videotype");
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(videotype));
			Enumeration<Object> enumeration = properties.keys();
			while (enumeration.hasMoreElements()) {
				String key = (String) enumeration.nextElement();
				String val = (String) properties.get(key);
				typesMap.put(key, val);
			}
			configs.put(key_content_type_map, typesMap);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	void initAbsDirs(File configDir) {
		File absDirsFile = new File(configDir, "absdirs");
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(absDirsFile));
			Enumeration<Object> enumeration = properties.keys();
			List<String> absdirs = new ArrayList<String>();
			while (enumeration.hasMoreElements()) {
				Object key = enumeration.nextElement();
				absdirs.add((String) properties.get(key));
			}
			configs.put(key_absdirs, absdirs);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
