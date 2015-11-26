package service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import listener.GlobalConfig;
import model.SimpleFile;
import model.Video;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import tool.VideoFileFilter;

@Service("FileManagerService")
@Scope("singleton")
public class FileManagerService {
	public static String key_root = "root";
	List<SimpleFile> absDirs;
	@Resource(name = "VideoFileFilter")
	VideoFileFilter filter;

	@SuppressWarnings("unchecked")
	public FileManagerService() {
		loadAbsDirs((List<String>) GlobalConfig.getShare().getConfig(
				GlobalConfig.key_absdirs));
	}

	public void loadAbsDirs(List<String> dirs) {
		absDirs = toSimpleFiles(dirs);
	}

	private List<SimpleFile> toSimpleFiles(List<String> dirs) {
		List<SimpleFile> simpleFiles = new ArrayList<SimpleFile>();
		for (String dir : dirs) {
			SimpleFile simpleFile = new SimpleFile();
			if(dir.matches(".+:/")){
				simpleFile.setName(dir.substring(0, dir.indexOf('/')));
			}else{
				simpleFile.setName(dir.substring(dir.lastIndexOf('/') + 1));
			}
			simpleFile.setPath(dir);
			simpleFile.setType(SimpleFile.type_dir);
			simpleFiles.add(simpleFile);
		}
		return simpleFiles;
	}

	public List<SimpleFile> getFiles(String dir) {
		File[] children = new File(dir).listFiles();
		if(null != children){
			return toSimpleFiles(new File(dir).listFiles());
		}
		return new ArrayList<SimpleFile>(0);
	}

	public List<SimpleFile> toSimpleFiles(File[] files) {
		List<SimpleFile> videos = new ArrayList<SimpleFile>();
		for (File file : files) {
			videos.add(toSimpleFile(file));
		}
		return videos;
	}

	public SimpleFile toSimpleFile(File file) {
		SimpleFile simpleFile = null;
		if(filter.accept(file)){
			Video video = new Video();
			video.setName(file.getName());
			video.setPath(file.getPath().replace('\\', '/'));
			video.setType(SimpleFile.type_video);
			video.setSize(file.length());
			video.setThum(getThumb(file));
			simpleFile = video;
		}else{
			simpleFile = new SimpleFile();
			simpleFile.setName(file.getName());
			simpleFile.setPath(file.getPath().replace('\\', '/'));
			if(file.isDirectory()){
				simpleFile.setType(SimpleFile.type_dir);
			}else{
				simpleFile.setType(SimpleFile.type_noamal_file);
				simpleFile.setSize(file.length());
			}
		}
		return simpleFile;
	}

	public String getThumb(File file) {
		return "";
	}

	public List<SimpleFile> getAbsDirs() {
		return absDirs;
	}
}
