package test;

import java.io.File;

public class FilesAndDirs {

	public static void main(String[] args) {
		File f = new File("e:/");
		System.out.println(f.getPath());
		String[] paths = f.list();
		for(String path : paths){
			System.out.println(path);
		}
	}
}
