package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import model.SimpleFile;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import service.FileManagerService;
import tool.Download;

@Controller
@RequestMapping(value = "/file")
public class VideoShowController {

	@Resource(name = "FileManagerService")
	FileManagerService service;

	@RequestMapping("/files")
	public Model getFiles(
			@RequestParam(value = "dir", required = false, defaultValue = "_abs") String dir,
			Model model) {
		List<SimpleFile> videos = null;
		if ("_abs".equals(dir)) {
			videos = service.getAbsDirs();
		} else {
			videos = service.getFiles(dir);
		}
		if (null == videos) {
			videos = new ArrayList<SimpleFile>(0);
		}
		model.addAttribute("data", videos);
		model.addAttribute("code", 1);
		return model;
	}

	@RequestMapping("/videodetail")
	public Model getFile(
			@RequestParam(value = "path", required = true, defaultValue = "_empty") String dir,
			Model model) {

		return model;
	}

	@RequestMapping("/download")
	public ResponseEntity<byte[]> download() throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		File file = getDictionaryFile();
		headers.setContentDispositionFormData("attachment", file.getName());
		return new ResponseEntity<byte[]>(readFileToByteArray(file), headers,
				HttpStatus.CREATED);
	}

	@RequestMapping("/play")
	public void play(
			HttpServletResponse response,
			@RequestParam(value = "path", required = true, defaultValue = "E:/suvivalshooter/Survival Shooter Tutorial - 1 of 10 .mp4") String path) {
		response.setCharacterEncoding("utf-8");
		File file = new File(path);
		System.out.println("some one want get video");
		try {
			InputStream in = new FileInputStream(file);
			response.setContentType("multipart/form-data");
			response.setHeader("Content-Disposition", "attachment;fileName="
					+ file.getName());
			OutputStream out = response.getOutputStream();
			Download.share().download(in, out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			try {
				response.setContentType("multipart/text");
				response.getOutputStream().write("FileNotFound".getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private File getDictionaryFile() {
		return new File("E:/suvivalshooter/Survival Shooter Tutorial - 1 of 10 .mp4");
	}

	public byte[] readFileToByteArray(File file) {
		byte[] buf = null;
		FileInputStream in = null;
		try {
			buf = new byte[(int) file.length()];
			in = new FileInputStream(file);
			in.read(buf);
			return buf;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (null != in) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
