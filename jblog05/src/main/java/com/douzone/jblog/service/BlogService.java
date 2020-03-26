package com.douzone.jblog.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.douzone.jblog.repository.BlogRepository;
import com.douzone.jblog.vo.BlogVo;
import com.douzone.jblog.vo.UserVo;

@Service
public class BlogService {
	private static final String SAVE_PATH = "/jblog-uploads"; 
	private static final String URL = "/assets/image";
	
	@Autowired
	private BlogRepository blogRepository;

	public void insert(UserVo vo) {
		blogRepository.insert(vo);
		
	}

	public BlogVo getBlogInfo(String id) {
		return blogRepository.selectById(id);
	}
	

	public void updateBlogInfo(BlogVo vo) {
		blogRepository.updateInfo(vo);
	}
	
	public String restore(MultipartFile multipartFile) {
		String url = "";
		
		try {
			if(multipartFile.isEmpty()) {
				return url;
			}
			
			String originFilename = multipartFile.getOriginalFilename();
			
			String extName = originFilename.substring(originFilename.lastIndexOf(".") + 1);
			
			String saveFilenmae = generateSaveFilename(extName);
			long fileSize = multipartFile.getSize();
			
			System.out.println("##### file " + originFilename);
			System.out.println("##### file " + saveFilenmae);
			System.out.println("##### size " + fileSize);
			
			byte[] fileData = multipartFile.getBytes();
			OutputStream os = new FileOutputStream(SAVE_PATH + "/" + saveFilenmae);
			os.write(fileData);
			os.close();
			
			url = URL + "/" + saveFilenmae; 
			
			
		} catch(IOException ex) {
			throw new RuntimeException("file upload error:" + ex);
		}
		
		return url;
		
	}

	private String generateSaveFilename(String extName) {
		String filename = "";
		Calendar calendar = Calendar.getInstance();
		filename += calendar.get(Calendar.YEAR);
		filename += calendar.get(Calendar.MONTH);
		filename += calendar.get(Calendar.DATE);
		filename += calendar.get(Calendar.HOUR);
		filename += calendar.get(Calendar.MINUTE);
		filename += calendar.get(Calendar.SECOND);
		filename += calendar.get(Calendar.MILLISECOND);
		filename += ("." + extName); // 기존 확장자
		
		return filename;
	}


}
