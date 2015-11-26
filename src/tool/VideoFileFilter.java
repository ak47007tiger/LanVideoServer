package tool;

import java.io.File;
import java.io.FileFilter;
import java.util.HashSet;

import listener.GlobalConfig;

import org.springframework.stereotype.Service;

@Service("VideoFileFilter")
public class VideoFileFilter implements FileFilter{
	HashSet<String> suffixes;
	
	@SuppressWarnings("unchecked")
	public VideoFileFilter() {
		suffixes = (HashSet<String>) GlobalConfig.getShare().getConfig(GlobalConfig.key_videotypes);
	}

	@Override
	public boolean accept(File pathname) {
		return checkSuffix(pathname);
	}

	public boolean checkSuffix(File pathname) {
		String name = pathname.getName();
		return suffixes.contains(name.substring(name.lastIndexOf('.') + 1));
	}
	
}
